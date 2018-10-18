package gtPlusPlus.xmod.gregtech.common.blocks.textures.turbine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Turbine;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks4;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class LargeTurbineTextureHandler {

	/**
	 * LP Turbines
	 */
	private static CustomIcon aTex1_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_1");
	private static CustomIcon aTex1 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_1");
	private static CustomIcon aTex2_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_2");
	private static CustomIcon aTex2 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_2");
	private static CustomIcon aTex3_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_3");
	private static CustomIcon aTex3 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_3");
	private static CustomIcon aTex4_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_4");
	private static CustomIcon aTex4 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_4");
	private static CustomIcon aTex5_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_5");
	private static CustomIcon aTex5 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_5");
	private static CustomIcon aTex6_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_6");
	private static CustomIcon aTex6 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_6");
	private static CustomIcon aTex7_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_7");
	private static CustomIcon aTex7 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_7");
	private static CustomIcon aTex8_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_8");
	private static CustomIcon aTex8 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_8");
	private static CustomIcon aTex9_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_ACTIVE_9");
	private static CustomIcon aTex9 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_LP_9");

	private static CustomIcon frontFace_0 = (aTex1);
	private static CustomIcon frontFaceActive_0 = (aTex1_Active);
	private static CustomIcon frontFace_1 = (aTex2);
	private static CustomIcon frontFaceActive_1 = (aTex2_Active);
	private static CustomIcon frontFace_2 = (aTex3);
	private static CustomIcon frontFaceActive_2 = (aTex3_Active);
	private static CustomIcon frontFace_3 = (aTex4);
	private static CustomIcon frontFaceActive_3 = (aTex4_Active);
	public static CustomIcon frontFace_4 = (aTex5);
	public static CustomIcon frontFaceActive_4 = (aTex5_Active);
	private static CustomIcon frontFace_5 = (aTex6);
	private static CustomIcon frontFaceActive_5 = (aTex6_Active);
	private static CustomIcon frontFace_6 = (aTex7);
	private static CustomIcon frontFaceActive_6 = (aTex7_Active);
	private static CustomIcon frontFace_7 = (aTex8);
	private static CustomIcon frontFaceActive_7 = (aTex8_Active);
	private static CustomIcon frontFace_8 = (aTex9);
	private static CustomIcon frontFaceActive_8 = (aTex9_Active);

	CustomIcon[] OVERLAY_LP_TURBINE = new CustomIcon[]{
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

	CustomIcon[] OVERLAY_LP_TURBINE_ACTIVE = new CustomIcon[]{
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


	/**
	 * HP Turbines
	 */
	private static CustomIcon aTexHP1_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_1");
	private static CustomIcon aTexHP1 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_1");
	private static CustomIcon aTexHP2_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_2");
	private static CustomIcon aTexHP2 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_2");
	private static CustomIcon aTexHP3_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_3");
	private static CustomIcon aTexHP3 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_3");
	private static CustomIcon aTexHP4_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_4");
	private static CustomIcon aTexHP4 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_4");
	private static CustomIcon aTexHP5_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_5");
	private static CustomIcon aTexHP5 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_5");
	private static CustomIcon aTexHP6_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_6");
	private static CustomIcon aTexHP6 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_6");
	private static CustomIcon aTexHP7_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_7");
	private static CustomIcon aTexHP7 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_7");
	private static CustomIcon aTexHP8_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_8");
	private static CustomIcon aTexHP8 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_8");
	private static CustomIcon aTexHP9_Active = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_ACTIVE_9");
	private static CustomIcon aTexHP9 = new CustomIcon("iconsets/BigTurbine/LARGE_TURBINE_HP_9");

	private static CustomIcon frontFaceHP_0 = (aTexHP1);
	private static CustomIcon frontFaceHPActive_0 = (aTexHP1_Active);
	private static CustomIcon frontFaceHP_1 = (aTexHP2);
	private static CustomIcon frontFaceHPActive_1 = (aTexHP2_Active);
	private static CustomIcon frontFaceHP_2 = (aTexHP3);
	private static CustomIcon frontFaceHPActive_2 = (aTexHP3_Active);
	private static CustomIcon frontFaceHP_3 = (aTexHP4);
	private static CustomIcon frontFaceHPActive_3 = (aTexHP4_Active);
	public static CustomIcon frontFaceHP_4 = (aTexHP5);
	public static CustomIcon frontFaceHPActive_4 = (aTexHP5_Active);
	private static CustomIcon frontFaceHP_5 = (aTexHP6);
	private static CustomIcon frontFaceHPActive_5 = (aTexHP6_Active);
	private static CustomIcon frontFaceHP_6 = (aTexHP7);
	private static CustomIcon frontFaceHPActive_6 = (aTexHP7_Active);
	private static CustomIcon frontFaceHP_7 = (aTexHP8);
	private static CustomIcon frontFaceHPActive_7 = (aTexHP8_Active);
	private static CustomIcon frontFaceHP_8 = (aTexHP9);
	private static CustomIcon frontFaceHPActive_8 = (aTexHP9_Active);

	CustomIcon[] OVERLAY_HP_TURBINE = new CustomIcon[]{
			frontFaceHP_0,
			frontFaceHP_1,
			frontFaceHP_2,
			frontFaceHP_3,
			frontFaceHP_4,
			frontFaceHP_5,
			frontFaceHP_6,
			frontFaceHP_7,
			frontFaceHP_8
	};

	CustomIcon[] OVERLAY_HP_TURBINE_ACTIVE = new CustomIcon[]{
			frontFaceHPActive_0,
			frontFaceHPActive_1,
			frontFaceHPActive_2,
			frontFaceHPActive_3,
			frontFaceHPActive_4,
			frontFaceHPActive_5,
			frontFaceHPActive_6,
			frontFaceHPActive_7,
			frontFaceHPActive_8
	};



	public IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord, final int aSide, final GregtechMetaCasingBlocks4 thisBlock) {
		final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);

		//7 - shaft
		//8 LP
		//9 HP

		CustomIcon[] mGetCurrentTextureSet = null, mGetCurrentTextureSet_ACTIVE = null;


		if (tMeta <= 6 || tMeta >= 10) {
			return GregtechMetaCasingBlocks4.getStaticIcon((byte) aSide, (byte) tMeta);
		}
		else {
			if (tMeta == 8) {
				mGetCurrentTextureSet = OVERLAY_LP_TURBINE;
				mGetCurrentTextureSet_ACTIVE = OVERLAY_LP_TURBINE_ACTIVE;
			}
			else if (tMeta == 9) {
				mGetCurrentTextureSet = OVERLAY_HP_TURBINE;
				mGetCurrentTextureSet_ACTIVE = OVERLAY_HP_TURBINE_ACTIVE;
			}
			if (mGetCurrentTextureSet == null || mGetCurrentTextureSet_ACTIVE == null) {
				return GregtechMetaCasingBlocks4.getStaticIcon((byte) aSide, (byte) tMeta);
			}



			if ((aSide == 2) || (aSide == 3)) {
				TileEntity tTileEntity;
				IMetaTileEntity tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[0].getIcon();
					}
					return mGetCurrentTextureSet[0].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[3].getIcon();
					}
					return mGetCurrentTextureSet[3].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[6].getIcon();
					}
					return mGetCurrentTextureSet[6].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[1].getIcon();
					}
					return mGetCurrentTextureSet[1].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[7].getIcon();
					}
					return mGetCurrentTextureSet[7].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[8].getIcon();
					}
					return mGetCurrentTextureSet[8].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[5].getIcon();
					}
					return mGetCurrentTextureSet[5].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[2].getIcon();
					}
					return mGetCurrentTextureSet[2].getIcon();
				}
			} else if ((aSide == 4) || (aSide == 5)) {
				TileEntity tTileEntity;
				Object tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[0].getIcon();
					}
					return mGetCurrentTextureSet[0].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[3].getIcon();
					}
					return mGetCurrentTextureSet[3].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[6].getIcon();
					}
					return mGetCurrentTextureSet[6].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[1].getIcon();
					}
					return mGetCurrentTextureSet[1].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[7].getIcon();
					}
					return mGetCurrentTextureSet[7].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[8].getIcon();
					}
					return mGetCurrentTextureSet[8].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[5].getIcon();
					}
					return mGetCurrentTextureSet[5].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine))) {
					if (isUsingAnimatedTexture(tTileEntity)) {
						return mGetCurrentTextureSet_ACTIVE[2].getIcon();
					}
					return mGetCurrentTextureSet[2].getIcon();
				}
			}
		}
		return GregtechMetaCasingBlocks4.getStaticIcon((byte) aSide, (byte) tMeta);
	}

	public boolean isUsingAnimatedTexture(TileEntity tTileEntity) {
		boolean aVal = true;
		/*IGregTechTileEntity aTile;
		if (tTileEntity instanceof IGregTechTileEntity) {
			aTile = (IGregTechTileEntity) tTileEntity;
			if (aTile != null) {
				final IMetaTileEntity aMetaTileEntity = aTile.getMetaTileEntity();
				if (aMetaTileEntity != null && aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine) {
					aVal = ((GT_MetaTileEntity_Hatch_Turbine) aMetaTileEntity).isControllerActive();	
					Logger.INFO("Returning "+aVal+" as Rotor Assembly controller status");
				}
			}	
		}	*/
		return aVal;
	}

	public GT_MetaTileEntity_Hatch_Turbine isTurbineHatch(final IGregTechTileEntity aTileEntity) {
		if (aTileEntity != null) {
			final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity != null && aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Turbine) {
				return (GT_MetaTileEntity_Hatch_Turbine) aMetaTileEntity;			
			}
		}		
		return null;
	}

}
