package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.bedrock;

import gregtech.api.util.GT_ModHandler;
import gregtech.api.enums.GT_Values;
import net.minecraft.tileentity.TileEntity;
import gregtech.common.blocks.GT_TileEntity_Ores;

import gtPlusPlus.api.objects.Logger;

import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.api.enums.ItemList;
import net.minecraft.entity.player.EntityPlayer;
import gregtech.api.objects.ItemData;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraftforge.fluids.FluidStack;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.util.GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import net.minecraft.init.Blocks;
import gregtech.api.util.GT_Utility;
import net.minecraft.nbt.NBTTagCompound;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import net.minecraft.entity.player.InventoryPlayer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.GregTech_API;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.ChunkPosition;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;

public abstract class GregtechMetaTileEntity_BedrockMiningPlatformBase extends GT_MetaTileEntity_MultiBlockBase {

	//
	private static final ItemStack miningPipe;
	private static final ItemStack miningPipeTip;
	private static final Block miningPipeBlock;
	private static final Block miningPipeTipBlock;

	private final ArrayList<ChunkPosition> oreBlockPositions;

	private Block casingBlock;
	private int casingMeta;
	private int frameMeta;
	private int casingTextureIndex;

	private ForgeDirection back;

	private int xDrill;
	private int yDrill;
	private int zDrill;

	private int[] xCenter = new int[5];
	private int[] zCenter = new int[5];
	private int[] yHead = new int[5];
	private boolean[] isPickingPipes = new boolean[5];

	public GregtechMetaTileEntity_BedrockMiningPlatformBase(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		this.oreBlockPositions = new ArrayList<ChunkPosition>();
		this.initFields();
	}

	public GregtechMetaTileEntity_BedrockMiningPlatformBase(final String aName) {
		super(aName);
		this.oreBlockPositions = new ArrayList<ChunkPosition>();
		this.initFields();
	}

