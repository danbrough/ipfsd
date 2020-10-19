package danbroid.ipfsd.demo.content

import danbroid.ipfs.api.API
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import java.util.*

fun MenuItemBuilder.pubSubCommands() = menu {
  title = "PubSub Commands"

  menu {
    title = "PubSub Publish"
    onClick = {
      executor.exec(
        API.PubSub.publish(
          "poiqwe098123",
          "Hello from the IPFS app at ${Date()}\n "
        )
      ) {
        debug("RESULT: $it")
      }
    }
  }

  menu {
    title = "Pubsub Test Subscribe"
    onClick = {

      executor.exec(API.PubSub.subscribe("poiqwe098123", discover = true)) {
        debug("result: ${it.getOrNull()}")
      }

    }
  }
}