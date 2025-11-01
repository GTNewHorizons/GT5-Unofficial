package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.common.modularui2.util.CommonGuiComponents.gridTemplate4by4;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.net.GTPacketSetShape;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.common.modularui2.widget.GhostShapeSlotWidget;

public class MTEHatchExtrusion extends MTEHatchInputBus {

    public static final int shapeSlot = 2;
    public static final int circuitSlot = 3;

    public static final ItemStack[] extruderShapes = {
        // Tools
        ItemList.Shape_Extruder_Axe.get(1), ItemList.Shape_Extruder_File.get(1), ItemList.Shape_Extruder_Hammer.get(1),
        ItemList.Shape_Extruder_Hoe.get(1), ItemList.Shape_Extruder_Pickaxe.get(1), ItemList.Shape_Extruder_Saw.get(1),
        ItemList.Shape_Extruder_Shovel.get(1), ItemList.Shape_Extruder_Sword.get(1),
        ItemList.Shape_Extruder_ToolHeadDrill.get(1),
        // Machine Components
        ItemList.Shape_Extruder_Gear.get(1), ItemList.Shape_Extruder_Small_Gear.get(1),
        ItemList.Shape_Extruder_Rotor.get(1), ItemList.Shape_Extruder_Turbine_Blade.get(1),
        // Pipes
        ItemList.Shape_Extruder_Pipe_Tiny.get(1), ItemList.Shape_Extruder_Pipe_Small.get(1),
        ItemList.Shape_Extruder_Pipe_Medium.get(1), ItemList.Shape_Extruder_Pipe_Large.get(1),
        ItemList.Shape_Extruder_Pipe_Huge.get(1),
        // Materials
        ItemList.Shape_Extruder_Block.get(1), ItemList.Shape_Extruder_Bolt.get(1), ItemList.Shape_Extruder_Ingot.get(1),
        ItemList.Shape_Extruder_Plate.get(1), ItemList.Shape_Extruder_Ring.get(1), ItemList.Shape_Extruder_Rod.get(1),
        ItemList.Shape_Extruder_Wire.get(1),
        // Containers, Misc
        ItemList.Shape_Extruder_Bottle.get(1), ItemList.Shape_Extruder_Casing.get(1),
        ItemList.Shape_Extruder_Cell.get(1) };

    private boolean oneStackLimit = false;

    public MTEHatchExtrusion(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchExtrusion(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier), aDescription, aTextures);
    }

    public static int getSlots(int aTier) {
        return (aTier - 3) * 16 + 2;
    }

    public static int getInventorySlots(int aTier) {
        return (aTier - 3) * 16;
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Input Bus with Mold for " + EnumChatFormatting.YELLOW + "Extrusion Machine" + EnumChatFormatting.RESET,
            "Capacity: " + GTUtility.formatNumbers(getInventorySlots(mTier)) + " Slots",
            "Added by: " + EnumChatFormatting.BLUE + "VorTex" };
    }

    public static ItemStack findMatchingShape(ItemStack stack) {
        if (stack == null) return null;
        for (ItemStack shape : extruderShapes) {
            if (GTUtility.areStacksEqual(shape, stack, true)) return shape.copy();
        }
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        if (aIndex == shapeSlot) return findMatchingShape(aStack) != null;
        if (aIndex == circuitSlot) return GTUtility.isStackValid(aStack) && aStack.stackSize == 1
            && (aStack.getItem() instanceof ItemIntegratedCircuit
                || GTUtility.areStacksEqual(aStack, ItemList.Circuit_Integrated.get(1), true));
        return super.isItemValidForSlot(aIndex, aStack);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchExtrusion(mName, mTier, mDescriptionArray, mTextures);
    }

    public void setShape(ItemStack selected) {
        ItemStack phantom = findMatchingShape(selected);
        if (inventoryHandler != null) {
            inventoryHandler.setStackInSlot(shapeSlot, phantom);
        }
        try {
            this.setInventorySlotContents(shapeSlot, phantom);
        } catch (Throwable ignored) {}
        markDirty();
        GTValues.NW.sendToServer(new GTPacketSetShape(this, selected));
    }

    public int findMatchingShapeIndex(ItemStack stack) {
        for (int i = 0; i < extruderShapes.length; i++) {
            if (GTUtility.areStacksEqual(extruderShapes[i], stack, true)) return i;
        }
        return -1;
    }

    @Override
    public void onBlockDestroyed() {
        inventoryHandler.setStackInSlot(shapeSlot, null);
        inventoryHandler.setStackInSlot(circuitSlot, null);
        super.onBlockDestroyed();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex != shapeSlot && aIndex != circuitSlot && super.isValidSlot(aIndex);
    }

    @Override
    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++) {
            if (i != shapeSlot && i != circuitSlot && mInventory[i] != null && mInventory[i].stackSize <= 0) {
                mInventory[i] = null;
            }
        }
        if (!disableSort) fillStacksIntoFirstSlots();
        if (oneStackLimit) {
            for (ItemStack itemStack : mInventory) {
                if (itemStack != null) {
                    itemStack.stackSize = Math.min(1, itemStack.stackSize);
                }
            }
        }
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex != shapeSlot && aIndex != circuitSlot
            && super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex != shapeSlot && aIndex != circuitSlot
            && super.allowPullStack(aBaseMetaTileEntity, aIndex, side, aStack);
    }

    @Override
    protected boolean limitedAllowPutStack(int aIndex, ItemStack aStack) {
        for (int i = 0; i < getSizeInventory(); i++) {
            if (isValidSlot(i) && GTUtility.areStacksEqual(GTOreDictUnificator.get_nocopy(aStack), mInventory[i])) {
                return i == aIndex;
            }
        }
        return mInventory[aIndex] == null;
    }

    @Override
    public int getCircuitSlot() {
        return circuitSlot;
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        syncManager.registerSlotGroup("item_inv", 1);
        syncManager.registerSlotGroup("shape_slot", 1);
        syncManager.registerSlotGroup("circuit_slot", 1);

        syncManager.syncValue("oneStackLimit", new BooleanSyncValue(() -> oneStackLimit, v -> oneStackLimit = v));

        IntSyncValue shapeSyncHandler = new IntSyncValue(() -> {
            ItemStack current = inventoryHandler.getStackInSlot(shapeSlot);
            return current != null ? findMatchingShapeIndex(current) : -1;
        }, index -> {
            if (index >= 0 && index < extruderShapes.length) {
                setShape(extruderShapes[index]);
            } else {
                setShape(null);
            }
        });

        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .build();

        panel.child(gridTemplate4by4(index -> {
            if (index >= shapeSlot) index++;
            if (index >= circuitSlot) index++;
            return new ItemSlot().slot(new ModularSlot(inventoryHandler, index).slotGroup("item_inv"));
        }).pos(52, 7));

        panel.child(
            new GhostShapeSlotWidget(this).slot(new ModularSlot(inventoryHandler, shapeSlot))
                .pos(132, 34));

        return panel;
    }
}
