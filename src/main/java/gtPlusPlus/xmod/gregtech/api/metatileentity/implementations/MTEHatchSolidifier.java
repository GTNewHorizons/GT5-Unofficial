package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import ggfab.GGItemList;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.net.GTPacketSetMold;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.base.ItemSelectBaseGui;

public class MTEHatchSolidifier extends MTEHatchInput implements IConfigurationCircuitSupport {

    public static final int moldSlot = 2;
    public static final int circuitSlot = 3;

    public static final ItemStack[] solidifierMolds = { ItemList.Shape_Mold_Bottle.get(1),
        ItemList.Shape_Mold_Plate.get(1), ItemList.Shape_Mold_Ingot.get(1), ItemList.Shape_Mold_Casing.get(1),
        ItemList.Shape_Mold_Gear.get(1), ItemList.Shape_Mold_Gear_Small.get(1), ItemList.Shape_Mold_Credit.get(1),
        ItemList.Shape_Mold_Nugget.get(1), ItemList.Shape_Mold_Block.get(1), ItemList.Shape_Mold_Ball.get(1),
        ItemList.Shape_Mold_Cylinder.get(1), ItemList.Shape_Mold_Anvil.get(1), ItemList.Shape_Mold_Arrow.get(1),
        ItemList.Shape_Mold_Rod.get(1), ItemList.Shape_Mold_Bolt.get(1), ItemList.Shape_Mold_Round.get(1),
        ItemList.Shape_Mold_Screw.get(1), ItemList.Shape_Mold_Ring.get(1), ItemList.Shape_Mold_Rod_Long.get(1),
        ItemList.Shape_Mold_Rotor.get(1), ItemList.Shape_Mold_Turbine_Blade.get(1),
        ItemList.Shape_Mold_Pipe_Tiny.get(1), ItemList.Shape_Mold_Pipe_Small.get(1),
        ItemList.Shape_Mold_Pipe_Medium.get(1), ItemList.Shape_Mold_Pipe_Large.get(1),
        ItemList.Shape_Mold_Pipe_Huge.get(1), ItemList.Shape_Mold_ToolHeadDrill.get(1),
        GGItemList.SingleUseFileMold.get(1), GGItemList.SingleUseWrenchMold.get(1),
        GGItemList.SingleUseCrowbarMold.get(1), GGItemList.SingleUseWireCutterMold.get(1),
        GGItemList.SingleUseHardHammerMold.get(1), GGItemList.SingleUseSoftMalletMold.get(1),
        GGItemList.SingleUseScrewdriverMold.get(1), GGItemList.SingleUseSawMold.get(1) };

    public MTEHatchSolidifier(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchSolidifier(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, getSlots(aTier), aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Fluid Input with Mold for " + EnumChatFormatting.YELLOW
                + "Fluid Solidifier Multiblocks"
                + EnumChatFormatting.RESET,
            "Capacity: " + formatNumber(getCapacity()) + "L",
            "Added by: " + EnumChatFormatting.AQUA
                + "Quetz4l - "
                + EnumChatFormatting.RED
                + "[GT++]"
                + EnumChatFormatting.RESET };
    }

    public static ItemStack findMatchingMold(ItemStack stack) {
        if (stack == null) return null;
        for (ItemStack mold : solidifierMolds) {
            if (GTUtility.areStacksEqual(mold, stack, true)) return mold.copy();
        }
        return null;
    }

    private class MoldContainer extends BaseSlot {

        public MoldContainer(IItemHandlerModifiable inventory, int index) {
            super(inventory, index, true);
        }

        @Override
        public boolean isItemValidPhantom(ItemStack stack) {
            return super.isItemValidPhantom(stack) && getBaseMetaTileEntity().isItemValidForSlot(getSlotIndex(), stack);
        }
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        if (aIndex == moldSlot) return findMatchingMold(aStack) != null;
        return super.isItemValidForSlot(aIndex, aStack);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchSolidifier(mName, mTier, mDescriptionArray, mTextures);
    }

