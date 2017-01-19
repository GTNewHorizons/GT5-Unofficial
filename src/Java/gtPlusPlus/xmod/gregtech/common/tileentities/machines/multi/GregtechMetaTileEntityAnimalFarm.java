package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.trees.TreefarmManager;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntityAnimalFarm extends GT_MetaTileEntity_MultiBlockBase {


	private static final ITexture[] FACING_SIDE = {new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Tumbaga)};
	private static final ITexture[] FACING_FRONT = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE)};
	private static final ITexture[] FACING_ACTIVE = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE_ACTIVE)};


	//public ArrayList<GT_MetaTileEntity_TieredMachineBlock> mCasings = new ArrayList();

	private boolean running = false;
	private boolean p1, p2, p3, p4, p5, p6;
	public ItemStack mOutputItem1;
	public ItemStack mOutputItem2;
	private Block Humus;
	private boolean isForestryLoaded = TreefarmManager.isForestryValid();

	public GregtechMetaTileEntityAnimalFarm(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityAnimalFarm(String aName) {
		super(aName);
	}   

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Animal Farmer",
				"How to get your first logs without an axe.",
				"Max Size(WxHxD): 9x1x9 (Controller, with upto 4 dirt out each direction on a flat plane.)",
				"Dirt for the rest! [D = Dirt, X = Controller]"
				};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == 1) {
			return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Vent_Fast : TexturesGtBlock.Overlay_Machine_Vent)};
		}
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Farm_Manager)};
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	@Override
	public boolean isTeleporterCompatible() {
		return false;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID) {
		return (GregTech_API.getCoverBehavior(aCoverID.toStack()).isSimpleCover()) && (super.allowCoverOnSide(aSide, aCoverID));
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityAnimalFarm(this.mName);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) return true;

		return true;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		Utils.LOG_INFO("Working");
		/*if (!checkRecursiveBlocks()) {
			this.mEfficiency = 0;
			this.mEfficiencyIncrease = 0;
			this.mMaxProgresstime = 0;
			running = false;
			return false;
		}

		if (mEfficiency == 0) {
			this.mEfficiency = 10000;
			this.mEfficiencyIncrease = 10000;
			this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
			//GT_Pollution.addPollution(new ChunkPosition(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord()), mMaxProgresstime*5);
			return true;
		}*/
		this.mEfficiency = 0;
		this.mEfficiencyIncrease = 0;
		this.mMaxProgresstime = 0;
		return false;
	}


	private Block getHumus(){
		if (!LoadedMods.Forestry){
			return null;
		}
		return TreefarmManager.getHumus();	
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

		//this.mCasings.clear();
		Utils.LOG_INFO("Step 1");
		int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;

		for (int i = -7; i <= 7; i++) {
			Utils.LOG_INFO("Step 2");
			for (int j = -7; j <= 7; j++) {
				Utils.LOG_INFO("Step 3");
				for (int h = 0; h <= 1; h++) {
					Utils.LOG_INFO("Step 4");

					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					//Farm Floor inner 14x14
					if ((i != -7 && i != 7) && (j != -7 && j != 7)) {
						Utils.LOG_INFO("Step 5 - H:"+h);
						// Farm Dirt Floor and Inner Air/Log space.
						if (h == 0) {
							//Dirt Floor
							if (!TreefarmManager.isDirtBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
								Utils.LOG_INFO("Dirt like block missing from inner 14x14.");
								Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								aBaseMetaTileEntity.getWorld().setBlock(
										(aBaseMetaTileEntity.getXCoord()+(xDir+i)),
										(aBaseMetaTileEntity.getYCoord()+(h)),
										(aBaseMetaTileEntity.getZCoord()+(zDir+j)),
										Blocks.melon_block);
								return false;
							}							
						} 
						// Inside fenced area, mostly air or trees or saplings
						else if (h == 1){		
							//Farm Inner 14x14
							/*if (!TreefarmManager.isWoodLog(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)) || !TreefarmManager.isAirBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)) || !aBaseMetaTileEntity.getAirOffset(xDir+i, h, zDir+j)) {
								Utils.LOG_INFO("Wood like block missing from inner 14x14, layer 2."); //TODO
								Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());	
								Utils.LOG_INFO("Found at x:"+(xDir+i)+" y:"+h+" z:"+(zDir+j));
								//return false;
								}*/	
						}			

					}
					//Dealt with inner 5x5, now deal with the exterior.
					else {
						Utils.LOG_INFO("Step 6 - H:"+h);
						//Deal with all 4 sides (Fenced area)
						if (h == 1) {														
							if (!TreefarmManager.isFenceBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
								Utils.LOG_INFO("Fence/Gate missing from outside the second layer.");
								Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}					
						}
						//Deal with Bottom edges (Add Hatches/Busses first, othercheck make sure it's dirt) //TODO change the casings to not dirt~
						else if (h == 0) {

							try {
								addCasingToCasingList(tTileEntity);
							} catch (Throwable t){}

							if ((!addMaintenanceToMachineList(tTileEntity, 77)) && (!addInputToMachineList(tTileEntity, 77)) && (!addOutputToMachineList(tTileEntity, 77)) && (!addEnergyInputToMachineList(tTileEntity, 77))) {
								if ((xDir + i != 0) || (zDir + j != 0)) {//no controller			

									if (!(aBaseMetaTileEntity.getMetaTileID() != 752)) {
										Utils.LOG_INFO("Fark Keeper Casings Missing from one of the edges on the bottom edge. x:"+(xDir+i)+" y:"+h+" z:"+(zDir+j)+" | "+aBaseMetaTileEntity.getClass());
										Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									Utils.LOG_INFO("Found a farm keeper.");
								}
							}
						}
						Utils.LOG_INFO("Step a");

					}
					Utils.LOG_INFO("Step b");
				}
				Utils.LOG_INFO("Step c");
			}
			Utils.LOG_INFO("Step d");
		}
		Utils.LOG_INFO("Step 7");

		//Must have at least one energy hatch.
		if (this.mEnergyHatches != null) {
			for (int i = 0; i < this.mEnergyHatches.size(); i++) {
				if (this.mEnergyHatches.get(i).mTier < 2){
					Utils.LOG_INFO("You require at LEAST MV tier Energy Hatches.");
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					return false;
				}
			}
		}
		//Must have at least one output hatch.
		if (this.mOutputHatches != null) {
			for (int i = 0; i < this.mOutputHatches.size(); i++) {

				if (this.mOutputHatches.get(i).mTier < 2 && (this.mOutputHatches.get(i).getBaseMetaTileEntity() instanceof GregtechMTE_NuclearReactor)){
					Utils.LOG_INFO("You require at LEAST MV tier Output Hatches.");
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		//Must have at least one input hatch.
		if (this.mInputHatches != null) {
			for (int i = 0; i < this.mInputHatches.size(); i++) {
				if (this.mInputHatches.get(i).mTier < 2){
					Utils.LOG_INFO("You require at LEAST MV tier Input Hatches.");
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		mSolderingTool = true;
		//turnCasingActive(true);
		Utils.LOG_INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	public boolean addCasingToCasingList(IGregTechTileEntity aTileEntity) {
		if (aTileEntity == null)
			return false;
		IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null)
			return false;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_TieredMachineBlock) {
			//return this.mCasings.add((GT_MetaTileEntity_TieredMachineBlock) aMetaTileEntity);
		}
		return false;
	}

	private GT_MetaTileEntity_TieredMachineBlock changeTextureswithReflection(GT_MetaTileEntity_TieredMachineBlock casing, ITexture[][][] textureSet){
		GT_MetaTileEntity_TieredMachineBlock cv = casing;
		//System.out.println("Before: "+cv.mTextures.hashCode());
		//Get declared field from class
		Field f;
		try {
			Field[] x = cv.getClass().getFields();
			for (int i =0; i<x.length;i++){
				//Utils.LOG_INFO(x[i].getName());
			}
			try {
				//Try get the field variable
				f = cv.getClass().getField("mTextures");
				// set the accessiblity of the field to true, this will enable you to change the value
				f.setAccessible(true); 
				//change the field value
				f.set(cv, textureSet);
				//Verify change in texture set
				//System.out.println("After: "+cv.mTextures.hashCode());
				return cv;
			} catch (NoSuchFieldException e) {
				Utils.LOG_INFO("Could not find mTextures.");
				return casing;
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return casing;
		}
	}


	public ITexture[][][] getTextureSet() {
		ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = getFront(i);
			rTextures[1][i + 1] = getBack(i);
			rTextures[2][i + 1] = getBottom(i);
			rTextures[3][i + 1] = getTop(i);
			rTextures[4][i + 1] = getSides(i);
			rTextures[5][i + 1] = getFrontActive(i);
			rTextures[6][i + 1] = getBackActive(i);
			rTextures[7][i + 1] = getBottomActive(i);
			rTextures[8][i + 1] = getTopActive(i);
			rTextures[9][i + 1] = getSidesActive(i);
		}
		return rTextures;
	}

	public ITexture[] getFront(byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getBack(byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getBottom(byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getTop(byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getSides(byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getFrontActive(byte aColor) {
		return getFront(aColor);
	}

	public ITexture[] getBackActive(byte aColor) {
		return getBack(aColor);
	}

	public ITexture[] getBottomActive(byte aColor) {
		return getBottom(aColor);
	}

	public ITexture[] getTopActive(byte aColor) {
		return getTop(aColor);
	}

	public ITexture[] getSidesActive(byte aColor) {
		return getSides(aColor);
	}

}