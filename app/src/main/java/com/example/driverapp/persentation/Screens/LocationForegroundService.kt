package com.example.driverapp.persentation.Screens

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.driverapp.MainActivity
import com.example.driverapp.R
import com.google.android.gms.location.*

class LocationForegroundService : Service() {

    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var callback: LocationCallback

    override fun onCreate() {
        super.onCreate()

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return

                // 🔥 Send broadcast back to ViewModel
                val intent = Intent("LOCATION_UPDATE")
                intent.putExtra("lat", loc.latitude)
                intent.putExtra("lon", loc.longitude)
                sendBroadcast(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(
            1,
            buildNotification()
        )

        startLocationUpdates()
        return START_STICKY

        startForeground(1, buildNotification())
        startLocationUpdates()

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotification(): Notification {

        val channelId = "location_channel"

        // Create channel
        val channel = NotificationChannel(
            channelId,
            "Location Tracking",
            NotificationManager.IMPORTANCE_LOW   // Clean & quiet
        ).apply {
            description = "Shows when live bus tracking is active"
            setShowBadge(false)
            enableLights(false)
            enableVibration(false)
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        // Open app when notification clicked
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build beautiful modern notification
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation
            )     // prettier icon
            .setContentTitle("🚍 Live Location Sharing")
            .setContentText("Your bus location is updating in real-time")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "Your live bus location is continuously shared with the school system."
                )
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setColor(0xFF0A84FF.toInt())               // Blue accent color
            .build()
    }



//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun buildNotification(): Notification {
//
//        val channelId = "location_channel"
//
//        val channel = NotificationChannel(
//            channelId,
//            "Location Tracking",
//            NotificationManager.IMPORTANCE_LOW
//        )
//        val manager = getSystemService(NotificationManager::class.java)
//        manager.createNotificationChannel(channel)
//
//        // 👉 Action 1: Stop service
//        val stopIntent = Intent(this, LocationForegroundService::class.java).apply {
//            action = "STOP_SERVICE"
//        }
//        val stopPending = PendingIntent.getService(
//            this, 1, stopIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        // 👉 Action 2: Open App when clicking notification
//        val openAppIntent = Intent(this, MainActivity::class.java)
//        val openAppPending = PendingIntent.getActivity(
//            this, 2, openAppIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        return NotificationCompat.Builder(this, channelId)
//            .setContentTitle("Sharing Live Location")
//            .setContentText("Tap to return • Stop when done")
//            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
//            .setContentIntent(openAppPending)        // 🟢 Tap → open app
//            .addAction(android.R.drawable.ic_menu_close_clear_cancel
//                , "Stop", stopPending)  // 🛑 Stop button
//            .setOngoing(true)
//            .setSilent(true)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .build()
//    }
//

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 3000L
        ).build()

        fusedLocation.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocation.removeLocationUpdates(callback)
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
