package by.roomcrashtest.ui.main

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Data(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
