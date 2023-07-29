package ru.aries.hacaton.base.res

import ru.aries.hacaton.BuildConfig


object TextApp {
    val mockEmail: String = if (BuildConfig.DEBUG) "test@mum.ru" else ""
    val mockPass: String = if (BuildConfig.DEBUG) "test" else ""

    val mockEmail2: String = if (BuildConfig.DEBUG) "denis@example.com" else ""
    val mockPass2: String = if (BuildConfig.DEBUG) "password" else ""


    const val baseTextNameApp: String = "КОПИЛКА"
    const val FOLDER_NAME: String = "PIGGY_BANK"
    const val linkAxas: String = "https://axas.ru/"
    const val TELEGRAM_AXAS_BOT: String = "AXAS_BOT"
    const val TELEGRAM_IAXAS: String = "iAXAS"
    val FORMAT_TELEGRAM_URL_: (Any?) -> String = { "https://t.me/$it" }
    val FORMAT_TELEGRAM_INTENT: (Any?) -> String = { "tg://resolve?domain=$it" }
    val FORMAT_WHATSAPP_URL_: (Any?) -> String = { "https://api.whatsapp.com/send?phone=$it" }
    val FORMAT_WHATSAPP_INTENT: (Any?) -> String = { "whatsapp://send/?phone=$it" }
    val FORMAT_TEL_INTENT: (Any?) -> String = { "tel:+$it" }
    const val TEXT_OS: String = "android"

    const val titleCancel: String = "Отмена"
    const val titleOk: String = "Ок"
    const val titleNext: String = "Продолжить"
    const val titleAdd: String = "Добавить"
    const val titleFillInLater: String = "Заполнить позже"
    const val titleExit: String = "Выход"
    const val titleChange: String = "Изменить"
    const val titleAboutTheDeveloper: String = "О разработчике"
    const val titleRibbon: String = "Лента"
    const val titleAffairs: String = "Дела"
    const val titleChats: String = "Чаты"
    const val titleCalendar: String = "Календарь"
    const val titleGifts: String = "Подарки"
    const val titleChooseFamily: String = "Выбрать семью"
    const val titleEditProfile: String = "Редактировать профиль"
    const val titlePersonalData: String = "Личные данные"
    const val titleMedia: String = "Медиа"
    const val titleNewAlbum: String = "Новый альбом"
    const val titleWishList: String = "Вишлист"
    const val titleAwards: String = "Награды"
    const val titleCreate: String = "Создать"

    const val titleMyMedia: String = "Мои медиа"
    const val titleAll: String = "Все"
    const val titleFamily: String = "Семья"
    const val titleFavorites: String = "Избранное"

    const val holderTextPlus: String = "+"
    const val holderTextMinus: String = "-"
    const val holderDollar: String = "$"
    const val holderTextPhoneMask: String = "70000000000"
    const val holderName: String = "Имя"
    const val holderSurname: String = "Фамилия"
    const val holderPhone: String = "Телефон"
    const val holderTime: String = "Время"
    const val holderDate: String = "Дата"
    const val holderFamily: String = "Семья"
    const val holderContacts: String = "Контакты"
    const val holderPatronymic: String = "Отчество"
    const val holderSave: String = "Сохранить"
    const val holderFloor: String = "Этаж"
    const val holderImportant: String = "  ⃰"
    const val holderAsterisk: String = "⁂"
    const val holderSymbolStartDescription: String = "*-"
    const val holderMore: String = "Подробнее"
    const val holderShowAll: String = "Показать всех"
    const val holderShowAllS: String = "Показать все"
    const val holderEditProfile: String = "Редактировать профиль"
    const val holderViewAll: String = "Посмотреть всё"

