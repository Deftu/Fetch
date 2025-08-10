package dev.deftu.doggyfetch.api.data

import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import dev.deftu.doggyfetch.DoggyFetchConstants
import dev.deftu.omnicore.common.OmniIdentifier
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry

data class TargetType<T : FetchTarget>(val codec: MapCodec<T>) {
    companion object {
        @JvmField
        val REGISTRY = SimpleRegistry<TargetType<*>>(
            RegistryKey.ofRegistry(OmniIdentifier.create(DoggyFetchConstants.ID, "fetch_target_types")),
            Lifecycle.stable()
        )

        @JvmField
        val STRUCTURE = register("structure", TargetType(FetchTarget.StructureTarget.CODEC))

        @JvmField
        val ENTITY = register("entity", TargetType(FetchTarget.EntityTarget.CODEC))

        @JvmField
        val BLOCK = register("block", TargetType(FetchTarget.BlockTarget.CODEC))

        @JvmField
        val BIOME = register("biome", TargetType(FetchTarget.BiomeTarget.CODEC))

        private fun <T : FetchTarget> register(
            id: String,
            type: TargetType<T>
        ): TargetType<T> {
            return Registry.register(REGISTRY, OmniIdentifier.create(DoggyFetchConstants.ID, id), type)
        }
    }
}
