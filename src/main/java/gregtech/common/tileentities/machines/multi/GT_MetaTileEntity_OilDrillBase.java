package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_DRILL;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_DRILL_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_DRILL_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_DRILL_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.common.GT_UndergroundOil.undergroundOil;
import static gregtech.common.GT_UndergroundOil.undergroundOilReadInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public abstract class GT_MetaTileEntity_OilDrillBase extends GT_MetaTileEntity_DrillerBase {

    private final ArrayList<Chunk> mOilFieldChunks = new ArrayList<>();
    private int mOilId = 0;
    private int mOilFlow = 0;

    private int chunkRangeConfig = getRangeInChunks();

    public GT_MetaTileEntity_OilDrillBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_OilDrillBase(String aName) {
        super(aName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[] { getCasingTextureForId(casingTextureIndex), TextureFactory.builder()
                                                                                                          .addIcon(
                                                                                                                  OVERLAY_FRONT_OIL_DRILL_ACTIVE)
                                                                                                          .extFacing()
                                                                                                          .build(),
                    TextureFactory.builder()
                                  .addIcon(OVERLAY_FRONT_OIL_DRILL_ACTIVE_GLOW)
                                  .extFacing()
                                  .glow()
                                  .build() };
            return new ITexture[] { getCasingTextureForId(casingTextureIndex), TextureFactory.builder()
                                                                                             .addIcon(
                                                                                                     OVERLAY_FRONT_OIL_DRILL)
                                                                                             .extFacing()
                                                                                             .build(),
                    TextureFactory.builder()
                                  .addIcon(OVERLAY_FRONT_OIL_DRILL_GLOW)
                                  .extFacing()
                                  .glow()
                                  .build() };
        }
        return new ITexture[] { getCasingTextureForId(casingTextureIndex) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mOilId", mOilId);
        aNBT.setInteger("chunkRangeConfig", chunkRangeConfig);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mOilId = aNBT.getInteger("mOilId");
        if (aNBT.hasKey("chunkRangeConfig")) chunkRangeConfig = aNBT.getInteger("chunkRangeConfig");
    }

    protected GT_Multiblock_Tooltip_Builder createTooltip(String tierSuffix) {
        String casings = getCasingBlockItem().get(0)
                                             .getDisplayName();

        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Pump")
          .addInfo("Controller Block for the Oil/Gas/Fluid Drilling Rig " + (tierSuffix != null ? tierSuffix : ""))
          .addInfo("Works on " + getRangeInChunks() + "x" + getRangeInChunks() + " chunks")
          .addInfo("Use a Screwdriver to configure range")
          .addInfo("Use Programmed Circuits to ignore near exhausted oil field")
          .addInfo("If total circuit # is greater than output amount it will halt. If it worked right.") // doesn't
          // work
          .addSeparator()
          .beginStructureBlock(3, 7, 3, false)
          .addController("Front bottom")
          .addOtherStructurePart(casings, "form the 3x1x3 Base")
          .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
          .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
          .addEnergyHatch(VN[getMinTier()] + "+, Any base casing", 1)
          .addMaintenanceHatch("Any base casing", 1)
          .addInputBus("Mining Pipes or Circuits, optional, any base casing", 1)
          .addOutputHatch("Any base casing", 1)
          .toolTipFinisher("Gregtech");
        return tt;
    }

    protected abstract int getRangeInChunks();

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
        int oldChunkRange = chunkRangeConfig;
        if (aPlayer.isSneaking()) {
            if (chunkRangeConfig > 0) {
                chunkRangeConfig--;
            }
            if (chunkRangeConfig == 0) chunkRangeConfig = getRangeInChunks();
        } else {
            if (chunkRangeConfig <= getRangeInChunks()) {
                chunkRangeConfig++;
            }
            if (chunkRangeConfig > getRangeInChunks()) chunkRangeConfig = 1;
        }
        if (oldChunkRange != chunkRangeConfig) mOilFieldChunks.clear();
        GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.workareaset") + " "
                        + chunkRangeConfig
                        + "x"
                        + chunkRangeConfig
                        + StatCollector.translateToLocal("GT5U.machines.chunks")); // TODO Add translation support
    }

    @Override
    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mOutputHatches.isEmpty() && !mEnergyHatches.isEmpty();
    }

    @Override
    protected List<IHatchElement<? super GT_MetaTileEntity_DrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputBus, OutputHatch, Maintenance, Energy);
    }

    @Override
    protected void setElectricityStats() {
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(0, GT_Utility.getTier(getMaxInputVoltage()));
        this.mEUt = -7 << (tier << 1); // (1/4) A of current tier when at bottom (7/8) A of current tier while mining
        this.mMaxProgresstime = Math.max(
                1,
                (workState == STATE_AT_BOTTOM ? (64 * (chunkRangeConfig * chunkRangeConfig)) >> (getMinTier() - 1)
                        : 120) >> tier);
    }

    protected float computeSpeed() {
        return .5F + (GT_Utility.getTier(getMaxInputVoltage()) - getMinTier()) * .25F;
    }

    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
            int yHead, int oldYHead) {
        switch (tryLowerPipeState(true)) {
            case 0:
                workState = STATE_DOWNWARD;
                setElectricityStats();
                return true;
            case 3:
                workState = STATE_UPWARD;
                return true;
        }

        if (reachingVoidOrBedrock() && tryFillChunkList()) {
            if (mWorkChunkNeedsReload) {
                mCurrentChunk = new ChunkCoordIntPair(xDrill >> 4, zDrill >> 4);
                GT_ChunkManager.requestChunkLoad((TileEntity) getBaseMetaTileEntity(), null);
                mWorkChunkNeedsReload = false;
            }
            float speed = computeSpeed();
            FluidStack tFluid = pumpOil(speed);
            if (tFluid != null && tFluid.amount > getTotalConfigValue()) {
                this.mOutputFluids = new FluidStack[] { tFluid };
                return true;
            }
        }
        GT_ChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        workState = STATE_UPWARD;
        return true;
    }

    private boolean tryFillChunkList() {
        FluidStack tFluid, tOil;
        if (mOilId <= 0) {
            tFluid = undergroundOilReadInformation(getBaseMetaTileEntity());
            if (tFluid == null) return false;
            mOilId = tFluid.getFluidID();
        }
        if (debugDriller) {
            GT_Log.out.println(" Driller on  fluid = " + mOilId);
        }

        tOil = new FluidStack(FluidRegistry.getFluid(mOilId), 0);

        if (mOilFieldChunks.isEmpty()) {
            Chunk tChunk = getBaseMetaTileEntity().getWorld()
                                                  .getChunkFromBlockCoords(
                                                          getBaseMetaTileEntity().getXCoord(),
                                                          getBaseMetaTileEntity().getZCoord());
            int range = chunkRangeConfig;
            int xChunk = Math.floorDiv(tChunk.xPosition, range) * range; // Java was written by idiots. For negative
                                                                         // values, / returns rounded towards zero.
            // Fucking morons.
            int zChunk = Math.floorDiv(tChunk.zPosition, range) * range;
            if (debugDriller) {
                GT_Log.out.println(
                        "tChunk.xPosition = " + tChunk.xPosition
                                + " tChunk.zPosition = "
                                + tChunk.zPosition
                                + " xChunk = "
                                + xChunk
                                + " zChunk = "
                                + zChunk);
            }
            for (int i = 0; i < range; i++) {
                for (int j = 0; j < range; j++) {
                    if (debugDriller) {
                        GT_Log.out.println(" getChunkX = " + (xChunk + i) + " getChunkZ = " + (zChunk + j));
                    }
                    tChunk = getBaseMetaTileEntity().getWorld()
                                                    .getChunkFromChunkCoords(xChunk + i, zChunk + j);
                    tFluid = undergroundOilReadInformation(tChunk);
                    if (debugDriller) {
                        GT_Log.out.println(
                                " Fluid in chunk = " + tFluid.getFluid()
                                                             .getID());
                    }
                    if (tOil.isFluidEqual(tFluid) && tFluid.amount > 0) {
                        mOilFieldChunks.add(tChunk);
                        if (debugDriller) {
                            GT_Log.out.println(" Matching fluid, quantity = " + tFluid.amount);
                        }
                    }
                }
            }
        }
        if (debugDriller) {
            GT_Log.out.println("mOilFieldChunks.size = " + mOilFieldChunks.size());
        }
        return !mOilFieldChunks.isEmpty();
    }

    protected FluidStack pumpOil(float speed) {
        if (mOilId <= 0) return null;
        FluidStack tFluid, tOil;
        tOil = new FluidStack(FluidRegistry.getFluid(mOilId), 0);
        if (debugDriller) {
            GT_Log.out.println(" pump speed = " + speed);
        }

        ArrayList<Chunk> emptyChunks = new ArrayList<>();

        for (Chunk tChunk : mOilFieldChunks) {
            tFluid = undergroundOil(tChunk, speed);
            if (debugDriller) {
                GT_Log.out.println(
                        " chunkX = " + tChunk.getChunkCoordIntPair().chunkXPos
                                + " chunkZ = "
                                + tChunk.getChunkCoordIntPair().chunkZPos);
                if (tFluid != null) {
                    GT_Log.out.println("     Fluid pumped = " + tFluid.amount);
                } else {
                    GT_Log.out.println("     No fluid pumped ");
                }
            }
            if (tFluid == null || tFluid.amount < 1) emptyChunks.add(tChunk);
            if (tOil.isFluidEqual(tFluid)) tOil.amount += tFluid.amount;
        }
        for (Chunk tChunk : emptyChunks) {
            mOilFieldChunks.remove(tChunk);
        }
        mOilFlow = tOil.amount;
        return tOil.amount == 0 ? null : tOil;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_PUMP_OP;
    }

    @Override
    public String[] getInfoData() {
        List<String> l = new ArrayList<>(
                Arrays.asList(
                        EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.machines.oilfluidpump")
                                + EnumChatFormatting.RESET,
                        StatCollector.translateToLocal("GT5U.machines.workarea") + ": "
                                + EnumChatFormatting.GREEN
                                + (chunkRangeConfig)
                                + " x "
                                + (chunkRangeConfig)
                                + EnumChatFormatting.RESET
                                + " "
                                + StatCollector.translateToLocal("GT5U.machines.chunks"),
                        "Drilling fluid: " + EnumChatFormatting.GREEN
                                + (mOilId > 0 ? FluidRegistry.getFluid(mOilId)
                                                             .getName()
                                        : "None")
                                + EnumChatFormatting.RESET,
                        "Drilling flow: " + EnumChatFormatting.GREEN
                                + GT_Utility.formatNumbers(
                                        this.mMaxProgresstime > 0 ? (mOilFlow / this.mMaxProgresstime) : 0)
                                + EnumChatFormatting.RESET
                                + " L/t"));
        l.addAll(Arrays.asList(super.getInfoData()));
        return l.toArray(new String[0]);
    }
}
