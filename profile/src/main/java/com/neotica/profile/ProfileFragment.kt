package com.neotica.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.neotica.cinepoly.databinding.FragmentProfileBinding
import com.neotica.profile.workers.BlurWorker
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import com.neotica.cinepoly.ui.LauncherActivity
import java.io.File
import java.io.IOException
import com.neotica.cinepoly.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: ProfileViewModel by viewModel()
    private val blurModel: BlurViewModel by viewModel()
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        loadKoinModules(profileModule)

        initial(binding)
        update(binding)
        logout(binding)
        checkCameraPermission()
        val savedUri = BlurWorker.getSavedOutputUri(requireContext())
        if (savedUri != null) {
            loadImageView(savedUri)
        }
        Log.d("neotica-work-prof", savedUri.toString())
    }

    private fun loadImageView(outputUri: String) {
        // Use Glide or another image-loading library to load the image into your ImageView
        Glide.with(requireContext())
            .load(outputUri)
            .into(binding.ivProfile)
    }

    private fun initial(binding: FragmentProfileBinding) {
        with(binding) {
            val userId = viewModel.token.value
            if (userId != "") {
                viewModel.getUsername(userId).observe(viewLifecycleOwner) {
                    etUsername.hint = resources.getString(R.string.hint_username) + ": " + it.username
                    etFullname.hint = resources.getString(R.string.text_full_name) + ": " + it.fullname
                    etDob.hint = resources.getString(R.string.text_dob) + ": " + it.dob
                    etAddress.hint = resources.getString(R.string.text_address) + ": " + it.address
                }
            }
        }
    }

    private fun update(binding: FragmentProfileBinding) {
        val token = viewModel.token.value

        with(binding) {
            ivEditProfile.setOnClickListener { startTakePhoto() }
            btnUpdate.setOnClickListener {
                lifecycleScope.launch {
                    val fullname = etFullname.text.toString().ifEmpty { null }
                    val dob = etDob.text.toString().ifEmpty { null }
                    val address = etAddress.text.toString().ifEmpty { null }
                    viewModel.getUsername(token).observe(viewLifecycleOwner) {
                        val username = etUsername.text.toString().ifEmpty { it.username }
                        if (username != null) {
                            viewModel.updateUser(
                                userId = it.userId,
                                username = username,
                                email = it.email.toString(),
                                password = it.password.toString(),
                                fullname =  fullname,
                                dob = dob,
                                address = address,
                                token = token
                            )
                        }
                    }.also {
                        Toast.makeText(context, "Data updated.", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)

        File.createTempFile("temp_", ".jpg", requireContext().applicationContext.cacheDir).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.neotica.profile",
                it
            )
            startBlurOperation(photoURI)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun File.writeBitmapToFile(bitmap: Bitmap) {
        val fos = outputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            try {
                val bitmap = handleSamplingAndRotationBitMap(requireContext(), Uri.fromFile(myFile))
                binding.ivProfile.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun checkCameraPermission() {
        if (viewModel.checkCameraPermission(requireContext())) {
            // Camera permission already granted
            Toast.makeText(context, "permission granted", Toast.LENGTH_SHORT).show()
        } else {
            // Request camera permission
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, proceed with camera-related tasks
                Toast.makeText(context, "permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                // You might want to inform the user about the need for the camera permission
                // and ask them to grant it in the app settings.
            }
        }

    private fun startBlurOperation(uri: Uri) {
        blurModel.setInputImageUri(uri)
        blurModel.applyBlur(5)
    }

    private fun logout(binding: FragmentProfileBinding) {
        val gotoLauncher = Intent(context, LauncherActivity::class.java)
        binding.apply {
            btnLogout.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.logout()
                    Toast.makeText(context, "Logout success.", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    startActivity(gotoLauncher)
                }
            }
        }
    }
}