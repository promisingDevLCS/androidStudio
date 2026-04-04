import org.gradle.kotlin.dsl.annotationProcessor

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "com.example.myapplicationforreport"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.myapplicationforreport"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // 자동으로 binding 클래스를 생성하기 위한 설정
    // ex. 'item_diary.xml' -> ItemDiaryBinding 클래스 자동으로 생성
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.fragment)
    implementation(libs.recyclerview)
    // UI 관련 라이브러리(데이터 생명주기 보관, 데이터 변경 감지)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    // Room DB(Local DB)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    // 네비게이션 바
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    // ViewModel/Repository -> junit, Room DAO -> ext.junit, 화면 이동/ 버튼 클릭 -> espresso
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}