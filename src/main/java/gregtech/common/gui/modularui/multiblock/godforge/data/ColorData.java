package gregtech.common.gui.modularui.multiblock.godforge.data;

import com.cleanroommc.modularui.utils.Color;

public class ColorData {

    private int r = (int) StarColors.RGB.RED.getDefaultValue();
    private int g = (int) StarColors.RGB.GREEN.getDefaultValue();
    private int b = (int) StarColors.RGB.BLUE.getDefaultValue();
    private int h = (int) StarColors.HSV.HUE.getDefaultValue();
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

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
        updateFromHSV();
    }

    public float getS() {
        return s;
    }

    public void setS(float s) {
        this.s = s;
        updateFromHSV();
    }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
        updateFromHSV();
    }

    public float getGamma() {
        return gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public int getColor() {
        return color;
    }

    private void updateFromRGB() {
        color = Color.rgb(r, g, b);
        h = (int) Color.getHue(color);
        s = Color.getHSVSaturation(color);
        v = Color.getValue(color);
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
        h = (int) Color.getHue(color);
        s = Color.getHSVSaturation(color);
        v = Color.getValue(color);
    }
}
