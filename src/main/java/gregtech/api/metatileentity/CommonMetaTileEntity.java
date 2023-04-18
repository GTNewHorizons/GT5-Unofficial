package gregtech.api.metatileentity;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.modularui.IBindPlayerInventoryUI;
import gregtech.api.interfaces.modularui.IGetTitleColor;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;

public abstract class CommonMetaTileEntity extends CoverableTileEntity implements IGregTechTileEntity {

    protected boolean mNeedsBlockUpdate = true, mNeedsUpdate = true, mSendClientData = false, mInventoryChanged = false;

    protected boolean createNewMetatileEntity(short aID) {
        if (aID <= 0 || aID >= GregTech_API.METATILEENTITIES.length || GregTech_API.METATILEENTITIES[aID] == null) {
            GT_Log.err.println("MetaID " + aID + " not loadable => locking TileEntity!");
        } else {
            if (hasValidMetaTileEntity()) getMetaTileEntity().setBaseMetaTileEntity(null);
            GregTech_API.METATILEENTITIES[aID].newMetaEntity(this)
                .setBaseMetaTileEntity(this);
            mTickTimer = 0;
            mID = aID;
            return true;
        }
        return false;
    }

    protected void saveMetaTileNBT(NBTTagCompound aNBT) {
        try {
            if (hasValidMetaTileEntity()) {
                aNBT.setInteger("nbtVersion", GT_Mod.NBT_VERSION);
                final NBTTagList tItemList = new NBTTagList();
                for (int i = 0; i < getMetaTileEntity().getRealInventory().length; i++) {
                    final ItemStack tStack = getMetaTileEntity().getRealInventory()[i];
                    if (tStack != null) {
                        final NBTTagCompound tTag = new NBTTagCompound();
                        tTag.setInteger("IntSlot", i);
                        tStack.writeToNBT(tTag);
                        tItemList.appendTag(tTag);
                    }
                }
                aNBT.setTag("Inventory", tItemList);

                try {
                    getMetaTileEntity().saveNBTData(aNBT);
                } catch (Throwable e) {
                    GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.");
                    GT_Mod.logStackTrace(e);
                }
            }
        } catch (Throwable e) {
            GT_FML_LOGGER.error("Encountered CRITICAL ERROR while saving MetaTileEntity.");
            GT_Mod.logStackTrace(e);
        }
    }

    protected void loadMetaTileNBT(NBTTagCompound aNBT) {
        final int nbtVersion = aNBT.getInteger("nbtVersion");
        if (mID != 0 && createNewMetatileEntity(mID)) {
            final NBTTagList tItemList = aNBT.getTagList("Inventory", 10);
            for (int i = 0; i < tItemList.tagCount(); i++) {
                final NBTTagCompound tTag = tItemList.getCompoundTagAt(i);
                final int tSlot = migrateInventoryIndex(tTag.getInteger("IntSlot"), nbtVersion);
                if (tSlot >= 0 && tSlot < getMetaTileEntity().getRealInventory().length) {
                    getMetaTileEntity().getRealInventory()[tSlot] = GT_Utility.loadItem(tTag);
                }
            }

            try {
                getMetaTileEntity().loadNBTData(aNBT);
            } catch (Throwable e) {
                GT_FML_LOGGER.error("Encountered Exception while loading MetaTileEntity.");
                GT_Mod.logStackTrace(e);
            }
        }
    }

    /**
     * Shifts the machine Inventory index according to the change in Input/Output Slots. Default implementation does not
     * do anything to the slotIndex.
     */
    protected int migrateInventoryIndex(int slotIndex, int nbtVersion) {
        return slotIndex;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        mInventoryChanged = true;
    }

    @Override
    public boolean hasInventoryBeenModified() {
        return mInventoryChanged;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        if (canAccessData()) return getMetaTileEntity().isValidSlot(aIndex);
        return false;
    }

    @Override
    public Packet getDescriptionPacket() {
        issueClientUpdate();
        return null;
    }

    @Override
    public void issueTextureUpdate() {
        mNeedsUpdate = true;
    }

