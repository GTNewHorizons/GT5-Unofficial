package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

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

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.MapMaker;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import appeng.api.AEApi;
import bartworks.API.BorosilicateGlass;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.ResultMissingItem;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.render.TileEntityWormhole;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public class MTEWormholeGenerator extends MTEEnhancedMultiBlockBase<MTEWormholeGenerator>
    implements ISurvivalConstructable {

    /**
     * Number of seconds to average the wormhole energy over.
     */
    public static int WH_ENERGY_AVG_WINDOW = 90;

    /**
     * The amount of EU received per EU sent.
     */
    public static double TRANSFER_EFFICIENCY = (1.0 - 0.00_0004 * 64);

    /**
     * The amount of EU to lose every second the wormhole decays.
     */
    public static double DECAY_RATE = 0.25;

    /**
     * The amount of EU that the wormhole collapses at.
     */
    public static double COLLAPSE_THRESHOLD = 32;

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
    private static final byte GLASS_TIER_UNSET = -2;

    private static final int TT_CASING_INDEX = BlockGTCasingsTT.textureOffset;

    private static final int TOP_HATCH = 0, BOTTOM_HATCH = 1, LEFT_HATCH = 2, RIGHT_HATCH = 3, BACK_HATCH = 4,
        FRONT_HATCH = 5, MAX_HATCHES = 6;

    private static final int[] OPPOSITES = { BOTTOM_HATCH, TOP_HATCH, RIGHT_HATCH, LEFT_HATCH, FRONT_HATCH,
        BACK_HATCH, };

    private static final String[] HATCH_NAMES = { "Top", "Bottom", "Left", "Right", "Back", "Front" };
    private static final boolean[] HATCH_MASK = { true, true, true, true, false, false };

    private byte mGlassTier = -2;
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

    private boolean mIsUnloading = false;

    public MTEWormholeGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEWormholeGenerator(String aName) {
        super(aName);
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
                .dot(1)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .addElement(
            'A',
            withChannel(
                "glass",
                BorosilicateGlass.ofBoroGlass(GLASS_TIER_UNSET, (te, t) -> te.mGlassTier = t, te -> te.mGlassTier))
        )
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 5)) // Europium Reinforced Radiation Proof Machine Casing
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings4, 7)) // Fusion Coil Block
        .addElement('F', lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 4))) // Molecular Casing
        .addElement('t',
            buildHatchAdder(MTEWormholeGenerator.class)
                .anyOf(new TransferHatch(TOP_HATCH))
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .dot(2)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .addElement('b',
            buildHatchAdder(MTEWormholeGenerator.class)
                .anyOf(new TransferHatch(BOTTOM_HATCH))
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .dot(2)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .addElement('l',
            buildHatchAdder(MTEWormholeGenerator.class)
                .anyOf(new TransferHatch(LEFT_HATCH))
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .dot(2)
                .buildAndChain(lazy(() -> ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))) // High Power Casing
        )
        .addElement('r',
            buildHatchAdder(MTEWormholeGenerator.class)
                .anyOf(new TransferHatch(RIGHT_HATCH))
                .casingIndex(TT_CASING_INDEX) // High Power Casing
                .dot(2)
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0)) return false;

        mStructureBadGlassTier = false;

        for (var energyHatch : mExoticEnergyHatches) {
            if (energyHatch.getBaseMetaTileEntity() == null) {
                continue;
            }

            if (energyHatch.getTierForStructure() > mGlassTier) {
                mStructureBadGlassTier = true;
            }
        }

        return !mStructureBadGlassTier;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();

        Arrays.fill(mSendHatches, null);
        Arrays.fill(mReceiveHatches, null);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
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
    private TileEntityWormhole createRenderBlock() {

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

        TileEntityWormhole wormhole = (TileEntityWormhole) world.getTileEntity(xTarget, yTarget, zTarget);

        if (wormhole == null) {
            return null;
        }
        return wormhole;
    }

    @Nullable
    private TileEntityWormhole getRenderBlock() {
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
        if (tile instanceof TileEntityWormhole wormhole) return wormhole;
        return null;

    }

    public void updateRenderDim() {
        TileEntityWormhole temp = getRenderBlock();

        World target = Optional.ofNullable(mLink)
            .map(link -> link.getDest(mSelfReference))
            .map(MetaTileEntity::getBaseMetaTileEntity)
            .map(IHasWorldObjectAndCoords::getWorld)
            .orElse(null);

        TileEntityWormhole hole = getRenderBlock();
        if (hole == null) hole = createRenderBlock();

        if (hole != null) {
            hole.setDimFromWorld(target);
        }
    }

    public void updateRenderRadius(double radius) {
        TileEntityWormhole hole = getRenderBlock();
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
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public void onUnload() {
        super.onUnload();
        mIsUnloading = true;
        if (mLink != null) {
            mLink.disconnect(mSelfReference);
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
                        var singularityStack = singularity.copy();

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

        for (var slot : mInventory) {
            if (slot != null && qeSingularity.getItem() == slot.getItem()
                && qeSingularity.getItemDamage() == slot.getItemDamage()) {
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
        var dest = mLink.getDest(mSelfReference);

        if (dest == null || mMaxProgresstime == 0 || dest.mMaxProgresstime == 0) {
            return 0;
        }

        var inputHatch = mSendHatches[index];
        var outputHatch = dest.mReceiveHatches[OPPOSITES[index]];

        if (inputHatch == null || outputHatch == null) {
            return 0;
        }

        long available = inputHatch.getEUVar();
        long empty = outputHatch.maxEUStore() - outputHatch.getEUVar();

        return Math.min(available, empty);
    }

    private void transferPower(long optimal, int index) {
        var dest = mLink.getDest(mSelfReference);
        if (dest == null) {
            return;
        }

        var inputHatch = mSendHatches[index];
        var outputHatch = dest.mReceiveHatches[OPPOSITES[index]];

        if (inputHatch == null || outputHatch == null) {
            return;
        }

        long available = inputHatch.getEUVar();
        long empty = outputHatch.maxEUStore() - outputHatch.getEUVar();
        long maxSend = inputHatch.maxAmperesIn() * V[inputHatch.mTier] * 20;
        long maxReceive = outputHatch.maxAmperesOut() * V[outputHatch.mTier] * 20;

        // spotless:off
        long toSend = (long)(Math.min(Math.min(Math.min(available, empty), maxSend), maxReceive) * (1.0 - (getIdealStatus() - getRepairStatus()) * 0.1));

        double overclocks = Math.max(Math.log((double)toSend / (double)optimal) / Math.log(4.0), 0.0);

        long toReceive = (long) (
            toSend *
            (1.0 / Math.pow(4.0, overclocks)) *
            Math.pow(2.0, overclocks) *
            (1.0 - (dest.getIdealStatus() - dest.getRepairStatus()) * 0.1) *
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
                    for (var x : mLink.mReceiveAmounts) {
                        dos.writeLong(x);
                    }

                    dos.writeInt(mLink.mSendAmounts.length);
                    for (var x : mLink.mSendAmounts) {
                        dos.writeLong(x);
                    }

                    link.setByteArray("data", baos.toByteArray());
                }

                aNBT.setTag("mLink", link);
            }
        } catch (Throwable t) {
            GTMod.GT_FML_LOGGER.error("Could not save GT_MetaTileEntity_WormholeGenerator", t);
        }

    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        checkFrequency();

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
            } catch (Throwable t) {
                GTMod.GT_FML_LOGGER.error("Could not load GT_MetaTileEntity_WormholeGenerator", t);
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
                    var delta = mPendingEnergy / WH_ENERGY_AVG_WINDOW;

                    if (mPendingEnergy < mWormholeEnergy) {
                        // if the wormhole is shrinking and the next tick would take it below the pending energy, just
                        // use the pending energy
                        if (mWormholeEnergy - delta < mPendingEnergy) {
                            mWormholeEnergy = mPendingEnergy;
                        } else {
                            mWormholeEnergy -= delta;
                        }
                    } else if (mPendingEnergy > mWormholeEnergy) {
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
                var tile = tileReference.get();

                if (tile == null) {
                    return null;
                } else {
                    var base = tile.getBaseMetaTileEntity();

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
        tt.addMachineType("Wormhole Generator")
            .addInfo("Controller for the Miniature Wormhole Generator.")
            .addInfo("Transfers EU between two wormhole generators.")
            .addInfo("Wormholes are linked by placing an AE2 Entangled Singularity in each controller slot.")
            .addInfo("The transfer rate is limited by the wormhole size, and the wormhole size is governed by the transfer rate.")
            .addInfo("If the transfer rate is completely stable, the transfer efficiency is " + String.format("%.3f", TRANSFER_EFFICIENCY * 100.0) + "%.")
            .addInfo("EU will only be transferred if there is space in the laser source hatch.")
            .addInfo("Each laser target must have a laser source on the §oother§7 controller, on the §oopposite§7 side.")
            .addInfo("Consumes an AE2 Singularity from an input bus each time the wormhole is kick-started.")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .beginStructureBlock(7, 9, 7, false)
            .addSeparator()
            .addCasingInfoExactly("Molecular Casing", 2 * 12, false)
            .addCasingInfoExactly("Europium Reinforced Radiation Proof Machine Casing", 4, false)
            .addCasingInfoExactly("Fusion Coil Block", 3 * 4 + 5 * 2, false)
            .addCasingInfoRange("High Power Casing", 8 * 6 + 1, 8 * 6 + 1 + 4, false)
            .addCasingInfoExactly("Borosilicate Glass (any)", 9 * 4, true)
            .addMaintenanceHatch("§61§r (dot 1)")
            .addInputBus("§61§r (dot 1)")
            .addDynamoHatch("§60§r - §64§r (laser only, dot 2)")
            .addEnergyHatch("§60§r - §64§r (laser only, dot 2)")
            .addStructureInfo("§rThe glass tier limits the hatch tier.")
            .addSubChannelUsage("glass", "Borosilicate Glass Tier")
            .toolTipFinisher("Gregtech");
        // spotless:on

        return tt;
    }

    @Override
    public String[] getInfoData() {
        List<String> data = new ArrayList<>();

        data.addAll(Arrays.asList(super.getInfoData()));

        data.add("-----------------------");
        data.add("Wormhole Generator Info");

        if (mStructureBadGlassTier) {
            data.add(String.format("§cStructure errors:§r"));

            if (mStructureBadGlassTier) {
                data.add(String.format("§cGlass tier must be greater than or equal to the energy hatch tiers.§r"));
            }
        }

        if (mLink == null) {
            data.add("An entangled singularity must be present in the controller slot");
        } else {
            if (!mLink.isFormed()) {
                data.add("Wormhole status: §cNo destination§f");
            } else {
                if (mLink.mWormholeEnergy > 0) {
                    if (mLink.isActive()) {
                        data.add("Wormhole status: §bActive§f");
                    } else {
                        data.add("Wormhole status: §6Decaying§f");
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
                        data.add("Wormhole status: §7Inactive§f");
                    } else {
                        data.add("Wormhole status: §7No energy in input hatches§f");
                    }
                }

                double radius = Math.sqrt(mLink.mWormholeEnergy / 20.0 / 32.0);
                data.add(String.format("Wormhole diameter: §b%,d§r ångström", (long) (radius * 2)));

                data.add(String.format("Optimal transfer speed: §b%,.0f§r EU/t", mLink.mWormholeEnergy / 20));
            }
        }

        for (int i = 0; i < MAX_HATCHES; i++) {
            if (!HATCH_MASK[i]) continue;

            var inputHatch = mSendHatches[i];
            var outputHatch = mReceiveHatches[i];

            // spotless:off
            if(inputHatch != null) {
                data.add(String.format(
                    "%s hatch (%,dA/t %s) transferred §b%,d§f EU (equivalent to %,dA/t) with an efficiency of %.3f%% in the last second",
                    HATCH_NAMES[i],
                    inputHatch.Amperes,
                    VN[inputHatch.mTier],
                    mLink != null ? mLink.mSendAmounts[i] : 0,
                    mLink != null ? mLink.mSendAmounts[i] / 20 / V[inputHatch.mTier] : 0,
                    mLink != null && mLink.mSendAmounts[i] > 0 ? ((double)mLink.mReceiveAmounts[OPPOSITES[i]]) / ((double)mLink.mSendAmounts[i]) * 100 : 0
                ));
            } else if(outputHatch != null) {
                data.add(String.format(
                    "%s hatch (%,dA/t %s) received §b%,d§f EU (equivalent to %,dA/t) with an efficiency of %.3f%% in the last second",
                    HATCH_NAMES[i],
                    outputHatch.Amperes,
                    VN[outputHatch.mTier],
                    mLink != null ? mLink.mReceiveAmounts[i] : 0,
                    mLink != null ? mLink.mReceiveAmounts[i] / 20 / V[outputHatch.mTier] : 0,
                    mLink != null && mLink.mSendAmounts[OPPOSITES[i]] > 0 ? ((double)mLink.mReceiveAmounts[i]) / ((double)mLink.mSendAmounts[OPPOSITES[i]]) * 100 : 0
                ));
            } else {
                data.add(String.format("%s hatch is not present", HATCH_NAMES[i]));
            }
            // spotless:on
        }

        data.add("-----------------------");

        return data.toArray(new String[data.size()]);
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widgets(TextWidget.dynamicString(() -> {
            if (mLink == null) {
                return String.format("§7Missing Entangled Singularity§f");
            }

            if (!mLink.isFormed()) {
                return String.format("§7Wormhole status: §cNo destination§f");
            }

            if (mLink.mWormholeEnergy > 0 && !mLink.isActive()) {
                return String.format("§7Wormhole status: §6Decaying§f");
            }

            if (mLink.mWormholeEnergy > 0) {
                return String.format("§7Wormhole status: §bActive§f");
            }

            return String.format("§7Wormhole status: Inactive§f");
        }),

            TextWidget.dynamicString(() -> {
                if (mLink == null) {
                    return "";
                }

                // LV power = 1 angstrom in diameter
                double radius = Math.sqrt(mLink.mWormholeEnergy / 20.0 / 32.0);

                return String.format("§7Wormhole diameter: §b%,d§7 Å§f", (long) (radius * 2));
            })
                .setEnabled(w -> mWormholeEnergy_UI > 0),

            TextWidget.dynamicString(() -> {
                if (mLink == null) {
                    return "";
                }

                if (mLink.mWormholeEnergy >= 1e10) {
                    return String.format("§7Max I/O per hatch: §b%3.3e§7 EU/t§f", mLink.mWormholeEnergy / 20);
                } else {
                    return String.format("§7Max I/O per hatch: §b%,d§7 EU/t§f", (long) (mLink.mWormholeEnergy / 20));
                }
            })
                .setEnabled(w -> mWormholeEnergy_UI > 0),

            new FakeSyncWidget.DoubleSyncer(
                () -> mLink != null ? mLink.mWormholeEnergy : 0,
                val -> mWormholeEnergy_UI = val));
    }

    // #endregion

}
