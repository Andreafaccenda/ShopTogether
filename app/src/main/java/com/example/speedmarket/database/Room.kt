package com.example.speedmarket.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.speedmarket.model.Prodotto

@Dao
interface DaoProdotto {
    @Query("select * from databaseprodotto")
    fun getProdotti(): LiveData<List<DatabaseProdotto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProdotti(prodotti: List<DatabaseProdotto>)

    @Delete
    fun delete(prodotto: DatabaseProdotto)

    @Update
    fun update(prodotto: DatabaseProdotto)
}

@Database(entities = [DatabaseProdotto::class], version = 1)
abstract class ProductsDatabase: RoomDatabase() {
    abstract fun prodottoDao(): DaoProdotto

    companion object {
        @Volatile
        private var INSTANCE: ProductsDatabase? = null

        fun getInstance(context: Context): ProductsDatabase {
            return INSTANCE?: synchronized(this) {
                INSTANCE?: Room.databaseBuilder(
                context.applicationContext,
                ProductsDatabase::class.java, "products")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build()
                    .also { INSTANCE = it }
            }
        }
    }
}
