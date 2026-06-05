package gregtech.common.items;

import java.util.HashMap;

import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.api.enums.Materials;

public enum PollenType {

    MATRIX("matrix", true);

    private static final int[][] colours = new int[][] { { 0x19191B, 0x303032 }, };
    public boolean showInList;
    public Materials material;
    public int chance;
    private final String name;

    PollenType(String pName, boolean show) {
        this.name = pName;
        this.showInList = show;
        HashMap<String, String> tLang = new HashMap<>();
        tLang.put(
            "pollen." + this.name,
            this.name.substring(0, 1)
                .toUpperCase() + this.name.substring(1) + " Pollen");
        LanguageRegistry.instance()
            .injectLanguage("en_US", tLang);
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal("pollen." + this.name);
    }

    public int[] getColours() {
        return colours[this.ordinal()];
    }
}
