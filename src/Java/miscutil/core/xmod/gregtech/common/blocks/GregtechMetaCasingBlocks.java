package miscutil.core.xmod.gregtech.common.blocks;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import miscutil.core.lib.CORE;
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
	
	CasingTextureHandler TextureHandler = new CasingTextureHandler();
	
	private static Textures.BlockIcons.CustomIcon frontFace_0;
	private static Textures.BlockIcons.CustomIcon frontFace_1;
	private static Textures.BlockIcons.CustomIcon frontFace_2;
	private static Textures.BlockIcons.CustomIcon frontFace_3;
	private static Textures.BlockIcons.CustomIcon frontFace_4;
	private static Textures.BlockIcons.CustomIcon frontFace_5;
	private static Textures.BlockIcons.CustomIcon frontFace_6;
	private static Textures.BlockIcons.CustomIcon frontFace_7;
	private static Textures.BlockIcons.CustomIcon frontFace_8;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_0;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_1;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_2;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_3;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_4;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_5;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_6;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_7;
	private static Textures.BlockIcons.CustomIcon frontFaceActive_8;
	
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

	private static Textures.BlockIcons.CustomIcon GT9_1_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE1");
	private static Textures.BlockIcons.CustomIcon GT9_1 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST1");
	private static Textures.BlockIcons.CustomIcon GT9_2_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE2");
	private static Textures.BlockIcons.CustomIcon GT9_2 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST2");
	private static Textures.BlockIcons.CustomIcon GT9_3_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE3");
	private static Textures.BlockIcons.CustomIcon GT9_3 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST3");
	private static Textures.BlockIcons.CustomIcon GT9_4_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE4");
	private static Textures.BlockIcons.CustomIcon GT9_4 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST4");
	private static Textures.BlockIcons.CustomIcon GT9_5_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE5");
	private static Textures.BlockIcons.CustomIcon GT9_5 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST5");
	private static Textures.BlockIcons.CustomIcon GT9_6_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE6");
	private static Textures.BlockIcons.CustomIcon GT9_6 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST6");
	private static Textures.BlockIcons.CustomIcon GT9_7_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE7");
	private static Textures.BlockIcons.CustomIcon GT9_7 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST7");
	private static Textures.BlockIcons.CustomIcon GT9_8_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE8");
	private static Textures.BlockIcons.CustomIcon GT9_8 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST8");
	private static Textures.BlockIcons.CustomIcon GT9_9_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST_ACTIVE9");
	private static Textures.BlockIcons.CustomIcon GT9_9 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ST9");
	
	private static Textures.BlockIcons.CustomIcon GT8_1_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE1");
	private static Textures.BlockIcons.CustomIcon GT8_1 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE1");
	private static Textures.BlockIcons.CustomIcon GT8_2_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE2");
	private static Textures.BlockIcons.CustomIcon GT8_2 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE2");
	private static Textures.BlockIcons.CustomIcon GT8_3_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE3");
	private static Textures.BlockIcons.CustomIcon GT8_3 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE3");
	private static Textures.BlockIcons.CustomIcon GT8_4_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE4");
	private static Textures.BlockIcons.CustomIcon GT8_4 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE4.png");
	private static Textures.BlockIcons.CustomIcon GT8_5_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE5");
	private static Textures.BlockIcons.CustomIcon GT8_5 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE5");
	private static Textures.BlockIcons.CustomIcon GT8_6_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE6");
	private static Textures.BlockIcons.CustomIcon GT8_6 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE6");
	private static Textures.BlockIcons.CustomIcon GT8_7_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE7");
	private static Textures.BlockIcons.CustomIcon GT8_7 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE7");
	private static Textures.BlockIcons.CustomIcon GT8_8_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE8");
	private static Textures.BlockIcons.CustomIcon GT8_8 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE8");
	private static Textures.BlockIcons.CustomIcon GT8_9_Active = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE_ACTIVE9");
	private static Textures.BlockIcons.CustomIcon GT8_9 = new Textures.BlockIcons.CustomIcon("iconsets/LARGETURBINE9");
	
	

	public GregtechMetaCasingBlocks() {
		super(GregtechMetaCasingItems.class, "miscutils.blockcasings", GT_Material_Casings.INSTANCE);
		for (byte i = 0; i < 16; i = (byte) (i + 1)) {
			Textures.BlockIcons.CASING_BLOCKS[GTID + i] = new GT_CopiedBlockTexture(this, 6, i);
		}
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Centrifuge Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Structural Coke Oven Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Heat Resistant Coke Oven Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Heat Proof Coke Oven Casing"); //60
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Material Press Machine Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Electrolyzer Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Wire Factory Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Maceration Stack Casing");
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Matter Generation Coil"); //65
		GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Matter Fabricator Casing");
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
		GregtechItemList.Casing_Macerator.set(new ItemStack(this, 1, 6));
		GregtechItemList.Casing_U4.set(new ItemStack(this, 1, 7));
		GregtechItemList.Casing_MatterGen.set(new ItemStack(this, 1, 8));
		GregtechItemList.Casing_MatterFab.set(new ItemStack(this, 1, 9));
		GregtechItemList.Casing_IronPlatedBricks.set(new ItemStack(this, 1, 10));
		GregtechItemList.Casing_U7.set(new ItemStack(this, 1, 11));
		GregtechItemList.Casing_Coil_U1.set(new ItemStack(this, 1, 12));
		GregtechItemList.Casing_Coil_U2.set(new ItemStack(this, 1, 13));
		GregtechItemList.Casing_Coil_U3.set(new ItemStack(this, 1, 14));
		GregtechItemList.Casing_Coil_U4.set(new ItemStack(this, 1, 15));
		
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			frontFace_0 = GT9_1;
			frontFaceActive_0 = (GT9_1_Active);
			frontFace_1 = (GT9_2);
			frontFaceActive_1 = (GT9_2_Active);
			frontFace_2 = (GT9_3);
			frontFaceActive_2 = (GT9_3_Active);
			frontFace_3 = (GT9_4);
			frontFaceActive_3 = (GT9_4_Active);
			frontFace_4 = (GT9_5);
			frontFaceActive_4 = (GT9_5_Active);
			frontFace_5 = (GT9_6);
			frontFaceActive_5 = (GT9_6_Active);
			frontFace_6 = (GT9_7);
			frontFaceActive_6 = (GT9_7_Active);
			frontFace_7 = (GT9_8);
			frontFaceActive_7 = (GT9_8_Active);
			frontFace_8 = (GT9_9);
			frontFaceActive_8 = (GT9_9_Active);
		}
		else{
			frontFace_0 = (GT8_1);
			frontFaceActive_0 = (GT8_1_Active);
			frontFace_1 = (GT8_2);
			frontFaceActive_1 = (GT8_2_Active);
			frontFace_2 = (GT8_3);
			frontFaceActive_2 = (GT8_3_Active);
			frontFace_3 = (GT8_4);
			frontFaceActive_3 = (GT8_4_Active);
			frontFace_4 = (GT8_5);
			frontFaceActive_4 = (GT8_5_Active);
			frontFace_5 = (GT8_6);
			frontFaceActive_5 = (GT8_6_Active);
			frontFace_6 = (GT8_7);
			frontFaceActive_6 = (GT8_7_Active);
			frontFace_7 = (GT8_8);
			frontFaceActive_7 = (GT8_8_Active);
			frontFace_8 = (GT8_9);
			frontFaceActive_8 = (GT8_9_Active);
		}
		
		
	}

	@Override
	public IIcon getIcon(int aSide, int aMeta) { //Texture ID's. case 0 == ID[57]
		if ((aMeta >= 0) && (aMeta < 16)) {
			switch (aMeta) {
			//Centrifuge 
			case 0: 
				return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
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
				//Broken Blue Fusion Casings
			case 6:
				return Textures.BlockIcons.MACHINE_CASING_FUSION.getIcon();
				//Maceration Stack Casings
			case 7:
				return Textures.BlockIcons.MACHINE_LuV_BOTTOM.getIcon();
				//Broken Pink Fusion Casings
			case 8:
				return Textures.BlockIcons.MACHINE_CASING_FUSION_2.getIcon();
				//Matter Fabricator Casings
			case 9:
				return Textures.BlockIcons.MACHINE_CASING_DRAGONEGG.getIcon();
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
		
		GregtechMetaCasingBlocks i = this;
		
		int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
		if (((tMeta != 6) && (tMeta != 8) && (tMeta != 0))) {
			return getIcon(aSide, tMeta);
		}
		int tStartIndex = tMeta == 6 ? 1 : 13;
		if (tMeta == 0) {
			if ((aSide == 2) || (aSide == 3)) {
				TileEntity tTileEntity;
				IMetaTileEntity tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[0].getIcon();
					}
					return TURBINE[0].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[3].getIcon();
					}
					return TURBINE[3].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[6].getIcon();
					}
					return TURBINE[6].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[1].getIcon();
					}
					return TURBINE[1].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[7].getIcon();
					}
					return TURBINE[7].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[8].getIcon();
					}
					return TURBINE[8].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[5].getIcon();
					}
					return TURBINE[5].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[2].getIcon();
					}
					return TURBINE[2].getIcon();
				}
			} else if ((aSide == 4) || (aSide == 5)) {
				TileEntity tTileEntity;
				Object tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[0].getIcon();
					}
					return TURBINE[0].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[3].getIcon();
					}
					return TURBINE[3].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[6].getIcon();
					}
					return TURBINE[6].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[1].getIcon();
					}
					return TURBINE[1].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[7].getIcon();
					}
					return TURBINE[7].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[8].getIcon();
					}
					return TURBINE[8].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[5].getIcon();
					}
					return TURBINE[5].getIcon();
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntityIndustrialCentrifuge))) {
					if (((IGregTechTileEntity) tTileEntity).isActive()) {
						return TURBINE_ACTIVE[2].getIcon();
					}
					return TURBINE[2].getIcon();
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
