package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTOreDictUnificator.getAssociation;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.ParallelHelper.addFluidsLong;
import static gregtech.api.util.ParallelHelper.addItemsLong;
import static gregtech.api.util.ParallelHelper.calculateIntegralChancedOutputMultiplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.ITierConverter;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchBulkCatalystHousing;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@SuppressWarnings("SpellCheckingInspection")
public class MTEQuantumForceTransformer extends MTEExtendedPowerMultiBlockBase<MTEQuantumForceTransformer>
    implements ISurvivalConstructable {

    private int mCasing;
    protected int mCraftingTier = 0;
    protected int mFocusingTier = 0;
    protected int mMaxParallel = 0;
    private boolean mFluidMode = false, doFermium = false, doNeptunium = false;
    private static final Fluid mNeptunium = MaterialsElements.getInstance().NEPTUNIUM.getPlasma();
    private static final Fluid mFermium = MaterialsElements.getInstance().FERMIUM.getPlasma();
    private static final String MAIN_PIECE = "main";
    private final ArrayList<MTEHatchBulkCatalystHousing> catalystHounsings = new ArrayList<>();
    // spotless:off
    // y-axis offset by +0.5 to counter the coordinate adjustment when rendering
    private static final double[][] FORCE_FIELD_BASE_COORDINATES = {
        { 3, -3.5, 7 }, { 3, 0.5, 7 },
        { -3, -3.5, 7 }, { -3, 0.5, 7 },
        { -7, -3.5, 3 }, { -7, 0.5, 3 },
        { -7, -3.5, -3 }, { -7, 0.5, -3 },
        { -3, -3.5, -7 }, { -3, 0.5, -7 },
        { 3, -3.5, -7 }, { 3, 0.5, -7 },
        { 7, -3.5, -3 }, { 7, 0.5, -3 },
        { 7, -3.5, 3 }, { 7, 0.5, 3 },
        { 3, -3.5, 7 }, { 3, 0.5, 7 }
    };
    // spotless:on
    private static final IStructureDefinition<MTEQuantumForceTransformer> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEQuantumForceTransformer>builder()
        .addShape(
            MAIN_PIECE,
            new String[][] { // A - 142, B - 234, C - 177, D - 96, E - 224, H - 36, M - 21
                // spotless:off
                { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "      BAB      ", "   BBBBABBBB   ", "   BAAAAAAAB   ", "   BABBABBAB   ", "   BA     AB   ", "    A     A    ", "    A     A    ", "    A     A    " },
                { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "      BAB      ", "   AAABBBAAA   ", "  BAAAAAAAAAB  ", "  B         B  ", "  A         A  ", "  A         A  ", "               ", "               ", "               " },
                { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "      BAB      ", "    AA   AA    ", "  AA       AA  ", " BAA       AAB ", " B           B ", " A           A ", " A           A ", "               ", "               ", "               " },
                { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "     BAAAB     ", "   AA     AA   ", " AA         AA ", "BAA         AAB", "B             B", "A             A", "A             A", "A             A", "A             A", "A             A" },
                { "      TTT      ", "      EEE      ", "      EEE      ", "      EEE      ", "      DDD      ", "      EEE      ", "      DDD      ", "      EEE      ", "      EEE      ", "      EEE      ", "      DDD      ", "    BAEEEAB    ", "  AA  EEE  AA  ", " A    EEE    A ", "BA    DDD    AB", "B     EEE     B", "B     DDD     B", "      EEE      ", "      EEE      ", "      EEE      ", "      H~H      " },
                { "     TTTTT     ", "     ECCCE     ", "     ECCCE     ", "     ECCCE     ", "     D   D     ", "     ECCCE     ", "     D   D     ", "     ECCCE     ", "     ECCCE     ", "     ECCCE     ", "     D   D     ", "   BAECCCEAB   ", "  A  ECCCE  A  ", " A   ECCCE   A ", "BA   D   D   AB", "B    ECCCE    B", "B    D   D    B", "B    ECCCE    B", "     ECCCE     ", "     ECCCE     ", "     HHHHH     " },
                { "    TTTTTTT    ", "    ECCCCCE    ", "    EC   CE    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ", "    EC   CE    ", "    EC   CE    ", "    D     D    ", "  BAEC   CEAB  ", " B  EC   CE  B ", "BB  EC   CE  BB", "BA  D     D  AB", "A   EC   CE   A", "A   D     D   A", "A   EC   CE   A", "    EC   CE    ", "    EC   CE    ", "    HHHHHHH    " },
                { "    TTTTTTT    ", "    ECCCCCE    ", "    EC   CE    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ", "    EC   CE    ", "    EC   CE    ", "    D     D    ", "  AAEC   CEAA  ", " A  EC   CE  A ", "AB  EC   CE  BA", "AA  D     D  AA", "A   EC   CE   A", "A   D     D   A", "    EC   CE    ", "    EC   CE    ", "    EC   CE    ", "    HHHHHHH    " },
                { "    TTTTTTT    ", "    ECCCCCE    ", "    EC   CE    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ", "    EC   CE    ", "    EC   CE    ", "    D     D    ", "  BAEC   CEAB  ", " B  EC   CE  B ", "BB  EC   CE  BB", "BA  D     D  AB", "A   EC   CE   A", "A   D     D   A", "A   EC   CE   A", "    EC   CE    ", "    EC   CE    ", "    HHHHHHH    " },
                { "     TTTTT     ", "     ECCCE     ", "     ECCCE     ", "     ECCCE     ", "     D   D     ", "     ECCCE     ", "     D   D     ", "     ECCCE     ", "     ECCCE     ", "     ECCCE     ", "     D   D     ", "   BAECCCEAB   ", "  A  ECCCE  A  ", " A   ECCCE   A ", "BA   D   D   AB", "B    ECCCE    B", "B    D   D    B", "B    ECCCE    B", "     ECCCE     ", "     ECCCE     ", "     HHHHH     " },
                { "      TTT      ", "      EEE      ", "      EEE      ", "      EEE      ", "      DDD      ", "      EEE      ", "      DDD      ", "      EEE      ", "      EEE      ", "      EEE      ", "      DDD      ", "    BAEEEAB    ", "  AA  EEE  AA  ", " A    EEE    A ", "BA    DDD    AB", "B     EEE     B", "B     DDD     B", "      EEE      ", "      EEE      ", "      EEE      ", "      HHH      " },
                { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "     BAAAB     ", "   AA     AA   ", " AA         AA ", "BAA         AAB", "B             B", "A             A", "A             A", "A             A", "A             A", "A             A" },
                { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "      BAB      ", "    AA   AA    ", "  AA       AA  ", " BAA       AAB ", " B           B ", " A           A ", " A           A ", "               ", "               ", "               " },
                { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "      BAB      ", "   AAABBBAAA   ", "  BAAAAAAAAAB  ", "  B         B  ", "  A         A  ", "  A         A  ", "               ", "               ", "               " },
                { "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "               ", "      BAB      ", "   BBBBABBBB   ", "   BBBAAABBB   ", "   ABBAAABBA   ", "   A BA AB A   ", "      A A      ", "      A A      ", "      A A      " }, })
                // spotless:on
        .addElement(
            'A',
            GTStructureChannels.QFT_MANIPULATOR.use(
                StructureUtility.ofBlocksTiered(
                    craftingTierConverter(),
                    getAllCraftingTiers(),
                    0,
                    MTEQuantumForceTransformer::setCraftingTier,
                    MTEQuantumForceTransformer::getCraftingTier)))
        .addElement(
            'B',
            GTStructureChannels.QFT_SHIELDING.use(
                StructureUtility.ofBlocksTiered(
                    focusingTierConverter(),
                    getAllFocusingTiers(),
                    0,
                    MTEQuantumForceTransformer::setFocusingTier,
                    MTEQuantumForceTransformer::getFocusingTier)))
        .addElement('C', ofBlock(ModBlocks.blockCasings4Misc, 4))
        .addElement('D', ofBlock(ModBlocks.blockCasings2Misc, 12))
        .addElement('E', lazy(t -> ofBlock(t.getCasingBlock1(), t.getCasingMeta1())))
        .addElement(
            'H',
            buildHatchAdder(MTEQuantumForceTransformer.class)
                .atLeast(
                    InputBus,
                    InputHatch,
                    Maintenance,
                    Energy.or(ExoticEnergy),
                    SpecialHatchElement.CatalystHousing)
                .casingIndex(TAE.getIndexFromPage(0, 10))
                .hint(4)
                .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 12))))
        .addElement(
            'T',
            buildHatchAdder(MTEQuantumForceTransformer.class).atLeast(OutputBus, OutputHatch, Maintenance)
                .casingIndex(TAE.getIndexFromPage(0, 10))
                .hint(5)
                .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 12))))
        .build();

    public MTEQuantumForceTransformer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEQuantumForceTransformer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEQuantumForceTransformer(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType("Quantum Force Transformer, QFT")
            .addInfo("Allows Complex processing lines to be performed instantly in one step")
            .addSeparator()
            .addInfo(catalystText("Pulse Manipulator") + " Tier determines maximum recipe tier")
            .addInfo("Every recipe requires a specific " + catalystText("catalyst"))
            .addInfo(catalystText("Catalysts") + " have to be placed in a Bulk Catalyst Housing")
            .addInfo("Gains " + TooltipHelper.parallelText("1") + " Parallel per " + catalystText("Catalyst"))
            .addSeparator()
            .addInfo(focusText("Shielding Core") + " Tier determines " + focusText("Focusing") + " Bonuses")
            .addInfo("Put a circuit in the controller to specify the focused output, based on NEI order")
            .addInfo("If Input Separation is on: put the circuit in the circuit slot of the bus")
            .addInfo("Consumes 4 * " + focusText("Focus Tier") + " * sqrt(" + TooltipHelper.parallelText("parallels") + ") L "+EnumChatFormatting.BLUE+"Neptunium Plasma" +EnumChatFormatting.GRAY+" to "+focusText("focus"))
            .addInfo("The better the " + focusText("Focus Tier") + ", the " + focusText("stronger") + " the effect")
            .addInfo(focusText("Focused") + " Output will have its " + EnumChatFormatting.AQUA + "probability boosted" + EnumChatFormatting.GRAY + ", with other output's being reduced evenly by the total boost")
            .addInfo("Consumes 4 * " + focusText("Focus Tier") + " * sqrt(" + TooltipHelper.parallelText("parallels") + ") L " + EnumChatFormatting.DARK_GREEN + "Fermium Plasma" + EnumChatFormatting.GRAY + " to " + EnumChatFormatting.DARK_GREEN + "boost all outputs")
            .addSeparator()
            .addInfo("Use a screwdriver to enable "+EnumChatFormatting.BLUE+"Fluid mode")
            .addInfo("Fluid mode turns all possible outputs into their fluid variant, if avaliable")
            .addUnlimitedTierSkips()
            .addTecTechHatchInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(15, 21, 15, true)
            .addController("Bottom Center")
            .addCasingInfoMin("Bulk Production Frame", 80, false)
            .addCasingInfoMin("Quantum Force Conductor", 177, false)
            .addCasingInfoMin("Force Field Glass", 224, false)
            .addCasingInfoMin("Pulse Manipulators", 236, true)
            .addCasingInfoMin("Shielding Cores", 142, true)
            .addInputBus(EnumChatFormatting.BLUE + "Bottom" + EnumChatFormatting.GRAY + " Layer", 4)
            .addInputHatch(EnumChatFormatting.BLUE + "Bottom" + EnumChatFormatting.GRAY + " Layer", 4)
            .addOutputHatch(EnumChatFormatting.AQUA + "Top" + EnumChatFormatting.GRAY + " Layer", 5)
            .addOutputBus(EnumChatFormatting.AQUA + "Top" + EnumChatFormatting.GRAY + " Layer", 5)
            .addEnergyHatch(EnumChatFormatting.BLUE + "Bottom" + EnumChatFormatting.GRAY + " Layer", 4)
            .addStructureInfo(EnumChatFormatting.WHITE + "Bulk Catalyst Housing: " + EnumChatFormatting.BLUE + "Bottom" + EnumChatFormatting.GRAY + " Layer")
            .addSubChannelUsage(GTStructureChannels.QFT_SHIELDING)
            .addSubChannelUsage(GTStructureChannels.QFT_MANIPULATOR)
            .toolTipFinisher(GTAuthors.AuthorBlueWeabo, EnumChatFormatting.GREEN + "Steelux");
        return tt;
        //spotless:on
    }

    @Override
    public IStructureDefinition<MTEQuantumForceTransformer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mCasing = 0;
        this.mCraftingTier = 0;
        this.mFocusingTier = 0;
        catalystHounsings.clear();
        if (!checkPiece(MAIN_PIECE, 7, 20, 4)) {
            return false;
        }

        // Maintenance hatch not required but left for compatibility.
        // Don't allow more than 1, no free casing spam!
        if (mMaintenanceHatches.size() > 1) {
            return false;
        }

        return checkExoticAndNormalEnergyHatches();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(MAIN_PIECE, stackSize, hintsOnly, 7, 20, 4);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(MAIN_PIECE, stackSize, 7, 20, 4, elementBudget, env, false, true);
    }

    public static List<Pair<Block, Integer>> getAllCraftingTiers() {
        return new ArrayList<>() {

            {
                add(Pair.of(ModBlocks.blockCasings5Misc, 7));
                add(Pair.of(ModBlocks.blockCasings5Misc, 8));
                add(Pair.of(ModBlocks.blockCasings5Misc, 9));
                add(Pair.of(ModBlocks.blockCasings5Misc, 10));
            }
        };
    }

    public static List<Pair<Block, Integer>> getAllFocusingTiers() {
        return new ArrayList<>() {

            {
                add(Pair.of(ModBlocks.blockCasings5Misc, 11));
                add(Pair.of(ModBlocks.blockCasings5Misc, 12));
                add(Pair.of(ModBlocks.blockCasings5Misc, 13));
                add(Pair.of(ModBlocks.blockCasings5Misc, 14));
            }
        };
    }

    public static ITierConverter<Integer> craftingTierConverter() {
        return (block, meta) -> {
            if (block == null) {
                return null;
            } else if (block == ModBlocks.blockCasings5Misc) { // Resonance Chambers
                switch (meta) {
                    case 7 -> {
                        return 1;
                    }
                    case 8 -> {
                        return 2;
                    }
                    case 9 -> {
                        return 3;
                    }
                    case 10 -> {
                        return 4;
                    }
                }
            }
            return null;
        };
    }

    public static ITierConverter<Integer> focusingTierConverter() {
        return (block, meta) -> {
            if (block == null) {
                return null;
            } else if (block == ModBlocks.blockCasings5Misc) { // Generation Coils
                switch (meta) {
                    case 11 -> {
                        return 1;
                    }
                    case 12 -> {
                        return 2;
                    }
                    case 13 -> {
                        return 3;
                    }
                    case 14 -> {
                        return 4;
                    }
                }
            }
            return null;
        };
    }

    private void setCraftingTier(int tier) {
        mCraftingTier = tier;
    }

    private void setFocusingTier(int tier) {
        mFocusingTier = tier;
    }

    private int getCraftingTier() {
        return mCraftingTier;
    }

    private int getFocusingTier() {
        return mFocusingTier;
    }

    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(0, 10);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.quantumForceTransformerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            private int[] chances;
            private FluidStack[] fluidModeItems;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.mSpecialValue > getCraftingTier()) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                }

                int numberOfCatalyst = 0;
                ItemStack requiredCatalyst = recipe.getMetadata(GTRecipeConstants.QFT_CATALYST);
                assert requiredCatalyst != null;
                int catalystMeta = requiredCatalyst.getItemDamage();
                if (catalystHounsings.isEmpty()) {
                    return SimpleCheckRecipeResult.ofFailure("no_catalyst");
                }
                boolean catalystsFound = false;
                for (MTEHatchBulkCatalystHousing catalystHousing : catalystHounsings) {
                    ItemStack storedCatalysts = catalystHousing.getItemStack();
                    int storedCatalystMeta = catalystHousing.getStoredCatalystMeta();
                    if (storedCatalysts == null || storedCatalystMeta != catalystMeta) {
                        continue;
                    }
                    numberOfCatalyst = catalystHousing.getItemCount();
                    catalystsFound = true;
                    break;
                }
                if (!catalystsFound) {
                    return SimpleCheckRecipeResult.ofFailure("no_catalyst");
                }

                mMaxParallel = numberOfCatalyst;
                maxParallel = mMaxParallel;
                doFermium = false;
                doNeptunium = false;

                if (recipe.getMetadataOrDefault(GTRecipeConstants.QFT_FOCUS_TIER, 1) <= getFocusingTier()) {
                    FluidStack[] fluids = inputFluids;
                    for (FluidStack fluid : fluids) {
                        if (fluid.getFluid()
                            .equals(mNeptunium)) {
                            doNeptunium = true;
                        }
                        if (fluid.getFluid()
                            .equals(mFermium)) {
                            doFermium = true;
                        }
                    }
                }

                chances = getOutputChances(recipe, doNeptunium ? findProgrammedCircuitNumber() : -1);

                // Handle Fluid Mode. Add fluid that item can be turned into to fluidModeItems.
                // null if Fluid Mode is disabled or item cannot be turned into fluid.
                fluidModeItems = new FluidStack[recipe.mOutputs.length];
                if (mFluidMode) {
                    for (int i = 0; i < recipe.mOutputs.length; i++) {
                        ItemStack item = recipe.getOutput(i);
                        if (item == null) continue;
                        ItemData data = getAssociation(item);
                        Materials mat = data == null ? null : data.mMaterial.mMaterial;
                        if (mat != null) {
                            if (mat.mStandardMoltenFluid != null) {
                                fluidModeItems[i] = mat.getMolten(1 * INGOTS);
                            } else if (mat.mFluid != null) {
                                fluidModeItems[i] = mat.getFluid(1_000);
                            }
                        }
                    }
                }

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                return super.createParallelHelper(recipe).setCustomItemOutputCalculation(parallel -> {
                    ArrayList<ItemStack> items = new ArrayList<>();

                    for (int i = 0; i < recipe.mOutputs.length; i++) {
                        ItemStack item = recipe.getOutput(i);
                        if (item == null || fluidModeItems[i] != null) continue;
                        ItemStack itemToAdd = item.copy();
                        long outputMultiplier = calculateIntegralChancedOutputMultiplier(chances[i], parallel);
                        long itemAmount = item.stackSize * outputMultiplier;
                        addItemsLong(items, itemToAdd, itemAmount);
                    }

                    return items.toArray(new ItemStack[0]);
                })
                    .setCustomFluidOutputCalculation(parallel -> {
                        ArrayList<FluidStack> fluids = new ArrayList<>();

                        if (mFluidMode) {
                            for (int i = 0; i < recipe.mOutputs.length; i++) {
                                FluidStack fluid = fluidModeItems[i];
                                if (fluid == null) continue;
                                FluidStack fluidToAdd = fluid.copy();
                                long outputMultiplier = calculateIntegralChancedOutputMultiplier(chances[i], parallel);
                                int itemAmount = recipe.mOutputs[i].stackSize;
                                long fluidAmount = fluidToAdd.amount * outputMultiplier * itemAmount;
                                addFluidsLong(fluids, fluidToAdd, fluidAmount);
                            }
                        }

                        for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                            FluidStack fluid = recipe.getFluidOutput(i);
                            if (fluid == null) continue;
                            FluidStack fluidToAdd = fluid.copy();
                            long outputMultiplier = calculateIntegralChancedOutputMultiplier(
                                chances[i + recipe.mOutputs.length],
                                parallel);
                            long fluidAmount = fluidToAdd.amount * outputMultiplier;
                            addFluidsLong(fluids, fluidToAdd, fluidAmount);
                        }

                        return fluids.toArray(new FluidStack[0]);
                    });
            }

            private int findProgrammedCircuitNumber() {
                if (isInputSeparationEnabled()) {
                    for (ItemStack stack : inputItems) {
                        if (GTUtility.isAnyIntegratedCircuit(stack)) {
                            return stack.getItemDamage() - 1;
                        }
                    }
                    return -1;
                } else {
                    for (ItemStack stack : inputItems) {
                        if (GTUtility.isAnyIntegratedCircuit(stack)) {
                            return stack.getItemDamage() - 1;
                        }
                    }
                    final ItemStack controllerStack = getControllerSlot();
                    return GTUtility.isAnyIntegratedCircuit(controllerStack) ? controllerStack.getItemDamage() - 1 : -1;
                }
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getAverageInputVoltage());
        logic.setAvailableAmperage(getMaxInputAmps());
        logic.setUnlimitedTierSkips();
    }

    private byte runningTick = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            return false;
        }

        if (runningTick % 20 == 0) {
            int amount = (int) (getFocusingTier() * 4
                * Math.sqrt(Math.min(mMaxParallel, processingLogic.getCurrentParallels())));

            if (doNeptunium || doFermium) {
                startRecipeProcessing();
                List<FluidStack> fluids = getStoredFluids();
                for (FluidStack fluid : fluids) {
                    if (fluid == null) continue;
                    if (doNeptunium && fluid.getFluid() == mNeptunium) {
                        FluidStack neptuniumToConsume = new FluidStack(mNeptunium, amount);

                        if (!this.depleteInput(neptuniumToConsume)) {
                            this.depleteInput(fluid);
                            doNeptunium = false;
                            mOutputItems = null;
                            mOutputFluids = null;
                            mProgresstime = mMaxProgresstime;
                        }
                    }
                    if (doFermium && fluid.getFluid() == mFermium) {
                        FluidStack fermiumToConsume = new FluidStack(mFermium, amount);

                        if (!this.depleteInput(fermiumToConsume)) {
                            this.depleteInput(fluid);
                            doFermium = false;
                            mOutputItems = null;
                            mOutputFluids = null;
                            mProgresstime = mMaxProgresstime;
                        }
                    }
                }
                endRecipeProcessing();
            }

            runningTick = 1;
        } else {
            runningTick++;
        }

        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            // TODO: Look for proper fix
            // Updates every 30 sec
            if (mUpdate <= -550) mUpdate = 50;
        }
    }

    public static int getBaseOutputChance(GTRecipe tRecipe) {
        int aOutputsAmount = tRecipe.mOutputs.length + tRecipe.mFluidOutputs.length;
        return 10000 / aOutputsAmount;
    }

    private int[] getOutputChances(GTRecipe tRecipe, int aChanceIncreased) {
        int difference = getFocusingTier() - tRecipe.mSpecialValue;
        int aOutputsAmount = tRecipe.mOutputs.length + tRecipe.mFluidOutputs.length;
        int aChancePerOutput = 10000 / aOutputsAmount;
        int[] tChances = new int[aOutputsAmount];
        Arrays.fill(tChances, aChancePerOutput);

        switch (difference) {
            case 0 -> {
                for (int i = 0; i < tChances.length; i++) {
                    if (doNeptunium) {
                        if (i == aChanceIncreased) {
                            tChances[i] += aChancePerOutput / 2 * (aOutputsAmount - 1);
                        } else {
                            tChances[i] /= 2;
                        }
                    }

                    if (doFermium) {
                        tChances[i] += (10000 - tChances[i]) / 4;
                    }
                }
            }
            case 1 -> {
                for (int i = 0; i < tChances.length; i++) {
                    if (doNeptunium) {
                        if (i == aChanceIncreased) {
                            tChances[i] += aChancePerOutput * 3 / 4 * (aOutputsAmount - 1);
                        } else {
                            tChances[i] /= 4;
                        }
                    }

                    if (doFermium) {
                        tChances[i] += (10000 - tChances[i]) / 3;
                    }
                }
            }
            case 2, 3 -> {
                for (int i = 0; i < tChances.length; i++) {
                    if (doNeptunium) {
                        if (i == aChanceIncreased) {
                            tChances[i] = 10000;
                        } else {
                            tChances[i] = 0;
                        }
                    }

                    if (doFermium) {
                        tChances[i] += (10000 - tChances[i]) / 2;
                    }
                }
            }
        }
        return tChances;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        mFluidMode = !mFluidMode;
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal("miscutils.machines.QFTFluidMode") + " " + mFluidMode);
    }

    public boolean addCatalystHousingToMachineList(IGregTechTileEntity tileEntity, int baseCasingIndex) {
        if (tileEntity == null) return false;
        IMetaTileEntity metaTileEntity = tileEntity.getMetaTileEntity();
        if (metaTileEntity instanceof MTEHatchBulkCatalystHousing catalystHousing) {
            catalystHousing.updateTexture(baseCasingIndex);
            this.catalystHounsings.add(catalystHousing);
            return true;
        }
        return false;
    }

    private enum SpecialHatchElement implements IHatchElement<MTEQuantumForceTransformer> {

        CatalystHousing(MTEQuantumForceTransformer::addCatalystHousingToMachineList,
            MTEHatchBulkCatalystHousing.class) {

            @Override
            public long count(MTEQuantumForceTransformer gtMetaTileEntityQFT) {
                return gtMetaTileEntityQFT.catalystHounsings.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEQuantumForceTransformer> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTEQuantumForceTransformer> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEQuantumForceTransformer> adder() {
            return adder;
        }
    }

    public Block getCasingBlock1() {
        return ModBlocks.blockCasings5Misc;
    }

    public byte getCasingMeta1() {
        return 15;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("mFluidMode", mFluidMode);
        aNBT.setBoolean("doFermium", doFermium);
        aNBT.setBoolean("doNeptunium", doNeptunium);
        aNBT.setInteger("mMaxParallel", mMaxParallel);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("mSeparateInputBusses");
        }
        if (!aNBT.hasKey(BATCH_MODE_NBT_KEY)) {
            batchMode = aNBT.getBoolean("mBatchMode");
        }
        mFluidMode = aNBT.getBoolean("mFluidMode");
        doFermium = aNBT.getBoolean("doFermium");
        doNeptunium = aNBT.getBoolean("doNeptunium");
        mMaxParallel = aNBT.getInteger("mMaxParallel");
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) {
                return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAQFTActive)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.oMCAQFTActiveGlow)
                        .extFacing()
                        .glow()
                        .build() };
            }
            return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAQFT)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAQFTGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { getCasingTexture() };
    }

    private ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(getCasingTextureId());
    }

    @SideOnly(Side.CLIENT)
    private void renderForceField(double x, double y, double z, double minU, double maxU, double minV, double maxV) {
        // spotless:off
        Tessellator tes = Tessellator.instance;
        // Convert base coords to world offset -> position transform -> push to tessellator
        double [][] forceFieldCoordinates = new double [FORCE_FIELD_BASE_COORDINATES.length][];
        for (int i = 0; i < FORCE_FIELD_BASE_COORDINATES.length; i++) {
            double [] transformed = new double[3];
            getExtendedFacing().getWorldOffset(FORCE_FIELD_BASE_COORDINATES[i], transformed);
            transformed[0] += x;
            transformed[1] += y;
            transformed[2] += z;
            forceFieldCoordinates[i] = transformed;
        }
        for (int cur = 0; cur < forceFieldCoordinates.length - 3; cur += 2) {
            double [] cur_bot = forceFieldCoordinates[cur];
            double [] cur_top = forceFieldCoordinates[cur+1];
            double [] nex_bot = forceFieldCoordinates[cur+2];
            double [] nex_top = forceFieldCoordinates[cur+3];
            tes.addVertexWithUV(cur_bot[0], cur_bot[1], cur_bot[2], maxU, maxV);
            tes.addVertexWithUV(cur_top[0], cur_top[1], cur_top[2], maxU, minV);
            tes.addVertexWithUV(nex_top[0], nex_top[1], nex_top[2], minU, minV);
            tes.addVertexWithUV(nex_bot[0], nex_bot[1], nex_bot[2], minU, maxV);
            tes.addVertexWithUV(nex_bot[0], nex_bot[1], nex_bot[2], minU, maxV);
            tes.addVertexWithUV(nex_top[0], nex_top[1], nex_top[2], minU, minV);
            tes.addVertexWithUV(cur_top[0], cur_top[1], cur_top[2], maxU, minV);
            tes.addVertexWithUV(cur_bot[0], cur_bot[1], cur_bot[2], maxU, maxV);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderInWorld(ISBRWorldContext ctx) {
        Tessellator tes = Tessellator.instance;
        IIcon forceField = TexturesGtBlock.ForceField.getIcon();
        if (getBaseMetaTileEntity().isActive()) {
            double minU = forceField.getMinU();
            double maxU = forceField.getMaxU();
            double minV = forceField.getMinV();
            double maxV = forceField.getMaxV();
            double xBaseOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX;
            double yBaseOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetY;
            double zBaseOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
            tes.setColorOpaque_F(1f, 1f, 1f);
            tes.setBrightness(15728880);
            //Center O:  0,  0         1 ------- 8
            //Corner 1:  7, -2        /           \
            //Corner 2:  3, -6     2 /             \ 7
            //Corner 3: -2, -6      |               |
            //Corner 4: -6, -2      |       O       |
            //Corner 5: -6,  3      |               |
            //Corner 6: -2,  7     3 \             / 6
            //Corner 7:  3,  7        \           /
            //Corner 8:  7,  3         4 ------- 5
            renderForceField(ctx.getX() + xBaseOffset + 0.5, ctx.getY() + yBaseOffset + 0.5, ctx.getZ() + zBaseOffset + 0.5, minU, maxU, minV, maxV);
        }
        // Needs to be false to render the controller
        return false;
        //spotless:on
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_QUANTUM_FORCE_TRANSFORMER_LOOP;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }

    private String catalystText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.LIGHT_PURPLE, text, EnumChatFormatting.GRAY);
    }

    private String focusText(String text) {
        return String.format("%s%s%s", EnumChatFormatting.GREEN, text, EnumChatFormatting.GRAY);
    }

}