    const val textExitApp: String = "Выйти из приложения?"
    const val textApplicationDevelopedDigital: String = "Приложение разработано\nв Digital студии"
    const val textMissingPermission: String = "Отсутствуют необходимые разрешения"
    const val textVersionApp: String = "Версия ${BuildConfig.VERSION_NAME}"
    const val textStageConfirmed: String = "В работе"
    const val textOn: String = "включены"
    const val textDidNotChooseAFamily: String = "Вы не выбрали семью или что-то пошло не так!"
    const val textTelegramNotInstalled: String = "Telegram не установлен!"
    const val textWhatsappNotInstalled: String = "Whatsapp не установлен!"
    const val textItemsOnSale: String = "Товары со скидкой"
    const val textFeaturedProducts: String = "Избранные товары"
    const val textSomethingWentWrong: String = "Что-то пошло не так"
    const val textTotal: String = "Итого"
    const val textWishlistSecret: String = "Сделать вишлист секретным"
    const val textDescriptionWishlistSecret: String = "Видеть его сможете только вы и соавторы."
    const val textItEmpty: String = "Тут пусто"
    const val textItNoPhotosYet: String = "В этом вишлисте \nпока нет желаний"
    const val textCreateAFamilyCell: String = "Создать семейную ячейку"
    const val textJoinAFamilyUnit: String = "Присоединиться к семейной ячейке"
    const val textEnterTheDetailsOfYourFamilyMembers: String =
        "Введите данные членов вашей семьи и отправьте им приглашение.*"
    const val textSignInWithAnInvite: String =
        "Войдите с помощью приглашения или ID семейной ячейки."
    const val textWelcome: String = "Добро пожаловать!"
    const val textCreateAnAccount: String = "Создание учётной записи"
    const val textTellUsAboutYourInterests: String = "Расскажите о ваших интересах"
    const val textCreateACell: String = "Создание ячейки"
    const val textEnterFamilyCellID: String = "Введите ID семейной ячейки"
    const val textFamilyCellID: String = "ID семейной ячейки"
    const val textWelcomeFamilyVibe: String = "Добро пожаловать\nв КОПИЛКА!"
    const val textEnterYourDetails: String = "Введите ваши данные."
    const val textYourFamilyUnit: String = "Ваша семейная ячейка"
    const val textWelcomeInFamilyVibe: String = "Добро пожаловать в КОПИЛКА!"
    const val textRegisterWithEmail: String = "Зарегистрируйтесь с помощью электронной почты."
    const val textEmailAddress: String = "Адрес электронной почты"
    const val textPassword: String = "Пароль"
    const val textPasswordNoSimilar: String = "Пароли не совпадают"
    const val textPasswordNoRegular: String = "Пароль должен содержать спецсимволы (^.*?=$ и т.д.)"
    const val textEmailNoValide: String = "Неверный формат электронной почты"
    const val textPasswordTwo: String = "Повторите пароль"
    const val textPhoto: String = "Фотография"
    const val textName: String = "Имя*"
    const val textNameTitle: String = "Название"
    const val textDescription: String = "Описание"
    const val textWish: String = "Желание"
    const val textAddSomething: String =  "Добавить что-нибудь"
    const val textAddressOrPlace: String = "Адрес или место"
    const val buttonViewNextText: String = "развернуть"
    const val buttonGoneText: String = "скрыть"

    const val textCreatingAFamilyDescription: String =
        "при создании семейной ячейки вводятся данные родителей и детей. Только родители (муж и жена) могут создать семейную ячейку."
    const val textBiography: String = "Биография"
    const val textInterests: String = "Интересы"
    const val textBirthDayWye: String = "Дата рождения*"
    const val textCustomDay: String = "Дата*"
    const val textBirthDay: String = "Дата рождения"
    const val textGender: String = "Пол*"
    const val textGender_: String = "Пол"
    const val textSurname: String = "Фамилия*"
    const val textGift: String = "Подарки"
    const val textNewWishlist: String = "Новый вишлист"
    const val textAlbumCreated: String = "Альбом создан"
    const val textCityOfBirth: String = "Город рождения"
    const val textMobilePhone: String = "Мобильный телефон"
    const val textNotSpecified: String = "Не указан"
    const val textTelegram: String = "Телеграм"
    const val textAddInterests: String = "Добавьте интересы"
    const val textPatronymic: String = "Отчество"
    const val textCityOfResidence: String = "Город проживания"
    const val textSpecifyGender: String = "Укажите пол"
    const val textDetailedInformation: String = "Подробная информация"
    const val textBirthday: String = "День рождения"
    const val textCity: String = "Город"
    const val textOriginalCity: String = "Родной город"
    const val textPhone: String = "Мобильный телефон"
    const val textTg: String = "Телеграм"
    const val textAboutYourself: String = "О себе"
    const val textDoing: String = "Деятельность"

