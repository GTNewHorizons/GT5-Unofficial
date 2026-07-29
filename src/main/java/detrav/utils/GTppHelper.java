package detrav.utils;

import java.util.HashMap;

import net.minecraft.block.Block;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials2.Materials2Materials;
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
        Materials2Materials.AgarditeCd, Materials2Materials.AgarditeLa, Materials2Materials.AgarditeNd,
        Materials2Materials.AgarditeY, Materials2Materials.Alburnite, Materials2Materials.Cerite,
        Materials2Materials.Comancheite, Materials2Materials.Crocoite, Materials2Materials.CryoliteF,
        Materials2Materials.DemicheleiteBr, Materials2Materials.Florencite, Materials2Materials.Fluorcaphite,
        Materials2Materials.GadoliniteCe, Materials2Materials.GadoliniteY, Materials2Materials.Geikielite,
        Materials2Materials.Greenockite, Materials2Materials.Hibonite, Materials2Materials.Honeaite,
        Materials2Materials.Irarsite, Materials2Materials.Kashinite, Materials2Materials.Lafossaite,
        Materials2Materials.LanthaniteCe, Materials2Materials.LanthaniteLa, Materials2Materials.LanthaniteNd,
        Materials2Materials.Lautarite, Materials2Materials.Lepersonnite, Materials2Materials.Miessiite,
        Materials2Materials.Nichromite, Materials2Materials.Perroudite, Materials2Materials.Polycrase,
        Materials2Materials.BariteRa, Materials2Materials.SamarskiteY, Materials2Materials.SamarskiteYb,
        Materials2Materials.Titanite, Materials2Materials.Xenotime, Materials2Materials.Yttriaite,
        Materials2Materials.Yttrialite, Materials2Materials.Yttrocerite, Materials2Materials.Zimbabweite,
        Materials2Materials.Zircon, Materials2Materials.Zirconolite, Materials2Materials.Zircophyllite,
        Materials2Materials.Zirkelite, Materials2Materials.RadioactiveMineralMix, };
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
        decodeoresGTpp.put((short) (++n + 1), Materials2Materials.FluoriteF);
        encodeoresGTpp.put(Materials2Materials.FluoriteF, (short) (n + 1));
        // Rare Earths
        decodeoresGTpp.put((short) (++n + 1), Materials2Materials.RareEarthI);
        encodeoresGTpp.put(Materials2Materials.RareEarthI, (short) (n + 1));
        decodeoresGTpp.put((short) (++n + 1), Materials2Materials.RareEarthII);
        encodeoresGTpp.put(Materials2Materials.RareEarthII, (short) (n + 1));
        decodeoresGTpp.put((short) (++n + 1), Materials2Materials.RareEarthIII);
        encodeoresGTpp.put(Materials2Materials.RareEarthIII, (short) (n + 1));
        // Koboldite
        decodeoresGTpp.put((short) (++n + 1), Materials2Materials.Koboldite);
        encodeoresGTpp.put(Materials2Materials.Koboldite, (short) (n + 1));
        // Runite
        decodeoresGTpp.put((short) (++n + 1), Materials2Materials.Runite);
        encodeoresGTpp.put(Materials2Materials.Runite, (short) (n + 1));
        // Ancient granite
        decodeoresGTpp.put((short) (++n + 1), Materials2Materials.AncientGranite);
        encodeoresGTpp.put(Materials2Materials.AncientGranite, (short) (n + 1));
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
