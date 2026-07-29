package gttests.multiblockProcessing;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.ItemMatcher;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

@GameTestHolder(value = "gregtech", templatePrefix = "multiblock/ebf_double_read")
public class ProcessingTests {

    private static Multiblock ebf(GameTestHelper helper) {
        return helper.gtnh()
            .multiblock(helper.pos("ebfController"));
    }

    private static TileEntity getControllerTE(GameTestHelper helper) {
        return helper.assertTileEntityPresent(helper.pos("ebfController"));
    }

    private static Multiblock ebfAndInsertSteelDust(GameTestHelper helper) {
        Multiblock ebf = ebf(helper);
        ebf.assertFormed();
        helper.insertItem(helper.pos("inputChest"), Materials.Steel.getDust(1));
        return ebf;
    }

    @GameTest(template = "doubleReadCrashTest", timeoutTicks = 200)
    public static void doubleReadChestCrashController(GameTestHelper helper) {
        Multiblock ebf = ebfAndInsertSteelDust(helper);
        IGregTechTileEntity igte = (IGregTechTileEntity) getControllerTE(helper);
        helper.succeedWhen(() -> {
            ebf.outputs()
                .assertEmpty();
            return !igte.isAllowedToWork();
        });
    }

    @GameTest(template = "doubleReadOk", timeoutTicks = 200)
    public static void doubleReadStorageNoDupe(GameTestHelper helper) {
        Multiblock ebf = ebfAndInsertSteelDust(helper);
        helper.succeedWhen(() -> {
            ebf.outputs()
                .assertNotContains(ItemMatcher.predicate(stack -> stack.stackSize > 1));
            return ItemStack.areItemStacksEqual(
                ebf.outputBus(0)
                    .slot(0),
                Materials.Steel.getIngots(1));
        });
    }
}
