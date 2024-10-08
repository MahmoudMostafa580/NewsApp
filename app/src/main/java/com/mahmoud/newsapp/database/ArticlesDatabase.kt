package com.mahmoud.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mahmoud.newsapp.models.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticlesDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        private const val DB_NAME = "articles_db.db"

        @Volatile
        private var INSTANCE: ArticlesDatabase? = null

        fun getInstance(context: Context): ArticlesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
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