package com.example.mlkitlabeler.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ImageDTO::class], version = 1)
@TypeConverters(Converters::class)
abstract class ImageDataBase: RoomDatabase() {
    abstract fun getRunDao(): ImageDao
}