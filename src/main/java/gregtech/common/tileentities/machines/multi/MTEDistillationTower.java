package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.ECHatchElement.Energy;
import static gregtech.api.enums.ECHatchElement.InputBus;
import static gregtech.api.enums.ECHatchElement.InputHatch;
import static gregtech.api.enums.ECHatchElement.Maintenance;
import static gregtech.api.enums.ECHatchElement.OutputBus;
import static gregtech.api.enums.ECHatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.ec.component.multiblock.ILayeredFluidOutputLogicComponent;
import gregtech.api.ec.implementations.MTEMultiBlockECBase;
import gregtech.api.ec.implementations.MultiblockComponentBuilder;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.ec.multiblock.LayeredFluidOutputLogicComponent;
import gregtech.common.misc.GTStructureChannels;

@IMetaTileEntity.SkipGenerateDescription
public class MTEDistillationTower extends MTEMultiBlockECBase<MTEDistillationTower> implements ICasingTextureProvider {

    protected static final int CASING_INDEX = 49;
    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String STRUCTURE_PIECE_LAYER = "layer";
    protected static final String STRUCTURE_PIECE_LAYER_HINT = "layerHint";
    protected static final String STRUCTURE_PIECE_TOP_HINT = "topHint";
    private static final IStructureDefinition<MTEDistillationTower> STRUCTURE_DEFINITION;

