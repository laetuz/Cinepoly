/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.neotica.profile.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.neotica.profile.KEY_IMAGE_URI

private const val TAG = "BlurWorker"
private const val OUTPUT_URI_KEY = "output_uri"
class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)

        // This is an utility function added to emulate slower work.
        sleep()

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)))

            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)
            saveOutputUri(outputUri.toString())

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            Log.d("neotica-work", outputUri.toString())

            Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }

    private fun saveOutputUri(outputUri: String) {
        val sharedPreferences =
            applicationContext.getSharedPreferences("your_shared_preference_name", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(OUTPUT_URI_KEY, outputUri)
        editor.apply()
    }

    companion object {
        fun getSavedOutputUri(context: Context): String? {
            val sharedPreferences =
                context.getSharedPreferences("your_shared_preference_name", Context.MODE_PRIVATE)
            return sharedPreferences.getString(OUTPUT_URI_KEY, null)
        }
    }
}
