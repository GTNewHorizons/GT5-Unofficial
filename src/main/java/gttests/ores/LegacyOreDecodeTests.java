package gttests.ores;

import java.util.ArrayList;
import java.util.List;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Mods;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;
import gregtech.common.ores.BWOreAdapter;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.OreInfo;

/// Decoding of the legacy tile-entity ore metas that VisualProspecting reads from unconverted chunks.
/// `GT_TileEntity_Ores` metas are the legacy sub id, plus 1000 per stone index, plus 16000 for small ores.
/// `bw.blockoresTE` holds a werkstoff id on Stone.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class LegacyOreDecodeTests {

    private static final int NETHERRACK = 1000;
    private static final int ENDSTONE = 2000;
    private static final int SMALL = 16000;

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void gregTechOreMetasDecodeToMaterialStoneAndSize(GameTestHelper helper) {
        int iron = MaterialUtils.oldSubId(Materials.Iron);
        helper.assertTrue(iron >= 0, "Iron has no legacy sub id");
        List<String> mismatches = new ArrayList<>();
        check(mismatches, "meta " + iron, GTOreAdapter.INSTANCE.getLegacyOreInfo(iron), StoneType.Stone, false);
        check(
            mismatches,
            "meta " + (iron + NETHERRACK),
            GTOreAdapter.INSTANCE.getLegacyOreInfo(iron + NETHERRACK),
            StoneType.Netherrack,
            false);
        check(
            mismatches,
            "meta " + (iron + ENDSTONE + SMALL),
            GTOreAdapter.INSTANCE.getLegacyOreInfo(iron + ENDSTONE + SMALL),
            StoneType.Endstone,
            true);
        try (OreInfo steel = GTOreAdapter.INSTANCE.getLegacyOreInfo(MaterialUtils.oldSubId(Materials.Steel))) {
            helper.assertNull(steel, "Steel's legacy meta decoded to an ore");
        }
        helper.assertTrue(mismatches.isEmpty(), "Legacy GregTech ore metas decode wrongly: " + mismatches);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void werkstoffOreMetasDecodeToMaterialStoneAndSize(GameTestHelper helper) {
        int bismutite = LegacyWerkstoffIndex.idOf(Materials.Bismutite);
        helper.assertTrue(bismutite >= 0, "Bismutite has no legacy werkstoff id");
        List<String> mismatches = new ArrayList<>();
        check(
            mismatches,
            "ore " + bismutite,
            BWOreAdapter.INSTANCE.getLegacyOreInfo(bismutite, false),
            Materials.Bismutite,
            StoneType.Stone,
            false);
        check(
            mismatches,
            "small ore " + bismutite,
            BWOreAdapter.INSTANCE.getLegacyOreInfo(bismutite, true),
            Materials.Bismutite,
            StoneType.Stone,
            true);
        helper.assertTrue(mismatches.isEmpty(), "Legacy werkstoff ore metas decode wrongly: " + mismatches);
        helper.succeed();
    }

    private static void check(List<String> mismatches, String legacy, OreInfo decoded, StoneType stone, boolean small) {
        check(mismatches, legacy, decoded, Materials.Iron, stone, small);
    }

    private static void check(List<String> mismatches, String legacy, OreInfo decoded, Material material,
        StoneType stone, boolean small) {
        try (OreInfo info = decoded) {
            if (info == null || info.material != material
                || info.stoneType != stone
                || info.isSmall != small
                || !info.isNatural) {
                mismatches.add(legacy + " -> " + info);
            }
        }
    }
}
