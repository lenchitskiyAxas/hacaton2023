package ru.aries.hacaton.screens.module_main.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.launch
import ru.aries.hacaton.R
import ru.aries.hacaton.base.common_composable.BoxFillWeight
import ru.aries.hacaton.base.common_composable.BoxImageLoad
import ru.aries.hacaton.base.common_composable.BoxImageLoadSizeWidth
import ru.aries.hacaton.base.common_composable.BoxSpacer
import ru.aries.hacaton.base.common_composable.ButtonWeakApp
import ru.aries.hacaton.base.common_composable.DialogBottomSheet
import ru.aries.hacaton.base.common_composable.DialogDetailedInformation
import ru.aries.hacaton.base.common_composable.DialogGetImageList
import ru.aries.hacaton.base.common_composable.FillLineHorizontal
import ru.aries.hacaton.base.common_composable.IconApp
import ru.aries.hacaton.base.common_composable.IconButtonApp
import ru.aries.hacaton.base.common_composable.PanelNavBackTop
import ru.aries.hacaton.base.common_composable.ProgressIndicatorApp
import ru.aries.hacaton.base.common_composable.TextBodyLarge
import ru.aries.hacaton.base.common_composable.TextBodyMedium
import ru.aries.hacaton.base.common_composable.TextButtonApp
import ru.aries.hacaton.base.common_composable.TextCaption
import ru.aries.hacaton.base.common_composable.TextLabel
import ru.aries.hacaton.base.common_composable.TextLinksWeb
import ru.aries.hacaton.base.common_composable.TextTitleMedium
import ru.aries.hacaton.base.common_composable.TextTitleSmall
import ru.aries.hacaton.base.common_composable.colorsButtonAccentTextApp
import ru.aries.hacaton.base.extension.DriveShadow
import ru.aries.hacaton.base.extension.clickableRipple
import ru.aries.hacaton.base.extension.drawColoredShadow
import ru.aries.hacaton.base.extension.formatTimeElapsed
import ru.aries.hacaton.base.extension.toDateMillisToUnixStringFullMonth
import ru.aries.hacaton.base.res.DimApp
import ru.aries.hacaton.base.res.TextApp
import ru.aries.hacaton.base.theme.ThemeApp
import ru.aries.hacaton.base.util.BackPressHandler
import ru.aries.hacaton.base.util.getQualifiedName
import ru.aries.hacaton.base.util.rememberImageRaw
import ru.aries.hacaton.base.util.rememberModel
import ru.aries.hacaton.base.util.rememberState
import ru.aries.hacaton.models.api.GettingFamily
import ru.aries.hacaton.models.api.GettingFamilyMember
import ru.aries.hacaton.models.api.GettingMedia
import ru.aries.hacaton.models.api.GettingPost
import ru.aries.hacaton.models.api.GettingWish

class ProfileScreen : Screen {
    override val key: ScreenKey = keyName

    companion object {
        val keyName = object {}::class.getQualifiedName
    }

