plugins {
  kotlin("jvm")
  `java-library`
  `maven-publish`
  kotlin("plugin.serialization")
  id("org.jetbrains.dokka")
  // `java-test-fixtures`

}


java {
  sourceCompatibility = ProjectVersions.JAVA_VERSION
  targetCompatibility = ProjectVersions.JAVA_VERSION
}


//languageSettings.useExperimentalAnnotation("org.mylibrary.OptInAnnotation")

/*
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.serialization.InternalSerializationApi"
}
*/


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

repositories {
  maven("https://h1.danbrough.org/maven")
  maven("https://jitpack.io")
}

dependencies {


  testImplementation(Kotlin.Test.junit)

  api(Danbroid.utils.logging_core)
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.jdk8)

  api("org.jetbrains.kotlin:kotlin-reflect:_")

  implementation("com.google.code.gson:gson:_")
  api(Square.okHttp3.okHttp)
  implementation("com.github.ipld:java-cid:_")
  implementation("com.github.ipfs:java-ipfs-http-client:_")

  //implementation(KotlinX.serialization.runtimeCommon)

  //compileOnly("org.json:json:_")

  //testImplementation(files("/home/dan/workspace/android/ipfsd/go/ipfsd.aar"))

  api("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:_")
  //testFixturesImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:_")
  // testFixturesImplementation(KotlinX.coroutines.jdk8)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = ProjectVersions.KOTLIN_JVM_VERSION
    //languageVersion = "1.4"
    // freeCompilerArgs = listOf("-Xjvm-default=enable")
    freeCompilerArgs += listOf(
      "-Xopt-in=kotlinx.serialization.InternalSerializationApi"
    )
  }
}

val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(sourceSets.getByName("main").java.srcDirs)
}

group = ProjectVersions.GROUP_ID
version = ProjectVersions.VERSION_NAME

publishing {
  publications {
    create<MavenPublication>("default") {
      from(components["java"])
      artifact(sourcesJar)
    }
  }
}

