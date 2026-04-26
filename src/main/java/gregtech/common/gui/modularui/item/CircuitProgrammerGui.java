package gregtech.common.gui.modularui.item;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.factory.inventory.InventoryTypes;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import bartworks.common.items.ItemCircuitProgrammer;
import bartworks.util.BWUtil;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class CircuitProgrammerGui {

    private static final String NBT_KEY_HAS_CHIP = "HasChip";
    private static final String NBT_KEY_CHIP_CONFIG = "ChipConfig";

    private final PlayerInventoryGuiData data;
    private final PanelSyncManager syncManager;

    private final int usedSlot;

    public CircuitProgrammerGui(PlayerInventoryGuiData data, PanelSyncManager syncManager) {
        this.syncManager = syncManager;
        this.data = data;

        ItemStack usedItemStack = data.getUsedItemStack();

        if (usedItemStack != null && usedItemStack.getItem() instanceof ItemCircuitProgrammer)
            usedSlot = data.getSlotIndex();
        else throw new RuntimeException(
            "Tried to open the circuit programmer GUI with no programmer in main hand or offhand");

    }

    public ModularPanel build() {
        // config slot
        ModularSlot circuitSlot = new ModularSlot(new ItemStackHandler(1) {

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                saveChange(getStackInSlot(slot));
            }
        }, 0) {

            @Override
            public void putStack(ItemStack stack) {
                if (isLVCircuit(stack)) stack = createRealCircuit(0);
                super.putStack(stack);
            }
        }.filter(this::isValidCircuit)
            .singletonSlotGroup();

        // set slot contents upon opening gui
        if (ItemStackNBT.getBoolean(data.getUsedItemStack(), NBT_KEY_HAS_CHIP))
            circuitSlot.putStack(createRealCircuit(ItemStackNBT.getByte(data.getUsedItemStack(), NBT_KEY_CHIP_CONFIG)));

        // for syncing current circuit config
        IntSyncValue circuitSyncer = new IntSyncValue(
            () -> -1,
            val -> { if (val != -1) circuitSlot.putStack(createRealCircuit(val)); });
        syncManager.syncValue("circuit", circuitSyncer);

        ModularPanel panel = ModularPanel.defaultPanel("circuit_programmer", 256, 166)
            .background(GTGuiTextures.BW_BACKGROUND_CIRCUIT_PROGRAMMER);

        // player inventory
        panel.child(
            SlotGroupWidget.playerInventory(7, false)
                .right(8));
        if (data.getInventoryType() == InventoryTypes.PLAYER) {
            syncManager.bindPlayerInventory(
                data.getPlayer(),
                (inv, index) -> index == usedSlot ? new ModularSlot(inv, index).accessibility(false, false)
                    : new ModularSlot(inv, index));
        }

        // config button grid
        panel.child(
            new Grid().gridOfWidthHeight(
                12,
                2,
                ($x, $y, index) -> new ButtonWidget<>().disableThemeBackground(true)
                    .disableHoverThemeBackground(true)
                    .onMousePressed(mouseButton -> {
                        ItemStack current = circuitSlot.getStack();
                        if (current != null && isProgrammedCircuit(current)) circuitSyncer.setIntValue(index + 1);
                        return true;
                    }))
                .coverChildren()
                .top(21)
                .right(8));

        // config item slot
        panel.child(
            new ItemSlot().slot(circuitSlot)
                .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_INT_CIRCUIT)
                .top(60)
                .right(116));

        return panel;
    }

    private void saveChange(ItemStack newCircuit) {
        if (newCircuit != null) {
            ItemStackNBT.setBoolean(data.getUsedItemStack(), NBT_KEY_HAS_CHIP, true);
            ItemStackNBT.setByte(data.getUsedItemStack(), NBT_KEY_CHIP_CONFIG, (byte) newCircuit.getItemDamage());
        } else ItemStackNBT.setBoolean(data.getUsedItemStack(), NBT_KEY_HAS_CHIP, false);
    }

    private ItemStack createRealCircuit(int config) {
        return ItemList.Circuit_Integrated.getWithDamage(1, config);
    }

    private boolean isValidCircuit(ItemStack itemStack) {
        return isLVCircuit(itemStack) || isProgrammedCircuit(itemStack);
    }

    private boolean isProgrammedCircuit(ItemStack stack) {
        return stack.getItem()
            .equals(
                GTUtility.getIntegratedCircuit(0)
                    .getItem());
    }

    private boolean isLVCircuit(ItemStack stack) {
        return BWUtil.checkStackAndPrefix(stack)
            && OrePrefixes.circuit.equals(GTOreDictUnificator.getAssociation(stack).mPrefix)
            && GTOreDictUnificator.getAssociation(stack).mMaterial.mMaterial.equals(Materials.LV);
    }
}
