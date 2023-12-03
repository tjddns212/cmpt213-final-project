package group6.learnlock.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import group6.learnlock.model.Class
@Database(entities = [Class::class], version = 1)
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
                     ClassDatabase::class.java, "class_table").build()
                 INSTANCE = instance
             }
                return instance
            }
        }
    }

}