    //    const val textInterests: String = "Интересы"
    const val textLikeMusics: String = "Любимая музыка"
    const val textLikeFilms: String = "Любимые фильмы"
    const val textLikeBooks: String = "Любимые книги"
    const val textLikeGames: String = "Любимые игры"
    const val textMaidenName: String = "Девичья фамилия"
    const val textObligatoryField: String = "Обязательное поле"
    const val textIAmSuchAndSuch: String = "Я такой-то такой-то "
    const val textOpenTheCamera: String = "Открыть камеру"
    const val textOpenGallery: String = "Открыть галерею"
    const val textMailConfirmationCode: String = "Код подтверждения почты"
    const val textRegistered: String = "Регистрация"
    const val textToComeIn: String = "Войти"
    const val textDataUpdated: String = "Данные обновлены."
    const val textPrivateAlbum: String = "Приватный альбом"
    const val textPrivacyPolicy: String = "Политикой конфиденциальности"
    const val textForAMorePreciseSearch: String =
        "*-обязательные поля для более точного поиска совпадений с родственниками"
    const val textChangeEmailAddress: String = "Изменить адрес почты"
    const val textLinkAgreement: String = "согласие"
    const val textAgreementWhenInterPhone: String =
        "Даю $textLinkAgreement на обработку персональных данных"
    const val textLinkConditionsWhenInterPhone: String = "политики конфиденциальности"
    const val textConditionsWhenInterPhone: String =
        "Принимаю условия $textLinkConditionsWhenInterPhone"
    const val textAgreementPersonal: String =
        "Даю $textLinkAgreement на обработку персональных данных"
    const val textAgreementNews: String = "Даю $textLinkAgreement на получение новостей"
    const val textGenderOther: String = "Другое"
    const val textIPreferNotToAnswer: String = "Предпочитаю не отвечать"
    const val textGenderMan: String = "Мужской"
    const val textGenderWoman: String = "Женский"
    const val textGenderManShort: String = "Муж"
    const val textGenderWomanShort: String = "Жена"
    const val textChildren: String = "Дети"
    const val textPhotos: String = "Фотографии"
    const val textAlbum: String = "Альбом"
    const val textViewAll: String = "Посмотреть всё"
    const val textUploadAPhoto: String = "Загрузить фото"
    const val textGrandParent: String = "Родители"
    const val textBrotherSister: String = "Брат/Сестра"

    const val textAddSpouse: String = "Добавить супруга"
    const val textAddChild: String = "Добавить детей"
    const val textAddSelf: String = "Добавить себя"
    const val textAddParent: String = "Добавить родителей"
    const val textAddSibling: String = "Добавить брата или сестру"

    const val textAllFiles: String = "Все файлы"
    const val textWishlist: String = "Вишлисты"
    const val textAlbums: String = "Альбомы"
    const val textAllWishes: String = "Все желания"
    const val textChooseAlbum: String = "Выберите альбом"

    const val textAddToFavorite: String = "Добавить в избранное"
    const val textRemake: String = "Редактировать"
    const val textGoToAlbum: String = "Перейти к альбому"
    const val textTitleEditPhoto: String = "Редактировать фото"
    const val textTitleDeletePhoto: String = "Удалить фото?"
    const val textRename: String = "Переименовать"
    const val textDownload: String = "Скачать"
    const val textDelete: String = "Удалить"
    const val textWrite: String = "Написать"
    const val textRenameAlbum: String = "Переименовать альбом"
    const val textOwner: String = "Владелец"
    const val textDate: String = "Дата"
    const val textChangePicAlbum: String = "Изменить обложку"
    const val textAddPhoto: String = "Добавить фотографии"
    const val textAddOnePhoto: String = "Добавить фото"
    const val textNewDesire: String = "Новое желание"
    const val textMyAlbom: String = "Мой альбом"
    const val textNoPhotoInAlbum: String = "В этом альбоме \n" +
            "пока нет фотографий"

