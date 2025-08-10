plugins {
    java
    kotlin("jvm")
    id("dev.deftu.gradle.multiversion")
    id("dev.deftu.gradle.tools")
    id("dev.deftu.gradle.tools.resources")
    id("dev.deftu.gradle.tools.bloom")
    id("dev.deftu.gradle.tools.shadow")
    id("dev.deftu.gradle.tools.minecraft.loom")
    id("dev.deftu.gradle.tools.minecraft.api")
    id("dev.deftu.gradle.tools.minecraft.releases-v2")
}

toolkitMultiversion {
    moveBuildsToRootProject.set(true)
}

toolkitLoomHelper {
    if (!mcData.isNeoForge) {
        useMixinRefMap(modData.id)
    }

    if (mcData.isForge) {
        useTweaker("org.spongepowered.asm.launch.MixinTweaker")
        useForgeMixin(modData.id)
    }

    if (mcData.isForgeLike) {
        useKotlinForForge()
    }

    useDevAuth("+")
}

// TODO: toolkitReleasesV2

dependencies {
    with(libs.textile.get()) {
        api(this)
        modImplementation("${module.group}:${module.name}-$mcData:${versionConstraint.requiredVersion}")
    }

    with(libs.omnicore.get()) {
        modImplementation("${module.group}:${module.name}-$mcData:${versionConstraint.requiredVersion}")
    }

    if (mcData.isFabric) {
        modImplementation(libs.fapi.map { "${it.module.group}:${it.module.name}:${mcData.dependencies.fabric.fabricApiVersion}" })
        modImplementation(libs.flk.map { "${it.module.group}:${it.module.name}:${mcData.dependencies.fabric.fabricLanguageKotlinVersion}" })
    }
}
