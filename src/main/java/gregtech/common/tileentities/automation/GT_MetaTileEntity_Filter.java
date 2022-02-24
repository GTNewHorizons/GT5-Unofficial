package gregtech.common.tileentities.automation;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Buffer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_Filter;
import gregtech.common.gui.GT_GUIContainer_Filter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_FILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_FILTER_GLOW;

public class GT_MetaTileEntity_Filter extends GT_MetaTileEntity_Buffer {
    public boolean bIgnoreNBT = false;
    public boolean bInvertFilter = false;

    public GT_MetaTileEntity_Filter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 19, new String[]{
                "Filters up to 9 different Items",
                "Use Screwdriver to regulate output stack size",
                "Does not consume energy to move Item"});
    }

    public GT_MetaTileEntity_Filter(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }
    
    public GT_MetaTileEntity_Filter(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Filter(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
                TextureFactory.of(AUTOMATION_FILTER),
                TextureFactory.builder().addIcon(AUTOMATION_FILTER_GLOW).glow().build());
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 9;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Filter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_Filter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bInvertFilter", this.bInvertFilter);
        aNBT.setBoolean("bIgnoreNBT", this.bIgnoreNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.bInvertFilter = aNBT.getBoolean("bInvertFilter");
        this.bIgnoreNBT = aNBT.getBoolean("bIgnoreNBT");
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (!super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) {
            return false;
        }
        if (this.bInvertFilter) {
            for (byte i = 9; i < 18; i = (byte) (i + 1)) {
                if (GT_Utility.areStacksEqual(this.mInventory[i], aStack, this.bIgnoreNBT)) {
                    return false;
                }
            }
            return true;
        }
        return GT_Utility.areStacksEqual(this.mInventory[(aIndex + 9)], aStack, this.bIgnoreNBT);
    }

    @Override
    protected void handleRedstoneOutput(IGregTechTileEntity aBaseMetaTileEntity) {
        if (bRedstoneIfFull) {
            int emptySlots = 0;
            for (int i = 0; i < 9; i++) {
                if (mInventory[i] == null)
                    ++emptySlots;
            }
            if (!bInvert)
                emptySlots = 9 - emptySlots;
            for (byte b = 0; b < 6; b++)
                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(b, (byte) emptySlots);
        }
        else
            for (byte b = 0; b < 6; b++)
                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(b, (byte) 0);
    }
}
