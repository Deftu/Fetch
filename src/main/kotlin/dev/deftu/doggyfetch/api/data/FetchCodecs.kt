package dev.deftu.doggyfetch.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deftu.doggyfetch.api.FetchRegistries
import dev.deftu.omnicore.common.codecs.OmniCodecs
import net.minecraft.registry.RegistryKey

object FetchCodecs {

    @JvmField
    val TARGET: Codec<FetchTarget> = TargetType.REGISTRY.getCodec()
        .dispatch(FetchTarget::type, TargetType<*>::codec)

    @JvmField
    val TARGET_REF: Codec<RegistryKey<FetchTarget>> = RegistryKey.createCodec(FetchRegistries.TARGETS)

    @JvmField
    val BEHAVIOR = RecordCodecBuilder.create { instance ->
        instance.group(
            OmniCodecs.singleOrList(
                "target",
                "targets",
                TARGET_REF,
            ).forGetter(FetchBehavior::targets),
            Codec.INT.fieldOf("max_search_radius").forGetter(FetchBehavior::maxSearchRadius),
            Codec.INT.fieldOf("cooldown").forGetter(FetchBehavior::cooldown)
        ).apply(instance, ::FetchBehavior)
    }

    @JvmField
    val BINDING = RecordCodecBuilder.create { instance ->
        instance.group(
            OmniCodecs.singleOrList(
                "item",
                "items",
                OmniCodecs.IDENTIFIER
            ).forGetter(ItemBehaviorBinding::items),
            OmniCodecs.IDENTIFIER.fieldOf("behavior").forGetter(ItemBehaviorBinding::behavior)
        ).apply(instance, ::ItemBehaviorBinding)
    }

}
