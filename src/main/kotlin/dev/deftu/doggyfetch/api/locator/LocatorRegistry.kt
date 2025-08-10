package dev.deftu.doggyfetch.api.locator

import dev.deftu.doggyfetch.api.data.FetchTarget
import dev.deftu.doggyfetch.api.data.TargetType

object LocatorRegistry {
    private val locators = mutableMapOf<TargetType<*>, Locator<*>>()

    operator fun <T : FetchTarget> get(type: TargetType<T>): Locator<T>? {
        @Suppress("UNCHECKED_CAST")
        return locators[type] as Locator<T>?
    }

    fun <T : FetchTarget> register(type: TargetType<T>, locator: Locator<T>) {
        if (locators.containsKey(type)) {
            throw IllegalArgumentException("Locator for type $type is already registered.")
        }

        locators[type] = locator
    }
}
