package gttests.oredict;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Mods;
import gregtech.api.material.MaterialParts;
import gregtech.loaders.postload.ForeignPartTable;
import gttests.PosteaTestSupport;

/// Pins that a saved stack of another mod's unified part loads as the MaterialLib pair it stands for. Skips in a
/// world's first session; see [PosteaTestSupport].
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class ForeignPartMigrationTests {

    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void savedForeignPartsLoadAsTheirPair(GameTestHelper helper) {
        PosteaTestSupport.assumeItemTransformers(helper);
        List<String> mismatches = new ArrayList<>();
        int checked = 0;
        for (ForeignPartTable.Entry entry : ForeignPartTable.ENTRIES) {
            Item item = (Item) Item.itemRegistry.getObject(entry.registryName());
            if (item == null) continue;
            ItemStack expected = MaterialParts
                .stack(entry.prefix(), MaterialLibAPI.getMaterial("gregtech", entry.materialName()), 1);
            ItemStack loaded = ItemStack
                .loadItemStackFromNBT(new ItemStack(item, 1, entry.meta()).writeToNBT(new NBTTagCompound()));
            checked++;
            if (loaded == null || expected == null
                || loaded.getItem() != expected.getItem()
                || loaded.getItemDamage() != expected.getItemDamage()) {
                mismatches.add(entry.registryName() + "@" + entry.meta() + " -> " + describe(loaded));
            }
        }
        helper.assertTrue(checked > 0, "No foreign part in the table is registered");
        helper.assertTrue(mismatches.isEmpty(), "Saved foreign parts did not migrate: " + mismatches);
        helper.succeed();
    }

    private static String describe(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "null";
        return Item.itemRegistry.getNameForObject(stack.getItem()) + "@" + stack.getItemDamage();
    }
}
