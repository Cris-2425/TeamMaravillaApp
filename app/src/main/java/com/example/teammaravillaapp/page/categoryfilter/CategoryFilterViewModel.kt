package com.example.teammaravillaapp.page.categoryfilter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teammaravillaapp.R
import com.example.teammaravillaapp.data.prefs.CategoryFilterPrefs
import com.example.teammaravillaapp.model.ProductCategory
import com.example.teammaravillaapp.ui.events.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryFilterViewModel @Inject constructor(
    @ApplicationContext private val ctx: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryFilterUiState())
    val uiState: StateFlow<CategoryFilterUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        // Carga y sincroniza con DataStore
        viewModelScope.launch {
            CategoryFilterPrefs.observe(ctx).collect { saved ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selected = saved
                    )
                }
            }
        }
    }

    fun toggle(category: ProductCategory) {
        _uiState.update { st ->
            val next =
                if (category in st.selected) st.selected - category
                else st.selected + category
            st.copy(selected = next)
        }
    }

    fun toggleAll() {
        _uiState.update { st ->
            st.copy(selected = if (st.allSelected) emptySet() else st.all)
        }
    }

    fun save(onSaved: () -> Unit) {
        val selected = _uiState.value.selected
        viewModelScope.launch {
            runCatching { CategoryFilterPrefs.save(ctx, selected) }
                .onSuccess { onSaved() }
                .onFailure { _events.tryEmit(UiEvent.ShowSnackbar(R.string.snackbar_action_failed)) }
        }
    }
}