    @Composable
    override fun Content() {
        val model = rememberModel<ProfileModel>()
        val userData by model.userData.collectAsState()
        val cityUser by model.cityUser.collectAsState()
        val cityUserOrigin by model.cityUserOrigin.collectAsState()
        val listFamily by model.listFamily.collectAsState()
        val chooserFamily by model.chooserFamily.collectAsState()
        val menuRibbon by model.menuRibbon.collectAsState()
        val listMedia by model.listMedia.collectAsState()
        val listWishes by model.listWishes.collectAsState()
        val listPosts by model.listPosts.collectAsState()

        var getImage by rememberState { false }

        var focusDetails by rememberState { false }
        val focusManager = LocalFocusManager.current

        BackPressHandler(onBackPressed = model::goBackStack)
        ProfileScr(
            onClickBack = model::goBackStack,
            onClickChangeFamily = model::changeFamily,
            onClickSettings = { /**TODO()*/ },
            avatar = userData.avatar,
            name = userData.getNameAndLastName(),
            members = chooserFamily?.members ?: listOf(),
            listFamily = listFamily,
            chooserFamily = chooserFamily,
            listMedia = listMedia,
            listPosts = listPosts,
            listWishes = listWishes,
            locationCity = cityUser?.name,
            onClickDescriptions = { focusDetails = true },
            onClickChooseMenu = model::chooseMenu,
            onClickRedactionProfile = model::goToRedaction,
            onClickMember = { /**TODO()*/ },
            onClickAllFamily = { /**TODO()*/ },
            menuRibbon = menuRibbon,
            onClickAllMedia = model::goToAllMedia,
            onClickUploadAPhoto = { getImage = true },
            onClickOneMedia = { model.goToViewScreen(it.id) },
            onClickLike = { /**TODO()*/ },
            onClickLink = { /**TODO()*/ },
            onClickUser = { /**TODO()*/ },
            onClickModules = { /**TODO()*/ },
            onClickComment = { /**TODO()*/ },
            onClickShare = { /**TODO()*/ },
            onClickAddWish = { /**TODO()*/ },
            onClickViewAllWishes = model::goToWishes,
        )
        if (getImage) {
            DialogGetImageList(
                onDismiss = { getImage = false },
                getPhoto = {
                    if (it.isNotEmpty()) {
                        model.uploadPhoto(it)
                        getImage = false
                    }
                    getImage = false
                }
            )
        }
        if (focusDetails) {
            DialogBottomSheet(
                onDismiss = {
                    focusManager.clearFocus()
                    focusDetails = false
                }) {

                DialogDetailedInformation(
                    familyName = chooserFamily?.getNameFamily(),
                    numFamily = chooserFamily?.members?.size,
                    birthday = userData.birthdate?.toDateMillisToUnixStringFullMonth(),
                    city = cityUser?.name,
                    cityOrigin = cityUserOrigin?.name,
                    phone = userData.tel,
                    tg = userData.tg,
                    aboutYou = userData.description,
                    doing = userData.work,
                    interests = userData.interests.joinToString(separator = ", "),
                    likeMusic = userData.favorite_music,
                    films = userData.favorite_movies,
                    books = userData.favorite_books,
                    games = userData.favorite_games,
                )
            }
        }
    }
}

