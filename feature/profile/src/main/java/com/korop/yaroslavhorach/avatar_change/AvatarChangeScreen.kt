package com.korop.yaroslavhorach.avatar_change

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.korop.yaroslavhorach.avatar_change.model.AvatarChangeAction
import com.korop.yaroslavhorach.avatar_change.model.AvatarChangeViewState
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.theme.Golden
import com.korop.yaroslavhorach.designsystem.theme.LinguaTheme
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.BoxWithStripes
import com.korop.yaroslavhorach.designsystem.theme.components.PrimaryButton
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.domain.prefs.model.Avatar
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter

@Composable
internal fun AvatarChangeRoute(
    viewModel: AvatarChangeViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToPremium: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    AvatarChangeScreen(
        state = state,
        actioner = { action ->
            when (action) {
                is AvatarChangeAction.OnBackClicked -> {
                    navigateBack()
                    keyboardController?.hide()
                }
                is AvatarChangeAction.OnPremiumClicked -> {
                    navigateToPremium()
                    keyboardController?.hide()
                }
                else -> viewModel.submitAction(action)
            }
        })
}

@Composable
internal fun AvatarChangeScreen(
    state: AvatarChangeViewState,
    actioner: (AvatarChangeAction) -> Unit,
) {
    val typedText = remember { mutableStateOf("") }

    LaunchedEffect(state.userName) {
        if (typedText.value.isBlank()) {
            typedText.value = state.userName
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { typedText.value }
            .debounce(500)
            .filter { it.isEmpty().not() }
            .collect { actioner(AvatarChangeAction.OnNameTyped(it)) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
    ) {
        TopBar(state, actioner)
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(com.korop.yaroslavhorach.profile.R.string.avatar_change_avatar_block_name_title_text),
            modifier = Modifier
                .padding(horizontal = 20.dp),
            color = MaterialTheme.colorScheme.typoPrimary(),
            style = LinguaTypography.subtitle2
        )
        Spacer(Modifier.height(14.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .border(
                    1.5.dp,
                    MaterialTheme.colorScheme.onBackgroundDark(),
                    shape = RoundedCornerShape(8.dp)
                ),

            value = typedText.value,
            onValueChange = { if (it.length <= 50) { typedText.value = it } },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(Modifier.height(20.dp))
        Avatars(state, actioner)
        if (state.isOnboarding) {
            PrimaryButton(
                text = stringResource(com.korop.yaroslavhorach.profile.R.string.avatar_change_primary_btn_text),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                actioner(AvatarChangeAction.OnNextClicked)
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun ColumnScope.Avatars(
    state: AvatarChangeViewState,
    actioner: (AvatarChangeAction) -> Unit
) {
    Text(
        text = stringResource(com.korop.yaroslavhorach.profile.R.string.avatar_change_avatar_blick_title_text),
        modifier = Modifier
            .padding(horizontal = 20.dp),
        color = MaterialTheme.colorScheme.typoPrimary(),
        style = LinguaTypography.subtitle2
    )
    Spacer(Modifier.height(10.dp))
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .weight(1f)
            .padding(bottom = 16.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(state.avatars) { avatar ->
            AvatarItem(state, avatar, actioner)
        }
    }
}

@Composable
private fun AvatarItem(
    state: AvatarChangeViewState,
    avatar: Avatar,
    actioner: (AvatarChangeAction) -> Unit,
    ) {
    val isPremiumAvatar = avatar.isPremium && state.isPremiumUser.not()

    Box {
        BoxWithStripes(
            stripeColor = Color.Transparent,
            rawShadowYOffset = 2.3.dp,
            contentPadding = 16.dp,
            background = MaterialTheme.colorScheme.surface,
            backgroundShadow = MaterialTheme.colorScheme.onBackgroundDark(),
            borderWidth = 1.5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            onClick = {
                if (isPremiumAvatar) {
                    actioner(AvatarChangeAction.OnPremiumClicked)
                } else {
                    actioner(AvatarChangeAction.OnAvatarChosen(avatar.resId))
                }
            },
            borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(avatar.resId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
        if (isPremiumAvatar) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (-10).dp)
                    .background(
                        color = Golden,
                        shape = RoundedCornerShape(
                            bottomStart = 8.dp,
                            topEnd = 8.dp
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "PREMIUM",
                    color = Color.White,
                    style = LinguaTypography.subtitle6
                )
            }
        }
    }
}

@Composable
private fun TopBar(screenState: AvatarChangeViewState, actioner: (AvatarChangeAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Image(
            painter = painterResource(R.drawable.im_profile_gradient),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .align(Alignment.TopCenter),
        ) {
            if (screenState.isOnboarding.not()){
                Image(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .size(50.dp)
                        .clickable { actioner(AvatarChangeAction.OnBackClicked) },
                    painter = painterResource(LinguaIcons.CircleCloseDark),
                    contentDescription = null
                )
            }
            Spacer(Modifier.height(20.dp))
            if (screenState.avatarResId != null) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp),
                    painter = painterResource(screenState.avatarResId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(Modifier.height(22.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = screenState.userName,
                style = LinguaTypography.h2,
                color = MaterialTheme.colorScheme.typoPrimary()
            )
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Preview
@Composable
private fun AvatarChangePreview() {
    Column {
        LinguaTheme {
            AvatarChangeScreen(
                AvatarChangeViewState.Preview,
                actioner = { _ -> },
            )
        }
    }
}

