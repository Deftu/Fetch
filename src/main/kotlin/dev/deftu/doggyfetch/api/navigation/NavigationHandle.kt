package dev.deftu.doggyfetch.api.navigation

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

sealed interface NavigationHandle {
    fun currentPos(world: ServerWorld): BlockPos?
    fun isValid(world: ServerWorld): Boolean

    data class Entity(val id: Int) : NavigationHandle {
        override fun currentPos(world: ServerWorld): BlockPos? {
            return world.getEntityById(id)?.blockPos
        }

        override fun isValid(world: ServerWorld): Boolean {
            return world.getEntityById(id) != null
        }
    }

    data class Block(val pos: BlockPos) : NavigationHandle {
        override fun currentPos(world: ServerWorld): BlockPos {
            return pos
        }

        override fun isValid(world: ServerWorld): Boolean {
            return !world.getBlockState(pos).isAir
        }
    }
}
