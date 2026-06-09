package kubatech.tileentity.gregtech.hatch;

import static kubatech.loaders.ArcFurnaceLoader.ARC_FURNACE_ELECTRODE;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.common.gui.modularui.hatch.MTEElectrodeHatchGui;

public class MTEElectrodeHatch extends MTEHatchInputBus {

    private boolean hasBeenUpdated = false;

    public MTEElectrodeHatch(int id, String name, String nameRegional) {
        super(id, name, nameRegional, VoltageIndex.IV, 1, new String[] { "Holds an electrode." });
    }

    public MTEElectrodeHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEElectrodeHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public int getCircuitSlot() {
        return -1;
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        if (itemStack == null) return true;
        return itemStack.getItem() == ARC_FURNACE_ELECTRODE;
    }

    @Override
    public void onContentsChanged(int slot) {
        hasBeenUpdated = true;
    }

    public boolean hasJustBeenUpdated() {
        if (hasBeenUpdated) {
            hasBeenUpdated = false;
            return true;
        }
        return false;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEElectrodeHatchGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public String[] getDescription() {
        return mDescriptionArray;
    }
}
