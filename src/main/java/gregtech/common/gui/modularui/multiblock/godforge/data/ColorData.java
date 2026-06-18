package gregtech.common.gui.modularui.multiblock.godforge.data;

import com.cleanroommc.modularui.utils.Color;

import tectech.thing.metaTileEntity.multi.godforge.color.StarColorSetting;

public class ColorData {

    private int r = (int) StarColors.RGB.RED.getDefaultValue();
    private int g = (int) StarColors.RGB.GREEN.getDefaultValue();
    private int b = (int) StarColors.RGB.BLUE.getDefaultValue();
    private float h = StarColors.HSV.HUE.getDefaultValue();
    private float s = StarColors.HSV.SATURATION.getDefaultValue();
    private float v = StarColors.HSV.VALUE.getDefaultValue();
    private float gamma = StarColors.Extra.GAMMA.getDefaultValue();

    private int color = Color.rgb(r, g, b);

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
        updateFromRGB();
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
        updateFromRGB();
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
        updateFromRGB();
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = Float.parseFloat(String.format("%.1f", h));
        updateFromHSV();
    }

    public float getS() {
        return s;
    }

    public void setS(float s) {
        this.s = Float.parseFloat(String.format("%.3f", s));
        updateFromHSV();
    }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = Float.parseFloat(String.format("%.3f", v));
        updateFromHSV();
    }

    public float getGamma() {
        return gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = Float.parseFloat(String.format("%.2f", gamma));
    }

    public int getColor() {
        return color;
    }

    private void updateFromRGB() {
        color = Color.rgb(r, g, b);
        h = Float.parseFloat(String.format("%.1f", Color.getHue(color)));
        s = Float.parseFloat(String.format("%.3f", Color.getHSVSaturation(color)));
        v = Float.parseFloat(String.format("%.3f", Color.getValue(color)));
    }

    private void updateFromHSV() {
        color = Color.ofHSV(h, s, v);
        r = Color.getRed(color);
        g = Color.getGreen(color);
        b = Color.getBlue(color);
    }

    public void updateFrom(int color) {
        this.color = color;
        r = Color.getRed(color);
        g = Color.getGreen(color);
        b = Color.getBlue(color);
        updateFromRGB();
    }

    public void updateFrom(StarColorSetting setting) {
        color = Color.rgb(setting.getColorR(), setting.getColorG(), setting.getColorB());
        r = Color.getRed(color);
        g = Color.getGreen(color);
        b = Color.getBlue(color);
        gamma = setting.getGamma();
        updateFromRGB();
    }

    public void reset() {
        r = (int) StarColors.RGB.RED.getDefaultValue();
        g = (int) StarColors.RGB.GREEN.getDefaultValue();
        b = (int) StarColors.RGB.BLUE.getDefaultValue();
        h = StarColors.HSV.HUE.getDefaultValue();
        s = StarColors.HSV.SATURATION.getDefaultValue();
        v = StarColors.HSV.VALUE.getDefaultValue();
        gamma = StarColors.Extra.GAMMA.getDefaultValue();
        color = Color.rgb(r, g, b);
    }

    public StarColorSetting createSetting() {
        return new StarColorSetting(r, g, b, gamma);
    }

    public void decode(String colorStr) {
        if (colorStr == null) return;
        try {
            updateFrom((int) (long) Long.decode(colorStr));
        } catch (NumberFormatException ignored) {}
    }

    public String getHexString() {
        String hexString = Color.toFullHexString(r, g, b);
        return "#" + StarColors.RGB.RED.getColor()
            + hexString.substring(0, 2)
            + StarColors.RGB.GREEN.getColor()
            + hexString.substring(2, 4)
            + StarColors.RGB.BLUE.getColor()
            + hexString.substring(4, 6);
    }
}
