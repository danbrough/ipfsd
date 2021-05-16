plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
//kotlin("org.jetbrains.kotlin:kotlin-android-extensions-runtime:")
  id("org.jetbrains.dokka")
  kotlin("plugin.serialization")

}


android {

  compileSdk = ProjectVersions.SDK_VERSION

  defaultConfig {

    minSdk = ProjectVersions.MIN_SDK_VERSION
    targetSdk = ProjectVersions.SDK_VERSION
    versionCode = ProjectVersions.BUILD_VERSION
    versionName = ProjectVersions.VERSION_NAME
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
   // consumerProguardFiles("consumer-rules.pro")
  }

  lint{
    isAbortOnError = false
  }


  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }

  buildFeatures {
    viewBinding = true
  }

  kotlinOptions {
    jvmTarget = "1.8"
    //freeCompilerArgs = listOf("-Xjsr305=strict")

    freeCompilerArgs = mutableListOf("-Xopt-in=kotlin.ExperimentalStdlibApi").also {
      it.addAll(freeCompilerArgs)
    }

  }

  kotlin.sourceSets.all {
    setOf(
      "kotlinx.coroutines.ExperimentalCoroutinesApi",
      "kotlinx.coroutines.FlowPreview"
    ).forEach {
      languageSettings.useExperimentalAnnotation(it)
    }
  }


}



tasks.withType<Test> {
  useJUnit()

  testLogging {
    events("standardOut", "started", "passed", "skipped", "failed")
    showStandardStreams = true
    outputs.upToDateWhen {
      false
    }
  }
}


dependencies {
  implementation(project(":api"))
  implementation(project(":client"))
  implementation(Kotlin.stdlib.jdk8)
  //implementation(files("/shared/build/src/ipfsd/ipfsd.aar"))

  implementation(AndroidX.navigation.fragmentKtx)
  implementation(AndroidX.navigation.uiKtx)
  implementation(AndroidX.constraintLayout)
  implementation(AndroidX.preferenceKtx)
  implementation("org.jetbrains.kotlin:kotlin-reflect:_")

  implementation("com.google.code.gson:gson:_")
  implementation(AndroidX.lifecycle.runtimeKtx)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation(AndroidX.core.ktx)
  implementation(AndroidX.appCompatResources)
  implementation(AndroidX.appCompat)


  //implementation("androidx.datastore:datastore-preferences:1.0.0-alpha08")
  implementation("androidx.datastore:datastore-preferences:_")



  //implementation("org.jetbrains.kotlin:kotlin-reflect:_")


  //implementation("com.github.ligi:ipfs-api-kotlin:_")
  // implementation("com.github.ipfs:java-ipfs-http-client:_")

  //sliding panel library

  //implementation(Libs.slf4j_android)
  //implementation("org.slf4j:slf4j-api:_")
  //implementation(Libs.slf4j)


//  implementation("com.mikepenz:fontawesome-typeface:_")
  //implementation("com.mikepenz:google-material-typeface:_")
  implementation("com.mikepenz:iconics-core:_")
  implementation("com.mikepenz:material-design-iconic-typeface:_@aar")


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

 // implementation(files("/home/dan/workspace/android/ipfsd/go/ipfsd.aar"))

//  implementation(project(":menu"))
  testImplementation(Kotlin.Test.junit)




  androidTestImplementation(Testing.junit4)

  androidTestImplementation(AndroidX.test.core)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.test.rules)
  androidTestImplementation(AndroidX.test.ext.junitKtx)
  androidTestImplementation("com.google.truth:truth:_")



//  testImplementation(testFixtures(project(":api")))


}
