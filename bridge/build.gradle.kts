plugins {
  id("com.android.library")
  id("org.jetbrains.dokka")
  `maven-publish`
}

android {
 // ndkVersion = ProjectVersions.NDK_VERSION

  compileSdkVersion(ProjectVersions.SDK_VERSION)
  defaultConfig {
    minSdkVersion(ProjectVersions.MIN_SDK_VERSION)
    targetSdkVersion(ProjectVersions.SDK_VERSION)
    versionCode = 1
    versionName = Danbroid.bridge_version
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }


  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }


  testOptions {
    unitTests.isIncludeAndroidResources = true
    unitTests.isReturnDefaultValues = true
  }

/*  signingConfigs {
    register("release") {
      storeFile = file("/home/dan/.android/ipfsd.keystore")
      keyAlias = "ipfsd"
      storePassword = KeystoreConfig.PASSWORD
      keyPassword = KeystoreConfig.PASSWORD
    }
  }*/

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
      )
      consumerProguardFiles("proguard-rules.pro")
      //signingConfig = signingConfigs.getByName("release")
    }
  }

}

dependencies {
    implementation(project(":bridge_native"))
}
