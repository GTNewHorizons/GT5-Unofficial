package gregtech.api.items;

import net.minecraft.item.Item;

public class GT_Configurator_Item extends Item {

    private int setting = 0;
    private static final int MIN_SETTING = 0, MAX_SETTING = 32;

    public void setSetting(int newSetting) {
        setting = newSetting % MAX_SETTING;
    }

    public int getSetting() {
        return setting;
    }
}
