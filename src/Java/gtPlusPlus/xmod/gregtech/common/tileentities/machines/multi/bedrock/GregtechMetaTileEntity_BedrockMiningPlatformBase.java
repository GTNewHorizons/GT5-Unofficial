package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.bedrock;

import gregtech.api.util.GT_ModHandler;
import gregtech.api.enums.GT_Values;
import net.minecraft.tileentity.TileEntity;
import gregtech.common.blocks.GT_TileEntity_Ores;
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
import java.util.Iterator;
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
	private int xCenter;
	private int zCenter;
	private int yHead;
	private boolean isPickingPipes;

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
		this.isPickingPipes = false;
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
		aNBT.setBoolean("isPickingPipes", this.isPickingPipes);
	}

	public void loadNBTData(final NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		this.isPickingPipes = aNBT.getBoolean("isPickingPipes");
	}

	public boolean checkRecipe(final ItemStack aStack) {
		this.setElectricityStats();
		final int oldYHead = this.yHead;
		if (!this.checkPipesAndSetYHead() || !this.isEnergyEnough()) {
			this.stopMachine();
			return false;
		}
		if (this.yHead != oldYHead) {
			this.oreBlockPositions.clear();
		}
		if (this.isPickingPipes) {
			if (this.tryPickPipe()) {
				this.mOutputItems = new ItemStack[]{
						GT_Utility.copyAmount(1L, new Object[]{GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipe})};
				return true;
			}
			this.isPickingPipes = false;
			this.stopMachine();
			return false;
		} else {
			this.putMiningPipesFromInputsInController();
			if (!this.tryConsumeDrillingFluid()) {
				return false;
			}
			this.fillMineListIfEmpty();
			if (this.oreBlockPositions.isEmpty()) {
				if (!this.tryLowerPipe()) {
					return this.isPickingPipes = true;
				}
				this.fillMineListIfEmpty();
			}
			ChunkPosition oreBlockPos;
			Block oreBlock;
			for (oreBlockPos = null, oreBlock = null; (oreBlock == null || oreBlock == Blocks.air)
					&& !this.oreBlockPositions.isEmpty(); oreBlock = this.getBaseMetaTileEntity()
							.getBlock(oreBlockPos.chunkPosX, oreBlockPos.chunkPosY, oreBlockPos.chunkPosZ)) {
				oreBlockPos = this.oreBlockPositions.remove(0);
			}
			if (oreBlock != null && oreBlock != Blocks.air) {
				final ArrayList<ItemStack> oreBlockDrops = this.getBlockDrops(oreBlock, oreBlockPos.chunkPosX,
						oreBlockPos.chunkPosY, oreBlockPos.chunkPosZ);
				this.getBaseMetaTileEntity().getWorld().setBlockToAir(oreBlockPos.chunkPosX, oreBlockPos.chunkPosY,
						oreBlockPos.chunkPosZ);
				this.mOutputItems = this.getOutputByDrops(oreBlockDrops);
			}
			return true;
		}
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

	private boolean tryPickPipe() {
		if (this.yHead == this.yDrill) {
			return false;
		}
		if (this.checkBlockAndMeta(this.xCenter, this.yHead + 1, this.zCenter,
				GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeBlock, 32767)) {
			this.getBaseMetaTileEntity().getWorld().setBlock(this.xCenter, this.yHead + 1, this.zCenter,
					GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeTipBlock);
		}
		this.getBaseMetaTileEntity().getWorld().setBlockToAir(this.xCenter, this.yHead, this.zCenter);
		return true;
	}

	private void setElectricityStats() {
		this.mEfficiency = this.getCurrentEfficiency((ItemStack) null);
		this.mEfficiencyIncrease = 10000;
		final int overclock = 1 << GT_Utility.getTier(this.getMaxInputVoltage()) - 1;
		this.mEUt = -12 * overclock * overclock;
		this.mMaxProgresstime = (this.isPickingPipes ? 80 : this.getBaseProgressTime()) / overclock;
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
		if (!this.oreBlockPositions.isEmpty()) {
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
		}
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

	private boolean tryLowerPipe() {
		if (!this.isHasMiningPipes()) {
			return false;
		}
		if (this.yHead <= 0) {
			return false;
		}
		if (this.checkBlockAndMeta(this.xCenter, this.yHead - 1, this.zCenter, Blocks.bedrock, 32767)) {
			return false;
		}
		this.getBaseMetaTileEntity().getWorld().setBlock(this.xCenter, this.yHead - 1, this.zCenter,
				GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeTipBlock);
		if (this.yHead != this.yDrill) {
			this.getBaseMetaTileEntity().getWorld().setBlock(this.xCenter, this.yHead, this.zCenter,
					GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeBlock);
		}
		this.getBaseMetaTileEntity().decrStackSize(1, 1);
		return true;
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
						return false;
					}
				}
			}
		}
		if (this.mMaintenanceHatches.isEmpty() || this.mInputHatches.isEmpty() || this.mOutputBusses.isEmpty()
				|| this.mEnergyHatches.isEmpty()) {
			return false;
		}
		if (GT_Utility.getTier(this.getMaxInputVoltage()) < this.getMinTier()) {
			return false;
		}
		for (int yOff = 1; yOff < 4; ++yOff) {
			if (!this.checkCasingBlock(this.back.offsetX, yOff, this.back.offsetZ)
					|| !this.checkFrameBlock(this.back.offsetX + 1, yOff, this.back.offsetZ)
					|| !this.checkFrameBlock(this.back.offsetX - 1, yOff, this.back.offsetZ)
					|| !this.checkFrameBlock(this.back.offsetX, yOff, this.back.offsetZ + 1)
					|| !this.checkFrameBlock(this.back.offsetX, yOff, this.back.offsetZ - 1)
					|| !this.checkFrameBlock(this.back.offsetX, yOff + 3, this.back.offsetZ)) {
				return false;
			}
		}
		return true;
	}

	private void updateCoordinates() {
		this.xDrill = this.getBaseMetaTileEntity().getXCoord();
		this.yDrill = this.getBaseMetaTileEntity().getYCoord();
		this.zDrill = this.getBaseMetaTileEntity().getZCoord();
		this.back = ForgeDirection.getOrientation((int) this.getBaseMetaTileEntity().getBackFacing());
		this.xCenter = this.xDrill + this.back.offsetX;
		this.zCenter = this.zDrill + this.back.offsetZ;
	}

	private boolean checkPipesAndSetYHead() {
		this.yHead = this.yDrill - 1;
		while (this.checkBlockAndMeta(this.xCenter, this.yHead, this.zCenter,
				GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeBlock, 32767)) {
			--this.yHead;
		}
		if (this.checkBlockAndMeta(this.xCenter, this.yHead, this.zCenter,
				GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeTipBlock, 32767) || ++this.yHead == this.yDrill) {
			return true;
		}
		this.getBaseMetaTileEntity().getWorld().setBlock(this.xCenter, this.yHead, this.zCenter,
				GregtechMetaTileEntity_BedrockMiningPlatformBase.miningPipeTipBlock);
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
		return new String[]{"Controller Block for the Ore Drilling Plant " + ((tierSuffix != null) ? tierSuffix : ""),
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