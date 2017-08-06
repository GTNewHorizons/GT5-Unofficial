package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.magicAddon.EssentiaCompat;
import com.github.technus.tectech.magicAddon.EssentiaCompatEnabled;
import com.github.technus.tectech.magicAddon.definitions.AspectDefinitionCompat;
import com.github.technus.tectech.magicAddon.definitions.AspectDefinitionCompatEnabled;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;
import com.github.technus.tectech.thing.machineTT;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import java.util.List;

import static com.github.technus.tectech.TecTech.hasThaumcraft;
import static com.github.technus.tectech.TecTech.mainTab;
import static com.github.technus.tectech.magicAddon.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.magicAddon.definitions.AspectDefinitionCompat.aspectDefinitionCompat;
import static gregtech.api.enums.GT_Values.W;

public final class MainLoader {//TODO add checks for - is mod loaded dreamcraft to enable higher tier machinery. (above UV), or implement a check for GT tier values.
    public static DamageSource microwaving, elementalPollution;

    public void load() {
        ProgressManager.ProgressBar progressBarLoad = ProgressManager.push("TecTech Loader", 6);

        progressBarLoad.step("Elemental Things");
        new ElementalLoader().run();
        TecTech.Logger.info("Elemental Init Done");

        progressBarLoad.step("Thaumcraft Compatibility");
        if (hasThaumcraft) {
            essentiaContainerCompat = new EssentiaCompatEnabled();
            essentiaContainerCompat.run();
        } else {
            essentiaContainerCompat = new EssentiaCompat();
        }
        TecTech.Logger.info("Thaumcraft Compatibility Done");

        progressBarLoad.step("Regular Things");
        new ThingsLoader().run();
        TecTech.Logger.info("Block/Item Init Done");

        progressBarLoad.step("Machine Things");
        new MachineLoader().run();
        TecTech.Logger.info("Machine Init Done");

        progressBarLoad.step("Register entities");
        new EntityLoader().run();
        TecTech.Logger.info("Entities registered");

        progressBarLoad.step("Add damage types");
        microwaving=new DamageSource("microwaving");
        microwaving.setDamageBypassesArmor();

        elementalPollution=new DamageSource("elementalPollution");
        elementalPollution.setDamageBypassesArmor();
        elementalPollution.isUnblockable();
        TecTech.Logger.info("Damage types addition Done");

        ProgressManager.pop(progressBarLoad);
    }

    public void postLoad() {
        ProgressManager.ProgressBar progressBarPostLoad = ProgressManager.push("TecTech Post Loader", 5);

        progressBarPostLoad.step("Thaumcraft Compatibility");
        if (hasThaumcraft) {
            aspectDefinitionCompat = new AspectDefinitionCompatEnabled();
            aspectDefinitionCompat.run();
        } else {
            aspectDefinitionCompat = new AspectDefinitionCompat();
        }

        progressBarPostLoad.step("Recipes");
        new RecipeLoader().run();

        TecTech.Logger.info("Recipe Init Done");

        progressBarPostLoad.step("Creative Tab part1");
        mainTab = new CreativeTabs("TecTech") {
            @SideOnly(Side.CLIENT)
            @Override
            public Item getTabIconItem() {
                return DebugContainer_EM.INSTANCE;
            }

            @Override
            public void displayAllReleventItems(List stuffToShow) {
                for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
                    if (GregTech_API.METATILEENTITIES[i] instanceof machineTT) {
                        stuffToShow.add(new ItemStack(GregTech_API.sBlockMachines, 1, i));
                    }
                }
                super.displayAllReleventItems(stuffToShow);
            }
        };
        progressBarPostLoad.step("Creative Tab part2");
        registerThingsInTabs();
        TecTech.Logger.info("CreativeTab initiation complete");

        progressBarPostLoad.step("Register Extra Hazmat Suits");
        registerExtraHazmats();
        TecTech.Logger.info("CreativeTab initiation complete");

