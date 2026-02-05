package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchExtrusionGui;
import gregtech.common.items.ItemIntegratedCircuit;

public class MTEHatchExtrusion extends MTEHatchInputBus {

    public boolean disableFilter = false;
    public boolean disableLimited = false;

    public int shapeSlot = getSlots(mTier);
    public int circuitSlot = getSlots(mTier) + 1;

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
        // Containers, Misc
        ItemList.Shape_Extruder_Bottle.get(1), ItemList.Shape_Extruder_Casing.get(1),
        ItemList.Shape_Extruder_Cell.get(1) };

    public boolean oneStackLimit = false;

    public MTEHatchExtrusion(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchExtrusion(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier) + 2, aDescription, aTextures);
    }

    public static int getSlots(int aTier) {
        return (aTier - 4) * 18 + 9;
    }

    @Override
    public int getCircuitSlotX() {
        return 152;
    }

    @Override
    public int getCircuitSlotY() {
        int rows = 4 + (mTier - 5) * 2;
        return 65 + (rows - 4) * 18;
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Input Bus with Mold for " + EnumChatFormatting.YELLOW + "Extrusion Machine" + EnumChatFormatting.RESET,
            "Capacity: " + formatNumber(getSlots(mTier)) + " Slots",
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
        return new MTEHatchExtrusionGui(this).build(data, syncManager, uiSettings);
    }
}
