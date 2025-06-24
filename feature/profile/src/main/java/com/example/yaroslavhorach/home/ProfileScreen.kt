package com.example.yaroslavhorach.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.yaroslavhorach.common.utill.formatToShortDayOfWeek
import com.example.yaroslavhorach.common.utill.isToday
import com.example.yaroslavhorach.designsystem.R
import com.example.yaroslavhorach.designsystem.theme.Black_3
import com.example.yaroslavhorach.designsystem.theme.KellyGreen
import com.example.yaroslavhorach.designsystem.theme.LinguaTheme
import com.example.yaroslavhorach.designsystem.theme.LinguaTypography
import com.example.yaroslavhorach.designsystem.theme.White
import com.example.yaroslavhorach.designsystem.theme.components.BoxWithStripes
import com.example.yaroslavhorach.designsystem.theme.components.LinguaProgressBar
import com.example.yaroslavhorach.designsystem.theme.components.TextButton
import com.example.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.example.yaroslavhorach.designsystem.theme.typoControlSecondary
import com.example.yaroslavhorach.designsystem.theme.typoDisabled
import com.example.yaroslavhorach.designsystem.theme.typoPrimary
import com.example.yaroslavhorach.designsystem.theme.typoSecondary
import com.example.yaroslavhorach.home.model.ProfileAction
import com.example.yaroslavhorach.home.model.ProfileViewState
import com.example.yaroslavhorach.home.model.SpeakingLevel
import com.example.yaroslavhorach.ui.utils.conditional

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
        Spacer(Modifier.height(20.dp))
        SectionActivity(state)
        Spacer(Modifier.height(20.dp))
        SectionProgress(state)
        Spacer(Modifier.height(20.dp))
        SpeakerLevelProgress(state)
        Spacer(Modifier.height(14.dp))
        Achievements()
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun Achievements() {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Досягнення",
                color = MaterialTheme.colorScheme.typoPrimary(),
                style = LinguaTypography.subtitle2
            )

            TextButton(
                modifier = Modifier.weight(1f),
                text = "ПЕРЕГЛЯНУТИ ВСІ",
                textColor = MaterialTheme.colorScheme.typoDisabled(),
                onClick = {},
                alignment = Alignment.CenterEnd
            )
        }
        Spacer(Modifier.height(8.dp))
        Box {
            BoxWithStripes(
                stripeColor = Color.Transparent,
                isEnabled = false,
                rawShadowYOffset = 0.dp,
                background = Color.Transparent,
                contentPadding = 0.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.onBackgroundDark(), RoundedCornerShape(16.dp))
                ) {
                    val images = listOf(
                        R.drawable.im_achevement_1,
                        R.drawable.im_achevement_2,
                        R.drawable.im_achevement_3,
                        R.drawable.im_achevement_4
                    )
                    val borderColor = MaterialTheme.colorScheme.onBackgroundDark()

                    images.forEachIndexed { index, i ->
                        Image(
                            modifier = Modifier
                                .conditional(index % 2 == 0) {
                                    border(1.dp, borderColor, RoundedCornerShape(0.dp))
                                }
                                .padding(14.dp)
                                .weight(1f),
                            painter = painterResource(i),
                            contentDescription = null
                        )
                    }

                }
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "COMMING SOON",
                color = MaterialTheme.colorScheme.typoControlSecondary(),
                style = LinguaTypography.subtitle2
            )
        }
    }
}

@Composable
private fun SectionActivity(state: ProfileViewState) {
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = "Активність",
        color = MaterialTheme.colorScheme.typoPrimary(),
        style = LinguaTypography.subtitle2
    )
    Spacer(Modifier.height(14.dp))
    LastActiveDays(state)
    Spacer(Modifier.height(20.dp))
    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
        InfoItem(
            modifier = Modifier.weight(1f),
            title = state.activeDaysInRow.toString(),
            subtitle = "Активна Хвиля",
            iconRes = R.drawable.ic_fire_burning
        )
        Spacer(Modifier.width(20.dp))
        InfoItem(
            modifier = Modifier.weight(1f),
            title = state.activeDays.toString(),
            subtitle = "Активних днів",
            iconRes = R.drawable.ic_calendar
        )
    }
}

