package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import ggfab.GGItemList;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchSolidifierGui;
import gtPlusPlus.core.util.Utils;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchSolidifier extends MTEHatchInput implements IConfigurationCircuitSupport {

    public static final int moldSlot = 2;
    public static final int circuitSlot = 3;

    public static final ArrayList<ItemStack> solidifierMolds = new ArrayList<>(
        Arrays.asList(
            ItemList.Shape_Mold_Bottle.get(1),
            ItemList.Shape_Mold_Plate.get(1),
            ItemList.Shape_Mold_Ingot.get(1),
            ItemList.Shape_Mold_Casing.get(1),
            ItemList.Shape_Mold_Gear.get(1),
            ItemList.Shape_Mold_Gear_Small.get(1),
            ItemList.Shape_Mold_Credit.get(1),
            ItemList.Shape_Mold_Nugget.get(1),
            ItemList.Shape_Mold_Block.get(1),
            ItemList.Shape_Mold_Ball.get(1),
            ItemList.Shape_Mold_Cylinder.get(1),
            ItemList.Shape_Mold_Anvil.get(1),
            ItemList.Shape_Mold_Arrow.get(1),
            ItemList.Shape_Mold_Rod.get(1),
            ItemList.Shape_Mold_Bolt.get(1),
            ItemList.Shape_Mold_Round.get(1),
            ItemList.Shape_Mold_Screw.get(1),
            ItemList.Shape_Mold_Ring.get(1),
            ItemList.Shape_Mold_Rod_Long.get(1),
            ItemList.Shape_Mold_Rotor.get(1),
            ItemList.Shape_Mold_Turbine_Blade.get(1),
            ItemList.Shape_Mold_Pipe_Tiny.get(1),
            ItemList.Shape_Mold_Pipe_Small.get(1),
            ItemList.Shape_Mold_Pipe_Medium.get(1),
            ItemList.Shape_Mold_Pipe_Large.get(1),
            ItemList.Shape_Mold_Pipe_Huge.get(1),
            ItemList.Shape_Mold_ToolHeadDrill.get(1),
            GGItemList.SingleUseFileMold.get(1),
            GGItemList.SingleUseWrenchMold.get(1),
            GGItemList.SingleUseCrowbarMold.get(1),
            GGItemList.SingleUseWireCutterMold.get(1),
            GGItemList.SingleUseHardHammerMold.get(1),
            GGItemList.SingleUseSoftMalletMold.get(1),
            GGItemList.SingleUseScrewdriverMold.get(1),
            GGItemList.SingleUseSawMold.get(1)));

    public MTEHatchSolidifier(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchSolidifier(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, getSlots(aTier), aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return Utils.splitLocalizedFormattedWithAuthor(
            "gt.blockmachines.input_hatch_solidifier.desc",
            GTAuthors.AuthorQuetz4l,
            formatNumber(getCapacity()));
    }

    public static ItemStack findMatchingMold(ItemStack stack) {
        if (stack == null) return null;
        for (ItemStack mold : solidifierMolds) {
            if (GTUtility.areStacksEqual(mold, stack, true)) return mold.copy();
        }
        return null;
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

    public int findMatchingMoldIndex(ItemStack stack) {
        for (int i = 0; i < solidifierMolds.size(); i++) {
            if (GTUtility.areStacksEqual(solidifierMolds.get(i), stack, true)) return i;
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

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchSolidifierGui(this).build(guiData, syncManager, uiSettings);
    }
}
