package tp.com.n8422.roomshoppinglist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingListItem::class], version = 1)
abstract class ShoppingListRoomDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
}