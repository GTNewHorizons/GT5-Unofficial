package com.gtnewhorizons.gtnhintergalactic.nei;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.item.IGItems;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.event.NEIRegisterHandlerInfosEvent;
import codechicken.nei.recipe.HandlerInfo;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class NEI_IG_Config implements IConfigureNEI {

    public static boolean executed = false;

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new SpacePumpModuleRecipeHandler());
        API.registerUsageHandler(new SpacePumpModuleRecipeHandler());
        API.registerRecipeHandler(new GasSiphonRecipeHandler());
        API.registerUsageHandler(new GasSiphonRecipeHandler());

        for (ItemStack pump : Arrays.asList(
                IGItems.SpaceElevatorModulePumpT1,
                IGItems.SpaceElevatorModulePumpT2,
                IGItems.SpaceElevatorModulePumpT3)) {
            API.addRecipeCatalyst(pump, "com.gtnewhorizons.gtnhintergalactic.nei.SpacePumpModuleRecipeHandler");
        }
        API.addRecipeCatalyst(
                IGItems.PlanetaryGasSiphon,
                "com.gtnewhorizons.gtnhintergalactic.nei.GasSiphonRecipeHandler");

        executed = true;
    }

    @SubscribeEvent
    public void registerHandlerInfo(NEIRegisterHandlerInfosEvent event) {
        event.registerHandlerInfo(
                new HandlerInfo.Builder(
                        "com.gtnewhorizons.gtnhintergalactic.nei.SpacePumpModuleRecipeHandler",
                        Tags.MODNAME,
                        Tags.MODID).setDisplayStack(IGItems.SpaceElevatorModulePumpT1).setShiftY(6).setWidth(160)
                                .setHeight(90).setMaxRecipesPerPage(3).build());
        event.registerHandlerInfo(
                new HandlerInfo.Builder(
                        "com.gtnewhorizons.gtnhintergalactic.nei.GasSiphonRecipeHandler",
                        Tags.MODNAME,
                        Tags.MODID).setDisplayStack(IGItems.PlanetaryGasSiphon).setShiftY(6).setWidth(160).setHeight(90)
                                .setMaxRecipesPerPage(3).build());
    }

    @Override
    public String getName() {
        return "GTNH-Intergalactic NEI Plugin";
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }
}
