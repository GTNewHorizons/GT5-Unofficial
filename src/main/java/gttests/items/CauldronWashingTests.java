package gttests.items;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Mods;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTUtility;

/// Washing MaterialLib crushed ores and impure dusts in a cauldron that holds water.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class CauldronWashingTests {

    private static final TestPos CAULDRON = at(1, 1, 1);

    @GameTest(timeoutTicks = 60, batch = "gt5.cauldron")
    public static void droppedCrushedOreWashes(GameTestHelper helper) {
        helper.setBlock(CAULDRON, Blocks.cauldron, 3);
        ItemStack washed = MaterialLibAPI.getStack(Materials.Iron, Shapes.crushedPurified, 4);
        EntityItem item = helper.spawnEntity(
            new EntityItem(helper.getWorld(), 0, 0, 0, MaterialLibAPI.getStack(Materials.Iron, Shapes.crushed, 4)),
            CAULDRON.x() + 0.5,
            CAULDRON.y() + 0.5,
            CAULDRON.z() + 0.5);
        helper.succeedWhen(
            () -> GTUtility.areStacksEqual(item.getEntityItem(), washed) && item.getEntityItem().stackSize == 4
                && waterLevel(helper) == 2);
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.cauldron")
    public static void impureDustWashesWhenUsedOnTheCauldron(GameTestHelper helper) {
        helper.setBlock(CAULDRON, Blocks.cauldron, 3);
        EntityPlayer player = helper.spawnFakePlayer("CauldronWasher");
        ItemStack held = MaterialLibAPI.getStack(Materials.Iron, Shapes.dustImpure, 2);
        player.inventory.currentItem = 0;
        player.inventory.setInventorySlotContents(0, held);
        TestPos cauldron = helper.absolute(CAULDRON);

        boolean used = held.getItem()
            .onItemUse(held, player, helper.getWorld(), cauldron.x(), cauldron.y(), cauldron.z(), 1, 0.5F, 1F, 0.5F);

        ItemStack result = player.inventory.getStackInSlot(0);
        if (!used || !GTUtility.areStacksEqual(result, MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 2))
            || result.stackSize != 2
            || waterLevel(helper) != 2) {
            helper.fail("Impure iron dust did not wash into 2 iron dust; slot holds " + result);
        }
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.cauldron")
    public static void registeredCleaningTargetReplacesTheWashedDust(GameTestHelper helper) {
        helper.setBlock(CAULDRON, Blocks.cauldron, 3);
        EntityPlayer player = helper.spawnFakePlayer("CauldronWasher");
        ItemStack held = MaterialLibAPI.getStack(Materials.Cerium, Shapes.dustImpure, 1);
        player.inventory.currentItem = 0;
        player.inventory.setInventorySlotContents(0, held);
        TestPos cauldron = helper.absolute(CAULDRON);

        held.getItem()
            .onItemUse(held, player, helper.getWorld(), cauldron.x(), cauldron.y(), cauldron.z(), 1, 0.5F, 1F, 0.5F);

        ItemStack result = player.inventory.getStackInSlot(0);
        if (!GTUtility.areStacksEqual(result, MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 1))) {
            helper.fail("Impure cerium dust did not wash into cerium-rich mixture; slot holds " + result);
        }
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.cauldron")
    public static void plainDustNeitherWashesNorDrainsTheCauldron(GameTestHelper helper) {
        helper.setBlock(CAULDRON, Blocks.cauldron, 3);
        EntityPlayer player = helper.spawnFakePlayer("CauldronWasher");
        ItemStack held = MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 2);
        player.inventory.currentItem = 0;
        player.inventory.setInventorySlotContents(0, held);
        TestPos cauldron = helper.absolute(CAULDRON);

        boolean used = held.getItem()
            .onItemUse(held, player, helper.getWorld(), cauldron.x(), cauldron.y(), cauldron.z(), 1, 0.5F, 1F, 0.5F);

        if (used || player.inventory.getStackInSlot(0) != held || waterLevel(helper) != 3) {
            helper.fail("Iron dust was washed or drained the cauldron");
        }
        helper.succeed();
    }

    private static int waterLevel(GameTestHelper helper) {
        TestPos cauldron = helper.absolute(CAULDRON);
        return helper.getWorld()
            .getBlockMetadata(cauldron.x(), cauldron.y(), cauldron.z());
    }
}
