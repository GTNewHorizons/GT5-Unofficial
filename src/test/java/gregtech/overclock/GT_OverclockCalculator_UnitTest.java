package gregtech.overclock;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;

class GT_OverclockCalculator_UnitTest {

    private static final String messageDuration = "Duration Calculated Wrong";
    private static final String messageEUt = "EUt Calculated Wrong";

    @Test
    void fullPerfectOverclockEBF_Test() {
        int heatDiscounts = (1800 * 4) / 900;
        long correctConsumption = (long) Math.ceil(VP[5] * GTUtility.powInt(0.95, heatDiscounts));
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[5])
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(1800 * 5)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(4, 4), calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOverclockEBFWithOneHeatDiscount_Test() {
        int heatDiscounts = 1;
        long correctConsumption = (long) Math.ceil(VP[5] * GTUtility.powInt(0.95, heatDiscounts));
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[5])
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(2700)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 4), calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOverclockEBFWithoutHeatDiscounts_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[5])
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(1800)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 4), calculator.getDuration(), messageDuration);
        assertEquals(VP[5], calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectAndImperfectOverclockEBFWithTwoHeatDiscounts_Test() {
        int heatDiscounts = 2;
        long correctConsumption = (long) Math.ceil(VP[5] * GTUtility.powInt(0.95, heatDiscounts));
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[5])
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(3600)
            .calculate();
        assertEquals(1024 / (4.0D * GTUtility.powInt(2, 3)), calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void generalImperfectOverclock_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1024)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 5), calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void generalPerfectOverclock_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(2048)
            .enablePerfectOC()
            .calculate();
        assertEquals(2048 / GTUtility.powInt(4, 5), calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdHeatEBF_Test() {
        int heatDiscounts = 3;
        long correctConsumption = (long) Math.ceil(VP[6] * GTUtility.powInt(0.95, heatDiscounts));
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(2048)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(486)
            .setMachineHeat(3900)
            .calculate();
        assertEquals(2048 / (4.0D * GTUtility.powInt(2, 4)), calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void doubleEnergyHatchOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setAmperage(4)
            .setDuration(1024)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 6), calculator.getDuration(), messageDuration);
        assertEquals(VP[7], calculator.getConsumption(), messageEUt);
    }

    @Test
    void doubleEnergyHatchOCForULV_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[0])
            .setEUt(V[6])
            .setAmperage(4)
            .setDuration(1024)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 6), calculator.getDuration(), messageDuration);
        assertEquals(VP[0] * GTUtility.powInt(4, 6), calculator.getConsumption(), messageEUt);
    }

    @Test
    void multiAmpHatchOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setAmperage(256)
            .setDuration(1024)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 9), calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdAmpHatchOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setAmperage(320)
            .setDuration(1024)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 9), calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelImperfectOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[8])
            .setAmperage(4)
            .setDuration(1024)
            .setParallel(16)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 6), calculator.getDuration(), messageDuration);
        assertEquals(VP[9], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelPerfectOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[8])
            .setAmperage(4)
            .setDuration(4096)
            .setParallel(16)
            .setAmperageOC(true)
            .enablePerfectOC()
            .calculate();
        assertEquals(4096 / GTUtility.powInt(4, 6), calculator.getDuration(), messageDuration);
        assertEquals(VP[9], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelMultiAmpOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setAmperage(320)
            .setDuration(1024)
            .setParallel(16)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 7), calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdParallelOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[8])
            .setDuration(1024)
            .setParallel(8)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 5), calculator.getDuration(), messageDuration);
        assertEquals(VP[6] * 8, calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdParallelAndAmpsOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[8])
            .setAmperage(320)
            .setDuration(1024)
            .setParallel(8)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 9), calculator.getDuration(), messageDuration);
        assertEquals(VP[6] * 8 * 256, calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdTimeOC_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(77)
            .calculate();
        assertEquals((int) (77 / GTUtility.powInt(2, 5)), calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void correctWhenOneTicking_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(VP[1] * GTUtility.powInt(4, 5), calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOCWithEUtDiscount_Test() {
        long correctConsumption = (long) Math.ceil(VP[1] * 0.9f) << 10;
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setEUtDiscount(0.9)
            .setDuration(1024)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 5), calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectOCWithEUtDiscount_Test() {
        long correctConsumption = (long) Math.ceil(VP[1] * 0.9f) << 10;
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setEUtDiscount(0.9)
            .setDuration(1024)
            .enablePerfectOC()
            .calculate();
        assertEquals(1024 / GTUtility.powInt(4, 5), calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOCWithSpeedBoost_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDurationModifier(0.9)
            .setDuration(1024)
            .calculate();
        assertEquals((int) (1024 * 0.9f / GTUtility.powInt(2, 5)), calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectOCWithSpeedBoost_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDurationModifier(0.9)
            .setDuration(2048)
            .enablePerfectOC()
            .calculate();
        assertEquals((int) ((2048 * 0.9f) / GTUtility.powInt(4, 5)), calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectOC3TicksTo1Tick_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[4])
            .setEUt(V[5])
            .setDuration(3)
            .enablePerfectOC()
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(VP[5], calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeWithDiscount_Test() {
        long correctConsumption = (long) Math.ceil((VP[0] << 10) * 0.9f);
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[0])
            .setEUt(V[6])
            .setEUtDiscount(0.9)
            .setDuration(1024)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 5), calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeWithDiscountWithParallel_Test() {
        long correctConsumption = (long) Math.ceil((VP[0] << 6) * 14 * 0.9f);
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[0])
            .setEUt(V[5])
            .setEUtDiscount(0.9)
            .setParallel(14)
            .setDuration(1024)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 3), calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeCorrectWhenOneTicked_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[0])
            .setEUt(V[6])
            .setDuration(1)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(VP[0] * GTUtility.powInt(4, 5), calculator.getConsumption(), messageEUt);
    }

    @Test
    void testNotPowerOverflowing_Test() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(16)
            .setParallel(64)
            .setAmperage(64)
            .setAmperageOC(true)
            .setEUt(V[5])
            .setDuration(30)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(16 * GTUtility.powInt(4, 7), calculator.getConsumption(), messageEUt);
    }

    @Test
    void testCorrectEUtWhenOverclockingUnderOneTick_Test() {
        // log4(24 * 56) ~= 5.2
        // log4(V[14] * 1_048_576) ~= 25.5
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(24)
            .setParallel(56)
            .setAmperage(1)
            .setAmperageOC(true)
            .setDurationModifier(1 / 6.0)
            .setEUt(V[14] * 1_048_576)
            .setDuration(56)
            .setCurrentParallel(6144)
            .calculate();
        assertEquals((24 * 56) * GTUtility.powInt(4, 20), calculator.getConsumption());
    }

    @Test
    void testCorrectEUtWhenOverclockingUnderOneTickWithHeat_Test() {
        double heatDiscount = GTUtility.powInt(0.95, 15);
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(1920)
            .setParallel(256)
            .setAmperage(1)
            .setAmperageOC(true)
            .setHeatDiscount(true)
            .setHeatOC(true)
            .setRecipeHeat(2000)
            .setMachineHeat(15500)
            .setEUt(V[12] * 1_048_576)
            .setDuration(250);
        calculator.setCurrentParallel((int) (256 * calculator.calculateMultiplierUnderOneTick()))
            .calculate();
        assertEquals(
            Math.ceil((((long) 1920 * 256) * GTUtility.powInt(4, 14)) * heatDiscount),
            calculator.getConsumption());
    }

    @Test
    void testCorrectOverclockWhenCurrentParallelLessThanOriginalParallel() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(24)
            .setParallel(64)
            .setAmperage(1)
            .setAmperageOC(true)
            .setEUt(V[6])
            .setDuration(56)
            .setCurrentParallel(16)
            .calculate();
        assertEquals(56 / GTUtility.powInt(2, 3), calculator.getDuration());
        assertEquals((24 * 16) * GTUtility.powInt(4, 3), calculator.getConsumption());
    }

    @Test
    void stopsCorrectlyWhenOverclockingUnderOneTick() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[2])
            .setEUt(V[9])
            .setParallel(4)
            .setDuration(10)
            .setAmperageOC(false)
            .setAmperage(16)
            .setCurrentParallel(16)
            .calculate();
        assertEquals((VP[2] * 4) * GTUtility.powInt(4, 7), calculator.getConsumption());
    }

    @Test
    void testNoOverclockCorrectWithUnderOneTickLogic_Test() {
        OverclockCalculator calculator = OverclockCalculator.ofNoOverclock(2_693_264_510L, 100)
            .setParallel(24 * 64);
        assertEquals(1, calculator.calculateMultiplierUnderOneTick());
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOC() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(2)
            .setEUt(V[3])
            .setParallel(16)
            .setDuration(20)
            .setAmperageOC(false)
            .setAmperage(16)
            .calculate();
        assertEquals(32 * 16, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion2() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(2)
            .setEUt(V[3])
            .setParallel(64)
            .setDuration(20)
            .setAmperageOC(false)
            .setAmperage(16)
            .calculate();
        assertEquals(32 * 64, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion3() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(2)
            .setEUt(V[3])
            .setParallel(16)
            .setDuration(20)
            .setAmperageOC(false)
            .setAmperage(64)
            .calculate();
        assertEquals(32 * 16, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion4() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(8)
            .setEUt(V[3])
            .setParallel(16)
            .setDuration(20)
            .setAmperageOC(false)
            .setAmperage(16)
            .calculate();
        assertEquals((8 * 16) * GTUtility.powInt(4, 2), calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion5() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(8)
            .setEUt(V[3])
            .setParallel(16)
            .setDuration(20)
            .setAmperageOC(false)
            .calculate();
        assertEquals((8 * 16) * GTUtility.powInt(4, 1), calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion6() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(7)
            .setEUt(V[3])
            .setParallel(64)
            .setDuration(160)
            .setAmperage(64)
            .setAmperageOC(false)
            .calculate();
        assertEquals((7 * 64) * GTUtility.powInt(4, 2), calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion7() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(7)
            .setEUt(V[3])
            .setParallel(19)
            .setDuration(160)
            .setAmperage(19)
            .setAmperageOC(false)
            .calculate();
        assertEquals((7 * 19) * GTUtility.powInt(4, 2), calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion8() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(7)
            .setEUt(V[3])
            .setParallel(29)
            .setDuration(160)
            .setAmperage(25)
            .setAmperageOC(false)
            .calculate();
        assertEquals((7 * 29) * GTUtility.powInt(4, 2), calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeWorkCorrectlyWithCalculatingEutUnderOneTick() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(2)
            .setEUt(V[8])
            .setParallel(64)
            .setDuration(300)
            .setAmperage(64)
            .setAmperageOC(false)
            .calculate();
        assertEquals(2 * 64 * GTUtility.powInt(4, 7), calculator.getConsumption(), messageEUt);
    }

    @Test
    void overclockWithAbnormalEnergyIncrease_Test() {
        long expectedEUt = (long) Math.ceil(VP[1] * GTUtility.powInt(4.1, 5));
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1024)
            .setEUtIncreasePerOC(4.1)
            .calculate();
        assertEquals(1024 / GTUtility.powInt(2, 5), calculator.getDuration(), messageDuration);
        assertEquals(expectedEUt, calculator.getConsumption(), messageEUt);
    }

    @Test
    void overclockWithAbnormalDurationDecrease_Test() {
        int expectedDuration = (int) Math.floor(1024 / GTUtility.powInt(2.1, 5));
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1024)
            .setDurationDecreasePerOC(2.1)
            .calculate();
        assertEquals(expectedDuration, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void slightlyOverOneAmpRecipeWorksWithSingleEnergyHatch() {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(614400)
            .setEUt(TierEU.UV)
            .setDuration(600)
            .setAmperage(2)
            .setAmperageOC(false)
            .calculate();
        assertEquals(600, calculator.getDuration(), messageDuration);
        assertEquals(614400, calculator.getConsumption(), messageEUt);
    }

    static Stream<Arguments> noFloatImprecisionIssueParameters() {
        return Stream.of(
            // result using float
            Arguments.arguments(1 / 2.25f, 0.9f, 42_467_327),
            // result using double
            Arguments.arguments(1 / 2.25, 0.9, 42_467_328));
    }

    @ParameterizedTest
    @MethodSource("noFloatImprecisionIssueParameters")
    void floatPrecisionIssues(double speedBoost, double eutDiscount, int expectedEUt) {
        // See https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues/17207
        // Using centrifuge recipe: Black Granite dust
        // in an Industrial Centrifuge under UMV power
        long machineVoltage = V[12];
        int maxParallel = 6 * GTUtility.getTier(machineVoltage);
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(10)
            .setEUt(machineVoltage)
            .setDuration(20 * 20)
            .setDurationModifier(speedBoost)
            .setEUtDiscount(eutDiscount)
            .setParallel(maxParallel);
        maxParallel = GTUtility.safeInt((long) (maxParallel * calculator.calculateMultiplierUnderOneTick()), 0);
        calculator.setCurrentParallel(maxParallel)
            .calculate();
        assertEquals(expectedEUt, calculator.getConsumption());
    }

    @Test
    void laserOverclockAppliesEscalatingPenalty() {
        // 30 EU/t, 2 regular OCs then 2 laser OCs at 4.3x and 4.6x
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(30)
            .setEUt(10_000)
            .setDuration(100)
            .setLaserOC(true)
            .setMaxRegularOverclocks(2)
            .calculate();
        assertEquals((int) (100 / GTUtility.powInt(2, 4)), calculator.getDuration(), messageDuration);
        assertEquals(
            (long) Math.ceil(30 * GTUtility.powInt(4, 2) * 4.3 * 4.6),
            calculator.getConsumption(),
            messageEUt);
        assertEquals(9495, calculator.getConsumption(), messageEUt);
    }

    @Test
    void maxRegularOverclocksShiftsMixTowardsLaserOverclocks() {
        OverclockCalculator laserOnly = new OverclockCalculator().setRecipeEUt(VP[4])
            .setEUt(V[9])
            .setDuration(100)
            .setLaserOC(true)
            .setMaxRegularOverclocks(0)
            .calculate();
        OverclockCalculator mixed = new OverclockCalculator().setRecipeEUt(VP[4])
            .setEUt(V[9])
            .setDuration(100)
            .setLaserOC(true)
            .setMaxRegularOverclocks(3)
            .calculate();

        // Same total overclocks, but laser ones cost strictly more EU
        assertEquals((int) (100 / GTUtility.powInt(2, 4)), laserOnly.getDuration(), messageDuration);
        assertEquals((int) (100 / GTUtility.powInt(2, 4)), mixed.getDuration(), messageDuration);
        assertEquals(967_670, laserOnly.getConsumption(), messageEUt);
        assertEquals(528_384, mixed.getConsumption(), messageEUt);
    }

    @Test
    void noLaserOverclockWithoutPowerHeadroom() {
        OverclockCalculator tooLittle = new OverclockCalculator().setRecipeEUt(1000)
            .setEUt(4299)
            .setDuration(100)
            .setLaserOC(true)
            .setMaxRegularOverclocks(0)
            .calculate();
        assertEquals(100, tooLittle.getDuration(), messageDuration);
        assertEquals(1000, tooLittle.getConsumption(), messageEUt);

        OverclockCalculator justEnough = new OverclockCalculator().setRecipeEUt(1000)
            .setEUt(4400)
            .setDuration(100)
            .setLaserOC(true)
            .setMaxRegularOverclocks(0)
            .calculate();
        assertEquals(50, justEnough.getDuration(), messageDuration);
        assertEquals(4300, justEnough.getConsumption(), messageEUt);
    }

    static Stream<Arguments> laserSubLvRecipeParameters() {
        return Stream.of(
            // The 32 EU/t floor applies to the tier ratio only, never to the consumption base
            Arguments.arguments(8, 131_072L),
            Arguments.arguments(16, 262_144L),
            Arguments.arguments(30, 491_520L));
    }

    @ParameterizedTest
    @MethodSource("laserSubLvRecipeParameters")
    void laserOverclockDoesNotOverchargeSubLvRecipes(int recipeEUt, long expectedEUt) {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(recipeEUt)
            .setEUt(V[8])
            .setDuration(100)
            .setLaserOC(true)
            .calculate();
        assertEquals(recipeEUt * GTUtility.powInt(4, 7), calculator.getConsumption(), messageEUt);
        assertEquals(expectedEUt, calculator.getConsumption(), messageEUt);
    }

    static Stream<Arguments> laserUnderOneTickParameters() {
        return Stream.of(
            Arguments.arguments(12.5, 328.0),
            Arguments.arguments(25.0, 164.0),
            Arguments.arguments(100.0, 41.0),
            Arguments.arguments(1.25, 3277.0));
    }

    @ParameterizedTest
    @MethodSource("laserUnderOneTickParameters")
    void laserOverclockUnderOneTickMatchesNonLaserClosedForm(double durationPerSlice, double expectedMultiplier) {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(30)
            .setEUt(V[9] * 1024)
            .setLaserOC(true)
            .setMaxRegularOverclocks(8)
            .setDurationUnderOneTickSupplier(() -> durationPerSlice);
        assertEquals(expectedMultiplier, calculator.calculateMultiplierUnderOneTick());
        assertEquals(
            Math.ceil(GTUtility.powInt(2, 12) / durationPerSlice),
            calculator.calculateMultiplierUnderOneTick());
    }

    static Stream<Arguments> laserSubTwoTickParameters() {
        return Stream.of(
            // neededOverclocks goes negative/zero here, which must not clamp the multiplier
            Arguments.arguments(0.5, 4096.0),
            Arguments.arguments(1.0, 2048.0),
            Arguments.arguments(1.25, 1639.0));
    }

    @ParameterizedTest
    @MethodSource("laserSubTwoTickParameters")
    void laserOverclockHandlesSubTwoTickDuration(double durationPerSlice, double expectedMultiplier) {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(30)
            .setEUt(V[9] * 256)
            .setLaserOC(true)
            .setMaxRegularOverclocks(8)
            .setDurationUnderOneTickSupplier(() -> durationPerSlice);
        assertEquals(expectedMultiplier, calculator.calculateMultiplierUnderOneTick());
        assertEquals(
            Math.ceil(GTUtility.powInt(2, 11) / durationPerSlice),
            calculator.calculateMultiplierUnderOneTick());
    }

    static Stream<Arguments> laserWithinBudgetParameters() {
        return Stream.of(
            Arguments.arguments(30L, V[9] * 1024, 8),
            Arguments.arguments(VP[4], V[9], 4),
            Arguments.arguments(1920L, V[10] * 4096, 6),
            Arguments.arguments(VP[1], V[12], 0),
            Arguments.arguments(500_000L, V[11] * 64, 12));
    }

    @ParameterizedTest
    @MethodSource("laserWithinBudgetParameters")
    void laserOverclockNeverExceedsMachinePower(long recipeEUt, long machineEUt, int maxRegularOverclocks) {
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(recipeEUt)
            .setEUt(machineEUt)
            .setDuration(100)
            .setLaserOC(true)
            .setMaxRegularOverclocks(maxRegularOverclocks)
            .calculate();
        assertTrue(
            calculator.getConsumption() <= machineEUt,
            "Consumption " + calculator.getConsumption() + " exceeds machine power " + machineEUt);
    }

    @Test
    void laserOverclockUnderOneTickMatchesAdvAssemblyLineUsage() {
        final int slices = 8;
        final int recipeDuration = 200;
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(30)
            .setDurationUnderOneTickSupplier(() -> (double) recipeDuration / slices)
            .setParallel(1)
            .setEUt(V[9] * 1024 / slices)
            .setLaserOC(true)
            .setMaxRegularOverclocks(8);

        int maxParallel = GTUtility.safeInt((long) calculator.calculateMultiplierUnderOneTick(), 0);
        assertEquals(82, maxParallel);

        calculator.setCurrentParallel(maxParallel)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(190_556_406L, calculator.getConsumption(), messageEUt);
        assertTrue(calculator.getConsumption() <= V[9] * 1024 / slices);
    }
}
