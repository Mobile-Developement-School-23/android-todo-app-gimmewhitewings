package com.example.todoapp.data

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.todoapp.R
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.di.scope.AppScope
import com.example.todoapp.utils.DEADLINE_CHANNEL_ID
import com.example.todoapp.utils.ID_KEY
import com.example.todoapp.utils.IMPORTANCE_KEY
import com.example.todoapp.utils.TEXT_KEY
import javax.inject.Inject
import kotlin.random.Random



@AppScope
class TodoAlarmScheduler @Inject constructor(
    private val context: Context,
    private val alarmManager: AlarmManager
) {
    fun schedule(todoItem: TodoItem) {
        val intent = Intent(context, TodoAlarmReceiver::class.java).apply {
            putExtra(ID_KEY, todoItem.id)
            putExtra(IMPORTANCE_KEY, todoItem.importance.ordinal)
            putExtra(TEXT_KEY, todoItem.text)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            todoItem.deadline!!.time,
            PendingIntent.getBroadcast(
                context,
                todoItem.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel(todoItem: TodoItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                todoItem.id.hashCode(),
                Intent(context, TodoAlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}

class TodoAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationBuilder = NotificationCompat.Builder(context!!, DEADLINE_CHANNEL_ID)
            .setContentTitle(
                context.getString(
                    when (intent?.getIntExtra(IMPORTANCE_KEY, 1)) {
                        0 -> R.string.low
                        1 -> R.string.no
                        2 -> R.string.high
                        else -> R.string.no
                    }
                )
            )
            .setContentText(intent?.getStringExtra(TEXT_KEY))
            .setSmallIcon(
                when (intent?.getIntExtra(IMPORTANCE_KEY, 1)) {
                    0 -> R.drawable.ic_priority_low
                    1 -> R.drawable.baseline_notifications_24
                    2 -> R.drawable.ic_priority_high
                    else -> R.drawable.baseline_notifications_24
                }
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }
}
