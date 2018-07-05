package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.slots.SlotBuzzSaw.SAWTOOL;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_TreeFarmer;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_TreeFarmer;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper;

public class GregtechMetaTileEntityTreeFarm extends GregtechMeta_MultiBlockBase {

	public final static int TEX_INDEX = 31;
	protected boolean mIsCurrentlyWorking = false;




	public GregtechMetaTileEntityTreeFarm(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityTreeFarm(final String aName) {
		super(aName);
	}

	public boolean isCurrentlyWorking() {
		return this.mIsCurrentlyWorking;
	}

	@Override
	public String[] getDescription() {
		return new String[]{
		        "THIS MULTIBLOCK IS DISABLED - DO NOT BUILD",
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
				"Produces "+this.getPollutionPerTick(null)+" pollution per tick",
				CORE.GT_Tooltip
		};
	}

	@Override
	public long maxEUStore() {
		return 3244800; //13*13*150*128
	}

	@Override
	public boolean drainEnergyInput(final long aEU) {
		if (aEU <= 0L) {
			return true;
		}
		
		//Special Override, so that this function uses internally stored power first.
		if (this.getEUVar() >= aEU) {
			this.setEUVar(this.getEUVar()-aEU);
			return true;
		}
		
		for (final GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches) {
			if (isValidMetaTileEntity((MetaTileEntity) tHatch)
					&& tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU, false)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == 0) {
			return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log)};
		}
		if (aSide == 1) {
			return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Acacia_Log), new GT_RenderedTexture(isCurrentlyWorking() ? TexturesGtBlock.Overlay_Machine_Vent_Fast : TexturesGtBlock.Overlay_Machine_Vent)};
		}
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Farm_Manager)};
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}


	@Override
	public void loadNBTData(NBTTagCompound arg0) {
		super.loadNBTData(arg0);
	}


	@Override
	public void saveNBTData(NBTTagCompound arg0) {
		super.saveNBTData(arg0);
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

	public Block getCasingBlock() {
		return ModBlocks.blockCasings2Misc;
	}


	public byte getCasingMeta() {
		return 15;
	}


	public byte getCasingTextureIndex() {
		return (byte) TAE.GTPP_INDEX(31);
	}

	@Override
	public int getMaxEfficiency(ItemStack p0) {
		return 10000;
	}

	@Override
	public String[] getExtraInfoData() {
		String[] mSuper = new String[0];
		String[] mDesc = new String[mSuper.length+1];		
		mDesc[0] = "Yggdrasil"; // Machine name		
		for (int i=0;i<mSuper.length;i++) {
			mDesc[i+1] = mSuper[i];
		}
		return mDesc;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack p0) {
		return false;
	}
	@Override
	public boolean onRunningTick(final ItemStack aStack) {
		//Logger.INFO("s");

		return super.onRunningTick(aStack);
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		//Do Main Multi Logic first
		super.onPostTick(aBaseMetaTileEntity, aTick);

		//Do Tree Farm logic next on server side, once per second
		if (aBaseMetaTileEntity.isServerSide() && (aTick % 20 == 0)) {			

			//Simple Repairs for a simple machine
			if (isCurrentlyWorking()) {
				this.mSolderingTool = true;
			}

			if (this.getBaseMetaTileEntity().isServerSide()) {	
				if (this.mEnergyHatches.size() > 0) {
					for (GT_MetaTileEntity_Hatch_Energy j : this.mEnergyHatches) {
						//Logger.INFO(""+j.getInputTier());
						if (this.getEUVar() <= (this.maxEUStore()-GT_Values.V[(int) j.getInputTier()])) {
							this.setEUVar(this.getEUVar()+GT_Values.V[(int) j.getInputTier()]);
							j.setEUVar(j.getEUVar()-GT_Values.V[(int) j.getInputTier()]);
						}
						else if (this.getEUVar() > (this.maxEUStore()-GT_Values.V[(int) j.getInputTier()])) {
							long diff = (this.maxEUStore()-this.getEUVar());
							this.setEUVar(this.getEUVar()+diff);
							j.setEUVar(j.getEUVar()-diff);
						}
					}
				}	
			}


			//Try Work
			if (this.drainEnergyInput(32)) {
				BlockPos t;			
				if ((t = TreeFarmHelper.checkForLogsInGrowArea(this.getBaseMetaTileEntity())) != null) {
					//Logger.INFO("Lets try find new logs/branches.");
					TreeFarmHelper.findTreeFromBase(this.getBaseMetaTileEntity().getWorld(), t);
				}
			}


		}
	}


	@Override
	public boolean checkRecipe(ItemStack p0) {		
		mIsCurrentlyWorking = (isCorrectMachinePart(p0) && this.getEUVar() > 0);		
		if (isCurrentlyWorking()) {
			return true;
		}		
		return false;
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
							if (!TreeFarmHelper.isDirtBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
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
							if (!TreeFarmHelper.isFenceBlock(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
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
				if (this.mEnergyHatches.get(i).mTier < 1){
					Logger.MACHINE_INFO("You require at LEAST MV tier Energy Hatches.");
					Logger.MACHINE_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					return false;
				}
			}
		}
		//Must have at least one output hatch.
		if (this.mOutputHatches != null) {
			for (int i = 0; i < this.mOutputHatches.size(); i++) {

				if (this.mOutputHatches.get(i).mTier < 1){
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
				if (this.mInputHatches.get(i).mTier < 1){
					Logger.MACHINE_INFO("You require at LEAST MV tier Input Hatches.");
					Logger.MACHINE_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Logger.MACHINE_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		Logger.MACHINE_INFO("Multiblock Formed.");
		return true;
	}


	@Override
	public int getPollutionPerTick(ItemStack arg0) {
		return 0;
	}


	@Override
	public void onServerStart() {		
		super.onServerStart();
	}

}