package com.example.speedmarket.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoProdotto {
    @Query("select * from databaseprodotto")
    fun getProdotti(): LiveData<List<DatabaseProdotto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProdotto(prodotto: DatabaseProdotto)

    @Delete
    fun delete(prodotto: DatabaseProdotto)

    @Update
    fun update(prodotto: DatabaseProdotto)
}

@Dao
interface DaoCarrello {

    @Query("select * from databasecarrello")
    fun getCarrelli(): LiveData<List<DatabaseCarrello>>

    @Query("select * from databasecarrello where id=:id")
    fun getCarrello(id: String): LiveData<DatabaseCarrello>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCarrello(carrello: DatabaseCarrello)

    @Delete
    fun delete(carrello: DatabaseCarrello)

    @Update
    fun update(carrello: DatabaseCarrello)
}

@Database(entities = [DatabaseProdotto::class], version = 1, exportSchema = false)
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

@Database(entities = [DatabaseCarrello::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CarrelloDatabase: RoomDatabase() {
    abstract fun carrelloDao(): DaoCarrello

    companion object {
        @Volatile
        private var INSTANCE: CarrelloDatabase? = null

        fun getInstance(context: Context): CarrelloDatabase {
            return INSTANCE?: synchronized(this) {
                INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    CarrelloDatabase::class.java, "carrelli")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build()
                    .also{ INSTANCE = it }
            }
        }
    }
}
