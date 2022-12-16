package pjatk.smb.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StoreDao {

    @Query("select * from store where id = :storeId")
    fun getStoreById(storeId: Long): LiveData<Store>

    @Query("SELECT * FROM store")
    fun getAllStores(): LiveData<List<Store>>

    @Insert
    fun insert(store: Store): Long

    @Update
    fun update(store: Store)

    @Delete
    fun delete(store: Store)

}

