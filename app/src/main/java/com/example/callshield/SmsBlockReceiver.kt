package com.example.callshield

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsBlockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (message in messages) {
            val sender = message.originatingAddress ?: continue
            val db = AppDatabase.getInstance(context)
            val blocked = db.blockedNumberDao().findByNumber(sender)

            if (blocked != null) {
                val now = System.currentTimeMillis()
                val updated = blocked.copy(
                    attemptCount = blocked.attemptCount + 1,
                    lastAttemptTime = now
                )
                db.blockedNumberDao().update(updated)
                db.callLogDao().insert(CallLog(phoneNumber = sender, timestamp = now, type = "sms"))
                NotificationHelper.showBlockedSmsNotification(context, sender, updated.attemptCount)
                abortBroadcast()
            }
        }
    }
}
