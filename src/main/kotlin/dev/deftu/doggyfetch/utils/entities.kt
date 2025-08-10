package dev.deftu.doggyfetch.utils

import dev.deftu.doggyfetch.mixins.Mixin_MobEntity_AccessGoalSelector
import net.minecraft.entity.ai.goal.GoalSelector
import net.minecraft.entity.mob.MobEntity

val MobEntity.goalSelector: GoalSelector
    get() = (this as Mixin_MobEntity_AccessGoalSelector).goalSelector
