package com.github.technus.tectech.loader;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.AspectDefinitionCompat;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.AspectDefinitionCompatEnabled;
import com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.EssentiaCompat;
import com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.EssentiaCompatEnabled;
import com.github.technus.tectech.loader.entity.EntityLoader;
import com.github.technus.tectech.loader.gui.CreativeTabTecTech;
import com.github.technus.tectech.loader.gui.ModGuiHandler;
import com.github.technus.tectech.loader.mechanics.ElementalLoader;
import com.github.technus.tectech.loader.recipe.RecipeLoader;
import com.github.technus.tectech.loader.thing.MachineLoader;
import com.github.technus.tectech.loader.thing.ThingsLoader;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.Textures;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.network.RotationPacketDispatcher;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static com.github.technus.tectech.CommonValues.*;
import static com.github.technus.tectech.TecTech.*;
import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.AspectDefinitionCompat.aspectDefinitionCompat;
import static com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.loader.gui.CreativeTabTecTech.creativeTabTecTech;
import static gregtech.api.enums.Dyes.*;
import static gregtech.api.enums.GT_Values.W;

public final class MainLoader {
    public static DamageSource microwaving, elementalPollution;

    private MainLoader(){}

    public static void staticLoad(){
        for(int i=0;i<16;i++){
            GT_Values.V[i]=V[i];
            GT_Values.VN[i]=VN[i];
            GT_Values.VOLTAGE_NAMES[i]=VOLTAGE_NAMES[i];
        }
    }

    public static void preLoad(){
        //Set proper values in gt arrays
        dyeLightBlue.mRGBa[0]=96;
        dyeLightBlue.mRGBa[1]=128;
        dyeLightBlue.mRGBa[2]=255;
        dyeBlue.mRGBa[0]=0;
        dyeBlue.mRGBa[1]=32;
        dyeBlue.mRGBa[2]=255;
        MACHINE_METAL.mRGBa[0]=210;
        MACHINE_METAL.mRGBa[1]=220;
        MACHINE_METAL.mRGBa[2]=255;

        //set expanded texture arrays for tiers
        try {
            new Textures();
        }catch (Throwable t){
            LOGGER.error("Loading textures...",t);
        }
    }

    public static void load() {
        ProgressManager.ProgressBar progressBarLoad = ProgressManager.push("TecTech Loader", 8);

        progressBarLoad.step("Elemental Things");
        new ElementalLoader().run();
        LOGGER.info("Elemental Init Done");

        progressBarLoad.step("Thaumcraft Compatibility");
        if (Loader.isModLoaded(Reference.THAUMCRAFT)) {
            essentiaContainerCompat = new EssentiaCompatEnabled();
        } else {
            essentiaContainerCompat = new EssentiaCompat();
        }
        LOGGER.info("Thaumcraft Compatibility Done");

        progressBarLoad.step("Regular Things");
        new ThingsLoader().run();
        LOGGER.info("Block/Item Init Done");

        progressBarLoad.step("Machine Things");
        new MachineLoader().run();
        LOGGER.info("Machine Init Done");

        progressBarLoad.step("Register entities");
        new EntityLoader().run();
        LOGGER.info("Entities registered");

        progressBarLoad.step("Add damage types");
        microwaving =new DamageSource("microwaving").setDamageBypassesArmor();
        elementalPollution =new DamageSource("elementalPollution").setDamageBypassesArmor();
        LOGGER.info("Damage types addition Done");

        progressBarLoad.step("Register Packet Dispatcher");
        new RotationPacketDispatcher();
        LOGGER.info("Packet Dispatcher registered");

        progressBarLoad.step("Register GUI Handler");
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
        proxy.registerRenderInfo();
        LOGGER.info("GUI Handler registered");

        ProgressManager.pop(progressBarLoad);
    }

    public static void postLoad() {
        ProgressManager.ProgressBar progressBarPostLoad = ProgressManager.push("TecTech Post Loader", 7);

        progressBarPostLoad.step("Dreamcraft Compatibility");
        if(Loader.isModLoaded(Reference.DREAMCRAFT)){
            try {
                Class clazz = Class.forName("com.dreammaster.gthandler.casings.GT_Container_CasingsNH");
                TT_Container_Casings.sBlockCasingsNH = (Block)clazz.getField("sBlockCasingsNH").get(null);

                if(TT_Container_Casings.sBlockCasingsNH==null){
                    throw new NullPointerException("sBlockCasingsNH Is not set at this time");
                }
            }catch (Exception e){
                throw new Error("Unable to get NH casings",e);
            }
        }

        progressBarPostLoad.step("Thaumcraft Compatibility");
        if (Loader.isModLoaded(Reference.THAUMCRAFT)) {
            aspectDefinitionCompat = new AspectDefinitionCompatEnabled();
            aspectDefinitionCompat.run();
        } else {
            aspectDefinitionCompat = new AspectDefinitionCompat();
        }

        progressBarPostLoad.step("Recipes");
        new RecipeLoader().run();

        TecTech.LOGGER.info("Recipe Init Done");

        progressBarPostLoad.step("Creative Tab");
        creativeTabTecTech =new CreativeTabTecTech("TecTech");
        TecTech.LOGGER.info("CreativeTab initiation complete");

        progressBarPostLoad.step("Register Extra Hazmat Suits");
        registerExtraHazmats();
        TecTech.LOGGER.info("Hazmat additions done");


        progressBarPostLoad.step("Nerf fusion recipes");
        if (TecTech.configTecTech.NERF_FUSION) {
            FixBrokenFusionRecipes();
        }
        TecTech.LOGGER.info("Fusion nerf done");

        progressBarPostLoad.step("Nerf blocks blast resistance");
        fixBlocks();
        TecTech.LOGGER.info("Blocks nerf done");

        ProgressManager.pop(progressBarPostLoad);
    }

