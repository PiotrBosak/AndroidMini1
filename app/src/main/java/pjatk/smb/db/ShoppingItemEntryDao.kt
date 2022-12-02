package pjatk.smb.db

import androidx.lifecycle.LiveData

interface ShoppingItemEntryDao {

    fun getEntriesForList(listId: Long): LiveData<List<ShoppingItemEntry>>

    fun getEntryById(id: Long): ShoppingItemEntry

    fun insert(shoppingItemEntry: ShoppingItemEntry) : Long

    fun update(shoppingItemEntry: ShoppingItemEntry)

    fun delete(shoppingItemEntry: ShoppingItemEntry)

}