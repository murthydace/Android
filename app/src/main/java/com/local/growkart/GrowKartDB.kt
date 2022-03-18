//package com.local.growkart
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.local.growkart.dashboard.model.Product
//
//@Database(
//    entities = [Product::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class GrowKartDB : RoomDatabase() {
////    abstract fun productDao(): ProductDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: GrowKartDB? = null
//        fun getDatabaseInstance(context: Context): GrowKartDB {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    GrowKartDB::class.java,
//                    DataBaseName.dbName
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
class GrowKartDB