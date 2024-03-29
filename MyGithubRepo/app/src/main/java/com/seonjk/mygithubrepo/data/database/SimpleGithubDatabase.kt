package com.seonjk.mygithubrepo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GithubRepoEntity::class], exportSchema = false, version = 1)
abstract class SimpleGithubDatabase : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao

}