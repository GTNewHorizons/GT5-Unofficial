package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

@IMetaTileEntity.SkipGenerateDescription
public class MTEPurificationUnitOzonation extends MTEPurificationUnitBase<MTEPurificationUnitOzonation>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "main_survival";

    private static final String[][] structure = new String[][] {
        // spotless:off
        { "         ", "         ", "      A  ", "      A  ", "     AAA ", "     AAA ", "     A A ", "     A A ", "     A A ", "     A~A " },
        { "      A  ", "      A  ", "     A A ", "     A A ", "BBBBA   A", "BDDBA   A", "BBBBA D A", "E   A D A", "E   A D A", "E   AAAAA" },
        { "     AAA ", "     A A ", "    A   A", "    A   A", "BDDBA   A", "O  BA   A", "BBBBA   A", "  C A   A", "  CCA   A", "    AAAAA" },
        { "      A  ", "      A  ", "     A A ", "     A A ", "BBBBA   A", "BDDBA   A", "BBBBA   A", "E   A   A", "E   A   A", "E   AAAAA" },
        { "         ", "         ", "      A  ", "      A  ", "     AAA ", "     AAA ", "     AAA ", "     AAA ", "     AAA ", "     AAA " } };
    // spotless:on

    private static final int MAIN_CASING_INDEX = getTextureIndex(GregTechAPI.sBlockCasings9, 10);

    private static final int OFFSET_X = 6;
    private static final int OFFSET_Y = 9;
    private static final int OFFSET_Z = 0;

    /**
     * If the player inserts more ozone gas than this amount, the multi will explode.
     */
    public static final int MAX_OZONE_GAS_FOR_EXPLOSION = 1000 * (int) GTUtility.powInt(2, 10);

    /**
     * Success chance bonus in percent for each doubling of ozone in the recipe.
     */
    public static final float SUCCESS_PER_OZONE_DOUBLING = 20.0f;

    private int casingCount = 0;
    private static final int MIN_CASING = 96;

    private static final IStructureDefinition<MTEPurificationUnitOzonation> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPurificationUnitOzonation>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Inert Filtration Casing
        .addElement(
            'A',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEPurificationUnitOzonation>buildHatchAdder()
                        .atLeastList(ImmutableList.of(InputHatch, OutputHatch, OutputBus))
                        .casingIndex(getTextureIndex(GregTechAPI.sBlockCasings9, 10))
                        .hint(1)
                        .build()),
                onElementPass(t -> t.casingCount++, ofBlock(GregTechAPI.sBlockCasings9, 10))))
        // High Pressure Resistant Casing (possibly placeholder name)
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings9, 9))
        // PTFE pipe casing
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 1))
        // Any tinted industrial glass
        .addElement('D', ofBlockAnyMeta(GregTechAPI.sBlockTintedGlass))
        .addElement('E', ofFrame(Materials.TungstenSteel))
        // Ozone input hatch
        .addElement(
            'O',
            lazy(
                t -> GTStructureUtility.<MTEPurificationUnitOzonation>buildHatchAdder()
                    .atLeast(InputHatch)
                    .casingIndex(getTextureIndex(GregTechAPI.sBlockCasings9, 9))
                    .hint(2)
                    .buildAndChain(onElementPass(x -> x.casingCount++, ofBlock(GregTechAPI.sBlockCasings9, 9)))))
        .build();

    public MTEPurificationUnitOzonation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEPurificationUnitOzonation(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPurificationUnitOzonation(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            true);
    }

    @Override
    public IStructureDefinition<MTEPurificationUnitOzonation> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.purification_unit_ozonation"))
            .addMarkdown(
                new ResourceLocation("gregtech", "purification-unit-ozonation"),
                ImmutableMap.of(
                    "tier",
                    getWaterTier(),
                    "max_ozone",
                    formatNumber(MAX_OZONE_GAS_FOR_EXPLOSION),
                    "bonus",
                    formatNumber(SUCCESS_PER_OZONE_DOUBLING),
                    "ozone",
                    Materials.Ozone.getGas(1)
                        .getLocalizedName()))
            .beginStructureBlock(9, 10, 5, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing(MIN_CASING + "-99", Casings.InertFiltrationCasing.getLocalizedName(), false)
            .addCasing("27", Casings.ReactiveGasContainmentCasing.getLocalizedName(), false)
            .addCasing(
                "9",
                StatCollector.translateToLocalFormatted(
                    "GT5U.tooltip.windmill.any_color",
                    StatCollector.translateToLocal("gt.blocktintedglass.name")),
                false)
            .addCasing("6", OrePrefixes.frameGt.getLocalizedNameForItem(Materials.TungstenSteel), false)
            .addCasing("3", Casings.PTFEPipeCasing.getLocalizedName(), false)
            .addInputHatch("2+", StatCollector.translateToLocal("gt.mbtt.structure.ozonation_input_hatch"), 1, 2)
            .addOutputBus("0+", StatCollector.translateToLocal("gt.mbtt.structure.any_filtration_casing"), 1)
            .addOutputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_filtration_casing"), 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DataStick.Waterline"))
            .toolTipFinisher();
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationOzonationRecipes;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        // First do recipe checking logic
        CheckRecipeResult result = super.checkProcessing();
        if (!result.wasSuccessful()) return result;
        // Look for ozone, blow up if more than max allowed
        for (FluidStack fluid : this.storedFluids) {
            if (fluid.isFluidEqual(Materials.Ozone.getGas(1L))) {
                if (fluid.amount > MAX_OZONE_GAS_FOR_EXPLOSION) {
                    this.explodeMultiblock();
                }
            }
        }
        return result;
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_OZONATION_LOOP;
    }

    @Override
    public int getWaterTier() {
        return 2;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingCount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingCount, MIN_CASING);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
    }

    @Override
    public long getBasePowerUsage() {
        return TierEU.RECIPE_LuV;
    }
}
