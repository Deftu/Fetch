package dev.deftu.doggyfetch.api.data

import net.minecraft.registry.RegistryKey

data class FetchBehavior(
    val targets: List<RegistryKey<FetchTarget>>,
    /** The amount of blocks in each cardinal direction that the wolf will search for this target. This is calculated as a sphere. */
    val maxSearchRadius: Int,
    /** The cooldown in ticks before the wolf can search for this target again. */
    val cooldown: Int
)
