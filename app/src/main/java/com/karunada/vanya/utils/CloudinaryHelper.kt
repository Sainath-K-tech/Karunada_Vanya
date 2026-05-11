package com.karunada.vanya.utils

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

object CloudinaryHelper {

    fun uploadMedia(
        fileUri: Uri,
        folder: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        MediaManager.get().upload(fileUri)
            .option("folder", folder)
            .option("resource_type", "auto") // Crucial for video uploads
            .unsigned("karunada_unsigned")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val url = resultData?.get("secure_url") as? String
                    if (url != null) {
                        onSuccess(url)
                    } else {
                        onError("URL not found in response")
                    }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    onError(error?.description ?: "Unknown upload error")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    onError("Upload rescheduled")
                }
            })
            .dispatch()
    }
}