@Composable
private fun ProfileScr(
    onClickBack: () -> Unit,
    onClickChangeFamily: (GettingFamily) -> Unit,
    onClickSettings: () -> Unit,
    onClickDescriptions: () -> Unit,
    onClickRedactionProfile: () -> Unit,
    onClickMember: (GettingFamilyMember) -> Unit,
    onClickAllFamily: () -> Unit,
    onClickAllMedia: () -> Unit,
    onClickUploadAPhoto: () -> Unit,
    listFamily: List<GettingFamily>,
    members: List<GettingFamilyMember>,
    listMedia: List<GettingMedia>,
    listPosts: List<GettingPost>,
    listWishes: List<GettingWish>,
    chooserFamily: GettingFamily?,
    avatar: Any?,
    name: String,
    locationCity: String?,
    onClickAddWish: () -> Unit,
    onClickViewAllWishes: () -> Unit,
    onClickChooseMenu: (MenuRibbon) -> Unit,
    menuRibbon: MenuRibbon,
    onClickOneMedia: (GettingMedia) -> Unit,
    onClickLike: (GettingPost) -> Unit,
    onClickLink: (GettingPost) -> Unit,
    onClickUser: (GettingPost) -> Unit,
    onClickModules: (GettingPost) -> Unit,
    onClickComment: (GettingPost) -> Unit,
    onClickShare: (GettingPost) -> Unit,
) {

    var expandedFamily by rememberState { false }
    var isVisibilityDescriptions by rememberState { true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeApp.colors.background)
            .systemBarsPadding()
    ) {
        PanelNavBackTop(
            onClickBack = onClickBack,
            container = ThemeApp.colors.background,
            content = {
                DropMenuInDropMenuFamily(
                    content = {
                        IconButtonApp(
                            modifier = Modifier,
                            onClick = { expandedFamily = !expandedFamily }
                        ) {
                            IconApp(painter = rememberImageRaw(R.raw.ic_dashboard))
                        }
                    },
                    expanded = expandedFamily,
                    chooserFamily = chooserFamily,
                    onDismiss = { expandedFamily = false },
                    listFamily = listFamily,
                    onChooseFamily = onClickChangeFamily

                )

                IconButtonApp(
                    modifier = Modifier.padding(end = DimApp.screenPadding * 0.5f),
                    onClick = onClickSettings
                ) {
                    IconApp(painter = rememberImageRaw(R.raw.ic_settings))
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                CardWithAvatar(
                    avatar = avatar,
                    isVisibilityDescriptions = isVisibilityDescriptions,
                    name = name,
                    locationCity = locationCity,
                    onClickDescriptions = onClickDescriptions,
                    onClickRedactionProfile = onClickRedactionProfile
                )
            }

            if (members.isNotEmpty()) {
                item {
                    BoxSpacer(.5f)
                    RowWithFamily(
                        members = members,
                        onClickMember = onClickMember,
                        onClickAll = onClickAllFamily

                    )
                }

            }
            item {
                BoxSpacer(.5f)
                RibbonPager(
                    onClickChooseMenu = onClickChooseMenu,
                    menuRibbon = menuRibbon,
                    listMedia = listMedia,
                    onClickAllMedia = onClickAllMedia,
                    onClickUploadAPhoto = onClickUploadAPhoto,
                    onClickOneMedia = onClickOneMedia,
                    listWishes = listWishes,
                    onClickAddWish = onClickAddWish,
                    onClickViewAllWishes = onClickViewAllWishes,
                )
                BoxSpacer(.3f)
            }

            items(
                items = listPosts,
                key = { item -> item.id }) { item ->
                ItemsPost(
                    avatar = item.user.avatar,
                    name = item.user.getNameAndLastName(),
                    lastVisited = item.user.last_visit?.formatTimeElapsed() ?: "",
                    imageList = item.getUrlAttachments(),
                    description = item.text ?: "",
                    isLike = item.is_vote ?: false,
                    onClickLike = { onClickLike.invoke(item) },
                    onClickLink = { onClickLink.invoke(item) },
                    onClickUser = { onClickUser.invoke(item) },
                    onClickModules = { onClickModules.invoke(item) },
                    onClickComment = { onClickComment.invoke(item) },
                    onClickShare = { onClickShare.invoke(item) })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RibbonPager(
    onClickChooseMenu: (MenuRibbon) -> Unit,
    menuRibbon: MenuRibbon,
    listMedia: List<GettingMedia>,
    listWishes: List<GettingWish>,
    onClickAllMedia: () -> Unit,
    onClickAddWish: () -> Unit,
    onClickViewAllWishes: () -> Unit,
    onClickUploadAPhoto: () -> Unit,
    onClickOneMedia: (GettingMedia) -> Unit
) {

    val pagerState: PagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val menuList by rememberState { MenuRibbon.values() }
    var offsetTargetDot by rememberState { 0.dp }
    val offsetDot by animateDpAsState(targetValue = offsetTargetDot)
    val des = LocalDensity.current

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        menuList.getOrNull(pagerState.currentPage)?.let {
            onClickChooseMenu.invoke(it)
        }
    })
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant)
            .padding(bottom = DimApp.screenPadding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                menuList.forEachIndexed { index, item ->
                    TextButtonApp(
                        modifier = Modifier.onGloballyPositioned {
                            if (menuRibbon == item) {
                                offsetTargetDot =
                                    with(des) { it.positionInWindow().x.toDp() + (it.size.width * .5f).toDp() }
                            }
                        },
                        contentPadding = PaddingValues(DimApp.screenPadding * .5f),
                        colors = colorsButtonAccentTextApp().copy(
                            contentColor = if (menuRibbon == item) {
                                ThemeApp.colors.primary
                            } else {
                                ThemeApp.colors.textDark
                            }
                        ),
                        onClick = {
                            onClickChooseMenu.invoke(item)
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = item.getTextMenu()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .offset(x = offsetDot - (DimApp.menuItemsWidth * .5f))
                    .width(DimApp.menuItemsWidth)
                    .height(DimApp.menuItemsHeight)
                    .clip(ThemeApp.shape.smallTop)
                    .background(ThemeApp.colors.primary)
            )
            FillLineHorizontal(modifier = Modifier.fillMaxWidth())
            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp),
                verticalAlignment = Alignment.Top,
                pageCount = menuList.size,
                state = pagerState
            ) { page ->

                val menuItem = menuList.getOrNull(page) ?: return@HorizontalPager
                when (menuItem) {
                    MenuRibbon.MEDIA    -> ContentMediaMenu(
                        listMedia = listMedia,
                        onClickAllMedia = onClickAllMedia,
                        onClickUploadAPhoto = onClickUploadAPhoto,
                        onClickOneMedia = onClickOneMedia
                    )

                    MenuRibbon.WISHLIST -> ContentWishList(
                        listWishes = listWishes,
                        onClickAddWish = onClickAddWish,
                        onClickViewAllWishes = onClickViewAllWishes
                    )

                    MenuRibbon.AFFAIRS  -> {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .background(ThemeApp.colors.primary.copy(.1f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            TextBodyLarge(text = menuItem.getTextMenu())
                        }
                    }

                    MenuRibbon.AWARDS   -> {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .background(ThemeApp.colors.primary.copy(.1f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            TextBodyLarge(text = menuItem.getTextMenu())
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ContentWishList(
    listWishes: List<GettingWish>,
    onClickAddWish: () -> Unit,
    onClickViewAllWishes: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = DimApp.screenPadding),
    ) {
        BoxSpacer(.5f)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(DimApp.screenPadding),
            maxItemsInEachRow = 2
        ) {
            listWishes.take(4).forEach { wish ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(ThemeApp.shape.smallAll)
                        .clickableRipple { },
                ) {
                    BoxImageLoadSizeWidth(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(ThemeApp.shape.smallAll),
                        sizeWidth = 0.4f,
                        image = wish.cover,
                    )

                    TextCaption(
                        text = wish.title ?: ""
                    )

                    TextBodyMedium(
                        text = TextApp.formatRub(wish.price),
                        color = ThemeApp.colors.textLight
                    )
                    BoxSpacer()
                }
            }
        }
        BoxSpacer(.5f)
        Row {
            ButtonWeakApp(
                modifier = Modifier.weight(1f),
                onClick = onClickAddWish,
                text = TextApp.titleAdd
            )
            BoxSpacer(.5f)
            ButtonWeakApp(
                modifier = Modifier.weight(1f),
                onClick = onClickViewAllWishes,
                text = TextApp.holderViewAll
            )
        }
    }
}

@Composable
private fun ContentMediaMenu(
    listMedia: List<GettingMedia>,
    onClickAllMedia: () -> Unit,
    onClickUploadAPhoto: () -> Unit,
    onClickOneMedia: (GettingMedia) -> Unit,
) {

    val configuration = LocalConfiguration.current
    val widthImage by rememberState { (configuration.screenWidthDp * 0.3f).dp.coerceAtMost(DimApp.sizeImageInPager) }

    val listMediaRemember by rememberState(listMedia) { listMedia.chunked(3) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = DimApp.screenPadding)
            .padding(horizontal = DimApp.screenPadding),
    ) {
        if (listMedia.isEmpty()) {
            ProgressIndicatorApp(
                modifier = Modifier
                    .size(DimApp.iconSizeTouchStandard)
                    .align(Alignment.CenterHorizontally)
            )
        }

        listMediaRemember.take(2).forEach { listTree ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                listTree.forEach {
                    BoxImageLoad(
                        modifier = Modifier
                            .size(widthImage)
                            .clip(ThemeApp.shape.smallAll)
                            .clickableRipple { onClickOneMedia.invoke(it) },
                        image = it.url,
                    )
                }
            }
            BoxSpacer(.3f)
        }
        BoxSpacer(.7f)
        Row(modifier = Modifier.fillMaxWidth()) {
            ButtonWeakApp(
                modifier = Modifier.weight(1f),
                onClick = onClickAllMedia,
                text = TextApp.textViewAll
            )
            BoxSpacer()
            ButtonWeakApp(
                modifier = Modifier.weight(1f),
                onClick = onClickUploadAPhoto,
                text = TextApp.textUploadAPhoto
            )
        }
    }
}

@Composable
private fun DropMenuInDropMenuFamily(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onChooseFamily: (GettingFamily) -> Unit,
    listFamily: List<GettingFamily>,
    chooserFamily: GettingFamily?,
    content: @Composable BoxScope.() -> Unit,
) {
    var dropDownWidth by rememberState { 0.dp }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        content.invoke(this)
        DropdownMenu(
            modifier = Modifier
                .background(color = ThemeApp.colors.backgroundVariant)
                .padding(horizontal = DimApp.screenPadding),
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            var chooserFamilyChild by rememberState { chooserFamily }
            var dropMenuExpanded by rememberState { false }
            var dropDownOffset by rememberState { DpOffset(0.dp, 0.dp) }
            val des = LocalDensity.current
            Column(modifier = Modifier) {

                TextTitleSmall(text = TextApp.titleChooseFamily)

                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = DimApp.screenPadding)
                            .onGloballyPositioned { layout ->

                                val x = with(des) { layout.positionInWindow().x.toDp() }
                                val y = with(des) { layout.positionInWindow().y.toDp() }
                                val width = with(des) { layout.size.width.toDp() }
                                val height = with(des) { layout.size.height.toDp() }

                                dropDownOffset = DpOffset(x = x, y = y + height)
                                dropDownWidth = width

                            }
                            .clip(ThemeApp.shape.smallAll)
                            .widthIn(min = DimApp.widthTextViewSize)
                            .background(ThemeApp.colors.container)
                            .clickableRipple { dropMenuExpanded = !dropMenuExpanded }
                            .padding(start = DimApp.screenPadding),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        TextBodyLarge(
                            maxLines = 1,
                            text = "${chooserFamilyChild?.name ?: ""}#${chooserFamilyChild?.id ?: ""}"
                        )

                        BoxFillWeight()

                        IconButtonApp(
                            modifier = Modifier,
                            onClick = { dropMenuExpanded = !dropMenuExpanded }
                        ) {
                            IconApp(painter = rememberImageRaw(R.raw.ic_drop))
                        }

                    }

                    DropdownMenu(
                        modifier = Modifier
                            .width(dropDownWidth)
                            .background(color = ThemeApp.colors.backgroundVariant)
                            .padding(horizontal = DimApp.screenPadding),
                        offset = dropDownOffset,
                        expanded = dropMenuExpanded,
                        onDismissRequest = {
                            dropMenuExpanded = false
                        }
                    ) {

                        listFamily.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    chooserFamilyChild = item
                                    dropMenuExpanded = false
                                },
                                text = {
                                    Column() {
                                        TextBodyLarge(text = "${item.name ?: ""}#${item.id ?: ""}")
                                        FillLineHorizontal(modifier = Modifier.fillMaxWidth())
                                    }
                                })
                        }
                    }
                }

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButtonApp(
                        colors = colorsButtonAccentTextApp().copy(contentColor = ThemeApp.colors.textDark),
                        onClick = onDismiss,
                        text = TextApp.titleCancel
                    )
                    TextButtonApp(
                        onClick = {
                            chooserFamilyChild?.let {
                                onChooseFamily.invoke(it)
                                onDismiss.invoke()
                            }
                        },
                        text = TextApp.titleOk
                    )
                }
            }
        }
    }
}


