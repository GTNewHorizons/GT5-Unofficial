package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import ggfab.GGItemList;
import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.util.GTUtility;

public class MTEHatchSolidifier extends MTEHatchInput implements IConfigurationCircuitSupport {

    static final int moldSlot = 2;
    static final int circuitSlot = 3;

    static final ItemStack[] solidifierMolds = { ItemList.Shape_Mold_Bottle.get(1), ItemList.Shape_Mold_Plate.get(1),
        ItemList.Shape_Mold_Ingot.get(1), ItemList.Shape_Mold_Casing.get(1), ItemList.Shape_Mold_Gear.get(1),
        ItemList.Shape_Mold_Gear_Small.get(1), ItemList.Shape_Mold_Credit.get(1), ItemList.Shape_Mold_Nugget.get(1),
        ItemList.Shape_Mold_Block.get(1), ItemList.Shape_Mold_Ball.get(1), ItemList.Shape_Mold_Cylinder.get(1),
        ItemList.Shape_Mold_Anvil.get(1), ItemList.Shape_Mold_Arrow.get(1), ItemList.Shape_Mold_Rod.get(1),
        ItemList.Shape_Mold_Bolt.get(1), ItemList.Shape_Mold_Round.get(1), ItemList.Shape_Mold_Screw.get(1),
        ItemList.Shape_Mold_Ring.get(1), ItemList.Shape_Mold_Rod_Long.get(1), ItemList.Shape_Mold_Rotor.get(1),
        ItemList.Shape_Mold_Turbine_Blade.get(1), ItemList.Shape_Mold_Pipe_Tiny.get(1),
        ItemList.Shape_Mold_Pipe_Small.get(1), ItemList.Shape_Mold_Pipe_Medium.get(1),
        ItemList.Shape_Mold_Pipe_Large.get(1), ItemList.Shape_Mold_Pipe_Huge.get(1),
        ItemList.Shape_Mold_ToolHeadDrill.get(1),

        GGItemList.SingleUseFileMold.get(1), GGItemList.SingleUseWrenchMold.get(1),
        GGItemList.SingleUseCrowbarMold.get(1), GGItemList.SingleUseWireCutterMold.get(1),
        GGItemList.SingleUseHardHammerMold.get(1), GGItemList.SingleUseSoftMalletMold.get(1),
        GGItemList.SingleUseScrewdriverMold.get(1), GGItemList.SingleUseSawMold.get(1) };

    public MTEHatchSolidifier(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Fluid Input with Mold for " + EnumChatFormatting.YELLOW + "Fluid Shaper" + EnumChatFormatting.RESET,
            "Capacity: " + GTUtility.formatNumbers(getCapacity()) + "L",
            "Added by: " + EnumChatFormatting.AQUA
                + "Quetz4l"
                + " - "
                + EnumChatFormatting.RED
                + "[GT++]"
                + EnumChatFormatting.RESET };
    }

    public MTEHatchSolidifier(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, getSlots(aTier), aTier, aDescription, aTextures);
    }

    private class MoldSlot extends BaseSlot {

        public MoldSlot(IItemHandlerModifiable inventory, int index) {
            super(inventory, index);
        }

        @Override
        public boolean isItemValidPhantom(ItemStack stack) {
            return super.isItemValidPhantom(stack) && getBaseMetaTileEntity().isItemValidForSlot(getSlotIndex(), stack);
        }

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
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        if (aIndex == moldSlot && aStack != null) {
            for (final ItemStack itemStack : solidifierMolds) {
                if (GTUtility.areStacksEqual(itemStack, aStack, true)) {
                    return true;
                }
            }
            if (GTUtility.isAnyIntegratedCircuit(aStack)) {
                return true;
            }
        } else if (aIndex != moldSlot) {
            return super.isItemValidForSlot(aIndex, aStack);
        }

        return false;
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

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new SlotWidget(new MoldSlot(inventoryHandler, moldSlot)).setShiftClickPriority(-1)
                .setPos(125, 35)
                .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_MOLD)
                .setSize(18, 18));
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
    }

    // for a drop-down form when the hatch is destroyed
    @Override
    public boolean isValidSlot(int aIndex) {
        if (aIndex == moldSlot) return true;
        else return super.isValidSlot(aIndex);
    }
}
