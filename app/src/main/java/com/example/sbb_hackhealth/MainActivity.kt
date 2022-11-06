package com.example.sbb_hackhealth

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.sbb_hackhealth.databinding.ActivityMainBinding
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.InputStream
import java.lang.reflect.Array.get


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val CHANNEL_ID = "important"

    private var storicpriority = 1
    private val travelId = 1
    private val emergencyId = 2
    private val requestId = 3
    private val journeyId = 4
    private val generalId = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val data : List<Announcement> = import();


        for(announcement in data){
            val priority = announcement.priority

            val classifier = when(priority) {
                1 -> "Travel Issue"
                2 -> "Emergency"
                3 -> "Request"
                4 -> "Journey Information"
                else -> "General Information"
            }
            val foto = when(priority) {
                1 -> R.drawable.emergency
                2 -> R.drawable.emergency
                3 -> R.drawable.emergency
                4 -> R.mipmap.travelinfo
                else -> R.drawable.emergency
            }
            val id = when(priority) {
                1 -> travelId
                2 -> emergencyId
                3 -> requestId
                4 -> journeyId
                else -> generalId
            }

            val message = announcement.payload!!.textEn
            Thread.sleep(5000)

            createNotificationChannel(context = navController.context, id = id, pushNotificationTitle = classifier, pushNotificationContent = message, icon = foto, prior=priority)


        }


/*
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        var i = 0
        binding.fab.setOnClickListener { view ->
            val newMessageNotification = Notification.Builder(view.context, "important")
                .setSmallIcon(androidx.appcompat.R.drawable.abc_dialog_material_background)
                .setContentTitle(getString(R.string.pushNotificationTitle))
                .setContentText(getString(R.string.pushNotificationContent))
//                .addAction(action)
                .build()



// Issue the notification.
            with(NotificationManagerCompat.from(this)) {
                notificationManager.notify(i++, newMessageNotification)
            }
        }*/


    }

    private fun import(): List<Announcement> {
        val data: InputStream  = applicationContext.assets.open("category_data")
        return ObjectMapper().readValue(data, object : TypeReference<List<Announcement>>() {})
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun createNotificationChannel(
        context: Context,
        id: Int,
        pushNotificationTitle: String,
        pushNotificationContent: String,
        icon: Int,
        prior: Int
    ){
        val name = getString(R.string.channel_name)
        //var importance = NotificationManager.IMPORTANCE_DEFAULT
        val descriptionText = getString(R.string.channel_description)
        val importance = when(prior) {
            1 -> NotificationManager.IMPORTANCE_MAX
            2 -> NotificationManager.IMPORTANCE_DEFAULT
            3 -> NotificationManager.IMPORTANCE_DEFAULT
            4 ->NotificationManager.IMPORTANCE_DEFAULT
            else ->NotificationManager.IMPORTANCE_LOW
        }


        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        val newMessageNotification = Notification.Builder(context, "important")
            .setSmallIcon(icon)
            .setContentTitle(pushNotificationTitle)
            .setContentText(pushNotificationContent)

          //  .addAction(VibrationEffect.EFFECT_CLICK)
            .build()

// Issue the notification.
            with(NotificationManagerCompat.from(this)) {
                notificationManager.notify(id, newMessageNotification)
            }




    }
}

//private fun Notification.Builder.addAction(effectClick: Int): Notification.Builder {
//}
