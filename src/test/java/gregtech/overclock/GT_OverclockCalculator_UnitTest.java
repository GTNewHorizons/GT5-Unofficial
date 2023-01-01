package gregtech.overclock;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import gregtech.api.util.GT_OverclockCalculator;
import org.junit.jupiter.api.Test;

class GT_OverclockCalculator_UnitTest {

    private static final String messageDuration = "Duration Calculated Wrong";
    private static final String messageEUt = "EUt Calculated Wrong";

    @Test
    void fullPerfectOverclockEBF_Test() {
        int heatDiscounts = (1800 * 4) / 900;
        long correctConsumption = (long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[5])
                .setDuration(1024)
                .enableHeatOC()
                .setRecipeHeat(1800)
                .setMultiHeat(1800 * 5)
                .calculate();
        try {
            assertEquals(1024 >> 8, calculator.getDuration(), messageDuration);
            assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void imperfectOverclockEBFWithOneHeatDiscount_Test() {
        int heatDiscounts = 1;
        long correctConsumption = (long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[5])
                .setDuration(1024)
                .enableHeatOC()
                .setRecipeHeat(1800)
                .setMultiHeat(2700)
                .calculate();
        try {
            assertEquals(1024 >> 4, calculator.getDuration(), messageDuration);
            assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void imperfectOverclockEBFWithoutHeatDiscounts_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[5])
                .setDuration(1024)
                .enableHeatOC()
                .setRecipeHeat(1800)
                .setMultiHeat(1800)
                .calculate();
        try {
            assertEquals(1024 >> 4, calculator.getDuration(), messageDuration);
            assertEquals(VP[5], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void perfectAndImperfectOverclockEBFWithTwoHeatDiscounts_Test() {
        int heatDiscounts = 2;
        long correctConsumption = (long) Math.ceil(VP[5] * Math.pow(0.95, heatDiscounts));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[5])
                .setDuration(1024)
                .enableHeatOC()
                .setRecipeHeat(1800)
                .setMultiHeat(3600)
                .calculate();
        try {
            assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
            assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void generalImperfectOverclock_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setDuration(1024)
                .calculate();
        try {
            assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
            assertEquals(VP[6], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void generalPerfectOverclock_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setDuration(2048)
                .enablePerfectOC()
                .calculate();
        try {
            assertEquals(2048 >> 10, calculator.getDuration(), messageDuration);
            assertEquals(VP[6], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void weirdHeatEBF_Test() {
        int heatDiscounts = 3;
        long correctConsumption = (long) Math.ceil(VP[6] * Math.pow(0.95, heatDiscounts));
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setDuration(2048)
                .enableHeatOC()
                .setRecipeHeat(486)
                .setMultiHeat(3900)
                .calculate();
        try {
            assertEquals(2048 >> 6, calculator.getDuration(), messageDuration);
            assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void doubleEnergyHatchOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6] + V[6])
                .setAmperage(2)
                .setDuration(1024)
                .calculate();
        try {
            assertEquals(1024 >> 6, calculator.getDuration(), messageDuration);
            assertEquals(VP[7], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void multiAmpHatchOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setAmperage(256)
                .setDuration(1024)
                .calculate();
        try {
            assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
            assertEquals(VP[10], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void weirdAmpHatchOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setAmperage(320)
                .setDuration(1024)
                .calculate();
        try {
            assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
            assertEquals(VP[10], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void parallelImperfectOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[8])
                .setAmperage(4)
                .setDuration(1024)
                .setParallel(16)
                .calculate();
        try {
            assertEquals(1024 >> 6, calculator.getDuration(), messageDuration);
            assertEquals(VP[9], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void parallelPerfectOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[8])
                .setAmperage(4)
                .setDuration(4096)
                .setParallel(16)
                .enablePerfectOC()
                .calculate();
        try {
            assertEquals(4096 >> 12, calculator.getDuration(), messageDuration);
            assertEquals(VP[9], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void parallelMultiAmpOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setAmperage(320)
                .setDuration(1024)
                .setParallel(16)
                .calculate();
        try {
            assertEquals(1024 >> 7, calculator.getDuration(), messageDuration);
            assertEquals(VP[10], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void weirdParallelOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[8])
                .setDuration(1024)
                .setParallel(8)
                .calculate();
        try {
            assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
            assertEquals(VP[6] * 8, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void weirdParallelAndAmpsOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[8])
                .setAmperage(320)
                .setDuration(1024)
                .setParallel(8)
                .calculate();
        try {
            assertEquals(1024 >> 9, calculator.getDuration(), messageDuration);
            assertEquals(VP[6] * 8 * 256, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void weirdTimeOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setDuration(77)
                .calculate();
        try {
            assertEquals(77 >> 5, calculator.getDuration(), messageDuration);
            assertEquals(VP[6], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void stopsCorrectlyWhenOneTicking_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setDuration(1)
                .calculate();
        try {
            assertEquals(1, calculator.getDuration(), messageDuration);
            assertEquals(VP[1], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void imperfectOCWithEUtDiscount_Test() {
        long correctConsumption = (long) Math.ceil(VP[1] / 1.1f) << 10;
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setEUtDiscount(1.1f)
                .setDuration(1024)
                .calculate();
        try {
            assertEquals(1024 >> 5, calculator.getDuration(), messageDuration);
            assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void perfectOCWithEUtDiscount_Test() {
        long correctConsumption = (long) Math.ceil(VP[1] / 1.1f) << 10;
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setEUtDiscount(1.1f)
                .setDuration(1024)
                .enablePerfectOC()
                .calculate();

        try {
            assertEquals(1024 >> 10, calculator.getDuration(), messageDuration);
            assertEquals(correctConsumption, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void imperfectOCWithSpeedBoost_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setSpeedBoost(1.1f)
                .setDuration(1024)
                .calculate();
        try {
            assertEquals((int) (1024 / 1.1f) >> 5, calculator.getDuration(), messageDuration);
            assertEquals(VP[6], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void perfectOCWithSpeedBoost_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setSpeedBoost(1.1f)
                .setDuration(2048)
                .enablePerfectOC()
                .calculate();
        try {
            assertEquals((int) (2048 / 1.1f) >> 10, calculator.getDuration(), messageDuration);
            assertEquals(VP[6], calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void oneTickDiscountTurnsToOne_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setSpeedBoost(1.1f)
                .setDuration(1)
                .enableOneTickDiscount()
                .calculate();
        try {
            assertEquals(1, calculator.getDuration(), messageDuration);
            assertEquals(1, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void oneTickDiscountImperfectOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setSpeedBoost(1.1f)
                .setDuration(4)
                .enableOneTickDiscount()
                .calculate();
        try {
            assertEquals(1, calculator.getDuration(), messageDuration);
            assertEquals(480 >> 3, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void oneTickDiscountPerfectOC_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setSpeedBoost(1.1f)
                .setDuration(16)
                .enablePerfectOC()
                .enableOneTickDiscount()
                .calculate();
        try {
            assertEquals(1, calculator.getDuration(), messageDuration);
            assertEquals(480 >> 6, calculator.getConsumption(), messageEUt);
        } catch (Exception e) {
            assert fail("There was an exception") != null;
        }
    }

    @Test
    void correctExceptionsWhenNotCalculating_Test() {
        GT_OverclockCalculator calculator = new GT_OverclockCalculator()
                .setRecipeEUt(VP[1])
                .setEUt(V[6])
                .setSpeedBoost(1.1f)
                .setDuration(16);
        try {
            calculator.getDuration();
            assert fail("There wasn't exception") != null;
        } catch (Exception e) {
            assertEquals("Trtying to get duration before calculating!", e.getMessage());
        }
        try {
            calculator.getConsumption();
            assert fail("There wasn't exception") != null;
        } catch (Exception e) {
            assertEquals("Trtying to get consumption before calculating!", e.getMessage());
        }
    }
}
