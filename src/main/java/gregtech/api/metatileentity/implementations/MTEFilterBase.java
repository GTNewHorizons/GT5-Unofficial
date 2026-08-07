package gregtech.api.metatileentity.implementations;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.common.gui.modularui.singleblock.base.MTEFilterBaseGui;

public abstract class MTEFilterBase extends MTEBuffer {

    public static final int FILTER_SLOT_INDEX = 9;
    protected static final int NUM_INVENTORY_SLOTS = 9;
    protected boolean invertFilter = false;

    public MTEFilterBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public MTEFilterBase(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public boolean isInvertFilter() {
        return invertFilter;
    }

    public void setInvertFilter(boolean invertFilter) {
        this.invertFilter = invertFilter;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < NUM_INVENTORY_SLOTS;
    }

    @Override
    protected int getMovableInventoryEnd() {
        return NUM_INVENTORY_SLOTS;
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
    public int getRedstoneOutput() {
        if (!bRedstoneIfFull) {
            return 0;
        }
        int redstoneOutput = getEmptySlots();
        if (!bInvert) redstoneOutput = NUM_INVENTORY_SLOTS - redstoneOutput;
        return redstoneOutput;
    }

    public int getEmptySlots() {
        int emptySlots = 0;
        for (int i = 0; i < NUM_INVENTORY_SLOTS; i++) {
            if (mInventory[i] == null) ++emptySlots;
        }
        return emptySlots;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEFilterBaseGui<>(this).build(guiData, syncManager, uiSettings);
    }
}
