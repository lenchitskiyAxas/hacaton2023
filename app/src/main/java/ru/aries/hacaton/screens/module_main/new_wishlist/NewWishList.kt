package ru.aries.hacaton.screens.module_main.new_wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.ButtonWeakApp
import ru.aries.hacaton.base.common_composable.CheckerApp
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextFieldOutlinesApp
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState

class NewWishList() : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<NewWishListModel>()
        NewWishListScr(
            onClickCancel = model::goBackStack,
            onCreateNewWishList = model::createNewWishList,
        )
    }
}

@Composable
private fun NewWishListScr(
    onClickCancel: () -> Unit,
    onCreateNewWishList: (
        String,
        Boolean
    ) -> Unit,
) {

    val focus = LocalFocusManager.current
    var titleText by rememberState { TextFieldValue() }
    var isPrivate by rememberState { false }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelTopNewWishList(
            onClickCancel = onClickCancel,
        )

        TextBodyMedium(
            modifier = Modifier.padding(DimApp.screenPadding),
            text = TextApp.textNameTitle
        )

        TextFieldOutlinesApp(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding),
            value = titleText,
            onValueChange = { titleText = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focus.clearFocus()
                }
            ),
        )
        BoxSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = DimApp.screenPadding),
        ) {
            CheckerApp(checked = isPrivate, onCheckedChange = { isPrivate = !isPrivate })
            Column(
                modifier = Modifier.weight(1f),
            ) {
                TextBodyLarge(text = TextApp.textWishlistSecret)
                TextBodyMedium(
                    text = TextApp.textDescriptionWishlistSecret,
                    color = ThemeApp.colors.textLight
                )
            }
        }

        BoxFillWeight()
        PanelBottom {
            ButtonWeakApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = DimApp.screenPadding),
                onClick = onClickCancel,
                text = TextApp.titleCancel
            )
            BoxSpacer()
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = DimApp.screenPadding),
                enabled = titleText.text.isNotEmpty(),
                onClick = {
                    onCreateNewWishList.invoke(
                        titleText.text,
                        isPrivate
                    )
                },
                text = TextApp.titleCreate
            )
        }
    }
}


@Composable
private fun PanelTopNewWishList(
    onClickCancel: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightTopNavigationPanel)
            .shadow(elevation = DimApp.shadowElevation)
            .background(ThemeApp.colors.backgroundVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButtonApp(
            modifier = Modifier
                .padding(horizontal = DimApp.screenPadding * .5f),
            onClick = onClickCancel
        ) {
            IconApp(painter = rememberImageRaw(R.raw.ic_close))
        }

        TextBodyLarge(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Start,
            text = TextApp.textNewWishlist
        )
    }
}