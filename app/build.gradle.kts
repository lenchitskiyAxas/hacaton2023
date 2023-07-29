import com.android.SdkConstants.FN_LOCAL_PROPERTIES
import com.google.common.base.Charsets
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "ru.aries.hacaton"
    compileSdk = 33

    defaultConfig {
        applicationId = "ru.aries.hacaton"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0($versionCode)"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
        archivesName.convention(getNameFileForeConvention(applicationId, versionName))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            versionNameSuffix = "-DEBUG"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions { kotlinCompilerExtensionVersion = "1.4.7" }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val coroutinesVersion = "1.7.1"
    val kotlinVersion = "1.8.21"
    val composeBomVersion = "2023.06.01"
    val ktorBomVersion = "2.3.1"
    val voyagerVersion = "1.0.0-rc06"
    val roomVersion = "2.5.1"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("io.insert-koin:koin-android:3.4.2")
    implementation ("io.insert-koin:koin-core:3.4.2")
    runtimeOnly("io.insert-koin:koin-androidx-compose:3.4.5")
    implementation ("io.insert-koin:koin-core-coroutines:3.4.1")
    implementation("io.insert-koin:koin-ktor:3.4.1")

    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-androidx:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")

    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("com.google.android.play:core:1.10.3")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    implementation(platform("io.ktor:ktor-bom:$ktorBomVersion"))
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-logging")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.ktor:ktor-serialization-jackson")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-utils")

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("io.coil-kt:coil-svg:2.4.0")
    implementation("id.zelory:compressor:3.0.1")

    implementation("org.apache.tika:tika-parsers:2.8.0")
    implementation("org.apache.tika:tika-core:2.8.0")

    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")

    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.paging:paging-compose:3.2.0-rc01")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

fun getNameFileForeConvention(namespace: String?, version:String?): String {
    val dateName = SimpleDateFormat("yy'y_'MM'm_'dd'd-'HH'h'", Locale("ru", "RU"))
        .format(System.currentTimeMillis())
    val versionName = (version ?:"").replace(".", "_")
    var minNameSpace = namespace ?: ""
    minNameSpace = minNameSpace.substring(minNameSpace.lastIndexOf(".") + 1)
    minNameSpace = minNameSpace.replace(".", "_")
    return "app_${minNameSpace}_${dateName}-${versionName}"
}

fun gradleLocalProperties(projectRootDir: File): Properties {
    val properties = Properties()
    val localProperties = File(projectRootDir, FN_LOCAL_PROPERTIES)

    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}
