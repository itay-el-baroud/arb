package com.example.callshield

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_logs")
data class CallLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phoneNumber: String,
    val timestamp: Long,
    val type: String
)
