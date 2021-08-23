plugins {
  id("de.fayard.refreshVersions") version "0.11.0"
}

include(":api")
if (System.getenv().containsKey("JITPACK")) {
  include(":bridge_native", ":bridge")
} else {

  include(":client", ":service", ":demos:app", ":demos:api", ":demos:shopping")
  // include(":bridge_native", ":bridge")
}

rootProject.name = "ipfsd"


if (false) {
  include(":menu", ":slf4j", ":misc")
  project(":menu").projectDir = file("../androidutils/menu")
  project(":slf4j").projectDir = file("../androidutils/slf4j")
  project(":misc").projectDir = file("../androidutils/misc")
}

/*import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies


buildscript {
  repositories {
    gradlePluginPortal()
    jcenter()
  }

  dependencies.classpath("de.fayard:dependencies:+")

  // println("Danbroid: ${Danbroid.local_utils}")
}


bootstrapRefreshVersionsAndDependencies()*/





