package gregtech.common.hydrodam;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/// A dam machine (pump or turbine). Both share a 3x3x3 steel structure: the controller at the front, a gearbox behind
/// it, and a pipe casing at the back whose back face must touch the dam's water. Hatches replace the solid steel
/// casings around them.
///
/// Machines run in one-tick cycles. Each cycle moves water between the dam and a hatch, and the energy involved is
/// expressed through {@link #lEUt} and {@link #mEfficiency}, so the base class drains or produces it.
public abstract class MTEHydroDamMachine<T extends MTEHydroDamMachine<T>> extends MTEExtendedPowerMultiBlockBase<T>
    implements ISurvivalConstructable {

    /// The efficiency lost for each maintenance problem.
    public static final double LOSS_PER_MAINTENANCE_PROBLEM = 0.025;

    private static final String STRUCTURE_PIECE_MAIN = "main";

    // spotless:off
    private static final String[][] SHAPE = new String[][] {
        { " A ", "A~A", " A " },
        { " A ", "ABA", " A " },
        { " A ", "ACA", " A " },
    };
    // spotless:on

    private static final int OFFSET_HORIZONTAL = 1;
    private static final int OFFSET_VERTICAL = 1;
    private static final int OFFSET_DEPTH = 0;

    /// How far behind the controller the dam's water must be: right behind the pipe casing.
    private static final int WATER_DEPTH = 3;

    /// How often an idle machine checks whether it can run again, in ticks. The dam filling or draining doesn't trigger
    /// a
    /// recipe check by itself.
    private static final int IDLE_RECHECK_INTERVAL = 20;

    protected static final CheckRecipeResult NO_DAM_WATER = SimpleCheckRecipeResult.ofFailure("hydrodam_no_dam_water");
    protected static final CheckRecipeResult NO_ENERGY = SimpleCheckRecipeResult.ofFailure("hydrodam_no_energy");

    /// The fraction of an EU not yet settled. A cycle only moves whole EU, so the remainder carries over to the next
    /// cycle: unpaid output for a generator, prepaid input for a consumer. Always in [0, 1).
    private double energyRemainder;

    /// The litres moved by the current cycle.
    private long cycleLitres;

    protected MTEHydroDamMachine(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    protected MTEHydroDamMachine(String name) {
        super(name);
    }

    /// Builds the structure shared by every dam machine. The solid steel casings accept the given hatches.
    @SafeVarargs
    @NotNull
    protected static <T extends MTEHydroDamMachine<T>> IStructureDefinition<T> createStructureDefinition(
        @NotNull Class<T> type, IHatchElement<? super T>... hatches) {
        return StructureDefinition.<T>builder()
            .addShape(STRUCTURE_PIECE_MAIN, SHAPE)
            .addElement(
                'A',
                buildHatchAdder(type).atLeast(hatches)
                    .casingIndex(Casings.SolidSteelMachineCasing.textureId)
                    .hint(1)
                    .buildAndChain(Casings.SolidSteelMachineCasing.asElement()))
            .addElement('B', Casings.SteelGearBoxCasing.asElement())
            .addElement('C', Casings.SteelPipeCasing.asElement())
            .build();
    }

    /// Reports missing or extra hatches. Assumption: called after the structure was checked.
    protected abstract void checkDamHatches(@NotNull List<StructureError> errors);

    /// The highest efficiency this machine can run at, in [0, 1].
    protected abstract double getMaxDamEfficiency();

    protected abstract IIconContainer getFrontOverlay(boolean active);

    /// The glowing layer drawn over {@link #getFrontOverlay(boolean)}.
    protected abstract IIconContainer getFrontGlowOverlay(boolean active);

    /// The lang key for the WAILA line showing {@link #getLitresPerTick()}. Formatted with the litres.
    public abstract String getThroughputLangKey();

    /// The litres moved per tick while running, or 0 while idle.
    public long getLitresPerTick() {
        return getBaseMetaTileEntity().isActive() ? cycleLitres : 0;
    }

    @Override
    public void checkMachine(IGregTechTileEntity baseMetaTileEntity, ItemStack stack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_HORIZONTAL, OFFSET_VERTICAL, OFFSET_DEPTH, errors)) return;

        checkDamHatches(errors);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_HORIZONTAL, OFFSET_VERTICAL, OFFSET_DEPTH);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;

        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_HORIZONTAL,
            OFFSET_VERTICAL,
            OFFSET_DEPTH,
            elementBudget,
            env,
            false,
            true);
    }

    /// Adds the structure and hatch lines shared by every dam machine to a tooltip.
    protected static MultiblockTooltipBuilder addStructureInfo(MultiblockTooltipBuilder tt) {
        String casing = Casings.SolidSteelMachineCasing.getLocalizedName();

        return tt.beginStructureBlock(3, 3, 3, false)
            .addController("Front center")
            .addCasing("9+", casing, false)
            .addCasing("1", Casings.SteelGearBoxCasing.getLocalizedName(), false)
            .addCasing("1", Casings.SteelPipeCasing.getLocalizedName(), false)
            .addMaintenanceHatch("Exactly one, any " + casing, 1);
    }

    /// The efficiency after maintenance losses, in [0, {@link #getMaxDamEfficiency()}].
    protected double getDamEfficiency() {
        int problems = getIdealStatus() - getRepairStatus();

        return Math.max(0, Math.min(getMaxDamEfficiency(), 1 - LOSS_PER_MAINTENANCE_PROBLEM * problems));
    }

    /// Sets up a one-tick cycle that produces energy. {@link #lEUt} is set to the whole EU produced this tick.
    ///
    /// @param energy the exact EU produced, after efficiency
    protected void startGeneratingCycle(long litres, double energy) {
        double total = energy + energyRemainder;
        long whole = (long) Math.floor(total);

        energyRemainder = total - whole;

        startCycle(litres, whole);
    }

    /// Sets up a one-tick cycle that consumes energy. {@link #lEUt} is set to minus the whole EU consumed this tick.
    ///
    /// @param energy the exact EU consumed, after efficiency
    protected void startConsumingCycle(long litres, double energy) {
        double total = energy - energyRemainder;
        long whole = (long) Math.ceil(total);

        energyRemainder = whole - total;

        startCycle(litres, -whole);
    }

    /// The whole EU this cycle can consume without overrunning the given budget, given the prepaid remainder.
    protected double getConsumableEnergy(long budget) {
        return budget + energyRemainder;
    }

    private void startCycle(long litres, long euPerTick) {
        cycleLitres = litres;
        lEUt = euPerTick;
        mEfficiency = (int) Math.round(getDamEfficiency() * 10000);
        mEfficiencyIncrease = 0;
        mMaxProgresstime = 1;
    }

    /// Moves exactly {@link #lEUt}. Efficiency was already applied when the cycle started.
    @Override
    public boolean onRunningTick(ItemStack stack) {
        if (lEUt > 0) {
            addEnergyOutput(lEUt);
        } else if (lEUt < 0 && !drainEnergyInput(-lEUt)) {
            stopMachine(ShutDownReasonRegistry.POWER_LOSS);
            return false;
        }

        return true;
    }

    @Override
    protected long getActualEnergyUsage() {
        return -lEUt;
    }

    /// Reports {@link #lEUt} as is. The base class would apply the efficiency to it a second time for generators.
    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        if (getBaseMetaTileEntity().isActive()) tag.setLong("energyUsage", -lEUt);

        tag.setLong("damLitresPerTick", getLitresPerTick());

        DamVolumes volumes = DamVolumes.get(world);
        WaterVolume volume = getVolumeBehind(volumes);

        tag.setBoolean("hasDam", volume != null);

        if (volume != null) {
            tag.setDouble("damLitres", volume.getLitres());
            tag.setDouble("damNetLitresPerTick", volumes.getNetLitresPerTick(volume));
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("isActive")) {
            currentTip.add(
                StatCollector
                    .translateToLocalFormatted(getThroughputLangKey(), formatFluid(tag.getLong("damLitresPerTick"))));
        }

        if (tag.getBoolean("hasDam")) {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.hydrodam.reserve",
                    formatFluid((long) tag.getDouble("damLitres"))));
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.hydrodam.net_change",
                    formatNetChange(tag.getDouble("damNetLitresPerTick"))));
        }
    }

    /// Formats a net change with one decimal and a sign, green when filling and red when draining.
    private static String formatNetChange(double litresPerTick) {
        double rounded = Math.round(litresPerTick * 10) / 10.0;

        if (rounded > 0) return EnumChatFormatting.GREEN + "+" + formatFluid(rounded) + EnumChatFormatting.RESET;
        if (rounded < 0) return EnumChatFormatting.RED + formatFluid(rounded) + EnumChatFormatting.RESET;

        return formatFluid(0);
    }

    @Override
    protected boolean shouldCheckRecipeThisTick(long tick) {
        return super.shouldCheckRecipeThisTick(tick) || mTotalRunTime % IDLE_RECHECK_INTERVAL == 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        super.saveNBTData(tag);
        tag.setDouble("energyRemainder", energyRemainder);
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        super.loadNBTData(tag);
        energyRemainder = tag.getDouble("energyRemainder");
    }

    /// The position right behind the pipe casing, where the dam's water must be.
    @NotNull
    protected ChunkCoordinates getWaterPosition() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        ForgeDirection back = base.getFrontFacing()
            .getOpposite();

        return new ChunkCoordinates(
            base.getXCoord() + back.offsetX * WATER_DEPTH,
            base.getYCoord() + back.offsetY * WATER_DEPTH,
            base.getZCoord() + back.offsetZ * WATER_DEPTH);
    }

    /// The volume whose water is right behind the pipe casing, if any.
    @Nullable
    protected WaterVolume getVolumeBehind(@NotNull DamVolumes volumes) {
        ChunkCoordinates water = getWaterPosition();

        return volumes.getVolumeAt(water.posX, water.posY, water.posZ);
    }

    @Override
    public int getMaxEfficiency(ItemStack stack) {
        return 10000;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        ITexture casing = Casings.SolidSteelMachineCasing.getCasingTexture();

        if (side != facing) return new ITexture[] { casing };

        return new ITexture[] { casing, TextureFactory.builder()
            .addIcon(getFrontOverlay(active))
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(getFrontGlowOverlay(active))
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack stack) {
        return true;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }
}
