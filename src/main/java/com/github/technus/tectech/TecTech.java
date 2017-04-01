package com.github.technus.tectech;

import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.loader.Main;
import com.github.technus.tectech.proxy.CommonProxy;
import com.github.technus.tectech.thing.block.QuantumGlass;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.item.DebugBuilder;
import com.github.technus.tectech.thing.item.DebugContainer_EM;
import com.github.technus.tectech.thing.machineTT;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.usrv.yamcore.auxiliary.IngameErrorLog;
import eu.usrv.yamcore.auxiliary.LogHelper;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.List;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[10.13.4.1614,);"
        + "required-after:YAMCore@[0.5.70,);" + "required-after:gregtech;" + "after:CoFHCore")
public class TecTech {

    @SidedProxy(clientSide = Reference.CLIENTSIDE, serverSide = Reference.SERVERSIDE)
    public static CommonProxy proxy;

    @Instance(Reference.MODID)
    public static TecTech instance;

    public static LogHelper Logger = new LogHelper(Reference.MODID);
    private static IngameErrorLog Module_AdminErrorLogs = null;
    public static Main GTCustomLoader = null;
    public static TecTechConfig ModConfig;
    public static XSTR Rnd = null;
    public static CreativeTabs mainTab = null;
    private static boolean oneTimeFix=false;

    public static boolean hasCOFH=false;

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
    public void load(FMLInitializationEvent event) {
        proxy.registerRenderInfo();
    }

    @EventHandler
    public void PostLoad(FMLPostInitializationEvent PostEvent) {
        hasCOFH=Loader.isModLoaded(Reference.COFHCORE);

        QuantumGlass.run();
        DebugContainer_EM.run();
        DebugBuilder.run();

        GTCustomLoader = new Main();
        GTCustomLoader.run();
        GTCustomLoader.run2();

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

        RegisterThingsInTabs();

        if (Loader.isModLoaded("dreamcraft")) ;//TODO init recipes for GTNH version
        else ;//TODO init recipes for NON-GTNH version
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent pEvent) {
        if(!oneTimeFix) {
            if (ModConfig.NERF_FUSION) FixBrokenFusionRecipes();
            oneTimeFix=true;
        }
    }

    private void RegisterThingsInTabs() {
        QuantumGlass.INSTANCE.setCreativeTab(mainTab);
        GT_Container_CasingsTT.sBlockCasingsTT.setCreativeTab(mainTab);
        DebugContainer_EM.INSTANCE.setCreativeTab(mainTab);
        DebugBuilder.INSTANCE.setCreativeTab(mainTab);
    }

    private void FixBrokenFusionRecipes(){
        HashMap<Fluid,Fluid> binds=new HashMap<>();
        for(Materials m:Materials.values()){
            FluidStack p=m.getPlasma(1);
            if(     p!=null) {
                if(ModConfig.DEBUG_MODE) TecTech.Logger.info("Found Plasma of "+m.name());
                if (m.mElement != null &&
                        (m.mElement.mProtons >= Materials.Iron.mElement.mProtons ||
                        -m.mElement.mProtons >= Materials.Iron.mElement.mProtons ||
                         m.mElement.mNeutrons >= Materials.Iron.mElement.mNeutrons ||
                        -m.mElement.mNeutrons >= Materials.Iron.mElement.mNeutrons)) {
                    if (ModConfig.DEBUG_MODE) TecTech.Logger.info("Attempting to bind " + m.name());
                    if (m.getMolten(1) != null) binds.put(p.getFluid(), m.getMolten(1).getFluid());
                    else if (m.getGas(1) != null) binds.put(p.getFluid(), m.getGas(1).getFluid());
                    else if (m.getFluid(1) != null) binds.put(p.getFluid(), m.getFluid(1).getFluid());
                    else binds.put(p.getFluid(),Materials.Iron.getMolten(1).getFluid());
                }
            }
        }
        for(GT_Recipe r:GT_Recipe.GT_Recipe_Map.sFusionRecipes.mRecipeList){
            Fluid f=binds.get(r.mFluidOutputs[0].getFluid());
            if(f!=null){
                if(ModConfig.DEBUG_MODE) TecTech.Logger.info("Nerfing Recipe "+r.mFluidOutputs[0].getUnlocalizedName());
                r.mFluidOutputs[0]=new FluidStack(f,r.mFluidInputs[0].amount);
            }
        }
    }
}
