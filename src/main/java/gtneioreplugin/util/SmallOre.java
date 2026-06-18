package gtneioreplugin.util;

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
        int dimCount = DimensionHelper.getAllDim()
            .size();
        String[] headers = new String[sizeData + dimCount];

        headers[0] = "Ore Name";
        headers[1] = "Block Meta";
        headers[2] = "Height";
        headers[3] = "Amount Per Chunk";

        int i = 0;
        for (DimensionHelper.Dimension record : DimensionHelper.getAllDim()) {
            headers[sizeData + i] = DimensionHelper.getFullName(record.abbr());
            i++;
        }

        return String.join(",", headers);
    }

    public String getCsvEntry() {
        int dimCount = DimensionHelper.getAllDim()
            .size();
        String[] values = new String[sizeData + dimCount];

        values[0] = oreName;
        values[1] = oreMaterial;
        values[2] = getHeight();
        values[3] = Integer.toString(amount);

        int i = 0;
        for (DimensionHelper.Dimension record : DimensionHelper.getAllDim()) {
            values[sizeData + i] = Boolean.toString(dimensions.contains(record.abbr()));
            i++;
        }

        return String.join(",", values);
    }

    @Override
    public int compareTo(SmallOre other) {
        return oreName.compareTo(other.oreName);
    }
}
