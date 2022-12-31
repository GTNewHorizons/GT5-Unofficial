package gregtech.overclock;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VP;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gregtech.api.util.GT_OverclockCalculator;
import org.junit.jupiter.api.Test;

class GT_OverclockCalculator_UnitTest {

    private static final String messageDuration = "Duration Calculated Wrong";
    private static final String messageEUt = "EUt Calculated Wrong";

    @Test
    void fullPerfectOverclockEBFTest() {
        int heatDiscounts = (1800 * 4) / 900;
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[5])
                .setDuration(1024)
                .enableHeatOC()
                .setRecipeHeat(1800)
                .setMultiHeat(1800 * 5)
                .calculate();
        assertEquals(1024 >> 8, calculator.getDuration(), messageDuration);
        assertEquals((long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts)), calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOverclockEBFWithOneHeatDiscountTest() {
        int heatDiscounts = 1;
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[5])
                .setDuration(1024)
                .enableHeatOC()
                .setRecipeHeat(1800)
                .setMultiHeat(2700)
                .calculate();
        assertEquals(1024 >> 4, calculator.getDuration(), messageDuration);
        assertEquals((long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts)), calculator.getConsumption(), messageEUt);
    }

    @Test
    void imperfectOverclockEBFWithoutHeatDiscountsTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[5])
                .setDuration(1024)
                .enableHeatOC()
                .setRecipeHeat(1800)
                .setMultiHeat(1800)
                .calculate();
        assertEquals(1024 >> 4, calculator.getDuration(), messageDuration);
        assertEquals(VP[5], calculator.getConsumption(), messageEUt);
    }

    @Test
    void perfectAndImperfectOverclockEBFWithTwoHeatDiscountsTest() {
        int heatDiscounts = 2;
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[5])
                .setDuration(1024)
                .enableHeatOC()
                .setRecipeHeat(1800)
                .setMultiHeat(3600)
                .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals((long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts)), calculator.getConsumption(), messageEUt);
    }

    @Test
    void generalImperfectOverclockTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setDuration(1024)
                .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void generalPerfectOverclockTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setDuration(2048)
                .enablePerfectOC()
                .calculate();
        assertEquals(2048 >> 10, calculator.getDuration(), messageDuration);
        assertEquals(VP[6], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdHeatEBFTest() {
        int heatDiscounts = 3;
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setDuration(2048)
                .enableHeatOC()
                .setRecipeHeat(486)
                .setMultiHeat(3900)
                .calculate();
        assertEquals(2048 >> 6, calculator.getDuration(), messageDuration);
        assertEquals((long) Math.ceil(VP[6] * Math.pow(0.95, heatDiscounts)), calculator.getConsumption(), messageEUt);
    }

    @Test
    void doubleEnergyHatchOCTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6] + V[6])
                .setAmperage(2)
                .setDuration(1024)
                .calculate();
        assertEquals(1024 >> 6, calculator.getDuration(), messageDuration);
        assertEquals(VP[7], calculator.getConsumption(), messageEUt);
    }

    @Test
    void multiAmpHatchOCTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setAmperage(256)
                .setDuration(1024)
                .calculate();
        assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdAmpHatchOCTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setAmperage(320)
                .setDuration(1024)
                .calculate();
        assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelImperfectOCTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[8])
                .setAmperage(4)
                .setDuration(1024)
                .setParallel(16)
                .calculate();
        assertEquals(1024 >> 6, calculator.getDuration(), messageDuration);
        assertEquals(VP[9], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelPerfectOCTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[8])
                .setAmperage(4)
                .setDuration(4096)
                .setParallel(16)
                .enablePerfectOC()
                .calculate();
        assertEquals(4096 >> 12, calculator.getDuration(), messageDuration);
        assertEquals(VP[9], calculator.getConsumption(), messageEUt);
    }

    @Test
    void parallelMultiAmpOCTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setAmperage(320)
                .setDuration(1024)
                .setParallel(16)
                .calculate();
        assertEquals(1024 >> 7, calculator.getDuration(), messageDuration);
        assertEquals(VP[10], calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdParallelOCTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[8])
                .setDuration(1024)
                .setParallel(8)
                .calculate();
        assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
        assertEquals(VP[6] * 8, calculator.getConsumption(), messageEUt);
    }

    @Test
    void weirdParallelAndAmpsOCTest() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[8])
                .setAmperage(320)
                .setDuration(1024)
                .setParallel(8)
                .calculate();
        assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
        assertEquals(VP[6] * 8 * 256, calculator.getConsumption(), messageEUt);
    }
}
