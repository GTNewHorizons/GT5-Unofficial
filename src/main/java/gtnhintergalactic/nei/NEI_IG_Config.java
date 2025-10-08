package gtnhintergalactic.nei;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.event.NEIRegisterHandlerInfosEvent;
import codechicken.nei.recipe.HandlerInfo;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.GT_Version;
import gregtech.api.enums.ItemList;
import gtnhintergalactic.GTNHIntergalactic;

public class NEI_IG_Config implements IConfigureNEI {

    public static boolean executed = false;

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new SpacePumpModuleRecipeHandler());
        API.registerUsageHandler(new SpacePumpModuleRecipeHandler());
        API.registerRecipeHandler(new GasSiphonRecipeHandler());
        API.registerUsageHandler(new GasSiphonRecipeHandler());

        for (ItemStack pump : Arrays.asList(
            ItemList.SpaceElevatorModulePumpT1.get(1),
            ItemList.SpaceElevatorModulePumpT2.get(1),
            ItemList.SpaceElevatorModulePumpT3.get(1))) {
            API.addRecipeCatalyst(pump, "gtnhintergalactic.nei.SpacePumpModuleRecipeHandler");
        }
        API.addRecipeCatalyst(
            ItemList.PlanetaryGasSiphonController.get(1),
            "gtnhintergalactic.nei.GasSiphonRecipeHandler");

        executed = true;
    }

    @SubscribeEvent
    public void registerHandlerInfo(NEIRegisterHandlerInfosEvent event) {
        event.registerHandlerInfo(
            new HandlerInfo.Builder(
                "gtnhintergalactic.nei.SpacePumpModuleRecipeHandler",
                GTNHIntergalactic.MODNAME,
                GTNHIntergalactic.MODID).setDisplayStack(ItemList.SpaceElevatorModulePumpT1.get(1))
                    .setShiftY(6)
                    .setWidth(160)
                    .setHeight(90)
                    .setMaxRecipesPerPage(3)
                    .build());
        event.registerHandlerInfo(
            new HandlerInfo.Builder(
                "gtnhintergalactic.nei.GasSiphonRecipeHandler",
                GTNHIntergalactic.MODNAME,
                GTNHIntergalactic.MODID).setDisplayStack(ItemList.PlanetaryGasSiphonController.get(1))
                    .setShiftY(6)
                    .setWidth(160)
                    .setHeight(90)
                    .setMaxRecipesPerPage(3)
                    .build());
    }

    @Override
    public String getName() {
        return "GTNH-Intergalactic NEI Plugin";
    }

    @Override
    public String getVersion() {
        return GT_Version.VERSION;
    }
}
