package gregtech.api.metatileentity.implementations;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;

public abstract class GT_MetaTileEntity_FilterBase extends GT_MetaTileEntity_Buffer implements IAddUIWidgets {

    private static final String INVERT_FILTER_TOOLTIP = "GT5U.machines.invert_filter.tooltip";
    public boolean bInvertFilter = false;

    public GT_MetaTileEntity_FilterBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public GT_MetaTileEntity_FilterBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public GT_MetaTileEntity_FilterBase(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_FilterBase(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bInvertFilter", this.bInvertFilter);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.bInvertFilter = aNBT.getBoolean("bInvertFilter");
    }

    protected void addInvertFilterButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> bInvertFilter,
                val -> bInvertFilter = val,
                GT_UITextures.OVERLAY_BUTTON_INVERT_FILTER,
                INVERT_FILTER_TOOLTIP,
                3));
    }
}
