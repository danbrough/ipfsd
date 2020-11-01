import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies

buildscript {
  repositories {
    gradlePluginPortal()
    jcenter()
  }

  dependencies.classpath("de.fayard:dependencies:+")

  // println("Danbroid: ${Danbroid.local_utils}")
}


bootstrapRefreshVersionsAndDependencies()
include(":service", ":client", ":api", ":demo_app", ":demo_api")

rootProject.name = "ipfsd"

if (false) {
  include(":menu", ":slf4j", ":misc")
  project(":menu").projectDir = file("../androidutils/menu")
  project(":slf4j").projectDir = file("../androidutils/slf4j")
  project(":misc").projectDir = file("../androidutils/misc")
}





