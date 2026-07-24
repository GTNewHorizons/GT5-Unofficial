package bwcrossmod.tgregworks;

import static gregtech.api.enums.Mods.TinkersGregworks;

import java.util.Objects;

import bartworks.MainMod;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import gregtech.GT_Version;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MU;
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
        // TGregworks' registry is keyed on the legacy `Materials`, so a werkstoff joins it only through a live
        // GT counterpart: the retired MaterialBuilder bridge that once synthesized one for a werkstoff-own
        // material is gone (accepted API break), leaving the proxy/dual-nature materials that still resolve.
        Werkstoff.werkstoffHashSet.stream()
            .filter(x -> x.hasItemType(OrePrefixes.gem) || x.hasItemType(OrePrefixes.plate))
            .map(WerkstoffReconstruction::materialLibOf)
            .filter(Objects::nonNull)
            .filter(ml -> MU.oldSubId(ml) == -1)
            .filter(ml -> MU.durability(ml) != 0)
            .map(MU::materialOf)
            .filter(Objects::nonNull)
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
