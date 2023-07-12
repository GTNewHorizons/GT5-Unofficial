package gregtech.api.metatileentity.implementations;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;

public abstract class GT_MetaTileEntity_FilterBase extends GT_MetaTileEntity_Buffer implements IAddUIWidgets {

    private static final String INVERT_FILTER_TOOLTIP = "GT5U.machines.invert_filter.tooltip";
    protected static final int FILTER_SLOT_INDEX = 9;
    protected boolean invertFilter = false;

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
    public boolean isValidSlot(int aIndex) {
        return aIndex < FILTER_SLOT_INDEX;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bInvertFilter", this.invertFilter);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.invertFilter = aNBT.getBoolean("bInvertFilter");
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        addEmitEnergyButton(builder);
        addEmitRedstoneButton(builder);
        addInvertRedstoneButton(builder);
        addInvertFilterButton(builder);
    }

    private void addInvertFilterButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> invertFilter,
                val -> invertFilter = val,
                GT_UITextures.OVERLAY_BUTTON_INVERT_FILTER,
                INVERT_FILTER_TOOLTIP,
                3));
    }
}
