package com.example.callshield

import androidx.room.*

@Dao
interface BlockedNumberDao {

    @Query("SELECT * FROM blocked_numbers ORDER BY lastAttemptTime DESC")
    fun getAll(): List<BlockedNumber>

    @Query("SELECT * FROM blocked_numbers WHERE phoneNumber = :number LIMIT 1")
    fun findByNumber(number: String): BlockedNumber?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(number: BlockedNumber)

    @Update
    fun update(number: BlockedNumber)

    @Delete
    fun delete(number: BlockedNumber)
}
