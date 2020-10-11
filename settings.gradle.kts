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
include(":app", ":ipfsd", ":api")

rootProject.name = "ipfs_daemon"



//println("initProps()")
val fis = java.io.FileInputStream(file("project.properties"))
val props = java.util.Properties()
props.load(fis)
fis.close()
val localUtils: Boolean = props.get("localUtils") == "true"


if (localUtils) {
  include(":menu", ":slf4j", ":misc")
  project(":menu").projectDir = file("../androidutils/menu")
  project(":slf4j").projectDir = file("../androidutils/slf4j")
  project(":misc").projectDir = file("../androidutils/misc")
}





