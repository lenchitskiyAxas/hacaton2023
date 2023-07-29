package ru.aries.hacaton.screens.module_main.new_wish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.ButtonWeakApp
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextButtonApp
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.GettingWishlist

class NewWish(private val idWishlist: Int? = null) : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<NewWishModel>()
        var id by rememberState { idWishlist }

        val idWishlist by model.idWishlist.collectAsState()
        val listWishlists by model.listWishlists.collectAsState()

        id?.let {
            NewWishListEmpty(
                onClickCancel = model::goToBackOnMain,
                onClickNewWish = {
                    model.addNewWishlist(it)
                    id = null
                }
            )
        } ?: run {
            NewWishScr(
                onClickCancel = model::goToBackOnMain,
                idWishlist = idWishlist,
                listWishlists = listWishlists,
            )
        }
    }

}

@Composable
private fun NewWishScr(
    idWishlist: Int?,
    listWishlists: List<GettingWishlist>,
    onClickCancel: () -> Unit,

    ) {

    val image = rememberState<String?> { null }
    val name = rememberState{ TextFieldValue() }
    val description = rememberState{ TextFieldValue() }
    val price = rememberState{ TextFieldValue() }
    val uriLink = rememberState{ TextFieldValue() }
    val chooseWishlist = rememberState{ listWishlists. firstOrNull { it.id == idWishlist }}


    val onSend = remember { {} }
    val enabledButton = remember { false }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelTopNewWish(
            onClickCancel = onClickCancel,
        )

        ContentEnterNewWish(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),


        )


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
                enabled = enabledButton,
                onClick = onSend,
                text = TextApp.titleCreate
            )
        }
    }
}

@Composable
private fun ColumnScope.ContentEnterNewWish(
    modifier:Modifier,



){


}



@Composable
private fun NewWishListEmpty(
    onClickCancel: () -> Unit,
    onClickNewWish: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelTopNewWishListEmpty(
            onClickCancel = onClickCancel,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextBodyLarge(
                textAlign = TextAlign.Center,
                text = TextApp.textItNoPhotosYet
            )
            BoxSpacer()
            TextButtonApp(onClick = onClickNewWish,
                text = TextApp.textAddOnePhoto,
                contentStart = {
                    IconApp(
                        modifier = Modifier.padding(end = DimApp.screenPadding * 0.3f),
                        painter = rememberImageRaw(id = R.raw.ic_cancel))
                }
            )
        }
    }
}

@Composable
private fun PanelTopNewWish(
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
            text = TextApp.textNewDesire
        )
    }
}

@Composable
private fun PanelTopNewWishListEmpty(
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
            IconApp(painter = rememberImageRaw(R.raw.ic_arrow_back))
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