    public List<ItemStack> getNonConsumableItems() {
        List<ItemStack> items = new ArrayList<>();
        if (getStackInSlot(moldSlot) != null) items.add(getStackInSlot(moldSlot));
        if (getStackInSlot(circuitSlot) != null) items.add(getStackInSlot(circuitSlot));
        return items;
    }

    public void setMold(ItemStack selected) {
        ItemStack phantom = findMatchingMold(selected);
        if (inventoryHandler != null) {
            inventoryHandler.setStackInSlot(moldSlot, phantom);
        }
        try {
            this.setInventorySlotContents(moldSlot, phantom);
        } catch (Exception ignored) {}
        markDirty();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        final AtomicBoolean dialogOpened = new AtomicBoolean(false);
        builder.widget(new SlotWidget(new MoldContainer(inventoryHandler, moldSlot)) {

            @Override
            protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                final ItemStack current = inventoryHandler.getStackInSlot(moldSlot);
                final int currentIndex = current != null ? findMatchingMoldIndex(current) : -1;
                ItemStack newMold = current;
                if (clickData.shift && clickData.mouseButton == 0) {
                    if (NetworkUtils.isClient() && !dialogOpened.get()) {
                        openSelectMoldDialog(getContext(), dialogOpened);
                    }
                    return;
                } else {
                    int newIndex = currentIndex;
                    if (clickData.mouseButton == 0) newIndex++;
                    else if (clickData.mouseButton == 1) newIndex--;
                    newIndex = Math.floorMod(newIndex, solidifierMolds.length);
                    newMold = solidifierMolds[newIndex];
                    inventoryHandler.setStackInSlot(moldSlot, solidifierMolds[newIndex].copy());
                }

                setMold(newMold);
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
                        StatCollector.translateToLocal("GT5U.machines.select_mold.tooltip.1")),
                    EnumChatFormatting.DARK_GRAY + EnumChatFormatting.getTextWithoutFormattingCodes(
                        StatCollector.translateToLocal("GT5U.machines.select_mold.tooltip.2")));
            }
        }.setOverwriteItemStackTooltip(list -> {
            list.removeIf(
                line -> line.contains(GTUtility.translate("gt.integrated_circuit.tooltip.0"))
                    || line.contains(GTUtility.translate("gt.integrated_circuit.tooltip.1")));
            return list;
        })
            .disableShiftInsert()
            .setHandlePhantomActionClient(true)
            .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_MOLD)
            .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.select_mold.tooltip"))
            .setTooltipShowUpDelay(10)
            .setPos(124, 34));
    }

    protected void openSelectMoldDialog(ModularUIContext uiContext, AtomicBoolean dialogOpened) {
        final IItemHandlerModifiable inv = getInventoryHandler();
        if (inv == null) return;
        uiContext.openClientWindow(
            player -> new ItemSelectBaseGui(
                GTUtility.translate("GT5U.machines.select_mold"),
                getStackForm(0),
                (ItemStack selected) -> {
                    this.onMoldSelected(selected);
                    GTValues.NW.sendToServer(new GTPacketSetMold(this, selected));
                },
                Arrays.asList(solidifierMolds),
                findMatchingMoldIndex(inv.getStackInSlot(moldSlot))).setAnotherWindow(true, dialogOpened)
                    .setGuiTint(getGUIColorization())
                    .setCurrentGetter(() -> inv.getStackInSlot(moldSlot))
                    .createWindow(new UIBuildContext(player)));
    }

    protected void onMoldSelected(ItemStack selected) {
        setMold(selected);
    }

    private int findMatchingMoldIndex(ItemStack stack) {
        for (int i = 0; i < solidifierMolds.length; i++) {
            if (GTUtility.areStacksEqual(solidifierMolds[i], stack, true)) return i;
        }
        return -1;
    }

    @Override
    public void onBlockDestroyed() {
        inventoryHandler.setStackInSlot(moldSlot, null);
        super.onBlockDestroyed();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex == moldSlot || super.isValidSlot(aIndex);
    }

    @Override
    public int getCircuitSlot() {
        return circuitSlot;
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int getCircuitSlotX() {
        return 153;
    }

    @Override
    public int getCircuitSlotY() {
        return 63;
    }
}
