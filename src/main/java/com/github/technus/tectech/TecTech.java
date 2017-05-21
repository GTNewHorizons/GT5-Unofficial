package com.github.technus.tectech;

import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.loader.MainLoader;
import com.github.technus.tectech.proxy.CommonProxy;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import eu.usrv.yamcore.auxiliary.IngameErrorLog;
import eu.usrv.yamcore.auxiliary.LogHelper;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[10.13.4.1614,);"
        + "required-after:YAMCore@[0.5.70,);" + "required-after:gregtech;" + "after:CoFHCore")
public class TecTech {

    @SidedProxy(clientSide = Reference.CLIENTSIDE, serverSide = Reference.SERVERSIDE)
    public static CommonProxy proxy;

    @Instance(Reference.MODID)
    public static TecTech instance;

    public static final LogHelper Logger = new LogHelper(Reference.MODID);
    private static IngameErrorLog Module_AdminErrorLogs = null;
    public static MainLoader GTCustomLoader = null;
    public static TecTechConfig ModConfig;
    public static XSTR Rnd = null;
    public static CreativeTabs mainTab = null;
    private static boolean oneTimeFix = false;

    public static boolean hasCOFH = false;

    public static void AddLoginError(String pMessage) {
        if (Module_AdminErrorLogs != null)
            Module_AdminErrorLogs.AddErrorLogOnAdminJoin(pMessage);
    }

    @EventHandler
    public void PreLoad(FMLPreInitializationEvent PreEvent) {
        Logger.setDebugOutput(true);
        Rnd = new XSTR();

        ModConfig = new TecTechConfig(PreEvent.getModConfigurationDirectory(), Reference.COLLECTIONNAME,
                Reference.MODID);
        if (!ModConfig.LoadConfig())
            Logger.error(Reference.MODID + " could not load its config file. Things are going to be weird!");

        if (ModConfig.ModAdminErrorLogs_Enabled) {
            Logger.debug("Module_AdminErrorLogs is enabled");
            Module_AdminErrorLogs = new IngameErrorLog();
        }
    }

    @EventHandler
    public void Load(FMLInitializationEvent event) {
        GTCustomLoader = new MainLoader();
        GTCustomLoader.load();

        proxy.registerRenderInfo();
    }

    @EventHandler
    public void PostLoad(FMLPostInitializationEvent PostEvent) {
        hasCOFH = Loader.isModLoaded(Reference.COFHCORE);

        GTCustomLoader.postLoad();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent pEvent) {
    }

    @EventHandler
    public void onServerAboutToStart(FMLServerAboutToStartEvent ev) {
        if (!oneTimeFix) {
            oneTimeFix = true;
            if (ModConfig.NERF_FUSION) FixBrokenFusionRecipes();
        }
    }

    private void FixBrokenFusionRecipes() {
        HashMap<Fluid, Fluid> binds = new HashMap<>();
        for (Materials m : Materials.values()) {
            FluidStack p = m.getPlasma(1);
            if (p != null) {
                if (TecTechConfig.DEBUG_MODE) TecTech.Logger.info("Found Plasma of " + m.mName);
                if (m.mElement != null &&
                        (m.mElement.mProtons >= Materials.Iron.mElement.mProtons ||
                                -m.mElement.mProtons >= Materials.Iron.mElement.mProtons ||
                                m.mElement.mNeutrons >= Materials.Iron.mElement.mNeutrons ||
                                -m.mElement.mNeutrons >= Materials.Iron.mElement.mNeutrons)) {
                    if (TecTechConfig.DEBUG_MODE) TecTech.Logger.info("Attempting to bind " + m.mName);
                    if (m.getMolten(1) != null) binds.put(p.getFluid(), m.getMolten(1).getFluid());
                    else if (m.getGas(1) != null) binds.put(p.getFluid(), m.getGas(1).getFluid());
                    else if (m.getFluid(1) != null) binds.put(p.getFluid(), m.getFluid(1).getFluid());
                    else binds.put(p.getFluid(), Materials.Iron.getMolten(1).getFluid());
                }
            }
        }
        for (GT_Recipe r : GT_Recipe.GT_Recipe_Map.sFusionRecipes.mRecipeList) {
            Fluid f = binds.get(r.mFluidOutputs[0].getFluid());
            if (f != null) {
                if (TecTechConfig.DEBUG_MODE)
                    TecTech.Logger.info("Nerfing Recipe " + r.mFluidOutputs[0].getUnlocalizedName());
                r.mFluidOutputs[0] = new FluidStack(f, r.mFluidInputs[0].amount);
            }
        }
    }
}
