//package com.local.growkart.dashboard.dao
//
//import androidx.room.*
//import com.local.growkart.DataConverter
//import com.local.growkart.TableName
//import com.local.growkart.dashboard.model.Product
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface ProductDao {
//    @Query("SELECT * FROM ${TableName.PRODUCT} ORDER BY id ASC")
//    @TypeConverters(DataConverter::class)
//    suspend fun getAllProducts(): List<Product>
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(product: Product)
//
//    @Delete
//    suspend fun delete(product: Product)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertProducts(vararg products: Product)
//}