package gttests.ores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.Material;

import bartworks.common.configs.Configuration;
import bwcrossmod.galacticgreg.VoidMinerUtility;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.Mods;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.Materials;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.config.Gregtech;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

/// The Deep Dark void miner list holds one Stone ore per material, each at weight 1 when no weights are
/// configured.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class VoidMinerDeepDarkTests {

    @GameTest(timeoutTicks = 20, batch = "gt5.voidminer")
    public static void deepDarkListsEachStoneOreOnceAtUnitWeight(GameTestHelper helper) {
        helper.assumeTrue(Gregtech.voidMiners.oreWeightsDD.length == 0, "Deep Dark ore weights are configured");
        helper.assumeTrue(
            Configuration.multiblocks.voidMinerBlacklist.length == 0
                && Configuration.multiblocks.voidMinerBlacklistMaterials.length == 0,
            "The void miner blacklist is configured");
        VoidMinerUtility.DropMap dropMap = VoidMinerUtility.dropMapsByDimName.get(DimensionDef.DimNames.DEEPDARK);
        helper.assertNotNull(dropMap, "No Deep Dark void miner list");

        Map<Material, ItemId> byMaterial = new HashMap<>();
        List<String> mismatches = new ArrayList<>();
        for (Map.Entry<ItemId, Float> entry : dropMap.getInternalMap()
            .entrySet()) {
            ItemId ore = entry.getKey();
            try (OreInfo info = OreManager.getOreInfo(Block.getBlockFromItem(ore.item()), ore.metaData())) {
                if (info == null || info.stoneType != StoneType.Stone || info.isSmall) {
                    mismatches.add(ore.getItemStack() + " is not a Stone ore");
                    continue;
                }
                if (entry.getValue() != 1f) {
                    mismatches.add(info.material.getName() + " at weight " + entry.getValue());
                }
                if (byMaterial.put(info.material, ore) != null) {
                    mismatches.add(info.material.getName() + " listed twice");
                }
            }
        }
        for (Material material : new Material[] { Materials.Iron, Materials.Bismutite, Materials.Cerite, Materials.Salt,
            Materials.RockSalt, Materials.Spodumene }) {
            ItemStack ore = GTOreAdapter.INSTANCE.getVariantStack(material, StoneType.Stone, false);
            ItemId listed = byMaterial.get(material);
            if (ore == null || listed == null
                || listed.item() != ore.getItem()
                || listed.metaData() != ore.getItemDamage()) {
                mismatches.add(material.getName() + " Stone ore not listed");
            }
        }
        helper.assertTrue(mismatches.isEmpty(), "Deep Dark void miner list mismatches: " + mismatches);
        helper.succeed();
    }
}
