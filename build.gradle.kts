plugins {
  id("org.jetbrains.dokka")
}

buildscript {

  repositories {
    google()
    jcenter()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.1.0")
    //classpath("com.android.tools.build:gradle:4.2.0-alpha13")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
    classpath("org.jetbrains.dokka:dokka-gradle-plugin:_")

  }

}

apply("project.gradle.kts")


allprojects {

  repositories {
    //mavenLocal()
    google()
    jcenter()
    maven("https://jitpack.io")
    //maven("https://h1.danbrough.org/maven")
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




