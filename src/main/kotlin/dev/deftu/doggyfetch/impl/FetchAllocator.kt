package dev.deftu.doggyfetch.impl

import dev.deftu.doggyfetch.api.FetchRegistryCache
import dev.deftu.doggyfetch.api.locator.FetchResult
import dev.deftu.doggyfetch.api.locator.LocatorContext
import dev.deftu.doggyfetch.api.locator.LocatorOrchestrator
import dev.deftu.doggyfetch.impl.navigation.FetchNavigator
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult

object FetchAllocator {

    fun attemptStartFetch(
        player: PlayerEntity,
        wolf: WolfEntity,
        stack: ItemStack,
    ): ActionResult {
        if (!player.isSneaking || !wolf.isTamed || wolf.isBaby || wolf.owner != player) {
            return ActionResult.PASS
        }

        println("FetchAllocator: Attempting to start fetch with item ${stack.item}")
        val behavior = FetchRegistryCache.resolve(stack.item)
            ?: return ActionResult.PASS
        println("FetchAllocator: Found association for item ${stack.item}: $behavior")
        val result = LocatorOrchestrator.locate(
            ctx = LocatorContext(
                world = player.world,
                origin = wolf.blockPos,
            ),
            behavior = behavior
        )

        println("FetchAllocator: Attempting to start fetch with item ${stack.item}, result: $result")
        if (result is FetchResult.NotFound) {
            return ActionResult.PASS
        }

        if (!FetchNavigator.begin(wolf, result)) {
            println("FetchAllocator: Failed to start fetch navigation for wolf ${wolf.name?.string ?: "unknown"}")
            return ActionResult.PASS
        }

        return ActionResult.SUCCESS
    }

}