        ProgressManager.pop(progressBarPostLoad);
    }

    private void registerThingsInTabs() {
        QuantumGlassBlock.INSTANCE.setCreativeTab(mainTab);
        GT_Container_CasingsTT.sBlockCasingsTT.setCreativeTab(mainTab);
        DebugContainer_EM.INSTANCE.setCreativeTab(mainTab);
        DebugBuilder.INSTANCE.setCreativeTab(mainTab);
    }

    private void registerExtraHazmats() {
        GregTech_API.sFrostHazmatList.add(new ItemStack(GameRegistry.findItem("EMT","itemArmorQuantumChestplate"),1,W));
        GregTech_API.sFrostHazmatList.add(new ItemStack(GameRegistry.findItem("GraviSuite","graviChestPlate"),1,W));
        GregTech_API.sFrostHazmatList.add(GT_ModHandler.getIC2Item("quantumHelmet", 1L, W));
        GregTech_API.sFrostHazmatList.add(GT_ModHandler.getIC2Item("quantumBodyarmor", 1L, W));
        GregTech_API.sFrostHazmatList.add(GT_ModHandler.getIC2Item("quantumLeggings", 1L, W));
        GregTech_API.sFrostHazmatList.add(GT_ModHandler.getIC2Item("quantumBoots", 1L, W));

        GregTech_API.sHeatHazmatList.add(new ItemStack(GameRegistry.findItem("EMT","itemArmorQuantumChestplate"),1,W));
        GregTech_API.sHeatHazmatList.add(new ItemStack(GameRegistry.findItem("GraviSuite","graviChestPlate"),1,W));
        GregTech_API.sHeatHazmatList.add(GT_ModHandler.getIC2Item("quantumHelmet", 1L, W));
        GregTech_API.sHeatHazmatList.add(GT_ModHandler.getIC2Item("quantumBodyarmor", 1L, W));
        GregTech_API.sHeatHazmatList.add(GT_ModHandler.getIC2Item("quantumLeggings", 1L, W));
        GregTech_API.sHeatHazmatList.add(GT_ModHandler.getIC2Item("quantumBoots", 1L, W));

        GregTech_API.sBioHazmatList.add(new ItemStack(GameRegistry.findItem("EMT","itemArmorQuantumChestplate"),1,W));
        GregTech_API.sBioHazmatList.add(new ItemStack(GameRegistry.findItem("GraviSuite","graviChestPlate"),1,W));
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getIC2Item("quantumHelmet", 1L, W));
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getIC2Item("quantumBodyarmor", 1L, W));
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getIC2Item("quantumLeggings", 1L, W));
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getIC2Item("quantumBoots", 1L, W));
        
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getModItem("GraviSuite", "advNanoChestPlate", 1, W);
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getIC2Item("nanoHelmet", 1L, W));
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getIC2Item("nanoBodyarmor", 1L, W));
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getIC2Item("nanoLeggings", 1L, W));
        GregTech_API.sBioHazmatList.add(GT_ModHandler.getIC2Item("nanoBoots", 1L, W));

        GregTech_API.sGasHazmatList.add(new ItemStack(GameRegistry.findItem("EMT","itemArmorQuantumChestplate"),1,W));
        GregTech_API.sGasHazmatList.add(new ItemStack(GameRegistry.findItem("GraviSuite","graviChestPlate"),1,W));
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getIC2Item("quantumHelmet", 1L, W));
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getIC2Item("quantumBodyarmor", 1L, W));
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getIC2Item("quantumLeggings", 1L, W));
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getIC2Item("quantumBoots", 1L, W));
        
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getModItem("GraviSuite", "advNanoChestPlate", 1, W);
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getIC2Item("nanoHelmet", 1L, W));
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getIC2Item("nanoBodyarmor", 1L, W));
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getIC2Item("nanoLeggings", 1L, W));
        GregTech_API.sGasHazmatList.add(GT_ModHandler.getIC2Item("nanoBoots", 1L, W));

        GregTech_API.sRadioHazmatList.add(new ItemStack(GameRegistry.findItem("EMT","itemArmorQuantumChestplate"),1,W));
        GregTech_API.sRadioHazmatList.add(new ItemStack(GameRegistry.findItem("GraviSuite","graviChestPlate"),1,W));
        GregTech_API.sRadioHazmatList.add(GT_ModHandler.getIC2Item("quantumHelmet", 1L, W));
        GregTech_API.sRadioHazmatList.add(GT_ModHandler.getIC2Item("quantumBodyarmor", 1L, W));
        GregTech_API.sRadioHazmatList.add(GT_ModHandler.getIC2Item("quantumLeggings", 1L, W));
        GregTech_API.sRadioHazmatList.add(GT_ModHandler.getIC2Item("quantumBoots", 1L, W));

        GregTech_API.sElectroHazmatList.add(new ItemStack(GameRegistry.findItem("EMT","itemArmorQuantumChestplate"),1,W));
        GregTech_API.sElectroHazmatList.add(new ItemStack(GameRegistry.findItem("GraviSuite","graviChestPlate"),1,W));
        GregTech_API.sElectroHazmatList.add(GT_ModHandler.getIC2Item("quantumHelmet", 1L, W));
        GregTech_API.sElectroHazmatList.add(GT_ModHandler.getIC2Item("quantumBodyarmor", 1L, W));
        GregTech_API.sElectroHazmatList.add(GT_ModHandler.getIC2Item("quantumLeggings", 1L, W));
        GregTech_API.sElectroHazmatList.add(GT_ModHandler.getIC2Item("quantumBoots", 1L, W));
        
        //todo add GC GS stuff
    }
}
