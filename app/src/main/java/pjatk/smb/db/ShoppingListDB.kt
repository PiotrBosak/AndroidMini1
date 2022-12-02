package pjatk.smb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class ShoppingListDB : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingEntryDao(): ShoppingItemEntryDao

    companion object {
        private var shoppingListDbInstance: ShoppingListDB? = null

        fun getDatabase(context: Context): ShoppingListDB {
            return if (shoppingListDbInstance != null)
                shoppingListDbInstance as ShoppingListDB
            else Room.databaseBuilder(
                context.applicationContext,
                ShoppingListDB::class.java,
                "Shopping list db"
            ).build()
        }
    }
}