    static {
        IHatchElement<MTEDistillationTower> layeredOutputHatch = OutputHatch
            .withCount(MTEDistillationTower::getCurrentLayerOutputHatchCount)
            .withAdder(MTEDistillationTower::addLayerOutputHatch);
        STRUCTURE_DEFINITION = StructureDefinition.<MTEDistillationTower>builder()
            .addShape(STRUCTURE_PIECE_BASE, transpose(new String[][] { { "b~b", "bbb", "bbb" }, }))
            .addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][] { { "lll", "lcl", "lll" }, }))
            .addShape(STRUCTURE_PIECE_LAYER_HINT, transpose(new String[][] { { "lll", "l-l", "lll" }, }))
            .addShape(STRUCTURE_PIECE_TOP_HINT, transpose(new String[][] { { "LLL", "LcL", "LLL" }, }))
            .addChannel(GTStructureChannels.STRUCTURE_HEIGHT.get(), 3, 12)
            .addElement(
                'b',
                ofChain(
                    buildHatchAdder(MTEDistillationTower.class)
                        .atLeast(Energy, OutputBus, InputHatch, InputBus, Maintenance)
                        .casingIndex(CASING_INDEX)
                        .hint(1)
                        .build(),
                    onElementPass(MTEDistillationTower::onCasingFound, ofBlock(GregTechAPI.sBlockCasings4, 1))))
            .addElement(
                'l',
                ofChain(
                    buildHatchAdder(MTEDistillationTower.class).atLeast(layeredOutputHatch)
                        .casingIndex(CASING_INDEX)
                        .hint(2)
                        .disallowOnly(ForgeDirection.UP, ForgeDirection.DOWN)
                        .build(),
                    ofHatchAdder(Energy.adder()::apply, CASING_INDEX, 2),
                    ofHatchAdder(MTEDistillationTower::addLayerOutputHatch, CASING_INDEX, 2),
                    ofHatchAdder(Maintenance.adder()::apply, CASING_INDEX, 2),
                    onElementPass(MTEDistillationTower::onCasingFound, ofBlock(GregTechAPI.sBlockCasings4, 1))))
            // hint element only used in top layer
            .addElement(
                'L',
                buildHatchAdder(MTEDistillationTower.class).atLeast(layeredOutputHatch)
                    .casingIndex(CASING_INDEX)
                    .hint(2)
                    .disallowOnly(ForgeDirection.UP)
                    .buildAndChain(GregTechAPI.sBlockCasings4, 1))
            .addElement(
                'c',
                ofChain(
                    onElementPass(
                        t -> t.onTopLayerFound(false),
                        ofHatchAdder(
                            (t, te, i) -> OutputBus.adder()
                                .apply(t, te, i) || t.addLayerOutputHatch(te, i),
                            CASING_INDEX,
                            3)),
                    onElementPass(
                        t -> t.onTopLayerFound(false),
                        ofHatchAdder(Maintenance.adder()::apply, CASING_INDEX, 3)),
                    onElementPass(t -> t.onTopLayerFound(true), ofBlock(GregTechAPI.sBlockCasings4, 1)),
                    isAir()))
            .build();
    }

    protected int mHeight;
    protected int mCasing;
    protected boolean mTopLayerFound;

    public MTEDistillationTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEDistillationTower(String aName) {
        super(aName);
    }

    @Override
    protected @NotNull MTEDistillationTower createNewMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDistillationTower(this.mName);
    }

    @Override
    protected void configureComponents(@NotNull MultiblockComponentBuilder builder) {
        super.configureComponents(builder);
        builder.setFluidOutput(new LayeredFluidOutputLogicComponent())
            .enableStandardQoL()
            .enableRecipeLocking()
            .setActivitySound(SoundResource.GT_MACHINES_DISTILLERY_LOOP);
    }

    private ILayeredFluidOutputLogicComponent fluidOutputLogic;

    @Override
    public void onComponentsReady() {
        super.onComponentsReady();
        fluidOutputLogic = getComponent(ILayeredFluidOutputLogicComponent.KEY);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.distillation_tower"))
            .addMarkdown(
                new ResourceLocation("gregtech", "distillation-tower"),
                ImmutableMap.<String, Object>builder().build())
            .beginVariableStructureBlock(3, 3, 3, 12, 3, 3, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("16-79", Casings.CleanStainlessSteelMachineCasing.getLocalizedName(), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1, 2)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1, 2)
            .addInputBus("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_casing"), 1)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_casing"), 1)
            .addOutputBus("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_casing"), 1)
            .addOutputHatch("2-11", StatCollector.translateToLocal("gt.mbtt.structure.one_per_layer_except_bottom"), 2)
            .addAir(StatCollector.translateToLocal("gt.mbtt.structure.interior"))
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("gt.mbtt.structure.distillation_min_casings_height"))
            .addSubChannel(GTStructureChannels.STRUCTURE_HEIGHT)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(CASING_INDEX);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.distillationTowerRecipes;
    }

    protected void onCasingFound() {
        mCasing++;
    }

    protected void onTopLayerFound(boolean aIsCasing) {
        mTopLayerFound = true;
        if (aIsCasing) onCasingFound();
    }

    protected int getCurrentLayerOutputHatchCount() {
        return fluidOutputLogic != null ? fluidOutputLogic.getLayerHatchCount(mHeight - 1) : 0;
    }

    protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity metaTile = aTileEntity.getMetaTileEntity();
        if (metaTile == null) return false;
        return fluidOutputLogic != null
            && fluidOutputLogic.tryAttachFluidOutput(metaTile, aBaseCasingIndex, mHeight - 1);
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a freaking tower, it won't work
        return (d, r, f) -> (d.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0 && r.isNotRotated()
            && !f.isVerticallyFliped();
    }

    @Override
    public boolean isRotationChangeAllowed() {
        return false;
    }

    @Override
    public IStructureDefinition<MTEDistillationTower> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        // reset
        mHeight = 1;
        mTopLayerFound = false;
        mCasing = 0;

        // check base
        if (!checkPiece(STRUCTURE_PIECE_BASE, 1, 0, 0, errors)) return;

        while (mHeight < 12) {
            if (!checkPiece(STRUCTURE_PIECE_LAYER, 1, mHeight, 0, errors)) return;
            if (mTopLayerFound) {
                break;
            }
            // not top
            mHeight++;
        }
    }

    @Override
    public void verifyMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        int scannedLayers = mTopLayerFound ? mHeight : mHeight - 1;
        List<Integer> missingLayers = new ArrayList<>();

        // check each layer
        for (int i = 0; i < scannedLayers; i++) {
            if (fluidOutputLogic.getLayerHatchCount(i) == 0) {
                missingLayers.add(i + 2);
            }
        }

        if (!missingLayers.isEmpty()) {
            errors.add(StructureErrors.missingOutputHatchDT(missingLayers));
        }

        // validate final invariants... (actual height is mHeight+1)
        if (mHeight + 1 < 3) {
            errors.add(StructureErrorRegistry.TOO_SHORT_HEIGHT);
            return;
        }
        if (!mTopLayerFound) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.missing_top"));
            return;
        }
        checkCasingMin(errors, mCasing, 7 * (mHeight + 1) - 5);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasInputHatch(errors);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 1, 0, 0);
        // min 2 output layer, so at least 1 + 2 height
        int tTotalHeight = GTStructureChannels.STRUCTURE_HEIGHT.getValueLimited(stackSize, 3, 12);
        for (int i = 1; i < tTotalHeight - 1; i++) {
            buildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, hintsOnly, 1, i, 0);
        }
        buildPiece(STRUCTURE_PIECE_TOP_HINT, stackSize, hintsOnly, 1, tTotalHeight - 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (isFormed()) return SURVIVAL_BUILD_DONE;
        mHeight = 0;
        int built = survivalBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 1, 0, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        // min 2 output layer, so at least 1 + 2 height
        int tTotalHeight = GTStructureChannels.STRUCTURE_HEIGHT.getValueLimited(stackSize, 3, 12);
        for (int i = 1; i < tTotalHeight - 1; i++) {
            mHeight = i;
            built = survivalBuildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, 1, i, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        mHeight = tTotalHeight - 1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_TOP_HINT,
            stackSize,
            1,
            tTotalHeight - 1,
            0,
            elementBudget,
            env,
            false,
            true);
    }
}
