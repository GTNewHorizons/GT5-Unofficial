package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import tectech.util.CommonValues;

@IMetaTileEntity.SkipGenerateDescription
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
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier + 1] };
            rTextures[1][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier + 1] };
            rTextures[2][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier + 1] };
            rTextures[3][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 2] };
            rTextures[4][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 2] };
            rTextures[5][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 2] };
            rTextures[6][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier + 1] };
            rTextures[7][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier + 1] };
            rTextures[8][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier + 1] };
            rTextures[9][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 2] };
            rTextures[10][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 2] };
            rTextures[11][b + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 2] };
        }
        return rTextures;
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalizedFormattedWithWarped(
            "gt.blockmachines.transformer_advanced.desc",
            super.getDescription()[0],
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
            return getBaseMetaTileEntity().isAllowedToWork() ? 10 : 40;
        }
        return getBaseMetaTileEntity().isAllowedToWork() ? 20 : 80;
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
