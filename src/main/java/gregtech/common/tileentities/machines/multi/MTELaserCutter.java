package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.MultiAmpEnergy;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ENGRAVER_ACTIVE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.structure.error.TranslatableText;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.MTELaserCutterGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.render.RenderingTileEntityLaser;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class MTELaserCutter extends MTEExtendedPowerMultiBlockBase<MTELaserCutter> implements ISurvivalConstructable {

    private static final String MAIN = "main";
    private static final int OFFSET_X = 3;
    private static final int OFFSET_Y = 5;
    private static final int OFFSET_Z = 0;
    private static final IStructureDefinition<MTELaserCutter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELaserCutter>builder()
        .addShape(
            MAIN,
            transpose(
                new String[][] { { "       ", "   B   ", "   B   ", "   B   ", "       " },
                    { "   B   ", "  CCC  ", "  CEC  ", "  CCC  ", "   B   " },
                    { "  CBC  ", "  C C  ", "   A   ", "  C C  ", "  CBC  " },
                    { "  CBC  ", "       ", "   A   ", "       ", "  CBC  " },
                    { "  CBC  ", "       ", "   A   ", "       ", "  CBC  " },
                    { "  B~B  ", "BBBBBBB", "BBBDBBB", "BBBBBBB", "  BBB  " } }))
        .addElement('A', chainAllGlasses(-1, (machine, tier) -> machine.glassTier = tier, machine -> machine.glassTier))
        .addElement(
            'B',
            buildHatchAdder(MTELaserCutter.class)
                .atLeast(InputBus, OutputBus, InputHatch, Maintenance, Energy.or(ExoticEnergy), MultiAmpEnergy)
                .casingIndex(Casings.LaserContainmentCasing.textureId)
                .hint(1)
                .buildAndChain(Casings.LaserContainmentCasing.asElement()))
        .addElement('C', ofFrame(Materials.TungstenSteel))
        .addElement('D', ofBlock(GregTechAPI.sLaserRender, 0))
        .addElement(
            'E',
            buildHatchAdder(MTELaserCutter.class).anyOf(gregtech.api.enums.HatchElement.LaserSource)
                .adder(MTELaserCutter::addLaserSource)
                .casingIndex(Casings.LaserContainmentCasing.textureId)
                .hint(2)
                .build())
        .build();

    private MTEHatchDynamoTunnel laserSource;
    private RenderingTileEntityLaser renderer;
    private int laserAmperage;
    private int glassTier = -1;
    private Fluid coolingFluid;

    public MTELaserCutter(int id, String name, String regionalName) {
        super(id, name, regionalName);
    }

    public MTELaserCutter(String name) {
        super(name);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new MTELaserCutter(mName);
    }

    @Override
    public IStructureDefinition<MTELaserCutter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.laserCutterRecipes;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTELaserCutterGui(this);
    }

    private boolean addLaserSource(IGregTechTileEntity tile, int casingTexture) {
        if (tile == null || !(tile.getMetaTileEntity() instanceof MTEHatchDynamoTunnel source)) return false;
        laserSource = source;
        source.updateTexture(casingTexture);
        switch (getRotation()) {
            case NORMAL -> source.getBaseMetaTileEntity()
                .setFrontFacing(ForgeDirection.DOWN);
            case UPSIDE_DOWN -> source.getBaseMetaTileEntity()
                .setFrontFacing(ForgeDirection.UP);
            case CLOCKWISE -> source.getBaseMetaTileEntity()
                .setFrontFacing(getDirection().getRotation(ForgeDirection.UP));
            default -> source.getBaseMetaTileEntity()
                .setFrontFacing(getDirection().getRotation(ForgeDirection.DOWN));
        }
        laserAmperage = (int) source.maxAmperesOut();
        return laserAmperage > 0;
    }

    @Override
    public void checkMachine(IGregTechTileEntity base, ItemStack stack, List<StructureError> errors) {
        setLaserAnimation(false);
        renderer = null;
        laserSource = null;
        laserAmperage = 0;
        glassTier = -1;
        coolingFluid = null;
        if (!checkPiece(MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkHasAnyEnergy(errors);
        checkOneMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);
        if (laserSource == null) {
            errors.add(StructureErrors.of("GT5U.MBTT.LaserSourceHatch"));
            return;
        }
        if (laserSource.mTier < VoltageIndex.IV) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.laser_cutter_tier_below_iv"));
        }
        if (glassTier < laserSource.mTier) errors.add(StructureErrors.glassTierNotEnough(laserSource.mTier));
        for (var energyHatch : getExoticAndNormalEnergyHatchList()) {
            if (energyHatch.getTierForStructure() != laserSource.mTier) {
                errors.add(
                    StructureErrors.of(
                        "GT5U.gui.text.structure_error.laser_cutter_energy_tier_mismatch",
                        TranslatableText.literal(GTValues.VN[laserSource.mTier]),
                        TranslatableText.literal(GTValues.VN[energyHatch.getTierForStructure()])));
                return;
            }
        }
        coolingFluid = getCoolingFluid(laserSource.mTier);
        if (!errors.isEmpty()) return;
        findLaserRenderer(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord());
    }

    private Fluid getCoolingFluid(int tier) {
        if (tier <= VoltageIndex.LuV) {
            return GTModHandler.getIC2Coolant(1)
                .getFluid();
        }
        if (tier <= VoltageIndex.UV) {
            return Materials.SuperCoolant.getFluid(1)
                .getFluid();
        }
        return bartworks.system.material.WerkstoffLoader.LiquidHelium.getFluidOrGas(1)
            .getFluid();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                setLaserAnimation(true);
                return super.onRecipeStart(recipe);
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (laserSource == null) return CheckRecipeResultRegistry.NO_RECIPE;
                if (GTUtility.getTier(recipe.mEUt) > laserSource.mTier) {
                    return SimpleCheckRecipeResult.ofFailure("laser_insufficient");
                }
                int coolingUse = getCoolingUsePerSecond();
                if (coolingFluid == null || !depleteInput(new FluidStack(coolingFluid, coolingUse), true)) {
                    return SimpleCheckRecipeResult.ofFailure("laser_cooling_required");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            protected gregtech.api.util.@NotNull OverclockCalculator createOverclockCalculator(
                @Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setMaxOverclocks(Math.max(0, laserSource.mTier - GTUtility.getTier(recipe.mEUt)));
            }

            @Override
            public ProcessingLogic clear() {
                setLaserAnimation(false);
                return super.clear();
            }
        }.setSpeedBonusSupplier(() -> {
            double speed = calculateLaserSpeed();
            return speed <= 0.0 ? 1.0 : 1.0 / speed;
        });
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        if (coolingFluid == null) return false;
        boolean consumeCooling = (mProgresstime + 1) % 20 == 0 || mProgresstime + 1 >= mMaxProgresstime;
        int amount = consumeCooling ? getCoolingUsePerSecond() : 0;
        if (consumeCooling && !depleteInput(new FluidStack(coolingFluid, amount), true)) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return false;
        }
        if (!super.onRunningTick(stack)) return false;
        if (consumeCooling && !depleteInput(new FluidStack(coolingFluid, amount))) {
            stopMachine(ShutDownReasonRegistry.NONE);
            return false;
        }
        return true;
    }

    private int getCoolingUsePerSecond() {
        double ratio = Math.max(1, laserAmperage) / 256.0;
        double speed = Math.sqrt(ratio);
        double log2 = Math.log(ratio) / Math.log(2.0);
        return Math.max(1, (int) Math.round(100.0 * (1.0 + speed * log2)));
    }

    private double calculateLaserSpeed() {
        return Math.sqrt(Math.max(1, laserAmperage) / 256.0);
    }

    private void findLaserRenderer(World world, int x, int y, int z) {
        ForgeDirection opposite = getDirection().getOpposite();
        x += opposite.offsetX;
        y += opposite.offsetY;
        z += opposite.offsetZ;
        if (world.getTileEntity(x, y, z) instanceof RenderingTileEntityLaser laser) {
            renderer = laser;
            renderer.setRotationFields(getExtendedFacing());
        }
    }

    private void setLaserAnimation(boolean active) {
        if (renderer == null) return;
        if (active) renderer.setColors(1.0F, 0.0F, 0.0F);
        renderer.setShouldRender(active);
    }

    @Override
    public void onBlockDestroyed() {
        setLaserAnimation(false);
        super.onBlockDestroyed();
    }

    public String getCoolingFluidName() {
        return coolingFluid == null ? "Unavailable" : coolingFluid.getLocalizedName(new FluidStack(coolingFluid, 1));
    }

    @Override
    public void construct(ItemStack stack, boolean hintsOnly) {
        buildPiece(MAIN, stack, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(MAIN, stack, OFFSET_X, OFFSET_Y, OFFSET_Z, elementBudget, env, false, true);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType(StatCollector.translateToLocal("gt.recipe.laser_cutter"))
            .beginStructureBlock(7, 6, 5, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addOutputBus("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMiscHatch(
                "1",
                StatCollector.translateToLocal("GT5U.tooltip.structure.laser_source_hatch"),
                "Casing above glass",
                2)
            .addCasing("25+", Casings.LaserContainmentCasing.getLocalizedName(), false)
            .addCasing("24", "Tungstensteel Frame Box", false)
            .addCasing("3", "Any Tiered Glass", true)
            .addCasing("1", "Laser Resistant Plate", false)
            .addSubChannel(gregtech.common.misc.GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, ForgeDirection facing, int color,
        boolean active, boolean redstone) {
        ITexture casing = Textures.BlockIcons.getCasingTextureForId(Casings.LaserContainmentCasing.textureId);
        if (side != facing) return new ITexture[] { casing };
        return new ITexture[] { casing, TextureFactory.builder()
            .addIcon(active ? OVERLAY_FRONT_ENGRAVER_ACTIVE : OVERLAY_FRONT_ENGRAVER)
            .extFacing()
            .build() };
    }
}
