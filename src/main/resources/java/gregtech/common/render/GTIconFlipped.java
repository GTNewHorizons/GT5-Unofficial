package gregtech.common.render;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GTIconFlipped implements IIcon {

    private final IIcon baseIcon;
    private final boolean flipU;
    private final boolean flipV;

    public GTIconFlipped(IIcon baseIcon, boolean flipU, boolean flipV) {
        this.baseIcon = baseIcon;
        this.flipU = flipU;
        this.flipV = flipV;
    }

    /**
     * Returns the width of the icon, in pixels.
     */
    @Override
    public int getIconWidth() {
        return getBaseIcon().getIconWidth();
    }

    /**
     * Returns the height of the icon, in pixels.
     */
    @Override
    public int getIconHeight() {
        return getBaseIcon().getIconHeight();
    }

    /**
     * Returns the minimum U coordinate to use when rendering with this icon.
     */
    @Override
    public float getMinU() {
        final IIcon icon = getBaseIcon();
        return this.flipU ? icon.getMaxU() : icon.getMinU();
    }

    /**
     * Returns the maximum U coordinate to use when rendering with this icon.
     */
    @Override
    public float getMaxU() {
        final IIcon icon = getBaseIcon();
        return this.flipU ? icon.getMinU() : icon.getMaxU();
    }

    /**
     * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
     */
    @Override
    public float getInterpolatedU(double p_94214_1_) {
        final float f = this.getMaxU() - this.getMinU();
        return this.getMinU() + f * ((float) p_94214_1_ / 16.0F);
    }

    /**
     * Returns the minimum V coordinate to use when rendering with this icon.
     */
    @Override
    public float getMinV() {
        final IIcon icon = getBaseIcon();
        return this.flipV ? icon.getMaxV() : icon.getMinV();
    }

    /**
     * Returns the maximum V coordinate to use when rendering with this icon.
     */
    @Override
    public float getMaxV() {
        final IIcon icon = getBaseIcon();
        return this.flipV ? icon.getMinV() : icon.getMaxV();
    }

    /**
     * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
     */
    @Override
    public float getInterpolatedV(double p_94207_1_) {
        final float f = this.getMaxV() - this.getMinV();
        return this.getMinV() + f * ((float) p_94207_1_ / 16.0F);
    }

    @Override
    public String getIconName() {
        return getBaseIcon().getIconName();
    }

    IIcon getBaseIcon() {
        return baseIcon;
    }
}
