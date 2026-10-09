package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
public class MTEIndustrialAlloySmelter extends GTPPMultiBlockBase<MTEIndustrialAlloySmelter>
    implements ISurvivalConstructable {

    /** Coil heat needed for each perfect overclock */
    private static final int HEAT_PER_PERFECT_OVERCLOCK = 900;

    public static int CASING_TEXTURE_ID;
    private HeatingCoilLevel mHeatingCapacity;
    private int mLevel = 0;
    private int mCasing;
    private static IStructureDefinition<MTEIndustrialAlloySmelter> STRUCTURE_DEFINITION = null;

    public MTEIndustrialAlloySmelter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 1);
    }

    public MTEIndustrialAlloySmelter(String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 1);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialAlloySmelter(this.mName);
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE_GLOW;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_GLOW;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.alloySmelterRecipes;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialAlloySmelter;
    }

    @Override
    public String getMachineType() {
        return StatCollector.translateToLocal("gt.mbtt.machine_type.alloy_smelter");
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(getMachineType())
            .addMarkdown(
                new ResourceLocation("gregtech", "industrial-alloy-smelter"),
                ImmutableMap.of("heat_per_perfect_oc", formatNumber(HEAT_PER_PERFECT_OVERCLOCK)))
            .addDynamicSpeedBonusInfo(0.05f, TooltipTier.COIL)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 5, 3, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("16", StatCollector.translateToLocal("GT5U.structure.heating_coil"), true)
            .addCasing("5-12", Casings.InconelReinforcedCasing.getLocalizedName(), false)
            .addCasing("8", Casings.IntegralEncasementEV.getLocalizedName(), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMufflerHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addOutputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addAir(StatCollector.translateToLocal("gt.mbtt.structure.interior"))
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialAlloySmelter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialAlloySmelter>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "VVV", "V-V", "VVV" },
                            { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialAlloySmelter.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(CASING_TEXTURE_ID)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 1))))
                .addElement(
                    'H',
                    GTStructureChannels.HEATING_COIL.use(
                        activeCoils(
                            ofCoil(MTEIndustrialAlloySmelter::setCoilLevel, MTEIndustrialAlloySmelter::getCoilLevel))))
                .addElement('V', ofBlock(ModBlocks.blockCasingsTieredGTPP, 4))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mCasing = 0;
        mLevel = 0;
        setCoilLevel(HeatingCoilLevel.None);
        if (!checkPiece(mName, 1, 4, 0, errors)) return;
        if (getCoilLevel() == HeatingCoilLevel.None) {
            errors.add(StructureErrorRegistry.COIL_LEVEL_NOT_ENOUGH);
        }
        checkCasingMin(errors, mCasing, 5);
        mLevel = getCoilLevel().getTier() + 1;
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (this.mLevel * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setDurationModifier(100.0 / (100 + 5 * mLevel))
                    .setHeatOC(true)
                    .setRecipeHeat(0)
                    // Scale coil heat so that every HEAT_PER_PERFECT_OVERCLOCK of it counts as one heat OC
                    .setMachineHeat(
                        (int) (getCoilLevel().getHeat() * OverclockCalculator.HEAT_OVERCLOCK_THRESHOLD
                            / HEAT_PER_PERFECT_OVERCLOCK));
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    public HeatingCoilLevel getCoilLevel() {
        return mHeatingCapacity;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mHeatingCapacity = aCoilLevel;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    public double getSpeedBonus() {
        return 1.0D / (1.0D + 0.05D * mLevel);
    }

    @Override
    public void getExtraWailaNBT(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setDouble("speedBonus", getSpeedBonus());
    }

    private static final DecimalFormat dfNone = new DecimalFormat("#");

    @Override
    public void getExtraWailaBody(ItemStack itemStack, List<String> list, NBTTagCompound tag,
        IWailaDataAccessor accessor, IWailaConfigHandler config) {
        list.add(
            StatCollector.translateToLocal("GT5U.multiblock.speed") + ": "
                + EnumChatFormatting.WHITE
                + dfNone.format(Math.max(0, 100 / tag.getFloat("speedBonus")))
                + "%");
    }
}
