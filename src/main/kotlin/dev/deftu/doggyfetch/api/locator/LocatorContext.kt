package dev.deftu.doggyfetch.api.locator

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class LocatorContext(
    val world: World,
    val origin: BlockPos
)
