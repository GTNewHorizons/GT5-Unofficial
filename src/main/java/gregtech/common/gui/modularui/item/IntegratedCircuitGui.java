package gregtech.common.gui.modularui.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;

import gregtech.api.enums.GTValues;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.net.GTPacketUpdateItem;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.modularui2.factory.SelectItemGuiBuilder;

public class IntegratedCircuitGui {

    private final PlayerInventoryGuiData data;
    private final ItemStack configurator;

    public IntegratedCircuitGui(PlayerInventoryGuiData data, ItemStack configurator) {
        this.data = data;
        this.configurator = configurator;

        ItemStack usedItemStack = data.getUsedItemStack();

        if (!(usedItemStack.getItem() instanceof ItemIntegratedCircuit)) throw new RuntimeException(
            "Tried to open the integrated circuit GUI with no circuit in main hand or offhand");

    }

    public ModularPanel build() {
        return new SelectItemGuiBuilder(
            GTGuis.createPopUpPanel("programmed_circuit"),
            ItemIntegratedCircuit.ALL_VARIANTS).setHeaderItem(configurator)
                .setTitle(IKey.lang("GT5U.item.programmed_circuit.select.header"))
                // selected index 0 == config 1
                .setSelected(
                    data.getUsedItemStack()
                        .getItemDamage() - 1)
                .setOnSelectedClientAction((selected, $) -> {
                    onConfigured(selected + 1);
                    MCHelper.closeScreen();
                })
                .setCurrentItemSlotOverlay(GTGuiTextures.OVERLAY_SLOT_INT_CIRCUIT)
                .build();
    }

    private void onConfigured(int meta) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("meta", (byte) meta);
        GTValues.NW.sendToServer(new GTPacketUpdateItem(tag));
    }
}
