package gregtech.common.gui.modularui.item;

import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.COLOR_NBT_TAG;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.COLOR_SELECTIONS;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.REMOVE_COLOR;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.sendPacket;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.enums.Dyes;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.net.GTPacketInfiniteSpraycan;
import gregtech.common.modularui2.factory.SelectItemGuiBuilder;

public class SprayColorInfiniteGui {

    private final ItemStack itemStack;

    public SprayColorInfiniteGui(ItemStack itemStack) {
        this.itemStack = itemStack;

        if (!(itemStack.getItem() instanceof MetaGeneratedItem)) {
            throw new RuntimeException("Tried to open the infinite spray can GUI with no can in main hand or offhand");
        }
    }

    public ModularPanel build() {
        int currentSelected = getColor(itemStack);

        return new SelectItemGuiBuilder(GTGuis.createPopUpPanel("infinite_spray"), COLOR_SELECTIONS) //
            .setHeaderItem(itemStack)
            .setTitle(IKey.lang("gt.behaviour.paintspray.infinite.gui.header"))
            .setSelected(currentSelected)
            .setOnSelectedClientAction((selected, $) -> {
                sendPacket(GTPacketInfiniteSpraycan.Action.SET_COLOR, selected);
                MCHelper.closeScreen();
            })
            .setCurrentItemWidgetCustomizer(
                widget -> widget.tooltipBuilder(
                    tooltip -> tooltip.clearText()
                        .add(getTooltip(currentSelected))))
            .setChoiceWidgetCustomizer(
                (index, widget) -> widget.tooltipBuilder(
                    tooltip -> tooltip.clearText()
                        .add(getTooltip(index))))
            .build();
    }

    public byte getColor(ItemStack sprayCan) {
        if (ItemStackNBT.hasKey(sprayCan, COLOR_NBT_TAG)) {
            return ItemStackNBT.getByte(sprayCan, COLOR_NBT_TAG);
        }
        return REMOVE_COLOR;
    }

    public String getTooltip(int index) {
        return index == REMOVE_COLOR ? StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.solvent")
            : Dyes.get(index).mName;
    }
}
