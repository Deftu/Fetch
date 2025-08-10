package dev.deftu.doggyfetch.utils

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deftu.omnicore.common.codecs.OmniCodecs
import net.minecraft.util.Identifier

interface IdentifierHolder {
    companion object {
        fun <T : IdentifierHolder> createBasicIdentifierCodec(
            create: (Identifier) -> T
        ): MapCodec<T> {
            return RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    OmniCodecs.IDENTIFIER.fieldOf("id").forGetter(IdentifierHolder::id)
                ).apply(instance) { identifier ->
                    create(identifier)
                }
            }
        }
    }

    val id: Identifier
}
