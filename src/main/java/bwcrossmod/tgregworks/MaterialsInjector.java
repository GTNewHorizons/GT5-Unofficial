package bwcrossmod.tgregworks;

import static gregtech.api.enums.Mods.TinkersGregworks;

import bartworks.MainMod;
import bartworks.system.material.Werkstoff;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import gregtech.GT_Version;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Config;

@Mod(
    modid = MaterialsInjector.MOD_ID,
    name = MaterialsInjector.NAME,
    version = MaterialsInjector.VERSION,
    dependencies = """
        required-after:IC2;\
        required-after:gregtech;\
        required-after:bartworks;\
        before:TGregworks;\
        before:miscutils;""")
public class MaterialsInjector {

    public static final String NAME = "BartWorks Mod Additions - TGregworks Container";
    public static final String VERSION = GT_Version.VERSION;
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
            .map(Werkstoff::getBridgeMaterial)
            .filter(x -> x.mMetaItemSubID == -1)
            .filter(x -> x.mDurability != 0)
            .forEach(MaterialsInjector::registerParts);

        ItemTGregPart.toolMaterialNames = TGregworks.registry.toolMaterialNames;
    }

    private static void registerParts(Materials m) {
        if (!TGregworks.config.get(Config.Category.Enable, m.mName, true)
            .getBoolean(true)) {
            return;
        }

        int matID = TGregworks.registry.getMaterialID(m);

        TGregworks.registry.toolMaterials.add(m);
        TGregworks.registry.toolMaterialNames.add(m.mDefaultLocalName);

        TGregworks.registry.addToolMaterial(matID, m);
        TGregworks.registry.addBowMaterial(matID, m);
        TGregworks.registry.addArrowMaterial(matID, m);

        TGregworks.registry.matIDs.put(m, matID);
        TGregworks.registry.materialIDMap.put(matID, m);
    }
}
