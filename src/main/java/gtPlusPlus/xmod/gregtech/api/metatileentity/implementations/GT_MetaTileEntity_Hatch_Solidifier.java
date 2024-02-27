package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Hatch_Solidifier extends GT_MetaTileEntity_Hatch_Input {

    static final int moldSlot = 2;

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
        builder.widget(new SlotWidget(inventoryHandler, moldSlot).setPos(125, 35).setSize(18, 18));
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