    const val textGenderInterWoman: String = "Введите данные вашей жены."
    const val textGenderInterMan: String = "Введите данные вашего мужа."
    const val textGenderInterSatellite: String = "Введите данные вашего спутника."
    const val textEnterChildren: String = "Введите данные выших детей."
    const val textCheckYourFamilyDetails: String = "Проверьте данные вашей семьи"

    const val errorSomethingWrong: String = "Что-то пошло не так, попробуйте повторить позже"
    const val errorPagingContent: String = "Ошибка загрузки контента"
    const val errorEnterADeliveryAddress: String = "Укажите адрес доставки"
    const val errorEnterADeliveryEntrance: String = "Укажите номер подъезда"
    const val errorEnterADeliveryIntercom: String = "Укажите номер домофона"
    const val errorEnterADeliveryFloor: String = "Укажите этаж"
    const val errorEnterADeliveryApartmentOffice: String = "Укажите Квартиру/Офис"
    const val errorEnterADeliveryPhoneNumber: String = "Укажите номер телефона"
    const val errorCreateAlbum: String = "Ошибка при создании альбома"
    const val errorInvalidCodeEntered: String = "Введен неверный код"

    val formatNotRegisteredYet: (Any?) -> String = { "Ещё не зарегистрированы?   $it" }
    val formatYouAgreeTo: (Any?) -> String = { "Продолжая, вы соглашаетесь с $it" }
    val formatAlreadyHaveAnAccount: (Any?) -> String = { "Уже есть учётная запись?   $it" }
    val formatConfirmationCodeSentYourEmail: (Any?) -> String =
        { "На Ваш адрес электронной почты $it выслан код подтверждения. Введите код или перейдитепо ссылке из письма." }
    val formatDollar: (Any?) -> String = { " $it $" }
    val formatStepFrom: (Any?, Any?) -> String = { str1, str2 -> "Шаг $str1 из $str2" }
    val formatRub: (Any?) -> String = { "$it ₽" }
    val formatQty: (Any?) -> String = { " $it шт." }
    val formatWeight: (Any?) -> String = { "$it г" }
    val formatOrderNumber: (Any?) -> String = { "Ордер №$it" }
    val formatTextPhoneSend: (Any?) -> String = { "Отправили код на номер\n$it" }
    val formatPostedCreated: (Any?) -> String = { "Размещен $it" }
    val formatHelloUser: (Any?) -> String = { "Привет, $it" }
    val formatProductsFromSupplier: (Any?) -> String = { "Продукты от ${it ?: "..."}" }
    val formatPushNotificationsOnOff: (Any?) -> String = { "Push-уведомления ${it ?: ""}" }
    val formatSentARequestToJoin: (Any?, Any?) -> String =
        { str1, str2 -> "Вы отправили запрос на присоединение к семейной ячейки ${str1 ?: ""} ${str2 ?: ""}. Ожидайте, пока глава ячейки не примет ваше приглашение. " }
    val formatDeadlineDay: (Any?) -> String = { "$it дней" }
    val formatDataCreated: (Any?) -> String = { "Дата оформления: ${it ?: ""}" }
    val formatDeliveryTime: (Any?) -> String = { "Доставка к: ${it ?: ""}" }
    val formatGetCodeAgain: (Any?) -> String = { "Отправить код повторно через $it" }
    val formatDelete: (Any?) -> String = {
        "Фотография “$it”" +
                "будет удалена без возможности восстановления."
    }
    val formatSomethingYou: (Any?) -> String = { "$it (Вы)" }
    val formatAuthorFaq: (Any?, Any?) -> String = { str1, str2 -> "Автор: $str1, $str2" }
    val formatNumbOrderAndStatus: (Any?, Any?) -> String =
        { str1, str2 -> "№ ${str1 ?: "-"} ${str2 ?: ""}" }
    val formatUpdateFaq: (Any?) -> String = { str1 -> "Обновлено: $str1" }
    val textSelectedN: (Any?) -> String = { str -> "Выбрано: $str" }

}