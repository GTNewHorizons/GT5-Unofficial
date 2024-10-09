package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialCentrifuge;

public class TexturesCentrifugeMultiblock {

    public IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final ForgeDirection side, final GregtechMetaCasingBlocks thisBlock) {
        return this.handleCasingsGT58(aWorld, xCoord, yCoord, zCoord, side, thisBlock);
    }

    private static int isCentrifugeControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ,
        ForgeDirection side) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity tTile)) return 0;
        if (tTile.getMetaTileEntity() instanceof MTEIndustrialCentrifuge && tTile.getFrontFacing() == side)
            return tTile.isActive() ? 1 : 2;
        return 0;
    }

    public IIcon handleCasingsGT58(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final ForgeDirection side, final GregtechMetaCasingBlocks thisBlock) {
        final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        final int ordinalSide = side.ordinal();
        if (tMeta != 0) {
            return CasingTextureHandler.getIcon(ordinalSide, tMeta);
        }

        int tInvertLeftRightMod = ordinalSide % 2 * 2 - 1;
        switch (ordinalSide / 2) {
            case 0 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        if (isCentrifugeControllerWithSide(aWorld, xCoord + j, yCoord, zCoord + i, side) != 0) {
                            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aWorld
                                .getTileEntity(xCoord + j, yCoord, zCoord + i)).getMetaTileEntity();
                            return getIconByIndex(tMetaTileEntity, 4 - i * 3 - j);
                        }
                    }
                }
            }
            case 1 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        if (isCentrifugeControllerWithSide(aWorld, xCoord + j, yCoord + i, zCoord, side) != 0) {
                            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aWorld
                                .getTileEntity(xCoord + j, yCoord + i, zCoord)).getMetaTileEntity();
                            return getIconByIndex(tMetaTileEntity, 4 + i * 3 - j * tInvertLeftRightMod);
                        }
                    }
                }
            }
            case 2 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        if (isCentrifugeControllerWithSide(aWorld, xCoord, yCoord + i, zCoord + j, side) != 0) {
                            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aWorld
                                .getTileEntity(xCoord, yCoord + i, zCoord + j)).getMetaTileEntity();
                            return getIconByIndex(tMetaTileEntity, 4 + i * 3 + j * tInvertLeftRightMod);
                        }
                    }
                }
            }
        }
        return TexturesGtBlock.Casing_Material_Centrifuge.getIcon();
    }

    public boolean isCentrifugeRunning(IMetaTileEntity aTile) {
        if (aTile == null) {
            return false;
        } else {
            return aTile.getBaseMetaTileEntity()
                .isActive();
        }
    }

    public boolean isUsingAnimatedTexture(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity != null) {
            if (aMetaTileEntity instanceof MTEIndustrialCentrifuge) {
                return ((MTEIndustrialCentrifuge) aMetaTileEntity).usingAnimations();
            }
        }
        return false;
    }

    public IIcon getIconByIndex(IMetaTileEntity aMetaTileEntity, int aIndex) {
        if (isUsingAnimatedTexture(aMetaTileEntity)) {
            if (isCentrifugeRunning(aMetaTileEntity)) {
                return TexturesGtBlock.CENTRIFUGEACTIVE[aIndex].getIcon();
            }
        }
        return TexturesGtBlock.CENTRIFUGE[aIndex].getIcon();
    }
}
