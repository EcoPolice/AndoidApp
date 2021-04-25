package com.yoloroy.disastersMonitor

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.yoloroy.disastersMonitor.web.apiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.await
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


// @returns link to img on server
suspend fun putImage(data: Uri, contentResolver: ContentResolver): String? {
    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data)

    val size: Int = bitmap.rowBytes * bitmap.height
    val byteBuffer: ByteBuffer = ByteBuffer.allocate(size)
    bitmap.copyPixelsToBuffer(byteBuffer)
    val byteArray = byteBuffer.array()
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes: ByteArray = baos.toByteArray()
    //val encodedImage: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)

    val requestFile: RequestBody = RequestBody.create(
        MediaType.parse("*/*"),
        imageBytes
    )

    val body = MultipartBody.Part.createFormData(
        byteArray.hashCode().toString(),
        data.lastPathSegment!!,
        requestFile
    )

    try {
        return apiClient.uploadImage(body).await().toString()
    } catch (e: Exception) {
        Log.e("imger", e.toString())
        e.printStackTrace() // TODO: add on error
    }

    return null
}
