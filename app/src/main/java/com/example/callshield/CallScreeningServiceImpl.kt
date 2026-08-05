package com.example.callshield

import android.telecom.Call
import android.telecom.CallScreeningService
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class CallScreeningServiceImpl : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val number = callDetails.handle?.schemeSpecificPart ?: ""
        val db = AppDatabase.getInstance(applicationContext)
        val blocked = db.blockedNumberDao().findByNumber(number)

        val now = System.currentTimeMillis()
        val isTemporarilyExpired = blocked != null && blocked.blockUntil in 1 until now

        if (blocked != null && !isTemporarilyExpired) {
            val response = CallResponse.Builder()
                .setDisallowCall(true)
                .setRejectCall(true)
                .setSkipCallLog(false)
                .setSkipNotification(false)
                .build()
            respondToCall(callDetails, response)

            val updated = blocked.copy(
                attemptCount = blocked.attemptCount + 1,
                lastAttemptTime = now
            )
            db.blockedNumberDao().update(updated)
            db.callLogDao().insert(CallLog(phoneNumber = number, timestamp = now, type = "call"))

            NotificationHelper.showBlockedCallNotification(applicationContext, number, updated.attemptCount)
        } else {
            respondToCall(callDetails, CallResponse.Builder().build())
            checkSmartBlock(applicationContext, number)
        }
    }

    private fun checkSmartBlock(context: android.content.Context, number: String) {
        val prefs = context.getSharedPreferences("callshield_prefs", 0)
        val threshold = prefs.getInt("smart_block_threshold", 0)
        if (threshold <= 0) return

        val db = AppDatabase.getInstance(context)
        val logs = db.callLogDao().getAll().filter { it.phoneNumber == number }
        if (logs.size >= threshold) {
            val existing = db.blockedNumberDao().findByNumber(number)
            if (existing == null) {
                db.blockedNumberDao().insert(
                    BlockedNumber(phoneNumber = number, category = "سبام")
                )
            }
        }
    }
}
