import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.compile_sdk)
    buildToolsVersion = Versions.buildToolsVersion
    defaultConfig {
        applicationId = "co.touchlab.kampstarter"
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
    buildTypes {
        getByName("release")  {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation(project(":shared"))
    implementation(Deps.app_compat_x)
    implementation(Deps.core_ktx)
    implementation(Deps.ktor.androidCore)
    implementation(Deps.constraintlayout)
    implementation(Deps.SqlDelight.runtimeJdk)
    implementation(Deps.SqlDelight.driverAndroid)
    implementation(Deps.Coroutines.jdk)
    implementation(Deps.Coroutines.android)
    implementation(Deps.multiplatformSettings)
    implementation(Deps.koinCore)
    implementation(Deps.cardView)
    implementation(Deps.recyclerView)
    implementation(Deps.glide)
    implementation(Deps.viewModel)
    implementation(Deps.liveData)
    implementation(Deps.lifecycleExtensions)
    implementation(Deps.lifecycleReactiveStreams)
    implementation(Deps.lifecycleRuntime)
    implementation(Deps.koinCore)

    kapt(Deps.lifecycleCompilerKapt)
    kapt(Deps.glideCompilerKapt)

    testImplementation(Deps.junit)
}
