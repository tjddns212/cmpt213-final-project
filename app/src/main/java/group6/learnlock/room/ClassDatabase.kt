package group6.learnlock.room

import android.content.Context
import android.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import group6.learnlock.model.Assignment

import group6.learnlock.model.Class
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Random

@Database(entities = [Class::class], version = 2)
abstract class ClassDatabase: RoomDatabase() {
    abstract val classDao: ClassDao

    companion object {
        @Volatile
        private var INSTANCE: ClassDatabase? = null

        fun getInstance(context: Context): ClassDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        ClassDatabase::class.java, "class_table")
                        .fallbackToDestructiveMigration() // Add this line
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
