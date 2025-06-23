package com.example.yaroslavhorach.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yaroslavhorach.designsystem.R
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.White
import com.example.yaroslavhorach.designsystem.theme.typoPrimary
import com.example.yaroslavhorach.home.model.ProfileAction
import com.example.yaroslavhorach.home.model.ProfileViewState

@Composable
internal fun ProfileRoute(viewModel: ProfileViewModel = hiltViewModel()) {
    val profileState by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = profileState,
        onMessageShown = viewModel::clearMessage,
        actioner = { action ->
            when (action) {

                else -> viewModel.submitAction(action)
            }
        })
}

@Composable
internal fun ProfileScreen(
    state: ProfileViewState,
    onMessageShown: (id: Long) -> Unit,
    actioner: (ProfileAction) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(state, actioner)
        Spacer(Modifier.height(20.dp))
        PremiumBanner(state, actioner)
    }
}

@Composable
private fun TopBar(screenState: ProfileViewState, actioner: (ProfileAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Image(
            painter = painterResource(R.drawable.im_games_gradient),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
        )

        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            Image(
                modifier = Modifier
                    .size(120.dp),
                painter = painterResource(R.drawable.im_avatar_1),
                contentDescription = null,
                contentScale = ContentScale.Crop,

                )
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

@Composable
private fun PremiumBanner(screenState: ProfileViewState, actioner: (ProfileAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center),
            painter = painterResource(R.drawable.im_gradient_premium),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(20.dp))
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(R.drawable.ic_premium_crown),
                contentDescription = null
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.padding(top = 18.dp, bottom = 20.dp, end = 20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Активувати Преміум",
                        style = LinguaTypography.subtitle2,
                        color = White
                    )
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.ic_circle_right),
                        tint = White,
                        contentDescription = null
                    )
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    "Користуйся всіма можливостями застосунку — без реклами та обмежень",
                    style = LinguaTypography.body5,
                    color = White
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfilePreview() {
    Column {
        LinguaTheme {
            ProfileScreen(
                ProfileViewState.Preview,
                {},
                actioner = { _ -> },
            )
        }
    }
}

