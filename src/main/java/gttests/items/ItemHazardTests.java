package gttests.items;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Mods;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.hazards.ItemHazards;

/// Checks that [ItemHazards] reads the hazards of MaterialLib parts from their materials.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class ItemHazardTests {

    @GameTest(timeoutTicks = 20, batch = "gt5.hazards")
    public static void materialPartsCarryTheirMaterialsHazards(GameTestHelper helper) {
        expectRadiation(helper, MaterialLibAPI.getStack(Materials.Thorium, Shapes.ingot, 1), 1);
        expectRadiation(helper, MaterialLibAPI.getStack(Materials.Uranium235, Shapes.ingot, 1), 2);
        expectRadiation(helper, MaterialLibAPI.getStack(Materials.Iron, Shapes.ingot, 1), 0);
        if (!ItemHazards.isToxic(MaterialLibAPI.getStack(Materials.Tiberium, Shapes.dust, 1))) {
            helper.fail("Tiberium dust is not toxic");
        }
        if (ItemHazards.heatDamage(MaterialLibAPI.getStack(Materials.Titanium, Shapes.ingotHot, 1)) <= 0) {
            helper.fail("Hot titanium ingot deals no heat damage");
        }
        helper.succeed();
    }

    private static void expectRadiation(GameTestHelper helper, ItemStack stack, int level) {
        int actual = ItemHazards.radiationLevel(stack);
        if (actual != level) helper.fail(stack.getDisplayName() + " has radiation level " + actual + ", not " + level);
    }
}
