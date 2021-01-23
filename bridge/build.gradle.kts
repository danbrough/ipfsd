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


/*val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(android.sourceSets.getByName("main").java.srcDirs)
}


afterEvaluate {
  publishing {
    //val projectName = name
    publications {
      // Creates a Maven publication called "release".
      create<MavenPublication>("release") {
        from(components["release"])
        artifact(sourcesJar.get())
        version = Danbroid.bridge_version
        groupId = ProjectVersions.GROUP_ID
       // artifactId = projectName
        // Applies the component for the release build variant.
        *//*  from components . release

              // You can then customize attributes of the publication as shown below.
              groupId = 'com.example.MyLibrary'
          artifactId = 'final'
          version = '1.0'*//*
      }
      // Creates a Maven publication called “debug”.

    }
  }
}*/

/*

        val sourcesJar by tasks.registering(Jar::class) {
          archiveClassifier.set("sources")
          from(sourceSets.getByName("main").java.srcDirs)
        }


        afterEvaluate {
          publishing.apply {
            val projectName = name
            publications {
              val release by registering(MavenPublication::class) {
                from(components["release"])
                artifact(sourcesJar.get())
                artifactId = projectName
                groupId = ProjectVersions.GROUP_ID
                version = defaultConfig.versionName
                maven = true
              }
            }
          }
        }
 */

/*
val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(sourceSets.getByName("main").java.srcDirs)
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = ProjectVersions.GROUP_ID
      artifact(sourcesJar.get())

      //artifactId = "library"
     // version = defaultConfig.versionName

      from(components["release"])
    }
  }
}
*/


dependencies {
  implementation(AndroidX.annotation)
}


