package dev.deftu.doggyfetch.impl.locator

import dev.deftu.doggyfetch.api.data.FetchBehavior
import dev.deftu.doggyfetch.api.data.FetchTarget
import dev.deftu.doggyfetch.api.locator.FetchResult
import dev.deftu.doggyfetch.api.locator.Locator
import dev.deftu.doggyfetch.api.locator.LocatorContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld

object BiomeLocator : Locator<FetchTarget.BiomeTarget> {
    override fun locate(ctx: LocatorContext, behavior: FetchBehavior, target: FetchTarget.BiomeTarget): FetchResult {
        val world = ctx.world as? ServerWorld ?: return FetchResult.NotFound
        val posAndEntry = world.locateBiome(
            { entry ->
                world.registryManager
                    .getOrThrow(RegistryKeys.BIOME)
                    .getId(entry.comp_349()) == target.id
            },
            ctx.origin,
            behavior.maxSearchRadius,
            32,
            64
        )

        val pos = posAndEntry?.first ?: return FetchResult.NotFound
        return FetchResult.Block(pos)
    }
}
