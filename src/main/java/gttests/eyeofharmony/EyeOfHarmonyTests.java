package gttests.eyeofharmony;

import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static gregtech.common.misc.WirelessNetworkManager.setUserEU;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gtneioreplugin.plugin.block.ModBlocks;
import tectech.thing.metaTileEntity.multi.MTEEyeOfHarmony;

@GameTestHolder(value = Mods.ModIDs.GREG_TECH, templatePrefix = "multiblock/eye_of_harmony")
public final class EyeOfHarmonyTests {

    private static final String LABEL_CONTROLLER = "controller";
    private static final String LABEL_HYDROGEN_HATCH = "hatch_1";
    private static final String LABEL_HELIUM_HATCH = "hatch_2";

    private static final String STORED_HYDROGEN_TAG = "stored.fluid.hydrogen";
    private static final String STORED_HELIUM_TAG = "stored.fluid.helium";
    private static final String SUCCESS_CHANCE_TAG = "eyeOfHarmonyOutputrecipeSuccessChance";
    private static final String PITY_CHANCE_TAG = "eyeOfHarmonyOutputpityChance";
    private static final String PREVIOUS_CHANCE_TAG = "eyeOfHarmonyOutputpreviousChance";

    private static final int FILL_CHUNK = 2_000_000_000;
    private static final int PENULTIMATE_FILL_CHUNK = 1_990_000_000;
    private static final int FINAL_FILL_CHUNK = 20_000_000;
    private static final long PRELOADED_FLUID = 8_000_000_000L;
    private static final double RAW_SUCCESS_CHANCE = 1.0 - 9 * 0.05 - 7 * 0.0925 + 7 * 0.05;

    private EyeOfHarmonyTests() {}

    @GameTest(template = "valid")
    public static void pityGuaranteeSurvivesFluidOverflow(GameTestHelper helper) {
        TestPos controllerPos = helper.pos(LABEL_CONTROLLER);
        TestPos hydrogenHatchPos = helper.pos(LABEL_HYDROGEN_HATCH);
        TestPos heliumHatchPos = helper.pos(LABEL_HELIUM_HATCH);
        Multiblock multiblock = helper.gtnh()
            .multiblock(controllerPos);
        helper.assertTrue(multiblock.forceStructureCheck(), "The labeled Eye of Harmony structure did not form");
        multiblock.assertFormed();
        multiblock.fixMaintenance();

        MTEEyeOfHarmony controller = (MTEEyeOfHarmony) helper.gtnh()
            .metaTileEntity(controllerPos);
        controller
            .setInventorySlotContents(controller.getControllerSlotIndex(), new ItemStack(ModBlocks.getBlock("DD")));

        double excess = (PRELOADED_FLUID + PENULTIMATE_FILL_CHUNK + FINAL_FILL_CHUNK) / 10_000_000_000.0 - 1;
        double overflowPenalty = 1 - Math.exp(-(30 * excess) * (30 * excess));
        double effectiveChance = RAW_SUCCESS_CHANCE - 2 * overflowPenalty;

        NBTTagCompound state = new NBTTagCompound();
        controller.saveNBTData(state);
        state.setLong(STORED_HYDROGEN_TAG, 0);
        state.setLong(STORED_HELIUM_TAG, 0);
        state.setDouble(SUCCESS_CHANCE_TAG, effectiveChance);
        state.setDouble(PREVIOUS_CHANCE_TAG, effectiveChance);
        state.setDouble(PITY_CHANCE_TAG, 1);
        controller.loadNBTData(state);

        helper.gtnh()
            .fastForwardTicks(1);
        UUID owner = controller.getBaseMetaTileEntity()
            .getOwnerUuid();
        BigInteger originalEU = getUserEU(owner);
        helper.afterTest(() -> setUserEU(owner, originalEU));
        setUserEU(owner, BigInteger.valueOf(Long.MAX_VALUE));

        for (int i = 0; i < 4; i++) {
            helper.gtnh()
                .fillHatch(hydrogenHatchPos, Materials.Hydrogen.getGas(FILL_CHUNK));
            helper.gtnh()
                .fillHatch(heliumHatchPos, Materials.Helium.getGas(FILL_CHUNK));
            helper.gtnh()
                .fastForwardTicks(20);
        }

        NBTTagCompound preloadedState = new NBTTagCompound();
        controller.saveNBTData(preloadedState);
        helper.assertEquals(PRELOADED_FLUID, preloadedState.getLong(STORED_HYDROGEN_TAG));
        helper.assertEquals(PRELOADED_FLUID, preloadedState.getLong(STORED_HELIUM_TAG));

        helper.gtnh()
            .fillHatch(hydrogenHatchPos, Materials.Hydrogen.getGas(PENULTIMATE_FILL_CHUNK));
        helper.gtnh()
            .fillHatch(heliumHatchPos, Materials.Helium.getGas(PENULTIMATE_FILL_CHUNK));
        helper.gtnh()
            .fastForwardTicks(20);

        helper.gtnh()
            .fillHatch(hydrogenHatchPos, Materials.Hydrogen.getGas(FINAL_FILL_CHUNK));
        helper.gtnh()
            .fillHatch(heliumHatchPos, Materials.Helium.getGas(FINAL_FILL_CHUNK));
        helper.gtnh()
            .fastForwardTicks(20);

        helper.assertTrue(multiblock.isProcessing(), "The real Deep Dark recipe never started");
        NBTTagCompound runningState = new NBTTagCompound();
        controller.saveNBTData(runningState);
        helper.assertEquals(
            1.0,
            runningState.getDouble(SUCCESS_CHANCE_TAG),
            "A full pity meter must guarantee the next attempt even with fluid overflow");
        helper.succeed();
    }
}
