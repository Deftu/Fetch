package dev.deftu.doggyfetch.impl.locator

import dev.deftu.doggyfetch.api.data.FetchBehavior
import dev.deftu.doggyfetch.api.data.FetchTarget
import dev.deftu.doggyfetch.api.locator.FetchResult
import dev.deftu.doggyfetch.api.locator.Locator
import dev.deftu.doggyfetch.api.locator.LocatorContext
import dev.deftu.doggyfetch.utils.Spiral
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.World

object BlockLocator : Locator<FetchTarget.BlockTarget> {
    override fun locate(ctx: LocatorContext, behavior: FetchBehavior, target: FetchTarget.BlockTarget): FetchResult {
        val type = resolveBlockType(ctx.world, target.id) ?: return FetchResult.NotFound
        for (pos in Spiral.around(ctx.origin, behavior.maxSearchRadius)) {
            if (ctx.world.getBlockState(pos).block == type) {
                return FetchResult.Block(pos)
            }
        }

        return FetchResult.NotFound
    }

    private fun resolveBlockType(world: World, identifier: Identifier): net.minecraft.block.Block? {
        return world.registryManager
            .getOrThrow(RegistryKeys.BLOCK)
            .get(identifier)
    }
}