	private void initFields() {
		this.casingBlock = this.getCasingBlockItem().getBlock();
		this.casingMeta = this.getCasingBlockItem().get(0L, new Object[0]).getItemDamage();
		final int frameId = 4096 + this.getFrameMaterial().mMetaItemSubID;
		this.frameMeta = ((GregTech_API.METATILEENTITIES[frameId] != null)
				? GregTech_API.METATILEENTITIES[frameId].getTileEntityBaseType()
						: 32767);
		this.casingTextureIndex = this.getCasingTextureIndex();

		for (int g=0;g<5;g++) {
			this.isPickingPipes[g] = false;
		}
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[this.casingTextureIndex],
					new GT_RenderedTexture((IIconContainer) (aActive
							? Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_ACTIVE
									: Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL))};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[this.casingTextureIndex]};
	}

	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"OreDrillingPlant.png");
	}

	public void saveNBTData(final NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);

		for (int g=0;g<5;g++) {
			aNBT.setBoolean("isPickingPipes"+g, this.isPickingPipes[g]);
		}
	}

	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		for (int g=0;g<5;g++) {
			this.isPickingPipes[g] = aNBT.getBoolean("isPickingPipes"+g);
		}
	}

	public boolean checkRecipe(final ItemStack aStack) {
		this.setElectricityStats();
		
		boolean[] didWork = new boolean[5];

		final int oldYHead = this.yHead[0];
		if (!this.checkPipesAndSetYHead() || !this.isEnergyEnough()) {
			this.stopMachine();
			return false;
		}
		if (this.yHead[0] != oldYHead) {
			this.oreBlockPositions.clear();
		}
		
		for (int g=0;g<5;g++) {
			if (this.isPickingPipes[g]) {
				if (this.tryPickPipe(g)) {
					this.mOutputItems = new ItemStack[]{GT_Utility.copyAmount(1L, new Object[]{GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe})};
					didWork[g] = true;
					continue;
				}
				this.isPickingPipes[g] = false;
				this.stopMachine();
				didWork[g] = false;
			} else {
				this.putMiningPipesFromInputsInController();
				/*if (!this.tryConsumeDrillingFluid()) {
					return false;
				}*/
				//this.fillMineListIfEmpty();
				if (this.oreBlockPositions.isEmpty()) {
					//Logger.INFO("[Bedrock Miner] No Stored Ores.");
					if (!this.tryLowerPipe(g)) {
						//Logger.INFO("[Bedrock Miner] Fail [3]");
						
						//Hit bedrock Either retract pipe or Dig!
						//this.isPickingPipes[g] = true;
						//didWork[g] = this.isPickingPipes[g];
						
						didWork[g] = true;
					}
					//this.fillMineListIfEmpty();
				}
			}			
		}
		
		//Fail recipe handling if one pipe didn't handle properly, to try again next run.
		for (boolean y : didWork) {
			if (!y) {
				return false;
			}
		}		
		//Logger.INFO("[Bedrock Miner] Success? [2]");
		return true;
	}

	private boolean isEnergyEnough() {
		long requiredEnergy = 512L + this.getMaxInputVoltage() * 4L;
		for (final GT_MetaTileEntity_Hatch_Energy energyHatch : this.mEnergyHatches) {
			requiredEnergy -= energyHatch.getEUVar();
			if (requiredEnergy <= 0L) {
				return true;
			}
		}
		return false;
	}

	private boolean tryPickPipe(int pipe) {
		if (this.yHead[pipe] == this.yDrill) {
				return false;
		}
		boolean didWork[] = new boolean[3];			
		didWork[0] = this.checkBlockAndMeta(this.xCenter[pipe], this.yHead[pipe] + 1, this.zCenter[pipe],	GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeBlock, 32767);
		if (didWork[0]) {
			didWork[1] = this.getBaseMetaTileEntity().getWorld().setBlock(this.xCenter[pipe], this.yHead[pipe] + 1, this.zCenter[pipe],GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeTipBlock);
		}
		if (didWork[1]) {
			didWork[2] = this.getBaseMetaTileEntity().getWorld().setBlockToAir(this.xCenter[pipe], this.yHead[pipe], this.zCenter[pipe]);	
		}
		
		if (didWork[0] && didWork[1] && didWork[2]) {
			return true;
		}
		return false;
	}

	private void setElectricityStats() {
		this.mEfficiency = this.getCurrentEfficiency((ItemStack) null);
		this.mEfficiencyIncrease = 10000;
		final int overclock = 1 << GT_Utility.getTier(this.getMaxInputVoltage()) - 1;
		this.mEUt = -12 * overclock * overclock;
		int mCombinedAvgTime = 0;
		for (int g=0;g<5;g++) {
			mCombinedAvgTime += (this.isPickingPipes[g] ? 80 : this.getBaseProgressTime()) / overclock;
		}		
		this.mMaxProgresstime = (mCombinedAvgTime/5);
	}

	private ItemStack[] getOutputByDrops(final ArrayList<ItemStack> oreBlockDrops) {
		final long voltage = this.getMaxInputVoltage();
		final ArrayList<ItemStack> outputItems = new ArrayList<ItemStack>();
		while (!oreBlockDrops.isEmpty()) {
			final ItemStack currentItem = oreBlockDrops.remove(0).copy();
			if (!this.doUseMaceratorRecipe(currentItem)) {
				this.multiplyStackSize(currentItem);
				outputItems.add(currentItem);
			} else {
				final GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.findRecipe(
						(IHasWorldObjectAndCoords) this.getBaseMetaTileEntity(), false, voltage, (FluidStack[]) null,
						new ItemStack[]{currentItem});
				if (tRecipe == null) {
					outputItems.add(currentItem);
				} else {
					for (int i = 0; i < tRecipe.mOutputs.length; ++i) {
						final ItemStack recipeOutput = tRecipe.mOutputs[i].copy();
						if (this.getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(i)) {
							this.multiplyStackSize(recipeOutput);
						}
						outputItems.add(recipeOutput);
					}
				}
			}
		}
		return outputItems.toArray(new ItemStack[0]);
	}

	private boolean doUseMaceratorRecipe(final ItemStack currentItem) {
		final ItemData itemData = GT_OreDictUnificator.getItemData(currentItem);
		return itemData == null
				|| (itemData.mPrefix != OrePrefixes.crushed && itemData.mPrefix != OrePrefixes.dustImpure
				&& itemData.mPrefix != OrePrefixes.dust && itemData.mMaterial.mMaterial != Materials.Oilsands);
	}

	private void multiplyStackSize(final ItemStack itemStack) {
		itemStack.stackSize *= this.getBaseMetaTileEntity().getRandomNumber(4) + 1;
	}

	private ArrayList<ItemStack> getBlockDrops(final Block oreBlock, final int posX, final int posY, final int posZ) {
		final int blockMeta = this.getBaseMetaTileEntity().getMetaID(posX, posY, posZ);
		if (oreBlock.canSilkHarvest(this.getBaseMetaTileEntity().getWorld(), (EntityPlayer) null, posX, posY, posZ,
				blockMeta)) {
			return new ArrayList<ItemStack>() {
				{
					this.add(new ItemStack(oreBlock, 1, blockMeta));
				}
			};
		}
		return (ArrayList<ItemStack>) oreBlock.getDrops(this.getBaseMetaTileEntity().getWorld(), posX, posY, posZ,
				blockMeta, 1);
	}

	private boolean tryConsumeDrillingFluid() {
		return this.depleteInput(new FluidStack(ItemList.sDrillingFluid, 2000));
	}

	private void putMiningPipesFromInputsInController() {
		final int maxPipes = GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe.getMaxStackSize();
		if (this.isHasMiningPipes(maxPipes)) {
			return;
		}
		ItemStack pipes = this.getStackInSlot(1);
		for (final ItemStack storedItem : this.getStoredInputs()) {
			if (!storedItem.isItemEqual(GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe)) {
				continue;
			}
			if (pipes == null) {
				this.setInventorySlotContents(1,
						GT_Utility.copy(new Object[]{GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe}));
				pipes = this.getStackInSlot(1);
			}
			if (pipes.stackSize == maxPipes) {
				break;
			}
			final int needPipes = maxPipes - pipes.stackSize;
			final int transferPipes = (storedItem.stackSize < needPipes) ? storedItem.stackSize : needPipes;
			final ItemStack itemStack = pipes;
			itemStack.stackSize += transferPipes;
			final ItemStack itemStack2 = storedItem;
			itemStack2.stackSize -= transferPipes;
		}
		this.updateSlots();
	}

	private void fillMineListIfEmpty() {
		/*if (!this.oreBlockPositions.isEmpty()) {
			return;
		}
		this.tryAddOreBlockToMineList(this.xCenter, this.yHead - 1, this.zCenter);
		if (this.yHead == this.yDrill) {
			return;
		}
		for (int radius = this.getRadiusInChunks() << 4, xOff = -radius; xOff <= radius; ++xOff) {
			for (int zOff = -radius; zOff <= radius; ++zOff) {
				this.tryAddOreBlockToMineList(this.xDrill + xOff, this.yHead, this.zDrill + zOff);
			}
		}*/
	}

	private void tryAddOreBlockToMineList(final int x, final int y, final int z) {
		final Block block = this.getBaseMetaTileEntity().getBlock(x, y, z);
		final int blockMeta = this.getBaseMetaTileEntity().getMetaID(x, y, z);
		final ChunkPosition blockPos = new ChunkPosition(x, y, z);
		if (this.oreBlockPositions.contains(blockPos)) {
			return;
		}
		if (block instanceof GT_Block_Ores_Abstract) {
			final TileEntity tTileEntity = this.getBaseMetaTileEntity().getTileEntity(x, y, z);
			if (tTileEntity != null && tTileEntity instanceof GT_TileEntity_Ores
					&& ((GT_TileEntity_Ores) tTileEntity).mNatural) {
				this.oreBlockPositions.add(blockPos);
			}
		} else {
			final ItemData association = GT_OreDictUnificator.getAssociation(new ItemStack(block, 1, blockMeta));
			if (association != null && association.mPrefix.toString().startsWith("ore")) {
				this.oreBlockPositions.add(blockPos);
			}
		}
	}

	private boolean tryLowerPipe(int pipe) {
		if (!this.isHasMiningPipes()) {
			Logger.INFO("[Bedrock Miner] No Pipes to Lower.");
			return false;
		}		
		boolean didWork[] = new boolean[3];			
		didWork[0] = this.checkBlockAndMeta(this.xCenter[pipe], this.yHead[pipe] + 1, this.zCenter[pipe],	GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeBlock, 32767);
		if (didWork[0]) {
			didWork[1] = (this.yHead[pipe] != this.yDrill);
			}
		if (didWork[1]) {
			didWork[2] = this.getBaseMetaTileEntity().getWorld().setBlock(this.xCenter[pipe], this.yHead[pipe], this.zCenter[pipe], GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeBlock);	
		}
		
		if (didWork[0] && didWork[1] && didWork[2]) {
			this.getBaseMetaTileEntity().decrStackSize(1, 1);
			return true;
		}		
		return false;
	}

	private boolean isHasMiningPipes() {
		return this.isHasMiningPipes(1);
	}

	private boolean isHasMiningPipes(final int minCount) {
		final ItemStack pipe = this.getStackInSlot(1);
		return pipe != null && pipe.stackSize > minCount - 1
				&& pipe.isItemEqual(GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe);
	}

	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		this.updateCoordinates();
		for (int xOff = -1 + this.back.offsetX; xOff <= 1 + this.back.offsetX; ++xOff) {
			for (int zOff = -1 + this.back.offsetZ; zOff <= 1 + this.back.offsetZ; ++zOff) {
				if (xOff != 0 || zOff != 0) {
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xOff, 0,
							zOff);
					if (!this.checkCasingBlock(xOff, 0, zOff)
							&& !this.addMaintenanceToMachineList(tTileEntity, this.casingTextureIndex)
							&& !this.addInputToMachineList(tTileEntity, this.casingTextureIndex)
							&& !this.addOutputToMachineList(tTileEntity, this.casingTextureIndex)
							&& !this.addEnergyInputToMachineList(tTileEntity, this.casingTextureIndex)) {
						Logger.INFO("[Bedrock Miner] Found bad block in Structure.");
						return false;
					}
				}
			}
		}
		if (this.mMaintenanceHatches.isEmpty() || this.mInputHatches.isEmpty() || this.mOutputBusses.isEmpty()
				|| this.mEnergyHatches.isEmpty()) {
			Logger.INFO("[Bedrock Miner] Missing Hatches/Busses.");
			return false;
		}
		if (GT_Utility.getTier(this.getMaxInputVoltage()) < this.getMinTier()) {
			Logger.INFO("[Bedrock Miner] getMaxInputVoltage() < getMinTier().");
			return false;
		}
		for (int yOff = 1; yOff < 4; ++yOff) {
			if (!this.checkCasingBlock(this.back.offsetX, yOff, this.back.offsetZ)
					|| !this.checkFrameBlock(this.back.offsetX + 1, yOff, this.back.offsetZ)
					|| !this.checkFrameBlock(this.back.offsetX - 1, yOff, this.back.offsetZ)
					|| !this.checkFrameBlock(this.back.offsetX, yOff, this.back.offsetZ + 1)
					|| !this.checkFrameBlock(this.back.offsetX, yOff, this.back.offsetZ - 1)
					|| !this.checkFrameBlock(this.back.offsetX, yOff + 3, this.back.offsetZ)) {
				Logger.INFO("[Bedrock Miner] Missing Frames? yOff = "+yOff);
				return false;
			}
		}
		Logger.INFO("[Bedrock Miner] Built.");
		return true;
	}

	private void updateCoordinates() {
		this.xDrill = this.getBaseMetaTileEntity().getXCoord();
		this.yDrill = this.getBaseMetaTileEntity().getYCoord();
		this.zDrill = this.getBaseMetaTileEntity().getZCoord();
		this.back = ForgeDirection.getOrientation((int) this.getBaseMetaTileEntity().getBackFacing());

		//Middle
		this.xCenter[0] = this.xDrill + this.back.offsetX;
		this.zCenter[0] = this.zDrill + this.back.offsetZ;

		this.xCenter[1] = xCenter[0] + 1;
		this.zCenter[1] = zCenter[0];

		this.xCenter[2] = xCenter[0] - 1;
		this.zCenter[2] = zCenter[0];

		this.xCenter[3] = xCenter[0];
		this.zCenter[3] = zCenter[0] + 1;

		this.xCenter[4] = xCenter[0];
		this.zCenter[4] = zCenter[0] - 1;

	}

	private boolean checkPipesAndSetYHead() {
		for (int g=0;g<5;g++) {		
			this.yHead[g] = this.yDrill - 1;
			//Logger.INFO("[Bedrock Miner] Set yHead["+g+"] to "+this.yHead[g]+".");
			while (this.checkBlockAndMeta(this.xCenter[g], this.yHead[g], this.zCenter[g],
					GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeBlock, 32767)) {
				--this.yHead[g];
			}
			if (this.checkBlockAndMeta(this.xCenter[g], this.yHead[g], this.zCenter[g],
					GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeTipBlock, 32767) || ++this.yHead[g] == this.yDrill) {
				continue;
			}
			this.getBaseMetaTileEntity().getWorld().setBlock(this.xCenter[g], this.yHead[g], this.zCenter[g],
					GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeTipBlock);

		}
		return true;
	}

	private boolean checkCasingBlock(final int xOff, final int yOff, final int zOff) {
		return this.checkBlockAndMetaOffset(xOff, yOff, zOff, this.casingBlock, this.casingMeta);
	}

	private boolean checkFrameBlock(final int xOff, final int yOff, final int zOff) {
		return this.checkBlockAndMetaOffset(xOff, yOff, zOff, GregTech_API.sBlockMachines, this.frameMeta);
	}

	private boolean checkBlockAndMetaOffset(final int xOff, final int yOff, final int zOff, final Block block,
			final int meta) {
		return this.checkBlockAndMeta(this.xDrill + xOff, this.yDrill + yOff, this.zDrill + zOff, block, meta);
	}

	private boolean checkBlockAndMeta(final int x, final int y, final int z, final Block block, final int meta) {
		return (meta == 32767 || this.getBaseMetaTileEntity().getMetaID(x, y, z) == meta)
				&& this.getBaseMetaTileEntity().getBlock(x, y, z) == block;
	}

	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	protected abstract ItemList getCasingBlockItem();

	protected abstract Materials getFrameMaterial();

	protected abstract int getCasingTextureIndex();

	protected abstract int getRadiusInChunks();

	protected abstract int getMinTier();

	protected abstract int getBaseProgressTime();

	protected String[] getDescriptionInternal(final String tierSuffix) {
		final String casings = this.getCasingBlockItem().get(0L, new Object[0]).getDisplayName();
		return new String[]{"Controller Block for the Experimental Deep Earth Drilling Platform - MK " + ((tierSuffix != null) ? tierSuffix : ""),
				"Size(WxHxD): 3x7x3, Controller (Front middle bottom)", "3x1x3 Base of " + casings,
				"1x3x1 " + casings + " pillar (Center of base)",
				"1x3x1 " + this.getFrameMaterial().mName + " Frame Boxes (Each pillar side and on top)",
				"1x Input Hatch for drilling fluid (Any bottom layer casing)",
				"1x Input Bus for mining pipes (Any bottom layer casing; not necessary)",
				"1x Output Bus (Any bottom layer casing)", "1x Maintenance Hatch (Any bottom layer casing)",
				"1x " + GT_Values.VN[this.getMinTier()] + "+ Energy Hatch (Any bottom layer casing)",
				"Radius is " + (this.getRadiusInChunks() << 4) + " blocks"};
	}

	static {
		miningPipe = GT_ModHandler.getIC2Item("miningPipe", 0L);
		miningPipeTip = GT_ModHandler.getIC2Item("miningPipeTip", 0L);
		miningPipeBlock = GT_Utility.getBlockFromStack(GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe);
		miningPipeTipBlock = GT_Utility.getBlockFromStack(GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeTip);
	}
}