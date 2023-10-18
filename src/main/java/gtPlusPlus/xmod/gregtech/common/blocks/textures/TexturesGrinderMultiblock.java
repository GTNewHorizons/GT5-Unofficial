package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks5;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IsaMill;

public class TexturesGrinderMultiblock {

    private static CustomIcon GT8_1_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE1");
    private static CustomIcon GT8_1 = new CustomIcon("iconsets/Grinder/GRINDER1");
    private static CustomIcon GT8_2_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE2");
    private static CustomIcon GT8_2 = new CustomIcon("iconsets/Grinder/GRINDER2");
    private static CustomIcon GT8_3_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE3");
    private static CustomIcon GT8_3 = new CustomIcon("iconsets/Grinder/GRINDER3");
    private static CustomIcon GT8_4_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE4");
    private static CustomIcon GT8_4 = new CustomIcon("iconsets/Grinder/GRINDER4");
    private static CustomIcon GT8_5_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE5");
    private static CustomIcon GT8_5 = new CustomIcon("iconsets/Grinder/GRINDER5");
    private static CustomIcon GT8_6_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE6");
    private static CustomIcon GT8_6 = new CustomIcon("iconsets/Grinder/GRINDER6");
    private static CustomIcon GT8_7_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE7");
    private static CustomIcon GT8_7 = new CustomIcon("iconsets/Grinder/GRINDER7");
    private static CustomIcon GT8_8_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE8");
    private static CustomIcon GT8_8 = new CustomIcon("iconsets/Grinder/GRINDER8");
    private static CustomIcon GT8_9_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE9");
    private static CustomIcon GT8_9 = new CustomIcon("iconsets/Grinder/GRINDER9");

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

    CustomIcon[] GRINDER = new CustomIcon[] { frontFace_0, frontFace_1, frontFace_2, frontFace_3, frontFace_4,
            frontFace_5, frontFace_6, frontFace_7, frontFace_8 };

    CustomIcon[] GRINDER_ACTIVE = new CustomIcon[] { frontFaceActive_0, frontFaceActive_1, frontFaceActive_2,
            frontFaceActive_3, frontFaceActive_4, frontFaceActive_5, frontFaceActive_6, frontFaceActive_7,
            frontFaceActive_8 };

    private static int isIsaControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection side) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity tTile)) return 0;
        if (tTile.getMetaTileEntity() instanceof GregtechMetaTileEntity_IsaMill && tTile.getFrontFacing() == side)
            return tTile.isActive() ? 1 : 2;
        return 0;
    }

    public IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
            final int ordinalSide, final GregtechMetaCasingBlocks5 ii) {
        final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        if (tMeta != 1) {
            return GregtechMetaCasingBlocks5.getStaticIcon(ordinalSide, tMeta);
        }
        int tInvertLeftRightMod = ordinalSide % 2 * 2 - 1;
        switch (ordinalSide / 2) {
            case 0 -> {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        if (isIsaControllerWithSide(aWorld, xCoord + j, yCoord, zCoord + i, side) != 0) {
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
                        if (isIsaControllerWithSide(aWorld, xCoord + j, yCoord + i, zCoord, side) != 0) {
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
                        if (isIsaControllerWithSide(aWorld, xCoord, yCoord + i, zCoord + j, side) != 0) {
                            IMetaTileEntity tMetaTileEntity = ((IGregTechTileEntity) aWorld
                                    .getTileEntity(xCoord, yCoord + i, zCoord + j)).getMetaTileEntity();
                            return getIconByIndex(tMetaTileEntity, 4 + i * 3 + j * tInvertLeftRightMod);
                        }
                    }
                }
            }
        }
        return TexturesGtBlock.TEXTURE_CASING_GRINDING_MILL.getIcon();
    }

    public boolean isCentrifugeRunning(IMetaTileEntity aTile) {
        if (aTile == null) {
            return false;
        } else {
            return aTile.getBaseMetaTileEntity().isActive();
        }
    }

    public IIcon getIconByIndex(IMetaTileEntity aMetaTileEntity, int aIndex) {
        if (isCentrifugeRunning(aMetaTileEntity)) {
            return this.GRINDER_ACTIVE[aIndex].getIcon();
        }

        return this.GRINDER[aIndex].getIcon();
    }
}
