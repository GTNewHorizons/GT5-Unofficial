package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class GregtechMetaTransformerHiAmp extends GT_MetaTileEntity_Transformer {

    private boolean mHalfMode = false;

    public GregtechMetaTransformerHiAmp(int aID, String aName, String aNameRegional, int aTier, String aDescription) {
        super(aID, aName, aNameRegional, aTier, aDescription);
    }

    public GregtechMetaTransformerHiAmp(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public long maxEUStore() {
        return ((512L + gregtech.api.enums.GT_Values.V[(this.mTier + 1)] * 2L) * 8);
    }

    @Override
    public long maxAmperesOut() {
        if (this.mHalfMode) {
            return ((getBaseMetaTileEntity().isAllowedToWork()) ? 8L : 2L);
        }
        return ((getBaseMetaTileEntity().isAllowedToWork()) ? 16L : 4L);
    }

    @Override
    public long maxAmperesIn() {
        if (this.mHalfMode) {
            return ((getBaseMetaTileEntity().isAllowedToWork()) ? 2L : 8L);
        }
        return ((getBaseMetaTileEntity().isAllowedToWork()) ? 4L : 16L);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
            rTextures[1][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
            rTextures[2][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier] };
            rTextures[3][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier + 1] };
            rTextures[4][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier + 1] };
            rTextures[5][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI[mTier + 1] };
            rTextures[6][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
            rTextures[7][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
            rTextures[8][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier] };
            rTextures[9][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier + 1] };
            rTextures[10][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier + 1] };
            rTextures[11][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                    Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier + 1] };
        }
        return rTextures;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTransformerHiAmp(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(
                this.mDescriptionArray,
                "Accepts 4A and outputs 16A",
                "Toggle 2A/8A half-mode with Screwdriver",
                CORE.GT_Tooltip.get());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("mHalfMode", this.mHalfMode);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mHalfMode = aNBT.getBoolean("mHalfMode");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.mHalfMode = !mHalfMode;
        if (this.mHalfMode) {
            PlayerUtils.messagePlayer(aPlayer, "Transformer is now running at 2A:8A in/out Ratio.");
        } else {
            PlayerUtils.messagePlayer(aPlayer, "Transformer is now running at 4A:16A in/out Ratio.");
        }
    }
}
