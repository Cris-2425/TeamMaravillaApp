package com.example.teammaravillaapp.page.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.data.repository.ListProgress
import com.example.teammaravillaapp.data.repository.ListsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listsRepository: ListsRepository
) : ViewModel() {

    private val search = MutableStateFlow("")

    // Revisar en los demás viewModels que hay private mutablestateflow
    // y val uistate stateFlow
    private val pendingDeletes = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<HomeUiState> =
        combine(
            search,
            listsRepository.lists,
            listsRepository.observeProgress(),
            pendingDeletes
        ) { q, lists, progressMap, pending ->

            val trimmed = q.trim()

            val filtered = lists
                .filter { (id, _) -> id !in pending }   // 👈 CLAVE
                .let {
                    if (trimmed.isBlank()) it
                    else it.filter { (_, list) ->
                        list.name.contains(trimmed, ignoreCase = true)
                    }
                }

            val rows = filtered.map { (id, list) ->
                HomeListRow(
                    id = id,
                    list = list,
                    progress = progressMap[id]
                        ?: ListProgress(0, 0)
                )
            }

            HomeUiState(
                search = q,
                recentLists = rows
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    init {
        viewModelScope.launch { listsRepository.seedIfEmpty() }
    }

    fun onSearchChange(newValue: String) {
        search.value = newValue
    }

    fun deleteList(id: String) = viewModelScope.launch {
        listsRepository.deleteById(id)
    }

    // ✅ Paso 1: marcar como “pendiente” (desaparece de UI)
    fun requestDelete(id: String) {
        pendingDeletes.value = pendingDeletes.value + id
    }

    // ✅ Si deshace: vuelve a aparecer
    fun undoDelete(id: String) {
        pendingDeletes.value = pendingDeletes.value - id
    }

    // ✅ Paso 2: cuando el snackbar se cierra sin deshacer -> borra de verdad
    fun commitDelete(id: String) {
        viewModelScope.launch {
            listsRepository.deleteById(id)
            pendingDeletes.value = pendingDeletes.value - id // limpieza por si acaso
        }
    }

    fun renameList(id: String, newName: String) {
        viewModelScope.launch {
            listsRepository.rename(id, newName)
        }
    }
}