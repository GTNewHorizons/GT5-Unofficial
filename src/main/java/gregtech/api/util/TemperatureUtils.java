package gregtech.api.util;

import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.api.util.GTUtility.safeInt;

import gregtech.GTMod;

public class TemperatureUtils {

    /**
     *
     * @param temp Temperature in Kelvin as an int
     * @return String in the format "T X" where T is an integer and X is an abbreviation for the currently configured
     *         unit. Will clamp values to max/min int if there is an over/underflow in the conversion.
     */
    public static String getTemperatureAsCurrentUnit(int temp) {
        return switch (GTMod.gregtechproxy.tooltipTemperatureUnits) {
            case Celsius -> formatNumbers(safeInt(temp - 273, 0)) + " C";
            case Fahrenheit -> formatNumbers(safeInt((long) (((double) temp / ((double) 5 / 9)) - 459.67), 0)) + " F";
            case Rankine -> formatNumbers(safeInt((long) ((double) temp * ((double) 9 / 5)), 0)) + " R";
            case Delisle -> formatNumbers(safeInt((long) ((373.15 - (double) temp) * ((double) 3 / 2)), 0)) + " D";
            case Newton -> formatNumbers(safeInt((long) (((double) temp - 273.15) * 0.33), 0)) + " N";
            case Reaumur -> formatNumbers(safeInt((long) (((double) temp - 273.15) * 0.8), 0)) + " Ré";
            case Romer -> formatNumbers(safeInt((long) ((((double) temp - 273.15) * ((double) 21 / 40)) + 7.5), 0))
                + " Rø";
            default -> formatNumbers(temp) + " K";
        };
    }

    /**
     *
     * @param temp Temperature in Kelvin as a long
     * @return String in the format "T X" where T is an integer and X is an abbreviation for the currently configured
     *         unit.
     */
    public static String getTemperatureAsCurrentUnit(long temp) {
        return switch (GTMod.gregtechproxy.tooltipTemperatureUnits) {
            case Celsius -> formatNumbers(temp - 273) + " C";
            case Fahrenheit -> formatNumbers((long) (((double) temp / ((double) 5 / 9)) - 459.67)) + " F";
            case Rankine -> formatNumbers((long) ((double) temp * ((double) 9 / 5))) + " R";
            case Delisle -> formatNumbers((long) ((373.15 - (double) temp) * ((double) 3 / 2))) + " D";
            case Newton -> formatNumbers((long) (((double) temp - 273.15) * 0.33)) + " N";
            case Reaumur -> formatNumbers((long) (((double) temp - 273.15) * 0.8)) + " Ré";
            case Romer -> formatNumbers((long) ((((double) temp - 273.15) * ((double) 21 / 40)) + 7.5)) + " Rø";
            default -> formatNumbers(temp) + " K";
        };
    }

    /**
     *
     * @param increase Temperature increase in Kelvin as long
     * @return String in the format "+T X" where T is an integer and X is an abbreviation for the currently configured
     *         unit. The + sign will be automatically converted to a - sign if T is negative.
     */
    public static String convertKelvinIncreaseToCurrentUnit(long increase) {
        String temp = switch (GTMod.gregtechproxy.tooltipTemperatureUnits) {
            case Celsius -> formatNumbers(increase) + " C";
            case Fahrenheit -> formatNumbers((double) increase * 1.8) + " F";
            case Rankine -> formatNumbers((double) increase * 1.8) + " R";
            case Delisle -> formatNumbers((double) increase * -1.5) + " D";
            case Newton -> formatNumbers((double) increase * 0.33) + " N";
            case Reaumur -> formatNumbers((double) increase * 0.8) + " Ré";
            case Romer -> formatNumbers((double) increase * 0.525) + " Rø";
            default -> GTUtility.formatNumbers(increase) + " K";
        };
        if (temp.toCharArray()[0] == '-') {
            return temp;
        } else return "+" + temp;
    }

    /**
     *
     * @return The name of the currently configured temperature measurement
     */
    public static String getCurrentTemperatureUnitName() {
        return switch (GTMod.gregtechproxy.tooltipTemperatureUnits) {
            case Celsius -> "Celsius";
            case Fahrenheit -> "Fahrenheit";
            case Rankine -> "Rankine";
            case Delisle -> "Delisle";
            case Newton -> "Newton";
            case Reaumur -> "Réaumur";
            case Romer -> "Rømer";
            default -> "Kelvin";
        };
    }
}
