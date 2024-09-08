package com.mahmoud.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mahmoud.newsapp.models.Article

@Database(entities = [Article::class], version = 1)
abstract class ArticlesDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "articles_db.db"
        private var INSTANCE: ArticlesDatabase? = null

        fun getInstance(context: Context): ArticlesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        ArticlesDatabase::class.java,
                        DB_NAME
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }

        }
    }
}