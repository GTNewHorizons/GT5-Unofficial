package goodgenerator.blocks.regularBlock;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_LargeTurbineBase;
import goodgenerator.client.render.BlockRenderHandler;
import goodgenerator.main.GoodGenerator;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

public class TurbineCasing extends Casing implements ITextureBlock {

    public static IIconContainer[][] turbineShape = new IIconContainer[3][9];
    public IIconContainer base;

    static {
        for (int i = 0; i < 3; i++) for (int j = 1; j <= 9; j++)
            turbineShape[i][j - 1] = new Textures.BlockIcons.CustomIcon("icons/turbines/TURBINE_" + i + "" + j);
    }

    public TurbineCasing(String name, String texture) {
        super(name, new String[] { GoodGenerator.MOD_ID + ":" + texture });
        base = new Textures.BlockIcons.CustomIcon("icons/" + texture);
    }

    private static int isTurbineControllerWithSide(IBlockAccess aWorld, int aX, int aY, int aZ, ForgeDirection side) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof IGregTechTileEntity)) return 0;
        IGregTechTileEntity tTile = (IGregTechTileEntity) tTileEntity;
        if (tTile.getMetaTileEntity() instanceof GT_MetaTileEntity_LargeTurbineBase && tTile.getFrontFacing() == side) {
            if (tTile.isActive()) return 1;
            return ((GT_MetaTileEntity_LargeTurbineBase) tTile.getMetaTileEntity()).hasTurbine() ? 2 : 3;
        }
        return 0;
    }

    public ITexture[] getTurbineCasing(int iconIndex, boolean active, boolean hasTurbine) {
        int states = active ? 0 : hasTurbine ? 1 : 2;
        return new ITexture[] { TextureFactory.of(base), TextureFactory.of(turbineShape[states][iconIndex]) };
    }

    @Override
    public ITexture[] getTexture(Block aBlock, ForgeDirection side, IBlockAccess aWorld, int xCoord, int yCoord,
            int zCoord) {
        final int ordinalSide = side.ordinal();
        int tInvertLeftRightMod = ordinalSide % 2 * 2 - 1;
        switch (ordinalSide / 2) {
            case 0:
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord + j, yCoord, zCoord + i, side)) != 0) {
                            return getTurbineCasing(4 - i * 3 - j, tState == 1, tState == 2);
                        }
                    }
                }
                break;
            case 1:
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord + j, yCoord + i, zCoord, side)) != 0) {
                            return getTurbineCasing(4 + i * 3 - j * tInvertLeftRightMod, tState == 1, tState == 2);
                        }
                    }
                }
                break;
            case 2:
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (i == 0 && j == 0) continue;
                        int tState;
                        if ((tState = isTurbineControllerWithSide(aWorld, xCoord, yCoord + i, zCoord + j, side)) != 0) {
                            return getTurbineCasing(4 + i * 3 + j * tInvertLeftRightMod, tState == 1, tState == 2);
                        }
                    }
                }
                break;
        }
        return getTexture(aBlock, side);
    }

    @Override
    public ITexture[] getTexture(Block aBlock, int aMeta, ForgeDirection side) {
        return new ITexture[] { TextureFactory.of(base) };
    }

    @Override
    public int getRenderType() {
        if (BlockRenderHandler.INSTANCE == null) {
            return super.getRenderType();
        }
        return BlockRenderHandler.INSTANCE.mRenderID;
    }
}
