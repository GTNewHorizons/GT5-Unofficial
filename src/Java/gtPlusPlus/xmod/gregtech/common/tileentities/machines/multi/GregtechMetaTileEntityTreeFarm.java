package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import forestry.api.genetics.IIndividual;
import forestry.core.utils.GeneticsUtil;
import forestry.plugins.PluginManager;
import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.players.FakeFarmer;
import gtPlusPlus.core.slots.SlotBuzzSaw.SAWTOOL;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.particles.BlockBreakParticles;
import gtPlusPlus.xmod.forestry.trees.TreefarmManager;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_TreeFarmer;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_TreeFarmer;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.Optional;

public class GregtechMetaTileEntityTreeFarm extends GT_MetaTileEntity_MultiBlockBase {

	/* private */ private int treeCheckTicks = 0;
	/* private */ private int plantSaplingTicks = 0;
	/* private */ private int cleanupTicks = 0;
	/* private */ public long mInternalPower = 0;	
	/* private */ private static int powerDrain = 32;

	private short energyHatchRetryCount = 0;

	private EntityPlayerMP farmerAI;

	private SAWTOOL mCurrentMachineTool = SAWTOOL.NONE;
	private boolean canChop = false;

	private int cuttingNumber = 0;
	private int cuttingNumber2 = 0;
	private int cuttingNumber3 = 0;

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
				"Size(WxHxD): 15x2x15",
				"Purple: Farm Keeper Blocks",
				"Dark Purple: Dirt/Grass/Podzol/Humus",
				"Light Blue: Fence/Fence Gate",
				"Blue/Yellow: Controller"
		};
	}

	public long getStoredInternalPower(){
		//Utils.LOG_MACHINE_INFO("returning "+this.mInternalPower+"EU to the called method.");
		return this.mInternalPower;
	}


	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setLong("mInternalPower", this.mInternalPower);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mInternalPower = aNBT.getLong("mInternalPower");
		super.loadNBTData(aNBT);
	}

	@Override
	public boolean drainEnergyInput(long aEU) {
		this.mInternalPower = (this.mInternalPower-32);
		Utils.LOG_MACHINE_INFO("Draining internal power storage by 32EU. Stored:"+this.mInternalPower);	
		return true;
	}

	public boolean addPowerToInternalStorage(IGregTechTileEntity aBaseMetaTileEntity){
		if (this.mEnergyHatches.size() > 0) {
			for (final GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches){
				if (isValidMetaTileEntity(tHatch)) {
					if (tHatch.getEUVar() >= 128) {	
						for (int o=0;o<(tHatch.getEUVar()/128);o++){
							if (this.mInternalPower<(maxEUStore()-128)){
								tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(128, false);	
								this.mInternalPower = (this.mInternalPower+128);		
								//Utils.LOG_MACHINE_INFO("Increasing internal power storage by 128EU. Stored:"+this.mInternalPower);						
							}
						}
					}
				}
				else {
					Utils.LOG_INFO("Bad Power hatch to obtain energy from.");
				}
			}
		}	
		else {			
			if (energyHatchRetryCount <= 10){
				energyHatchRetryCount++;
				Utils.LOG_INFO("No energy hatches found.");
			}
			else {
				Utils.LOG_MACHINE_INFO("Rechecking for Energy hatches.");
				energyHatchRetryCount = 0;
				this.mEnergyHatches = new ArrayList<GT_MetaTileEntity_Hatch_Energy>();
				checkMachine(aBaseMetaTileEntity, mInventory[1]);
			}			
		}
		return true;
	}

	@Override
	public long maxEUStore() {
		return 3244800; //13*13*150*128
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == 1) {
			return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log), new GT_RenderedTexture(canChop ? TexturesGtBlock.Overlay_Machine_Vent_Fast : TexturesGtBlock.Overlay_Machine_Vent)};
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
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_TreeFarmer(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "TreeFarmer.png");
	}	

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_TreeFarmer(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) {
			return true;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		boolean isValid = false;
		SAWTOOL currentInputItem = TreeFarmHelper.isCorrectMachinePart(aStack);
		if (currentInputItem != SAWTOOL.NONE){
			if (currentInputItem == SAWTOOL.SAW){
				mCurrentMachineTool = SAWTOOL.SAW;
			}
			else {
				mCurrentMachineTool = SAWTOOL.BUZZSAW;	
			}
			isValid = true;
		}
		else {
			//Utils.LOG_MACHINE_INFO("Found "+aStack.getDisplayName());
		}
		return isValid;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		Utils.LOG_MACHINE_INFO("Working");	
		this.mEfficiency = 0;
		this.mEfficiencyIncrease = 0;
		this.mMaxProgresstime = 0;
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
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
								Utils.LOG_MACHINE_INFO("Dirt like block missing from inner 14x14.");
								Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}							
						}
					}
					//Dealt with inner 5x5, now deal with the exterior.
					else {
						Utils.LOG_WARNING("Step 6 - H:"+h);
						//Deal with all 4 sides (Fenced area)
						if (h == 1) {														
							if (!TreefarmManager.isFenceBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
								Utils.LOG_MACHINE_INFO("Fence/Gate missing from outside the second layer.");
								Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}					
						}
						//Deal with Bottom edges (Add Hatches/Busses first, othercheck make sure it's dirt) //TODO change the casings to not dirt~
						else if (h == 0) {
							if ((!addMaintenanceToMachineList(tTileEntity, 77)) && (!addInputToMachineList(tTileEntity, 77)) && (!addOutputToMachineList(tTileEntity, 77)) && (!addEnergyInputToMachineList(tTileEntity, 77))) {
								if ((xDir + i != 0) || (zDir + j != 0)) {//no controller			

									if (tTileEntity.getMetaTileID() != 752) {
										Utils.LOG_MACHINE_INFO("Farm Keeper Casings Missing from one of the edges on the bottom edge. x:"+(xDir+i)+" y:"+h+" z:"+(zDir+j)+" | "+aBaseMetaTileEntity.getClass());
										Utils.LOG_MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" "+tTileEntity.getMetaTileID());
										return false;
									}
									Utils.LOG_WARNING("Found a farm keeper.");
								}
							}
						}
					}
				}
			}
		}

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
		this.mSolderingTool = true;
		//turnCasingActive(true);
		//Utils.LOG_MACHINE_INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 1;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 1;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}


	private boolean isMachineRepaired(){
		if (this.mSolderingTool || this.mCrowbar || this.mHardHammer || this.mScrewdriver || this.mSoftHammer || this.mWrench){
			return true;
		}		
		return false;
	}



	private boolean findLogs(final IGregTechTileEntity aBaseMetaTileEntity){

		Utils.LOG_MACHINE_INFO("called findLogs()");
		int logsCut = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		final World world = aBaseMetaTileEntity.getWorld();
		int posX, posY, posZ;

		if (this.mEnergyHatches.size() > 0) {
			for (final GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches){
				if (isValidMetaTileEntity(tHatch)) {
					if (mInternalPower >= 128) {						

						for (int i = -7; i <= 7; i++) {
							for (int j = -7; j <= 7; j++) {
								for (int h=1;h<150;h++){
									if ((i != -7 && i != 7) && (j != -7 && j != 7)) {										
										Block loopBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);						
										if (TreefarmManager.isWoodLog(loopBlock) || TreefarmManager.isLeaves(loopBlock)){														
											long tempStoredEU = mInternalPower;											
											if (tempStoredEU >= powerDrain){										
												Utils.LOG_MACHINE_INFO("Cutting a "+loopBlock.getLocalizedName()+", currently stored:"+tempStoredEU+" | max:"+maxEUStore());
												drainEnergyInput(powerDrain);

												long tempStoredEU2 = mInternalPower;
												if (tempStoredEU != tempStoredEU2){
													if (tempStoredEU == (tempStoredEU2+powerDrain)){
														Utils.LOG_MACHINE_INFO(powerDrain+"EU was drained.");													
													}
													else {
														Utils.LOG_MACHINE_INFO(""+(tempStoredEU-tempStoredEU2)+"EU was drained.");
													}
												}
												else {
													Utils.LOG_MACHINE_INFO("Stored EU did not change.");	
												}

												posX = aBaseMetaTileEntity.getXCoord()+xDir+i;
												posY = aBaseMetaTileEntity.getYCoord()+h;
												posZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
												cutLog(world, posX, posY, posZ);
												logsCut++;											
											}
											else {
												Utils.LOG_MACHINE_INFO("Not enough power to cut.");
											}										
										}
									}
								}
							}
						}	
					}
					else {
						Utils.LOG_MACHINE_INFO("Not enough Power | can hold:"+maxEUStore()+" | holding:"+aBaseMetaTileEntity.getStoredEU());
					}
				}
				else {
					Utils.LOG_MACHINE_INFO("Invalid Hatch Entitity");
				}
			}
		}
		else {
			Utils.LOG_MACHINE_INFO("No energy hatches found.");
		}


		canChop = false;
		if (logsCut > 250)
			TreeFarmHelper.cleanUp(aBaseMetaTileEntity);
		//Utils.LOG_MACHINE_INFO("general failure | maybe there is no logs, not an error. | cut:"+logsCut );
		return false;		
	}

	private boolean growSaplingsWithBonemeal(final IGregTechTileEntity aBaseMetaTileEntity){
		Utils.LOG_MACHINE_INFO("called growSaplingsWithBonemeal()");
		int saplings = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		for (int i = -7; i <= 7; i++) {
			for (int j = -7; j <= 7; j++) {
				int h = 1;
				//Utils.LOG_MACHINE_INFO("Looking for saplings.");
				if (TreefarmManager.isSapling(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){
					int posiX, posiY, posiZ;
					posiX = aBaseMetaTileEntity.getXCoord()+xDir+i;
					posiY = aBaseMetaTileEntity.getYCoord()+h;
					posiZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
					//Utils.LOG_MACHINE_INFO("Found a sapling to grow.");
					saplings++;

					if (depleteFertiliser()){
						if (drainEnergyInput(powerDrain)){
							short fertTier = getFertiliserTier(getCurrentFertiliserStack());
							TreeFarmHelper.applyBonemeal(getFakePlayer(), aBaseMetaTileEntity.getWorld(), posiX, posiY, posiZ, fertTier);	
						}
						else {
							Utils.LOG_MACHINE_INFO("x3");
							break;
						}
					}
					else {
						Utils.LOG_MACHINE_INFO("x2");
						break;
					}					
				}				
			}
		}
		Utils.LOG_MACHINE_INFO("Tried to grow saplings: | "+saplings );
		return true;		
	}

	private boolean plantSaplings(final IGregTechTileEntity aBaseMetaTileEntity){
		Utils.LOG_MACHINE_INFO("called plantSaplings()");
		World world = aBaseMetaTileEntity.getWorld();
		ArrayList<ItemStack> r = getStoredInputs();
		int saplings = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7; 
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		int counter = 0;
		if (r.size() > 0){
			Utils.LOG_MACHINE_INFO("| r was not null. "+r.size()+" |");
			OUTER : for (ItemStack n : r){
				Utils.LOG_MACHINE_INFO("found "+n.getDisplayName());
				if (OrePrefixes.sapling.contains(n) || n.getDisplayName().toLowerCase().contains("sapling")){
					Utils.LOG_MACHINE_INFO(""+n.getDisplayName());

					counter = n.stackSize;
					Block saplingToPlace = Block.getBlockFromItem(n.getItem());						


					//Find Gaps for Saplings after scanning Item Busses
					for (int i = -7; i <= 7; i++) {
						INNER : for (int j = -7; j <= 7; j++) {
							int h = 1;
							if (counter > 0){								
								if ((i != -7 && i != 7) && (j != -7 && j != 7)) {		
									if (TreefarmManager.isAirBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){									

										//Get block location to place sapling block
										int posX, posY, posZ;
										posX = aBaseMetaTileEntity.getXCoord()+xDir+i;
										posY = aBaseMetaTileEntity.getYCoord()+h;
										posZ = aBaseMetaTileEntity.getZCoord()+zDir+j;

										//If sapling block is not null || Ignore if forestry is loaded.
										if ((saplingToPlace != null && !LoadedMods.Forestry) || LoadedMods.Forestry){

											//Plant Sapling
											if (!LoadedMods.Forestry){
												world.setBlock(posX, posY, posZ, saplingToPlace);
												world.setBlockMetadataWithNotify(posX, posY, posZ, n.getItemDamage(), 4);
											}
											else {
												plantSaplingAt(n, this.getBaseMetaTileEntity().getWorld(), posX, posY, posZ);
											}											

											//Deplete Input stack
											depleteInputEx(n);
											drainEnergyInput(powerDrain);
											counter--;
											//Update slot contents?
											updateSlots();

											//Test If Inputs Changed
											ArrayList<ItemStack> temp = getStoredInputs();
											if (r != temp){
												Utils.LOG_MACHINE_INFO("Inputs changed, updating.");
												for (ItemStack xr : r){
													Utils.LOG_MACHINE_INFO("xr:"+xr.getDisplayName()+"x"+xr.stackSize);
												}
												for (ItemStack xc : temp){
													Utils.LOG_MACHINE_INFO("xc:"+xc.getDisplayName()+"x"+xc.stackSize);
												}
												r = temp;											
											}
										}
										else {
											Utils.LOG_MACHINE_INFO(n.getDisplayName()+" did not have a valid block.");										
										}
									}	
									else {
										//Utils.LOG_MACHINE_INFO("No space for sapling, no air.");
										continue INNER;
									}
								}

							} //TODO
							else {
								break OUTER;
							}

						}
					}
				}
				else {
					Utils.LOG_MACHINE_INFO("item was not a sapling");
					continue OUTER;
				}
			}
		}
		else{
			/*Utils.LOG_MACHINE_INFO("Input stack empty or null - hatch count "+this.mInputBusses.size());
			for (GT_MetaTileEntity_Hatch_InputBus x : this.mInputBusses){
				Utils.LOG_MACHINE_INFO("x:"+x.getBaseMetaTileEntity().getXCoord()+" | y:"+x.getBaseMetaTileEntity().getYCoord()+" | z:"+x.getBaseMetaTileEntity().getZCoord());
			}*/
		}
		Utils.LOG_MACHINE_INFO("Tried to plant saplings: | "+saplings );
		return true;		
	}

	@Optional.Method(modid = "Forestry")
	public boolean plantSaplingAt(ItemStack germling, World world, int x, int y, int z) {
		Utils.LOG_MACHINE_INFO("Planting Sapling with Forestry method, since it's installed.");
		if (PluginManager.Module.ARBORICULTURE.isEnabled()) {
			IIndividual tree = GeneticsUtil.getGeneticEquivalent(germling);
			if (!(tree instanceof ITree)) {
				return false;
			}
			return TreeManager.treeRoot.plantSapling(world, (ITree) tree, getFakePlayer().getGameProfile(), x, y, z);
		}
		return germling.copy().tryPlaceItemIntoWorld(getFakePlayer(), world, x, y - 1, z, 1, 0.0F, 0.0F, 0.0F);
	}

	private boolean cutLog(final World world, final int x, final int y, final int z){
		//Utils.LOG_MACHINE_INFO("Cutting Log");
		try {
			final Block block = world.getBlock(x, y, z);			
			int chanceForLeaves = 1000;
			//is it leaves or a log? if leaves, heavily reduce chance to obtain rubber
			if (block.getUnlocalizedName().toLowerCase().contains("leaves")){
				chanceForLeaves = MathUtils.randInt(1, 1000);
				if (chanceForLeaves > 990)
					Utils.LOG_MACHINE_INFO("Found some leaves that will drop, chance to drop item "+chanceForLeaves+", needed 990-1000.");
			}

			//IC2 Sticky Rubber handling
			if (block.getUnlocalizedName().toLowerCase().contains("blockrubwood") || block.getUnlocalizedName().toLowerCase().contains("blockrubleaves")){
				ItemStack rubberResin = ItemUtils.getCorrectStacktype("IC2:itemHarz", 1);
				int chanceForRubber = MathUtils.randInt(1, 10);
				int multiplier = MathUtils.randInt(1, 3);								
				if (chanceForRubber > 7 && chanceForLeaves > 990){
					rubberResin.stackSize = multiplier;
					Utils.LOG_MACHINE_INFO("Adding "+rubberResin.getDisplayName()+" x"+rubberResin.stackSize);
					addOutput(rubberResin);
					updateSlots();
				}				
			}

			int dropMeta = world.getBlockMetadata(x, y, z);
			ArrayList<ItemStack> blockDrops = block.getDrops(world, x, y, z, dropMeta, 0);
			ItemStack[] drops = ItemUtils.getBlockDrops(blockDrops);

			//Add Drops
			if (drops != null){				
				for (ItemStack outputs : drops){
					if (chanceForLeaves > 990){
						Utils.LOG_MACHINE_INFO("Adding 1x "+outputs.getDisplayName());
						addOutput(outputs);
						//Update bus contents.
						updateSlots();
					}
				}				

			}	

			//Remove drop that was added to the bus.
			world.setBlockToAir(x, y, z);
			new BlockBreakParticles(world, x, y, z, block);
			return true;


		} catch (NullPointerException e){}
		return false;
	}

	public FluidStack[] getStoredInputFluids(){
		ArrayList<FluidStack> fluidArray = this.getStoredFluids();
		if (fluidArray.size() < 1){
			return new FluidStack[] {};
		}
		FluidStack storedInputFluids[] = new FluidStack[fluidArray.size()];
		if (storedInputFluids.length >= 1){
			int counter = 0;
			for (FluidStack inputPuddle : fluidArray){
				storedInputFluids[counter] = inputPuddle;
				counter++;
			}
			return storedInputFluids;			
		}
		return new FluidStack[] {};
	}

	public boolean doesInputHatchContainAnyFertiliser(){
		FluidStack[] tempFluids = getStoredInputFluids();
		if (tempFluids.length >= 1){
			for (FluidStack f : tempFluids){
				if (f.isFluidEqual(TreeFarmHelper.fertT1) || f.isFluidEqual(TreeFarmHelper.fertT2) || f.isFluidEqual(TreeFarmHelper.fertT3)){
					return true;
				}
			}
		}
		else {
			Utils.LOG_MACHINE_INFO("No fertiliser found.");
			return false;
		}
		return false;
	}

	public short getFertiliserTier(FluidStack f){
		if (f.isFluidEqual(TreeFarmHelper.fertT1)){
			return 1;
		}
		else if(f.isFluidEqual(TreeFarmHelper.fertT2)){
			return 2;
		}
		else if(f.isFluidEqual(TreeFarmHelper.fertT3)){
			return 3;
		}
		else {
			return 0;
		}
	}

	public FluidStack getCurrentFertiliserStack(){
		if (!doesInputHatchContainAnyFertiliser()){
			return null;
		}		
		FluidStack[] tempFluids = getStoredInputFluids();
		if (tempFluids.length >= 1){
			for (FluidStack f : tempFluids){
				if (f.isFluidEqual(TreeFarmHelper.fertT1)){
					return TreeFarmHelper.fertT1;
				}
				else if(f.isFluidEqual(TreeFarmHelper.fertT2)){
					return TreeFarmHelper.fertT2;
				}
				else if(f.isFluidEqual(TreeFarmHelper.fertT3)){
					return TreeFarmHelper.fertT3;
				}
			}
		}
		return null;
	}


public boolean doesInputHatchContainEnoughFertiliser(){
	FluidStack[] tempFluids = getStoredInputFluids();
	if (tempFluids.length >= 1){
		for (FluidStack f : tempFluids){
			if (f.amount >= TreeFarmHelper.fertT1.amount || f.amount >= TreeFarmHelper.fertT2.amount || f.amount >= TreeFarmHelper.fertT3.amount){
				return true;
			}
		}
	}
	Utils.LOG_MACHINE_INFO("No fertiliser found.");
	return false;
}

public boolean depleteFertiliser(){
	if (!doesInputHatchContainEnoughFertiliser()){
		return false;
	}		
	FluidStack[] tempFluids = getStoredInputFluids();
	if (tempFluids.length >= 1){
		for (FluidStack f : tempFluids){
			if (f.isFluidEqual(TreeFarmHelper.fertT1) || f.isFluidEqual(TreeFarmHelper.fertT2) || f.isFluidEqual(TreeFarmHelper.fertT3)){					
				if (f.isFluidEqual(TreeFarmHelper.fertT1) && f.amount >= TreeFarmHelper.fertT1.amount){
					this.depleteInput(TreeFarmHelper.fertT1);
					return true;
				}
				else if(f.isFluidEqual(TreeFarmHelper.fertT2) && f.amount >= TreeFarmHelper.fertT2.amount){
					this.depleteInput(TreeFarmHelper.fertT2);
					return true;
				}
				else if(f.isFluidEqual(TreeFarmHelper.fertT3) && f.amount >= TreeFarmHelper.fertT3.amount){
					this.depleteInput(TreeFarmHelper.fertT3);
					return true;
				}
			}
		}
	}
	return false;
}



public boolean depleteInputEx(ItemStack aStack) {
	if (GT_Utility.isStackInvalid(aStack))	return false;

	Utils.LOG_MACHINE_INFO("Taking one sapling away from in input bus.");

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

public EntityPlayerMP getFakePlayer() {
	return this.farmerAI;
}

@Override
public boolean onRunningTick(ItemStack aStack) {
	return super.onRunningTick(aStack);
}

@Override
public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
	//super.onPostTick(aBaseMetaTileEntity, aTick);
	if (aBaseMetaTileEntity.isServerSide()) {

		//Does it have a tool this cycle to cut?
		boolean validCuttingTool = false;
		boolean isRepaired = isMachineRepaired();
		//Add some Power
		addPowerToInternalStorage(aBaseMetaTileEntity);

		//Set Forestry Fake player Sapling Planter
		if (this.farmerAI == null) {
			this.farmerAI = new FakeFarmer(MinecraftServer.getServer().worldServerForDimension(this.getBaseMetaTileEntity().getWorld().provider.dimensionId));
		}			
		//Check Inventory slots [1] - Find a valid Buzzsaw Blade or a Saw
		try {
			validCuttingTool = isCorrectMachinePart(mInventory[1]);
			if (validCuttingTool){
				this.mMaxProgresstime = 600;
				String materialName = GT_MetaGenerated_Tool.getPrimaryMaterial(mInventory[1]).mDefaultLocalName;
				if (materialName.toLowerCase().contains("null")){

				}
				else {

				}
			} 
			else {
				this.mMaxProgresstime = 0;
			}
		} catch (NullPointerException t){}

		if (isRepaired){

			//Utils.LOG_INFO("Ticking3");

			//If Machine can work and it's only once every 5 seconds this will tick.
			if (canChop){
				//Set Machine State
				if (treeCheckTicks == 200){
					Utils.LOG_MACHINE_INFO("Looking For Trees - Serverside | "+treeCheckTicks);
					//Find wood to Cut
					if (validCuttingTool){
						findLogs(aBaseMetaTileEntity);						
					}
					else {
						Utils.LOG_MACHINE_INFO("Did not find a valid saw or Buzzsaw blade.");
					}
				}
			}	
			else {
				if (plantSaplingTicks == 100){
					Utils.LOG_MACHINE_INFO("Looking For space to plant saplings - Serverside | "+plantSaplingTicks);
					//Plant Some Saplings
					plantSaplings(aBaseMetaTileEntity);
				}	
				else if (plantSaplingTicks == 200){
					Utils.LOG_MACHINE_INFO("Looking For Saplings to grow - Serverside | "+plantSaplingTicks);
					//Try Grow some Saplings

					if (doesInputHatchContainAnyFertiliser()){
						growSaplingsWithBonemeal(aBaseMetaTileEntity);
					}						

					//Set can work state
					this.mInputBusses = new ArrayList<GT_MetaTileEntity_Hatch_InputBus>();
					this.mEnergyHatches = new ArrayList<GT_MetaTileEntity_Hatch_Energy>();
					canChop = checkMachine(aBaseMetaTileEntity, mInventory[1]);
				}
			}
			//Call Cleanup Task last, before ticking.
			if (cleanupTicks == 600){
				Utils.LOG_MACHINE_INFO("Looking For rubbish to cleanup - Serverside | "+cleanupTicks);
				//cleanUp(aBaseMetaTileEntity);
			}
			//Tick TE
			tickHandler();
		}
		else {
			if (treeCheckTicks == 200 || plantSaplingTicks == 100 || plantSaplingTicks == 200 || cleanupTicks == 600){
				Utils.LOG_MACHINE_INFO("Machine is not fully repaired, not ticking.");					
			}
		}

	}
	//Client Side - do nothing

}

}