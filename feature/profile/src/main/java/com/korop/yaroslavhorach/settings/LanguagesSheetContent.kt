package com.korop.yaroslavhorach.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.korop.yaroslavhorach.designsystem.theme.LinguaTypography
import com.korop.yaroslavhorach.designsystem.theme.components.LinguaBackground
import com.korop.yaroslavhorach.designsystem.theme.divider
import com.korop.yaroslavhorach.designsystem.theme.primaryIcon
import com.korop.yaroslavhorach.designsystem.theme.typoPrimary
import com.korop.yaroslavhorach.profile.R
import com.korop.yaroslavhorach.settings.model.Language


@Composable
fun LanguagesSheetContent(
    languages: List<Language>, currentLanguage: String, onLanguageSelected: (Language) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            text = stringResource(R.string.choose_language_tilte_text),
            color = MaterialTheme.colorScheme.typoPrimary(),
            style = LinguaTypography.subtitle2
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.divider())
        )

        LazyColumn(content = {
            itemsIndexed(languages, key = { _, lang -> lang.value }) { index, language ->
                Column(
                    Modifier
                        .clickable { onLanguageSelected(language) }
                        .padding(horizontal = 20.dp)) {
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = language.nativeDisplayName,
                                style = LinguaTypography.subtitle3,
                                color = MaterialTheme.colorScheme.typoPrimary()
                            )
                            Text(
                                text = language.getLocalisedDisplayName(LocalContext.current),
                                style = LinguaTypography.body3,
                                color = MaterialTheme.colorScheme.typoPrimary()
                            )
                        }
                        if (currentLanguage == language.value) {
                            Icon(
                                modifier = Modifier
                                    .align(CenterVertically)
                                    .size(24.dp),
                                painter = painterResource(id = com.korop.yaroslavhorach.designsystem.R.drawable.ic_check_circle_checked),
                                tint = MaterialTheme.colorScheme.primaryIcon(),
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (languages.lastIndex != index) {
                        Spacer(
                            modifier = Modifier
                                .height(0.33.dp)
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.divider())
                        )
                    }
                }
            }
        })
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview
@Composable
private fun PreviewSheet() {
    LinguaBackground {
        LanguagesSheetContent(listOf(Language("uk"), Language("en")), "en") {}
    }
}