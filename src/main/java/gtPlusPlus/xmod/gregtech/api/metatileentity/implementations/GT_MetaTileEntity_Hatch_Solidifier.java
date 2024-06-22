package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Hatch_Solidifier extends GT_MetaTileEntity_Hatch_Input {

    static final int moldSlot = 2;
    static final Item[] solidifierMolds = { ItemList.Shape_Mold_Bottle.get(1)
        .getItem(),
        ItemList.Shape_Mold_Plate.get(1)
            .getItem(),
        ItemList.Shape_Mold_Ingot.get(1)
            .getItem(),
        ItemList.Shape_Mold_Casing.get(1)
            .getItem(),
        ItemList.Shape_Mold_Gear.get(1)
            .getItem(),
        ItemList.Shape_Mold_Gear_Small.get(1)
            .getItem(),
        ItemList.Shape_Mold_Credit.get(1)
            .getItem(),
        ItemList.Shape_Mold_Nugget.get(1)
            .getItem(),
        ItemList.Shape_Mold_Block.get(1)
            .getItem(),
        ItemList.Shape_Mold_Ball.get(1)
            .getItem(),
        ItemList.Shape_Mold_Cylinder.get(1)
            .getItem(),
        ItemList.Shape_Mold_Anvil.get(1)
            .getItem(),
        ItemList.Shape_Mold_Arrow.get(1)
            .getItem(),
        ItemList.Shape_Mold_Rod.get(1)
            .getItem(),
        ItemList.Shape_Mold_Bolt.get(1)
            .getItem(),
        ItemList.Shape_Mold_Round.get(1)
            .getItem(),
        ItemList.Shape_Mold_Screw.get(1)
            .getItem(),
        ItemList.Shape_Mold_Ring.get(1)
            .getItem(),
        ItemList.Shape_Mold_Rod_Long.get(1)
            .getItem(),
        ItemList.Shape_Mold_Rotor.get(1)
            .getItem(),
        ItemList.Shape_Mold_Turbine_Blade.get(1)
            .getItem(),
        ItemList.Shape_Mold_Pipe_Tiny.get(1)
            .getItem(),
        ItemList.Shape_Mold_Pipe_Small.get(1)
            .getItem(),
        ItemList.Shape_Mold_Pipe_Medium.get(1)
            .getItem(),
        ItemList.Shape_Mold_Pipe_Large.get(1)
            .getItem(),
        ItemList.Shape_Mold_Pipe_Huge.get(1)
            .getItem(),
        ItemList.Shape_Mold_ToolHeadDrill.get(1)
            .getItem() };

    public GT_MetaTileEntity_Hatch_Solidifier(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Fluid Input with Mold for " + EnumChatFormatting.YELLOW
                + "Large Processing Factory"
                + EnumChatFormatting.RESET,
            "#22 Circuit is imprinted in Hatch", "Capacity: " + GT_Utility.formatNumbers(getCapacity()) + "L",
            "Added by: " + EnumChatFormatting.AQUA
                + "Quetz4l"
                + " - "
                + EnumChatFormatting.RED
                + "[GT++]"
                + EnumChatFormatting.RESET };
    }

    public GT_MetaTileEntity_Hatch_Solidifier(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
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
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        if (aIndex == moldSlot && aStack != null) {
            Item inputItem = aStack.getItem();

            for (final Item item : solidifierMolds) {
                if (inputItem == item) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Solidifier(mName, mTier, mDescriptionArray, mTextures);
    }

    public ItemStack getMold() {
        return this.getStackInSlot(moldSlot);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new SlotWidget(new MoldSlot(inventoryHandler, moldSlot)).setPos(125, 35)
                .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_MOLD)
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
