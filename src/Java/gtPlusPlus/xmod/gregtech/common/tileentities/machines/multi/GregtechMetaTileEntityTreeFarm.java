package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;

import cpw.mods.fml.common.Optional;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import forestry.api.genetics.IIndividual;
import forestry.core.utils.GeneticsUtil;
import forestry.plugins.PluginManager;
import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.players.FakeFarmer;
import gtPlusPlus.core.slots.SlotBuzzSaw.SAWTOOL;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.particles.BlockBreakParticles;
import gtPlusPlus.xmod.forestry.trees.TreefarmManager;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_TreeFarmer;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_TreeFarmer;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntityTreeFarm extends GregtechMeta_MultiBlockBase {

	/* private */ private int treeCheckTicks = 0;
	/* private */ private int plantSaplingTicks = 0;
	/* private */ private int cleanupTicks = 0;
	/* private */ public long mInternalPower = 0;
	/* private */ private static int powerDrain = 32;
	public final static int TEX_INDEX = 31;

	private short energyHatchRetryCount = 0;

	//Too Many logs, lag breaker -- CONVERTED to local variables
	//private final boolean takingBreak = false;
	//private final int logsToBreakAfter = 500;

	private EntityPlayerMP farmerAI;

	private boolean canChop = false;

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
				"Blue/Yellow: Controller",
				"1x Input Bus (anywhere)",
				"1x Output Bus (anywhere)",
				"1x Input Hatch (anywhere)",
				"1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)",
				CORE.GT_Tooltip
		};
	}

	public long getStoredInternalPower(){
		//Utils.LOG_MACHINE_INFO("returning "+this.mInternalPower+"EU to the called method.");
		return this.mInternalPower;
	}


	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {

		Logger.WARNING("Called NBT data save");
		aNBT.setLong("mInternalPower", this.mInternalPower);

		//Save [Buzz]Saw
		final NBTTagList list = new NBTTagList();
		for(int i = 0;i<2;i++){
			final ItemStack stack = this.mInventory[i];
			if(stack != null){
				final NBTTagCompound data = new NBTTagCompound();
				stack.writeToNBT(data);
				data.setInteger("Slot", i);
				Logger.WARNING("Saving "+stack.getDisplayName()+" in slot "+i);
				list.appendTag(data);
			}
		}
		aNBT.setTag("Items", list);

		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.mInternalPower = aNBT.getLong("mInternalPower");

		//Load [Buzz]Saw
		final NBTTagList list = aNBT.getTagList("Items", 10);
		for(int i = 0;i<list.tagCount();i++){
			final NBTTagCompound data = list.getCompoundTagAt(i);
			final int slot = data.getInteger("Slot");
			if((slot >= 0) && (slot < 2)){
				this.mInventory[slot] = ItemStack.loadItemStackFromNBT(data);
				Logger.WARNING("Loading "+this.mInventory[slot].getDisplayName()+" in slot "+i);
			}
		}

		super.loadNBTData(aNBT);
	}

	@Override
	public boolean drainEnergyInput(final long aEU) {
		if (this.mInternalPower >= 32){
			this.mInternalPower = (this.mInternalPower-32);
			Logger.MACHINE_INFO("Draining internal power storage by 32EU. Stored:"+this.mInternalPower);
			return true;}
		return false;
	}

	public boolean addPowerToInternalStorage(final IGregTechTileEntity aBaseMetaTileEntity){
		if (this.mEnergyHatches.size() > 0) {
			for (final GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches){
				if (isValidMetaTileEntity(tHatch)) {
					if (tHatch.getEUVar() >= 128) {
						for (int o=0;o<(tHatch.getEUVar()/128);o++){
							if (this.mInternalPower<(this.maxEUStore()-128)){
								tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(128, false);
								this.mInternalPower = (this.mInternalPower+128);
								//Utils.LOG_MACHINE_INFO("Increasing internal power storage by 128EU. Stored:"+this.mInternalPower);
							}
						}
					}
				}
				else {
					Logger.MACHINE_INFO("Bad Power hatch to obtain energy from.");
				}
			}
		}
		else {
			if (this.energyHatchRetryCount <= 10){
				this.energyHatchRetryCount++;
				//Utils.LOG_MACHINE_INFO("No energy hatches found.");
			}
			else {
				//Utils.LOG_MACHINE_INFO("Rechecking for Energy hatches.");
				this.energyHatchRetryCount = 0;
				this.mEnergyHatches = new ArrayList<>();
				this.checkMachine(aBaseMetaTileEntity, this.mInventory[1]);
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
			return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log), new GT_RenderedTexture(this.canChop ? TexturesGtBlock.Overlay_Machine_Vent_Fast : TexturesGtBlock.Overlay_Machine_Vent)};
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
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_TreeFarmer(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "TreeFarmer.png");
	}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_TreeFarmer(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide() || aBaseMetaTileEntity.getWorld().isRemote) {
			Logger.WARNING("Doing nothing Client Side.");
			return false;
		}
		aBaseMetaTileEntity.openGUI(aPlayer);
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		boolean isValid = false;
		final SAWTOOL currentInputItem = TreeFarmHelper.isCorrectMachinePart(aStack);
		if (currentInputItem != SAWTOOL.NONE){			
			isValid = true;
		}
		return isValid;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		Logger.MACHINE_INFO("Working");
		this.mEfficiency = 0;
		this.mEfficiencyIncrease = 0;
		this.mMaxProgresstime = 0;
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		Logger.WARNING("Step 1");
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7;
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;

		for (int i = -7; i <= 7; i++) {
			Logger.WARNING("Step 2");
			for (int j = -7; j <= 7; j++) {
				Logger.WARNING("Step 3");
				for (int h = 0; h <= 1; h++) {
					Logger.WARNING("Step 4");
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
					//Farm Floor inner 14x14
					if (((i != -7) && (i != 7)) && ((j != -7) && (j != 7))) {
						Logger.WARNING("Step 5 - H:"+h);
						// Farm Dirt Floor and Inner Air/Log space.
						if (h == 0) {
							//Dirt Floor
							if (!TreefarmManager.isDirtBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
								Logger.MACHINE_INFO("Dirt like block missing from inner 14x14.");
								Logger.MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}
						}
					}
					//Dealt with inner 5x5, now deal with the exterior.
					else {
						Logger.WARNING("Step 6 - H:"+h);
						//Deal with all 4 sides (Fenced area)
						if (h == 1) {
							if (!TreefarmManager.isFenceBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
								Logger.MACHINE_INFO("Fence/Gate missing from outside the second layer.");
								Logger.MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}
						}
						//Deal with Bottom edges (Add Hatches/Busses first, othercheck make sure it's dirt) //TODO change the casings to not dirt~?
						else if (h == 0) {
							if (tTileEntity != null)
								if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(TEX_INDEX))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(TEX_INDEX))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(TEX_INDEX))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(TEX_INDEX)))) {
									if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller

										if (tTileEntity.getMetaTileID() != 752) {
											Logger.MACHINE_INFO("Farm Keeper Casings Missing from one of the edges on the bottom edge. x:"+(xDir+i)+" y:"+h+" z:"+(zDir+j)+" | "+aBaseMetaTileEntity.getClass());
											Logger.MACHINE_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName()+" "+tTileEntity.getMetaTileID());
											return false;
										}
										Logger.WARNING("Found a farm keeper.");
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
					Logger.MACHINE_INFO("You require at LEAST MV tier Energy Hatches.");
					Logger.MACHINE_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					return false;
				}
			}
		}
		//Must have at least one output hatch.
		if (this.mOutputHatches != null) {
			for (int i = 0; i < this.mOutputHatches.size(); i++) {

				if ((this.mOutputHatches.get(i).mTier < 2) && (this.mOutputHatches.get(i).getBaseMetaTileEntity() instanceof GregtechMTE_NuclearReactor)){
					Logger.MACHINE_INFO("You require at LEAST MV tier Output Hatches.");
					Logger.MACHINE_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Logger.MACHINE_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		//Must have at least one input hatch.
		if (this.mInputHatches != null) {
			for (int i = 0; i < this.mInputHatches.size(); i++) {
				if (this.mInputHatches.get(i).mTier < 2){
					Logger.MACHINE_INFO("You require at LEAST MV tier Input Hatches.");
					Logger.MACHINE_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Logger.MACHINE_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		this.mSolderingTool = true;
		Logger.MACHINE_INFO("Multiblock Formed.");
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
		return 0;//moved to cut log instead
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
		if (this.mMaintenanceHatches.size() >= 1){
			GT_MetaTileEntity_Hatch_Maintenance x = this.mMaintenanceHatches.get(0);
			//Utils.LOG_MACHINE_INFO("Checking status of maint. hatches.");
			if (x.mCrowbar && x.mHardHammer && x.mScrewdriver && x.mSoftHammer && x.mSolderingTool && x.mWrench){
				//Utils.LOG_MACHINE_INFO("Maint. hatch 0 was okay.");
				return true;
			}
			else {
				if (x.getBaseMetaTileEntity().isActive() == false){
					return true;
				}
				/*Utils.LOG_MACHINE_INFO("Maint. Hatches requires Hard Hammer? "+x.mHardHammer);
				Utils.LOG_MACHINE_INFO("Maint. Hatches requires Soft Hammer? "+x.mSoftHammer);
				Utils.LOG_MACHINE_INFO("Maint. Hatches requires Crowbar? "+x.mCrowbar);
				Utils.LOG_MACHINE_INFO("Maint. Hatches requires Screwdriver? "+x.mScrewdriver);
				Utils.LOG_MACHINE_INFO("Maint. Hatches requires Soldering Iron? "+x.mSolderingTool);
				Utils.LOG_MACHINE_INFO("Maint. Hatches requires Wrench? "+x.mWrench);*/
			}
		}
		else {
			//Utils.LOG_MACHINE_INFO("Found no maint. hatches.");
		}
		return false;
	}

	private boolean findLogs(final IGregTechTileEntity aBaseMetaTileEntity){

		Logger.MACHINE_INFO("called findLogs()");
		int logsCut = 0;
		boolean stopCheck = false;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7;
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		final World world = aBaseMetaTileEntity.getWorld();
		int posX, posY, posZ;

		int logsToBreakAfter = 500;
		if (this.mInternalPower >= 128) {

			OUTER :	for (int h=0;h<150;h++){
				for (int i = -7; i <= 7; i++) {
					for (int j = -7; j <= 7; j++) {


						//can be simplified...
						//Cut too many logs, do this to prevent lag.
						boolean takingBreak = false;
						if (logsCut >= logsToBreakAfter){
							stopCheck = true;
						}
						//Already Breaking but first two layers are empty, let's do a full check.
						else if ((logsCut == 0) && (h == 2) && takingBreak){
							stopCheck = false;
						}
						//No Trees Grown and not breaking, take a break, reduce lag.
						else if ((logsCut == 0) && (h == 3) && !takingBreak){
							stopCheck = true;
						}
						else {
							stopCheck = false;
						}

						if (stopCheck){
							Logger.MACHINE_INFO("Either found too many logs, so taking a break mid cut for lag conservation, or found no trees to cut, so stopping");
							Logger.MACHINE_INFO("found: "+logsCut +" and check up to h:"+h+" - Taking Break:"+ takingBreak);
							stopCheck = false;
							break OUTER;
						}



						if (((i != -7) && (i != 7)) && ((j != -7) && (j != 7))) {
							final Block loopBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							if (TreefarmManager.isWoodLog(loopBlock) || TreefarmManager.isLeaves(loopBlock)){
								final long tempStoredEU = this.mInternalPower;
								if (tempStoredEU >= powerDrain){
									Logger.MACHINE_INFO("Cutting a "+loopBlock.getLocalizedName()+", currently stored:"+tempStoredEU+" | max:"+this.maxEUStore());
									this.drainEnergyInput(powerDrain);

									final long tempStoredEU2 = this.mInternalPower;
									if (tempStoredEU != tempStoredEU2){
										if (tempStoredEU == (tempStoredEU2+powerDrain)){
											Logger.MACHINE_INFO(powerDrain+"EU was drained.");
										}
										else {
											Logger.MACHINE_INFO(""+(tempStoredEU-tempStoredEU2)+"EU was drained.");
										}
									}
									else {
										Logger.MACHINE_INFO("Stored EU did not change.");
									}

									posX = aBaseMetaTileEntity.getXCoord()+xDir+i;
									posY = aBaseMetaTileEntity.getYCoord()+h;
									posZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
									this.cutLog(world, posX, posY, posZ);
									if (TreefarmManager.isWoodLog(loopBlock)){
										logsCut++;
									}
								}
								else {
									Logger.MACHINE_INFO("Not enough power to cut.");
								}
							}
						}
					}
				}
			}
		}
		else {
			Logger.MACHINE_INFO("Not enough Power | can hold:"+this.maxEUStore()+" | holding:"+aBaseMetaTileEntity.getStoredEU());
		}

		this.canChop = false;
		if (logsCut >= logsToBreakAfter){
			TreeFarmHelper.cleanUp(aBaseMetaTileEntity);
		}
		return false;
	}


	private boolean growSaplingsWithBonemeal(final IGregTechTileEntity aBaseMetaTileEntity){
		Logger.MACHINE_INFO("called growSaplingsWithBonemeal()");
		int saplings = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7;
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		for (int i = -7; i <= 7; i++) {
			for (int j = -7; j <= 7; j++) {
				final int h = 1;
				//Utils.LOG_MACHINE_INFO("Looking for saplings.");
				if (this.mInternalPower >= 32) {
					if (TreefarmManager.isSapling(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){
						int posiX, posiY, posiZ;
						posiX = aBaseMetaTileEntity.getXCoord()+xDir+i;
						posiY = aBaseMetaTileEntity.getYCoord()+h;
						posiZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
						//Utils.LOG_MACHINE_INFO("Found a sapling to grow.");
						saplings++;

						if (this.depleteFertiliser()){
							if (this.drainEnergyInput(powerDrain)){
								final short fertTier = this.getFertiliserTier(this.getCurrentFertiliserStack());
								TreeFarmHelper.applyBonemeal(this.getFakePlayer(), aBaseMetaTileEntity.getWorld(), posiX, posiY, posiZ, fertTier);
							}
							else {
								Logger.MACHINE_INFO("x3");
								break;
							}
						}
						else {
							Logger.MACHINE_INFO("x2");
							break;
						}
					}
				}
			}
		}
		Logger.MACHINE_INFO("Tried to grow saplings: | "+saplings );
		return true;
	}

	@SuppressWarnings("deprecation")
	private boolean plantSaplings(final IGregTechTileEntity aBaseMetaTileEntity){
		Logger.MACHINE_INFO("called plantSaplings()");
		final World world = aBaseMetaTileEntity.getWorld();
		ArrayList<ItemStack> r = this.getStoredInputs();
		final int saplings = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7;
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		int counter = 0;
		if (r.size() > 0){
			Logger.MACHINE_INFO("| r was not null. "+r.size()+" |");
			if (this.getStoredInternalPower() >= 32) {
				OUTER : for (final ItemStack n : r){
					Logger.MACHINE_INFO("found "+n.getDisplayName());
					if (OrePrefixes.sapling.contains(n) || n.getDisplayName().toLowerCase().contains("sapling")){
						Logger.MACHINE_INFO(""+n.getDisplayName());

						counter = n.stackSize;
						final Block saplingToPlace = Block.getBlockFromItem(n.getItem());


						//Find Gaps for Saplings after scanning Item Busses
						for (int i = -7; i <= 7; i++) {
							INNER : for (int j = -7; j <= 7; j++) {
								final int h = 1;
								if (counter > 0){
									if (((i != -7) && (i != 7)) && ((j != -7) && (j != 7))) {
										if (TreefarmManager.isAirBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))){

											//Get block location to place sapling block
											int posX, posY, posZ;
											posX = aBaseMetaTileEntity.getXCoord()+xDir+i;
											posY = aBaseMetaTileEntity.getYCoord()+h;
											posZ = aBaseMetaTileEntity.getZCoord()+zDir+j;

											//If sapling block is not null || Ignore if forestry is loaded.
											if (saplingToPlace != null){

												//Plant Sapling
												if (!LoadedMods.Forestry){
													world.setBlock(posX, posY, posZ, saplingToPlace);
													world.setBlockMetadataWithNotify(posX, posY, posZ, n.getItemDamage(), 4);
												}
												else {
													this.plantSaplingAt(n, this.getBaseMetaTileEntity().getWorld(), posX, posY, posZ);
												}

												//Deplete Input stack
												this.depleteInputEx(n);
												this.drainEnergyInput(powerDrain);
												counter--;
												//Update slot contents?
												this.updateSlots();

												//Test If Inputs Changed
												final ArrayList<ItemStack> temp = this.getStoredInputs();
												if (r != temp){
													Logger.MACHINE_INFO("Inputs changed, updating.");
													for (final ItemStack xr : r){
														Logger.MACHINE_INFO("xr:"+xr.getDisplayName()+"x"+xr.stackSize);
													}
													for (final ItemStack xc : temp){
														Logger.MACHINE_INFO("xc:"+xc.getDisplayName()+"x"+xc.stackSize);
													}
													r = temp;
												}
											}
											else {
												Logger.MACHINE_INFO(n.getDisplayName()+" did not have a valid block.");
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
						Logger.MACHINE_INFO("item was not a sapling");
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
		}
		Logger.MACHINE_INFO("Tried to plant saplings: | "+saplings );
		return true;
	}

	@Optional.Method(modid = "Forestry")
	public boolean plantSaplingAt(final ItemStack germling, final World world, final int x, final int y, final int z) {
		Logger.MACHINE_INFO("Planting Sapling with Forestry method, since it's installed.");
		if (PluginManager.Module.ARBORICULTURE.isEnabled()) {
			final IIndividual tree = GeneticsUtil.getGeneticEquivalent(germling);
			if (!(tree instanceof ITree)) {
				return false;
			}
			return TreeManager.treeRoot.plantSapling(world, (ITree) tree, this.getFakePlayer().getGameProfile(), x, y, z);
		}
		return germling.copy().tryPlaceItemIntoWorld(this.getFakePlayer(), world, x, y - 1, z, 1, 0.0F, 0.0F, 0.0F);
	}

	private boolean cutLog(final World world, final int x, final int y, final int z){
		//Utils.LOG_MACHINE_INFO("Cutting Log");
		try {
			//Actually damage item...
			if (!isToolCreative(this.mInventory[1])){
				if(!GT_ModHandler.damageOrDechargeItem(this.mInventory[1], 1, 10, this.farmerAI)){
					if (mInventory[1].stackSize == 0) mInventory[1] = null;
					return false;
				}
				if (mInventory[1].stackSize == 0) mInventory[1] = null;
			}
			final Block block = world.getBlock(x, y, z);
			int chanceForLeaves = 1000;
			//is it leaves or a log? if leaves, heavily reduce chance to obtain rubber/output
			if (block.getUnlocalizedName().toLowerCase().contains("leaves") || block.getUnlocalizedName().toLowerCase().contains("leaf") || TreefarmManager.isLeaves(block)){
				chanceForLeaves = MathUtils.randInt(1, 10);
				if (chanceForLeaves > 8) {
					Logger.MACHINE_INFO("Found some leaves that will drop, chance to drop item "+chanceForLeaves+", needed 800-1000.");
				}
			}

			//IC2 Sticky Rubber handling
			if (block.getUnlocalizedName().toLowerCase().contains("blockrubwood") || block.getUnlocalizedName().toLowerCase().contains("blockrubleaves")){
				final ItemStack rubberResin = ItemUtils.getCorrectStacktype("IC2:itemHarz", 1);
				final int chanceForRubber = MathUtils.randInt(1, 10);
				final int multiplier = MathUtils.randInt(1, 3);
				if ((chanceForRubber > 7) && (chanceForLeaves > 8)){
					rubberResin.stackSize = multiplier;
					Logger.MACHINE_INFO("Adding "+rubberResin.getDisplayName()+" x"+rubberResin.stackSize);
					this.addOutput(rubberResin);
					this.updateSlots();
				}
			}

			final int dropMeta = world.getBlockMetadata(x, y, z);
			final ArrayList<ItemStack> blockDrops = block.getDrops(world, x, y, z, dropMeta, 0);
			final ItemStack[] drops = ItemUtils.getBlockDrops(blockDrops);

			//Add Drops
			if (drops != null){
				for (final ItemStack outputs : drops){
					if (chanceForLeaves > 8){
						Logger.MACHINE_INFO("Adding 1x "+outputs.getDisplayName());
						this.addOutput(outputs);
						//Update bus contents.
						this.updateSlots();
					}
				}

			}

			//Remove drop that was added to the bus.
			world.setBlockToAir(x, y, z);
			new BlockBreakParticles(world, x, y, z, block);
			//damageOrDechargeItem(this.mInventory[1], 1, 10, this.farmerAI); done above
			return true;
		} catch (final NullPointerException e){}
		return false;
	}

	public FluidStack[] getStoredInputFluids(){
		final ArrayList<FluidStack> fluidArray = this.getStoredFluids();
		if (fluidArray.size() < 1){
			return new FluidStack[] {};
		}
		final FluidStack storedInputFluids[] = new FluidStack[fluidArray.size()];
		if (storedInputFluids.length >= 1){
			int counter = 0;
			for (final FluidStack inputPuddle : fluidArray){
				storedInputFluids[counter] = inputPuddle;
				counter++;
			}
			return storedInputFluids;
		}
		return new FluidStack[] {};
	}

	public boolean doesInputHatchContainAnyFertiliser(){
		final FluidStack[] tempFluids = this.getStoredInputFluids();
		if (tempFluids.length >= 1){
			for (final FluidStack f : tempFluids){
				if (f.isFluidEqual(TreeFarmHelper.fertT1) || f.isFluidEqual(TreeFarmHelper.fertT2) || f.isFluidEqual(TreeFarmHelper.fertT3)){
					return true;
				}
			}
		}
		else {
			Logger.MACHINE_INFO("No fertiliser found.");
			return false;
		}
		return false;
	}

	public short getFertiliserTier(final FluidStack f){
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
		if (!this.doesInputHatchContainAnyFertiliser()){
			return null;
		}
		final FluidStack[] tempFluids = this.getStoredInputFluids();
		if (tempFluids.length >= 1){
			for (final FluidStack f : tempFluids){
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
		final FluidStack[] tempFluids = this.getStoredInputFluids();
		if (tempFluids.length >= 1){
			for (final FluidStack f : tempFluids){
				if ((f.amount >= TreeFarmHelper.fertT1.amount) || (f.amount >= TreeFarmHelper.fertT2.amount) || (f.amount >= TreeFarmHelper.fertT3.amount)){
					return true;
				}
			}
		}
		Logger.MACHINE_INFO("No fertiliser found.");
		return false;
	}

	public boolean depleteFertiliser(){
		if (!this.doesInputHatchContainEnoughFertiliser()){
			return false;
		}
		final FluidStack[] tempFluids = this.getStoredInputFluids();
		if (tempFluids.length >= 1){
			for (final FluidStack f : tempFluids){
				if (f.isFluidEqual(TreeFarmHelper.fertT1) || f.isFluidEqual(TreeFarmHelper.fertT2) || f.isFluidEqual(TreeFarmHelper.fertT3)){
					if (f.isFluidEqual(TreeFarmHelper.fertT1) && (f.amount >= TreeFarmHelper.fertT1.amount)){
						this.depleteInput(TreeFarmHelper.fertT1);
						return true;
					}
					else if(f.isFluidEqual(TreeFarmHelper.fertT2) && (f.amount >= TreeFarmHelper.fertT2.amount)){
						this.depleteInput(TreeFarmHelper.fertT2);
						return true;
					}
					else if(f.isFluidEqual(TreeFarmHelper.fertT3) && (f.amount >= TreeFarmHelper.fertT3.amount)){
						this.depleteInput(TreeFarmHelper.fertT3);
						return true;
					}
				}
			}
		}
		return false;
	}



	public boolean depleteInputEx(final ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return false;
		}

		Logger.MACHINE_INFO("Taking one sapling away from in input bus.");

		for (final GT_MetaTileEntity_Hatch_InputBus tHatch : this.mInputBusses) {
			tHatch.mRecipeMap = this.getRecipeMap();
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
		if (this.treeCheckTicks > 200){
			this.treeCheckTicks = 0;
		}
		else {
			this.treeCheckTicks++;
		}
	}

	private void tickSaplings(){
		if (this.plantSaplingTicks > 200){
			this.plantSaplingTicks = 0;
		}
		else {
			this.plantSaplingTicks++;
		}
	}

	private void tickCleanup(){
		if (this.cleanupTicks > 600){
			this.cleanupTicks = 0;
		}
		else {
			this.cleanupTicks++;
		}
	}

	private void tickHandler(){
		//Count Sapling Timer
		this.tickSaplings();
		//Count Tree Cutting Timer
		this.tickTrees();
		//Tick Cleanup script Timer.
		this.tickCleanup();
	}

	public EntityPlayerMP getFakePlayer() {
		return this.farmerAI;
	}

	@Override
	public boolean onRunningTick(final ItemStack aStack) {
		return super.onRunningTick(aStack);
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		//super.onPostTick(aBaseMetaTileEntity, aTick);
		if (aBaseMetaTileEntity.isServerSide()) {

			//Does it have a tool this cycle to cut?
			boolean validCuttingTool = false;
			final boolean isRepaired = isMachineRepaired();
			//Add some Power
			this.addPowerToInternalStorage(aBaseMetaTileEntity);

			//Set Forestry Fake player Sapling Planter
			if (this.farmerAI == null) {
				this.farmerAI = new FakeFarmer(MinecraftServer.getServer().worldServerForDimension(this.getBaseMetaTileEntity().getWorld().provider.dimensionId));
			}
			//Check Inventory slots [1] - Find a valid Buzzsaw Blade or a Saw
			try {
				validCuttingTool = this.isCorrectMachinePart(this.mInventory[1]);
				if (validCuttingTool){
					this.mMaxProgresstime = 600;
					final String materialName = GT_MetaGenerated_Tool.getPrimaryMaterial(this.mInventory[1]).mDefaultLocalName;
					if (materialName.toLowerCase().contains("null")){

					}
					else {

					}
				}
				else {
					this.mMaxProgresstime = 0;
				}
			} catch (final NullPointerException t){}

			if (isRepaired){

				//Utils.LOG_MACHINE_INFO("Ticking3");

				//If Machine can work and it's only once every 5 seconds this will tick.
				if (this.canChop){
					//Set Machine State
					if (this.treeCheckTicks == 200){
						Logger.MACHINE_INFO("Looking For Trees - Serverside | "+this.treeCheckTicks);
						//Find wood to Cut
						if (validCuttingTool){
							this.findLogs(aBaseMetaTileEntity);
						}
						else {
							Logger.MACHINE_INFO("Did not find a valid saw or Buzzsaw blade.");
						}
					}
				}
				else {
					if (this.plantSaplingTicks == 100){
						Logger.MACHINE_INFO("Looking For space to plant saplings - Serverside | "+this.plantSaplingTicks);
						//Plant Some Saplings

						if (aBaseMetaTileEntity != null){
							this.plantSaplings(aBaseMetaTileEntity);
						}
					}
					else if (this.plantSaplingTicks == 200){
						Logger.MACHINE_INFO("Looking For Saplings to grow - Serverside | "+this.plantSaplingTicks);
						//Try Grow some Saplings

						if (this.doesInputHatchContainAnyFertiliser()){
							this.growSaplingsWithBonemeal(aBaseMetaTileEntity);
						}

						//Set can work state
						this.mInputBusses = new ArrayList<>();
						this.mEnergyHatches = new ArrayList<>();
						if (aBaseMetaTileEntity != null && this.mInventory[1] != null){
							this.canChop = this.checkMachine(aBaseMetaTileEntity, this.mInventory[1]);
						}
					}
				}				
			}
			else {
				if ((this.treeCheckTicks == 200) || (this.plantSaplingTicks == 100) || (this.plantSaplingTicks == 200)){
					Logger.MACHINE_INFO("Machine is not fully repaired, not ticking. Repair status code:"+this.getRepairStatus());
				}
			}

			//Call Cleanup Task last, before ticking.
			if (this.cleanupTicks == 600){
				if (aBaseMetaTileEntity != null && this.mInventory[1] != null){
					this.checkMachine(aBaseMetaTileEntity, this.mInventory[1]);
				}
				/*Utils.LOG_MACHINE_INFO("Looking For rubbish to cleanup - Serverside | "+this.cleanupTicks);
				TreeFarmHelper.cleanUp(aBaseMetaTileEntity);*/
			}
			//Tick TE
			this.tickHandler();
			this.markDirty();

		}
		//Client Side - do nothing

	}

	//@Deprecated
	//public static boolean damageOrDechargeItem(ItemStack aStack, int aDamage, int aDecharge, EntityLivingBase aPlayer) {
	//	if ((GT_Utility.isStackInvalid(aStack)) || ((aStack.getMaxStackSize() <= 1) && (aStack.stackSize > 1)))
	//		return false;
	//	if ((aPlayer != null) && (aPlayer instanceof EntityPlayer)
	//			&& (((EntityPlayer) aPlayer).capabilities.isCreativeMode))
	//		return true;
	//	if (aStack.getItem() instanceof IDamagableItem)
	//		return ((IDamagableItem) aStack.getItem()).doDamageToItem(aStack, aDamage);
	//	if (GT_ModHandler.isElectricItem(aStack)) {
	//		if (GT_ModHandler.canUseElectricItem(aStack, aDecharge)) {
	//			if ((aPlayer != null) && (aPlayer instanceof EntityPlayer)) {
	//				return GT_ModHandler.useElectricItem(aStack, aDecharge, (EntityPlayer) aPlayer);
	//			}
	//			return (GT_ModHandler.dischargeElectricItem(aStack, aDecharge, 2147483647, true, false, true) >= aDecharge);
	//		}
	//	}
	//	else if (aStack.getItem().isDamageable()) {
	//		if (aPlayer == null)
	//			aStack.setItemDamage(aStack.getItemDamage() + aDamage);
	//		else {
	//			aStack.damageItem(aDamage, aPlayer);
	//		}
	//		if (aStack.getItemDamage() >= aStack.getMaxDamage()) {
	//			aStack.setItemDamage(aStack.getMaxDamage() + 1);
	//			ItemStack tStack = GT_Utility.getContainerItem(aStack, true);
	//			if (tStack != null) {
	//				aStack.func_150996_a(tStack.getItem());
	//				aStack.setItemDamage(tStack.getItemDamage());
	//				aStack.stackSize = tStack.stackSize;
	//				aStack.setTagCompound(tStack.getTagCompound());
	//			}
	//		}
	//		return true;
	//	}
	//	return false;
	//}

}