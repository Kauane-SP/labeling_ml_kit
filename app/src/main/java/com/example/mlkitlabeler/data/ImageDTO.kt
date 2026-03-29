package com.example.mlkitlabeler.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.net.Uri

@Entity
data class ImageDTO(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "image") var image: Uri
)