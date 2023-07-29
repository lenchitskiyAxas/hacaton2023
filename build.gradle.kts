// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    val kotlin = "1.8.21"
    val application = "8.0.2"

    id ("com.android.application") version application apply false
    id ("org.jetbrains.kotlin.android") version kotlin apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version kotlin apply false
}
true // Needed to make the Suppress annotation work for the plugins block