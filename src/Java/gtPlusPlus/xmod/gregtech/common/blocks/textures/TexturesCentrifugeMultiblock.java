package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_IndustrialCentrifuge;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class TexturesCentrifugeMultiblock {

	private static CustomIcon	GT8_1_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE1");
	private static CustomIcon	GT8_1				= new CustomIcon("iconsets/LARGECENTRIFUGE1");
	private static CustomIcon	GT8_2_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE2");
	private static CustomIcon	GT8_2				= new CustomIcon("iconsets/LARGECENTRIFUGE2");
	private static CustomIcon	GT8_3_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE3");
	private static CustomIcon	GT8_3				= new CustomIcon("iconsets/LARGECENTRIFUGE3");
	private static CustomIcon	GT8_4_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE4");
	private static CustomIcon	GT8_4				= new CustomIcon("iconsets/LARGECENTRIFUGE4");
	private static CustomIcon	GT8_5_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE5");
	private static CustomIcon	GT8_5				= new CustomIcon("iconsets/LARGECENTRIFUGE5");
	private static CustomIcon	GT8_6_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE6");
	private static CustomIcon	GT8_6				= new CustomIcon("iconsets/LARGECENTRIFUGE6");
	private static CustomIcon	GT8_7_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE7");
	private static CustomIcon	GT8_7				= new CustomIcon("iconsets/LARGECENTRIFUGE7");
	private static CustomIcon	GT8_8_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE8");
	private static CustomIcon	GT8_8				= new CustomIcon("iconsets/LARGECENTRIFUGE8");
	private static CustomIcon	GT8_9_Active		= new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE9");
	private static CustomIcon	GT8_9				= new CustomIcon("iconsets/LARGECENTRIFUGE9");

	private static CustomIcon	frontFace_0			= TexturesCentrifugeMultiblock.GT8_1;
	private static CustomIcon	frontFaceActive_0	= TexturesCentrifugeMultiblock.GT8_1_Active;
	private static CustomIcon	frontFace_1			= TexturesCentrifugeMultiblock.GT8_2;
	private static CustomIcon	frontFaceActive_1	= TexturesCentrifugeMultiblock.GT8_2_Active;
	private static CustomIcon	frontFace_2			= TexturesCentrifugeMultiblock.GT8_3;
	private static CustomIcon	frontFaceActive_2	= TexturesCentrifugeMultiblock.GT8_3_Active;
	private static CustomIcon	frontFace_3			= TexturesCentrifugeMultiblock.GT8_4;
	private static CustomIcon	frontFaceActive_3	= TexturesCentrifugeMultiblock.GT8_4_Active;
	private static CustomIcon	frontFace_4			= TexturesCentrifugeMultiblock.GT8_5;
	private static CustomIcon	frontFaceActive_4	= TexturesCentrifugeMultiblock.GT8_5_Active;
	private static CustomIcon	frontFace_5			= TexturesCentrifugeMultiblock.GT8_6;
	private static CustomIcon	frontFaceActive_5	= TexturesCentrifugeMultiblock.GT8_6_Active;
	private static CustomIcon	frontFace_6			= TexturesCentrifugeMultiblock.GT8_7;
	private static CustomIcon	frontFaceActive_6	= TexturesCentrifugeMultiblock.GT8_7_Active;
	private static CustomIcon	frontFace_7			= TexturesCentrifugeMultiblock.GT8_8;
	private static CustomIcon	frontFaceActive_7	= TexturesCentrifugeMultiblock.GT8_8_Active;
	private static CustomIcon	frontFace_8			= TexturesCentrifugeMultiblock.GT8_9;
	private static CustomIcon	frontFaceActive_8	= TexturesCentrifugeMultiblock.GT8_9_Active;

	CustomIcon[]				CENTRIFUGE			= new CustomIcon[] {
			TexturesCentrifugeMultiblock.frontFace_0, TexturesCentrifugeMultiblock.frontFace_1,
			TexturesCentrifugeMultiblock.frontFace_2, TexturesCentrifugeMultiblock.frontFace_3,
			TexturesCentrifugeMultiblock.frontFace_4, TexturesCentrifugeMultiblock.frontFace_5,
			TexturesCentrifugeMultiblock.frontFace_6, TexturesCentrifugeMultiblock.frontFace_7,
			TexturesCentrifugeMultiblock.frontFace_8
	};

	CustomIcon[]				CENTRIFUGE_ACTIVE	= new CustomIcon[] {
			TexturesCentrifugeMultiblock.frontFaceActive_0, TexturesCentrifugeMultiblock.frontFaceActive_1,
			TexturesCentrifugeMultiblock.frontFaceActive_2, TexturesCentrifugeMultiblock.frontFaceActive_3,
			TexturesCentrifugeMultiblock.frontFaceActive_4, TexturesCentrifugeMultiblock.frontFaceActive_5,
			TexturesCentrifugeMultiblock.frontFaceActive_6, TexturesCentrifugeMultiblock.frontFaceActive_7,
			TexturesCentrifugeMultiblock.frontFaceActive_8
	};

	public IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
			final int aSide, final GregtechMetaCasingBlocks thisBlock) {
		return this.handleCasingsGT58(aWorld, xCoord, yCoord, zCoord, aSide, thisBlock);
	}

	public IIcon handleCasingsGT58(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
			final int aSide, final GregtechMetaCasingBlocks thisBlock) {
		final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
		if (tMeta != 6 && tMeta != 8 && tMeta != 0) {
			return CasingTextureHandler.getIcon(aSide, tMeta);
		}
		final int tStartIndex = tMeta == 6 ? 1 : 13;
		if (tMeta == 0) {
			if (aSide == 2 || aSide == 3) {
				TileEntity tTileEntity;
				IMetaTileEntity tMetaTileEntity;
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord - 1, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[0].getIcon();
					}
					return this.CENTRIFUGE[0].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[3].getIcon();
					}
					return this.CENTRIFUGE[3].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord + 1, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[6].getIcon();
					}
					return this.CENTRIFUGE[6].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[1].getIcon();
					}
					return this.CENTRIFUGE[1].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[7].getIcon();
					}
					return this.CENTRIFUGE[7].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord + 1, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[8].getIcon();
					}
					return this.CENTRIFUGE[8].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[5].getIcon();
					}
					return this.CENTRIFUGE[5].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord - 1, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[2].getIcon();
					}
					return this.CENTRIFUGE[2].getIcon();
				}
			}
			else if (aSide == 4 || aSide == 5) {
				TileEntity tTileEntity;
				Object tMetaTileEntity;
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 4 ? 1 : -1)))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[0].getIcon();
					}
					return this.CENTRIFUGE[0].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 4 ? 1 : -1)))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[3].getIcon();
					}
					return this.CENTRIFUGE[3].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 4 ? 1 : -1)))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[6].getIcon();
					}
					return this.CENTRIFUGE[6].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[1].getIcon();
					}
					return this.CENTRIFUGE[1].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[7].getIcon();
					}
					return this.CENTRIFUGE[7].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 5 ? 1 : -1)))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[8].getIcon();
					}
					return this.CENTRIFUGE[8].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 5 ? 1 : -1)))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[5].getIcon();
					}
					return this.CENTRIFUGE[5].getIcon();
				}
				if (null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 5 ? 1 : -1)))
						&& tTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide
						&& null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
						&& tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.CENTRIFUGE_ACTIVE[2].getIcon();
					}
					return this.CENTRIFUGE[2].getIcon();
				}
			}
			return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
		}
		final boolean[] tConnectedSides = {
				aWorld.getBlock(xCoord, yCoord - 1, zCoord) == thisBlock
						&& aWorld.getBlockMetadata(xCoord, yCoord - 1, zCoord) == tMeta,
				aWorld.getBlock(xCoord, yCoord + 1, zCoord) == thisBlock
						&& aWorld.getBlockMetadata(xCoord, yCoord + 1, zCoord) == tMeta,
				aWorld.getBlock(xCoord + 1, yCoord, zCoord) == thisBlock
						&& aWorld.getBlockMetadata(xCoord + 1, yCoord, zCoord) == tMeta,
				aWorld.getBlock(xCoord, yCoord, zCoord + 1) == thisBlock
						&& aWorld.getBlockMetadata(xCoord, yCoord, zCoord + 1) == tMeta,
				aWorld.getBlock(xCoord - 1, yCoord, zCoord) == thisBlock
						&& aWorld.getBlockMetadata(xCoord - 1, yCoord, zCoord) == tMeta,
				aWorld.getBlock(xCoord, yCoord, zCoord - 1) == thisBlock
						&& aWorld.getBlockMetadata(xCoord, yCoord, zCoord - 1) == tMeta
		};
		switch (aSide) {
			case 0:
				if (tConnectedSides[0]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 6].getIcon();
				}
				if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 2].getIcon();
				}
				if (tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 3].getIcon();
				}
				if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 4].getIcon();
				}
				if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 5].getIcon();
				}
				if (!tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 8].getIcon();
				}
				if (tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 9].getIcon();
				}
				if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 10].getIcon();
				}
				if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 11].getIcon();
				}
				if (!tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (!tConnectedSides[4] && !tConnectedSides[2]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 1].getIcon();
				}
				if (!tConnectedSides[5] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 0].getIcon();
				}
			case 1:
				if (tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 6].getIcon();
				}
				if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 2].getIcon();
				}
				if (tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 3].getIcon();
				}
				if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 4].getIcon();
				}
				if (tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 5].getIcon();
				}
				if (!tConnectedSides[4] && !tConnectedSides[5] && tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 8].getIcon();
				}
				if (tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 9].getIcon();
				}
				if (tConnectedSides[4] && tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 10].getIcon();
				}
				if (!tConnectedSides[4] && tConnectedSides[5] && tConnectedSides[2] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 11].getIcon();
				}
				if (!tConnectedSides[4] && !tConnectedSides[5] && !tConnectedSides[2] && !tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (!tConnectedSides[2] && !tConnectedSides[4]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 1].getIcon();
				}
				if (!tConnectedSides[3] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 0].getIcon();
				}
			case 2:
				if (tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 6].getIcon();
				}
				if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 2].getIcon();
				}
				if (tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 5].getIcon();
				}
				if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 4].getIcon();
				}
				if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 3].getIcon();
				}
				if (!tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 11].getIcon();
				}
				if (tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 10].getIcon();
				}
				if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 9].getIcon();
				}
				if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 8].getIcon();
				}
				if (!tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (!tConnectedSides[2] && !tConnectedSides[4]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 1].getIcon();
				}
				if (!tConnectedSides[0] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 0].getIcon();
				}
			case 3:
				if (tConnectedSides[3]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 6].getIcon();
				}
				if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 4].getIcon();
				}
				if (tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 5].getIcon();
				}
				if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 2].getIcon();
				}
				if (tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 3].getIcon();
				}
				if (!tConnectedSides[2] && !tConnectedSides[0] && tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 10].getIcon();
				}
				if (tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 11].getIcon();
				}
				if (tConnectedSides[2] && tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 8].getIcon();
				}
				if (!tConnectedSides[2] && tConnectedSides[0] && tConnectedSides[4] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 9].getIcon();
				}
				if (!tConnectedSides[2] && !tConnectedSides[0] && !tConnectedSides[4] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (!tConnectedSides[2] && !tConnectedSides[4]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 1].getIcon();
				}
				if (!tConnectedSides[0] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 0].getIcon();
				}
			case 4:
				if (tConnectedSides[4]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 6].getIcon();
				}
				if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 5].getIcon();
				}
				if (tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 4].getIcon();
				}
				if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 3].getIcon();
				}
				if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 2].getIcon();
				}
				if (!tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 10].getIcon();
				}
				if (tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 9].getIcon();
				}
				if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 8].getIcon();
				}
				if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 11].getIcon();
				}
				if (!tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (!tConnectedSides[0] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 0].getIcon();
				}
				if (!tConnectedSides[3] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 1].getIcon();
				}
			case 5:
				if (tConnectedSides[2]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 6].getIcon();
				}
				if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 5].getIcon();
				}
				if (tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 2].getIcon();
				}
				if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 3].getIcon();
				}
				if (tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 4].getIcon();
				}
				if (!tConnectedSides[0] && !tConnectedSides[3] && tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 11].getIcon();
				}
				if (tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 8].getIcon();
				}
				if (tConnectedSides[0] && tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 9].getIcon();
				}
				if (!tConnectedSides[0] && tConnectedSides[3] && tConnectedSides[1] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 10].getIcon();
				}
				if (!tConnectedSides[0] && !tConnectedSides[3] && !tConnectedSides[1] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
				}
				if (!tConnectedSides[0] && !tConnectedSides[1]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 0].getIcon();
				}
				if (!tConnectedSides[3] && !tConnectedSides[5]) {
					return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 1].getIcon();
				}
				break;
		}
		return Textures.BlockIcons.CONNECTED_HULLS[tStartIndex + 7].getIcon();
	}

}
