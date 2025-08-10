package dev.deftu.doggyfetch.api.data

import com.mojang.serialization.MapCodec
import dev.deftu.doggyfetch.utils.IdentifierHolder
import net.minecraft.util.Identifier

/**
 * Some "thing" which a wolf can lead you to or fetch for you.
 * Implementations of this interface represent pure data, and do not
 * provide the locating logic itself.
 */
interface FetchTarget {
    data class StructureTarget(override val id: Identifier) : FetchTarget, IdentifierHolder {
        companion object {
            @JvmField
            val CODEC: MapCodec<StructureTarget> = IdentifierHolder.createBasicIdentifierCodec(::StructureTarget)
        }

        override val type: TargetType<StructureTarget> = TargetType.STRUCTURE
    }

    data class EntityTarget(override val id: Identifier) : FetchTarget, IdentifierHolder {
        companion object {
            @JvmField
            val CODEC: MapCodec<EntityTarget> = IdentifierHolder.createBasicIdentifierCodec(::EntityTarget)
        }

        override val type: TargetType<EntityTarget> = TargetType.ENTITY
    }

    data class BlockTarget(override val id: Identifier) : FetchTarget, IdentifierHolder {
        companion object {
            @JvmField
            val CODEC: MapCodec<BlockTarget> = IdentifierHolder.createBasicIdentifierCodec(::BlockTarget)
        }

        override val type: TargetType<BlockTarget> = TargetType.BLOCK
    }

    data class BiomeTarget(override val id: Identifier) : FetchTarget, IdentifierHolder {
        companion object {
            @JvmField
            val CODEC: MapCodec<BiomeTarget> = IdentifierHolder.createBasicIdentifierCodec(::BiomeTarget)
        }

        override val type: TargetType<BiomeTarget> = TargetType.BIOME
    }

    /** The unique identifier for this target type. Used for registration and lookup. */
    val type: TargetType<*>
}
