package com.example.teammaravillaapp.page.listdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.local.prefs.user.RecentListsPrefs
import com.example.teammaravillaapp.navigation.NavRoute.ListDetail.ARG_LIST_ID
import com.example.teammaravillaapp.page.listdetail.usecase.ListDetailHandleActionUseCase
import com.example.teammaravillaapp.page.listdetail.usecase.ListDetailObserveStateUseCase
import com.example.teammaravillaapp.page.listdetail.usecase.RefreshCatalogUseCase
import com.example.teammaravillaapp.ui.events.UiEvent
import com.example.teammaravillaapp.ui.events.UiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDetailViewModel @Inject constructor(
    observeState: ListDetailObserveStateUseCase,
    private val handleAction: ListDetailHandleActionUseCase,
    private val refreshCatalog: RefreshCatalogUseCase,
    private val listDetailPrefs: ListDetailPrefs,
    private val recentListsPrefs: RecentListsPrefs,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navListId: String? = savedStateHandle.get<String>(ARG_LIST_ID)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow() // opcional (solo si lo quieres exponer)

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _uiState: StateFlow<ListDetailUiState> =
        observeState.execute(navListId = navListId, queryFlow = _query)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ListDetailUiState(listId = navListId, isLoadingCatalog = true)
            )

    val uiState: StateFlow<ListDetailUiState> = _uiState

    init {
            navListId?.let { id ->
                viewModelScope.launch { recentListsPrefs.push(id) }
            }

            viewModelScope.launch {
                when (val res = refreshCatalog.executeBestEffort()) {
                    is UiResult.Error -> _events.tryEmit(UiEvent.ShowSnackbar(res.messageResId))
                    else -> Unit
                }
            }
    }

    fun clearCategoryFilter() {
        viewModelScope.launch { listDetailPrefs.clearCategoryFilter() }
    }

    fun onAction(action: ListDetailAction) {
        when (action) {
            is ListDetailAction.QueryChanged -> _query.value = action.value

            else -> {
                val listId = uiState.value.listId ?: return
                viewModelScope.launch {
                    runCatching { handleAction.execute(listId, action) }
                        .onSuccess {
                            when (action) {
                                ListDetailAction.RemoveChecked ->
                                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_checked_removed))

                                ListDetailAction.ClearList ->
                                    _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_list_cleared))

                                else -> Unit
                            }
                        }
                        .onFailure {
                            _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed))
                        }
                }
            }
        }
    }
}