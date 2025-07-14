package com.korop.yaroslavhorach.common.utill

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

fun buildDialIntent(phoneNumber: String): Intent =
    Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:$phoneNumber"))

fun buildSMSIntent(body: String): Intent =
    Intent(Intent.ACTION_VIEW).apply {
        putExtra("sms_body", body)
        type = "vnd.android-dir/mms-sms"
    }

fun buildEmailIntent(email: String): Intent =
    Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
        putExtra(Intent.EXTRA_EMAIL, email)
    }

fun buildSendIntent(text: String): Intent {
    val intent = Intent(Intent.ACTION_SEND)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_TEXT, text)
    return Intent.createChooser(intent, "")
}

fun buildOpenUrlIntent(url: String): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse(url))
}

fun buildGoogleMapDirectionIntent(latitude: Double, longitude: Double): Intent {
    val direction = "http://maps.google.com/maps?daddr=$latitude,$longitude"
    return Intent(Intent.ACTION_VIEW, Uri.parse(direction))
}

fun buildMultipleImagesChooserIntent(): Intent {
    return Intent().apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        action = Intent.ACTION_GET_CONTENT
    }
}

fun buildAppSettingsIntent(context: Context?): Intent {
    return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context?.packageName, null)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun buildAppLocaleSettingsIntent(context: Context?): Intent {
    return Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
        data = Uri.fromParts("package", context?.packageName, null)
    }
}

fun buildNotificationSettingsIntent(context: Context?): Intent {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context?.packageName)
        }
    } else {
        buildAppSettingsIntent(context)
    }
}

fun buildSendAppToBackground(): Intent {
    return Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }
}

fun buildPdfFileIntent(url: String): Intent {
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        setDataAndType(Uri.parse(url), "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    return Intent.createChooser(intent, null)
}

fun buildRateAppIntent(): Intent {
    val packageName = "com.korop.yaroslavhorach.lingoFlow"

    return Intent(
        Intent.ACTION_VIEW,
        Uri.parse("market://details?id=$packageName")
    ).apply {
        addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
    }
}

fun buildRateAppWebIntent(): Intent {
    val packageName = "com.korop.yaroslavhorach.lingoFlow"

    return Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
    )
}