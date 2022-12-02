package pjatk.smb.db

import androidx.lifecycle.LiveData
import androidx.room.*

interface ShoppingListDao{

    fun getLists(): LiveData<List<ShoppingList>>

    fun insert(shoppingList: ShoppingList)

    fun update(shoppingList: ShoppingList)

    fun delete(shoppingList: ShoppingList)

    fun deleteAll()
}