@Composable
private fun SectionProgress(state: ProfileViewState) {
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = "Прогресс",
        color = MaterialTheme.colorScheme.typoPrimary(),
        style = LinguaTypography.subtitle2
    )
    Spacer(Modifier.height(14.dp))
    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
        InfoItem(
            modifier = Modifier.weight(1f),
            title = state.experience.toString(),
            subtitle = "Рівень досвіду",
            iconRes = R.drawable.ic_lightning_thunder
        )
        Spacer(Modifier.width(20.dp))
        InfoItem(
            modifier = Modifier.weight(1f),
            title = state.levelOfSpeaking.level.toString() + ": " + state.levelOfSpeaking.title,
            subtitle = "Рівень мовлення",
            iconRes = R.drawable.ic_brain
        )
    }
}

@Composable
private fun SpeakerLevelProgress(state: ProfileViewState) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.onBackgroundDark(), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.levelOfSpeaking.level.toString(),
                color = MaterialTheme.colorScheme.typoPrimary(),
                style = LinguaTypography.h6
            )
            Spacer(Modifier.width(12.dp))
            LinguaProgressBar(
                state.experience.toFloat() / state.levelOfSpeaking.experienceRequired.last.toFloat(),
                modifier = Modifier
                    .weight(1f)
                    .height(18.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = SpeakingLevel.nextLevel(state.levelOfSpeaking.level)?.level.toString(),
                color = MaterialTheme.colorScheme.typoPrimary(),
                style = LinguaTypography.h6
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = state.levelOfSpeaking.description,
            color = MaterialTheme.colorScheme.typoPrimary(),
            style = LinguaTypography.body4,
            textAlign = TextAlign.Justify
        )
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun InfoItem(modifier: Modifier, title: String, subtitle: String, iconRes: Int) {
    BoxWithStripes(
        rawShadowYOffset = 0.dp,
        contentPadding = 12.dp,
        background = MaterialTheme.colorScheme.surface,
        backgroundShadow = MaterialTheme.colorScheme.onBackgroundDark(),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        borderColor = MaterialTheme.colorScheme.onBackgroundDark(),
        borderWidth = 1.dp,
        stripeWidth = 50.dp,
        stripeSpacing = 100.dp,
        stripeColor = Black_3
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier
                    .align(Alignment.Top)
                    .size(24.dp),
                painter = painterResource(iconRes),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.typoPrimary(),
                    style = LinguaTypography.subtitle2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.typoSecondary(),
                    style = LinguaTypography.body4,
                )
            }
        }
    }
}

@Composable
private fun LastActiveDays(state: ProfileViewState) {
    val listState = rememberLazyListState()
    val todayIndex = state.lasActiveDays.indexOfFirst { it.time.isToday() }

    LaunchedEffect(todayIndex) {
        if (todayIndex >= 0) {
            listState.scrollToItem(todayIndex)
        }
    }

    LazyRow(state = listState) {
        itemsIndexed(state.lasActiveDays) { index, item ->
            Spacer(Modifier.width(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onBackground, CircleShape)
                        .conditional(item.time.isToday()) {
                            border(1.dp, color = KellyGreen, CircleShape)
                        }
                ) {
                    if (item.isActive) {
                        Image(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(24.dp),
                            painter = painterResource(R.drawable.ic_fire_burning),
                            contentDescription = null
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(24.dp),
                            painter = painterResource(R.drawable.ic_fire),
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = item.time.formatToShortDayOfWeek(),
                    color = MaterialTheme.colorScheme.typoPrimary(),
                    style = LinguaTypography.body5
                )
            }
            if (index == state.lasActiveDays.lastIndex) {
                Spacer(Modifier.width(20.dp))
            }
        }
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

