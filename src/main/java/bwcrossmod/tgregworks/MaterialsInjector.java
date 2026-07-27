package bwcrossmod.tgregworks;

import static gregtech.api.enums.Mods.TinkersGregworks;

import bartworks.MainMod;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import gregtech.GT_Version;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.item.ItemTGregPart;

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
        // TGregworks' registry is keyed on the legacy `Materials`, so a bartworks material joins it only
        // through a live GT counterpart. Stubbed pending a MaterialLib-based port of this registration.

        ItemTGregPart.toolMaterialNames = TGregworks.registry.toolMaterialNames;
    }
}
