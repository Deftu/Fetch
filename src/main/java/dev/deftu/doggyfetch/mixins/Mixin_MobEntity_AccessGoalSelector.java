package dev.deftu.doggyfetch.mixins;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEntity.class)
public interface Mixin_MobEntity_AccessGoalSelector {
    @Accessor GoalSelector getGoalSelector();
}
