package com.example.smarthr_app.services

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.smarthr_app.MainActivity
import com.example.smarthr_app.R
import com.example.smarthr_app.data.local.DataStoreManager
import com.example.smarthr_app.data.repository.AttendanceRepository
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationTrackingService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var attendanceRepository: AttendanceRepository
    private var attendanceId: String? = null

    companion object {
        const val CHANNEL_ID = "LocationTrackingChannel"
        const val NOTIFICATION_ID = 1001
        const val TRACKING_INTERVAL_MS = 30000L // 30 seconds
    }

    override fun onCreate() {
        super.onCreate()
        val dataStoreManager = DataStoreManager(applicationContext)
        attendanceRepository = AttendanceRepository(dataStoreManager)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        createNotificationChannel()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation ?: return
                sendLocationToBackend(location)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        attendanceId = intent?.getStringExtra("attendance_id")
        
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        startLocationUpdates()

        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TRACKING_INTERVAL_MS
        ).setMinUpdateIntervalMillis(TRACKING_INTERVAL_MS / 2)
         .build()

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            // No permission
        }
    }

    private fun sendLocationToBackend(location: Location) {
        val id = attendanceId ?: return
        CoroutineScope(Dispatchers.IO).launch {
            attendanceRepository.recordLocationTrail(id, location.latitude, location.longitude)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Employee Tracking Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("CrewHQ Location Tracker")
            .setContentText("Securing your attendance boundaries...")
            .setSmallIcon(R.drawable.app_logo)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