@Composable
private fun RowWithFamily(
    members: List<GettingFamilyMember>,
    onClickMember: (GettingFamilyMember) -> Unit,
    onClickAll: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant)
            .padding(horizontal = DimApp.screenPadding)
    ) {

        Row(
            modifier = Modifier
                .height(DimApp.heightButtonInLine)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextBodyLarge(
                text = TextApp.holderFamily
            )
            BoxFillWeight()
            if (members.size > 4) {
                TextButtonApp(
                    onClick = onClickAll,
                    text = TextApp.holderShowAll
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (members.size > 3) DimApp.screenPadding else 0.dp),
            horizontalArrangement = if (members.size > 3) Arrangement.SpaceBetween
            else Arrangement.spacedBy(DimApp.screenPadding * 2f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            members.take(4).forEach { item ->
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickableRipple(
                            bounded = false,
                            radius = DimApp.iconSizeTouchStandard
                        ) {
                            onClickMember.invoke(item)
                        },

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BoxImageLoad(
                        modifier = Modifier
                            .size(DimApp.iconSizeTouchStandard)
                            .clip(CircleShape),
                        drawableError = R.drawable.stab_avatar,
                        drawablePlaceholder = R.drawable.stab_avatar,
                        image = item.user?.avatar
                    )
                    TextLabel(text = item.first_name ?: "")
                }

            }
        }
        BoxSpacer()
    }
}

