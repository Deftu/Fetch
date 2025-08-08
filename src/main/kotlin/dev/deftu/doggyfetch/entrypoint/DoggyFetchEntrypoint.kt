package dev.deftu.doggyfetch.entrypoint

//#if FABRIC
import net.fabricmc.api.ModInitializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.DedicatedServerModInitializer
//#elseif FORGE
//$$ import net.minecraftforge.eventbus.api.IEventBus
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
//$$ import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
//$$ import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//#elseif NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus
//$$ import net.neoforged.fml.common.Mod
//$$ import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
//$$ import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
//$$ import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
//#endif

import dev.deftu.doggyfetch.DoggyFetch
import dev.deftu.doggyfetch.client.DoggyFetchClient
import dev.deftu.doggyfetch.server.DoggyFetchServer

//#if FORGE-LIKE
//$$ import dev.deftu.doggyfetch.DoggyFetchConstants
//#if MC >= 1.16.5
//$$ @Mod(DoggyFetchConstants.ID)
//#else
//$$ @Mod(modid = DoggyFetchConstants.ID, version = DoggyFetchConstants.VERSION)
//#endif
//#endif
class DoggyFetchEntrypoint
    //#if FABRIC
    : ModInitializer, ClientModInitializer, DedicatedServerModInitializer
    //#endif
{

    //#if FORGE
    //$$ init {
    //$$     setupForgeEvents(FMLJavaModLoadingContext.get().modEventBus)
    //$$ }
    //#elseif NEOFORGE
    //$$ constructor(modEventBus: IEventBus) {
    //$$     setupForgeEvents(modEventBus)
    //$$ }
    //#endif

    //#if FABRIC
    override
    //#endif
    fun onInitialize(
        //#if FORGE-LIKE
        //$$ event: FMLCommonSetupEvent
        //#endif
    ) {
        DoggyFetch.onInitializeCommon()
    }

    //#if FABRIC
    override
    //#endif
    fun onInitializeClient(
        //#if FORGE-LIKE
        //$$ event: FMLClientSetupEvent
        //#endif
    ) {
        DoggyFetchClient.onInitializeClient()
    }

    //#if FABRIC
    override
    //#endif
    fun onInitializeServer(
        //#if FORGE-LIKE
        //$$ event: FMLDedicatedServerSetupEvent
        //#endif
    ) {
        DoggyFetchServer.onInitializeServer()
    }

    //#if FORGE-LIKE
    //$$ fun setupForgeEvents(modEventBus: IEventBus) {
    //$$     modEventBus.addListener(this::onInitialize)
    //$$     modEventBus.addListener(this::onInitializeClient)
    //$$     modEventBus.addListener(this::onInitializeServer)
    //$$ }
    //#endif

}
