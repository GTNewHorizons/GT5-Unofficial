package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialCentrifuge;

public class TexturesGregtech59 {

	private static Textures.BlockIcons.CustomIcon GT8_1_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE1");
	private static Textures.BlockIcons.CustomIcon GT8_1 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST1");
	private static Textures.BlockIcons.CustomIcon GT8_2_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE2");
	private static Textures.BlockIcons.CustomIcon GT8_2 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST2");
	private static Textures.BlockIcons.CustomIcon GT8_3_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE3");
	private static Textures.BlockIcons.CustomIcon GT8_3 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST3");
	private static Textures.BlockIcons.CustomIcon GT8_4_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE4");
	private static Textures.BlockIcons.CustomIcon GT8_4 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST4");
	private static Textures.BlockIcons.CustomIcon GT8_5_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE5");
	private static Textures.BlockIcons.CustomIcon GT8_5 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST5");
	private static Textures.BlockIcons.CustomIcon GT8_6_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE6");
	private static Textures.BlockIcons.CustomIcon GT8_6 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST6");
	private static Textures.BlockIcons.CustomIcon GT8_7_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE7");
	private static Textures.BlockIcons.CustomIcon GT8_7 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST7");
	private static Textures.BlockIcons.CustomIcon GT8_8_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE8");
	private static Textures.BlockIcons.CustomIcon GT8_8 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST8");
	private static Textures.BlockIcons.CustomIcon GT8_9_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE9");
	private static Textures.BlockIcons.CustomIcon GT8_9 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST9");

	private static Textures.BlockIcons.CustomIcon frontFace_0 = (GT8_1);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_0 = (GT8_1_Active);
	private static Textures.BlockIcons.CustomIcon frontFace_1 = (GT8_2);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_1 = (GT8_2_Active);
	private static Textures.BlockIcons.CustomIcon frontFace_2 = (GT8_3);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_2 = (GT8_3_Active);
	private static Textures.BlockIcons.CustomIcon frontFace_3 = (GT8_4);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_3 = (GT8_4_Active);
	private static Textures.BlockIcons.CustomIcon frontFace_4 = (GT8_5);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_4 = (GT8_5_Active);
	private static Textures.BlockIcons.CustomIcon frontFace_5 = (GT8_6);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_5 = (GT8_6_Active);
	private static Textures.BlockIcons.CustomIcon frontFace_6 = (GT8_7);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_6 = (GT8_7_Active);
	private static Textures.BlockIcons.CustomIcon frontFace_7 = (GT8_8);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_7 = (GT8_8_Active);
	private static Textures.BlockIcons.CustomIcon frontFace_8 = (GT8_9);
	private static Textures.BlockIcons.CustomIcon frontFaceActive_8 = (GT8_9_Active);

	Textures.BlockIcons.CustomIcon[] TURBINE = new Textures.BlockIcons.CustomIcon[]{
			frontFace_0,
			frontFace_1,
			frontFace_2,
			frontFace_3,
			frontFace_4,
			frontFace_5,
			frontFace_6,
			frontFace_7,
			frontFace_8
	};

	Textures.BlockIcons.CustomIcon[] TURBINE_ACTIVE = new Textures.BlockIcons.CustomIcon[]{
			frontFaceActive_0,
			frontFaceActive_1,
			frontFaceActive_2,
			frontFaceActive_3,
			frontFaceActive_4,
			frontFaceActive_5,
			frontFaceActive_6,
			frontFaceActive_7,
			frontFaceActive_8
	};


