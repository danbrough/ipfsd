package danbroid.ipfsd.demo.api.content

import danbroid.ipfs.api.flow
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import kotlinx.coroutines.flow.collect
import java.util.*

fun MenuItemBuilder.pubSubCommands() = menu {
  title = "PubSub Commands"

  menu {
    title = "PubSub Publish"
    onClick = {
      api.pubsub.pub(
        "poiqwe098123",
        "Hello from the IPFS app at ${Date()}\n "
      ).invoke().also {
        log.info("msg: ${it.text()}")
      }
    }
  }

  menu {
    title = "Pubsub Test Subscribe"
    onClick = {
      api.pubsub.sub("poiqwe098123").flow().collect {
        log.debug("result: $it")
      }
    }
  }
}