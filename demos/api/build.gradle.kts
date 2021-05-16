plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
//kotlin("org.jetbrains.kotlin:kotlin-android-extensions-runtime:")
  id("org.jetbrains.dokka")
}



android {

  compileSdkVersion(ProjectVersions.SDK_VERSION)

  defaultConfig {

    minSdkVersion(ProjectVersions.MIN_SDK_VERSION)
    targetSdkVersion(ProjectVersions.SDK_VERSION)
    versionCode = ProjectVersions.BUILD_VERSION
    versionName = ProjectVersions.VERSION_NAME
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    //consumerProguardFiles("consumer-rules.pro")

  }

  lintOptions {
    isAbortOnError = false
  }
  buildFeatures {
    viewBinding = true
  }
  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }


/*
  kotlinOptions {
    jvmTarget = "1.8"
    //freeCompilerArgs = listOf("-Xjsr305=strict")
  }
*/

/*  kotlin.sourceSets.all {
    setOf(
      "kotlinx.coroutines.ExperimentalCoroutinesApi",
      "kotlinx.coroutines.FlowPreview",
      "kotlinx.coroutines.InternalCoroutinesApi",
      "kotlin.time.ExperimentalTime"
    ).forEach {
      languageSettings.useExperimentalAnnotation(it)
    }
  }*/
}


dependencies {
  implementation(project(":client"))
  implementation(project(":api"))
  androidTestImplementation(project(":service"))

  implementation(AndroidX.lifecycle.runtimeKtx)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation("androidx.core:core-ktx:_")
  implementation(AndroidX.appCompatResources)
  implementation(AndroidX.appCompat)
  implementation(Kotlin.stdlib.jdk8)
  //implementation("com.github.ligi:ipfs-api-kotlin:_")
  // implementation("com.github.ipfs:java-ipfs-http-client:_")

  //sliding panel library

  //implementation(Libs.slf4j_android)
  //implementation(Libs.slf4j)

  if (Danbroid.utils.useLocalUtils) {
    implementation(project(":menu"))
    implementation(project(":misc"))
  } else {
    implementation(Danbroid.utils.menu)
    implementation(Danbroid.utils.misc)
  }

  implementation(Danbroid.utils.logging)


  implementation("com.google.zxing:android-core:_")

  implementation("com.google.zxing:core:_")

  implementation(Square.okHttp3.okHttp)

  androidTestImplementation(Testing.junit4)

  androidTestImplementation(AndroidX.test.core)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.test.rules)
  androidTestImplementation(AndroidX.test.ext.junitKtx)
  androidTestImplementation("com.google.truth:truth:_")



  implementation(AndroidX.navigation.fragmentKtx)
  implementation(AndroidX.navigation.uiKtx)
  implementation(AndroidX.constraintLayout)
  implementation(AndroidX.preferenceKtx)


}
