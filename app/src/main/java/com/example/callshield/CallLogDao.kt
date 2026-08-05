package com.example.callshield

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CallLogDao {

    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    fun getAll(): List<CallLog>

    @Insert
    fun insert(log: CallLog)
}
