package dev.deftu.doggyfetch.api

import dev.deftu.doggyfetch.api.data.FetchBehavior
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Identifier
import java.util.concurrent.ConcurrentHashMap
import kotlin.jvm.optionals.getOrNull

object FetchRegistryCache {
    private val itemMappings = ConcurrentHashMap<Identifier, FetchBehavior>()

    fun initialize() {
        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            println("DoggyFetch: Initially building fetch registry cache...")
            rebuildCache(server)
        }

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register { server, _, isSuccessfulReload ->
            println("DoggyFetch: Rebuilding fetch registry cache...")
            if (!isSuccessfulReload) {
                return@register
            }

            rebuildCache(server)
        }
    }

    private fun rebuildCache(server: MinecraftServer) {
        this.itemMappings.clear()

        val behaviors = FetchRegistries.getBehaviorRegistry(server).getOrNull() ?: return
        println("DoggyFetch: Found ${behaviors.size()} fetch behaviors in registry")
        val bindings = FetchRegistries.getBindingRegistry(server).getOrNull() ?: return
        println("DoggyFetch: Found ${bindings.size()} item associations in registry")
        for ((_, binding) in bindings.entrySet) {
            val behavior = behaviors.get(binding.behavior) ?: continue
            for (item in binding.items) {
                println("DoggyFetch: Associating item $item with behavior $behavior")
                this.itemMappings[item] = behavior
            }
        }
    }

    fun resolve(item: Item): FetchBehavior? {
        return itemMappings[Registries.ITEM.getId(item)]
    }
}
