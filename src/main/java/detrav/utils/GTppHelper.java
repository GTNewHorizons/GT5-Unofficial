package detrav.utils;

import java.util.HashMap;

import net.minecraft.block.Block;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials.Materials;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class GTppHelper {

    private static boolean initialized;
    private static final HashMap<Short, Material> decodeoresGTpp = new HashMap<>();
    private static final HashMap<Material, Short> encodeoresGTpp = new HashMap<>();

    /// The gtpp ore materials detrav's ore scanner assigns ids to, in the exact order the retired
    /// `MaterialsOres` pool declared its fields -- detrav's external scanner keys ids by this order (see
    /// [#generate_OreIDs]), so it must stay stable.
    // spotless:off
    private static final Material[] ORE_IDS = {
        Materials.AgarditeCd, Materials.AgarditeLa, Materials.AgarditeNd,
        Materials.AgarditeY, Materials.Alburnite, Materials.Cerite,
        Materials.Comancheite, Materials.Crocoite, Materials.CryoliteF,
        Materials.DemicheleiteBr, Materials.Florencite, Materials.Fluorcaphite,
        Materials.GadoliniteCe, Materials.GadoliniteY, Materials.Geikielite,
        Materials.Greenockite, Materials.Hibonite, Materials.Honeaite,
        Materials.Irarsite, Materials.Kashinite, Materials.Lafossaite,
        Materials.LanthaniteCe, Materials.LanthaniteLa, Materials.LanthaniteNd,
        Materials.Lautarite, Materials.Lepersonnite, Materials.Miessiite,
        Materials.Nichromite, Materials.Perroudite, Materials.Polycrase,
        Materials.BariteRa, Materials.SamarskiteY, Materials.SamarskiteYb,
        Materials.Titanite, Materials.Xenotime, Materials.Yttriaite,
        Materials.Yttrialite, Materials.Yttrocerite, Materials.Zimbabweite,
        Materials.Zircon, Materials.Zirconolite, Materials.Zircophyllite,
        Materials.Zirkelite, Materials.RadioactiveMineralMix, };
    // spotless:on

    private static void generate_OreIDs() {
        short n = 0;
        for (; n < ORE_IDS.length; ++n) {
            Short i = (short) (n + 1);
            decodeoresGTpp.put(i, ORE_IDS[n]);
            encodeoresGTpp.put(ORE_IDS[n], i);
        }
        // Manually add ores from other places than the ore class
        // Fluorite
        decodeoresGTpp.put((short) (++n + 1), Materials.FluoriteF);
        encodeoresGTpp.put(Materials.FluoriteF, (short) (n + 1));
        // Rare Earths
        decodeoresGTpp.put((short) (++n + 1), Materials.RareEarthI);
        encodeoresGTpp.put(Materials.RareEarthI, (short) (n + 1));
        decodeoresGTpp.put((short) (++n + 1), Materials.RareEarthII);
        encodeoresGTpp.put(Materials.RareEarthII, (short) (n + 1));
        decodeoresGTpp.put((short) (++n + 1), Materials.RareEarthIII);
        encodeoresGTpp.put(Materials.RareEarthIII, (short) (n + 1));
        // Koboldite
        decodeoresGTpp.put((short) (++n + 1), Materials.Koboldite);
        encodeoresGTpp.put(Materials.Koboldite, (short) (n + 1));
        // Runite
        decodeoresGTpp.put((short) (++n + 1), Materials.Runite);
        encodeoresGTpp.put(Materials.Runite, (short) (n + 1));
        // Ancient granite
        decodeoresGTpp.put((short) (++n + 1), Materials.AncientGranite);
        encodeoresGTpp.put(Materials.AncientGranite, (short) (n + 1));
    }

    public static short getMetaFromBlock(Block block) {
        if (!initialized) {
            generate_OreIDs();
            initialized = true;
        }
        return (short) (GTppHelper.encodeoresGTpp.get(materialOf(block)) + 7000);
    }

    public static Material getMatFromMeta(int meta) {
        if (!initialized) {
            generate_OreIDs();
            initialized = true;
        }
        return GTppHelper.decodeoresGTpp.get((short) (meta - 7000));
    }

    public static boolean isGTppBlock(Block block) {
        return materialOf(block) != null;
    }

    /// The gtpp-family material a world-placed ore block holds, or null when the block is not gtpp ore.
    /// [GTOreAdapter#supports(Block,int)] cannot answer this -- it recognises any GT ore block, whatever family
    /// the material belongs to -- so the family test is applied to the resolved material instead.
    private static Material materialOf(Block block) {
        try (OreInfo info = OreManager.getOreInfo(block, 0)) {
            if (info == null || !GTOreAdapter.isGtppFamily(info.material)) return null;

            return info.material;
        }
    }

    public static String getGTppVeinName(Block block) {
        return block.getLocalizedName();
    }

}
