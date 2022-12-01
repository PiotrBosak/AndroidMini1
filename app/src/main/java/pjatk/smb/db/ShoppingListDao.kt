package pjatk.smb.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingListDao{

    @Query("SELECT * FROM shoppinglist")
    fun getLists(): LiveData<List<ShoppingList>>

    @Insert
    fun insert(shoppingList: ShoppingList)

    @Update
    fun update(shoppingList: ShoppingList)

    @Delete
    fun delete(shoppingList: ShoppingList)

    @Query("delete from ShoppingList")
    fun deleteAll()
}