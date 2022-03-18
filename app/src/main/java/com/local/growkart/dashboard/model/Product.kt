package com.local.growkart.dashboard.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//@Entity(tableName = TableName.PRODUCT)
@Parcelize
data class Product(
//    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
//    @ColumnInfo (name = "name")
    val name: String?,
//    @ColumnInfo (name="iconUrl")
    val iconUrl: String?,
//    @ColumnInfo (name="price")
    val price: Float?
) : Parcelable {

    constructor() : this(
        "", "", "", 0.0F
    )
}