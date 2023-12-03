package group6.learnlock.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class ClassDatabase: RoomDatabase() {
    abstract val classDao: ClassDao

    companion object {
        @Volatile
        private var INSTANCE: ClassDatabase? = null

        fun getInstance(context: Context): ClassDatabase {
            synchronized(this) {
             var instance = INSTANCE
             if (instance == null) {
                 instance = Room.databaseBuilder(context.applicationContext, ClassDatabase::class.java, "classes").build()
                 INSTANCE = instance
             }
                return instance
            }
        }
    }

}