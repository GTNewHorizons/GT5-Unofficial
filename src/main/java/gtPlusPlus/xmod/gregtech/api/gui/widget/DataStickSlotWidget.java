package gtPlusPlus.xmod.gregtech.api.gui.widget;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Utility;

public class DataStickSlotWidget extends SlotWidget {

    public DataStickSlotWidget(IItemHandlerModifiable handler, int index) {
        this(new BaseSlot(handler, index) {
            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
    }

    private DataStickSlotWidget(BaseSlot slot) {
        super(slot);
        setFilter(stack -> GT_Utility.areStacksEqual(stack, ItemList.Tool_DataStick.get(1), true)
                || GT_Utility.areStacksEqual(stack, ItemList.Tool_DataOrb.get(1), true));
        setBackground(ModularUITextures.ITEM_SLOT, GT_UITextures.OVERLAY_SLOT_DATA_ORB);
    }
}
