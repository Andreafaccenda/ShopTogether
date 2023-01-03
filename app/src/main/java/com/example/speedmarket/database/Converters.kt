package com.example.speedmarket.database

import androidx.room.TypeConverter
import com.example.speedmarket.model.Prodotto
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class Converters {
    @TypeConverter
    fun fromArrayList(list: MutableList<Prodotto?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(value: String?): MutableList<Prodotto> {
        val listType: Type = object: TypeToken<MutableList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
}