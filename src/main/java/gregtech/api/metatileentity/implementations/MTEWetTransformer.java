package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import tectech.util.CommonValues;

public class MTEWetTransformer extends MTETransformer {

    private boolean mHalfMode = false;

    public MTEWetTransformer(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEWetTransformer(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEWetTransformer(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];
        for (byte b = -1; b < 16; b++) {
            rTextures[0][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier] };
            rTextures[1][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier] };
            rTextures[2][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier] };
            rTextures[3][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1] };
            rTextures[4][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1] };
            rTextures[5][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1] };
            rTextures[6][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier] };
            rTextures[7][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier] };
            rTextures[8][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier] };
            rTextures[9][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 1] };
            rTextures[10][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 1] };
            rTextures[11][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 1] };
        }
        return rTextures;
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalizedFormattedWithWarped(
            super.getDescription()[0],
            "gt.blockmachines.transformer_advanced.desc",
            CommonValues.TEC_MARK_GENERAL,
            16,
            64);
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier + 1] * 128L;
    }

    @Override
    public long maxAmperesOut() {
        if (mHalfMode) {
            return getBaseMetaTileEntity().isAllowedToWork() ? 32 : 8;
        }
        return getBaseMetaTileEntity().isAllowedToWork() ? 64 : 16;
    }

    @Override
    public long maxAmperesIn() {
        if (mHalfMode) {
            return getBaseMetaTileEntity().isAllowedToWork() ? 8 : 32;
        }
        return getBaseMetaTileEntity().isAllowedToWork() ? 16 : 64;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mHalfMode", mHalfMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mHalfMode = aNBT.getBoolean("mHalfMode");
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        this.mHalfMode = !mHalfMode;
        if (this.mHalfMode) {
            GTUtility.sendChatToPlayer(aPlayer, "Transformer is now running at 8A:32A in/out Ratio.");
        } else {
            GTUtility.sendChatToPlayer(aPlayer, "Transformer is now running at 16A:64A in/out Ratio.");
        }
    }
}
