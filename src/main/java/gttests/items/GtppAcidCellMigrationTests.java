package gttests.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Mods;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialParts;
import gttests.PosteaTestSupport;

/// Pins that the acid cells gtPlusPlus minted onto IC2's cell item load as the MaterialLib cells of the same
/// acids. Skips in a world's first session; see [PosteaTestSupport].
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class GtppAcidCellMigrationTests {

    @GameTest(timeoutTicks = 20, batch = "gt5.items")
    public static void savedAcidCellsLoadAsTheirMaterialCells(GameTestHelper helper) {
        PosteaTestSupport.assumeItemTransformers(helper);
        Item ic2Cell = (Item) Item.itemRegistry.getObject("IC2:itemCellEmpty");
        helper.assertTrue(ic2Cell != null, "IC2's cell item is not registered");
        Material[] acids = { Materials.IndustrialStrengthHydrofluoricAcid, Materials.SulfurousAcid,
            Materials.SulfuricApatiteMix, Materials.IndustrialStrengthHydrogenChloride, Materials.SulfuricLithiumMix,
            Materials.LithiumHydroxide };
        List<String> mismatches = new ArrayList<>();
        for (int i = 0; i < acids.length; i++) {
            ItemStack loaded = ItemStack
                .loadItemStackFromNBT(new ItemStack(ic2Cell, 1, 15 + i).writeToNBT(new NBTTagCompound()));
            ItemStack expected = MaterialParts.requireCell(acids[i], 1);
            if (loaded == null || loaded.getItem() != expected.getItem()
                || loaded.getItemDamage() != expected.getItemDamage()) {
                mismatches.add((15 + i) + " -> " + (loaded == null ? "null" : loaded.getDisplayName()));
            }
        }
        helper.assertTrue(mismatches.isEmpty(), "Saved gtPlusPlus acid cells did not migrate: " + mismatches);
        helper.succeed();
    }
}
