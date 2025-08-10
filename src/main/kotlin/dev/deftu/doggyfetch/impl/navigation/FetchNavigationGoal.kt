package dev.deftu.doggyfetch.impl.navigation

import dev.deftu.doggyfetch.api.navigation.NavigationHandle
import dev.deftu.omnicore.common.OmniSounds
import net.minecraft.block.Blocks
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.MobNavigation
import net.minecraft.entity.ai.pathing.PathNodeType
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.tag.FluidTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import java.util.EnumSet

class FetchNavigationGoal(
    private val wolf: WolfEntity,
    private val handle: NavigationHandle,
    private val speed: Double = 1.15,
    private val arriveRadius: Double = 1.75,
    private val maxOwnerDistance: Double = 15.0,
    private val repathingInterval: Int = 10,
    private val hardTimeout: Int = 20 * 25,
    private val noProgressTimeout: Int = 40
) : Goal() {
    private inline val world get() = wolf.world as ServerWorld
    private inline val navigation get() = wolf.navigation

    private var isCompleted = false
    private var isWaiting = false
    private var owner: PlayerEntity? = null
    private var ticks = 0
    private var lastRepath = 0
    private var lastPos = BlockPos(0, 0, 0)
    private var noProgressTicks = 0
    private var lastDistanceSq = Double.MAX_VALUE
    private var isEscapingWater = false

    private var prevWaterPenalty: Float = 0f
    private var prevWaterBorderPenalty: Float = 0f
    private var prevLavaPenalty: Float = 0f
    private var prevFirePenalty: Float = 0f
    private var prevDangerFirePenalty: Float = 0f

    init {
        controls = EnumSet.of(Control.MOVE, Control.JUMP, Control.LOOK)
    }

    override fun canStart(): Boolean {
        if (!wolf.isTamed) {
            return false
        }

        if (wolf.isSitting) {
            wolf.isSitting = false
        }

        return handle.isValid(world) && handle.currentPos(world) != null
    }

    override fun shouldContinue(): Boolean {
        if (
            !wolf.isTamed ||
            isCompleted ||
            owner == null ||
            owner?.isAlive == false ||
            !handle.isValid(world) ||
            ticks >= hardTimeout
        ) {
            return false
        }

        val currentPos = handle.currentPos(world) ?: return false
        return wolf.squaredDistanceTo(currentPos.toCenterPos()) > arriveRadius * arriveRadius
    }

    override fun start() {
        isCompleted = false
        isWaiting = false
        owner = wolf.owner as? PlayerEntity ?: return
        ticks = 0
        lastRepath = -repathingInterval
        lastPos = wolf.blockPos
        noProgressTicks = 0

        if (navigation is MobNavigation) navigation.setCanSwim(true)
        prevWaterPenalty = wolf.getPathfindingPenalty(PathNodeType.WATER)
        prevWaterBorderPenalty = wolf.getPathfindingPenalty(PathNodeType.WATER_BORDER)
        prevLavaPenalty = wolf.getPathfindingPenalty(PathNodeType.LAVA)
        prevFirePenalty = wolf.getPathfindingPenalty(PathNodeType.DAMAGE_FIRE)
        prevDangerFirePenalty = wolf.getPathfindingPenalty(PathNodeType.DANGER_FIRE)

        wolf.setPathfindingPenalty(PathNodeType.WATER, 16f)
        wolf.setPathfindingPenalty(PathNodeType.WATER_BORDER, 4f)
        wolf.setPathfindingPenalty(PathNodeType.LAVA, 1000f)
        wolf.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 16f)
        wolf.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 24f)

        beginNavigation()
    }

    override fun stop() {
        navigation.stop()

        wolf.setPathfindingPenalty(PathNodeType.WATER, prevWaterPenalty)
        wolf.setPathfindingPenalty(PathNodeType.WATER_BORDER, prevWaterBorderPenalty)
        wolf.setPathfindingPenalty(PathNodeType.LAVA, prevLavaPenalty)
        wolf.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, prevFirePenalty)
        wolf.setPathfindingPenalty(PathNodeType.DANGER_FIRE, prevDangerFirePenalty)

        if (isCompleted) {
            val sound = OmniSounds.WOLF_WHINE.event
            wolf.playSound(sound, 1f, 1f)
            FetchNavigator.cancel(wolf)
        }
    }

    override fun tick() {
        ticks++

        if (wolf.isInLava || (wolf.isTouchingWater && wolf.getFluidHeight(FluidTags.WATER) > wolf.swimHeight)) {
            if (!isEscapingWater) {
                wolf.setPathfindingPenalty(PathNodeType.WATER, 24f)
                isEscapingWater = true
            }

            wolf.jumpControl.setActive()
            lastRepath = ticks
            return
        } else if (isEscapingWater) {
            wolf.setPathfindingPenalty(PathNodeType.WATER, 16f)
            isEscapingWater = false
        }

        val owner = owner
        if (owner != null) {
            val distOwnerSq = wolf.squaredDistanceTo(owner)
            if (distOwnerSq > maxOwnerDistance * maxOwnerDistance) {
                if (!navigation.isIdle) navigation.stop()
                wolf.lookAtEntity(owner, 30f, 30f)
                isWaiting = true
                return
            } else if (isWaiting) {
                isWaiting = false
                lastRepath = 0 // force immediate repath
            }
        }

        handle.currentPos(world)?.toCenterPos()?.let { targetPos ->
            val distanceSq = wolf.squaredDistanceTo(targetPos)
            if (distanceSq <= arriveRadius * arriveRadius) {
                isCompleted = true
                return
            }
        }

        if (ticks - lastRepath >= repathingInterval || navigation.isIdle) {
            beginNavigation()
        }

        val distanceSq = handle.currentPos(world)?.toCenterPos()?.let(wolf::squaredDistanceTo) ?: Double.MAX_VALUE
        if (distanceSq + 0.01 >= lastDistanceSq - 0.01) {
            if (wolf.pos.squaredDistanceTo(lastPos.toCenterPos()) < 0.005) {
                noProgressTicks++
            } else {
                noProgressTicks = 0
            }
        } else {
            noProgressTicks = 0
        }

        lastDistanceSq = distanceSq
        lastPos = wolf.blockPos

        if (noProgressTicks >= noProgressTimeout) {
            if (ticks - lastRepath < repathingInterval / 2) {
                stop()
            } else {
                beginNavigation(forced = true)
                noProgressTicks = 0
            }
        }
    }

    private fun beginNavigation(forced: Boolean = false) {
        lastRepath = ticks
        if (handle is NavigationHandle.Entity) {
            val entity = world.getEntityById(handle.id) ?: return
            navigation.startMovingTo(entity, speed)
            return
        }

        if (forced) {
            navigation.stop()
        }

        val targetCenter = handle.currentPos(world)?.toCenterPos() ?: return
        val arrival = (chooseSafeArrival(BlockPos.ofFloored(targetCenter)) ?: BlockPos.ofFloored(targetCenter)).toCenterPos()

        navigation.startMovingTo(arrival.x, arrival.y, arrival.z, speed)
    }

    private fun chooseSafeArrival(target: BlockPos, maxRadius: Int = 4): BlockPos? {
        val world = this@FetchNavigationGoal.world
        findStandableNear(world, target, 0)?.let { return it }

        for (r in 1..maxRadius) {
            val offsets = sequence {
                for (dx in -r..r) {
                    yield(BlockPos(target.x + dx, target.y, target.z + r))
                    yield(BlockPos(target.x + dx, target.y, target.z - r))
                }

                for (dz in -r+1..r-1) {
                    yield(BlockPos(target.x + r, target.y, target.z + dz))
                    yield(BlockPos(target.x - r, target.y, target.z + dz))
                }
            }

            for (p in offsets) {
                findStandableNear(world, p, 2)?.let { return it }
            }
        }

        return null
    }

    private fun findStandableNear(w: ServerWorld, seed: BlockPos, verticalProbe: Int): BlockPos? {
        for (dy in 0..verticalProbe) {
            val down = seed.down(dy)
            if (isStandable(w, down)) {
                return down
            }
        }

        for (dy in 1..verticalProbe) {
            val up = seed.up(dy)
            if (isStandable(w, up)) {
                return up
            }
        }

        return null
    }

    private fun isStandable(w: ServerWorld, pos: BlockPos): Boolean {
        val below = pos.down()
        val stateBelow = w.getBlockState(below)
        if (!stateBelow.isSolidBlock(w, below)) {
            return false
        }

        val feet = w.getBlockState(pos)
        val head = w.getBlockState(pos.up())
        if (!feet.getCollisionShape(w, pos).isEmpty) {
            return false
        }

        if (!head.getCollisionShape(w, pos.up()).isEmpty) {
            return false
        }

        if (!w.getFluidState(pos).isEmpty) {
            return false
        }

        if (!w.getFluidState(pos.up()).isEmpty) {
            return false
        }

        // avoid obviously bad surfaces
        val bad = stateBelow.isOf(Blocks.CACTUS) || stateBelow.isOf(Blocks.MAGMA_BLOCK) || stateBelow.isOf(Blocks.FIRE) || stateBelow.isOf(Blocks.LAVA)
        return !bad
    }
}
