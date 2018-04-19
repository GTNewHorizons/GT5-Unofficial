package com.github.technus.tectech;

import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.elementalMatter.core.commands.GiveEM;
import com.github.technus.tectech.elementalMatter.core.commands.ListEM;
import com.github.technus.tectech.loader.MainLoader;
import com.github.technus.tectech.loader.ModGuiHandler;
import com.github.technus.tectech.proxy.CommonProxy;
import com.github.technus.tectech.thing.metaTileEntity.Textures;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.network.RotationPacketDispatcher;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import eu.usrv.yamcore.auxiliary.IngameErrorLog;
import eu.usrv.yamcore.auxiliary.LogHelper;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static com.github.technus.tectech.CommonValues.*;
import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static gregtech.api.GregTech_API.sGTBlockIconload;
import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.enums.Dyes.dyeBlue;
import static gregtech.api.enums.Dyes.dyeLightBlue;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[10.13.4.1614,);"
        + "required-after:YAMCore@[0.5.70,);" + "required-after:gregtech;" + "after:CoFHCore;" + "after:Thaumcraft;" + "after:dreamcraft;")
public class TecTech {
    @SidedProxy(clientSide = Reference.CLIENTSIDE, serverSide = Reference.SERVERSIDE)
    public static CommonProxy proxy;

    @Mod.Instance(Reference.MODID)
    public static TecTech instance;

    public static final XSTR Rnd = XSTR.XSTR_INSTANCE;
    public static final LogHelper Logger = new LogHelper(Reference.MODID);
    private static IngameErrorLog Module_AdminErrorLogs;
    public static MainLoader GTCustomLoader;
    public static TecTechConfig ModConfig;
    public static CreativeTabs mainTab;

    public static boolean hasCOFH = false, hasThaumcraft = false;

    public static final byte tectechTexturePage1=8;

    public static void AddLoginError(String pMessage) {
        if (Module_AdminErrorLogs != null) {
            Module_AdminErrorLogs.AddErrorLogOnAdminJoin(pMessage);
        }
    }

    @Mod.EventHandler
    public void PreLoad(FMLPreInitializationEvent PreEvent) {
        for(int i=0;i<16;i++){
            GT_Values.V[i]=V[i];
            GT_Values.VN[i]=VN[i];
            GT_Values.VOLTAGE_NAMES[i]=VOLTAGE_NAMES[i];
        }
        Logger.setDebugOutput(true);

        dyeLightBlue.mRGBa[0]=96;
        dyeLightBlue.mRGBa[1]=128;
        dyeLightBlue.mRGBa[2]=255;
        dyeBlue.mRGBa[0]=0;
        dyeBlue.mRGBa[1]=32;
        dyeBlue.mRGBa[2]=255;
        MACHINE_METAL.mRGBa[0]=210;
        MACHINE_METAL.mRGBa[1]=220;
        MACHINE_METAL.mRGBa[2]=255;

        try {
            sGTBlockIconload.add(new Runnable() {
                @Override
                public void run() {
                    new Textures();
                }
            });
        }catch (Throwable t){
            Logger.error("Loading textures...",t);
        }

        ModConfig = new TecTechConfig(PreEvent.getModConfigurationDirectory(), Reference.COLLECTIONNAME,
                Reference.MODID);

        if (!ModConfig.LoadConfig()) {
            Logger.error(Reference.MODID + " could not load its config file. Things are going to be weird!");
        }

        if (ModConfig.ModAdminErrorLogs_Enabled) {
            Logger.debug("Module_AdminErrorLogs is enabled");
            Module_AdminErrorLogs = new IngameErrorLog();
        }

        GTCustomLoader = new MainLoader();

        Logger.info("Added Atom Overrider");
    }

    @Mod.EventHandler
    public void Load(FMLInitializationEvent event) {
        hasCOFH = Loader.isModLoaded(Reference.COFHCORE);
        hasThaumcraft = Loader.isModLoaded(Reference.THAUMCRAFT);

        GTCustomLoader.load();

        new RotationPacketDispatcher();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
        proxy.registerRenderInfo();
    }

    @Mod.EventHandler
    public void PostLoad(FMLPostInitializationEvent PostEvent) {
        GTCustomLoader.postLoad();
        if (ModConfig.NERF_FUSION) {
            FixBrokenFusionRecipes();
        }
        fixBlocks();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent pEvent) {
        if(DEBUG_MODE) {
            pEvent.registerServerCommand(new GiveEM());
            pEvent.registerServerCommand(new ListEM());
        }
    }

    @Mod.EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent ev) {
    }

    private void FixBrokenFusionRecipes() {
        HashMap<Fluid, Fluid> binds = new HashMap<>();
        for (Materials material : Materials.values()) {
            FluidStack p = material.getPlasma(1);
            if (p != null) {
                if (DEBUG_MODE) {
                    Logger.info("Found Plasma of " + material.mName);
                }
                if (material.mElement != null &&
                        (material.mElement.mProtons >= Materials.Iron.mElement.mProtons ||
                                -material.mElement.mProtons >= Materials.Iron.mElement.mProtons ||
                                material.mElement.mNeutrons >= Materials.Iron.mElement.mNeutrons ||
                                -material.mElement.mNeutrons >= Materials.Iron.mElement.mNeutrons)) {
                    if (DEBUG_MODE) {
                        Logger.info("Attempting to bind " + material.mName);
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
                    Logger.info("Nerfing Recipe " + r.mFluidOutputs[0].getUnlocalizedName());
                }
                r.mFluidOutputs[0] = new FluidStack(fluid, r.mFluidInputs[0].amount);
            }
        }
    }

    private void fixBlocks(){
        HashSet<String> modIDs=new HashSet<>(Arrays.asList(
                "minecraft",
                "IC2",
                "gregtech",
                "dreamcraft",
                "miscutils",
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
        String modId;
        for(Block block : GameData.getBlockRegistry().typeSafeIterable()) {
            GameRegistry.UniqueIdentifier uniqueIdentifier=GameRegistry.findUniqueIdentifierFor(block);
            if (uniqueIdentifier != null) {
                modId = uniqueIdentifier.modId;
                if (modIDs.contains(modId)) {//Full Whitelisted Mods
                    continue;
                } else if ("OpenBlocks".equals(modId)) {
                    if ("grave".equals(GameRegistry.findUniqueIdentifierFor(block).name)) {
                        continue;
                    }
                } else if ("TwilightForest".equals(modId)){
                    if ("tile.TFShield".equals(GameRegistry.findUniqueIdentifierFor(block).name)){
                        block.setResistance(40);
                        continue;
                    }else if ("tile.TFThorns".equals(GameRegistry.findUniqueIdentifierFor(block).name)){
                        block.setResistance(10);
                        continue;
                    }else if ("tile.TFTowerTranslucent".equals(GameRegistry.findUniqueIdentifierFor(block).name)){
                        block.setResistance(30);
                        continue;
                    }else if ("tile.TFDeadrock".equals(GameRegistry.findUniqueIdentifierFor(block).name)) {
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
