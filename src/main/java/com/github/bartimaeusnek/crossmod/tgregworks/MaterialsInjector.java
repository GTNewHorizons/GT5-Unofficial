package com.github.bartimaeusnek.crossmod.tgregworks;

import static gregtech.api.enums.Mods.TinkersGregworks;

import net.minecraftforge.common.config.Property;

import com.github.bartimaeusnek.bartworks.API.API_REFERENCE;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Config;

@Mod(
        modid = MaterialsInjector.MOD_ID,
        name = MaterialsInjector.NAME,
        version = MaterialsInjector.VERSION,
        dependencies = "required-after:IC2; " + "required-after:gregtech; "
                + "required-after:bartworks;"
                + "before:TGregworks;"
                + "before:miscutils; ")
public class MaterialsInjector {

    public static final String NAME = "BartWorks Mod Additions - TGregworks Container";
    public static final String VERSION = API_REFERENCE.VERSION;
    public static final String MOD_ID = "bartworkscrossmodtgregworkscontainer";

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (TinkersGregworks.isModLoaded()) {
            MaterialsInjector.run();
        }
    }

    public static void run() {
        MainMod.LOGGER.info("Registering TGregworks - BartWorks tool parts.");
        Werkstoff.werkstoffHashSet.stream()
                .filter(x -> x.hasItemType(OrePrefixes.gem) || x.hasItemType(OrePrefixes.plate))
                .map(Werkstoff::getBridgeMaterial).filter(x -> x.mMetaItemSubID == -1).filter(x -> x.mDurability != 0)
                .forEach(m -> {
                    setConfigProps(m);
                    registerParts(m);
                });

        TGregworks.registry.configProps.clear();
        TGregworks.registry.configIDs.clear();

        ItemTGregPart.toolMaterialNames = TGregworks.registry.toolMaterialNames;
    }

    private static void registerParts(Materials m) {
        TGregworks.registry.toolMaterialNames.add(m.mDefaultLocalName);
        int matID = TGregworks.registry.getMaterialID(m);

        TGregworks.registry.addToolMaterial(matID, m);
        TGregworks.registry.addBowMaterial(matID, m);
        TGregworks.registry.addArrowMaterial(matID, m);

        TGregworks.registry.matIDs.put(m, matID);
        TGregworks.registry.materialIDMap.put(matID, m);
    }

    private static void setConfigProps(Materials m) {
        if (TGregworks.config.get(Config.Category.Enable, m.mName, true).getBoolean(true)) {
            TGregworks.registry.toolMaterials.add(m);
            Property configProp = TGregworks.config
                    .get(Config.onMaterial(Config.MaterialID), m.mName, 0, null, 0, 100000);
            TGregworks.registry.configProps.put(m, configProp);
            TGregworks.registry.configIDs.add(configProp.getInt());
        }
    }
}
