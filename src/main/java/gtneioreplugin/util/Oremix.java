package gtneioreplugin.util;

import java.util.Map;

public class Oremix implements Comparable<Oremix> {

    private String oreMixName;

    private String primary = "";

    private String secondary = "";

    private String inbetween = "";

    private String sporadic = "";

    private String oreMixIDs = "";

    private String height = "";

    private int density;

    private int size;

    private int weight;

    private static final int sizeData = 10; // hors dims

    private Map<String, Boolean> dimensions;

    public void setDims(Map<String, Boolean> dims) {
        this.dimensions = dims;
    }

    public String getOreMixName() {
        return this.oreMixName;
    }

    public void setOreMixName(String s) {
        this.oreMixName = s;
    }

    public void setPrimary(String s) {
        this.primary = s;
    }

    public void setSecondary(String s) {
        this.secondary = s;
    }

    public void setInbetween(String s) {
        this.inbetween = s;
    }

    public void setSporadic(String s) {
        this.sporadic = s;
    }

    public void setOreMixIDs(String s) {
        this.oreMixIDs = s;
    }

    public void setHeight(String s) {
        this.height = s;
    }

    public void setDensity(int i) {
        this.density = i;
    }

    public void setSize(int i) {
        this.size = i;
    }

    public void setWeight(int i) {
        this.weight = i;
    }

    @Override
    public int compareTo(Oremix ore) {
        return ore.oreMixName.compareTo(ore.oreMixName);
    }

    public static String getCsvHeader() {
        String[] headers = new String[sizeData + DimensionHelper.DimNameDisplayed.length];
        headers[0] = "Ore Mix Name";
        headers[1] = "Primary Ore";
        headers[2] = "Secondary Ore";
        headers[3] = "InBetween Ore";
        headers[4] = "Sporadic Ore";
        headers[5] = "Ore Meta IDs";
        headers[6] = "Height";
        headers[7] = "Density";
        headers[8] = "Size";
        headers[9] = "Weight";
        for (int i = 0; i < DimensionHelper.DimNameDisplayed.length; i++) {
            headers[sizeData + i] = DimensionHelper.getFullName(DimensionHelper.DimNameDisplayed[i]);
        }
        return String.join(",", headers);
    }

    public String getCsvEntry() {
        String[] values = new String[sizeData + DimensionHelper.DimNameDisplayed.length];
        values[0] = oreMixName;
        values[1] = primary;
        values[2] = secondary;
        values[3] = inbetween;
        values[4] = sporadic;
        values[5] = oreMixIDs;
        values[6] = height;
        values[7] = Integer.toString(density);
        values[8] = Integer.toString(size);
        values[9] = Integer.toString(weight);
        for (int i = 0; i < DimensionHelper.DimNameDisplayed.length; i++) {
            values[sizeData + i] = Boolean
                .toString(dimensions.getOrDefault(DimensionHelper.DimNameDisplayed[i], false));
        }
        return String.join(",", values);
    }
}
