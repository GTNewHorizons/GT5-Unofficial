package gregtech.common.tileentities.storage;

import static gregtech.api.enums.Textures.BlockIcons.LOCKERS;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_IN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_LOCKER;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Locker extends GT_MetaTileEntity_TieredMachineBlock {

    public byte mType = 0;

    public GT_MetaTileEntity_Locker(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 4, "Stores and recharges Armor");
    }

    public GT_MetaTileEntity_Locker(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Locker(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[mDescriptionArray.length + 1];
        System.arraycopy(mDescriptionArray, 0, desc, 0, mDescriptionArray.length);
        desc[mDescriptionArray.length] = "Click with Screwdriver to change Style";
        return desc;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[3][17][];
        for (byte i = -1; i < 16; i = (byte) (i + 1)) {
            ITexture[] tmp0 = { MACHINE_CASINGS[this.mTier][(i + 1)] };
            rTextures[0][(i + 1)] = tmp0;
            ITexture[] tmp1 = { MACHINE_CASINGS[this.mTier][(i + 1)], OVERLAYS_ENERGY_IN[this.mTier] };
            rTextures[1][(i + 1)] = tmp1;
            ITexture[] tmp2 = { MACHINE_CASINGS[this.mTier][(i + 1)], TextureFactory.of(OVERLAY_LOCKER) };
            rTextures[2][(i + 1)] = tmp2;
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection aSide, ForgeDirection aFacing,
        byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[] { this.mTextures[2][(aColorIndex + 1)][0], this.mTextures[2][(aColorIndex + 1)][1],
                LOCKERS[Math.abs(this.mType % LOCKERS.length)] };
        }
        return this.mTextures[0][(aColorIndex + 1)];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Locker(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return aFacing > 1;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection aSide) {
        return aSide == getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long maxEUStore() {
        return gregtech.api.enums.GT_Values.V[this.mTier] * maxAmperesIn();
    }

    @Override
    public long maxEUInput() {
        return gregtech.api.enums.GT_Values.V[this.mTier];
    }

    @Override
    public long maxAmperesIn() {
        return this.mInventory.length * 2L;
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return getBaseMetaTileEntity().isAllowedToWork() ? this.mInventory.length : 0;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("mType", this.mType);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mType = aNBT.getByte("mType");
    }

    @Override
    public void onValueUpdate(byte aValue) {
        this.mType = aValue;
    }

    @Override
    public byte getUpdateData() {
        return this.mType;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == 16) {
            GT_Utility.doSoundAtClient(SoundResource.RANDOM_CLICK, 1, 1.0F);
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
            this.mType = ((byte) (this.mType + 1));
        }
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection aSide, GT_ItemStack aStack) {
        return aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection aSide,
        float aX, float aY, float aZ) {
        if ((aBaseMetaTileEntity.isServerSide()) && (aSide == aBaseMetaTileEntity.getFrontFacing())) {
            for (int i = 0; i < 4; i++) {
                ItemStack tSwapStack = this.mInventory[i];
                this.mInventory[i] = aPlayer.inventory.armorInventory[i];
                aPlayer.inventory.armorInventory[i] = tSwapStack;
            }
            aPlayer.inventoryContainer.detectAndSendChanges();
            sendSound((byte) 16);
        }
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection aSide,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection aSide,
        ItemStack aStack) {
        return false;
    }
}
