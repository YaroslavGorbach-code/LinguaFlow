package com.example.yaroslavhorach.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.domain.exercise.model.Exercise
import com.example.yaroslavhorach.home.model.GamesAction
import com.example.yaroslavhorach.home.model.GamesViewState

@Composable
internal fun GamesRoute(
    onNavigateToGame: (Exercise) -> Unit,
    viewModel: GamesViewModel = hiltViewModel(),
) {
    val homeState by viewModel.state.collectAsStateWithLifecycle()

    GamesScreen(
        screenState = homeState,
        onMessageShown = viewModel::clearMessage,
        actioner = { action ->
            when (action) {
                else -> viewModel.submitAction(action)
            }
        })
}

@Composable
internal fun GamesScreen(
    screenState: GamesViewState,
    onMessageShown: (id: Long) -> Unit,
    actioner: (GamesAction) -> Unit,
) {

}

@Preview
@Composable
private fun HomePreview() {
    Column {
        LinguaTheme {
            GamesScreen(
                GamesViewState.Preview,
                {},
                actioner = { _ -> },
            )
        }
    }
}

