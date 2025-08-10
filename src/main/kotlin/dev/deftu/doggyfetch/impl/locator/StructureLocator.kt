package dev.deftu.doggyfetch.impl.locator

import dev.deftu.doggyfetch.api.data.FetchBehavior
import dev.deftu.doggyfetch.api.data.FetchTarget
import dev.deftu.doggyfetch.api.locator.FetchResult
import dev.deftu.doggyfetch.api.locator.Locator
import dev.deftu.doggyfetch.api.locator.LocatorContext
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World
import net.minecraft.world.gen.structure.Structure

object StructureLocator : Locator<FetchTarget.StructureTarget> {
    override fun locate(
        ctx: LocatorContext,
        behavior: FetchBehavior,
        target: FetchTarget.StructureTarget
    ): FetchResult {
        val world = ctx.world as? ServerWorld ?: return FetchResult.NotFound
        val registry = resolveStructureRegistry(world)
        val list = registry
            .getOptional(RegistryKey.of(RegistryKeys.STRUCTURE, target.id))
            .map(RegistryEntryList<Structure>::of)
        if (!list.isPresent) {
            return FetchResult.NotFound
        }

        val radiusChunks = (behavior.maxSearchRadius / 16)
        val posAndEntry = world
            .chunkManager
            .chunkGenerator
            .locateStructure(
                world,
                list.get(),
                ctx.origin,
                radiusChunks,
                true
            )
        val pos = posAndEntry?.first ?: return FetchResult.NotFound
        return FetchResult.Block(pos)
    }

    private fun resolveStructureRegistry(world: World): Registry<Structure> {
        return world.registryManager.getOrThrow(RegistryKeys.STRUCTURE)
    }
}
