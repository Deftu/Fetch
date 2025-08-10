package dev.deftu.doggyfetch.impl.locator

import dev.deftu.doggyfetch.api.data.FetchBehavior
import dev.deftu.doggyfetch.api.data.FetchTarget
import dev.deftu.doggyfetch.api.locator.FetchResult
import dev.deftu.doggyfetch.api.locator.Locator
import dev.deftu.doggyfetch.api.locator.LocatorContext
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World

object EntityLocator : Locator<FetchTarget.EntityTarget> {
    override fun locate(ctx: LocatorContext, behavior: FetchBehavior, target: FetchTarget.EntityTarget): FetchResult {
        val type = resolveEntityType(ctx.world, target.id) ?: return FetchResult.NotFound
        val centeredOrigin = ctx.origin.toCenterPos()
        val searchBoundingBox = Box(centeredOrigin, centeredOrigin).expand(behavior.maxSearchRadius.toDouble())
        val entities = ctx.world.getEntitiesByType(type, searchBoundingBox) { true }
        if (entities.isEmpty()) {
            return FetchResult.NotFound
        }

        val closestEntity = entities.minByOrNull { it.squaredDistanceTo(centeredOrigin) } ?: return FetchResult.NotFound
        return FetchResult.Entity(closestEntity.id)
    }

    private fun resolveEntityType(world: World, identifier: Identifier): EntityType<*>? {
        return world.registryManager
            .getOrThrow(RegistryKeys.ENTITY_TYPE)
            .get(identifier)
    }
}
