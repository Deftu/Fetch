package dev.deftu.doggyfetch.api.locator

import dev.deftu.doggyfetch.api.FetchRegistries
import dev.deftu.doggyfetch.api.data.FetchBehavior
import dev.deftu.doggyfetch.api.data.FetchTarget
import kotlin.jvm.optionals.getOrNull

object LocatorOrchestrator {
    fun locate(ctx: LocatorContext, behavior: FetchBehavior): FetchResult {
        val server = ctx.world.server ?: return FetchResult.NotFound
        val targetRegistry = FetchRegistries.getTargetRegistry(server)
            .getOrNull() ?: return FetchResult.NotFound

        for (id in behavior.targets) {
            val target = targetRegistry.get(id) ?: continue
            val locator = LocatorRegistry[target.type] ?: continue
            val result = performLocation(locator, ctx, behavior, target)
            if (result !is FetchResult.NotFound) {
                return result
            }
        }

        return FetchResult.NotFound
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : FetchTarget> performLocation(
        locator: Locator<T>,
        ctx: LocatorContext,
        behavior: FetchBehavior,
        target: FetchTarget
    ): FetchResult {
        return locator.locate(ctx, behavior, target as T)
    }
}
