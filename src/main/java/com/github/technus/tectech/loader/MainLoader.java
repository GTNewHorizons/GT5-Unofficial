package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.thaumcraft.EssentiaCompat;
import com.github.technus.tectech.compatibility.thaumcraft.EssentiaCompatEnabled;
import com.github.technus.tectech.compatibility.thaumcraft.definitions.AspectDefinitionCompat;
import com.github.technus.tectech.compatibility.thaumcraft.definitions.AspectDefinitionCompatEnabled;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.item.ConstructableTriggerItem;
import com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM;
import com.github.technus.tectech.thing.item.ElementalDefinitionScanStorage_EM;
import com.github.technus.tectech.thing.item.ParametrizerMemoryCard;
import cpw.mods.fml.common.ProgressManager;
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
import static com.github.technus.tectech.compatibility.thaumcraft.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.compatibility.thaumcraft.definitions.AspectDefinitionCompat.aspectDefinitionCompat;
import static gregtech.api.enums.GT_Values.W;

public final class MainLoader {
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
        microwaving=new DamageSource("microwaving").setDamageBypassesArmor();
        elementalPollution=new DamageSource("elementalPollution").setDamageBypassesArmor();
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
                return DebugElementalInstanceContainer_EM.INSTANCE;
            }

            @Override
            public void displayAllReleventItems(List stuffToShow) {
                for(CustomItemList item: CustomItemList.values()){
                    if (item.hasBeenSet() && item.getBlock() == GregTech_API.sBlockMachines) {
                        stuffToShow.add(item.get(1));
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
        TecTech.Logger.info("Hazmat additions done");

        ProgressManager.pop(progressBarPostLoad);
    }

    private void registerThingsInTabs() {
        QuantumGlassBlock.INSTANCE.setCreativeTab(mainTab);
        TT_Container_Casings.sBlockCasingsTT.setCreativeTab(mainTab);
        TT_Container_Casings.sHintCasingsTT.setCreativeTab(mainTab);
        DebugElementalInstanceContainer_EM.INSTANCE.setCreativeTab(mainTab);
        ConstructableTriggerItem.INSTANCE.setCreativeTab(mainTab);
        ParametrizerMemoryCard.INSTANCE.setCreativeTab(mainTab);
        ElementalDefinitionScanStorage_EM.INSTANCE.setCreativeTab(mainTab);
    }

    private void registerExtraHazmats() {
        ItemStack EMT_iqC=GT_ModHandler.getModItem("EMT","itemArmorQuantumChestplate",1,W);
        ItemStack GRAVI_gC=GT_ModHandler.getModItem("GraviSuite","graviChestPlate",1,W);
        ItemStack GRAVI_anC=GT_ModHandler.getModItem("GraviSuite", "advNanoChestPlate", 1, W);

        ItemStack IC2_qH=GT_ModHandler.getIC2Item("quantumHelmet", 1L, W);
        ItemStack IC2_qC=GT_ModHandler.getIC2Item("quantumBodyarmor", 1L, W);
        ItemStack IC2_qL=GT_ModHandler.getIC2Item("quantumLeggings", 1L, W);
        ItemStack IC2_qB=GT_ModHandler.getIC2Item("quantumBoots", 1L, W);

        ItemStack IC2_nH=GT_ModHandler.getIC2Item("nanoHelmet", 1L, W);
        ItemStack IC2_nC=GT_ModHandler.getIC2Item("nanoBodyarmor", 1L, W);
        ItemStack IC2_nL=GT_ModHandler.getIC2Item("nanoLeggings", 1L, W);
        ItemStack IC2_nB=GT_ModHandler.getIC2Item("nanoBoots", 1L, W);

        GregTech_API.sFrostHazmatList.add(EMT_iqC);
        GregTech_API.sFrostHazmatList.add(GRAVI_gC);
        GregTech_API.sFrostHazmatList.add(IC2_qH);
        GregTech_API.sFrostHazmatList.add(IC2_qC);
        GregTech_API.sFrostHazmatList.add(IC2_qL);
        GregTech_API.sFrostHazmatList.add(IC2_qB);

        GregTech_API.sHeatHazmatList.add(EMT_iqC);
        GregTech_API.sHeatHazmatList.add(GRAVI_gC);
        GregTech_API.sHeatHazmatList.add(IC2_qH);
        GregTech_API.sHeatHazmatList.add(IC2_qC);
        GregTech_API.sHeatHazmatList.add(IC2_qL);
        GregTech_API.sHeatHazmatList.add(IC2_qB);

        GregTech_API.sBioHazmatList.add(EMT_iqC);
        GregTech_API.sBioHazmatList.add(GRAVI_gC);
        GregTech_API.sBioHazmatList.add(IC2_qH);
        GregTech_API.sBioHazmatList.add(IC2_qC);
        GregTech_API.sBioHazmatList.add(IC2_qL);
        GregTech_API.sBioHazmatList.add(IC2_qB);
        
        GregTech_API.sBioHazmatList.add(GRAVI_anC);
        GregTech_API.sBioHazmatList.add(IC2_nH);
        GregTech_API.sBioHazmatList.add(IC2_nC);
        GregTech_API.sBioHazmatList.add(IC2_nL);
        GregTech_API.sBioHazmatList.add(IC2_nB);

        GregTech_API.sGasHazmatList.add(EMT_iqC);
        GregTech_API.sGasHazmatList.add(GRAVI_gC);
        GregTech_API.sGasHazmatList.add(IC2_qH);
        GregTech_API.sGasHazmatList.add(IC2_qC);
        GregTech_API.sGasHazmatList.add(IC2_qL);
        GregTech_API.sGasHazmatList.add(IC2_qB);
        
        GregTech_API.sGasHazmatList.add(GRAVI_anC);
        GregTech_API.sGasHazmatList.add(IC2_nH);
        GregTech_API.sGasHazmatList.add(IC2_nC);
        GregTech_API.sGasHazmatList.add(IC2_nL);
        GregTech_API.sGasHazmatList.add(IC2_nB);

        GregTech_API.sRadioHazmatList.add(EMT_iqC);
        GregTech_API.sRadioHazmatList.add(GRAVI_gC);
        GregTech_API.sRadioHazmatList.add(IC2_qH);
        GregTech_API.sRadioHazmatList.add(IC2_qC);
        GregTech_API.sRadioHazmatList.add(IC2_qL);
        GregTech_API.sRadioHazmatList.add(IC2_qB);

        GregTech_API.sElectroHazmatList.add(EMT_iqC);
        GregTech_API.sElectroHazmatList.add(GRAVI_gC);
        GregTech_API.sElectroHazmatList.add(IC2_qH);
        GregTech_API.sElectroHazmatList.add(IC2_qC);
        GregTech_API.sElectroHazmatList.add(IC2_qL);
        GregTech_API.sElectroHazmatList.add(IC2_qB);
        
        //todo add GC GS stuff
    }
}
