package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_Adv_DistillationTower extends GregtechMeta_MultiBlockBase {

	private short mControllerY = 0;    
	private byte mMode = 0;
	private boolean mUpgraded = false;

	public GregtechMetaTileEntity_Adv_DistillationTower(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_Adv_DistillationTower(String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Adv_DistillationTower(this.mName);
	}

	public String[] getTooltip() {
		String s = "Max parallel dictated by tower tier and mode";
		String s1 = "DTower Mode: T1=4, T2=12";
		String s2 = "Distilery Mode: Tower Tier * (4*InputTier)";
		return new String[]{
				"Controller Block for the Advanced Distillation Tower",
				"T1 constructed identical to standard DT",
				"Place Distillus Upgrade Chip into Controller GUI to upgrade to T2",
				"T2 is not variable height",
				"Size(WxHxD): 3x26x3",
				"Controller (Front bottom)",
				"1x Input Hatch (Any bottom layer casing)",
				"24x Output Hatch (One per layer except bottom/top layer)",
				"1x Output Bus (Any bottom layer casing)",
				"1x Maintenance Hatch (Any casing)",
				"1x Energy Hatch (Any casing)",
				"Integral Framework I's for the rest",
				s,
				s1,
				s2};
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName()+(mUpgraded ? " T2" : ""), "MultiblockDisplay.png");
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return mMode == 0 ? GT_Recipe.GT_Recipe_Map.sDistillationRecipes : GT_Recipe.GT_Recipe_Map.sDistilleryRecipes;
	}

	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(ItemStack aStack) {
		return this.mMode == 1 ? 12 : 24;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setByte("mMode", mMode);
		aNBT.setInteger("mCasingTier", this.mCasingTier);
		aNBT.setBoolean("mUpgraded", mUpgraded);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		mMode = aNBT.getByte("mMode");
		mCasingTier = aNBT.getInteger("mCasingTier");
		mUpgraded = aNBT.getBoolean("mUpgraded");
		super.loadNBTData(aNBT);
	}	

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(203));
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mMode++;		
		if (mMode > 1){
			mMode = 0;
			PlayerUtils.messagePlayer(aPlayer, "Now running in Distillation Tower Mode.");
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Now running in Distillery Mode.");
		}		
	}

	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public boolean addOutput(FluidStack aLiquid) {
		if (aLiquid == null) return false;
		FluidStack tLiquid = aLiquid.copy();
		for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
			if (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid) ? tHatch.outputsSteam() : tHatch.outputsLiquids()) {
				if (tHatch.getBaseMetaTileEntity().getYCoord() == this.mControllerY + 1) {
					int tAmount = tHatch.fill(tLiquid, false);
					if (tAmount >= tLiquid.amount) {
						return tHatch.fill(tLiquid, true) >= tLiquid.amount;
					} else if (tAmount > 0) {
						tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
		for (int i = 0; i < mOutputFluids2.length; i++) {
			if (mOutputHatches.size() > i && mOutputHatches.get(i) != null && mOutputFluids2[i] != null && isValidMetaTileEntity(mOutputHatches.get(i))) {
				if (mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord() == this.mControllerY + 1 + i) {
					mOutputHatches.get(i).fill(mOutputFluids2[i], true);
				}
			}
		}

	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "DistillationTower";
	}

	@Override
	public String getMachineType() {
		return "Distillery, Distillation Tower";
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
			ArrayList<ItemStack> tBusItems = new ArrayList<ItemStack>();
			tBus.mRecipeMap = getRecipeMap();
			if (isValidMetaTileEntity(tBus)) {
				for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
					if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null)
						tBusItems.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
				}
			}			
			ItemStack[] inputs = new ItemStack[tBusItems.size()];
			int slot = 0;
			for (ItemStack g : tBusItems) {
				inputs[slot++] = g;
			}			
			if (inputs.length > 0) {				
				int para = (4* GT_Utility.getTier(this.getMaxInputVoltage()));
				log("Recipe. ["+inputs.length+"]["+para+"]");				
				if (checkRecipeGeneric(inputs, new FluidStack[]{}, para, 100, 250, 10000)) {
					log("Recipe 2.");
					return true;
				}
			}			

		}
		return false;
	}

	@Override
	public int getMaxParallelRecipes() {		
		if (this.mMode == 0) {
			return getTierOfTower() == 1 ? 4 : getTierOfTower() == 2 ? 12 : 0;			
		}
		else if (this.mMode == 1) {
			return getTierOfTower() * (4 * GT_Utility.getTier(this.getMaxInputVoltage()));
		}		
		return 0;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 15;
	}

	@Override
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int aTier = getTierOfTower();
		if (aTier > 0) {
			if (aTier == 1) {
				return checkTierOneTower(aBaseMetaTileEntity, aStack);
			}
			else if (aTier == 2) {
				return checkTierTwoTower(aBaseMetaTileEntity, aStack);				
			}
		}
		return false;
	}

	private int getTierOfTower() {
		return mUpgraded ? 2 : 1;
	}

	private boolean checkTierOneTower(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mControllerY = aBaseMetaTileEntity.getYCoord();
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int y = 0; //height
		int casingAmount = 0;
		boolean reachedTop = false;

		for (int x = xDir - 1; x <= xDir + 1; x++) { //x=width
			for (int z = zDir - 1; z <= zDir + 1; z++) { //z=depth
				if (x != 0 || z != 0) {
					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
					if (!addInputToMachineList(tileEntity, getCasingTextureID()) 
							&& !addOutputToMachineList(tileEntity, getCasingTextureID()) 
							&& !addMaintenanceToMachineList(tileEntity, getCasingTextureID()) 
							&& !addEnergyInputToMachineList(tileEntity, getCasingTextureID())) {
						if (block == GregTech_API.sBlockCasings4 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
							casingAmount++;
						} else {
							return false;
						}        						
					}
				}
			}
		}
		y++;

		while (y < 12 && !reachedTop) {
			for (int x = xDir - 1; x <= xDir + 1; x++) { //x=width
				for (int z = zDir - 1; z <= zDir + 1; z++) { //z=depth
					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
					if (aBaseMetaTileEntity.getAirOffset(x, y, z)) {
						if (x != xDir || z != zDir) {
							return false;
						}
					} else {
						if (x == xDir && z == zDir) {
							reachedTop = true;
						}
						if (!addOutputToMachineList(tileEntity, getCasingTextureID()) 
								&& !addMaintenanceToMachineList(tileEntity, getCasingTextureID()) 
								&& !addEnergyInputToMachineList(tileEntity, getCasingTextureID())) {
							if (block == GregTech_API.sBlockCasings4 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
								casingAmount++;
							} else {
								return false;
							}
						}
					}
				}
			}
			y++;
		}        
		return casingAmount >= 7 * y - 5 && y >= 3 && y <= 12 && reachedTop;    
	}

	private boolean checkTierTwoTower(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mControllerY = aBaseMetaTileEntity.getYCoord();
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int y = 0; //height
		int casingAmount = 0;
		boolean reachedTop = false;

		for (int x = xDir - 1; x <= xDir + 1; x++) { //x=width
			for (int z = zDir - 1; z <= zDir + 1; z++) { //z=depth
				if (x != 0 || z != 0) {
					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
					if (!addInputToMachineList(tileEntity, getCasingTextureID()) 
							&& !addOutputToMachineList(tileEntity, getCasingTextureID()) 
							&& !addMaintenanceToMachineList(tileEntity, getCasingTextureID()) 
							&& !addEnergyInputToMachineList(tileEntity, getCasingTextureID())) {
						if (block == GregTech_API.sBlockCasings4 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
							casingAmount++;
						} else {
							return false;
						}        						
					}
				}
			}
		}
		y++;

		while (y < 12 && !reachedTop) {
			for (int x = xDir - 1; x <= xDir + 1; x++) { //x=width
				for (int z = zDir - 1; z <= zDir + 1; z++) { //z=depth
					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
					if (aBaseMetaTileEntity.getAirOffset(x, y, z)) {
						if (x != xDir || z != zDir) {
							return false;
						}
					} else {
						if (x == xDir && z == zDir) {
							reachedTop = true;
						}
						if (!addOutputToMachineList(tileEntity, getCasingTextureID()) 
								&& !addMaintenanceToMachineList(tileEntity, getCasingTextureID()) 
								&& !addEnergyInputToMachineList(tileEntity, getCasingTextureID())) {
							if (block == GregTech_API.sBlockCasings4 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
								casingAmount++;
							} else {
								return false;
							}
						}
					}
				}
			}
			y++;
		}        
		return casingAmount >= 7 * y - 5 && y >= 3 && y <= 12 && reachedTop;    
	}


	private int mCasingTier = 0;

	private int getMachineCasingTier() {
		return mCasingTier;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {

		ITexture aOriginalTexture;

		// Check things exist client side (The worst code ever)
		if (aBaseMetaTileEntity.getWorld() != null) {

		}
		// Check the Tier Client Side
		int aTier = mCasingTier;		

		if (aTier == 0) {
			aOriginalTexture = Textures.BlockIcons.CASING_BLOCKS[49];
		}
		else if (aTier == 1) {
			aOriginalTexture = Textures.BlockIcons.CASING_BLOCKS[43];
		}
		else {
			aOriginalTexture = Textures.BlockIcons.CASING_BLOCKS[49];
		}

		if (aSide == aFacing) {
			return new ITexture[]{aOriginalTexture, new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER)};
		}
		return new ITexture[]{aOriginalTexture};
	}

	private int getCasingTextureID() {
		// Check the Tier Client Side
		int aTier = mCasingTier;		

		if (aTier == 1) {
			return 49;
		}
		else if (aTier == 2) {
			return 43;
		}
		else {
			return 49;
		}
	}

	public boolean addToMachineList(IGregTechTileEntity aTileEntity) {		
		int aMaxTier = getMachineCasingTier();		
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();		
		if (aMetaTileEntity instanceof GT_MetaTileEntity_TieredMachineBlock) {
			GT_MetaTileEntity_TieredMachineBlock aMachineBlock = (GT_MetaTileEntity_TieredMachineBlock) aMetaTileEntity;
			int aTileTier = aMachineBlock.mTier;
			if (aTileTier > aMaxTier) {
				Logger.INFO("Hatch tier too high.");
				return false;
			}
			else {
				return addToMachineList(aTileEntity, getCasingTextureID());
			}
		}
		else {
			Logger.INFO("Bad Tile Entity being added to hatch map."); // Shouldn't ever happen, but.. ya know..
			return false;
		}		
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		if (aTick % 20 == 0 && !mUpgraded) {
			ItemStack aGuiStack = this.getGUIItemStack();
			if (aGuiStack != null) {
				if (GT_Utility.areStacksEqual(aGuiStack, GregtechItemList.Distillus_Upgrade_Chip.get(1))) {
					this.mUpgraded = true;
					ItemUtils.depleteStack(aGuiStack);
				}
			}
		}
		// Silly Client Syncing
		if (aBaseMetaTileEntity.isClientSide()) {
			this.mCasingTier = getCasingTierOnClientSide();
		}
	}



	@SideOnly(Side.CLIENT)
	private final int getCasingTierOnClientSide() {
		if (this == null || this.getBaseMetaTileEntity().getWorld() == null) {
			return 0;
		}
		try {
			Block aInitStructureCheck;
			int aInitStructureCheckMeta;
			IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();			
			for (int i=1;1<10;i++) {
				aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, i, 0);
				aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, i, 0);				
				if (aInitStructureCheck == GregTech_API.sBlockCasings4 && aInitStructureCheckMeta == 1) {
					return 0;
				}
				else if (aInitStructureCheck == ModBlocks.blockCasingsTieredGTPP) {
					return 1;
				}				
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
			return 0;
		}

	}

	@Override
	public void setItemNBT(NBTTagCompound aNBT) {
		aNBT.setBoolean("mUpgraded", mUpgraded);
		super.setItemNBT(aNBT);
	}

}