@Composable
private fun CardWithAvatar(
    avatar: Any?,
    name: String,
    locationCity: String?,
    onClickDescriptions: () -> Unit,
    onClickRedactionProfile: () -> Unit,
    isVisibilityDescriptions: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(DimApp.avatarProfileSize + DimApp.screenPadding)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
                    .align(Alignment.BottomCenter)
                    .drawColoredShadow(
                        driveShadow = DriveShadow.UPPER
                    )
                    .clip(ThemeApp.shape.mediumTop)
                    .background(color = ThemeApp.colors.backgroundVariant)
            )
            Box(
                modifier = Modifier
                    .padding(bottom = DimApp.screenPadding)
                    .align(Alignment.Center)
                    .shadow(
                        elevation = DimApp.shadowElevation,
                        shape = CircleShape
                    )
                    .size(DimApp.avatarProfileSize)
                    .border(
                        width = DimApp.lineWidthBorderProfile,
                        color = ThemeApp.colors.backgroundVariant,
                        shape = CircleShape
                    )
                    .background(color = ThemeApp.colors.backgroundVariant)
                    .padding(DimApp.lineWidthBorderProfile)
            ) {

                BoxImageLoad(
                    modifier = Modifier.fillMaxSize(),
                    drawableError = R.drawable.stab_avatar,
                    drawablePlaceholder = R.drawable.stab_avatar,
                    image = avatar
                )

            }
        }

        AnimatedVisibility(visible = isVisibilityDescriptions) {
            Column(
                modifier = Modifier
                    .background(color = ThemeApp.colors.backgroundVariant)
                    .fillMaxWidth()
                    .padding(horizontal = DimApp.screenPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextTitleMedium(text = name)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    locationCity?.let {
                        IconApp(
                            modifier = Modifier.padding(end = DimApp.screenPadding * .3f),
                            painter = rememberImageRaw(id = R.raw.ic_location)
                        )
                        TextBodyMedium(text = locationCity)
                    }

                    TextButtonApp(
                        onClick = onClickDescriptions,
                        text = TextApp.holderMore,
                        contentStart = {
                            IconApp(
                                modifier = Modifier.padding(end = DimApp.screenPadding * .3f),
                                painter = rememberImageRaw(id = R.raw.ic_info)
                            )
                        })
                }
                BoxSpacer(.5f)
                ButtonWeakApp(
                    onClick = onClickRedactionProfile,
                    text = TextApp.holderEditProfile
                )
                BoxSpacer(.5f)
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(DimApp.screenPadding)
                .drawColoredShadow(
                    offsetY = 5.dp,
                    driveShadow = DriveShadow.LOWER
                )
                .clip(ThemeApp.shape.mediumBottom)
                .background(color = ThemeApp.colors.backgroundVariant)
        )
    }
}


