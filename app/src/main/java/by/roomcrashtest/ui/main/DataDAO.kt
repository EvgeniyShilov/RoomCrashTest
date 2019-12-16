package by.roomcrashtest.ui.main

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDAO {

    @Query("SELECT * FROM data")
    fun observeData(): DataSource.Factory<Int, Data>

    @Insert
    fun insertData(data: Data)
}
