plugins {
  kotlin("jvm")

  `java-library`
  `maven-publish`
  kotlin("plugin.serialization")
  id("org.jetbrains.dokka")
}


java {
  sourceCompatibility = ProjectVersions.JAVA_VERSION
  targetCompatibility = ProjectVersions.JAVA_VERSION
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
    languageVersion = "1.4"
    // freeCompilerArgs = listOf("-Xjvm-default=enable")
    freeCompilerArgs += listOf(
      "-Xopt-in=kotlinx.serialization.InternalSerializationApi",
      "-Xopt-in=kotlinx.coroutines.InternalCoroutinesApi"
    )
  }
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

dependencies {
/* api(project(":common_domain")) {
   exclude(group = "com.android", module = "android")
 }*/


  testImplementation(Kotlin.Test.junit)
  testImplementation("org.slf4j:slf4j-api:_")
  testImplementation("ch.qos.logback:logback-core:_")
  testImplementation("ch.qos.logback:logback-classic:_")

  implementation("org.slf4j:slf4j-api:_")
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.jdk8)
  implementation("org.jetbrains.kotlin:kotlin-reflect:_")

  implementation("com.google.code.gson:gson:_")
  api(Square.okHttp3.okHttp)

  //implementation(KotlinX.serialization.runtimeCommon)

  //compileOnly("org.json:json:_")


  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:_")

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

