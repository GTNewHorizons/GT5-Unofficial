package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.MapMaker;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import appeng.api.AEApi;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.ResultMissingItem;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.render.RenderingTileEntityWormhole;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public class MTEWormholeGenerator extends MTEEnhancedMultiBlockBase<MTEWormholeGenerator>
    implements ISurvivalConstructable {

    /**
     * Number of seconds to average the wormhole energy over. (controls the weights in a weighted average)
     */
    public static int WH_ENERGY_AVG_WINDOW = 5;

    /**
     * The amount of EU received per EU sent.
     */
    public static double TRANSFER_EFFICIENCY = 0.995;

    /**
     * The amount of EU to lose every second the wormhole decays.
     */
    public static double DECAY_RATE = 0.25;

    /**
     * The amount of EU that the wormhole collapses at (EU/s).
     */
    public static double COLLAPSE_THRESHOLD = 20;

    /**
     * The max number of 'overclocks' allowed when the wormhole's energy is increasing.
     */
    public static int MAX_OVERCLOCKS = 2;

    /**
     * The number of seconds to record for scan EU/t measurements. Purely visual and not saved.
     */
    public static int SCAN_AVG_WINDOW = 10;

    /**
     * The wormhole render radius percent of the max size when specified wormhole power is reached (purely aesthetical)
     */
    public static double RENDER_PERCENT_TARGET = .1;

    /**
     * Wormhole energy to specify reach the specified size percentage (purely aesthetical)
     */
    public static double RENDER_TARGET_ENERGY = TierEU.IV * 256;

    /**
     * Maximum wormhole radius (purely aesthetical)
     */
    public static double RENDER_MAX_RADIUS = 2.999 / Math.sqrt(3);

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int TT_CASING_INDEX = BlockGTCasingsTT.textureOffset;

    private static final int TOP_HATCH = 0, BOTTOM_HATCH = 1, LEFT_HATCH = 2, RIGHT_HATCH = 3, BACK_HATCH = 4,
        FRONT_HATCH = 5, MAX_HATCHES = 6;

    private static final int[] OPPOSITES = { BOTTOM_HATCH, TOP_HATCH, RIGHT_HATCH, LEFT_HATCH, FRONT_HATCH,
        BACK_HATCH, };

    private static final String[] HATCH_NAMES = { "Top", "Bottom", "Left", "Right", "Back", "Front" };
    private static final boolean[] HATCH_MASK = { true, true, true, true, false, false };

    private int glassTier = -1;
    private boolean mStructureBadGlassTier = false;

    private final MTEHatchEnergyMulti[] mSendHatches = new MTEHatchEnergyMulti[MAX_HATCHES];
    private final MTEHatchDynamoMulti[] mReceiveHatches = new MTEHatchDynamoMulti[MAX_HATCHES];

    private final ItemStack singularity = AEApi.instance()
        .definitions()
        .materials()
        .singularity()
        .maybeStack(1)
        .get();
    private final ItemStack qeSingularity = AEApi.instance()
        .definitions()
        .materials()
        .qESingularity()
        .maybeStack(1)
        .get();

    private WormholeLink mLink;
    private final WeakReference<MTEWormholeGenerator> mSelfReference = new WeakReference<>(this);

    private double mWormholeEnergy_UI = 0;

    private boolean mAllowOverclocks = true;
    private boolean mIsUnloading = false;

    public MTEWormholeGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEWormholeGenerator(String aName) {
        super(aName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEWormholeGenerator(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) {
                return new ITexture[] { getCasingTextureForId(TT_CASING_INDEX), TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                    .extFacing()
                    .build() };
            } else {
                return new ITexture[] { getCasingTextureForId(TT_CASING_INDEX), TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                    .extFacing()
                    .build() };
            }
        }
        return new ITexture[] { getCasingTextureForId(TT_CASING_INDEX) };
    }

    // #region Structure

    // spotless:off
    private static final IStructureDefinition<MTEWormholeGenerator> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEWormholeGenerator>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][]{
                    {"       ","   F   ","  EEE  "," FEtEF ","  EEE  ","   F   ","       "},
                    {"   F   "," AADAA "," A B A ","FDBBBDF"," A B A "," AADAA ","   F   "},
                    {"  EEE  "," A B A ","E     E","EB   BE","E     E"," A B A ","  EEE  "},
                    {" FE~EF ","FA B AF","E     E","lB   Br","E     E","FA B AF"," FEEEF "},
                    {"  EEE  "," A B A ","E     E","EB   BE","E     E"," A B A ","  EEE  "},
                    {"   F   "," AADAA "," A B A ","FDBBBDF"," A B A "," AADAA ","   F   "},
                    {"       ","   F   ","  EEE  "," FEbEF ","  EEE  ","   F   ","       "}
                }))
        .addElement('E',
            buildHatchAdder(MTEWormholeGenerator.class)
                .atLeast(Maintenance, InputBus)
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .hint(1)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 5)) // Europium Reinforced Radiation Proof Machine Casing
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings4, 7)) // Fusion Coil Block
        .addElement('F', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 4))) // Molecular Casing
        .addElement('t',
            buildHatchAdder(MTEWormholeGenerator.class)
                .anyOf(new TransferHatch(TOP_HATCH))
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .hint(2)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .addElement('b',
            buildHatchAdder(MTEWormholeGenerator.class)
                .anyOf(new TransferHatch(BOTTOM_HATCH))
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .hint(2)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .addElement('l',
            buildHatchAdder(MTEWormholeGenerator.class)
                .anyOf(new TransferHatch(LEFT_HATCH))
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .hint(2)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .addElement('r',
            buildHatchAdder(MTEWormholeGenerator.class)
                .anyOf(new TransferHatch(RIGHT_HATCH))
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .hint(2)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .build();
    // spotless:on

    private static class TransferHatch implements IHatchElement<MTEWormholeGenerator> {

        public final int mIndex;

        public TransferHatch(int index) {
            this.mIndex = index;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Arrays.asList(MTEHatchEnergyMulti.class, MTEHatchDynamoMulti.class);
        }

        @Override
        public IGTHatchAdder<? super MTEWormholeGenerator> adder() {
            return (tile, aTileEntity, aBaseCasingIndex) -> {
                if (aTileEntity == null) return false;

                IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();

                if (aMetaTileEntity == null) return false;

                if (aMetaTileEntity instanceof MTEHatchEnergyMulti input) {
                    input.updateTexture(aBaseCasingIndex);
                    input.updateCraftingIcon(tile.getMachineCraftingIcon());
                    tile.mSendHatches[mIndex] = input;
                    return true;
                } else if (aMetaTileEntity instanceof MTEHatchDynamoMulti output) {
                    output.updateTexture(aBaseCasingIndex);
                    output.updateCraftingIcon(tile.getMachineCraftingIcon());
                    tile.mReceiveHatches[mIndex] = output;
                    return true;
                }

                return false;
            };
        }

        @Override
        public String name() {
            return "TransferHatch";
        }

        @Override
        public long count(MTEWormholeGenerator t) {
            return t.mExoticEnergyHatches.size();
        }
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GT_MACHINES_FUSION_LOOP;
    }

    @Override
    public IStructureDefinition<MTEWormholeGenerator> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        Arrays.fill(mSendHatches, null);
        Arrays.fill(mReceiveHatches, null);
        glassTier = -1;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0)) return false;

        mStructureBadGlassTier = false;

        for (MTEHatch energyHatch : mExoticEnergyHatches) {
            if (energyHatch.getBaseMetaTileEntity() == null) {
                continue;
            }

            if (energyHatch.getTierForStructure() > glassTier) {
                mStructureBadGlassTier = true;
                break;
            }
        }

        return !mStructureBadGlassTier;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    // #endregion

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        destroyRenderBlock();
    }

    @Override
    public void onDisableWorking() {
        super.onDisableWorking();
        // destroyRenderBlock();
    }

    @Override
    public void onStructureChange() {
        super.onStructureChange();
        if (!checkStructure(false, this.getBaseMetaTileEntity())) {
            destroyRenderBlock();
        }
    }

    private void destroyRenderBlock() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        if (gregTechTileEntity.getWorld() == null) {
            return;
        }

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        int xOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        int yOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        int zOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ;

        int xTarget = x + xOffset;
        int yTarget = y + yOffset;
        int zTarget = z + zOffset;

        Optional.of(gregTechTileEntity)
            .map(IHasWorldObjectAndCoords::getWorld)
            .ifPresent(w -> w.setBlock(xTarget, yTarget, zTarget, Blocks.air));
    }

    @Nullable
    private RenderingTileEntityWormhole createRenderBlock() {

        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        World world = gregTechTileEntity.getWorld();

        if (world == null) {
            return null;
        }

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        int xOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        int yOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        int zOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ;

        int xTarget = x + xOffset;
        int yTarget = y + yOffset;
        int zTarget = z + zOffset;

        world.setBlock(xTarget, yTarget, zTarget, Blocks.air);
        world.setBlock(xTarget, yTarget, zTarget, GregTechAPI.sWormholeRender);

        return (RenderingTileEntityWormhole) world.getTileEntity(xTarget, yTarget, zTarget);
    }

    @Nullable
    private RenderingTileEntityWormhole getRenderBlock() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        int wX = (int) (x + xOffset);
        int wY = (int) (y + yOffset);
        int wZ = (int) (z + zOffset);

        TileEntity tile = Optional.ofNullable(gregTechTileEntity.getWorld())
            .map(w -> w.getTileEntity(wX, wY, wZ))
            .orElse(null);
        if (tile instanceof RenderingTileEntityWormhole wormhole) return wormhole;
        return null;

    }

    public void updateRenderDim() {
        World target = Optional.ofNullable(mLink)
            .map(link -> link.getDest(mSelfReference))
            .map(MetaTileEntity::getBaseMetaTileEntity)
            .map(IHasWorldObjectAndCoords::getWorld)
            .orElse(null);

        RenderingTileEntityWormhole hole = getRenderBlock();
        if (hole == null) hole = createRenderBlock();

        if (hole != null) {
            hole.setDimFromWorld(target);
        }
    }

    public void updateRenderRadius(double radius) {
        RenderingTileEntityWormhole hole = getRenderBlock();
        if (hole == null) hole = createRenderBlock();

        if (hole != null) {
            hole.setRadius(radius);
        }
    }

    private static double wormholeRadiusCalc(double energy) {
        if (energy < COLLAPSE_THRESHOLD) return 0;

        double offset = RENDER_TARGET_ENERGY + (COLLAPSE_THRESHOLD - RENDER_TARGET_ENERGY) / (RENDER_PERCENT_TARGET);
        return RENDER_MAX_RADIUS * (COLLAPSE_THRESHOLD - energy) / (offset - energy);
    }

    // #region Logic

    @Override
    public void onUnload() {
        super.onUnload();
        mIsUnloading = true;
        if (mLink != null) {
            mLink.disconnect(mSelfReference);
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!aPlayer.isSneaking()) {
            mAllowOverclocks = !mAllowOverclocks;
            GTUtility.sendChatTrans(
                aPlayer,
                mAllowOverclocks ? "GT5U.chat.worm_hole_generator.overclocks.enable"
                    : "GT5U.chat.worm_hole_generator.overclocks.disable");
        } else {
            super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
        }
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        checkFrequency();

        mMaxProgresstime = 20;
        mEfficiency = Math.max(0, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000));

        if (doRandomMaintenanceDamage()) {
            if (onRunningTick(mInventory[1])) {
                markDirty();

                if (++mProgresstime >= mMaxProgresstime) {
                    mProgresstime = 0;

                    if (mLink != null) {
                        mLink.update(mSelfReference);
                    }
                    checkRecipe();
                }
            }
        }
    }

    @Override
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        if (mLink == null || !mLink.isConnected(mSelfReference)
            || getBaseMetaTileEntity() == null
            || !getBaseMetaTileEntity().isAllowedToWork()) {
            return SimpleCheckRecipeResult.ofFailure("none");
        }

        if (mLink.isActive()) {
            for (int i = 0; i < MAX_HATCHES; i++) {
                if (!HATCH_MASK[i]) continue;
                long optimal = mLink.mWormholeEnergy > Long.MAX_VALUE ? Long.MAX_VALUE : ((long) mLink.mWormholeEnergy);
                if (getTransferable(i) > 0) {
                    if (mLink.mWormholeEnergy <= 0) {
                        ItemStack singularityStack = singularity.copy();

                        if (!depleteInput(singularityStack)) {
                            return new ResultMissingItem(singularityStack);
                        }
                        mLink.mWormholeEnergy = 1;
                        optimal = 1;
                    }
                    transferPower(optimal, i);
                }
            }
        }

        if (mLink.mWormholeEnergy > 0) {
            return SimpleCheckRecipeResult.ofSuccess("none");
        }
        return SimpleCheckRecipeResult.ofFailure("none");
    }

    private void checkFrequency() {
        if (mIsUnloading) {
            return;
        }

        ItemStack link = null;

        for (ItemStack slot : mInventory) {
            if (slot != null && qeSingularity.isItemEqual(slot)) {
                link = slot;
                break;
            }
        }

        Long freq = link != null && link.getTagCompound() != null
            && link.getTagCompound()
                .hasKey("freq", 4 /* Long */) ? link.getTagCompound()
                    .getLong("freq") : null;

        if (!Objects.equals(freq, mLink == null ? null : mLink.mFrequency)) {
            if (mLink != null) {
                mLink.disconnect(mSelfReference);
                mLink = null;
            }

            if (freq != null) {
                mLink = WormholeLink.get(freq);
                mLink.connect(mSelfReference);
            }
        }
    }

    private long getTransferable(int index) {
        MTEWormholeGenerator dest = mLink.getDest(mSelfReference);

        if (dest == null || mMaxProgresstime == 0 || dest.mMaxProgresstime == 0) {
            return 0;
        }

        MTEHatchEnergyMulti inputHatch = mSendHatches[index];
        MTEHatchDynamoMulti outputHatch = dest.mReceiveHatches[OPPOSITES[index]];

        if (inputHatch == null || outputHatch == null) {
            return 0;
        }

        long available = inputHatch.getEUVar();
        long empty = outputHatch.maxEUStore() - outputHatch.getEUVar();

        return Math.min(available, empty);
    }

    private void transferPower(long optimal, int index) {
        MTEWormholeGenerator dest = mLink.getDest(mSelfReference);
        if (dest == null) {
            return;
        }

        MTEHatchEnergyMulti inputHatch = mSendHatches[index];
        MTEHatchDynamoMulti outputHatch = dest.mReceiveHatches[OPPOSITES[index]];

        if (inputHatch == null || outputHatch == null) {
            return;
        }

        long available = inputHatch.getEUVar();
        long empty = outputHatch.maxEUStore() - outputHatch.getEUVar();
        long maxSend = inputHatch.maxAmperesIn() * V[inputHatch.mTier] * 20;
        long maxReceive = outputHatch.maxAmperesOut() * V[outputHatch.mTier] * 20;
        long maxIO = (long) (optimal * Math.pow(4.0, MAX_OVERCLOCKS));

        // spotless:off
        double maintenance_efficiency = (1.0 - (dest.getIdealStatus() - dest.getRepairStatus()) * 0.1);

        long toSend = GTUtility.min(available, empty, maxSend, maxReceive, maxIO);

        int overclocks = 0;

        if (mAllowOverclocks) {
            overclocks = (int) GTUtility.log4(toSend / optimal);
            overclocks = MathHelper.clamp_int(overclocks, 0, MAX_OVERCLOCKS);
        }

        long toReceive = (long) (
            toSend *
            (Math.pow(2.0, overclocks) / Math.pow(4.0, overclocks)) *
            maintenance_efficiency *
            TRANSFER_EFFICIENCY
        );
        // spotless:on

        inputHatch.setEUVar(inputHatch.getEUVar() - toSend);
        outputHatch.setEUVar(outputHatch.getEUVar() + toReceive);

        double size = wormholeRadiusCalc((double) optimal / 20);
        this.updateRenderRadius(size);
        dest.updateRenderRadius(size);

        mLink.onEnergyTransferred(toSend);
        mLink.mSendAmounts[index] += toSend;
        mLink.mReceiveAmounts[OPPOSITES[index]] += toReceive;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setBoolean("mAllowOverclocks", mAllowOverclocks);

        try {
            if (mLink != null) {
                mLink.tryPromote();

                NBTTagCompound link = new NBTTagCompound();

                link.setLong("mFrequency", mLink.mFrequency);
                link.setDouble("mWormholeEnergy", mLink.mWormholeEnergy);

                if (mLink.isMaster(mSelfReference)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);

                    dos.writeInt(mLink.mReceiveAmounts.length);
                    for (long x : mLink.mReceiveAmounts) {
                        dos.writeLong(x);
                    }

                    dos.writeInt(mLink.mSendAmounts.length);
                    for (long x : mLink.mSendAmounts) {
                        dos.writeLong(x);
                    }

                    link.setByteArray("data", baos.toByteArray());
                }

                aNBT.setTag("mLink", link);
            }
        } catch (Exception t) {
            GTMod.GT_FML_LOGGER.error("Could not save MTEWormholeGenerator", t);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        // check the inventory and populate mLink if the inv contains a singularity
        checkFrequency();

        mAllowOverclocks = aNBT.getBoolean("mAllowOverclocks");

        // if we have a singularity and the freq matches what's saved, then we need to load the saved data into mLink
        if (aNBT.hasKey("mLink") && mLink != null) {
            NBTTagCompound link = aNBT.getCompoundTag("mLink");

            long freq = link.getLong("mFrequency");

            try {
                if (freq == mLink.mFrequency && mLink.isMaster(mSelfReference)) {
                    mLink.mWormholeEnergy = link.getDouble("mWormholeEnergy");

                    ByteArrayInputStream bais = new ByteArrayInputStream(link.getByteArray("data"));
                    DataInputStream dis = new DataInputStream(bais);

                    long[] recv_amounts = new long[dis.readInt()];
                    for (int i = 0; i < recv_amounts.length; i++) {
                        recv_amounts[i] = dis.readLong();
                    }

                    System.arraycopy(
                        recv_amounts,
                        0,
                        mLink.mReceiveAmounts,
                        0,
                        Math.min(recv_amounts.length, mLink.mReceiveAmounts.length));

                    long[] send_amounts = new long[dis.readInt()];
                    for (int i = 0; i < send_amounts.length; i++) {
                        send_amounts[i] = dis.readLong();
                    }

                    System.arraycopy(
                        send_amounts,
                        0,
                        mLink.mSendAmounts,
                        0,
                        Math.min(send_amounts.length, mLink.mSendAmounts.length));
                }
            } catch (Exception t) {
                GTMod.GT_FML_LOGGER.error("Could not load MTEWormholeGenerator", t);
            }
        }
    }

    private static class WormholeLink {

        private final static Map<Long, WormholeLink> WORMHOLE_GENERATORS = new MapMaker().weakValues()
            .makeMap();

        public final long mFrequency;

        public WeakReference<MTEWormholeGenerator> mMaster, mSlave;

        public final long[] mSendAmounts = new long[MAX_HATCHES];
        public final long[] mReceiveAmounts = new long[MAX_HATCHES];

        public final long[][] mAvgSendAmounts = new long[MAX_HATCHES][SCAN_AVG_WINDOW];
        public final long[][] mAvgReceiveAmounts = new long[MAX_HATCHES][SCAN_AVG_WINDOW];

        public double mWormholeEnergy;
        private double mPendingEnergy;

        private WormholeLink(long frequency) {
            mFrequency = frequency;
        }

        public static WormholeLink get(long frequency) {
            return WORMHOLE_GENERATORS.computeIfAbsent(frequency, WormholeLink::new);
        }

        public void update(WeakReference<MTEWormholeGenerator> updater) {
            tryPromote();

            if (isMaster(updater)) {
                if (isActive() && mPendingEnergy > 0) {
                    if (mPendingEnergy < mWormholeEnergy) {
                        double delta = mWormholeEnergy * (1.0 - DECAY_RATE);

                        // if the wormhole is shrinking and the next tick would take it below the pending energy, just
                        // use the pending energy
                        if (mWormholeEnergy - delta < mPendingEnergy) {
                            mWormholeEnergy = mPendingEnergy;
                        } else {
                            mWormholeEnergy -= delta;
                        }
                    } else if (mPendingEnergy > mWormholeEnergy) {
                        double delta = mPendingEnergy / WH_ENERGY_AVG_WINDOW;

                        // if the wormhole is growing and the next tick would take it above the pending energy, just use
                        // the pending energy
                        if (mWormholeEnergy + delta > mPendingEnergy) {
                            mWormholeEnergy = mPendingEnergy;
                        } else {
                            mWormholeEnergy += delta;
                        }
                    }

                    mPendingEnergy = 0;
                } else {
                    mWormholeEnergy *= (1.0 - DECAY_RATE);

                    if (mWormholeEnergy < COLLAPSE_THRESHOLD) {
                        mWormholeEnergy = 0;
                    }
                }

                for (int hatch = 0; hatch < MAX_HATCHES; hatch++) {
                    for (int i = 0; i < SCAN_AVG_WINDOW; i++) {
                        if (i < SCAN_AVG_WINDOW - 1) {
                            mAvgReceiveAmounts[hatch][i] = mAvgReceiveAmounts[hatch][i + 1];
                            mAvgSendAmounts[hatch][i] = mAvgSendAmounts[hatch][i + 1];
                        } else {
                            mAvgReceiveAmounts[hatch][i] = mReceiveAmounts[hatch];
                            mAvgSendAmounts[hatch][i] = mSendAmounts[hatch];
                        }
                    }
                }

                Arrays.fill(mSendAmounts, 0);
                Arrays.fill(mReceiveAmounts, 0);
            }
        }

        public void onEnergyTransferred(long amount) {
            mPendingEnergy += amount;
        }

        public boolean isMaster(WeakReference<MTEWormholeGenerator> tile) {
            return mMaster == tile;
        }

        public boolean isConnected(WeakReference<MTEWormholeGenerator> tile) {
            return mMaster == tile || mSlave == tile;
        }

        public boolean isFormed() {
            return mMaster != null && mSlave != null;
        }

        public boolean connect(WeakReference<MTEWormholeGenerator> tile) {
            tryPromote();

            if (mMaster == null) {
                mMaster = tile;
                Optional.ofNullable(mMaster)
                    .map(Reference::get)
                    .ifPresent(MTEWormholeGenerator::updateRenderDim);

                Optional.ofNullable(mSlave)
                    .map(Reference::get)
                    .ifPresent(MTEWormholeGenerator::updateRenderDim);
                return true;
            }
            if (mSlave == null) {
                mSlave = tile;

                Optional.of(mMaster)
                    .map(Reference::get)
                    .ifPresent(MTEWormholeGenerator::updateRenderDim);

                Optional.ofNullable(mSlave)
                    .map(Reference::get)
                    .ifPresent(MTEWormholeGenerator::updateRenderDim);

                return true;
            }

            return false;
        }

        public void disconnect(WeakReference<MTEWormholeGenerator> tile) {
            Objects.requireNonNull(tile.get())
                .destroyRenderBlock();

            if (tile == mMaster) mMaster = null;
            if (tile == mSlave) mSlave = null;

            tryPromote();

            if (mMaster == null && mSlave == null) {
                WORMHOLE_GENERATORS.remove(mFrequency, this);
            }
        }

        public void tryPromote() {
            mMaster = tryClean(mMaster);
            mSlave = tryClean(mSlave);

            if (mMaster == null && mSlave != null) {
                mMaster = mSlave;
                mSlave = null;
            }
        }

        private static WeakReference<MTEWormholeGenerator> tryClean(WeakReference<MTEWormholeGenerator> tileReference) {
            if (tileReference != null) {
                MTEWormholeGenerator tile = tileReference.get();

                if (tile == null) {
                    return null;
                } else {
                    IGregTechTileEntity base = tile.getBaseMetaTileEntity();

                    if (base == null || base.isDead()) {
                        return null;
                    }
                }
            }

            return tileReference;
        }

        public MTEWormholeGenerator getDest(WeakReference<MTEWormholeGenerator> tile) {
            if (tile == mMaster) {
                return mSlave != null ? mSlave.get() : null;
            }

            if (tile == mSlave) {
                return mMaster != null ? mMaster.get() : null;
            }

            return null;
        }

        public boolean isActive() {
            boolean masterCanWork = Optional.ofNullable(mMaster)
                .map(WeakReference::get)
                .map(MTEWormholeGenerator::getBaseMetaTileEntity)
                .map(IGregTechTileEntity::isAllowedToWork)
                .orElse(false);

            boolean slaveCanWork = Optional.ofNullable(mSlave)
                .map(WeakReference::get)
                .map(MTEWormholeGenerator::getBaseMetaTileEntity)
                .map(IGregTechTileEntity::isAllowedToWork)
                .orElse(false);

            return masterCanWork && slaveCanWork;
        }
    }

    // #endregion

    // #region UI

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        // spotless:off
        tt.addMachineType("Wormhole Generator, MWG")
            .addInfo("Transfers EU between two wormhole generators")
            .addInfo("Wormholes are linked by placing an AE2 Entangled Singularity in each controller slot")
            .addInfo("The transfer rate is limited by the wormhole size, and the wormhole size is governed by the transfer rate")
            .addInfo("If the transfer rate is completely stable, the transfer efficiency is " + String.format("%.1f", TRANSFER_EFFICIENCY * 100.0) + "%")
            .addInfo("EU will only be transferred if there is space in the laser source hatch")
            .addInfo("Each laser target must have a laser source on the §oother§7 controller, on the §oopposite§7 side")
            .addInfo("Consumes an AE2 Singularity from an input bus each time the wormhole is kick-started")
            .addInfo("Right click the controller with a screwdriver to disable overclocking")
            .addGlassEnergyLimitInfo()
            .addTecTechHatchInfo()
            .beginStructureBlock(7, 9, 7, false)
            .addController("Front center")
            .addCasingInfoExactly("Molecular Casing", 2 * 12, false)
            .addCasingInfoExactly("Europium Reinforced Radiation Proof Machine Casing", 4, false)
            .addCasingInfoExactly("Fusion Coil Block", 3 * 4 + 5 * 2, false)
            .addCasingInfoRange("High Power Casing", 8 * 6 + 1, 8 * 6 + 1 + 4, false)
            .addCasingInfoExactly("Any Tiered Glass", 9 * 4, true)
            .addMaintenanceHatch("§61§r (Hint Block Number 1)")
            .addInputBus("§61§r (Hint Block Number 1)")
            .addDynamoHatch("§60§r - §64§r (Laser Only, Hint Block Number 2)")
            .addEnergyHatch("§60§r - §64§r (Laser Only, Hint Block Number 2)")
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(GTAuthors.AuthorPineapple + EnumChatFormatting.GRAY + ", Rendering by: " + EnumChatFormatting.WHITE + "BucketBrigade");
        // spotless:on

        return tt;
    }

    private static String getLocalizedHatchName(int index) {
        String unlocalizedName = "GT5U.infodata.wormhole_generator.hatch." + HATCH_NAMES[index].toLowerCase();
        if (StatCollector.canTranslate(unlocalizedName)) {
            return StatCollector.translateToLocal(unlocalizedName);
        }
        return HATCH_NAMES[index];
    }

    @Override
    public String[] getInfoData() {

        List<String> data = new ArrayList<>(Arrays.asList(super.getInfoData()));

        data.add(EnumChatFormatting.STRIKETHROUGH + "-----------------------");
        data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator"));

        if (mStructureBadGlassTier) {
            data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator.structure_error"));
            data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator.bad_class_tier"));
        }

        if (mLink == null) {
            data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator.no_link"));
        } else {
            if (!mLink.isFormed()) {
                data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator.status.no_destination"));
            } else {
                if (mLink.mWormholeEnergy > 0) {
                    if (mLink.isActive()) {
                        data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator.status.active"));
                    } else {
                        data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator.status.decaying"));
                    }
                } else {
                    boolean anyTransferable = false;

                    for (int i = 0; i < MAX_HATCHES; i++) {
                        if (!HATCH_MASK[i]) continue;

                        if (getTransferable(i) > 0) {
                            anyTransferable = true;
                            break;
                        }
                    }

                    if (anyTransferable) {
                        data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator.status.inactive"));
                    } else {
                        data.add(StatCollector.translateToLocal("GT5U.infodata.wormhole_generator.status.no_energy"));
                    }
                }

                double radius = Math.sqrt(mLink.mWormholeEnergy / 20.0 / 32.0);
                data.add(
                    StatCollector
                        .translateToLocalFormatted("GT5U.infodata.wormhole_generator.diameter", (long) (radius * 2)));

                data.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.infodata.wormhole_generator.optimal_transfer_speed",
                        mLink.mWormholeEnergy / 20));
            }
        }

        for (int i = 0; i < MAX_HATCHES; i++) {
            if (!HATCH_MASK[i]) continue;

            MTEHatchEnergyMulti inputHatch = mSendHatches[i];
            MTEHatchDynamoMulti outputHatch = mReceiveHatches[i];

            long avgSend = 0, avgReceive = 0, avgSendOpposite = 0, avgReceiveOpposite = 0;

            if (mLink != null) {
                long[] send = mLink.mAvgSendAmounts[i], recv = mLink.mAvgReceiveAmounts[i];
                long[] sendOpposite = mLink.mAvgSendAmounts[OPPOSITES[i]];
                long[] recvOpposite = mLink.mAvgReceiveAmounts[OPPOSITES[i]];

                for (int second = 0; second < SCAN_AVG_WINDOW; second++) {
                    avgSend += send[second];
                    avgReceive += recv[second];
                    avgSendOpposite += sendOpposite[second];
                    avgReceiveOpposite += recvOpposite[second];
                }

                avgSend /= SCAN_AVG_WINDOW;
                avgReceive /= SCAN_AVG_WINDOW;
                avgSendOpposite /= SCAN_AVG_WINDOW;
                avgReceiveOpposite /= SCAN_AVG_WINDOW;
            }

            // spotless:off
            if(inputHatch != null) {
                data.add(StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.wormhole_generator.transferred",
                    getLocalizedHatchName(i),
                    inputHatch.Amperes,
                    VN[inputHatch.mTier],
                    avgSend,
                    avgSend / 20 / V[inputHatch.mTier],
                    avgSend > 0 ? (avgReceiveOpposite / (double)avgSend * 100) : 0,
                    SCAN_AVG_WINDOW
                ));
            } else if(outputHatch != null) {
                data.add(StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.wormhole_generator.received",
                    getLocalizedHatchName(i),
                    outputHatch.Amperes,
                    VN[outputHatch.mTier],
                    avgReceive,
                    avgReceive / 20 / V[outputHatch.mTier],
                    avgSendOpposite > 0 ? (avgReceive / (double)avgSendOpposite * 100) : 0,
                    SCAN_AVG_WINDOW
                ));
            } else {
                data.add(StatCollector.translateToLocalFormatted("GT5U.infodata.wormhole_generator.not_present", getLocalizedHatchName(i)));
            }
            // spotless:on
        }

        data.add(EnumChatFormatting.STRIKETHROUGH + "-----------------------");

        return data.toArray(new String[0]);
    }

    @Override
    protected boolean useMui2() {
        return false;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widgets(TextWidget.dynamicString(() -> {
            if (mLink == null) {
                return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.missing_entangled_singularity");
            }

            if (!mLink.isFormed()) {
                return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.status.no_destination");
            }

            if (mLink.mWormholeEnergy > 0 && !mLink.isActive()) {
                return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.status.decaying");
            }

            if (mLink.mWormholeEnergy > 0) {
                return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.status.active");
            }

            return StatCollector.translateToLocal("GT5U.gui.text.wormhole_generator.status.inactive");
        })
            .setTextAlignment(Alignment.CenterLeft),

            TextWidget.dynamicString(() -> {
                if (mLink == null) {
                    return "";
                }

                // LV power = 1 angstrom in diameter
                double radius = Math.sqrt(mLink.mWormholeEnergy / 20.0 / 32.0);

                return StatCollector
                    .translateToLocalFormatted("GT5U.gui.text.wormhole_generator.diameter", (long) (radius * 2));
            })
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(w -> mWormholeEnergy_UI > 0),

            TextWidget.dynamicString(() -> {
                if (mLink == null) {
                    return "";
                }

                if (mLink.mWormholeEnergy >= 1e10) {
                    return StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.wormhole_generator.max_io.large",
                        mLink.mWormholeEnergy / 20);
                } else {
                    return StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.wormhole_generator.max_io",
                        (long) (mLink.mWormholeEnergy / 20));
                }
            })
                .setTextAlignment(Alignment.CenterLeft)
                .setEnabled(w -> mWormholeEnergy_UI > 0),

            new FakeSyncWidget.DoubleSyncer(
                () -> mLink != null ? mLink.mWormholeEnergy : 0,
                val -> mWormholeEnergy_UI = val));
    }

    // #endregion

}
