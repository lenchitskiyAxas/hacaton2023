package ru.aries.hacaton.screens.module_registration.step_5_new_cell_2

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonAccentApp
import ru.aries.hacaton.base.common_composable.ButtonWeakApp
import ru.aries.hacaton.base.common_composable.FillLineHorizontal
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.PanelBottom
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextButtonStyle
import ru.aries.hacaton.base.common_composable.TextTitleLarge
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.models.api.CreatingFamilyMember
import ru.aries.hacaton.screens.module_registration.RegStepsModel
import ru.aries.hacaton.screens.module_registration.step_5_new_cell_1.ColumnContentNewCell

class StepFifthCreateNewCellSecond : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<RegStepsModel>()
        val familyMembers by model.familyMembers.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        StepFifthCreateNewCellFirstScr(onClickBack = model::goBackStack,
            onClickAdd = model::addFamilyMember,
            listNewDataMens = familyMembers,
            updateFamilyMembers = model::updateListFamilyMembers,
            onClickNextScreen = model::goToNewCellThird
            )
    }
}


@Composable
private fun StepFifthCreateNewCellFirstScr(
    onClickBack: () -> Unit,
    onClickAdd: () -> Unit,
    onClickNextScreen: () -> Unit,
    listNewDataMens: List<CreatingFamilyMember>,
    updateFamilyMembers: (List<CreatingFamilyMember>) -> Unit,
) {

    val checkData: Boolean = remember(
        listNewDataMens,
    ) { listNewDataMens.all { it.isFullForeSend() } }

    val nextStep = remember(checkData) {
        {
            if (checkData) {
                onClickNextScreen.invoke()
            }
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(ThemeApp.colors.backgroundVariant)
        .systemBarsPadding()
        .imePadding()) {
        PanelNavBackTop(onClickBack = onClickBack)
        TextButtonStyle(modifier = Modifier.padding(horizontal = DimApp.screenPadding),
            text = TextApp.formatStepFrom(4, 4))
        Column(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(durationMillis = 300))) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding)) {
                    TextTitleLarge(text = TextApp.textCreateACell)
                    TextBodyLarge(text = TextApp.textEnterChildren)
                }
                BoxSpacer()

                listNewDataMens.forEachIndexed { index, item ->
                    IconButtonApp(modifier = Modifier.align(Alignment.End), onClick = {
                        updateFamilyMembers.invoke(listNewDataMens.filterIndexed { indexData, _ ->
                            indexData != index
                        })
                    }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_delete),
                            tint = ThemeApp.colors.attentionContent)
                    }
                    ColumnContentNewCell(
                        firstName = item.first_name,
                        lastName = item.last_name,
                        patronymic = item.patronymic,
                        birthdate = item.birthdate,
                        maidenName = item.maiden_name,
                        roleFamily = item.convertInEnumRoleFamily(),
                        gender = item.convertInEnumGender(),
                        setFirstName = {
                            updateFamilyMembers.invoke(updateNewDataMens(index = index,
                                oldList = listNewDataMens,
                                newUpdate = item.copy(first_name = it)))
                        },
                        setLastName = {
                            updateFamilyMembers.invoke(updateNewDataMens(index = index,
                                oldList = listNewDataMens,
                                newUpdate = item.copy(last_name = it)))
                        },
                        setPatronymic = {
                            updateFamilyMembers.invoke(updateNewDataMens(index = index,
                                oldList = listNewDataMens,
                                newUpdate = item.copy(patronymic = it)))
                        },
                        setBirthdate = {
                            updateFamilyMembers.invoke(updateNewDataMens(index = index,
                                oldList = listNewDataMens,
                                newUpdate = item.copy(birthdate = it)))
                        },
                        setMaidenName = {
                            updateFamilyMembers.invoke(updateNewDataMens(index = index,
                                oldList = listNewDataMens,
                                newUpdate = item.copy(maiden_name = it)))
                        },
                        setGender = {
                            updateFamilyMembers.invoke(updateNewDataMens(index = index,
                                oldList = listNewDataMens,
                                newUpdate = item.copy(gender = it.numbGender)))
                        },
                    )
                    FillLineHorizontal(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding))
                }
            }

            ButtonWeakApp(modifier = Modifier.padding(DimApp.screenPadding),
                onClick = onClickAdd,
                text = TextApp.titleAdd,
                contentStart = {
                    IconApp(painter = rememberImageRaw(id = R.raw.ic_add))
                })

            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = nextStep,
                    text = TextApp.titleAdd)
            }

        }
    }
}

fun updateNewDataMens(
    index: Int, oldList: List<CreatingFamilyMember>, newUpdate: CreatingFamilyMember
): List<CreatingFamilyMember> {
    val oldListMut = oldList.toMutableList()
    return try {
        oldListMut[index] = newUpdate
        oldListMut
    } catch (e: Exception) {
        e.printStackTrace()
        oldListMut
    }
}