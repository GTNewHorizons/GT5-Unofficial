package gregtech.common.items;

import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.text.WordUtils;

import gregtech.api.enums.Materials;
import gregtech.api.util.ColorUtils;
import gregtech.api.util.GTLanguageManager;

public enum DropType {

    OIL("oil", true),
    MUTAGEN("small mutagen catalyst", true),
    COOLANT("coolant", true),
    HOT_COOLANT("hot coolant", true),
    HYDRA("hydra blood", true),
    SNOW_QUEEN("snow queen blood", true),
    OXYGEN("oxygen", true),
    LAPIS("lapis coolant", true),
    ENDERGOO("ender goo", true);

    private static final int[][] colours = new int[][] {
        { ColorUtils.drop1Oil.getColor(), ColorUtils.drop2Oil.getColor() },
        { ColorUtils.drop1Mutagen.getColor(), ColorUtils.drop2Mutagen.getColor() },
        { ColorUtils.drop1Coolant.getColor(), ColorUtils.drop2Coolant.getColor() },
        { ColorUtils.drop1HotCoolant.getColor(), ColorUtils.drop2HotCoolant.getColor() },
        { ColorUtils.drop1Hydra.getColor(), ColorUtils.drop2Hydra.getColor() },
        { ColorUtils.drop1SnowQueen.getColor(), ColorUtils.drop2SnowQueen.getColor() },
        { ColorUtils.drop1Oxygen.getColor(), ColorUtils.drop2Oxygen.getColor() },
        { ColorUtils.drop1Lapis.getColor(), ColorUtils.drop2Lapis.getColor() },
        { ColorUtils.drop1Endergoo.getColor(), ColorUtils.drop2Endergoo.getColor() }, };
    public boolean showInList;
    public Materials material;
    public int chance;
    private final String name;

    DropType(String pName, boolean show) {
        this.name = pName;
        this.showInList = show;
        GTLanguageManager.addStringLocalization("drop." + this.name, WordUtils.capitalize(this.name) + " Drop");
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal("drop." + this.name);
    }

    public int[] getColours() {
        return colours[this.ordinal()];
    }
}
