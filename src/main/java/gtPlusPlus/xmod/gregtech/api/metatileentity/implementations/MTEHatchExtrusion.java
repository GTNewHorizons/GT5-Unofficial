package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.ItemShapeExtruder;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchExtrusionGui;

@IMetaTileEntity.SkipGenerateDescription
@IMetaTileEntity.SkipGenerateName
public class MTEHatchExtrusion extends MTEHatchInputBus {

    public int shapeSlot = getSlots(mTier);
    public int circuitSlot = getSlots(mTier) + 1;

    public static final ItemStack[] extruderShapes = ItemShapeExtruder.shapes.stream()
        .map(shape -> new ItemStack(shape, 1))
        .toArray(ItemStack[]::new);

    public MTEHatchExtrusion(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchExtrusion(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier) + 2, aDescription, aTextures);
    }

    public static int getSlots(int aTier) {
        return MTEHatchInputBus.getSlots(aTier);
    }

    @Override
    public String getLocalName() {
        if (!hasOwnLocalName()) return super.getLocalName();
        return StatCollector
            .translateToLocalFormatted("gt.blockmachines.hatch.extrusion.name", GTUtility.getRomanNumeral(mTier - 4));
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
        return GTSplit.splitLocalizedFormatted(
            "gt.blockmachines.hatch.extrusion.desc",
            EnumChatFormatting.YELLOW + StatCollector.translateToLocal("gt.blockmachines.hatch.extrusion.machine")
                + EnumChatFormatting.RESET,
            formatNumber(getSlots(mTier)));
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
        return aIndex != shapeSlot && super.isValidSlot(aIndex);
    }

    @Override
    protected void fillStacksIntoFirstSlots() {
        // don't sort the ghost shape slot
        GTUtility.compactInventory(this, 0, mInventory.length - 2);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex != shapeSlot && super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex != shapeSlot && super.allowPullStack(aBaseMetaTileEntity, aIndex, side, aStack);
    }

    @Override
    public int getCircuitSlot() {
        return circuitSlot;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchExtrusionGui(this).build(data, syncManager, uiSettings);
    }
}
