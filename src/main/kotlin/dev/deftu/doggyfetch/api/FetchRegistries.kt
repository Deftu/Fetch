package dev.deftu.doggyfetch.api

import dev.deftu.doggyfetch.DoggyFetchConstants
import dev.deftu.doggyfetch.api.data.FetchBehavior
import dev.deftu.doggyfetch.api.data.FetchCodecs
import dev.deftu.doggyfetch.api.data.FetchTarget
import dev.deftu.doggyfetch.api.data.ItemBehaviorBinding
import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.server.MinecraftServer
import java.util.Optional

object FetchRegistries {

    @JvmField val TARGETS   = RegistryKey.ofRegistry<FetchTarget>(DoggyFetchConstants.id("targets"))
    @JvmField val BEHAVIORS = RegistryKey.ofRegistry<FetchBehavior>(DoggyFetchConstants.id("behaviors"))
    @JvmField val BINDINGS  = RegistryKey.ofRegistry<ItemBehaviorBinding>(DoggyFetchConstants.id("item_associations"))

    fun initialize() {
        DynamicRegistries.register(TARGETS, FetchCodecs.TARGET)
        DynamicRegistries.register(BEHAVIORS, FetchCodecs.BEHAVIOR)
        DynamicRegistries.register(BINDINGS, FetchCodecs.BINDING)
    }

    @JvmStatic
    fun getTargetRegistry(server: MinecraftServer): Optional<Registry<FetchTarget>> {
        return server.registryManager.getOptional(TARGETS)
    }

    @JvmStatic
    fun getBehaviorRegistry(server: MinecraftServer): Optional<Registry<FetchBehavior>> {
        return server.registryManager.getOptional(BEHAVIORS)
    }

    @JvmStatic
    fun getBindingRegistry(server: MinecraftServer): Optional<Registry<ItemBehaviorBinding>> {
        return server.registryManager.getOptional(BINDINGS)
    }

}
