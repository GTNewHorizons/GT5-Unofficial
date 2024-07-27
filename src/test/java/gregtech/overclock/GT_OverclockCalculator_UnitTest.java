package gregtech.overclock;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VP;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OverclockCalculator;

class GT_OverclockCalculator_UnitTest {

    private static final String messageDuration = "Duration Calculated Wrong";
    private static final String messageEUt = "EUt Calculated Wrong";

    @Test
    void fullPerfectOverclockEBF_Test() {
        int heatDiscounts = (1800 * 4) / 900;
        long correctConsumption = (long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[5])
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(1800 * 5)
            .calculate();
        assertEquals(1024 >> 8, calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOverclockEBFWithOneHeatDiscount_Test() {
        int heatDiscounts = 1;
        long correctConsumption = (long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[5])
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(2700)
            .calculate();
        assertEquals(1024 >> 4, calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOverclockEBFWithoutHeatDiscounts_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[5])
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(1800)
            .calculate();
        assertEquals(1024 >> 4, calculator.getDuration(), messageDuration);
        assertEquals(VP[5], calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectAndImperfectOverclockEBFWithTwoHeatDiscounts_Test() {
        int heatDiscounts = 2;
        long correctConsumption = (long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[5])
            .setDuration(1024)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(1800)
            .setMachineHeat(3600)
            .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void generalImperfectOverclock_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1024)
            .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void generalPerfectOverclock_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(2048)
            .enablePerfectOC()
            .calculate();
        assertEquals(2048 >> 10, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdHeatEBF_Test() {
        int heatDiscounts = 3;
        long correctConsumption = (long) Math.ceil(VP[6] * Math.pow(0.95, heatDiscounts));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(2048)
            .setHeatOC(true)
            .setHeatDiscount(true)
            .setRecipeHeat(486)
            .setMachineHeat(3900)
            .calculate();
        assertEquals(2048 >> 6, calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void doubleEnergyHatchOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setAmperage(4)
            .setDuration(1024)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 >> 6, calculator.getDuration(), messageDuration);
        assertEquals(VP[7], calculator.getConsumption(), messageEUt);
    }

    @Test
    void doubleEnergyHatchOCForULV_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[0])
            .setEUt(V[6])
            .setAmperage(4)
            .setDuration(1024)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 >> 6, calculator.getDuration(), messageDuration);
        assertEquals(VP[0] << 12, calculator.getConsumption(), messageEUt);
    }

    @Test
    void multiAmpHatchOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setAmperage(256)
            .setDuration(1024)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdAmpHatchOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setAmperage(320)
            .setDuration(1024)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelImperfectOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[8])
            .setAmperage(4)
            .setDuration(1024)
            .setParallel(16)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 >> 6, calculator.getDuration(), messageDuration);
        assertEquals(VP[9], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelPerfectOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[8])
            .setAmperage(4)
            .setDuration(4096)
            .setParallel(16)
            .setAmperageOC(true)
            .enablePerfectOC()
            .calculate();
        assertEquals(4096 >> 12, calculator.getDuration(), messageDuration);
        assertEquals(VP[9], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelMultiAmpOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setAmperage(320)
            .setDuration(1024)
            .setParallel(16)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 >> 7, calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdParallelOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[8])
            .setDuration(1024)
            .setParallel(8)
            .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(VP[6] * 8, calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdParallelAndAmpsOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[8])
            .setAmperage(320)
            .setDuration(1024)
            .setParallel(8)
            .setAmperageOC(true)
            .calculate();
        assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
        assertEquals(VP[6] * 8 * 256, calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdTimeOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(77)
            .calculate();
        assertEquals(77 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void stopsCorrectlyWhenOneTicking_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(VP[1], calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOCWithEUtDiscount_Test() {
        long correctConsumption = (long) Math.ceil(VP[1] * 0.9f) << 10;
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setEUtDiscount(0.9f)
            .setDuration(1024)
            .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectOCWithEUtDiscount_Test() {
        long correctConsumption = (long) Math.ceil(VP[1] * 0.9f) << 10;
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setEUtDiscount(0.9f)
            .setDuration(1024)
            .enablePerfectOC()
            .calculate();
        assertEquals(1024 >> 10, calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOCWithSpeedBoost_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setSpeedBoost(0.9f)
            .setDuration(1024)
            .calculate();
        assertEquals((int) (1024 * 0.9f) >> 5, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectOCWithSpeedBoost_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setSpeedBoost(0.9f)
            .setDuration(2048)
            .enablePerfectOC()
            .calculate();
        assertEquals((int) (2048 * 0.9f) >> 10, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectOC3TicksTo1Tick_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[4])
            .setEUt(V[5])
            .setDuration(3)
            .enablePerfectOC()
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(VP[5], calculator.getConsumption(), messageEUt);
    }

    @Test
    void oneTickDiscountTurnsToOne_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1)
            .setOneTickDiscount(true)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(1, calculator.getConsumption(), messageEUt);
    }

    @Test
    void oneTickDiscountImperfectOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setSpeedBoost(1.1f)
            .setDuration(4)
            .setOneTickDiscount(true)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);

        /*
         * duration with speedboost = 5
         * log_2(5) ~ 2.3;
         * round up to 3 to reach one tick duration
         */
        int overclocksTillOneTick = 3;
        int overclocksBeyondOneTick = 2;

        // 3 overclocks, each gives 4x consumption growth per tick (1920)
        long targetEUt = VP[1] << 2 * overclocksTillOneTick;
        // 2 remaining overclocks are beyond 1 tick, each provides 2x comsumption discount (480)
        targetEUt >>= overclocksBeyondOneTick;

        assertEquals(targetEUt, calculator.getConsumption(), messageEUt);
    }

    @Test
    void oneTickDiscountPerfectOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setSpeedBoost(1.1f)
            .setDuration(16)
            .enablePerfectOC()
            .setOneTickDiscount(true)
            .calculate();

        /*
         * duration with speedboost = 18
         * log_4(18) ~ 2.08;
         * round up to 3 to reach one tick duration
         */
        int overclocksTillOneTick = 3;
        int overclocksBeyondOneTick = 2;

        // 3 overclocks, each gives 4x consumption growth per tick (1920)
        long targetEUt = VP[1] << 2 * overclocksTillOneTick;
        // 2 remaining overclocks are beyond 1 tick, each provides 4x comsumption discount (120)
        targetEUt >>= 2 * overclocksBeyondOneTick;

        assertEquals(targetEUt, calculator.getConsumption(), messageEUt);
        assertEquals(1, calculator.getDuration(), messageDuration);
    }

    @Test
    void ulvRecipeWithDiscount_Test() {
        long correctConsumption = (long) Math.ceil((VP[0] << 10) * 0.9f);
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[0])
            .setEUt(V[6])
            .setEUtDiscount(0.9f)
            .setDuration(1024)
            .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeWithDiscountWithParallel_Test() {
        long correctConsumption = (long) Math.ceil((VP[0] << 6) * 14 * 0.9f);
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[0])
            .setEUt(V[5])
            .setEUtDiscount(0.9f)
            .setParallel(14)
            .setDuration(1024)
            .calculate();
        assertEquals(1024 >> 3, calculator.getDuration(), messageDuration);
        assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeStopsWhenOneTicked_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[0])
            .setEUt(V[6])
            .setDuration(1)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(VP[0], calculator.getConsumption(), messageEUt);
    }

    @Test
    void testNotPowerOverflowing_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(16)
            .setParallel(64)
            .setAmperage(64)
            .setAmperageOC(true)
            .setEUt(V[5])
            .setDuration(30)
            .calculate();
        assertEquals(1, calculator.getDuration(), messageDuration);
        assertEquals(16 << 14, calculator.getConsumption(), messageEUt);
    }

    @Test
    void testCorrectEUtWhenOverclockingUnderOneTick_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(24)
            .setParallel(56)
            .setAmperage(1)
            .setAmperageOC(true)
            .setSpeedBoost(1f / 6f)
            .setEUt(V[14])
            .setDuration(56);
        assertEquals((24 * 56) << 20, calculator.calculateEUtConsumptionUnderOneTick(56, 6144));
    }

    @Test
    void testCorrectEUtWhenOverclockingUnderOneTickWithHeat_Test() {
        double heatDiscount = Math.pow(0.95, (15500 - 2000) / 900);
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(1920)
            .setParallel(256)
            .setAmperage(1)
            .setAmperageOC(true)
            .setHeatDiscount(true)
            .setHeatOC(true)
            .setRecipeHeat(2000)
            .setMachineHeat(15500)
            .setEUt(V[12] * 1_048_576)
            .setDuration(250);
        assertEquals(
            Math.ceil((((long) 1920 * 256) << 28) * heatDiscount),
            calculator
                .calculateEUtConsumptionUnderOneTick(256, (int) (256 / calculator.calculateDurationUnderOneTick())));
    }

    @Test
    void testNoOverclockCorrectWithUnderOneTickLogic_Test() {
        GT_OverclockCalculator calculator = GT_OverclockCalculator.ofNoOverclock(2_693_264_510L, 100)
            .setParallel(24 * 64);
        assertEquals(100, calculator.calculateDurationUnderOneTick());
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOC() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(2)
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
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(2)
            .setEUt(V[3])
            .setParallel(64)
            .setDuration(20)
            .setAmperageOC(false)
            .setAmperage(16)
            .calculate();
        assertEquals((32 * 64), calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion3() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(2)
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
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(8)
            .setEUt(V[3])
            .setParallel(16)
            .setDuration(20)
            .setAmperageOC(false)
            .setAmperage(16)
            .calculate();
        assertEquals((8 << 4) * 16, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion5() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(8)
            .setEUt(V[3])
            .setParallel(16)
            .setDuration(20)
            .setAmperageOC(false)
            .calculate();
        assertEquals((8 << 2) * 16, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion6() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(7)
            .setEUt(V[3])
            .setParallel(64)
            .setDuration(160)
            .setAmperage(64)
            .setAmperageOC(false)
            .calculate();
        assertEquals((7 << 4) * 64, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion7() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(7)
            .setEUt(V[3])
            .setParallel(19)
            .setDuration(160)
            .setAmperage(19)
            .setAmperageOC(false)
            .calculate();
        assertEquals((7 << 4) * 19, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeDoesntOverclockExtraWithAmperageWithoutAmperageOCVersion8() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(7)
            .setEUt(V[3])
            .setParallel(29)
            .setDuration(160)
            .setAmperage(25)
            .setAmperageOC(false)
            .calculate();
        assertEquals((7 << 4) * 29, calculator.getConsumption(), messageEUt);
    }

    @Test
    void ulvRecipeWorkCorrectlyWithCalculatingEutUnderOneTick() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(2)
            .setEUt(TierEU.UV)
            .setParallel(64)
            .setDuration(300)
            .setAmperage(64)
            .setAmperageOC(false);
        assertEquals(TierEU.LuV * 64, calculator.calculateEUtConsumptionUnderOneTick(64, 64), messageEUt);
    }

    @Test
    void overclockWithAbnormalEnergyIncrease_Test() {
        long expectedEUt = (long) Math.floor(VP[1] * Math.pow(4.1, 5));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1024)
            .setEUtIncreasePerOC(4.1)
            .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(expectedEUt, calculator.getConsumption(), messageEUt);
    }

    @Test
    void overclockWithAbnormalDurationDecrease_Test() {
        int expectedDuration = (int) Math.floor(1024 / Math.pow(2.1, 5));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(VP[1])
            .setEUt(V[6])
            .setDuration(1024)
            .setDurationDecreasePerOC(2.1)
            .calculate();
        assertEquals(expectedDuration, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void slightlyOverOneAmpRecipeWorksWithSingleEnergyHatch() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(614400)
            .setEUt(TierEU.UV)
            .setDuration(600)
            .setAmperage(2)
            .setAmperageOC(false)
            .calculate();
        assertEquals(600, calculator.getDuration(), messageDuration);
        assertEquals(614400, calculator.getConsumption(), messageEUt);
    }
}
