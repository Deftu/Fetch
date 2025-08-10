package dev.deftu.doggyfetch.api.data

import net.minecraft.util.Identifier

data class ItemBehaviorBinding(
    val items: List<Identifier> = emptyList(), // explicit item ids
    val behavior: Identifier, // id in doggyfetch:behaviors
)
