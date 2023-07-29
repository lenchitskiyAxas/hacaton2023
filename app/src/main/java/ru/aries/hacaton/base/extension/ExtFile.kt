package ru.aries.hacaton.base.extension

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.aries.hacaton.BuildConfig
import ru.aries.hacaton.R
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.util.logD
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val DEFAULT_CHANNEL_ID_FCM_DOWNLOAD = "fcm_default_channel_download"
fun patchFromPictures(fileName: String): File {
    val directory = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        TextApp.FOLDER_NAME)
    directory.mkdirs()
    return File(directory, fileName)
}

fun getFileMimeType(fileName: String): String? {
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileName)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
}

@Throws(IOException::class)
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = Date().toTimeStringForeFile()
    val storageDir = cacheDir
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        deleteOnExit()
    }
}

private fun Date.toTimeStringForeFile(): String =
    SimpleDateFormat("yyyyMMdd_HHmmss", LocaleRu).format(this)

private val LocaleRu = Locale("ru", "RU")

fun Context.makeUri() = this.createImageFile().getUriForFile(this)

fun File.getUriForFile(context: Context): Uri {
    return FileProvider.getUriForFile(context,
        "${context.applicationInfo.packageName}.fileprovider",
        this)
}

fun downloadFromUrlExt(context: Context, link: String) {

    val nameFileDownload = link.substringAfterLast("/")
    val fillNamePatch = patchFromPictures(nameFileDownload)

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(Uri.parse(fillNamePatch.absolutePath), getFileMimeType(nameFileDownload))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    val pendingIntent = PendingIntent.getActivity(context, 101, intent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

    val builder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID_FCM_DOWNLOAD)
            .setSmallIcon(R.raw.ic_download)
            .setContentTitle(nameFileDownload)
            .setAutoCancel(true)
            .setVibrate(null)
            .setSound(null)
            .setOnlyAlertOnce(false)
            .setContentIntent(pendingIntent)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notificationChannel = NotificationChannel(DEFAULT_CHANNEL_ID_FCM_DOWNLOAD,
        BuildConfig.APPLICATION_ID,
        NotificationManager.IMPORTANCE_HIGH)

    notificationManager.createNotificationChannel(notificationChannel)
    CoroutineScope(Dispatchers.Main).launch {
        withContext(Dispatchers.IO){
            try {
                val connection = URL(link).openConnection() as? HttpURLConnection
                connection?.let {
                    it.connect()
                    val inputStream = it.inputStream
                    val fileOutputStream = FileOutputStream(fillNamePatch)
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    var totalBytesRead: Long = 0
                    val totalFileSize = connection.contentLength.toLong()
                    var progress: Int

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead.toLong()
                        progress = ((totalBytesRead * 100) / totalFileSize).toInt()
                        builder.setProgress(100, progress, false)
                        notificationManager.notify(101, builder.build())
                    }

                    fileOutputStream.flush()
                    fileOutputStream.close()
                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        builder.setProgress(0, 0, false)
        notificationManager.notify(101, builder.build())
    }
}


fun getPhotoLocationFromUri(context: Context, photoUri: Uri) {
    try {
        val inputStream = context.contentResolver.openInputStream(photoUri)
        inputStream?.use {
            val exifInterface = ExifInterface(it)

            val latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            val latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
            val longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            val longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
            val TAG_APERTURE = exifInterface.getAttribute(ExifInterface.TAG_APERTURE)
            val TAG_APERTURE_VALUE = exifInterface.getAttribute(ExifInterface.TAG_APERTURE_VALUE)
            val TAG_ARTIST = exifInterface.getAttribute(ExifInterface.TAG_ARTIST)
            val TAG_BITS_PER_SAMPLE = exifInterface.getAttribute(ExifInterface.TAG_BITS_PER_SAMPLE)
            val TAG_BRIGHTNESS_VALUE = exifInterface.getAttribute(ExifInterface.TAG_BRIGHTNESS_VALUE)
            val TAG_CFA_PATTERN = exifInterface.getAttribute(ExifInterface.TAG_CFA_PATTERN)
            val TAG_COLOR_SPACE = exifInterface.getAttribute(ExifInterface.TAG_COLOR_SPACE)
            val TAG_COMPONENTS_CONFIGURATION = exifInterface.getAttribute(ExifInterface.TAG_COMPONENTS_CONFIGURATION)
            val TAG_COMPRESSED_BITS_PER_PIXEL = exifInterface.getAttribute(ExifInterface.TAG_COMPRESSED_BITS_PER_PIXEL)
            val TAG_COMPRESSION = exifInterface.getAttribute(ExifInterface.TAG_COMPRESSION)
            val TAG_CONTRAST = exifInterface.getAttribute(ExifInterface.TAG_CONTRAST)
            val TAG_COPYRIGHT = exifInterface.getAttribute(ExifInterface.TAG_COPYRIGHT)
            val TAG_CUSTOM_RENDERED = exifInterface.getAttribute(ExifInterface.TAG_CUSTOM_RENDERED)
            val TAG_DATETIME = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)
            val TAG_DATETIME_DIGITIZED = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
            val TAG_DATETIME_ORIGINAL = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
            val TAG_DEFAULT_CROP_SIZE = exifInterface.getAttribute(ExifInterface.TAG_DEFAULT_CROP_SIZE)
            val TAG_DEVICE_SETTING_DESCRIPTION = exifInterface.getAttribute(ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION)
            val TAG_DIGITAL_ZOOM_RATIO = exifInterface.getAttribute(ExifInterface.TAG_DIGITAL_ZOOM_RATIO)


            logD("jhjljkljjlkj Latitude1: $latitude")
            logD("jhjljkljjlkj Longitude1: $longitude")
            logD("jhjljkljjlkj Latitude2: $latitudeRef")
            logD("jhjljkljjlkj Longitude2: $longitudeRef")
            logD("jhjljkljjlkj TAG_APERTURE: $TAG_APERTURE")
            logD("jhjljkljjlkj TAG_APERTURE_VALUE: $TAG_APERTURE_VALUE")
            logD("jhjljkljjlkj TAG_ARTIST: $TAG_ARTIST")
            logD("jhjljkljjlkj TAG_BITS_PER_SAMPLE: $TAG_BITS_PER_SAMPLE")
            logD("jhjljkljjlkj TAG_BRIGHTNESS_VALUE: $TAG_BRIGHTNESS_VALUE")
            logD("jhjljkljjlkj TAG_CFA_PATTERN: $TAG_CFA_PATTERN")
            logD("jhjljkljjlkj TAG_COLOR_SPACE: $TAG_COLOR_SPACE")
            logD("jhjljkljjlkj TAG_COMPONENTS_CONFIGURATION: $TAG_COMPONENTS_CONFIGURATION")
            logD("jhjljkljjlkj TAG_COMPRESSED_BITS_PER_PIXEL: $TAG_COMPRESSED_BITS_PER_PIXEL")
            logD("jhjljkljjlkj TAG_COMPRESSION: $TAG_COMPRESSION")
            logD("jhjljkljjlkj TAG_CONTRAST: $TAG_CONTRAST")
            logD("jhjljkljjlkj TAG_COPYRIGHT: $TAG_COPYRIGHT")
            logD("jhjljkljjlkj TAG_CUSTOM_RENDERED: $TAG_CUSTOM_RENDERED")
            logD("jhjljkljjlkj TAG_DATETIME: $TAG_DATETIME")
            logD("jhjljkljjlkj TAG_DATETIME_DIGITIZED: $TAG_DATETIME_DIGITIZED")
            logD("jhjljkljjlkj TAG_DATETIME_ORIGINAL: $TAG_DATETIME_ORIGINAL")
            logD("jhjljkljjlkj TAG_DEFAULT_CROP_SIZE: $TAG_DEFAULT_CROP_SIZE")
            logD("jhjljkljjlkj TAG_DEVICE_SETTING_DESCRIPTION: $TAG_DEVICE_SETTING_DESCRIPTION")
            logD("jhjljkljjlkj TAG_DIGITAL_ZOOM_RATIO: $TAG_DIGITAL_ZOOM_RATIO")
            if (latitude != null && longitude != null && latitudeRef != null && longitudeRef != null) {
                val lat = convertToDegree(latitude)
                val lon = convertToDegree(longitude)

                val latResult = if (latitudeRef == "N") lat else -lat
                val lonResult = if (longitudeRef == "E") lon else -lon

                logD("Latitude: $latResult")
                logD("Longitude: $lonResult")
            } else {
                println("Location not available.")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun convertToDegree(coordinate: String): Float {
    val parts = coordinate.split(",").map { it.trim() }
    val degrees = parts[0].toDouble()
    val minutes = parts[1].toDouble()
    val seconds = parts[2].toDouble()

    return (degrees + minutes / 60 + seconds / 3600).toFloat()
}