@Composable
private fun ItemsPost(
    modifier: Modifier = Modifier,
    avatar: String?,
    name: String,
    lastVisited: String,
    imageList: List<String>,
    description: String,
    isLike: Boolean,
    onClickLike: (Boolean) -> Unit,
    onClickLink: (String) -> Unit,
    onClickUser: () -> Unit,
    onClickModules: () -> Unit,
    onClickComment: () -> Unit,
    onClickShare: () -> Unit,
) {
    var isOverflowHeight by remember { mutableStateOf(false) }
    var isOverStart by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = isOverflowHeight, block = {
        if (isOverflowHeight) isOverStart = true
    })
    var isClickFull by remember { mutableStateOf(false) }

    val painterLike = when (isLike) {
        true  -> rememberImageRaw(R.raw.ic_heart_solid)
        false -> rememberImageRaw(R.raw.ic_heart_outline)
    }

    val configuration = LocalConfiguration.current
    val widthImage by rememberState {
        (configuration.screenWidthDp * 0.3f).dp.coerceAtMost(DimApp.sizeImageInPager)
    }

    Column(
        modifier = modifier
            .padding(vertical = DimApp.shadowElevation)
            .fillMaxWidth()
            .shadow(
                elevation = DimApp.shadowElevation,
                shape = ThemeApp.shape.mediumAll
            )
            .background(color = ThemeApp.colors.backgroundVariant)
            .clickableRipple { onClickUser.invoke() }

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            BoxImageLoad(
                modifier = Modifier
                    .padding(top = DimApp.screenPadding)
                    .padding(horizontal = DimApp.screenPadding)
                    .size(DimApp.iconSizeBig)
                    .clip(CircleShape),
                drawableError = R.drawable.stab_avatar,
                drawablePlaceholder = R.drawable.stab_avatar,
                image = avatar
            )

            Column(
                modifier = Modifier
                    .padding(end = DimApp.screenPadding)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                TextTitleSmall(
                    text = name, maxLines = 1,
                )
                TextBodyMedium(text = lastVisited)
            }
            IconButtonApp(
                modifier = Modifier,
                onClick = onClickModules
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_mero_vert))
            }

        }

        if (imageList.isNotEmpty())
            BoxImageLoad(
                modifier = Modifier
                    .size(widthImage)
                    .clip(ThemeApp.shape.smallAll),
                image = imageList.first(),
            )
        Column(
            modifier = Modifier.padding(horizontal = DimApp.screenPadding)
        ) {
            TextLinksWeb(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DimApp.starsPadding),
                text = description,
                style = ThemeApp.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (!isClickFull) 5 else Int.MAX_VALUE,
                onClick = onClickLink,
                onTextLayout = { result ->
                    isOverflowHeight = result.didOverflowHeight
                },
            )

            if (isOverStart) {
                Text(modifier = Modifier
                    .clickableRipple { isClickFull = !isClickFull }
                    .padding(start = DimApp.textPaddingMin),
                    text = if (isClickFull) TextApp.buttonGoneText else TextApp.buttonViewNextText,
                    color = ThemeApp.colors.primary,
                    style = ThemeApp.typography.button)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = DimApp.screenPadding, start = DimApp.screenPadding / 2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonApp(
                modifier = Modifier,
                onClick = { onClickLike(!isLike) }
            ) {
                IconApp(painter = painterLike)
            }

            IconButtonApp(
                modifier = Modifier,
                onClick = onClickComment
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_comment))
            }
            IconButtonApp(
                modifier = Modifier,
                onClick = onClickShare
            ) {
                IconApp(painter = rememberImageRaw(R.raw.ic_share))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
