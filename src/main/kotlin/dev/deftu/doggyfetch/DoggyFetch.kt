package dev.deftu.doggyfetch

import dev.deftu.doggyfetch.api.FetchRegistries
import dev.deftu.doggyfetch.api.FetchRegistryCache
import dev.deftu.doggyfetch.api.data.TargetType
import dev.deftu.doggyfetch.api.locator.LocatorRegistry
import dev.deftu.doggyfetch.impl.FetchAllocator
import dev.deftu.doggyfetch.impl.locator.BiomeLocator
import dev.deftu.doggyfetch.impl.locator.BlockLocator
import dev.deftu.doggyfetch.impl.locator.EntityLocator
import dev.deftu.doggyfetch.impl.locator.StructureLocator
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.util.ActionResult

object DoggyFetch {
    fun onInitializeCommon() {
        FetchRegistries.initialize()
        FetchRegistryCache.initialize()
        registerDefaultLocators()

        UseEntityCallback.EVENT.register { player, world, hand, entity, hitResult ->
            if (world.isClient || entity !is WolfEntity) {
                return@register ActionResult.PASS
            }

            FetchAllocator.attemptStartFetch(player, entity, player.getStackInHand(hand))
        }
    }

    private fun registerDefaultLocators() {
        LocatorRegistry.register(TargetType.STRUCTURE, StructureLocator)
        LocatorRegistry.register(TargetType.ENTITY, EntityLocator)
        LocatorRegistry.register(TargetType.BLOCK, BlockLocator)
        LocatorRegistry.register(TargetType.BIOME, BiomeLocator)
    }
}
