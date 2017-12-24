package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.lang.reflect.Field;

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
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.forestry.trees.TreefarmManager;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntityAnimalFarm extends GT_MetaTileEntity_MultiBlockBase {


	private static final ITexture[] FACING_SIDE = {new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Tumbaga)};
	private static final ITexture[] FACING_FRONT = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE)};
	private static final ITexture[] FACING_ACTIVE = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE_ACTIVE)};


	//public ArrayList<GT_MetaTileEntity_TieredMachineBlock> mCasings = new ArrayList();

	private final boolean running = false;
	private boolean p1, p2, p3, p4, p5, p6;
	public ItemStack mOutputItem1;
	public ItemStack mOutputItem2;
	private Block Humus;
	private final boolean isForestryLoaded = TreefarmManager.isForestryValid();

	public GregtechMetaTileEntityAnimalFarm(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityAnimalFarm(final String aName) {
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
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
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
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean isAccessAllowed(final EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean allowCoverOnSide(final byte aSide, final GT_ItemStack aCoverID) {
		return (GregTech_API.getCoverBehavior(aCoverID.toStack()).isSimpleCover()) && (super.allowCoverOnSide(aSide, aCoverID));
	}

	@Override
	public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityAnimalFarm(this.mName);
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}

		return true;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		Logger.INFO("Working");
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
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {

		//this.mCasings.clear();
		Logger.INFO("Step 1");
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7;
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;

		for (int i = -7; i <= 7; i++) {
			Logger.INFO("Step 2");
			for (int j = -7; j <= 7; j++) {
				Logger.INFO("Step 3");
				for (int h = 0; h <= 1; h++) {
					Logger.INFO("Step 4");

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					//Farm Floor inner 14x14
					if (((i != -7) && (i != 7)) && ((j != -7) && (j != 7))) {
						Logger.INFO("Step 5 - H:"+h);
						// Farm Dirt Floor and Inner Air/Log space.
						if (h == 0) {
							//Dirt Floor
							if (!TreefarmManager.isDirtBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
								Logger.INFO("Dirt like block missing from inner 14x14.");
								Logger.INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
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
						Logger.INFO("Step 6 - H:"+h);
						//Deal with all 4 sides (Fenced area)
						if (h == 1) {
							if (!TreefarmManager.isFenceBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
								Logger.INFO("Fence/Gate missing from outside the second layer.");
								Logger.INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}
						}
						//Deal with Bottom edges (Add Hatches/Busses first, othercheck make sure it's dirt) //TODO change the casings to not dirt~
						else if (h == 0) {

							try {
								this.addCasingToCasingList(tTileEntity);
							} catch (final Throwable t){}

							if ((!this.addMaintenanceToMachineList(tTileEntity, 77)) && (!this.addInputToMachineList(tTileEntity, 77)) && (!this.addOutputToMachineList(tTileEntity, 77)) && (!this.addEnergyInputToMachineList(tTileEntity, 77))) {
								if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller

									if (!(aBaseMetaTileEntity.getMetaTileID() != 752)) {
										Logger.INFO("Fark Keeper Casings Missing from one of the edges on the bottom edge. x:"+(xDir+i)+" y:"+h+" z:"+(zDir+j)+" | "+aBaseMetaTileEntity.getClass());
										Logger.INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									Logger.INFO("Found a farm keeper.");
								}
							}
						}
						Logger.INFO("Step a");

					}
					Logger.INFO("Step b");
				}
				Logger.INFO("Step c");
			}
			Logger.INFO("Step d");
		}
		Logger.INFO("Step 7");

		//Must have at least one energy hatch.
		if (this.mEnergyHatches != null) {
			for (int i = 0; i < this.mEnergyHatches.size(); i++) {
				if (this.mEnergyHatches.get(i).mTier < 2){
					Logger.INFO("You require at LEAST MV tier Energy Hatches.");
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					return false;
				}
			}
		}
		//Must have at least one output hatch.
		if (this.mOutputHatches != null) {
			for (int i = 0; i < this.mOutputHatches.size(); i++) {

				if ((this.mOutputHatches.get(i).mTier < 2) && (this.mOutputHatches.get(i).getBaseMetaTileEntity() instanceof GregtechMTE_NuclearReactor)){
					Logger.INFO("You require at LEAST MV tier Output Hatches.");
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		//Must have at least one input hatch.
		if (this.mInputHatches != null) {
			for (int i = 0; i < this.mInputHatches.size(); i++) {
				if (this.mInputHatches.get(i).mTier < 2){
					Logger.INFO("You require at LEAST MV tier Input Hatches.");
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		this.mSolderingTool = true;
		//turnCasingActive(true);
		Logger.INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	public boolean addCasingToCasingList(final IGregTechTileEntity aTileEntity) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_TieredMachineBlock) {
			//return this.mCasings.add((GT_MetaTileEntity_TieredMachineBlock) aMetaTileEntity);
		}
		return false;
	}

	private GT_MetaTileEntity_TieredMachineBlock changeTextureswithReflection(final GT_MetaTileEntity_TieredMachineBlock casing, final ITexture[][][] textureSet){
		final GT_MetaTileEntity_TieredMachineBlock cv = casing;
		//System.out.println("Before: "+cv.mTextures.hashCode());
		//Get declared field from class
		Field f;
		try {
			final Field[] x = cv.getClass().getFields();
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
			} catch (final NoSuchFieldException e) {
				Logger.INFO("Could not find mTextures.");
				return casing;
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return casing;
		}
	}


	public ITexture[][][] getTextureSet() {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFrontActive(i);
			rTextures[6][i + 1] = this.getBackActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public ITexture[] getFrontActive(final byte aColor) {
		return this.getFront(aColor);
	}

	public ITexture[] getBackActive(final byte aColor) {
		return this.getBack(aColor);
	}

	public ITexture[] getBottomActive(final byte aColor) {
		return this.getBottom(aColor);
	}

	public ITexture[] getTopActive(final byte aColor) {
		return this.getTop(aColor);
	}

	public ITexture[] getSidesActive(final byte aColor) {
		return this.getSides(aColor);
	}

}