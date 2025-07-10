package com.tonyxlab.notemark.presentation.screens.editor

import com.tonyxlab.notemark.presentation.core.base.BaseViewModel
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorActionEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiEvent
import com.tonyxlab.notemark.presentation.screens.editor.handling.EditorUiState

typealias EditorBaseViewModel = BaseViewModel<EditorUiState, EditorUiEvent, EditorActionEvent>
class EditorViewModel : EditorBaseViewModel(){

    override val initialState: EditorUiState
        get() = EditorUiState()

    override fun onEvent(event: EditorUiEvent) {
        TODO("Not yet implemented")
    }

}
