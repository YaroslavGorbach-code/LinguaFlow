package com.korop.yaroslavhorach.settings

import android.content.ActivityNotFoundException
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.korop.yaroslavhorach.common.utill.buildEmailIntent
import com.korop.yaroslavhorach.common.utill.buildRateAppIntent
import com.korop.yaroslavhorach.common.utill.buildRateAppWebIntent
import com.korop.yaroslavhorach.designsystem.R
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons
import com.korop.yaroslavhorach.designsystem.theme.graphics.LinguaIcons.CircleClose
import com.korop.yaroslavhorach.designsystem.theme.onBackgroundDark
import com.korop.yaroslavhorach.designsystem.theme.primaryIcon
import com.korop.yaroslavhorach.designsystem.theme.secondaryIcon
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.settings.model.SettingsAction
import com.korop.yaroslavhorach.settings.model.SettingsItemType
import com.korop.yaroslavhorach.settings.model.SettingsItemUi
import com.korop.yaroslavhorach.settings.model.SettingsSectionUi
import com.korop.yaroslavhorach.settings.model.SettingsUiMessage
import com.korop.yaroslavhorach.settings.model.SettingsViewState
import com.korop.yaroslavhorach.ui.utils.conditional
import kotlinx.coroutines.launch

@Composable
internal fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    state.uiMessage?.let { uiMessage ->
        viewModel.clearMessage(uiMessage.id)

        when (uiMessage.message) {
            is SettingsUiMessage.ToGiveFeedback -> {
                val emailIntent = buildEmailIntent("yaroslavgorbach2@gmail.com")
                if (emailIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(emailIntent)
                } else {
                    Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                }
            }
            is SettingsUiMessage.ToRateApp -> {
                try {
                    context.startActivity(buildRateAppIntent())
                } catch (e: ActivityNotFoundException) {
                    context.startActivity(buildRateAppWebIntent())
                }
            }
            is SettingsUiMessage.ShowChooseLanguageBottomSheet -> {
                coroutineScope.launch {
                    sheetState.show()
                }
            }
            is SettingsUiMessage.ShowDailyTrainingChangesToast -> {
                Toast.makeText(context,
                    stringResource(com.korop.yaroslavhorach.profile.R.string.settings_daily_challange_changes_toast_text), Toast.LENGTH_LONG).show()
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            LanguagesSheetContent(state.languages, state.currentLanguage) { language ->
                coroutineScope.launch {
                    sheetState.hide()
                    viewModel.submitAction(SettingsAction.OnLanguageSelected(language))
                }
            }
        },
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
    ) {
        SettingsScreen(
            state = state,
            actioner = { action ->
                when (action) {
                    is SettingsAction.OnBackClicked -> navigateBack()
                    else -> viewModel.submitAction(action)
                }
            })
    }
}

@Composable
internal fun SettingsScreen(
    state: SettingsViewState,
    actioner: (SettingsAction) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        Spacer(Modifier.height(20.dp))
        TopBar(actioner)
        LazyColumn {
            itemsIndexed(state.sections) { index, item ->
                Spacer(Modifier.height(20.dp))
                SettingsSection(state, item, actioner)
            }
        }
    }
}

@Composable
private fun SettingsSection(
    state: SettingsViewState,
    sectionItem: SettingsSectionUi,
    actioner: (action: SettingsAction) -> Unit
) {
    Text(
        text = sectionItem.title.asString(),
        color = MaterialTheme.colorScheme.typoPrimary(),
        style = LinguaTypography.h5
    )
    Spacer(Modifier.height(14.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackgroundDark(),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        sectionItem.items.forEachIndexed { index, item ->
            if (index > 0) {
                Divider(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onBackgroundDark())
            }
            DetailsItem(state, item, index, sectionItem, actioner)
        }
    }
}

@Composable
private fun DetailsItem(
    state: SettingsViewState,
    item: SettingsItemUi,
    index: Int,
    sectionItem: SettingsSectionUi,
    actioner: (action: SettingsAction) -> Unit
) {
    val subtitle = item.subtitle.asString()

    Row(modifier = Modifier
        .conditional(index == 0) {
            clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
        }
        .conditional(index == sectionItem.items.lastIndex) {
            clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
        }
        .clickable {
            actioner(SettingsAction.OnSettingsItemClicked(item.type))
        }, verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .conditional(index > 0) {
                    padding(top = 12.dp)
                }
                .conditional(index == sectionItem.items.lastIndex) {
                    clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                }
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.conditional(index == 0) { padding(top = 12.dp) },
                text = item.title.asString(),
                color = MaterialTheme.colorScheme.typoPrimary(),
                style = LinguaTypography.subtitle3
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .weight(1f),
                    text = subtitle,
                    color = MaterialTheme.colorScheme.typoPrimary(),
                    style = LinguaTypography.body4
                )
            }
        }
        when (item.type) {
            SettingsItemType.CHANGE_LANGUAGE,
            SettingsItemType.RATE,
            SettingsItemType.FEEDBACK -> {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_arrow_right),
                    tint = MaterialTheme.colorScheme.primaryIcon(),
                    contentDescription = null
                )
            }

            SettingsItemType.ACTIVATE_DAILY_MIX -> {
                Switch(checked = state.isMixTrainingAvailable, onCheckedChange = {
                    actioner(SettingsAction.OnSettingsItemChecked(item.type, it))
                })
            }

            SettingsItemType.ACTIVATE_15_MINUTES_TOPIC -> {
                Switch(checked = state.is15MinutesTrainingAvailable, onCheckedChange = {
                    actioner(SettingsAction.OnSettingsItemChecked(item.type, it))
                })
            }
        }

        Spacer(Modifier.width(12.dp))
    }
}

@Composable
private fun TopBar(
    actioner: (action: SettingsAction) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .size(34.dp)
                .clickable { actioner(SettingsAction.OnBackClicked) },
            tint = MaterialTheme.colorScheme.secondaryIcon(),
            painter = painterResource(LinguaIcons.Close),
            contentDescription = null
        )
        Spacer(Modifier.width(20.dp))
        Text(
            stringResource(com.korop.yaroslavhorach.profile.R.string.settings_toolbar_title_text),
            style = LinguaTypography.h4,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.typoPrimary()
        )
    }
}

@Preview
@Composable
private fun AvatarChangePreview() {
    LinguaBackground {
        SettingsScreen(
            SettingsViewState.Preview,
            actioner = { _ -> },
        )
    }
}