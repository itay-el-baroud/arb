package com.example.callshield

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_numbers")
data class BlockedNumber(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phoneNumber: String,
    val nickname: String = "",
    val category: String = "شخصي",
    val attemptCount: Int = 0,
    val lastAttemptTime: Long = 0,
    val blockUntil: Long = 0
)
