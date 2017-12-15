package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Arrays;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_MultiTank
extends GregtechMeta_MultiBlockBase {
	public GregtechMetaTileEntity_MultiTank(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	private short multiblockCasingCount = 0;
	private int mInternalSaveClock = 0;
	private final short storageMultiplier = 1;
	private int maximumFluidStorage = 128000;
	private FluidStack internalStorageTank = null;
	private final NBTTagCompound internalCraftingComponentsTag = new NBTTagCompound();

	@Override
	public String[] getInfoData() {
		final ArrayList<GT_MetaTileEntity_Hatch_Energy> mTier = this.mEnergyHatches;
		if (!mTier.isEmpty()){
			final int temp = mTier.get(0).mTier;
			if (this.internalStorageTank == null) {
				return new String[]{
						GT_Values.VOLTAGE_NAMES[temp]+" Large Fluid Tank",
						"Stored Fluid: No Fluid",
						"Internal | Current: "+Integer.toString(0) + "L",
						"Internal | Maximum: "+Integer.toString(this.maximumFluidStorage) + "L"};
			}
			return new String[]{
					GT_Values.VOLTAGE_NAMES[temp]+" Large Fluid Tank",
					"Stored Fluid: "+this.internalStorageTank.getLocalizedName(),
					"Internal | Current: "+Integer.toString(this.internalStorageTank.amount) + "L",
					"Internal | Maximum: "+Integer.toString(this.maximumFluidStorage) + "L"};
		}
		return new String[]{
				"Voltage Tier not set -" +" Large Fluid Tank",
				"Stored Fluid: No Fluid",
				"Internal | Current: "+Integer.toString(0) + "L",
				"Internal | Maximum: "+Integer.toString(this.maximumFluidStorage) + "L"};
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		/*final NBTTagCompound gtCraftingComponentsTag = aNBT.getCompoundTag("GT.CraftingComponents");
		if (gtCraftingComponentsTag != null){

			Utils.LOG_WARNING("Got Crafting Tag");

			if (this.internalStorageTank != null){
				Utils.LOG_WARNING("mFluid was not null, Saving TileEntity NBT data.");

				gtCraftingComponentsTag.setString("xFluid", this.internalStorageTank.getFluid().getName());
				gtCraftingComponentsTag.setInteger("xAmount", this.internalStorageTank.amount);
				gtCraftingComponentsTag.setLong("xAmountMax", this.maximumFluidStorage);

				aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);
			}
			else {
				Utils.LOG_WARNING("mFluid was null, Saving TileEntity NBT data.");
				gtCraftingComponentsTag.removeTag("xFluid");
				gtCraftingComponentsTag.removeTag("xAmount");
				gtCraftingComponentsTag.removeTag("xAmountMax");
				gtCraftingComponentsTag.setLong("xAmountMax", this.maximumFluidStorage);


				aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);
			}
		}*/
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		/*final NBTTagCompound gtCraftingComponentsTag = aNBT.getCompoundTag("GT.CraftingComponents");
		String xFluid = null;
		int xAmount = 0;
		if (gtCraftingComponentsTag.hasNoTags()){
			if (this.internalStorageTank != null){
				Utils.LOG_WARNING("mFluid was not null, Creating TileEntity NBT data.");
				gtCraftingComponentsTag.setInteger("xAmount", this.internalStorageTank.amount);
				gtCraftingComponentsTag.setString("xFluid", this.internalStorageTank.getFluid().getName());
				aNBT.setTag("GT.CraftingComponents", gtCraftingComponentsTag);
			}
		}
		else {

			//internalCraftingComponentsTag = gtCraftingComponentsTag.getCompoundTag("backupTag");

			if (gtCraftingComponentsTag.hasKey("xFluid")){
				Utils.LOG_WARNING("xFluid was not null, Loading TileEntity NBT data.");
				xFluid = gtCraftingComponentsTag.getString("xFluid");
			}
			if (gtCraftingComponentsTag.hasKey("xAmount")){
				Utils.LOG_WARNING("xAmount was not null, Loading TileEntity NBT data.");
				xAmount = gtCraftingComponentsTag.getInteger("xAmount");
			}
			if ((xFluid != null) && (xAmount != 0)){
				Utils.LOG_WARNING("Setting Internal Tank, loading "+xAmount+"L of "+xFluid);
				this.setInternalTank(xFluid, xAmount);
			}
		}*/
	}

	private boolean setInternalTank(final String fluidName, final int amount){
		final FluidStack temp = FluidUtils.getFluidStack(fluidName, amount);
		if (temp != null){
			if (this.internalStorageTank == null){
				this.internalStorageTank = temp;
				Utils.LOG_WARNING(temp.getFluid().getName()+" Amount: "+temp.amount+"L");
			}
			else{
				Utils.LOG_WARNING("Retained Fluid.");
				Utils.LOG_WARNING(this.internalStorageTank.getFluid().getName()+" Amxount: "+this.internalStorageTank.amount+"L");
			}
			this.markDirty();
			return true;
		}
		return false;
	}

	@Override
	public void onLeftclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		this.tryForceNBTUpdate();
		super.onLeftclick(aBaseMetaTileEntity, aPlayer);
	}

	@Override
	public boolean onWrenchRightClick(final byte aSide, final byte aWrenchingSide, final EntityPlayer aPlayer, final float aX, final float aY, final float aZ) {
		this.tryForceNBTUpdate();
		return super.onWrenchRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public void onRemoval() {
		this.tryForceNBTUpdate();
		super.onRemoval();
	}


	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);

		if ((this.internalStorageTank != null) && this.internalStorageTank.amount >= this.maximumFluidStorage){
			if (this.internalStorageTank.amount > this.maximumFluidStorage){
				this.internalStorageTank.amount = this.maximumFluidStorage;
			}
			this.stopMachine();
		}

		if (this.mInternalSaveClock != 20){
			this.mInternalSaveClock++;
		}
		else {
			this.mInternalSaveClock = 0;
			this.tryForceNBTUpdate();
		}

	}

	public GregtechMetaTileEntity_MultiTank(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_MultiTank(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Multitank",
				"Size: 3xHx3 (Block behind controller must be air)",
				"Structure must be at least 4 blocks tall, maximum 20.",
				"Each casing within the structure adds 128000L storage.",
				"Controller (front centered)",
				"1x Input hatch (anywhere)",
				"1x Output hatch (anywhere)",
				"1x Energy Hatch (anywhere)",
				"1x Maintenance Hatch (anywhere)",
				"Multitank Exterior Casings for the rest (16 at least!)",
				CORE.GT_Tooltip
		};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(11)], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Screen_Logo : TexturesGtBlock.Overlay_Machine_Screen_Logo)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(11)]};
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "VacuumFreezer.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {

		final ArrayList<ItemStack> tInputList = this.getStoredInputs();
		for (int i = 0; i < (tInputList.size() - 1); i++) {
			for (int j = i + 1; j < tInputList.size(); j++) {
				if (GT_Utility.areStacksEqual(tInputList.get(i), tInputList.get(j))) {
					if (tInputList.get(i).stackSize >= tInputList.get(j).stackSize) {
						tInputList.remove(j--);
					} else {
						tInputList.remove(i--);
						break;
					}
				}
			}
		}
		final ItemStack[] tInputs = Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

		final ArrayList<FluidStack> tFluidList = this.getStoredFluids();
		for (int i = 0; i < (tFluidList.size() - 1); i++) {
			for (int j = i + 1; j < tFluidList.size(); j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
					} else {
						tFluidList.remove(i--);
						break;
					}
				}
			}
		}
		final FluidStack[] tFluids = Arrays.copyOfRange(tFluidList.toArray(new FluidStack[1]), 0, 1);

		if (tFluids.length >= 2){
			Utils.LOG_WARNING("Bad");
			return false;
		}

		final ArrayList<Pair<GT_MetaTileEntity_Hatch_Input, Boolean>> rList = new ArrayList<>();
		int slotInputCount = 0;
		for (final GT_MetaTileEntity_Hatch_Input tHatch : this.mInputHatches) {
			boolean containsFluid = false;
			if (isValidMetaTileEntity(tHatch)) {
				slotInputCount++;
				for (int i=0; i<tHatch.getBaseMetaTileEntity().getSizeInventory(); i++) {
					if (tHatch.canTankBeEmptied()){containsFluid=true;}
				}
				rList.add(new Pair<>(tHatch, containsFluid));
			}
		}
		if ((tFluids.length <= 0) || (slotInputCount > 1)){
			Utils.LOG_WARNING("Bad");
			return false;
		}

		Utils.LOG_WARNING("Okay - 2");
		if (this.internalStorageTank == null){
			Utils.LOG_WARNING("Okay - 3");
			if ((rList.get(0).getKey().mFluid != null) && (rList.get(0).getKey().mFluid.amount > 0)){
				Utils.LOG_WARNING("Okay - 4");
				Utils.LOG_WARNING("Okay - 1"+" rList.get(0).getKey().mFluid.amount: "+rList.get(0).getKey().mFluid.amount /*+" internalStorageTank:"+internalStorageTank.amount*/);
				final FluidStack tempFluidStack = rList.get(0).getKey().mFluid;
				final Fluid tempFluid = tempFluidStack.getFluid();
				this.internalStorageTank = FluidUtils.getFluidStack(tempFluid.getName(), tempFluidStack.amount);
				rList.get(0).getKey().mFluid.amount = 0;
				Utils.LOG_WARNING("Okay - 1.1"+" rList.get(0).getKey().mFluid.amount: "+rList.get(0).getKey().mFluid.amount +" internalStorageTank:"+this.internalStorageTank.amount);
				return true;
			}
			Utils.LOG_WARNING("No Fluid in hatch.");
			return false;
		}
		else if (this.internalStorageTank.isFluidEqual(rList.get(0).getKey().mFluid)){
			Utils.LOG_WARNING("Storing "+rList.get(0).getKey().mFluid.amount+"L");
			Utils.LOG_WARNING("Contains "+this.internalStorageTank.amount+"L");


			int tempAdd = 0;
			tempAdd = rList.get(0).getKey().getFluidAmount();
			rList.get(0).getKey().mFluid = null;
			Utils.LOG_WARNING("adding "+tempAdd);
			this.internalStorageTank.amount = this.internalStorageTank.amount + tempAdd;
			Utils.LOG_WARNING("Tank now Contains "+this.internalStorageTank.amount+"L of "+this.internalStorageTank.getFluid().getName()+".");


			//Utils.LOG_WARNING("Tank");
			return true;
		}
		else {
			final FluidStack superTempFluidStack = rList.get(0).getKey().mFluid;
			Utils.LOG_WARNING("is input fluid equal to stored fluid? "+(this.internalStorageTank.isFluidEqual(superTempFluidStack)));
			if (superTempFluidStack != null) {
				Utils.LOG_WARNING("Input hatch[0] Contains "+superTempFluidStack.amount+"L of "+superTempFluidStack.getFluid().getName()+".");
			}
			Utils.LOG_WARNING("Large Multi-Tank Contains "+this.internalStorageTank.amount+"L of "+this.internalStorageTank.getFluid().getName()+".");

			if (this.internalStorageTank.amount <= 0){
				Utils.LOG_WARNING("Internal Tank is empty, sitting idle.");
				return false;
			}

			if ((this.mOutputHatches.get(0).mFluid == null) || this.mOutputHatches.isEmpty() || (this.mOutputHatches.get(0).mFluid.isFluidEqual(this.internalStorageTank) && (this.mOutputHatches.get(0).mFluid.amount < this.mOutputHatches.get(0).getCapacity()))){
				Utils.LOG_WARNING("Okay - 3");
				final int tempCurrentStored = this.internalStorageTank.amount;
				int tempResult = 0;
				final int tempHatchSize = this.mOutputHatches.get(0).getCapacity();
				final int tempHatchCurrentHolding = this.mOutputHatches.get(0).getFluidAmount();
				final int tempHatchRemainingSpace = tempHatchSize - tempHatchCurrentHolding;
				final FluidStack tempOutputFluid = this.internalStorageTank;
				if (tempHatchRemainingSpace <= 0){
					return false;
				}
				Utils.LOG_WARNING("Okay - 3.1.x"+" hatchCapacity: "+tempHatchSize +" tempCurrentStored: "+tempCurrentStored+" output hatch holds: "+tempHatchCurrentHolding+" tank has "+tempHatchRemainingSpace+"L of space left.");

				if (tempHatchSize >= tempHatchRemainingSpace){
					Utils.LOG_WARNING("Okay - 3.1.1"+" hatchCapacity: "+tempHatchSize +" tempCurrentStored: "+tempCurrentStored+" output hatch holds: "+tempHatchCurrentHolding+" tank has "+tempHatchRemainingSpace+"L of space left.");

					int adder;
					if ((tempCurrentStored > 0) && (tempCurrentStored <= tempHatchSize)){
						adder = tempCurrentStored;
						if (adder >= tempHatchRemainingSpace){
							adder = tempHatchRemainingSpace;
						}
					}
					else {
						adder = 0;
						if (tempCurrentStored >= tempHatchRemainingSpace){
							adder = tempHatchRemainingSpace;
						}
					}

					tempResult = adder;
					tempOutputFluid.amount = tempResult;
					Utils.LOG_WARNING("Okay - 3.1.2"+" result: "+tempResult +" tempCurrentStored: "+tempCurrentStored + " filling output hatch with: "+tempOutputFluid.amount+"L of "+tempOutputFluid.getFluid().getName());
					this.mOutputHatches.get(0).fill(tempOutputFluid, true);
					//mOutputHatches.get(0).mFluid.amount = tempResult;
					this.internalStorageTank.amount = (tempCurrentStored-adder);
					Utils.LOG_WARNING("Okay - 3.1.3"+" internalTankStorage: "+this.internalStorageTank.amount +"L | output hatch contains: "+this.mOutputHatches.get(0).mFluid.amount+"L of "+this.mOutputHatches.get(0).mFluid.getFluid().getName());
					/*if (internalStorageTank.amount <= 0)
						internalStorageTank = null;*/
				}
				Utils.LOG_WARNING("Tank ok.");
				return true;
			}
		}
		//this.getBaseMetaTileEntity().(tFluids[0].amount, true);
		Utils.LOG_WARNING("Tank");
		return false;
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			Utils.LOG_WARNING("Must be hollow.");
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 19; h++) {
					if ((h != 0) || ((((xDir + i) != 0) || ((zDir + j) != 0)) && ((i != 0) || (j != 0)))) {
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(11))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(11))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(11))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(11)))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								if (h < 3){
									Utils.LOG_WARNING("Casing Expected.");
									return false;
								}
								else if (h >= 3){
									//Utils.LOG_WARNING("Your Multitank can be 20 blocks tall.");
								}
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 11) {
								if (h < 3){
									Utils.LOG_WARNING("Wrong Meta.");
									return false;
								}
								else if (h >= 3){
									//Utils.LOG_WARNING("Your Multitank can be 20 blocks tall.");
								}
							}
							if (h < 3){
								tAmount++;
							}
							else if (h >= 3){
								if ((aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) == Blocks.air) || aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName().contains("residual")){
									Utils.LOG_WARNING("Found air");
								}
								else {
									Utils.LOG_WARNING("Layer "+(h+2)+" is complete. Adding "+(64000*9)+"L storage to the tank.");
									tAmount++;
								}
							}
						}
					}
				}
			}
		}
		this.multiblockCasingCount = (short) tAmount;
		this.maximumFluidStorage = getMaximumTankStorage(tAmount);
		Utils.LOG_INFO("Your Multitank can be 20 blocks tall.");
		Utils.LOG_INFO("Casings Count: "+this.multiblockCasingCount+" Valid Multiblock: "+(tAmount >= 16)+" Tank Storage Capacity:"+this.maximumFluidStorage+"L");
		this.tryForceNBTUpdate();
		return tAmount >= 16;
	}

	/*public int countCasings() {
		Utils.LOG_INFO("Counting Machine Casings");
		try{
		if (this.getBaseMetaTileEntity().getWorld() == null){
			Utils.LOG_INFO("Tile Entity's world was null for casing count.");
			return 0;
		}
		if (this.getBaseMetaTileEntity() == null){
			Utils.LOG_INFO("Tile Entity was null for casing count.");
			return 0;
		}
		} catch(NullPointerException r){
			Utils.LOG_INFO("Null Pointer Exception caught.");
			return 0;
		}
		int xDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(this.getBaseMetaTileEntity().getBackFacing()).offsetZ;
		if (!this.getBaseMetaTileEntity().getAirOffset(xDir, 0, zDir)) {
			Utils.LOG_INFO("Failed due to air being misplaced.");
			Utils.LOG_WARNING("Must be hollow.");
			return 0;
		}
		int tAmount = 0;
		Utils.LOG_INFO("Casing Count set to 0.");
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 19; h++) {
					if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
						IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(11))) && (!addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(11))) && (!addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(11))) && (!addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(11)))) {
							if (this.getBaseMetaTileEntity().getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								if (h < 3){
									Utils.LOG_WARNING("Casing Expected.");
									return 0;
								}
								else if (h >= 3){
									//Utils.LOG_WARNING("Your Multitank can be 20 blocks tall.");
								}
							}
							if (this.getBaseMetaTileEntity().getMetaIDOffset(xDir + i, h, zDir + j) != 11) {
								if (h < 3){
									Utils.LOG_WARNING("Wrong Meta.");
									return 0;
								}
								else if (h >= 3){
									//Utils.LOG_WARNING("Your Multitank can be 20 blocks tall.");
								}
							}
							if (h < 3){
								tAmount++;
							}
							else if (h >= 3){
								if (this.getBaseMetaTileEntity().getBlockOffset(xDir + i, h, zDir + j) == Blocks.air || this.getBaseMetaTileEntity().getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName().contains("residual")){
									Utils.LOG_WARNING("Found air");
								}
								else {
									Utils.LOG_WARNING("Layer "+(h+2)+" is complete. Adding "+(64000*9)+"L storage to the tank.");
									tAmount++;
								}
							}
						}
					}
				}
			}
		}
		Utils.LOG_INFO("Finished counting.");
		multiblockCasingCount = (short) tAmount;
		//Utils.LOG_INFO("Your Multitank can be 20 blocks tall.");
		Utils.LOG_INFO("Casings Count: "+tAmount+" Valid Multiblock: "+(tAmount >= 16)+" Tank Storage Capacity:"+getMaximumTankStorage(tAmount)+"L");
		return tAmount;
	}*/

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(final ItemStack aStack) {
		return 5;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	private static short getStorageMultiplier(final int casingCount){
		final int tsm = 1*casingCount;
		if (tsm <= 0){
			return 1;
		}
		return (short) tsm;
	}

	private static int getMaximumTankStorage(final int casingCount){
		final int multiplier = getStorageMultiplier(casingCount);
		final int tempTankStorageMax = 128000*multiplier;
		if (tempTankStorageMax <= 0){return 128000;}
		return tempTankStorageMax;
	}

	private boolean tryForceNBTUpdate(){
/*
		//Block is invalid.
		if ((this == null) || (this.getBaseMetaTileEntity() == null)){
			Utils.LOG_WARNING("Block was not valid for saving data.");
			return false;
		}

		//Don't need this to run clientside.
		if (!this.getBaseMetaTileEntity().isServerSide()) {
			return false;
		}

		//Internal Tag was not valid.
		try{
			if (this.internalCraftingComponentsTag == null){
				Utils.LOG_WARNING("Internal NBT data tag was null.");
				return false;
			}
		} catch (final NullPointerException x){
			Utils.LOG_WARNING("Caught null NBT.");
		}

		//Internal tag was valid.
		this.saveNBTData(this.internalCraftingComponentsTag);


		//Mark block for update
		int x,y,z = 0;
		x = this.getBaseMetaTileEntity().getXCoord();
		y = this.getBaseMetaTileEntity().getYCoord();
		z = this.getBaseMetaTileEntity().getZCoord();
		this.getBaseMetaTileEntity().getWorld().markBlockForUpdate(x, y, z);

		//Mark block dirty, let chunk know it's data has changed and it must be saved to disk. (Albeit slowly)
		this.getBaseMetaTileEntity().markDirty();*/
		return true;
	}
}