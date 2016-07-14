package miscutil.core.xmod.gregtech.common.blocks;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIndustrialCentrifuge;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GregtechMetaCasingBlocks
extends GregtechMetaCasingBlocksAbstract {

	public final static int GTID = 57;

	public GregtechMetaCasingBlocks() {
		super(GregtechMetaCasingItems.class, "miscutils.blockcasings", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			Textures.BlockIcons.CASING_BLOCKS[GTID + i] = new GT_CopiedBlockTexture(this, 6, i);
		}
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Centrifuge Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Structural Coke Oven Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Heat Resistant Coke Oven Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Heat Proof Coke Oven Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Material Press Machine Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Electrolyzer Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Maceration Stack Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Unused Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Unused Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Unused Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Iron Plated Bricks");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Unused Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Unused Coil Block");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Unused Coil Block");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Unused Coil Block");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Unused Coil Block");
		GregtechItemList.Casing_Centrifuge1.set(new ItemStack(this, 1, 0));
		GregtechItemList.Casing_CokeOven.set(new ItemStack(this, 1, 1));
		GregtechItemList.Casing_CokeOven_Coil1.set(new ItemStack(this, 1, 2));
		GregtechItemList.Casing_CokeOven_Coil2.set(new ItemStack(this, 1, 3));
		GregtechItemList.Casing_MaterialPress.set(new ItemStack(this, 1, 4));
		GregtechItemList.Casing_Electrolyzer.set(new ItemStack(this, 1, 5));
		GregtechItemList.Casing_U3.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_U4.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_U5.set(new ItemStack(this, 1, 8));
		GregtechItemList.Casing_U6.set(new ItemStack(this, 1, 9));
		GregtechItemList.Casing_IronPlatedBricks.set(new ItemStack(this, 1, 10));
		GregtechItemList.Casing_U7.set(new ItemStack(this, 1, 11));
		GregtechItemList.Casing_Coil_U1.set(new ItemStack(this, 1, 12));
		GregtechItemList.Casing_Coil_U2.set(new ItemStack(this, 1, 13));
		GregtechItemList.Casing_Coil_U3.set(new ItemStack(this, 1, 14));
		GregtechItemList.Casing_Coil_U4.set(new ItemStack(this, 1, 15));
	}

	@Override
	public IIcon getIcon(int aSide, int aMeta) { //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
			//Centrifuge 
			case 0: 
				return Textures.BlockIcons.MACHINE_CASING_TURBINE.getIcon();
				//Coke Oven Frame
			case 1:
				return Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
				//Coke Oven Casing Tier 1
			case 2: 
				return Textures.BlockIcons.MACHINE_CASING_FIREBOX_BRONZE.getIcon();
				//Coke Oven Casing Tier 2
			case 3:
				return Textures.BlockIcons.MACHINE_CASING_FIREBOX_STEEL.getIcon();
				//Material Press Casings
			case 4:
				return Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
				//Electrolyzer Casings
			case 5:
				return Textures.BlockIcons.MACHINE_CASING_FUSION_2.getIcon();
				//Maceration Stack Casings
			case 6:
				return Textures.BlockIcons.MACHINE_CASING_MAGIC.getIcon();
				//Iron Blast Fuance Textures
			case 10:
				return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();			

			default:
				return Textures.BlockIcons.MACHINE_CASING_RADIOACTIVEHAZARD.getIcon();

			}
		}
		return Textures.BlockIcons.MACHINE_CASING_GEARBOX_TUNGSTENSTEEL.getIcon();
	}

	/*@Override
	public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
		return aWorld.getBlockMetadata(aX, aY, aZ) > 9 ? super.colorMultiplier(aWorld, aX, aY, aZ) : gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[0] << 16 | gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[1] << 8 | gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[2];
	}
	 */

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
		int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
		if (((tMeta != 6) && (tMeta != 8) && (tMeta != 0))/* || (!mConnectedMachineTextures)*/) {
			return getIcon(aSide, tMeta);
		}
		int tStartIndex = tMeta == 6 ? 1 : 13;
		if (tMeta == 0) {
			if ((aSide == 2) || (aSide == 3)) {
				TileEntity tTileEntity;
				IMetaTileEntity tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[0].getIcon();
					}
					return Textures.BlockIcons.TURBINE[0].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[3].getIcon();
					}
					return Textures.BlockIcons.TURBINE[3].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[6].getIcon();
					}
					return Textures.BlockIcons.TURBINE[6].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[1].getIcon();
					}
					return Textures.BlockIcons.TURBINE[1].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[7].getIcon();
					}
					return Textures.BlockIcons.TURBINE[7].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[8].getIcon();
					}
					return Textures.BlockIcons.TURBINE[8].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[5].getIcon();
					}
					return Textures.BlockIcons.TURBINE[5].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[2].getIcon();
					}
					return Textures.BlockIcons.TURBINE[2].getIcon();
				}
			} else if ((aSide == 4) || (aSide == 5)) {
				TileEntity tTileEntity;
				Object tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[0].getIcon();
					}
					return Textures.BlockIcons.TURBINE[0].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[3].getIcon();
					}
					return Textures.BlockIcons.TURBINE[3].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[6].getIcon();
					}
					return Textures.BlockIcons.TURBINE[6].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[1].getIcon();
					}
					return Textures.BlockIcons.TURBINE[1].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[7].getIcon();
					}
					return Textures.BlockIcons.TURBINE[7].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[8].getIcon();
					}
					return Textures.BlockIcons.TURBINE[8].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[5].getIcon();
					}
					return Textures.BlockIcons.TURBINE[5].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return Textures.BlockIcons.TURBINE_ACTIVE[2].getIcon();
					}
					return Textures.BlockIcons.TURBINE[2].getIcon();
				}
			}
			return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
		}
		boolean[] tConnectedSides = {(aWorld.getBlock(xCoord, yCoord - 1, zCoord) == this) && (aWorld.getBlockMetadata(xCoord, yCoord - 1, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord + 1, zCoord) == this) && (aWorld.getBlockMetadata(xCoord, yCoord + 1, zCoord) == tMeta), (aWorld.getBlock(xCoord + 1, yCoord, zCoord) == this) && (aWorld.getBlockMetadata(xCoord + 1, yCoord, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord, zCoord + 1) == this) && (aWorld.getBlockMetadata(xCoord, yCoord, zCoord + 1) == tMeta), (aWorld.getBlock(xCoord - 1, yCoord, zCoord) == this) && (aWorld.getBlockMetadata(xCoord - 1, yCoord, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord, zCoord - 1) == this) && (aWorld.getBlockMetadata(xCoord, yCoord, zCoord - 1) == tMeta)};
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
