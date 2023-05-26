package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class GT_MetaTileEntity_RedstoneLamp extends GT_MetaTileEntity_RedstoneBase {

    public byte mRedstoneStrength = 0, mType = 0;
    public static TexturesGtBlock.CustomIcon[] sIconList = new TexturesGtBlock.CustomIcon[2];

    static {
        sIconList[0] = new CustomIcon("TileEntities/gt4/redstone/Lamp/off");
        sIconList[1] = new CustomIcon("TileEntities/gt4/redstone/Lamp/on");
    }

    public GT_MetaTileEntity_RedstoneLamp(int aID) {
        super(aID, "redstone.lamp", "Redstone Controlled Lamp", 0, 0, "Redstone Controlled Lamp");
    }

    public GT_MetaTileEntity_RedstoneLamp(final String aName, String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, 0, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_RedstoneLamp(this.mName, mDescriptionArray, this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (getBaseMetaTileEntity().isAllowedToWork() && getBaseMetaTileEntity().isServerSide()) {
            getBaseMetaTileEntity().setLightValue(getBaseMetaTileEntity().getStrongestRedstone());
            getBaseMetaTileEntity().setActive(getBaseMetaTileEntity().getStrongestRedstone() > 0);
        }
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getSides(i);
            rTextures[1][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[(aActive ? 1 : 0)][aColorIndex + 1];
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(sIconList[0]) };
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { new GT_RenderedTexture(sIconList[1]) };
    }
}
