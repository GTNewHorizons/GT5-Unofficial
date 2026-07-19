package gtnhintergalactic.tile.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gtnhintergalactic.recipe.GasSiphonRecipes.calculateEUt;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IChunkLoader;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gtnhintergalactic.client.IGTextures;
import gtnhintergalactic.client.TooltipUtil;
import gtnhintergalactic.recipe.GasSiphonRecipes;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;

/**
 * Gas Siphon multiblock. Extracts gas from gas planets when placed on a space station in their orbit.
 *
 * @author glowredman
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEPlanetaryGasSiphon extends MTEExtendedPowerMultiBlockBase<MTEPlanetaryGasSiphon>
    implements IChunkLoader, ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 6;
    private static final int OFFSET_Y = 7;
    private static final int OFFSET_Z = 5;
    private static final double SPEED_PER_COIL = 0.10;
    private static final double LOG4 = Math.log10(4);

    private HeatingCoilLevel coilLevel;
    private int casingAmount;

    private static final IStructureDefinition<MTEPlanetaryGasSiphon> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPlanetaryGasSiphon>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "             ", "             ", "             ", "             ", "             ",
                "             ", "             ", "      C      ", "      C      ", "   CCCACCC   ", "     CAC     ",
                "     CAC     ", "      C      ", "      C      ", "             ", "             ", "             ",
                "             ", "             ", "             ", "             ", "             ", "             " },
                { "             ", "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "  C   B   C  ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " },
                { "    CCCCC    ", "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", " C    B    C ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " },
                { "   C  B  C   ", "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "C     B     C", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " },
                { "  C   B   C  ", "             ", "             ", "             ", "             ", "             ",
                    "    B   B    ", "    B   B    ", "    B   B    ", "C   B B B   C", "    B   B    ",
                    "    B   B    ", "    B   B    ", "    B   B    ", "    B   B    ", "    B   B    ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " },
                { "  C   B   C  ", "      C      ", "      C      ", "     CCC     ", "     CCC     ", "     CCC     ",
                    "     CCC     ", "     C~C     ", "     CCC     ", "C    CCC    C", "C    BCB    C",
                    "C    BCB    C", "     BCB     ", "      C      ", "      C      ", "      C      ",
                    "      C      ", "      B      ", "      B      ", "      B      ", "             ",
                    "             ", "             " },
                { "  CBBBBBBBC  ", "     CCC     ", "     CCC     ", "     C C     ", "     C C     ", "     C C     ",
                    "     C C     ", "C    C C    C", "C    C C    C", "ABBBBC CBBBBA", "A    CCC    A",
                    "A    CCC    A", "C    CCC    C", "C    CCC    C", "     CCC     ", "     CCC     ",
                    "     CCC     ", "     BDB     ", "     BDB     ", "     BDB     ", "      D      ",
                    "      D      ", "      D      " },
                { "  C   B   C  ", "      C      ", "      C      ", "     CCC     ", "     CCC     ", "     CCC     ",
                    "     CCC     ", "     CCC     ", "     CCC     ", "C    CCC    C", "C    BCB    C",
                    "C    BCB    C", "     BCB     ", "      C      ", "      C      ", "      C      ",
                    "      C      ", "      B      ", "      B      ", "      B      ", "             ",
                    "             ", "             " },
                { "  C   B   C  ", "             ", "             ", "             ", "             ", "             ",
                    "    B   B    ", "    B   B    ", "    B   B    ", "C   B B B   C", "    B   B    ",
                    "    B   B    ", "    B   B    ", "    B   B    ", "    B   B    ", "    B   B    ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " },
                { "   C  B  C   ", "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "C     B     C", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " },
                { "    CCCCC    ", "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", " C    B    C ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " },
                { "             ", "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "  C   B   C  ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " },
                { "             ", "             ", "             ", "             ", "             ", "             ",
                    "             ", "      C      ", "      C      ", "   CCCACCC   ", "     CAC     ",
                    "     CAC     ", "      C      ", "      C      ", "             ", "             ",
                    "             ", "             ", "             ", "             ", "             ",
                    "             ", "             " } })
        .addElement('B', ofFrame(Materials.TungstenSteel))
        .addElement('D', ofBlock(WerkstoffLoader.BWBlockCasingsAdvanced, 88))
        .addElement(
            'C',
            ofChain(
                buildHatchAdder(MTEPlanetaryGasSiphon.class).atLeast(Maintenance, InputBus, OutputHatch, Energy)
                    .hint(1)
                    .casingIndex(IGTextures.CASING_INDEX_SIPHON)
                    .build(),
                onElementPass(x -> ++x.casingAmount, ofBlock(GregTechAPI.sBlockCasingsSiphon, 0))))
        .addElement(
            'A',
            GTStructureChannels.HEATING_COIL
                .use(activeCoils(ofCoil(MTEPlanetaryGasSiphon::setCoilLevel, MTEPlanetaryGasSiphon::getCoilLevel))))
        .build();

    public MTEPlanetaryGasSiphon(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public MTEPlanetaryGasSiphon(String name) {
        super(name);
    }

    private int depth;
    private FluidStack fluid = new FluidStack(FluidRegistry.WATER, 0) {

        @Override
        public String getLocalizedName() {
            return "None";
        }
    };
    private boolean mChunkLoadingEnabled = true;
    private ChunkCoordIntPair mCurrentChunk = null;
    private boolean mWorkChunkNeedsReload = true;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stack,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tileEntity) {
        return new MTEPlanetaryGasSiphon(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gt.blockmachines.multimachine.ig.siphon.type"));
        if (TooltipUtil.siphonLoreText != null) {
            tt.addInfo(EnumChatFormatting.ITALIC + TooltipUtil.siphonLoreText);
        }
        tt.addInfo(
            StatCollector.translateToLocalFormatted(
                "gt.blockmachines.multimachine.ig.siphon.desc0",
                (int) (SPEED_PER_COIL * 100)));
        tt.addInfo(StatCollector.translateToLocal("gt.blockmachines.multimachine.ig.siphon.desc1"))
            .addInfo(StatCollector.translateToLocal("gt.blockmachines.multimachine.ig.siphon.desc2"))
            .addInfo(StatCollector.translateToLocal("gt.blockmachines.multimachine.ig.siphon.desc3"))
            .addInfo(StatCollector.translateToLocal("gt.blockmachines.multimachine.ig.siphon.desc4"))
            .addInfo(StatCollector.translateToLocal("gt.blockmachines.multimachine.ig.siphon.desc5"))
            .beginStructureBlock(13, 13, 23, false)
            .addController(StatCollector.translateToLocal("ig.siphon.structure.ControllerPos"))
            .addCasing("184", StatCollector.translateToLocal("ig.siphon.structure.SiphonCasing"), false)
            .addCasing("93", StatCollector.translateToLocal("ig.siphon.structure.FrameTungstensteel"), false)
            .addCasing("12", "Heating Coil", true)
            .addCasing("6", StatCollector.translateToLocal("ig.siphon.structure.ReboltedRhodiumPalladiumCasing"), false)
            .addEnergyHatch("1", StatCollector.translateToLocal("ig.siphon.structure.AnySiphonCasing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("ig.siphon.structure.AnySiphonCasing"), 1)
            .addInputBus("1", StatCollector.translateToLocal("ig.siphon.structure.AnySiphonCasing"), 1)
            .addOutputHatch("1", StatCollector.translateToLocal("ig.siphon.structure.AnySiphonCasing"), 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .addStructureAuthors(EnumChatFormatting.GOLD + "hugetrust")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEPlanetaryGasSiphon> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ChunkCoordIntPair getActiveChunk() {
        return mCurrentChunk;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstone) {
        if (side == facing) {
            if (active) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_SIPHON), TextureFactory.builder()
                    .addIcon(IGTextures.SIPHON_OVERLAY_FRONT)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(IGTextures.SIPHON_OVERLAY_FRONT_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_SIPHON),
                TextureFactory.builder()
                    .addIcon(IGTextures.SIPHON_OVERLAY_FRONT)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(IGTextures.SIPHON_OVERLAY_FRONT_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_SIPHON) };
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        depth = 0;

        if (mInputBusses.isEmpty() || !mInputBusses.get(0)
            .isValid()) {
            resetMachine();
            return SimpleCheckRecipeResult.ofFailure("no_mining_pipe");
        }

        if (!(this.getBaseMetaTileEntity()
            .getWorld().provider instanceof IOrbitDimension provider)) {
            resetMachine();
            return SimpleCheckRecipeResult.ofFailure("no_space_station");
        }

        Map<Integer, FluidStack> planetRecipes = GasSiphonRecipes.RECIPES.get(provider.getPlanetToOrbit()).depths;
        if (planetRecipes == null) {
            resetMachine();
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        MTEHatchInputBus bus = mInputBusses.get(0);
        int numPipes = 0;

        for (int i = 0; i < mInventory.length; i++) {
            ItemStack stack = mInventory[i];
            if (stack == null) continue;
            if (stack.getItem() == ItemList.Circuit_Integrated.getItem()) {
                depth = stack.getItemDamage();
                continue;
            }
            if (Objects.equals(
                stack.getItem(),
                GTModHandler.getIC2Item("miningPipe", 0)
                    .getItem())) {
                numPipes += stack.stackSize;
            }
        }

        for (int i = 0; i < bus.getBaseMetaTileEntity()
            .getSizeInventory(); i++) {
            ItemStack stack = bus.getBaseMetaTileEntity()
                .getStackInSlot(i);
            if (stack == null) continue;
            if (stack.getItem() == ItemList.Circuit_Integrated.getItem()) {
                depth = stack.getItemDamage();
                continue;
            }
            if (Objects.equals(
                stack.getItem(),
                GTModHandler.getIC2Item("miningPipe", 0)
                    .getItem())) {
                numPipes += stack.stackSize;
            }
        }

        if (depth == 0) {
            resetMachine();
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (numPipes < depth * 64) {
            resetMachine();
            return SimpleCheckRecipeResult.ofFailure("no_mining_pipe");
        }

        FluidStack recipeFluid = planetRecipes.get(depth);
        if (recipeFluid == null) {
            resetMachine();
            return SimpleCheckRecipeResult.ofFailure("invalid_depth");
        }

        if (!canOutputAll(new FluidStack[] { recipeFluid })) {
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }

        int recipeEUt = calculateEUt(
            depth,
            provider.getCelestialBody()
                .getTierRequirement());
        int ocLevel = MathHelper.floor_double(Math.log10((double) this.getMaxInputVoltage() / recipeEUt) / LOG4);

        if (ocLevel < 0) {
            resetMachine();
            return CheckRecipeResultRegistry.insufficientPower(recipeEUt);
        }

        fluid = recipeFluid.copy();

        if (ocLevel == 0) {
            lEUt = -recipeEUt;
        } else {
            ocLevel--;
            fluid.amount *= 2 << ocLevel;
            lEUt = -recipeEUt * (4 << (2 * ocLevel));
        }

        int processTime = (int) (20 * speedBoost(getCoilTier()));

        int batchMultiplierMax = 1;
        if (isBatchModeEnabled()) {
            batchMultiplierMax = getMaxBatchSize() / processTime;
        }

        fluid.amount = (int) (fluid.amount * batchMultiplierMax);

        mOutputFluids = new FluidStack[] { fluid };
        mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processTime * batchMultiplierMax;
        return SimpleCheckRecipeResult.ofSuccess("siphoning");
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkHatchExact(errors, HatchElement.InputBus, 1);
        checkHatchExact(errors, HatchElement.OutputHatch, 1);
        checkHatchExact(errors, HatchElement.Energy, 1);
        checkHatchMax(errors, HatchElement.InputHatch, 0);
        checkHatchMax(errors, HatchElement.OutputBus, 0);
        checkHasMaintenanceHatch(errors);
        checkCasingMin(errors, casingAmount, 175);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("chunkLoadingEnabled", mChunkLoadingEnabled);
        aNBT.setBoolean("isChunkloading", mCurrentChunk != null);
        if (mCurrentChunk != null) {
            aNBT.setInteger("loadedChunkXPos", mCurrentChunk.chunkXPos);
            aNBT.setInteger("loadedChunkZPos", mCurrentChunk.chunkZPos);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("chunkLoadingEnabled")) mChunkLoadingEnabled = aNBT.getBoolean("chunkLoadingEnabled");
        if (aNBT.getBoolean("isChunkloading")) {
            mCurrentChunk = new ChunkCoordIntPair(
                aNBT.getInteger("loadedChunkXPos"),
                aNBT.getInteger("loadedChunkZPos"));
        }
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer player,
        float x, float y, float z, ItemStack aTool) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            mChunkLoadingEnabled = !mChunkLoadingEnabled;
            GTUtility.sendChatTrans(
                player,
                mChunkLoadingEnabled ? "GT5U.chat.driller_base.chunk_loading.enable"
                    : "GT5U.chat.driller_base.chunk_loading.disabled");
            return true;
        }
        return super.onSolderingToolRightClick(side, wrenchingSide, player, x, y, z, aTool);
    }

    private void resetMachine() {
        lEUt = 0;
        mEfficiency = 0;
    }

    @Override
    public void onRemoval() {
        if (mChunkLoadingEnabled) GTChunkManager.releaseTicket((TileEntity) getBaseMetaTileEntity());
        super.onRemoval();
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        super.onPostTick(baseMetaTileEntity, tick);
        if (baseMetaTileEntity.isServerSide() && mCurrentChunk != null
            && !mWorkChunkNeedsReload
            && !baseMetaTileEntity.isAllowedToWork()) {
            GTChunkManager.releaseTicket((TileEntity) baseMetaTileEntity);
            mWorkChunkNeedsReload = true;
        }
    }

    @Override
    public String[] getInfoData() {
        return new String[] { "ig.infodata.hdr.operational_data",
            IGregTechDeviceInformation.encode("ig.infodata.gas_siphon.depth.fmt", depth),
            IGregTechDeviceInformation.encode(
                "ig.infodata.gas_siphon.fluid.fmt",
                fluid.amount,
                IGregTechDeviceInformation.translatable(fluid.getUnlocalizedName())),
            IGregTechDeviceInformation.encode("ig.infodata.gas_siphon.eu_required.fmt", formatNumber(-lEUt)),
            getRepairStatus() == getIdealStatus() ? "ig.infodata.maintenance.ok" : "ig.infodata.maintenance.problems",
            "---------------------------------------------" };
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    public int getCoilTier() {
        return this.coilLevel == null ? 0 : (int) this.coilLevel.getTier() + 1;
    }

    public HeatingCoilLevel getCoilLevel() {
        return this.coilLevel;
    }

    public float speedBoost(int coilTier) {
        return (float) 1 / (2 + (float) SPEED_PER_COIL * Math.max(1, coilTier));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.coilLevel") + ": "
                + EnumChatFormatting.WHITE
                + Math.max(0, tag.getInteger("coilTier")));
        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.speed") + ": "
                + EnumChatFormatting.WHITE
                + formatNumber(Math.max(0, 100 / speedBoost(tag.getInteger("coilTier"))))
                + "%");
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("coilTier", getCoilTier());
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}
