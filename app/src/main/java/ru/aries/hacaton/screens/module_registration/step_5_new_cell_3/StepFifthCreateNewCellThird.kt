package ru.aries.hacaton.screens.module_registration.step_5_new_cell_3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.FillLineHorizontal
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextTitleLarge
import ru.aries.hacaton.base.extension.toDateString
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.models.api.CreatingFamilyMember
import ru.aries.hacaton.models.api.Gender
import ru.aries.hacaton.screens.module_registration.RegStepsModel

class StepFifthCreateNewCellThird : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val userData by model.userData.collectAsState()
        val firstFamilyMember by model.firstFamilyMember.collectAsState()
        val familyMembers by model.familyMembers.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        StepFifthCreateNewCellFirstScr(
            onClickBack = model::goBackStack,
            onClickFinish = model::finishAddNewFamilyCell,
            userGender = userData.convertInEnumGender(),
            nameUser = userData.getFullName(),
            birthdayUser = userData.birthdate?.toDateString() ?: "",
            nameFirstFamilyMember = firstFamilyMember.getFullName(),
            birthdayFirstFamilyMemberName = firstFamilyMember.birthdate?.toDateString() ?: "",
            familyMembers = familyMembers,
        )
    }
}

@Composable
private fun StepFifthCreateNewCellFirstScr(
    onClickBack: () -> Unit,
    onClickFinish: () -> Unit,
    userGender: Gender,
    nameUser: String,
    birthdayUser: String,
    nameFirstFamilyMember: String,
    birthdayFirstFamilyMemberName: String,
    familyMembers: List<CreatingFamilyMember>,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.backgroundVariant)
            .systemBarsPadding()
            .imePadding()
    ) {
        PanelNavBackTop(onClickBack = onClickBack)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding)) {
                TextTitleLarge(text = TextApp.textYourFamilyUnit)
                TextBodyLarge(text = TextApp.textCheckYourFamilyDetails)
            }

            CompletedMapFamily(
                title = TextApp.formatSomethingYou(userGender.getGenderTextShort()),
                name = nameUser,
                gender = null,
                birthday = birthdayUser
            )
            FillLineHorizontal(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding))

            CompletedMapFamily(
                title = userGender.getGenderYourSatellite().getGenderTextShort(),
                name = nameFirstFamilyMember,
                gender = null,
                birthday = birthdayFirstFamilyMemberName
            )
            FillLineHorizontal(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding))

            BoxSpacer(.5f)
            TextBodyMedium(
                modifier = Modifier
                    .padding(horizontal = DimApp.screenPadding),
                text = TextApp.textChildren)
            familyMembers.forEachIndexed { index, member ->
                CompletedMapFamily(
                    title = null,
                    name = member.getFullName(),
                    gender = member.convertInEnumGender().getGenderText(),
                    birthday = member.birthdate?.toDateString() ?:""
                )
                if ((familyMembers.size - 1) > index) {
                    FillLineHorizontal(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding))
                }
            }
            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
                    onClick = onClickFinish,
                    text = TextApp.titleAdd)
            }
        }
    }
}

@Composable
private fun CompletedMapFamily(
    title: String?,
    name: String,
    gender: String?,
    birthday: String,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DimApp.screenPadding)
    ) {
        title?.let {
            BoxSpacer(.5f)
            TextBodyMedium(text = title)
        }

        BoxSpacer(.5f)
        Row(modifier = Modifier.fillMaxWidth()) {
            TextBodyMedium(
                modifier = Modifier.fillMaxWidth(.35f),
                text = TextApp.holderName,
                color = ThemeApp.colors.textLight)
            TextBodyLarge(text = name)

        }

        gender?.let {
            BoxSpacer(.5f)
            Row(modifier = Modifier.fillMaxWidth()) {
                TextBodyMedium(
                    modifier = Modifier.fillMaxWidth(.35f),
                    text = TextApp.textGender_,
                    color = ThemeApp.colors.textLight)
                TextBodyLarge(text = gender)
            }
        }
        BoxSpacer(.5f)
        Row(modifier = Modifier.fillMaxWidth()) {
            TextBodyMedium(
                modifier = Modifier.fillMaxWidth(.35f),
                text = TextApp.textBirthDay,
                color = ThemeApp.colors.textLight)
            TextBodyLarge(text = birthday)
        }
    }
}