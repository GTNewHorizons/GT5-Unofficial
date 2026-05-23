package gregtech.common.gui.modularui.item;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemVolumetricFlask;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;

public class VolumetricFlaskGui {

    private PlayerInventoryGuiData data;

    public VolumetricFlaskGui(PlayerInventoryGuiData data) {
        this.data = data;

        ItemStack usedItemStack = data.getUsedItemStack();
        if (!(usedItemStack.getItem() instanceof ItemVolumetricFlask))
            throw new RuntimeException("Tried to open the volumetric flask GUI with no flask in main hand or offhand");
    }

    public ModularPanel build() {
        IntSyncValue cap = new IntSyncValue(
            () -> VolumetricFlaskHelper.getFlaskCapacity(data.getUsedItemStack()),
            val -> VolumetricFlaskHelper.setFlaskCapacity(data.getUsedItemStack(), val));

        ModularPanel panel = new ModularPanel("volumetricFlask").size(150, 50)
            .child(ButtonWidget.panelCloseButton());

        Flow mainColumn = Flow.col()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(10)
            .marginTop(5)
            .marginLeft(5);

        mainColumn.child(
            IKey.str(
                data.getUsedItemStack()
                    .getDisplayName())
                .asWidget());

        Flow textRow = Flow.row()
            .coverChildren()
            .childPadding(2);

        int maxCapacity = VolumetricFlaskHelper.getMaxFlaskCapacity(data.getUsedItemStack());
        textRow.child(
            new TextFieldWidget().width(80)
                .value(cap)
                .setFormatAsInteger(true)
                .setNumbers(1, maxCapacity)
                .setMaxLength(10)
                .tooltipShowUpTimer(5)
                .tooltip(
                    t -> t.addLine(
                        EnumChatFormatting.GRAY + ""
                            + EnumChatFormatting.ITALIC
                            + GTUtility
                                .translate("GT5U.gui.text.volumetric_flask.max_capacity", formatNumber(maxCapacity)))));

        textRow.child(
            IKey.lang("GT5U.gui.text.volumetric_flask.capacity")
                .asWidget());

        mainColumn.child(textRow);
        panel.child(mainColumn);

        return panel;
    }
}
