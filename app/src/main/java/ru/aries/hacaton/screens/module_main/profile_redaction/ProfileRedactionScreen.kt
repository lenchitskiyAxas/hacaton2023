package ru.aries.hacaton.screens.module_main.profile_redaction

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxImageLoad
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonWeakApp
import ru.aries.hacaton.base.common_composable.DialogCropImage
import ru.aries.hacaton.base.common_composable.DialogGetImage
import ru.aries.hacaton.base.common_composable.FillLineHorizontal
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.extension.clickableRipple
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState

class ProfileRedactionScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileRedactionModel>()
        val userData by model.userData.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        var imageUriCamera by rememberState<Uri?> { null }
        var getImage by rememberState { false }

        ProfileRedactionScr(
            onClickBack = model::goBackStack,
            imageUri = userData.avatar,
            onClickNewImage = { getImage = true },
            onClickPersonalData = model::goToPersonalData,
            onClickInterests = model::goToInterestsData,
            onClickFamily = model::goToFamilyData,
            onClickContacts = model::goToContactsData,
        )

        imageUriCamera?.let {
            DialogCropImage(
                onDismiss = { imageUriCamera = null },
                uriImage = it,
                onImageCroppedUri = model::uploadPhoto,
            )
        }

        if (getImage) {
            DialogGetImage(
                onDismiss = { getImage = false },
                uploadPhoto = {
                    imageUriCamera = it
                    getImage = false
                },
            )
        }
    }
}

@Composable
private fun ProfileRedactionScr(
    onClickBack: () -> Unit,
    onClickNewImage: () -> Unit,
    onClickPersonalData: () -> Unit,
    onClickInterests: () -> Unit,
    onClickFamily: () -> Unit,
    onClickContacts: () -> Unit,
    imageUri: Any?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(
            modifier = Modifier.shadow(DimApp.shadowElevation),
            onClickBack = onClickBack,
            text = TextApp.titleEditProfile
        )
        BoxSpacer()
        Column(modifier = Modifier.padding(horizontal = DimApp.screenPadding)) {
            TextBodyMedium(text = TextApp.textPhoto)
            BoxSpacer(0.5f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoxImageLoad(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(DimApp.imageAvatarSize),
                    image = imageUri,
                    drawableError = R.drawable.stab_avatar,
                    drawablePlaceholder = R.drawable.stab_avatar
                )
                BoxSpacer()
                ButtonWeakApp(onClick = onClickNewImage, text = TextApp.titleChange)
            }
        }
        BoxSpacer()
        FillLineHorizontal(modifier = Modifier.fillMaxWidth())
        ButtonInRow(
            onClick = onClickPersonalData,
            text = TextApp.titlePersonalData
        )
        ButtonInRow(
            onClick = onClickInterests,
            text = TextApp.textInterests
        )

        ButtonInRow(
            onClick = onClickFamily,
            text = TextApp.holderFamily
        )

        ButtonInRow(
            onClick = onClickContacts,
            text = TextApp.holderContacts
        )
    }
}

@Composable
private fun ButtonInRow(
    onClick: () -> Unit,
    text: String,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimApp.heightButtonInLine)
            .clickableRipple {
                onClick.invoke()
            }
            .padding(start = DimApp.screenPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        TextBodyMedium(
            maxLines = 1,
            text = text
        )

        BoxFillWeight()

        IconButtonApp(
            modifier = Modifier,
            onClick = onClick
        ) {
            IconApp(painter = rememberImageRaw(R.raw.ic_chevron_right))
        }

    }


}