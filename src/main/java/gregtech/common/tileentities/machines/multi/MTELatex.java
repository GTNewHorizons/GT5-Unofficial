package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Mods.UniversalSingularities;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATEX;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATEX_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATEX_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATEX_GLOW;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.EnumChatFormatting.DARK_AQUA;
import static net.minecraft.util.EnumChatFormatting.DARK_GRAY;
import static net.minecraft.util.EnumChatFormatting.DARK_GREEN;
import static net.minecraft.util.EnumChatFormatting.GREEN;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;

public class MTELatex extends MTEExtendedPowerMultiBlockBase<MTELatex>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int CASING_INDEX = 176;
    private static final IStructureDefinition<MTELatex> STRUCTURE_DEFINITION = StructureDefinition.<MTELatex>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                " CCC ",
                "     ",
                "     ",
                "     ",
                "     ",
                "     ",
                "     ",
                " C~C "
            },{
                "CCCCC",
                " DDD ",
                " AAA ",
                " AAA ",
                " AAA ",
                " AAA ",
                " DDD ",
                "CCCCC"
            },{
                "CCCCC",
                " DBD ",
                " ABA ",
                " ABA ",
                " ABA ",
                " ABA ",
                " DBD ",
                "CCCCC"
            },{
                "CCCCC",
                " DDD ",
                " AAA ",
                " AAA ",
                " AAA ",
                " AAA ",
                " DDD ",
                "CCCCC"
            },{
                " CCC ",
                "     ",
                "     ",
                "     ",
                "     ",
                "     ",
                "     ",
                " CCC "
            }} )
        //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement(
            'B',
            GTStructureChannels.ITEM_PIPE_CASING.use(
                ofBlocksTiered(
                    MTELatex::getItemPipeTierFromMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCasings11, 0),
                        Pair.of(GregTechAPI.sBlockCasings11, 1),
                        Pair.of(GregTechAPI.sBlockCasings11, 2),
                        Pair.of(GregTechAPI.sBlockCasings11, 3),
                        Pair.of(GregTechAPI.sBlockCasings11, 4),
                        Pair.of(GregTechAPI.sBlockCasings11, 5),
                        Pair.of(GregTechAPI.sBlockCasings11, 6),
                        Pair.of(GregTechAPI.sBlockCasings11, 7)),
                    -1,
                    MTELatex::setItemPipeTier,
                    MTELatex::getItemPipeTier)))
        .addElement(
            'C',
            buildHatchAdder(MTELatex.class)
                .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(onElementPass(MTELatex::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 0))))
        .addElement('D', ofFrame(Materials.PolyvinylChloride))
        .build();

    private int itemPipeTier = -1;
    private int base_parallel = 8;
    private static final FluidStack[] valid_rubbers = { Materials.Rubber.getMolten(1L),
        Materials.RubberSilicone.getMolten(1L), Materials.StyreneButadieneRubber.getMolten(1L) };
    private static final ItemStack SINGULARITY = getModItem(
        UniversalSingularities.ID,
        "universal.rubber.singularity",
        1L,
        5);

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                return super.createParallelHelper(Objects.requireNonNull(recipeAfterAdjustments(recipe)));
            }

            @Nonnull
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                if (map == null) {
                    return Stream.empty();
                }
                return map.findRecipeQuery()
                    .caching(recipeCaching)
                    .items(inputItems)
                    .fluids(fluidsAfterAdjustments(inputFluids))
                    .specialSlot(specialSlotItem)
                    .cachedRecipe(lastRecipe)
                    .findAll();
            }

            private FluidStack[] fluidsAfterAdjustments(FluidStack[] inputFluids) {
                FluidStack[] copy = new FluidStack[inputFluids.length];

                for (int i = 0; i < inputFluids.length; i++) {
                    if (inputFluids[i] == null) continue;
                    copy[i] = inputFluids[i].copy();
                    copy[i].amount = (int) (copy[i].amount / getRubberCostMult());
                }

                return copy;
            }
        }.setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getTrueParallel)
            .setEuModifier(0.85F);
    }

    @Nullable
    private static Integer getItemPipeTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasings11) return null;
        if (metaID < 0 || metaID > 7) return null;
        return metaID + 1;
    }

    private void setItemPipeTier(int tier) {
        itemPipeTier = tier;
    }

    private int getItemPipeTier() {
        return itemPipeTier;
    }

    protected GTRecipe recipeAfterAdjustments(@Nonnull GTRecipe recipe) {
        GTRecipe tRecipe = recipe.copy();
        for (int i = 0; i < recipe.mFluidInputs.length; i++) {
            for (FluidStack rubber : valid_rubbers) {
                if (tRecipe.mFluidInputs[i].isFluidEqual(rubber)) {
                    tRecipe.mFluidInputs[i].amount = (int) Math
                        .round(tRecipe.mFluidInputs[i].amount * getRubberCostMult());
                    return tRecipe;
                }
            }
        }
        return tRecipe;
    }

    private double getRubberCostMult() {
        ItemStack controllerStack = this.getControllerSlot();
        double discount = 0.0625 * itemPipeTier;
        base_parallel = 8;
        if (controllerStack != null && controllerStack
            .isItemEqual(UniversalSingularities.isModLoaded() ? SINGULARITY : ItemList.Tool_DataStick.get(1))) {
            discount += 0.25;
            base_parallel = 16;
        }
        return 1 - discount;
    }

    public MTELatex(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELatex(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTELatex> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELatex(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_MULTI_LATEX,
            OVERLAY_FRONT_MULTI_LATEX_GLOW,
            OVERLAY_FRONT_MULTI_LATEX_ACTIVE,
            OVERLAY_FRONT_MULTI_LATEX_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 0));
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Cable Coater, LATEX")
            .addInfo(
                DARK_GRAY + "" + EnumChatFormatting.ITALIC + "AKA Laminate Application and Thermal Enclosure eXocoater")
            .addBulkMachineInfo(8, 2F, 0.85F)
            .addInfo(
                "Recipes have an additive " + TooltipHelper.coloredText("6.25%", DARK_GREEN)
                    + " rubber discount based on "
                    + TooltipHelper.tierText(TooltipTier.ITEM_PIPE_CASING))
            .addInfo("An Elastic Singularity can be inserted into the controller to gain the following bonuses")
            .addInfo(
                TooltipHelper.parallelText("2x") + " parallels, +"
                    + TooltipHelper.coloredText("25%", DARK_GREEN)
                    + " rubber discount, and the use of "
                    + TooltipHelper.coloredText("Multi-Amp & Laser Energy Hatches", GREEN))
            .addSeparator()
            .addInfo(DARK_AQUA + "Make sure to cover up!")
            .beginStructureBlock(5, 5, 8, false)
            .addController("Front bottom center")
            .addCasing("14-36", "Chemically Inert Casing", false)
            .addCasing("32", "Any Tiered Glass", false)
            .addCasing("16", "Polyvinyl Chloride Frame Box", false)
            .addCasing("6", "Item Pipe Casing", true)
            .addEnergyHatch("1+", "Any casing", 1)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addInputBus("1+", "Any casing", 1)
            .addInputHatch("1+", "Any casing", 1)
            .addOutputBus("1+", "Any casing", 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .addSubChannel(GTStructureChannels.ITEM_PIPE_CASING)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 7, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 7, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        itemPipeTier = -1;
        mCasingAmount = 0;
        clearHatches();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 7, 0, errors)) return;
        ItemStack controllerStack = this.getControllerSlot();
        boolean singularityPresent = controllerStack != null && controllerStack
            .isItemEqual(UniversalSingularities.isModLoaded() ? SINGULARITY : ItemList.Tool_DataStick.get(1));
        checkHasAnyEnergy(errors);
        checkHasMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
        checkHasOutputBus(errors);
        if (!mExoticEnergyHatches.isEmpty() && !singularityPresent) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.latex_singularity"));
        }
        checkCasingMin(errors, mCasingAmount, 14);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (base_parallel * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.cableRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

}
