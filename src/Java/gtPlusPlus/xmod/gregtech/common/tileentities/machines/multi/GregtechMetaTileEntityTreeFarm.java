package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static gtPlusPlus.core.lib.CORE.configSwitches.enableTreeFarmerParticles;
import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.forestry.trees.TreefarmManager;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class GregtechMetaTileEntityTreeFarm extends GT_MetaTileEntity_MultiBlockBase {

	public ArrayList<GT_MetaTileEntity_TieredMachineBlock> mCasings = new ArrayList();

	/* private */ private int treeCheckTicks = 0;
	/* private */ private int plantSaplingTicks = 0;
	/* private */ private int cleanupTicks = 0;
	/* private */ private boolean canChop = false;

	public GregtechMetaTileEntityTreeFarm(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityTreeFarm(final String aName) {
		super(aName);
	}   

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Tree Farmer",
				"How to get your first logs without an axe.",
				"Max Size(WxHxD): 9x1x9 (Controller, with upto 4 dirt out each direction on a flat plane.)"
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
		return new GregtechMetaTileEntityTreeFarm(this.mName);
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
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

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {

		this.mCasings.clear();
		Utils.LOG_WARNING("Step 1");
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;

		for (int i = -7; i <= 7; i++) {
			Utils.LOG_WARNING("Step 2");
			for (int j = -7; j <= 7; j++) {
				Utils.LOG_WARNING("Step 3");
				for (int h = 0; h <= 1; h++) {
					Utils.LOG_WARNING("Step 4");

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					//Farm Floor inner 14x14
					if ((i != -7 && i != 7) && (j != -7 && j != 7)) {
						Utils.LOG_WARNING("Step 5 - H:"+h);
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
						//else if (h == 1){		
						//Farm Inner 14x14
						/*if (!TreefarmManager.isWoodLog(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)) || !TreefarmManager.isAirBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)) || !aBaseMetaTileEntity.getAirOffset(xDir+i, h, zDir+j)) {
								Utils.LOG_INFO("Wood like block missing from inner 14x14, layer 2."); //TODO
								Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());	
								Utils.LOG_INFO("Found at x:"+(xDir+i)+" y:"+h+" z:"+(zDir+j));
								//return false;
								}*/	
						//}			

					}
					//Dealt with inner 5x5, now deal with the exterior.
					else {
						Utils.LOG_WARNING("Step 6 - H:"+h);
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
							if ((!addMaintenanceToMachineList(tTileEntity, 77)) && (!addInputToMachineList(tTileEntity, 77)) && (!addOutputToMachineList(tTileEntity, 77)) && (!addEnergyInputToMachineList(tTileEntity, 77))) {
								if ((xDir + i != 0) || (zDir + j != 0)) {//no controller			

									if (tTileEntity.getMetaTileID() != 752) {
										Utils.LOG_INFO("Farm Keeper Casings Missing from one of the edges on the bottom edge. x:"+(xDir+i)+" y:"+h+" z:"+(zDir+j)+" | "+aBaseMetaTileEntity.getClass());
										Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" "+tTileEntity.getMetaTileID());
										return false;
									}
									Utils.LOG_WARNING("Found a farm keeper.");
								}
							}
						}
						Utils.LOG_WARNING("Step a");

					}
					Utils.LOG_WARNING("Step b");
				}
				Utils.LOG_WARNING("Step c");
			}
			Utils.LOG_WARNING("Step d");
		}
		Utils.LOG_WARNING("Step 7");

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
		//Utils.LOG_INFO("Multiblock Formed.");
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

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}


	//Tree Manager

	private void tickTrees(){
		if (treeCheckTicks > 200){
			treeCheckTicks = 0;
		}
		else {
			treeCheckTicks++;
		}
	}

	private void tickSaplings(){
		if (plantSaplingTicks > 200){
			plantSaplingTicks = 0;
		}
		else {
			plantSaplingTicks++;
		}
	}

	private void tickCleanup(){
		if (cleanupTicks > 600){
			cleanupTicks = 0;
		}
		else {
			cleanupTicks++;
		}
	}

	private void tickHandler(){
		//Count Sapling Timer
		tickSaplings();
		//Count Tree Cutting Timer
		tickTrees();
		//Tick Cleanup script Timer.
		tickCleanup();
	}


	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {

		if (aBaseMetaTileEntity.isServerSide()) {
			//Check Inventory slots [1] - Find a valid Buzzsaw Blade or a Saw
			try {
				Utils.LOG_INFO(mInventory[1].getDisplayName());
			} catch (NullPointerException t){}

			//If Machine can work and it's only once every 5 seconds this will tick.
			if (mMachine){
				//Set Machine State
				if (treeCheckTicks == 200){
					Utils.LOG_INFO("Looking For Trees - Serverside | "+treeCheckTicks);
					//Find wood to Cut
					findLogs(aBaseMetaTileEntity);
				}
			}	
			else {
				if (plantSaplingTicks == 100){
					Utils.LOG_INFO("Looking For space to plant saplings - Serverside | "+plantSaplingTicks);
					//Plant Some Saplings
					plantSaplings(aBaseMetaTileEntity);
				}	
				else if (plantSaplingTicks == 200){
					Utils.LOG_INFO("Looking For Saplings to grow - Serverside | "+plantSaplingTicks);
					//Try Grow some Saplings
					findSaplings(aBaseMetaTileEntity);
					//Set can work state
					mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);
				}
			}
			//Call Cleanup Task last, before ticking.
			if (cleanupTicks == 600){
				Utils.LOG_INFO("Looking For rubbish to cleanup - Serverside | "+cleanupTicks);
				cleanUp(aBaseMetaTileEntity);
			}
			//Tick TE
			tickHandler();
		}
		//Client Side - do nothing

	}

	private boolean findLogs(final IGregTechTileEntity aBaseMetaTileEntity){

		Utils.LOG_INFO("called findLogs()");
		int logsCut = 0;

		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		for (int i = -7; i <= 7; i++) {
			for (int j = -7; j <= 7; j++) {
				for (int h=1;h<150;h++){

					//Farm Floor inner 14x14
					if ((i != -7 && i != 7) && (j != -7 && j != 7)) {							

						//Farm Inner 13*13
						if (TreefarmManager.isWoodLog(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)) || TreefarmManager.isWoodLog(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){
							//Utils.LOG_INFO("Found A log of some kind I can chop.");
							if (this.mEnergyHatches.size() > 0) {
								for (final GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches){
									if (isValidMetaTileEntity(tHatch)) {
										//Utils.LOG_INFO("Hatch ["+"]| can hold:"+maxEUStore()+" | holding:"+tHatch.getEUVar());
										if (tHatch.getEUVar() >= 128) {
											//Utils.LOG_INFO("I should cut wood instead of print messages.");
											//Utils.LOG_INFO("Found at x:"+(xDir+i)+" y:"+h+" z:"+(zDir+j));
											logsCut++;												
											//tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(128 * 1, false);

											final World world = aBaseMetaTileEntity.getWorld();
											int posX, posY, posZ;
											posX = aBaseMetaTileEntity.getXCoord()+xDir+i;
											posY = aBaseMetaTileEntity.getYCoord()+h;
											posZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
											cutLog(world, posX, posY, posZ);
											//Cut A Log
										}
										else {
											//Utils.LOG_INFO("Not enough Power | can hold:"+maxEUStore()+" | holding:"+aBaseMetaTileEntity.getStoredEU());
										}
									}
									else {
										Utils.LOG_INFO("Invalid Hatch Entitity");
									}
									//End For loop
								}
							}
							else {
								Utils.LOG_INFO("No energy hatches found.");
							}
						}

					}
					/*else { 
						Utils.LOG_INFO("tried scanning edge or fence");
						return false;
					}*/

				}
			}
		}
		mMachine = false;
		if (logsCut > 0)
			cleanUp(aBaseMetaTileEntity);
		//Utils.LOG_INFO("general failure | maybe there is no logs, not an error. | cut:"+logsCut );
		return false;		
	}

	private static boolean cleanUp(final IGregTechTileEntity aBaseMetaTileEntity){
		Utils.LOG_INFO("called cleanUp()");
		int cleanedUp = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		for (int i = -10; i <= 10; i++) {
			for (int j = -10; j <= 10; j++) {
				for (int h=1;h<175;h++){
					if (TreefarmManager.isLeaves(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)) || TreefarmManager.isWoodLog(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){
						int posiX, posiY, posiZ;
						posiX = aBaseMetaTileEntity.getXCoord()+xDir+i;
						posiY = aBaseMetaTileEntity.getYCoord()+h;
						posiZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
						Utils.LOG_INFO("Cleaning Up some leftovers.");
						cleanedUp++;
						aBaseMetaTileEntity.getWorld().setBlockToAir(posiX, posiY, posiZ);
					}

				}

			}
		}
		Utils.LOG_INFO("cleaning up | "+cleanedUp );
		return true;		
	}

	private static boolean findSaplings(final IGregTechTileEntity aBaseMetaTileEntity){
		Utils.LOG_INFO("called findSaplings()");
		int saplings = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		for (int i = -7; i <= 7; i++) {
			for (int j = -7; j <= 7; j++) {
				int h = 1;
				//Utils.LOG_INFO("Looking for saplings.");
				if (TreefarmManager.isSapling(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){
					int posiX, posiY, posiZ;
					posiX = aBaseMetaTileEntity.getXCoord()+xDir+i;
					posiY = aBaseMetaTileEntity.getYCoord()+h;
					posiZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
					Utils.LOG_INFO("Found a sapling to grow.");
					saplings++;
					applyBonemeal(aBaseMetaTileEntity.getWorld(), posiX, posiY, posiZ);
				}				
			}
		}
		Utils.LOG_INFO("Tried to grow saplings: | "+saplings );
		return true;		
	}
	
	public ArrayList<ItemStack> getStoredInputsEx() {
		ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
		for (GT_MetaTileEntity_Hatch_InputBus tHatch : this.mInputBusses) {
			tHatch.mRecipeMap = getRecipeMap();
			if (isValidMetaTileEntity(tHatch)) {
				for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; --i) {
					if (tHatch.getBaseMetaTileEntity().getStackInSlot(i) == null)
						continue;
					rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
				}
			}
		}
		return rList;
	}

	private boolean plantSaplings(final IGregTechTileEntity aBaseMetaTileEntity){
		Utils.LOG_INFO("called plantSaplings()");
		World world = aBaseMetaTileEntity.getWorld();
		ArrayList<ItemStack> r = getStoredInputs();
		int saplings = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		for (int i = -7; i <= 7; i++) {
			for (int j = -7; j <= 7; j++) {
				int h = 1;			

				if ((i != -7 && i != 7) && (j != -7 && j != 7)) {		
					if (TreefarmManager.isAirBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){
						//Utils.LOG_INFO("Found air");
						if (r.size() > 0){
							Utils.LOG_INFO("r was not null. "+r.size());
							for (ItemStack n : r){
								Utils.LOG_INFO("found "+n.getDisplayName());
								if (OrePrefixes.sapling.contains(n) || n.getDisplayName().toLowerCase().contains("sapling")){
									//Utils.LOG_INFO(""+n.getDisplayName());

									int posX, posY, posZ;
									posX = aBaseMetaTileEntity.getXCoord()+xDir+i;
									posY = aBaseMetaTileEntity.getYCoord()+h;
									posZ = aBaseMetaTileEntity.getZCoord()+zDir+j;

									//Works for everything but forestry saplings - TODO
									Block saplingToPlace;

									if (n.getClass().getName().toLowerCase().contains("forestry")){
										Utils.LOG_INFO("It's a forestry sapling, trying magic.");
										saplingToPlace = Block.getBlockFromItem(ItemUtils.getItem("Forestry:saplingGE"));

									}
									else {
										saplingToPlace = Block.getBlockFromItem(n.getItem());									
									}

									

									//If sapling block is not null
									if (saplingToPlace != null){
										Utils.LOG_INFO("Placing Sapling Block.");
										//Plant Sapling
										world.setBlock(posX, posY, posZ, saplingToPlace);
										world.setBlockMetadataWithNotify(posX, posY, posZ, n.getItemDamage(), 4);
										//Deplete Input stack
										depleteInput(n);
										//r = getStoredInputs();
										break;
									}
									Utils.LOG_INFO(n.getDisplayName()+" did not have a valid block.");
								}
								else {
									Utils.LOG_INFO("item was not a sapling");
								}
							}
						}
						else{
							Utils.LOG_INFO("Input stack empty or null - hatch count "+this.mInputBusses.size());
						}
					}	
					else {
						Utils.LOG_INFO("No space for sapling, no air.");					
					}
					}			
				
			}
		}
		Utils.LOG_INFO("Tried to grow saplings: | "+saplings );
		return true;		
	}

	private boolean cutLog(final World world, final int x, final int y, final int z){
		Utils.LOG_INFO("Cutting Log");
		try {
			//Get Log.
			final Block block = world.getBlock(x, y, z);
			//Add the stack to the bus.

			int dropMeta = world.getBlockMetadata(x, y, z);
			ArrayList<ItemStack> blockDrops = block.getDrops(world, x, y, z, dropMeta, 0);
			ItemStack[] drops = ItemUtils.getBlockDrops(blockDrops);
			if (drops != null){				
				for (ItemStack outputs : drops){
					Utils.LOG_INFO("Adding 1x "+outputs.getDisplayName());
					addOutput(outputs);
					//Update bus contents.
					updateSlots();
				}				
				//Remove drop that was added to the bus.
				world.setBlockToAir(x, y, z);
				return true;
			}			

		} catch (NullPointerException e){}
		return false;
	}

	public static boolean applyBonemeal(World world, int intX, int intY, int intZ){
		Block block = world.getBlock(intX, intY, intZ);
		int chance = MathUtils.randInt(0, 10);

		//Random Growth
		if (chance < 8){
			return false;
		}

		EntityPlayer player = FakePlayerFactory.getMinecraft((WorldServer)world);
		if (!world.isRemote){
			if (enableTreeFarmerParticles){
				world.playAuxSFX(2005, intX, intY, intZ, 0);
			}
		}
		BonemealEvent event = new BonemealEvent(player, world, block, intX, intY, intZ);
		if (MinecraftForge.EVENT_BUS.post(event)){
			Utils.LOG_INFO("Not sure why this returned false");
			return false;
		}
		if (event.getResult() == Result.ALLOW){
			if (!world.isRemote){
				world.playAuxSFX(2005, intX, intY, intZ, 0);
			}
			return true;
		}
		if (block instanceof IGrowable){
			IGrowable igrowable = (IGrowable)block;
			if (igrowable.func_149851_a(world, intX, intY, intZ, world.isRemote)){
				if (!world.isRemote){
					if (igrowable.func_149852_a(world, world.rand, intX, intY, intZ)){
						igrowable.func_149853_b(world, world.rand, intX, intY, intZ);
					}
				}				
				return true;
			}
		}
		return false;
	}

	public static ITexture[][][] getTextureSet() {
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

	public static ITexture[] getFront(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getFrontActive(final byte aColor) {
		return getFront(aColor);
	}

	public static ITexture[] getBackActive(final byte aColor) {
		return getBack(aColor);
	}

	public static ITexture[] getBottomActive(final byte aColor) {
		return getBottom(aColor);
	}

	public static ITexture[] getTopActive(final byte aColor) {
		return getTop(aColor);
	}

	public static ITexture[] getSidesActive(final byte aColor) {
		return getSides(aColor);
	}


	public boolean depleteInputEx(ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack))	return false;

		Utils.LOG_INFO("Taking one sapling away from in input bus.");

		for (GT_MetaTileEntity_Hatch_InputBus tHatch : this.mInputBusses) {
			tHatch.mRecipeMap = getRecipeMap();
			if (isValidMetaTileEntity(tHatch)) {
				for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; --i) {
					if ((!(GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(i)))) || (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize < aStack.stackSize)){
						continue;
					}
					tHatch.getBaseMetaTileEntity().decrStackSize(0,1);
					return true;
				}
			}

		}

		return false;
	}

}