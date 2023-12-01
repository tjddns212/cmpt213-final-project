package group6.learnlock.room

import androidx.room.RoomDatabase

abstract class ClassDatabase: RoomDatabase() {
    abstract fun classDao(): ClassDao
}