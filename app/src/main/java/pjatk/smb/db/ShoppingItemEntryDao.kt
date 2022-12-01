package pjatk.smb.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingItemEntryDao {

    @Query("select * from shoppingitementry where shoppingListId = :listId")
    fun getEntriesForList(listId: Long): LiveData<List<ShoppingItemEntry>>

    @Query("select * from shoppingitementry where id = :id")
    fun getEntryById(id: Long): ShoppingItemEntry

    @Insert
    fun insert(shoppingItemEntry: ShoppingItemEntry) : Long

    @Update
    fun update(shoppingItemEntry: ShoppingItemEntry)

    @Delete
    fun delete(shoppingItemEntry: ShoppingItemEntry)

}