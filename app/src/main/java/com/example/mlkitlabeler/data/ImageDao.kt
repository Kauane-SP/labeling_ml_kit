package com.example.mlkitlabeler.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Query("SELECT * FROM imagedto")
    fun getAll(): List<ImageDTO>

    @Insert
    fun insertAll(vararg imageDTO: ImageDTO)

    @Delete
    fun delete(imageDTO: ImageDTO)
}