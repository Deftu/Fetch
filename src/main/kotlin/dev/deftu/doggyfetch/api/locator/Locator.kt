package dev.deftu.doggyfetch.api.locator

import dev.deftu.doggyfetch.api.data.FetchBehavior
import dev.deftu.doggyfetch.api.data.FetchTarget

fun interface Locator<T : FetchTarget> {
    fun locate(ctx: LocatorContext, behavior: FetchBehavior, target: T): FetchResult
}
