package gttests.electricblastfurnace;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;

@GameTestHolder(value = "gregtech", templatePrefix = "multiblock/electric_blast_furnace")
public class ElectricBlastFurnaceFormationTests {

    @GameTest(template = "valid", batch = "gt5.ebf")
    public static void validStructureForms(GameTestHelper helper) {
        Multiblock ebf = helper.gtnh()
                .multiblock(at(1, 0, 0));
        ebf.fixMaintenance();

        helper.succeedWhen(ebf::isFormed);
    }

    @GameTest(template = "missing_coils", timeoutTicks = 60, batch = "gt5.ebf")
    public static void missingCoilsNeverForms(GameTestHelper helper) {
        Multiblock ebf = helper.gtnh()
                .multiblock(at(1, 0, 0));
        ebf.fixMaintenance();
        helper.onEachTick(() -> helper.assertFalse(ebf.isFormed(), "EBF formed without coils"));
        helper.succeedAtTimeout();
    }
}
