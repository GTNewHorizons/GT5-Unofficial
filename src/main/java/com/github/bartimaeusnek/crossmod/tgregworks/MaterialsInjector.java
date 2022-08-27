package com.github.bartimaeusnek.crossmod.tgregworks;

import com.github.bartimaeusnek.bartworks.API.LoaderReference;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraftforge.common.config.Property;
import tconstruct.library.TConstructRegistry;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.TGregRegistry;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Config;

@Mod(
        modid = MaterialsInjector.MOD_ID,
        name = MaterialsInjector.NAME,
        version = MaterialsInjector.VERSION,
        dependencies = "required-after:IC2; "
                + "required-after:gregtech; "
                + "required-after:bartworks;"
                + "before:TGregworks;"
                + "before:miscutils; ")
@SuppressWarnings("unchecked")
public class MaterialsInjector {

    public static final String NAME = "BartWorks Mod Additions - TGregworks Container";
    public static final String VERSION = MainMod.VERSION;
    public static final String MOD_ID = "bartworkscrossmodtgregworkscontainer";

    private static HashMap<Materials, Property> configProps;
    private static ArrayList<Integer> configIDs;
    private static Method getGlobalMultiplierMethod;
    private static Method getGlobalMultiplierMethodTwoArguments;
    private static Method getMultiplierMethod;
    private static Method getMaterialIDMethod;
    private static Method getReinforcedLevelMethod;
    private static Method getStoneboundLevelMethod;

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (LoaderReference.TGregworks) {
            MaterialsInjector.preinit();
            MaterialsInjector.run();
        }
    }

    private static void preinit() {
        try {
            getFields();
            getMethodes();
        } catch (IllegalArgumentException
                | IllegalAccessException
                | NoSuchFieldException
                | NoSuchMethodException
                | SecurityException e) {
            MainMod.LOGGER.catching(e);
            FMLCommonHandler.instance().exitJava(1, true);
        }
    }

    private static void getFields() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        Field configPropsField = TGregRegistry.class.getDeclaredField("configProps");
        configPropsField.setAccessible(true);
        configProps = (HashMap<Materials, Property>) configPropsField.get(TGregworks.registry);

        Field configIDsField = TGregRegistry.class.getDeclaredField("configIDs");
        configIDsField.setAccessible(true);
        configIDs = (ArrayList<Integer>) configIDsField.get(TGregworks.registry);
    }

    private static void getMethodes() throws NoSuchMethodException, SecurityException {
        getGlobalMultiplierMethod = TGregRegistry.class.getDeclaredMethod("getGlobalMultiplier", String.class);
        getGlobalMultiplierMethod.setAccessible(true);

        getGlobalMultiplierMethodTwoArguments =
                TGregRegistry.class.getDeclaredMethod("getGlobalMultiplier", String.class, double.class);
        getGlobalMultiplierMethodTwoArguments.setAccessible(true);

        getMultiplierMethod = TGregRegistry.class.getDeclaredMethod("getMultiplier", Materials.class, String.class);
        getMultiplierMethod.setAccessible(true);

        getMaterialIDMethod = TGregRegistry.class.getDeclaredMethod("getMaterialID", Materials.class);
        getMaterialIDMethod.setAccessible(true);

        getReinforcedLevelMethod = TGregRegistry.class.getDeclaredMethod("getReinforcedLevel", Materials.class);
        getReinforcedLevelMethod.setAccessible(true);

        getStoneboundLevelMethod = TGregRegistry.class.getDeclaredMethod("getStoneboundLevel", Materials.class);
        getStoneboundLevelMethod.setAccessible(true);
    }

    public static void run() {
        MainMod.LOGGER.info("Registering TGregworks - BartWorks tool parts.");
        Werkstoff.werkstoffHashSet.stream()
                .filter(x -> x.hasItemType(OrePrefixes.gem) || x.hasItemType(OrePrefixes.plate))
                .map(Werkstoff::getBridgeMaterial)
                .filter(x -> x.mMetaItemSubID == -1)
                .filter(x -> x.mDurability != 0)
                .forEach(m -> {
                    setConfigProps(m);
                    registerParts(m);
                });

        configProps.clear();
        configIDs.clear();

        ItemTGregPart.toolMaterialNames = TGregworks.registry.toolMaterialNames;
    }

    private static void registerParts(Materials m) {
        try {
            TGregworks.registry.toolMaterialNames.add(m.mDefaultLocalName);
            int matID = (int) getMaterialIDMethod.invoke(TGregworks.registry, m);

            addToolMaterial(matID, m);
            addBowMaterial(matID, m);
            addArrowMaterial(matID, m);

            TGregworks.registry.matIDs.put(m, matID);
            TGregworks.registry.materialIDMap.put(matID, m);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            MainMod.LOGGER.catching(e);
            FMLCommonHandler.instance().exitJava(1, true);
        }
    }

    private static void setConfigProps(Materials m) {
        if (TGregworks.config.get(Config.Category.Enable, m.mName, true).getBoolean(true)) {
            TGregworks.registry.toolMaterials.add(m);
            Property configProp =
                    TGregworks.config.get(Config.onMaterial(Config.MaterialID), m.mName, 0, null, 0, 100000);
            configProps.put(m, configProp);
            configIDs.add(configProp.getInt());
        }
    }

    private static void addToolMaterial(int matID, Materials m)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        TConstructRegistry.addToolMaterial(
                matID,
                m.mName,
                m.mLocalizedName,
                m.mToolQuality,
                (int) (m.mDurability
                        * (float) getGlobalMultiplierMethod.invoke(TGregworks.registry, Config.Durability)
                        * (float) getMultiplierMethod.invoke(TGregworks.registry, m, Config.Durability)), // Durability
                (int) (m.mToolSpeed
                        * 100F
                        * (float) getGlobalMultiplierMethod.invoke(TGregworks.registry, Config.MiningSpeed)
                        * (float)
                                getMultiplierMethod.invoke(TGregworks.registry, m, Config.MiningSpeed)), // Mining speed
                (int) (m.mToolQuality
                        * (float) getGlobalMultiplierMethod.invoke(TGregworks.registry, Config.Attack)
                        * (float) getMultiplierMethod.invoke(TGregworks.registry, m, Config.Attack)), // Attack
                (m.mToolQuality - 0.5F)
                        * (float) getGlobalMultiplierMethod.invoke(TGregworks.registry, Config.HandleModifier)
                        * (float) getMultiplierMethod.invoke(
                                TGregworks.registry, m, Config.HandleModifier), // Handle Modifier
                (int) getReinforcedLevelMethod.invoke(TGregworks.registry, m),
                (float) getStoneboundLevelMethod.invoke(TGregworks.registry, m),
                "",
                (m.getRGBA()[0] << 16) | (m.getRGBA()[1] << 8) | (m.getRGBA()[2]));
    }

    private static void addBowMaterial(int matID, Materials m)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        TConstructRegistry.addBowMaterial(
                matID,
                (int) ((float) m.mToolQuality
                        * 10F
                        * (float) getGlobalMultiplierMethod.invoke(TGregworks.registry, Config.BowDrawSpeed)
                        * (float) getMultiplierMethod.invoke(TGregworks.registry, m, Config.BowDrawSpeed)),
                (((float) m.mToolQuality) - 0.5F)
                        * (float) getGlobalMultiplierMethod.invoke(TGregworks.registry, Config.BowFlightSpeed)
                        * (float) getMultiplierMethod.invoke(TGregworks.registry, m, Config.BowFlightSpeed));
    }

    private static void addArrowMaterial(int matID, Materials m)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        TConstructRegistry.addArrowMaterial(
                matID,
                (float) ((((double) m.getMass()) / 10F)
                        * (float) getGlobalMultiplierMethod.invoke(TGregworks.registry, Config.ArrowMass)
                        * (float) getMultiplierMethod.invoke(TGregworks.registry, m, Config.ArrowMass)),
                (float) getGlobalMultiplierMethodTwoArguments.invoke(TGregworks.registry, Config.ArrowBreakChance, 0.9)
                        * (float) getMultiplierMethod.invoke(TGregworks.registry, m, Config.ArrowBreakChance));
    }
}
