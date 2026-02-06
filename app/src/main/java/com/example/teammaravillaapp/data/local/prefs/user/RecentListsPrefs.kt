package com.example.teammaravillaapp.data.local.prefs.user

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentListsPrefs @Inject constructor(
    private val store: RecentListsPrefsStore
) {
    fun observeIds(): Flow<List<String>> = store.observeIds()
    suspend fun push(listId: String, max: Int = 20) = store.push(listId, max)
    suspend fun remove(listId: String) = store.remove(listId)
    suspend fun clear() = store.clear()
}