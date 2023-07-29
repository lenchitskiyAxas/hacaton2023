package ru.aries.hacaton.base.res

import ru.aries.hacaton.BuildConfig


object TextApp {
    val mockEmail: String = if (BuildConfig.DEBUG) "admin@base-project.com" else ""
    val mockPass: String = if (BuildConfig.DEBUG) "changethis" else ""

    const val baseTextNameApp: String = "КОПИЛКА Банк"
    const val FOLDER_NAME: String = "PIGGY_BANK"

    const val titleCancel: String = "Отмена"
    const val titleNext: String = "Продолжить"
    const val titleExit: String = "Выход"
    const val textExitApp: String = "Выйти из приложения?"
    const val textMissingPermission: String = "Отсутствуют необходимые разрешения"
    const val textSomethingWentWrong: String = "Что-то пошло не так"
    const val textWelcome: String = "Добро пожаловать!"
    const val textEmailAddress: String = "Адрес электронной почты"
    const val textPassword: String = "Пароль"
    const val textOpenTheCamera: String = "Открыть камеру"
    const val textOpenGallery: String = "Открыть галерею"
    const val textGenderMan: String = "Мужской"
    const val textGenderWoman: String = "Женский"
    const val textGenderManShort: String = "Муж"
    const val textGenderWomanShort: String = "Жена"
    const val textChildren: String = "Дети"
    const val textPhotos: String = "Фотографии"
    const val textAlbum: String = "Альбом"
    const val textGrandParent: String = "Родители"
    const val textBrotherSister: String = "Брат/Сестра"
    const val textAddSpouse: String = "Добавить супруга"
    const val textAddChild: String = "Добавить детей"
    const val textAddSelf: String = "Добавить себя"
    const val textAddParent: String = "Добавить родителей"
    const val textAddSibling: String = "Добавить брата или сестру"
    const val textGenderInterWoman: String = "Введите данные вашей жены."
    const val textGenderInterMan: String = "Введите данные вашего мужа."
    val formatSomethingYou: (Any?) -> String = { "$it (Вы)" }

}