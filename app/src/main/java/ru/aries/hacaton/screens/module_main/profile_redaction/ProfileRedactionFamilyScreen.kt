package ru.aries.hacaton.screens.module_main.profile_redaction

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.logE
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.CreatingFamilyMember
import ru.aries.hacaton.models.api.RoleFamily
import ru.aries.hacaton.screens.module_registration.step_5_new_cell_1.ColumnContentNewCell
import ru.aries.hacaton.screens.module_registration.step_5_new_cell_2.updateNewDataMens

class ProfileRedactionFamilyScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileRedactionModel>()
        val familyMembers by model.familyMembers.collectAsState()
        val familyError by model.familyError.collectAsState()
        BackPressHandler(onBackPressed = model::goBackStack)
        LaunchedEffect(key1 = Unit, block = {
            model.getFamily()
        })
        ProfileRedactionFamilyScr(
            onClickBack = model::goBackStack,
            onClickSave = model::saveFamily,
            familyError = familyError,
            listDataMans = familyMembers,
        )
    }
}

@Composable
private fun ProfileRedactionFamilyScr(
    onClickBack: () -> Unit,
    familyError: Boolean,
    onClickSave: (List<CreatingFamilyMember>) -> Unit,
    listDataMans: List<CreatingFamilyMember>,
) {

    var listNewDataMans by rememberState(listDataMans) {
        logE(listDataMans.size)
        listDataMans
    }

    val onClickAdd: (RoleFamily) -> Unit = remember {
        {
            listNewDataMans = listNewDataMans + CreatingFamilyMember(role = it.numbRole)
        }
    }

    val checkData: Boolean = remember(
        listNewDataMans, listDataMans
    ) {
        val isFull = listNewDataMans.all { it.isFullForeSend() }
        val isChange = listDataMans != listNewDataMans
        isFull && isChange
    }

    val onSave = remember(checkData) {
        {
            if (checkData) {
                    onClickSave.invoke(listNewDataMans.subtract(listDataMans.toSet()).toList())
            }
        }
    }

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
            text = TextApp.holderFamily
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = tween(durationMillis = 300))
            ) {
                BoxSpacer()
                if (familyError && listNewDataMans.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center) {
                        TextBodyMedium(
                            color = ThemeApp.colors.attentionContent,
                            text = TextApp.textDidNotChooseAFamily)
                    }
                }
                listNewDataMans.forEachIndexed { index, item ->

                    Row(
                        modifier = Modifier
                            .padding(start = DimApp.screenPadding)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TextBodyLarge(text = item.getEnterDataYourSatellite())
                        BoxFillWeight()
                        if (item.convertInEnumRoleFamily() != RoleFamily.SPOUSE
                            && item.convertInEnumRoleFamily() != RoleFamily.SELF) {
                            IconButtonApp(modifier = Modifier, onClick = {
                                listNewDataMans = listNewDataMans.filterIndexed { indexData, _ ->
                                    indexData != index
                                }
                            }) {
                                IconApp(
                                    painter = rememberImageRaw(id = R.raw.ic_delete),
                                    tint = ThemeApp.colors.attentionContent
                                )
                            }
                        }
                    }
                    BoxSpacer(.5f)
                    ColumnContentNewCell(
                        firstName = item.first_name,
                        lastName = item.last_name,
                        patronymic = item.patronymic,
                        birthdate = item.birthdate,
                        maidenName = item.maiden_name,
                        roleFamily = item.convertInEnumRoleFamily(),
                        gender = item.convertInEnumGender(),
                        setFirstName = {
                            listNewDataMans = updateNewDataMens(
                                index = index,
                                oldList = listNewDataMans,
                                newUpdate = item.copy(first_name = it)
                            )
                        },
                        setLastName = {
                            listNewDataMans =
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(last_name = it)
                                )

                        },
                        setPatronymic = {
                            listNewDataMans =
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(patronymic = it)
                                )

                        },
                        setBirthdate = {
                            listNewDataMans =
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(birthdate = it)
                                )

                        },
                        setMaidenName = {
                            listNewDataMans =
                                updateNewDataMens(
                                    index = index,
                                    oldList = listNewDataMans,
                                    newUpdate = item.copy(maiden_name = it)
                                )

                        },
                        setGender = {
                            if (item.convertInEnumRoleFamily() != RoleFamily.SPOUSE
                                && item.convertInEnumRoleFamily() != RoleFamily.SELF) {
                                listNewDataMans =
                                    updateNewDataMens(
                                        index = index,
                                        oldList = listNewDataMans,
                                        newUpdate = item.copy(gender = it.numbGender)
                                    )
                            }
                        },
                    )
                    FillLineHorizontal(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = DimApp.screenPadding)
                    )
                }
            }


            ButtonWeakApp(modifier = Modifier.padding(DimApp.screenPadding),
                onClick = { onClickAdd.invoke(RoleFamily.CHILD) },
                text = RoleFamily.CHILD.getTextAddMembers(),
                contentStart = {
                    IconApp(painter = rememberImageRaw(id = R.raw.ic_add))
                })

//            ButtonWeakApp(modifier = Modifier.padding(DimApp.screenPadding),
//                onClick = { onClickAdd.invoke(RoleFamily.PARENT) },
//                text = RoleFamily.PARENT.getTextAddMembers(),
//                contentStart = {
//                    IconApp(painter = rememberImageRaw(id = R.raw.ic_add))
//                })
//
//            ButtonWeakApp(modifier = Modifier.padding(DimApp.screenPadding),
//                onClick = { onClickAdd.invoke(RoleFamily.SIBLING) },
//                text = RoleFamily.SIBLING.getTextAddMembers(),
//                contentStart = {
//                    IconApp(painter = rememberImageRaw(id = R.raw.ic_add))
//                })

            BoxFillWeight()
            PanelBottom {
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DimApp.screenPadding),
                    enabled = checkData,
                    onClick = onSave,
                    text = TextApp.holderSave
                )
            }
        }
    }
}