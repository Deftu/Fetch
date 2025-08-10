package dev.deftu.doggyfetch

import dev.deftu.omnicore.common.OmniIdentifier
import net.minecraft.util.Identifier

object DoggyFetchConstants {
    const val NAME = "@MOD_NAME@"
    const val ID = "@MOD_ID@"
    const val VERSION = "@MOD_VERSION@"

    @JvmStatic
    fun id(path: String): Identifier {
        return OmniIdentifier.create(ID, path)
    }
}
