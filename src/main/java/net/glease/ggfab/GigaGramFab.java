package net.glease.ggfab;

import net.glease.ggfab.mte.MTE_AdvAssLine;
import net.glease.ggfab.mte.MTE_LinkedInputBus;
import net.glease.ggfab.nei.IMCForNEI;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import gregtech.api.GregTech_API;

@Mod(
        modid = GGConstants.MODID,
        version = GGConstants.VERSION,
        name = GGConstants.MODNAME,
        acceptedMinecraftVersions = "[1.7.10]",
        dependencies = "required-after:IC2;required-before:gregtech")
public class GigaGramFab {

    public GigaGramFab() {
        // initialize the textures
        // noinspection ResultOfMethodCallIgnored
        BlockIcons.OVERLAY_FRONT_ADV_ASSLINE.name();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GregTech_API.sAfterGTPreload.add(() -> {
            GGItemList.AdvAssLine.set(
                    new MTE_AdvAssLine(13532, "ggfab.machine.adv_assline", "Advanced Assembly Line").getStackForm(1));
            GGItemList.LinkedInputBus.set(
                    new MTE_LinkedInputBus(13533, "ggfab.machine.linked_input_bus", "Linked Input Bus", 5)
                            .getStackForm(1));
        });
        GregTech_API.sBeforeGTPostload.add(new ComponentRecipeLoader());
        ConfigurationHandler.INSTANCE.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        IMCForNEI.IMCSender();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}

}