    @Override
    public void issueClientUpdate() {
        mSendClientData = true;
    }

    @Override
    public void issueBlockUpdate() {
        mNeedsBlockUpdate = true;
    }

    @Override
    public boolean isValidFacing(ForgeDirection side) {
        if (canAccessData()) return getMetaTileEntity().isFacingValid(side);
        return false;
    }

    protected boolean canAccessData() {
        return !isDead && hasValidMetaTileEntity();
    }

    protected abstract boolean hasValidMetaTileEntity();

    @Override
    public String[] getDescription() {
        if (canAccessData()) return getMetaTileEntity().getDescription();
        return new String[0];
    }

    @Override
    public boolean isStillValid() {
        return hasValidMetaTileEntity();
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aCoverID) {
        return hasValidMetaTileEntity() && getMetaTileEntity().allowCoverOnSide(side, aCoverID);
    }

    @Override
    public void issueCoverUpdate(ForgeDirection side) {
        super.issueCoverUpdate(side);
        issueClientUpdate();
    }

    /*
     * IC2 Energy Compat
     */
    @Override
    public boolean shouldJoinIc2Enet() {
        final IMetaTileEntity meta = getMetaTileEntity();
        return meta != null && meta.shouldJoinIc2Enet();
    }

    /*
     * Modular UI Support
     */

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IAddUIWidgets) {
            ((IAddUIWidgets) getMetaTileEntity()).addUIWidgets(builder, buildContext);
            return;
        }
        super.addUIWidgets(builder, buildContext);
    }

    @Override
    public void bindPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IBindPlayerInventoryUI) {
            ((IBindPlayerInventoryUI) getMetaTileEntity()).bindPlayerInventoryUI(builder, buildContext);
            return;
        }
        super.bindPlayerInventoryUI(builder, buildContext);
    }

    @Override
    public IConfigurationCircuitSupport getConfigurationCircuitSupport() {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IConfigurationCircuitSupport) {
            return (IConfigurationCircuitSupport) getMetaTileEntity();
        }
        return null;
    }

    @Override
    public ItemStackHandler getInventoryHandler() {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getInventoryHandler();
        }
        return null;
    }

    @Override
    public boolean useModularUI() {
        return hasValidMetaTileEntity() && getMetaTileEntity().useModularUI();
    }

    @Override
    public String getLocalName() {
        if (hasValidMetaTileEntity()) return getMetaTileEntity().getLocalName();
        return super.getLocalName();
    }

    @Override
    protected int getGUIWidth() {
        if (hasValidMetaTileEntity()) return getMetaTileEntity().getGUIWidth();

        return super.getGUIWidth();
    }

    @Override
    protected int getGUIHeight() {
        if (hasValidMetaTileEntity()) return getMetaTileEntity().getGUIHeight();

        return super.getGUIHeight();
    }

    @Override
    protected boolean doesBindPlayerInventory() {
        if (hasValidMetaTileEntity()) return getMetaTileEntity().doesBindPlayerInventory();

        return super.doesBindPlayerInventory();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IAddGregtechLogo) {
            ((IAddGregtechLogo) getMetaTileEntity()).addGregTechLogo(builder);
            return;
        }
        super.addGregTechLogo(builder);
    }

    @Override
    public ItemStack getStackForm(long aAmount) {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getStackForm(aAmount);
        }
        return super.getStackForm(aAmount);
    }

    @Override
    public int getTitleColor() {
        if (hasValidMetaTileEntity() && getMetaTileEntity() instanceof IGetTitleColor) {
            return ((IGetTitleColor) getMetaTileEntity()).getTitleColor();
        }
        return super.getTitleColor();
    }

    @Override
    public int getGUIColorization() {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getGUIColorization();
        }
        return super.getGUIColorization();
    }

    @Override
    protected int getTextColorOrDefault(String textType, int defaultColor) {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getTextColorOrDefault(textType, defaultColor);
        }
        return defaultColor;
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        if (hasValidMetaTileEntity()) {
            return getMetaTileEntity().getGUITextureSet();
        }
        return super.getGUITextureSet();
    }
}
