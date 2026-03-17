package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.debugBlockMiner;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.common.misc.DrillingLogicDelegate;
import gregtech.common.misc.IDrillingLogicDelegateOwner;

public class MTEMiner extends MTEBasicMachine implements IDrillingLogicDelegateOwner, IAddUIWidgets {

    static final int[] RADIUS = { 8, 8, 16, 24, 32 }; // Miner radius per tier
    static final int[] SPEED = { 160, 160, 80, 40, 20 }; // Miner cycle time per tier
    static final int[] ENERGY = { 8, 8, 32, 128, 512 }; // Miner energy consumption per tier

    /** Miner configured radius */
    private int radiusConfig;
    /** Found ore blocks cache of current drill depth */
    private final ArrayList<ChunkPosition> oreBlockPositions = new ArrayList<>();

    /** General pipe accessor */
    private final DrillingLogicDelegate pipe = new DrillingLogicDelegate(this);

    private final int mSpeed;

    private static String[] MTEMinerTooltip(int aTier) {
        return new String[] { StatCollector.translateToLocal(("GT5U.tooltip.miner.0")),
            StatCollector.translateToLocal("GT5U.tooltip.miner.1"),
            StatCollector.translateToLocal("GT5U.tooltip.miner.2"),
            StatCollector.translateToLocalFormatted("GT5U.tooltip.miner.3", ENERGY[aTier], SPEED[aTier] / 20),
            StatCollector
                .translateToLocalFormatted("GT5U.tooltip.miner.4", (RADIUS[aTier] * 2 + 1), (RADIUS[aTier] * 2 + 1)),
            StatCollector.translateToLocalFormatted("GT5U.tooltip.miner.5", aTier) };
    }

