package bwcrossmod.tgregworks;

import static gregtech.api.enums.Mods.TinkersGregworks;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.MainMod;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import gregtech.GT_Version;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Config;

/// Adds the werkstoff-origin materials to TGregworks' tool material registry, which TGregworks itself fills
/// during preInit with only the materials gregtech gave a meta-item sub id. Runs from init, before TGregworks'
/// own init reads [vexatos.tgregworks.integration.TGregRegistry#toolMaterials].
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
        for (Material material : MaterialLibAPI.getMaterials()) {
            if (!LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.gem)
                && !LegacyWerkstoffIndex.generatesPrefix(material, OrePrefixes.plate)) {
                continue;
            }
            if (MaterialUtils.durability(material) == 0) continue;
            // Hafnium and Zirconium pass the durability gate only on the default their gtPlusPlus declaration
            // carries, not on a bartworks-declared tool stat.
            if (material == Materials.Hafnium || material == Materials.Zirconium) continue;
            registerParts(material);
        }

        ItemTGregPart.toolMaterialNames = TGregworks.registry.toolMaterialNames;
    }

    private static void registerParts(Material material) {
        String name = MaterialUtils.internalName(material);
        if (!TGregworks.config.get(Config.Category.Enable, name, true)
            .getBoolean(true)) {
            return;
        }

        int matID = TGregworks.registry.getMaterialID(material);

        TGregworks.registry.toolMaterials.add(material);
        TGregworks.registry.toolMaterialNames.add(MaterialUtils.localName(material));

        TGregworks.registry.addToolMaterial(matID, material);
        TGregworks.registry.addBowMaterial(matID, material);
        TGregworks.registry.addArrowMaterial(matID, material);

        TGregworks.registry.matIDs.put(material, matID);
        TGregworks.registry.materialIDMap.put(matID, material);
    }
}
