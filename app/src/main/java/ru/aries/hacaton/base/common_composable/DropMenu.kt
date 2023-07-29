package ru.aries.hacaton.base.common_composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import ru.aries.hacaton.R
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.keyboardAsState
import ru.aries.hacaton.base.util.modifyList
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.City
import ru.aries.hacaton.models.api.GettingInterest

/**
 * ```
 *       TextDropMenu(
 *       modifier = Modifier,
 *       itemTarget = if (genderEnter.value == null) null else
 *       listGender.firstOrNull { it == genderEnter.value },
 *       items = Gender.values().toList(),
 *       fieldHolder = { item, expanded ->
 *          TextFieldOutlinesApp(
 *              modifier = Modifier.fillMaxWidth(),
 *              value = TextFieldValue(item?.getGenderText() ?: ""),
 *              onValueChange = {},
 *              readOnly = true,
 *              singleLine = true,
 *              placeholder = {
 *                  Text(text = TextApp.textNotSpecified)},
 *              trailingIcon = {
 *                  IconButton(onClick = expanded) {
 *                      IconApp(painter = rememberImageRaw(resImage = R.raw.ic_drop))}},)},
 *      checkItem = onClickSetGender,
 *      itemDropMenu = {
 *          TextBodyMedium(
 *              modifier = Modifier.fillMaxWidth(),
 *              text = it.getGenderText())})
 *```
 */
@Composable
fun <T> DropMenuApp(
    items: List<T>,
    fieldHolder: @Composable (((T?), expanded: () -> Unit) -> Unit),
    itemDropMenu: @Composable ((T) -> Unit),
    modifier: Modifier = Modifier,
    containerDropMenu: Color = ThemeApp.colors.background,
    paddingHorizontal: Dp = 0.dp,
    itemTarget: T? = null,
    take: Int = Int.MAX_VALUE,
    ratioHeightDrop: Float = .4f,
    checkItem: (T) -> Unit = { _ -> }
) {
    val maxHeight = LocalConfiguration.current.screenHeightDp * ratioHeightDrop
    val focus = LocalFocusManager.current
    var expanded by rememberState { false }
    val expandedInvoke: () -> Unit = remember(expanded) {
        {
            expanded = !expanded
        }
    }
    var checkValue by remember(itemTarget) { mutableStateOf(itemTarget) }
    var dropDownWidth by remember { mutableStateOf(0) }

    Box(modifier = modifier.wrapContentSize()) {
        Box(
            modifier = Modifier
                .padding(paddingHorizontal)
                .onFocusChanged { expanded = it.hasFocus }
                .onSizeChanged { dropDownWidth = it.width },
        ) {
            fieldHolder(checkValue, expandedInvoke)
        }
        DropdownMenu(
            modifier = Modifier
                .background(containerDropMenu)
                .heightIn(max = maxHeight.dp)
                .width(with(LocalDensity.current) { dropDownWidth.toDp() }),
            properties = PopupProperties(focusable = false),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            items.take(take).forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        checkItem(item)
                        checkValue = item
                        expanded = false
                        focus.clearFocus()
                    },
                    text = { itemDropMenu(item) })
            }
        }
    }
}

@Composable
fun DropMenuCities(
    items: List<City>,
    modifier: Modifier = Modifier,
    containerDropMenu: Color = ThemeApp.colors.background,
    paddingHorizontal: Dp = 0.dp,
    take: Int = Int.MAX_VALUE,
    locationChooser: String?,
    enterNameLocation: (String?) -> Unit,
    onClickClear: () -> Unit,
    checkItem: (City?) -> Unit
) {

    val confManager = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val isImeOpen = keyboardAsState()

    val maxHeight =
        rememberState(isImeOpen.value) { confManager.screenHeightDp * (if (!isImeOpen.value) .4f else .2f) }
    var expanded by rememberState { false }
    var location by rememberState(locationChooser) { TextFieldValue(locationChooser ?:"") }
    var dropDownWidth by rememberState { 0 }

    Box(modifier = modifier.wrapContentSize()) {
        Box(
            modifier = Modifier
                .padding(paddingHorizontal)
                .onSizeChanged { dropDownWidth = it.width },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier
                    .onFocusChanged {
                        expanded = it.hasFocus
                    }
                    .fillMaxWidth(),
                value = location,
                onValueChange = {
                    location = it
                    enterNameLocation.invoke(it.text)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                placeholder = {
                    Text(text = TextApp.textNotSpecified)
                },
                trailingIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        onClickClear.invoke()
                        enterNameLocation.invoke(null)
                        expanded = false
                        location = TextFieldValue("")

                    }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_cancel))
                    }
                },
            )
        }
        DropdownMenu(
            modifier = Modifier
                .background(containerDropMenu)
                .heightIn(max = maxHeight.value.dp)
                .width(with(LocalDensity.current) { dropDownWidth.toDp() }),
            properties = PopupProperties(
                focusable = false,
                dismissOnClickOutside = false,
            ),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            items.take(take).forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        focusManager.clearFocus()
                        location = TextFieldValue(item.name)
                        checkItem.invoke(item)
                        expanded = false
                    },
                    text = {
                        Column() {
                            TextBodyLarge(text = item.name)
                            FillLineHorizontal(modifier = Modifier.fillMaxWidth())
                        }
                    })
            }
        }
    }
}


@Composable
fun DropMenuInterestChips(
    items: List<GettingInterest>,
    itemsHave: MutableState<List<String>>,
    onEnterText: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerDropMenu: Color = ThemeApp.colors.background,
    paddingHorizontal: Dp = 0.dp,
) {

    val confManager = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val isImeOpen = keyboardAsState()

    val maxHeight =
        rememberState(isImeOpen.value) { confManager.screenHeightDp * (if (!isImeOpen.value) .4f else .2f) }
    var expanded by rememberState { false }
    var testEnter by rememberState { TextFieldValue() }
    var dropDownWidth by rememberState { 0 }

    Box(modifier = modifier.wrapContentSize()) {
        Box(
            modifier = Modifier
                .padding(paddingHorizontal)
                .onSizeChanged { dropDownWidth = it.width },
        ) {
            TextFieldOutlinesApp(
                modifier = Modifier
                    .onFocusChanged {
                        expanded = it.hasFocus
                    }
                    .fillMaxWidth(),
                value = testEnter,
                onValueChange = {
                    testEnter = it
                    onEnterText.invoke(it.text)
                },
                singleLine = true,
                placeholder = {
                    Text(text = TextApp.textAddInterests)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            expanded = false
                            onEnterText.invoke("")
                            testEnter = TextFieldValue("")
                        }) {
                        IconApp(painter = rememberImageRaw(id = R.raw.ic_drop))
                    }
                },
            )
        }
        DropdownMenu(
            modifier = Modifier
                .background(containerDropMenu)
                .heightIn(max = maxHeight.value.dp)
                .width(with(LocalDensity.current) { dropDownWidth.toDp() }),
            properties = PopupProperties(
                focusable = false,
                dismissOnClickOutside = false,
            ),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        focusManager.clearFocus()
                        itemsHave.value = modifyList(itemsHave.value, item.name)
                        onEnterText.invoke("")
                        expanded = false
                    },
                    text = {
                        Column() {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                CheckerApp(
                                    checked = itemsHave.value.contains(item.name),
                                    onCheckedChange = {
                                        itemsHave.value = modifyList(itemsHave.value, item.name)
                                    })
                                TextBodyLarge(text = item.name)
                            }
                            FillLineHorizontal(modifier = Modifier.fillMaxWidth())
                        }
                    })
            }
        }
    }
}


