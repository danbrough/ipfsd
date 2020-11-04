package danbroid.ipfsd.apidemo.content

import danbroid.ipfs.api.API
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import java.util.*

fun MenuItemBuilder.pubSubCommands() = menu {
  title = "PubSub Commands"

  menu {
    title = "PubSub Publish"
    onClick = {
      API.PubSub.publish(
        "poiqwe098123",
        "Hello from the IPFS app at ${Date()}\n "
      ).get(executor).also {
        debug("RESULT: $it")
      }
      false
    }
  }

  menu {
    title = "Pubsub Test Subscribe"
    onClick = {
      API.PubSub.subscribe("poiqwe098123", discover = true).get(executor).also {
        debug("result: $it")
      }
      false
    }
  }
}