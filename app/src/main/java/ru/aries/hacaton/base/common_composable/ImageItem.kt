package ru.aries.hacaton.base.common_composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import ru.aries.hacaton.R
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.rememberState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageItem(
    padding: PaddingValues,
    onPhotoSelected: () -> Unit,
    onLongPhotoSelected: () -> Unit,
    isSelected: Boolean,
    isSelectedMode: Boolean = false,
    image: Any?//todo
) {
    var tintValue by rememberState { .5f }
    LaunchedEffect(isSelected) {
        tintValue = if (isSelected)
            .1f
        else
            .5f
    }
    Column() {
        BoxSpacer(.5f)
        BoxImageLoad(
            modifier = Modifier
                .padding(padding)
                .aspectRatio(1f)
                .clip(ThemeApp.shape.smallAll)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = true, radius = Dp.Unspecified, color = Color.Unspecified
                    ),
                    onLongClick = onLongPhotoSelected,
                    onClick = onPhotoSelected,
                ),
            drawableError = R.drawable.stub_photo,
            drawablePlaceholder = R.drawable.stub_photo,
            image = image,
        ) {
            if (isSelectedMode) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(DimApp.starsPadding)
                        .size(DimApp.iconSizeStandard)
                        .clip(CircleShape)
                        .border(
                            width = DimApp.lineWidthBorderProfile,
                            color = ThemeApp.colors.backgroundVariant,
                            shape = CircleShape
                        )
                        .background(ThemeApp.colors.onBackground.copy(tintValue))
                        .padding(DimApp.lineWidthBorderProfile),
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(ThemeApp.colors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            BoxImageLoad(
                                image = R.raw.ic_check,
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = ColorFilter.tint(
                                    color = ThemeApp.colors.container,
                                )
                            )
                        }
                    }

                }
            }
        }
    }
}