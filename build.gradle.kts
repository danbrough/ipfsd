plugins {
  id("org.jetbrains.dokka")
}

buildscript {

  repositories {
    google()
    jcenter()
  }

  dependencies {
     classpath("com.android.tools.build:gradle:4.1.0-rc03")
   // classpath("com.android.tools.build:gradle:4.2.0-alpha13")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
  }

}


apply("project.gradle.kts")

allprojects {

  repositories {
    google()
    jcenter()
    //maven("https://jitpack.io")
    maven("https://h1.danbrough.org/maven")
   //s maven("https://jitpack.io")
  }

  tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
      configureEach {
        includes.from(file("README.md"))
      }
    }
  }

}

tasks.dokkaGfmMultiModule {
  outputDirectory.set(file("docs"))
}



