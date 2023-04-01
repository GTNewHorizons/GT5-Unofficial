package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_Values.VN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;

public abstract class GT_MetaTileEntity_OreDrillingPlantBase extends GT_MetaTileEntity_DrillerBase {

    private final ArrayList<ChunkPosition> oreBlockPositions = new ArrayList<>();
    protected int mTier = 1;
    private int chunkRadiusConfig = getRadiusInChunks();
    private boolean replaceWithCobblestone = true;

    GT_MetaTileEntity_OreDrillingPlantBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    GT_MetaTileEntity_OreDrillingPlantBase(String aName) {
        super(aName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("chunkRadiusConfig", chunkRadiusConfig);
        aNBT.setBoolean("replaceWithCobblestone", replaceWithCobblestone);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("chunkRadiusConfig")) {
            chunkRadiusConfig = aNBT.getInteger("chunkRadiusConfig");
        }
        if (aNBT.hasKey("replaceWithCobblestone")) {
            replaceWithCobblestone = aNBT.getBoolean("replaceWithCobblestone");
        }
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
        if (aPlayer.isSneaking()) {
            if (chunkRadiusConfig > 0) {
                chunkRadiusConfig--;
            }
            if (chunkRadiusConfig == 0) chunkRadiusConfig = getRadiusInChunks();
        } else {
            if (chunkRadiusConfig <= getRadiusInChunks()) {
                chunkRadiusConfig++;
            }
            if (chunkRadiusConfig > getRadiusInChunks()) chunkRadiusConfig = 1;
        }
        GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.workareaset") + " "
                        + (chunkRadiusConfig << 4)
                        + " "
                        + StatCollector.translateToLocal("GT5U.machines.radius"));
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
            float aZ) {
        replaceWithCobblestone = !replaceWithCobblestone;
        GT_Utility.sendChatToPlayer(aPlayer, "Replace with cobblestone " + replaceWithCobblestone);
        return true;
    }

    @Override
    protected boolean workingDownward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
            int yHead, int oldYHead) {
        if (yHead != oldYHead) oreBlockPositions.clear();

        if (mWorkChunkNeedsReload && mChunkLoadingEnabled) { // ask to load machine itself
            GT_ChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), null);
            mWorkChunkNeedsReload = false;
        }
        fillMineListIfEmpty(xDrill, yDrill, zDrill, xPipe, zPipe, yHead);
        if (oreBlockPositions.isEmpty()) {
            switch (tryLowerPipeState()) {
                case 2 -> {
                    mMaxProgresstime = 0;
                    return false;
                }
                case 3 -> {
                    workState = STATE_UPWARD;
                    return true;
                }
                case 1 -> {
                    workState = STATE_AT_BOTTOM;
                    return true;
                }
            }
            // new layer - fill again
            fillMineListIfEmpty(xDrill, yDrill, zDrill, xPipe, zPipe, yHead);
        }
        return processOreList();
    }

    private boolean processOreList() {
        ChunkPosition oreBlockPos = null;
        int x = 0, y = 0, z = 0;
        Block oreBlock = null;
        int oreBlockMetadata = 0;

        while ((oreBlock == null || !GT_Utility.isOre(oreBlock, oreBlockMetadata)) && !oreBlockPositions.isEmpty()) {
            oreBlockPos = oreBlockPositions.remove(0);
            x = oreBlockPos.chunkPosX;
            y = oreBlockPos.chunkPosY;
            z = oreBlockPos.chunkPosZ;
            if (GT_Utility.eraseBlockByFakePlayer(getFakePlayer(getBaseMetaTileEntity()), x, y, z, true))
                oreBlock = getBaseMetaTileEntity().getBlock(x, y, z);
            oreBlockMetadata = getBaseMetaTileEntity().getWorld()
                                                      .getBlockMetadata(x, y, z);
        }

        if (!tryConsumeDrillingFluid()) {
            oreBlockPositions.add(0, oreBlockPos);
            return false;
        }
        if (oreBlock != null && GT_Utility.isOre(oreBlock, oreBlockMetadata)) {
            short metaData = 0;
            TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntity(x, y, z);
            if (tTileEntity instanceof GT_TileEntity_Ores) {
                metaData = ((GT_TileEntity_Ores) tTileEntity).mMetaData;
            }

            Collection<ItemStack> oreBlockDrops = getBlockDrops(oreBlock, x, y, z);
            ItemStack cobble = GT_Utility.getCobbleForOre(oreBlock, metaData);
            if (replaceWithCobblestone) {
                getBaseMetaTileEntity().getWorld()
                                       .setBlock(
                                               x,
                                               y,
                                               z,
                                               Block.getBlockFromItem(cobble.getItem()),
                                               cobble.getItemDamage(),
                                               3);
            } else {
                getBaseMetaTileEntity().getWorld()
                                       .setBlockToAir(
                                               oreBlockPos.chunkPosX,
                                               oreBlockPos.chunkPosY,
                                               oreBlockPos.chunkPosZ);
            }
            mOutputItems = getOutputByDrops(oreBlockDrops);
        }
        return true;
    }

    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
            int yHead, int oldYHead) {
        if (!mChunkLoadingEnabled)
            return super.workingAtBottom(aStack, xDrill, yDrill, zDrill, xPipe, zPipe, yHead, oldYHead);

        if (mCurrentChunk == null) {
            createInitialWorkingChunk();
            return true;
        }

        if (mWorkChunkNeedsReload) {
            GT_ChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), mCurrentChunk);
            mWorkChunkNeedsReload = false;
            return true;
        }
        if (oreBlockPositions.isEmpty()) {
            fillChunkMineList(yHead, yDrill);
            if (oreBlockPositions.isEmpty()) {
                GT_ChunkManager.releaseChunk((TileEntity) getBaseMetaTileEntity(), mCurrentChunk);
                if (!moveToNextChunk(xDrill >> 4, zDrill >> 4)) workState = STATE_UPWARD;
                return true;
            }
        }
        return processOreList();
    }

    private void createInitialWorkingChunk() {
        final int centerX = getXDrill() >> 4;
        final int centerZ = getZDrill() >> 4;
        // use corner closest to the drill as mining area center
        final int leftRight = (getXDrill() - (centerX << 4)) < 8 ? 0 : 1;
        final int topBottom = (getZDrill() - (centerZ << 4)) < 8 ? 0 : 1;
        mCurrentChunk = new ChunkCoordIntPair(
                centerX - chunkRadiusConfig + leftRight,
                centerZ - chunkRadiusConfig + topBottom);
        GT_ChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), mCurrentChunk);
        mWorkChunkNeedsReload = false;
    }

    @Override
    protected boolean workingUpward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
            int yHead, int oldYHead) {
        if (!mChunkLoadingEnabled || oreBlockPositions.isEmpty())
            return super.workingUpward(aStack, xDrill, yDrill, zDrill, xPipe, zPipe, yHead, oldYHead);
        boolean result = processOreList();
        if (oreBlockPositions.isEmpty()) GT_ChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        return result;
    }

    private boolean moveToNextChunk(int centerX, int centerZ) {
        if (mCurrentChunk == null) return false;
        // use corner closest to the drill as mining area center
        final int left = centerX - chunkRadiusConfig + ((getXDrill() - (centerX << 4)) < 8 ? 0 : 1);
        final int right = left + chunkRadiusConfig * 2;
        final int bottom = centerZ + chunkRadiusConfig + ((getZDrill() - (centerZ << 4)) < 8 ? 0 : 1);

        int nextChunkX = mCurrentChunk.chunkXPos + 1;
        int nextChunkZ = mCurrentChunk.chunkZPos;

        // step to the next chunk
        if (nextChunkX >= right) {
            nextChunkX = left;
            ++nextChunkZ;
        }
        // skip center chunk - dug in workingDownward()
        if (nextChunkX == centerX && nextChunkZ == centerZ) {
            ++nextChunkX;

            if (nextChunkX >= right) {
                nextChunkX = left;
                ++nextChunkZ;
            }
        }

        if (nextChunkZ >= bottom) {
            mCurrentChunk = null;
            return false;
        }
        mCurrentChunk = new ChunkCoordIntPair(nextChunkX, nextChunkZ);
        GT_ChunkManager.requestChunkLoad(
                (TileEntity) getBaseMetaTileEntity(),
                new ChunkCoordIntPair(nextChunkX, nextChunkZ));
        return true;
    }

    @Override
    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mInputHatches.isEmpty()
                && !mOutputBusses.isEmpty()
                && !mEnergyHatches.isEmpty();
    }

    @Override
    protected List<IHatchElement<? super GT_MetaTileEntity_DrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, InputBus, OutputBus, Maintenance, Energy);
    }

    @Override
    protected void setElectricityStats() {
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));
        this.mEUt = -3 * (1 << (tier << 1));
        this.mMaxProgresstime = ((workState == STATE_DOWNWARD || workState == STATE_AT_BOTTOM) ? getBaseProgressTime()
                : 80) / (1 << tier);
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
    }

    private ItemStack[] getOutputByDrops(Collection<ItemStack> oreBlockDrops) {
        long voltage = getMaxInputVoltage();
        Collection<ItemStack> outputItems = new HashSet<>();
        oreBlockDrops.forEach(currentItem -> {
            if (!doUseMaceratorRecipe(currentItem)) {
                outputItems.add(multiplyStackSize(currentItem));
                return;
            }
            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.findRecipe(
                    getBaseMetaTileEntity(),
                    false,
                    voltage,
                    null,
                    currentItem);
            if (tRecipe == null) {
                outputItems.add(currentItem);
                return;
            }
            for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                ItemStack recipeOutput = tRecipe.mOutputs[i].copy();
                if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(i))
                    multiplyStackSize(recipeOutput);
                outputItems.add(recipeOutput);
            }
        });
        return outputItems.toArray(new ItemStack[0]);
    }

    private boolean doUseMaceratorRecipe(ItemStack currentItem) {
        ItemData itemData = GT_OreDictUnificator.getItemData(currentItem);
        return itemData == null || itemData.mPrefix != OrePrefixes.crushed && itemData.mPrefix != OrePrefixes.dustImpure
                && itemData.mPrefix != OrePrefixes.dust
                && itemData.mPrefix != OrePrefixes.gem
                && itemData.mPrefix != OrePrefixes.gemChipped
                && itemData.mPrefix != OrePrefixes.gemExquisite
                && itemData.mPrefix != OrePrefixes.gemFlawed
                && itemData.mPrefix != OrePrefixes.gemFlawless
                && itemData.mMaterial.mMaterial != Materials.Oilsands;
    }

    private ItemStack multiplyStackSize(ItemStack itemStack) {
        itemStack.stackSize *= getBaseMetaTileEntity().getRandomNumber(4) + 1;
        return itemStack;
    }

    private Collection<ItemStack> getBlockDrops(final Block oreBlock, int posX, int posY, int posZ) {
        final int blockMeta = getBaseMetaTileEntity().getMetaID(posX, posY, posZ);
        if (oreBlock.canSilkHarvest(getBaseMetaTileEntity().getWorld(), null, posX, posY, posZ, blockMeta)) {
            return Collections.singleton(new ItemStack(oreBlock, 1, blockMeta));
        } else return oreBlock.getDrops(getBaseMetaTileEntity().getWorld(), posX, posY, posZ, blockMeta, mTier + 3);
    }

    private boolean tryConsumeDrillingFluid() {
        if (!depleteInput(new FluidStack(ItemList.sDrillingFluid, 2000))) {
            mMaxProgresstime = 0;
            return false;
        }
        return true;
    }

    private void fillChunkMineList(int yHead, int yDrill) {
        if (mCurrentChunk == null || !oreBlockPositions.isEmpty()) return;
        final int minX = mCurrentChunk.chunkXPos << 4;
        final int maxX = minX + 16;
        final int minZ = mCurrentChunk.chunkZPos << 4;
        final int maxZ = minZ + 16;
        for (int x = minX; x < maxX; ++x)
            for (int z = minZ; z < maxZ; ++z) for (int y = yHead; y < yDrill; ++y) tryAddOreBlockToMineList(x, y, z);
    }

    private void fillMineListIfEmpty(int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead) {
        if (!oreBlockPositions.isEmpty()) return;

        tryAddOreBlockToMineList(xPipe, yHead - 1, zPipe);
        if (yHead == yDrill) return; // skip controller block layer

        if (mChunkLoadingEnabled) {
            int startX = (xDrill >> 4) << 4;
            int startZ = (zDrill >> 4) << 4;
            for (int x = startX; x < (startX + 16); ++x)
                for (int z = startZ; z < (startZ + 16); ++z) tryAddOreBlockToMineList(x, yHead, z);
        } else {
            int radius = chunkRadiusConfig << 4;
            for (int xOff = -radius; xOff <= radius; xOff++) for (int zOff = -radius; zOff <= radius; zOff++)
                tryAddOreBlockToMineList(xDrill + xOff, yHead, zDrill + zOff);
        }
    }

    private void tryAddOreBlockToMineList(int x, int y, int z) {
        Block block = getBaseMetaTileEntity().getBlock(x, y, z);
        int blockMeta = getBaseMetaTileEntity().getMetaID(x, y, z);
        ChunkPosition blockPos = new ChunkPosition(x, y, z);
        if (!oreBlockPositions.contains(blockPos)) {
            if (block instanceof GT_Block_Ores_Abstract) {
                TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntity(x, y, z);
                if (tTileEntity instanceof GT_TileEntity_Ores && ((GT_TileEntity_Ores) tTileEntity).mNatural)
                    oreBlockPositions.add(blockPos);
            } else if (GT_Utility.isOre(block, blockMeta)) oreBlockPositions.add(blockPos);
        }
    }

    protected abstract int getRadiusInChunks();

    protected abstract int getBaseProgressTime();

    protected GT_Multiblock_Tooltip_Builder createTooltip(String tierSuffix) {
        String casings = getCasingBlockItem().get(0)
                                             .getDisplayName();

        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Miner")
          .addInfo("Controller Block for the Ore Drilling Plant " + (tierSuffix != null ? tierSuffix : ""))
          .addInfo("Use a Screwdriver to configure block radius")
          .addInfo("Maximum radius is " + (getRadiusInChunks() << 4) + " blocks")
          .addInfo("Use Soldering iron to turn off chunk mode")
          .addInfo("Use Wire Cutter to toggle replacing mined blocks with cobblestone")
          .addInfo("In chunk mode, working area center is the chunk corner nearest to the drill")
          .addInfo("Gives ~3x as much crushed ore vs normal processing")
          .addInfo("Fortune bonus of " + (mTier + 3) + ". Only works on small ores")
          .addSeparator()
          .beginStructureBlock(3, 7, 3, false)
          .addController("Front bottom")
          .addOtherStructurePart(casings, "form the 3x1x3 Base")
          .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
          .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
          .addEnergyHatch(VN[getMinTier()] + "+, Any base casing", 1)
          .addMaintenanceHatch("Any base casing", 1)
          .addInputBus("Mining Pipes, optional, any base casing", 1)
          .addInputHatch("Drilling Fluid, any base casing", 1)
          .addOutputBus("Any base casing", 1)
          .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_MINER_OP;
    }

    @Override
    public String[] getInfoData() {
        final int diameter = chunkRadiusConfig * 2;
        return new String[] {
                EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.machines.minermulti")
                        + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.machines.workarea") + ": "
                        + EnumChatFormatting.GREEN
                        + diameter
                        + "x"
                        + diameter
                        + EnumChatFormatting.RESET
                        + " "
                        + StatCollector.translateToLocal("GT5U.machines.chunks") };
    }
}
