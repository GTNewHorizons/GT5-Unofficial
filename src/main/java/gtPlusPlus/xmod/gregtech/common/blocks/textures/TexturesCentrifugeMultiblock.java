package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialCentrifuge;

public class TexturesCentrifugeMultiblock {

    private static CustomIcon GT8_1_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE1");
    private static CustomIcon GT8_1 = new CustomIcon("iconsets/LARGECENTRIFUGE1");
    private static CustomIcon GT8_2_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE2");
    private static CustomIcon GT8_2 = new CustomIcon("iconsets/LARGECENTRIFUGE2");
    private static CustomIcon GT8_3_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE3");
    private static CustomIcon GT8_3 = new CustomIcon("iconsets/LARGECENTRIFUGE3");
    private static CustomIcon GT8_4_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE4");
    private static CustomIcon GT8_4 = new CustomIcon("iconsets/LARGECENTRIFUGE4");
    private static CustomIcon GT8_5_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE5");
    private static CustomIcon GT8_5 = new CustomIcon("iconsets/LARGECENTRIFUGE5");
    private static CustomIcon GT8_6_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE6");
    private static CustomIcon GT8_6 = new CustomIcon("iconsets/LARGECENTRIFUGE6");
    private static CustomIcon GT8_7_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE7");
    private static CustomIcon GT8_7 = new CustomIcon("iconsets/LARGECENTRIFUGE7");
    private static CustomIcon GT8_8_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE8");
    private static CustomIcon GT8_8 = new CustomIcon("iconsets/LARGECENTRIFUGE8");
    private static CustomIcon GT8_9_Active = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE9");
    private static CustomIcon GT8_9 = new CustomIcon("iconsets/LARGECENTRIFUGE9");

    private static CustomIcon frontFace_0 = (GT8_1);
    private static CustomIcon frontFaceActive_0 = (GT8_1_Active);
    private static CustomIcon frontFace_1 = (GT8_2);
    private static CustomIcon frontFaceActive_1 = (GT8_2_Active);
    private static CustomIcon frontFace_2 = (GT8_3);
    private static CustomIcon frontFaceActive_2 = (GT8_3_Active);
    private static CustomIcon frontFace_3 = (GT8_4);
    private static CustomIcon frontFaceActive_3 = (GT8_4_Active);
    private static CustomIcon frontFace_4 = (GT8_5);
    private static CustomIcon frontFaceActive_4 = (GT8_5_Active);
    private static CustomIcon frontFace_5 = (GT8_6);
    private static CustomIcon frontFaceActive_5 = (GT8_6_Active);
    private static CustomIcon frontFace_6 = (GT8_7);
    private static CustomIcon frontFaceActive_6 = (GT8_7_Active);
    private static CustomIcon frontFace_7 = (GT8_8);
    private static CustomIcon frontFaceActive_7 = (GT8_8_Active);
    private static CustomIcon frontFace_8 = (GT8_9);
    private static CustomIcon frontFaceActive_8 = (GT8_9_Active);

    CustomIcon[] CENTRIFUGE = new CustomIcon[] { frontFace_0, frontFace_1, frontFace_2, frontFace_3, frontFace_4,
            frontFace_5, frontFace_6, frontFace_7, frontFace_8 };

    CustomIcon[] CENTRIFUGE_ACTIVE = new CustomIcon[] { frontFaceActive_0, frontFaceActive_1, frontFaceActive_2,
            frontFaceActive_3, frontFaceActive_4, frontFaceActive_5, frontFaceActive_6, frontFaceActive_7,
            frontFaceActive_8 };

    public IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
            final ForgeDirection side, final GregtechMetaCasingBlocks thisBlock) {
        return this.handleCasingsGT58(aWorld, xCoord, yCoord, zCoord, side, thisBlock);
    }

    private static int isCentrifugeControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ,
            ForgeDirection side) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity tTile)) return 0;
        if (tTile.getMetaTileEntity() instanceof GregtechMetaTileEntity_IndustrialCentrifuge
                && tTile.getFrontFacing() == side)
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
            return aTile.getBaseMetaTileEntity().isActive();
        }
    }

    public boolean isUsingAnimatedTexture(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity != null) {
            if (aMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
                return ((GregtechMetaTileEntity_IndustrialCentrifuge) aMetaTileEntity).usingAnimations();
            }
        }
        return false;
    }

    public IIcon getIconByIndex(IMetaTileEntity aMetaTileEntity, int aIndex) {
        if (isUsingAnimatedTexture(aMetaTileEntity)) {
            if (isCentrifugeRunning(aMetaTileEntity)) {
                return this.CENTRIFUGE_ACTIVE[aIndex].getIcon();
            }
        }
        return this.CENTRIFUGE[aIndex].getIcon();
    }
}
