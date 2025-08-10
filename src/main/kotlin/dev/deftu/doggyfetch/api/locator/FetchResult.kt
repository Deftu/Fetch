package dev.deftu.doggyfetch.api.locator

import net.minecraft.util.math.BlockPos

sealed interface FetchResult {
    data class Block(val pos: BlockPos) : FetchResult
    data class Entity(val id: Int) : FetchResult
    data object NotFound : FetchResult
}
