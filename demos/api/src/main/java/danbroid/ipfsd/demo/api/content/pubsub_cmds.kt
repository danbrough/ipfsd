package danbroid.ipfsd.demo.api.content

import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import kotlinx.coroutines.flow.collect
import java.util.*

fun MenuItemBuilder.pubSubCommands() = menu {
  title = "PubSub Commands"

  menu {
    title = "PubSub Publish"
    onClick = {
      api.pubSub.publish(
        "poiqwe098123",
        "Hello from the IPFS app at ${Date()}\n "
      ).flow().collect {
        log.info("msg: $it")
      }
      false
    }
  }

  menu {
    title = "Pubsub Test Subscribe"
    onClick = {
      api.pubSub.subscribe("poiqwe098123", discover = true).get().also {
        debug("result: $it")
      }
      false
    }
  }
}