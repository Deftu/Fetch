package dev.deftu.doggyfetch.impl.navigation

import dev.deftu.doggyfetch.api.locator.FetchResult
import dev.deftu.doggyfetch.api.navigation.NavigationHandle
import dev.deftu.doggyfetch.utils.goalSelector
import net.minecraft.entity.passive.WolfEntity
import java.util.WeakHashMap

object FetchNavigator {
    private val activeGoals = WeakHashMap<WolfEntity, FetchNavigationGoal>()

    fun begin(
        wolf: WolfEntity,
        result: FetchResult,
    ): Boolean {
        val handle = when (result) {
            is FetchResult.Block -> NavigationHandle.Block(result.pos)
            is FetchResult.Entity -> NavigationHandle.Entity(result.id)
            else -> return false
        }

        cancel(wolf) // Cancel any existing navigation

        val goal = FetchNavigationGoal(wolf, handle)
        wolf.isSitting = false
        wolf.goalSelector.add(0, goal)
        activeGoals[wolf] = goal
        return true
    }

    fun cancel(wolf: WolfEntity) {
        activeGoals.remove(wolf)?.let { goal ->
            wolf.goalSelector.remove(goal)
            wolf.navigation.stop()
        }
    }
}
