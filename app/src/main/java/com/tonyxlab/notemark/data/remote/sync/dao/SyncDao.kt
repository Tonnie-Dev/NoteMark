package com.tonyxlab.notemark.data.remote.sync.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tonyxlab.notemark.data.remote.sync.entity.SyncRecord

@Dao
interface SyncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(syncRecord: SyncRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(syncRecords: List<SyncRecord>)

    @Query("""
       SELECT * FROM sync_record 
        WHERE userId = :userId
        ORDER BY timestamp
        LIMIT :limit
        
    """)
    suspend fun loadBatch(userId:String, limit:Int):List<SyncRecord>

   @Query("DELETE FROM sync_record WHERE id IN (:ids)")
   suspend fun deleteByIds(ids:List<String>)

   @Query("SELECT COUNT(*) FROM sync_record WHERE userId =:userId")
   suspend fun countForUser(userId: String): Int

}