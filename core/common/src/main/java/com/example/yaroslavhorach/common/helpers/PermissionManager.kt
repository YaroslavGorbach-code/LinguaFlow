package com.example.yaroslavhorach.common.helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.yaroslavhorach.common.utill.buildAppSettingsIntent
import com.example.yaroslavhorach.common.utill.buildNotificationSettingsIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor(@ApplicationContext val context: Context) {

    private val askedPermission: MutableList<String> = emptyList<String>().toMutableList()

    @Composable
    fun AskPermission(permission: String, onResult: (Boolean) -> Unit) {

        val context = LocalContext.current

        when (permission) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    onResult(true)
                    return
                }
            }

            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    onResult(true)
                    return
                }
            }
        }

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission(), onResult)

        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                onResult(true)
            }

            askedPermission.contains(permission).not() -> {
                SideEffect {
                    launcher.launch(permission)
                }
            }

            (context as Activity).shouldShowRequestPermissionRationale(permission) -> {
                SideEffect {
                    launcher.launch(permission)
                }
            }

            else -> {
                val isPushPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permission == Manifest.permission.POST_NOTIFICATIONS
                } else {
                    false
                }

                if (isPushPermission) {
                    goToNotificationSettings(context)
                } else {
                    goToSystemSettings(context)
                }

                onResult(false)
            }
        }

        askedPermission += permission
    }

    fun isPushPermissionAllowedToAsk() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun canAskPermissionWithRationale(context: Context, permission: String): Boolean {
        val shouldShowRationale = (context as Activity).shouldShowRequestPermissionRationale(permission)

        return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                && (askedPermission.contains(permission).not() || shouldShowRationale)
    }

    fun arePushNotificationsEnabled(): Boolean {
        return if (isPushPermissionAllowedToAsk()) {
            checkPermission(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            areNotificationsEnabled()
        }
    }

    fun goToSystemSettings(context: Context) {
        startActivity(context, buildAppSettingsIntent(context), null)
    }

    fun goToNotificationSettings(context: Context) {
        startActivity(context, buildNotificationSettingsIntent(context), null)
    }
}
