package gregtech.api.items;

import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class GT_Configurator_Item extends Item {

    private int setting = 0;
    private static final int MIN_SETTING = 0, MAX_SETTING = 32;

    public void adjust() {
        setting = setting % MAX_SETTING;
    }

    public void setSetting(int newSetting) {
        setting = newSetting;
        adjust();
    }

    public int getSetting() {
        return setting;
    }

    public int bumpUp() {
        setting++;
        adjust();
        return setting;
    }

    public int bumpDown() {
        setting--;
        adjust();
        return setting;
    }

    public void notify(EntityPlayer aPlayer) {
        GT_Utility.sendChatToPlayer(aPlayer, "Configuration: " + setting);
    }

}
