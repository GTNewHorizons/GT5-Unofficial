package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class MTERedstoneStrengthScale extends MTERedstoneStrengthDisplay {

    public static TexturesGtBlock.CustomIcon[] sIconList = new TexturesGtBlock.CustomIcon[32];

    static {
        for (int i = 0; i < 32; i++) {
            sIconList[i] = new CustomIcon("TileEntities/gt4/redstone/Scale/" + i);
        }
    }

    public MTERedstoneStrengthScale(int aID) {
        super(aID, "redstone.display.scale", "Redstone Scale", "Redstone Strength on a Scale");
    }

    public MTERedstoneStrengthScale(final String aName, String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTERedstoneStrengthScale(this.mName, mDescriptionArray, this.mTextures);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) mType = (byte) ((mType + 1) % 2);
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1],
                new GTRenderedTexture(sIconList[mType * 16 + mRedstoneStrength]) };
        }
        return this.mTextures[(aActive || hasRedstoneSignal() ? 5 : 0) + (side == facing ? 0
            : side == facing.getOpposite() ? 1
                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex + 1];
    }
}