    private static void registerExtraHazmats() {
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

    private static void FixBrokenFusionRecipes() {
        HashMap<Fluid, Fluid> binds = new HashMap<>();
        for (Materials material : Materials.values()) {
            FluidStack p = material.getPlasma(1);
            if (p != null) {
                if (DEBUG_MODE) {
                    LOGGER.info("Found Plasma of " + material.mName);
                }
                if (material.mElement != null &&
                        (material.mElement.mProtons >= Materials.Iron.mElement.mProtons ||
                                -material.mElement.mProtons >= Materials.Iron.mElement.mProtons ||
                                material.mElement.mNeutrons >= Materials.Iron.mElement.mNeutrons ||
                                -material.mElement.mNeutrons >= Materials.Iron.mElement.mNeutrons)) {
                    if (DEBUG_MODE) {
                        LOGGER.info("Attempting to bind " + material.mName);
                    }
                    if (material.getMolten(1) != null) {
                        binds.put(p.getFluid(), material.getMolten(1).getFluid());
                    } else if (material.getGas(1) != null) {
                        binds.put(p.getFluid(), material.getGas(1).getFluid());
                    } else if (material.getFluid(1) != null) {
                        binds.put(p.getFluid(), material.getFluid(1).getFluid());
                    } else {
                        binds.put(p.getFluid(), Materials.Iron.getMolten(1).getFluid());
                    }
                }
            }
        }
        for (GT_Recipe r : GT_Recipe.GT_Recipe_Map.sFusionRecipes.mRecipeList) {
            Fluid fluid = binds.get(r.mFluidOutputs[0].getFluid());
            if (fluid != null) {
                if (DEBUG_MODE) {
                    LOGGER.info("Nerfing Recipe " + r.mFluidOutputs[0].getUnlocalizedName());
                }
                r.mFluidOutputs[0] = new FluidStack(fluid, r.mFluidInputs[0].amount);
            }
        }
    }

    private static void fixBlocks(){
        HashSet<String> modIDs=new HashSet<>(Arrays.asList(
                "minecraft",
                "IC2",
                "gregtech",
                Reference.DREAMCRAFT,
                Reference.GTPLUSPLUS,
                "GT++DarkWorld",
                "GalacticraftCore",
                "GalacticraftMars",
                "GalaxySpace",
                "extracells",
                "Avaritia",
                "avaritiaddons",
                "EnderStorage",
                "enhancedportals",
                "DraconicEvolution",
                "IC2NuclearControl",
                "IronChest",
                "opensecurity",
                "openmodularturrets",
                "Railcraft",
                "RIO",
                "SGCraft",
                "appliedenergistics2",
                "thaumicenergistics",
                "witchery",
                "lootgames",
                "utilityworlds",
                Reference.MODID
        ));
        for(Block block : GameData.getBlockRegistry().typeSafeIterable()) {
            GameRegistry.UniqueIdentifier uniqueIdentifier=GameRegistry.findUniqueIdentifierFor(block);
            if (uniqueIdentifier != null) {
                if (modIDs.contains(uniqueIdentifier.modId)) {//Full Whitelisted Mods
                    continue;
                } else if ("OpenBlocks".equals(uniqueIdentifier.modId)) {
                    if ("grave".equals(uniqueIdentifier.name)) {
                        continue;
                    }
                } else if ("TwilightForest".equals(uniqueIdentifier.modId)){
                    if ("tile.TFShield".equals(uniqueIdentifier.name)){
                        block.setResistance(30);
                        continue;
                    }else if ("tile.TFThorns".equals(uniqueIdentifier.name)){
                        block.setResistance(10);
                        continue;
                    }else if ("tile.TFTowerTranslucent".equals(uniqueIdentifier.name)){
                        block.setResistance(30);
                        continue;
                    }else if ("tile.TFDeadrock".equals(uniqueIdentifier.name)) {
                        block.setResistance(5);
                        continue;
                    } else {
                        continue;
                    }
                }
            }
            block.setResistance(5);
        }
    }
}
