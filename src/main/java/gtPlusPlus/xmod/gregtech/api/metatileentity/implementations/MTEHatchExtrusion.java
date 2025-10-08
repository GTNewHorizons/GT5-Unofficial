package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.net.GTPacketSetShape;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.uifactory.SelectItemUIFactory;
import gregtech.common.items.ItemIntegratedCircuit;

public class MTEHatchExtrusion extends MTEHatchInputBus implements IAddUIWidgets {

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

    private class ShapeContainer extends BaseSlot {

        public ShapeContainer(IItemHandlerModifiable inventory, int index) {
            super(inventory, index, true);
        }

        @Override
        public boolean isItemValidPhantom(ItemStack stack) {
            return super.isItemValidPhantom(stack) && getBaseMetaTileEntity().isItemValidForSlot(getSlotIndex(), stack);
        }
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

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        buildContext.addCloseListener(() -> uiButtonCount = 0);
        addSortStacksButton(builder);
        addOneStackLimitButton(builder);

        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int row = 0; row * 4 < inventoryHandler.getSlots() - 1; row++) {
            int columnsToMake = Math.min(inventoryHandler.getSlots() - row * 4 - 2, 4);
            for (int column = 0; column < columnsToMake; column++) {
                int slotIndex = row * 4 + column;
                if (slotIndex >= shapeSlot) slotIndex++;
                if (slotIndex >= circuitSlot) slotIndex++;
                scrollable.widget(
                    new SlotWidget(inventoryHandler, slotIndex).setPos(column * 18, row * 18)
                        .setSize(18, 18));
            }
        }
        builder.widget(
            scrollable.setSize(18 * 4 + 4, 18 * 4)
                .setPos(52, 7));

        final AtomicBoolean dialogOpened = new AtomicBoolean(false);
        builder.widget(new SlotWidget(new ShapeContainer(inventoryHandler, shapeSlot)) {

            @Override
            protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                final ItemStack current = inventoryHandler.getStackInSlot(shapeSlot);
                final int currentIndex = current != null ? findMatchingShapeIndex(current) : -1;
                ItemStack newShape = current;
                if (clickData.shift && clickData.mouseButton == 0) {
                    if (NetworkUtils.isClient() && !dialogOpened.get()) {
                        openSelectShapeDialog(getContext(), dialogOpened);
                    }
                    return;
                } else {
                    int newIndex = currentIndex;
                    if (clickData.mouseButton == 0) newIndex++;
                    else if (clickData.mouseButton == 1) newIndex--;
                    newIndex = Math.floorMod(newIndex, extruderShapes.length);
                    newShape = extruderShapes[newIndex];
                }
                setShape(newShape);
            }

            @Override
            protected void phantomScroll(int direction) {
                if (GTMod.proxy.invertCircuitScrollDirection) {
                    phantomClick(new ClickData(direction > 0 ? 0 : 1, false, false, false));
                } else {
                    phantomClick(new ClickData(direction > 0 ? 1 : 0, false, false, false));
                }
            }

            @Override
            public List<String> getExtraTooltip() {
                return Arrays.asList(
                    EnumChatFormatting.DARK_GRAY + EnumChatFormatting.getTextWithoutFormattingCodes(
                        StatCollector.translateToLocal("GT5U.machines.select_shape.tooltip.1")),
                    EnumChatFormatting.DARK_GRAY + EnumChatFormatting.getTextWithoutFormattingCodes(
                        StatCollector.translateToLocal("GT5U.machines.select_shape.tooltip.2")));
            }
        }.setOverwriteItemStackTooltip(list -> {
            list.removeIf(
                line -> line.contains(GTUtility.translate("gt.integrated_circuit.tooltip.0"))
                    || line.contains(GTUtility.translate("gt.integrated_circuit.tooltip.1")));
            return list;
        })
            .disableShiftInsert()
            .setHandlePhantomActionClient(true)
            .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_EXTRUDER_SHAPE)
            .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.select_shape.tooltip"))
            .setTooltipShowUpDelay(10)
            .setPos(132, 34));
    }

    protected void openSelectShapeDialog(ModularUIContext uiContext, AtomicBoolean dialogOpened) {
        final IItemHandlerModifiable inv = getInventoryHandler();
        if (inv == null) return;
        uiContext.openClientWindow(
            player -> new SelectItemUIFactory(
                GTUtility.translate("GT5U.machines.select_shape"),
                getStackForm(0),
                this::onShapeSelected,
                Arrays.asList(extruderShapes),
                findMatchingShapeIndex(inv.getStackInSlot(shapeSlot))).setAnotherWindow(true, dialogOpened)
                    .setGuiTint(getGUIColorization())
                    .setCurrentGetter(() -> inv.getStackInSlot(shapeSlot))
                    .createWindow(new UIBuildContext(player)));
    }

    protected void onShapeSelected(ItemStack selected) {
        setShape(selected);
    }

    private int findMatchingShapeIndex(ItemStack stack) {
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
}
