package dev.deftu.doggyfetch.utils

import dev.deftu.omnicore.common.OmniBlockPos
import net.minecraft.util.math.BlockPos
import kotlin.math.abs

internal object Spiral {
    /**
     * Square spiral around [center], clamped by [radius] in taxicab distance.
     * Iterates Y from top to bottom around the origin column (cheap heuristic).
     */
    fun around(
        center: BlockPos,
        radius: Int,
        yRange: IntProgression = ((center.y + 6) downTo (center.y - 6))
    ): Sequence<BlockPos> = sequence {
        yield(center)
        var x = 0
        var z = 0
        var dx = 0
        var dz = -1
        val max = radius * radius * 4 // plenty
        for (i in 0 until max) {
            if (abs(x) <= radius && abs(z) <= radius) {
                for (y in yRange) yield(OmniBlockPos.from(center.x + x, y, center.z + z))
            }
            if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                val t = dx; dx = -dz; dz = t
            }
            x += dx; z += dz
        }
    }
}
