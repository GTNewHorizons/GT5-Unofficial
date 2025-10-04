package gtneioreplugin.util;

import static gtneioreplugin.util.DimensionHelper.DimNameDisplayed;

import java.util.Set;

@SuppressWarnings("unused")
public class SmallOre implements Comparable<SmallOre> {

    private String oreName;
    private String oreMaterial;
    private int amount;
    private String height = "";
    private static final int sizeData = 4; // hors dims

    private Set<String> dimensions;

    public void setDims(Set<String> dims) {
        this.dimensions = dims;
    }

    public String getOreName() {
        return this.oreName;
    }

    public void setOreName(String s) {
        this.oreName = s;
    }

    public String getOreMaterial() {
        return this.oreMaterial;
    }

    public void setOreMaterial(String mat) {
        this.oreMaterial = mat;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String s) {
        this.height = s;
    }

    public void setAmount(int i) {
        this.amount = i;
    }

    public int getAmount() {
        return this.amount;
    }

    public static String getCsvHeader() {
        String[] headers = new String[sizeData + DimNameDisplayed.length];
        headers[0] = "Ore Name";
        headers[1] = "Block Meta";
        headers[2] = "Height";
        headers[3] = "Amount Per Chunk";
        for (int i = 0; i < DimNameDisplayed.length; i++) {
            headers[sizeData + i] = DimensionHelper.getFullName(DimNameDisplayed[i]);
        }
        return String.join(",", headers);
    }

    public String getCsvEntry() {
        String[] values = new String[sizeData + DimNameDisplayed.length];
        values[0] = oreName;
        values[1] = oreMaterial;
        values[2] = getHeight();
        values[3] = Integer.toString(amount);
        for (int i = 0; i < DimNameDisplayed.length; i++) {
            values[sizeData + i] = Boolean.toString(dimensions.contains(DimNameDisplayed[i]));
        }
        return String.join(",", values);
    }

    @Override
    public int compareTo(SmallOre other) {
        return oreName.compareTo(other.oreName);
    }
}
