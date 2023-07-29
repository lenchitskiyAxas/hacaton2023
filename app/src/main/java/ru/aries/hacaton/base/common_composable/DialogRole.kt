package ru.aries.hacaton.base.common_composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import ru.aries.hacaton.base.extension.clickableRipple
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.Gender
import ru.aries.hacaton.models.api.RoleFamily


@Composable
fun DialogRole(
    roleEnter: RoleFamily?,
    setRoleFamily: (RoleFamily) -> Unit,
    onDismiss: () -> Unit,

    ){
    var roleSet by rememberState { roleEnter }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(horizontal = DimApp.screenPadding)
    ) {
        BoxSpacer()
        TextBodyLarge(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = TextApp.textSpecifyGender
        )
        BoxSpacer(2f)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ThemeApp.shape.smallAll)
                .clickableRipple {
                    roleSet = RoleFamily.SPOUSE
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadioButtonApp(
                checked = roleSet == RoleFamily.SPOUSE,
                onCheckedChange = { roleSet = RoleFamily.SPOUSE },
            )

            TextBodyLarge(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                text = Gender.MAN.getGenderText()
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ThemeApp.shape.smallAll)
                .clickableRipple {
                    roleSet = RoleFamily.CHILD
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButtonApp(
                checked = roleSet == RoleFamily.CHILD,
                onCheckedChange = { roleSet = RoleFamily.CHILD },
            )
            TextBodyLarge(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                text = Gender.WOMAN.getGenderText()
            )
        }
        BoxSpacer()
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonAccentTextApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = onDismiss,
                text = TextApp.titleCancel
            )
            BoxSpacer()
            ButtonAccentApp(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                enabled = roleSet != null,
                onClick = {
                    roleSet?.let { genderChoose ->
                        setRoleFamily.invoke(genderChoose)
                        onDismiss.invoke()
                    }
                },
                text = TextApp.titleNext
            )
        }
        BoxSpacer()
    }
}
