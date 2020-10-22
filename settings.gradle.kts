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
include( ":service", ":client", ":api", ":demo_shopping", ":demo_chat",":demo_api")

rootProject.name = "ipfsd"




if (true) {
  include(":menu", ":slf4j", ":misc")
  project(":menu").projectDir = file("../androidutils/menu")
  project(":slf4j").projectDir = file("../androidutils/slf4j")
  project(":misc").projectDir = file("../androidutils/misc")
}