	public IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord, final int aSide, final GregtechMetaCasingBlocks thisBlock) {
		return this.handleCasingsGT59(aWorld, xCoord, yCoord, zCoord, aSide, thisBlock);
	}


	public IIcon handleCasingsGT59(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord, final int aSide, final GregtechMetaCasingBlocks thisBlock) {
		final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
		if (((tMeta != 6) && (tMeta != 8) && (tMeta != 0))) {
			return CasingTextureHandler.getIcon(aSide, tMeta);
		}
		final int tStartIndex = tMeta == 6 ? 1 : 13;
		if (tMeta == 0) {
			if ((aSide == 2) || (aSide == 3)) {
				TileEntity tTileEntity;
				IMetaTileEntity tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[0].getIcon();
					}
					return this.TURBINE[0].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[3].getIcon();
					}
					return this.TURBINE[3].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[6].getIcon();
					}
					return this.TURBINE[6].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[1].getIcon();
					}
					return this.TURBINE[1].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[7].getIcon();
					}
					return this.TURBINE[7].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[8].getIcon();
					}
					return this.TURBINE[8].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[5].getIcon();
					}
					return this.TURBINE[5].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[2].getIcon();
					}
					return this.TURBINE[2].getIcon();
				}
			} else if ((aSide == 4) || (aSide == 5)) {
				TileEntity tTileEntity;
				Object tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[0].getIcon();
					}
					return this.TURBINE[0].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[3].getIcon();
					}
					return this.TURBINE[3].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[6].getIcon();
					}
					return this.TURBINE[6].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[1].getIcon();
					}
					return this.TURBINE[1].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[7].getIcon();
					}
					return this.TURBINE[7].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[8].getIcon();
					}
					return this.TURBINE[8].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[5].getIcon();
					}
					return this.TURBINE[5].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return this.TURBINE_ACTIVE[2].getIcon();
					}
					return this.TURBINE[2].getIcon();
				}
			}
			return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
		}
		final boolean[] tConnectedSides = {(aWorld.getBlock(xCoord, yCoord - 1, zCoord) == thisBlock) && (aWorld.getBlockMetadata(xCoord, yCoord - 1, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord + 1, zCoord) == thisBlock) && (aWorld.getBlockMetadata(xCoord, yCoord + 1, zCoord) == tMeta), (aWorld.getBlock(xCoord + 1, yCoord, zCoord) == thisBlock) && (aWorld.getBlockMetadata(xCoord + 1, yCoord, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord, zCoord + 1) == thisBlock) && (aWorld.getBlockMetadata(xCoord, yCoord, zCoord + 1) == tMeta), (aWorld.getBlock(xCoord - 1, yCoord, zCoord) == thisBlock) && (aWorld.getBlockMetadata(xCoord - 1, yCoord, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord, zCoord - 1) == thisBlock) && (aWorld.getBlockMetadata(xCoord, yCoord, zCoord - 1) == tMeta)};
		switch (aSide) {
		case 0:
			if (tConnectedSides[0]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[4]) && (!tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (!tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((tConnectedSides[4]) && (!tConnectedSides[5]) && (!tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (!tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((!tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[5]) && (!tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[2])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			if ((!tConnectedSides[5]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
		case 1:
			if (tConnectedSides[1]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[4]) && (!tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (!tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((tConnectedSides[4]) && (!tConnectedSides[5]) && (!tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (!tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((!tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[5]) && (!tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[4])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			if ((!tConnectedSides[3]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
		case 2:
			if (tConnectedSides[5]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[2]) && (!tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (!tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((tConnectedSides[2]) && (!tConnectedSides[0]) && (!tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (!tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((!tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[0]) && (!tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[4])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
		case 3:
			if (tConnectedSides[3]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[2]) && (!tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (!tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((tConnectedSides[2]) && (!tConnectedSides[0]) && (!tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (!tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((!tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[0]) && (!tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[4])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
		case 4:
			if (tConnectedSides[4]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((tConnectedSides[0]) && (!tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (!tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((tConnectedSides[0]) && (!tConnectedSides[3]) && (!tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (!tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((!tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[3]) && (!tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
			if ((!tConnectedSides[3]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
		case 5:
			if (tConnectedSides[2]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((tConnectedSides[0]) && (!tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (!tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((tConnectedSides[0]) && (!tConnectedSides[3]) && (!tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (!tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((!tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[3]) && (!tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
			if ((!tConnectedSides[3]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			break;
		}
		return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
	}


}
