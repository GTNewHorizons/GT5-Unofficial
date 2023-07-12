package gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class GT_MetaTileEntity_FilterBase extends GT_MetaTileEntity_Buffer implements IAddUIWidgets {
    private static final String INVERT_FILTER_TOOLTIP = "GT5U.machines.invert_filter.tooltip";
    protected static final int FILTER_SLOT_INDEX = 9;
    protected static final int NUM_INVENTORY_SLOTS = 9;
    private static final String EMIT_REDSTONE_GRADUALLY_TOOLTIP = "GT5U" +
        ".machines.emit_redstone_gradually.tooltip";
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
        return aIndex < NUM_INVENTORY_SLOTS;
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
    protected void handleRedstoneOutput(IGregTechTileEntity aBaseMetaTileEntity) {
        if (bRedstoneIfFull) {
            int emptySlots = 0;
            for (int i = 0; i < NUM_INVENTORY_SLOTS; i++) {
                if (mInventory[i] == null) ++emptySlots;
            }
            if (!bInvert) emptySlots = NUM_INVENTORY_SLOTS - emptySlots;
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) emptySlots);
            }
        } else {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) 0);
            }
        }
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        addEmitEnergyButton(builder);
        addEmitRedstoneGraduallyButton(builder);
        addInvertRedstoneButton(builder);
        addInvertFilterButton(builder);
    }

    private void addEmitRedstoneGraduallyButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> bRedstoneIfFull,
                val -> bRedstoneIfFull = val,
                GT_UITextures.OVERLAY_BUTTON_EMIT_REDSTONE,
                EMIT_REDSTONE_GRADUALLY_TOOLTIP,
                1));
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
