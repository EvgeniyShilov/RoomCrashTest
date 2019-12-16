package by.roomcrashtest.ui.main

import android.app.Application
import android.util.Log
import androidx.arch.core.internal.SafeIterableMap
import androidx.lifecycle.AndroidViewModel
import androidx.paging.Config
import androidx.paging.toFlowable
import androidx.room.InvalidationTracker
import androidx.room.Room
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val disposable: Disposable

    init {
        val database = Room.databaseBuilder(application, Database::class.java, "database")
            .build()
        val dataDAO = database.dataDAO()
        disposable = dataDAO
            .observeData()
            .toFlowable(
                config = Config(pageSize = 25, maxSize = 200),
                fetchScheduler = Schedulers.io()
            )
            .subscribeOn(Schedulers.io())
            .subscribe { Log.d("NEW_LIST", "${it.hashCode()}") }

        Thread {
            while (true) {
                Thread.sleep(15L)
                dataDAO.insertData(Data())
            }
        }.start()

        val observerMapField = InvalidationTracker::class.java.getDeclaredField("mObserverMap")
        observerMapField.isAccessible = true
        val fieldValue = observerMapField.get(database.invalidationTracker) as SafeIterableMap<*, *>
        Thread {
            while (true) {
                Thread.sleep(1000L)
                Log.d("OBSERVER_SIZE", "${fieldValue.size()}")
            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}