    public MTEMiner(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            MTEMinerTooltip(aTier),
            2,
            2,
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_SIDE_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_SIDE_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_SIDE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_SIDE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_FRONT_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_FRONT")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_FRONT_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_TOP_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_TOP_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_TOP")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_TOP_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_BOTTOM_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_BOTTOM_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_BOTTOM")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.customOptional("basicmachines/miner/OVERLAY_BOTTOM_GLOW"))
                    .glow()
                    .build()));
        mSpeed = SPEED[aTier];
        radiusConfig = RADIUS[mTier];
    }

    public MTEMiner(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 2);
        mSpeed = SPEED[aTier];
        radiusConfig = RADIUS[mTier];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMiner(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        pipe.findTipDepth();
        fillOreList(aBaseMetaTileEntity);
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack) //
            && aStack.getItem() == DrillingLogicDelegate.MINING_PIPE_STACK.getItem();
    }

    /** Both output slots must be free to work */
    public boolean hasFreeSpace() {
        for (int i = getOutputSlot(); i < getOutputSlot() + 2; i++) {
            if (mInventory[i] != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
        if (side != getBaseMetaTileEntity().getFrontFacing() && side != mMainFacing) {
            if (aPlayer.isSneaking()) {
                if (radiusConfig >= 0) {
                    radiusConfig--;
                }
                if (radiusConfig < 0) {
                    radiusConfig = RADIUS[mTier];
                }
            } else {
                if (radiusConfig <= RADIUS[mTier]) {
                    radiusConfig++;
                }
                if (radiusConfig > RADIUS[mTier]) {
                    radiusConfig = 0;
                }
            }

            GTUtility.sendChatToPlayer(
                aPlayer,
                String.format(
                    "%s %dx%d",
                    StatCollector.translateToLocal("GT5U.machines.workareaset"),
                    (radiusConfig * 2 + 1),
                    (radiusConfig * 2 + 1)));

            // Rebuild ore cache after change config
            fillOreList(getBaseMetaTileEntity());
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!aBaseMetaTileEntity.isServerSide()) {
            return;
        }

        // Pipe workaround
        pipe.onOwnerPostTick(aBaseMetaTileEntity, aTick);

        if (!aBaseMetaTileEntity.isAllowedToWork()) {
            mMaxProgresstime = 0;
            if (debugBlockMiner) {
                GTLog.out.println("MINER: Disabled");
            }
            return;
        }

        if (!hasFreeSpace()) {
            mMaxProgresstime = 0;
            if (debugBlockMiner) {
                GTLog.out.println("MINER: No free space");
            }
            return;
        }

        if (!aBaseMetaTileEntity.isUniversalEnergyStored((long) ENERGY[mTier] * (mSpeed - mProgresstime))) {
            mMaxProgresstime = 0;
            if (debugBlockMiner) {
                GTLog.out.println(
                    "MINER: Not enough energy yet, want " + (ENERGY[mTier] * mSpeed)
                        + " have "
                        + aBaseMetaTileEntity.getUniversalEnergyStored());
            }
            return;
        }

        /* Checks if machine are waiting new mining pipe item */
        if (!pipe.canContinueDrilling(aTick)) {
            mMaxProgresstime = 0;
            return;
        }

        mMaxProgresstime = mSpeed;

        aBaseMetaTileEntity.decreaseStoredEnergyUnits(ENERGY[mTier], true);

        // Real working only when progress done. TODO some legacy code... refactorings needed
        if (mProgresstime == mSpeed - 1) {
            if (pipe.getTipDepth() == 0 || oreBlockPositions.isEmpty()) {
                boolean descends = pipe.descent(aBaseMetaTileEntity);
                if (descends) {
                    fillOreList(aBaseMetaTileEntity);
                }
            } else {
                int x;
                int y;
                int z;
                Block oreBlock;
                boolean isOre;
                do {
                    ChunkPosition oreBlockPos = oreBlockPositions.remove(0);
                    oreBlock = aBaseMetaTileEntity
                        .getBlockOffset(oreBlockPos.chunkPosX, oreBlockPos.chunkPosY, oreBlockPos.chunkPosZ);
                    x = aBaseMetaTileEntity.getXCoord() + oreBlockPos.chunkPosX;
                    y = aBaseMetaTileEntity.getYCoord() + oreBlockPos.chunkPosY;
                    z = aBaseMetaTileEntity.getZCoord() + oreBlockPos.chunkPosZ;
                    isOre = GTUtility.isOre(
                        oreBlock,
                        aBaseMetaTileEntity.getWorld()
                            .getBlockMetadata(x, y, z));
                } // someone else might have removed the block
                while (!isOre && !oreBlockPositions.isEmpty());

                if (isOre) {
                    pipe.mineBlock(aBaseMetaTileEntity, oreBlock, x, y, z);
                }
            }
        }
    }

    /** Finds the ores in current drill Y level */
    private void fillOreList(IGregTechTileEntity aBaseMetaTileEntity) {
        if (pipe.getTipDepth() == 0) {
            return;
        }
        oreBlockPositions.clear();
        for (int z = -radiusConfig; z <= radiusConfig; ++z) {
            for (int x = -radiusConfig; x <= radiusConfig; ++x) {
                Block block = aBaseMetaTileEntity.getBlockOffset(x, pipe.getTipDepth(), z);
                int blockMeta = aBaseMetaTileEntity.getMetaIDOffset(x, pipe.getTipDepth(), z);

                if (GTUtility.isOre(block, blockMeta)) {
                    oreBlockPositions.add(new ChunkPosition(x, pipe.getTipDepth(), z));
                }
            }
        }
    }

    /** Pulls (or check can pull) items from an input slots. */
    @Override
    public boolean pullInputs(Item item, int count, boolean simulate) {
        for (int i = 0; i < mInputSlotCount; i++) {
            ItemStack stack = getInputAt(i);
            if (stack != null && stack.getItem() == item && stack.stackSize >= count) {
                if (simulate) {
                    return true;
                }
                stack.stackSize -= count;
                if (stack.stackSize == 0) {
                    mInventory[getInputSlot() + i] = null;
                }
                return true;
            }
        }
        return false;
    }

    /** Pushes (or check can push) item to output slots. */
    @Override
    public boolean pushOutputs(ItemStack stack, int count, boolean simulate, boolean allowInputSlots) {
        return allowInputSlots && pushOutput(getInputSlot(), getInputSlot() + mInputSlotCount, stack, count, simulate)
            || pushOutput(getOutputSlot(), getOutputSlot() + mOutputItems.length, stack, count, simulate);
    }

    private boolean pushOutput(int startIndex, int endIndex, ItemStack stack, int count, boolean simulate) {
        for (int i = startIndex; i < endIndex; i++) {
            ItemStack slot = mInventory[i];
            if (slot == null || slot.stackSize == 0) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.stackSize = count;
                    mInventory[i] = copy;
                }
                return true;
            } else if (GTUtility.areStacksEqual(slot, stack) && slot.stackSize <= slot.getMaxStackSize() - count) {
                if (!simulate) {
                    slot.stackSize += count;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public long maxEUStore() {
        return Math.max(V[mTier] * 64, 4096);
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (radiusConfig != RADIUS[mTier]) aNBT.setInteger("radiusConfig", radiusConfig);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("radiusConfig", radiusConfig);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("radiusConfig")) {
            int newRadius = aNBT.getInteger("radiusConfig");
            if (RADIUS[mTier] <= newRadius && newRadius > 0) {
                radiusConfig = newRadius;
            }
        }
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            String.format(
                "%s%s%s",
                EnumChatFormatting.BLUE,
                StatCollector.translateToLocal("GT5U.machines.miner"),
                EnumChatFormatting.RESET),
            String.format(
                "%s: %s%d%s %s",
                StatCollector.translateToLocal("GT5U.machines.workarea"),
                EnumChatFormatting.GREEN,
                (radiusConfig * 2 + 1),
                EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.machines.blocks")) };
    }

    @Override
    public int getMachineTier() {
        return mTier;
    }

    @Override
    public int getMachineSpeed() {
        return mSpeed;
    }

    public DrillingLogicDelegate getPipe() {
        return pipe;
    }

    private static final FallbackableUITexture progressBarTexture = GTUITextures
        .fallbackableProgressbar("miner", GTUITextures.PROGRESSBAR_CANNER);

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .progressBarTexture(progressBarTexture)
            .build();
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_MINER;
    }
}
