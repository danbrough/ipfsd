plugins {
  id("com.android.application")

  kotlin("android")
  id("org.jetbrains.dokka")
  // `maven-publish`
}

android {

  ndkVersion = ProjectVersions.NDK_VERSION

  compileSdk = ProjectVersions.SDK_VERSION

  defaultConfig {
    minSdk = ProjectVersions.MIN_SDK_VERSION
    targetSdk = ProjectVersions.SDK_VERSION
    //versionCode = ProjectVersions.BUILD_VERSION
    //versionName = ProjectVersions.VERSION_NAME
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    //consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }


  testOptions {
    unitTests.isIncludeAndroidResources = true
    unitTests.isReturnDefaultValues = true
  }

}


tasks.withType<Test> {
  useJUnit()
  testLogging {
    events("standardOut", "started", "passed", "skipped", "failed")
    showStandardStreams = true
    /*  outputs.upToDateWhen {
        false
      }*/
  }
}


dependencies {
  /* api(project(":common_domain")) {
     exclude(group = "com.android", module = "android")
   }*/


  implementation("com.github.danbrough.ipfsd:bridge:_")


  implementation(project(":client"))


  implementation(AndroidX.appCompat)
  implementation(Google.android.material)

  if (Danbroid.utils.useLocalUtils) {
    implementation(project(":menu"))
    implementation(project(":misc"))
  } else {
    implementation(Danbroid.utils.menu)
    implementation(Danbroid.utils.misc)
  }
  implementation(Danbroid.utils.logging)

  //implementation("com.mikepenz:iconics-core:_")
  //implementation("com.mikepenz:library-typeface-api:5.2.4")
  implementation("com.mikepenz:iconics-core:_")


  implementation("com.mikepenz:google-material-typeface:_@aar")

  /*implementation("com.mikepenz:fontawesome-typeface:_")
  implementation("com.mikepenz:material-design-iconic-typeface:_")*/


//
  //implementation("com.mikepenz:library-typeface-api:5.2.4")

  implementation("commons-io:commons-io:_")

  //api(Libs.kotlinx_coroutines_android)
  //implementation("com.google.code.gson:gson:_")
  //api(Libs.gson)
  implementation(AndroidX.core.ktx)
  implementation(AndroidX.preference)


  //implementation(AndroidX.lifecycle.runtimeKtx)
  implementation(KotlinX.coroutines.android)
//  implementation(Square.okHttp3.okHttp)


  implementation(AndroidX.lifecycle.liveDataKtx)
  testImplementation(Testing.junit4)
  testImplementation(AndroidX.test.core)


  // testImplementation("net.sf.kxml:kxml2-min:_")
  //testImplementation(Libs.robolectric)
  androidTestImplementation(Testing.junit4)

  androidTestImplementation(AndroidX.test.core)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.test.rules)
  androidTestImplementation(AndroidX.test.ext.junitKtx)
  androidTestImplementation("com.google.truth:truth:_")
}


/*
tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
  dokkaSourceSets {
    configureEach {
      includes.from("README.md")
    }
  }
}*/
