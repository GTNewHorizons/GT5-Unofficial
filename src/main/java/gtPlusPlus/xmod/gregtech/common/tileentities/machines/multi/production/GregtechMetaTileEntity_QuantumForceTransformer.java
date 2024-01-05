package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_OreDictUnificator.getAssociation;
import static gregtech.api.util.GT_RecipeBuilder.BUCKETS;
import static gregtech.api.util.GT_RecipeBuilder.INGOTS;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.ITierConverter;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@SuppressWarnings("SpellCheckingInspection")
public class GregtechMetaTileEntity_QuantumForceTransformer
        extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GregtechMetaTileEntity_QuantumForceTransformer>
        implements ISurvivalConstructable {

    private int mCasing;
    protected int mCraftingTier = 0;
    protected int mFocusingTier = 0;
    private boolean mFluidMode = false, doFermium = false, doNeptunium = false;
    private static final Fluid mNeptunium = ELEMENT.getInstance().NEPTUNIUM.getPlasma();
    private static final Fluid mFermium = ELEMENT.getInstance().FERMIUM.getPlasma();
    private static final String MAIN_PIECE = "main";
    private GT_MetaTileEntity_Hatch_Input mNeptuniumHatch;
    private GT_MetaTileEntity_Hatch_Input mFermiumHatch;
    private static final IStructureDefinition<GregtechMetaTileEntity_QuantumForceTransformer> STRUCTURE_DEFINITION = StructureDefinition
            .<GregtechMetaTileEntity_QuantumForceTransformer>builder().addShape(
                    MAIN_PIECE,
                    new String[][] { // A - 142, B - 234, C - 177, D - 96, E - 224, H - 36, M - 21
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "      BAB      ", "   BBBBABBBB   ", "   BAAAAAAAB   ",
                                    "   BABBABBAB   ", "   BA     AB   ", "    A     A    ", "    A     A    ",
                                    "    A     A    " },
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "      BAB      ", "   AAABBBAAA   ", "  BAAAAAAAAAB  ", "  B         B  ",
                                    "  A         A  ", "  A         A  ", "               ", "               ",
                                    "               " },
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "      BAB      ",
                                    "    AA   AA    ", "  AA       AA  ", " BAA       AAB ", " B           B ",
                                    " A           A ", " A           A ", "               ", "               ",
                                    "               " },
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "     BAAAB     ",
                                    "   AA     AA   ", " AA         AA ", "BAA         AAB", "B             B",
                                    "A             A", "A             A", "A             A", "A             A",
                                    "A             A" },
                            { "      TTT      ", "      EEE      ", "      EEE      ", "      EEE      ",
                                    "      DDD      ", "      EEE      ", "      DDD      ", "      EEE      ",
                                    "      EEE      ", "      EEE      ", "      DDD      ", "    BAEEEAB    ",
                                    "  AA  EEE  AA  ", " A    EEE    A ", "BA    DDD    AB", "B     EEE     B",
                                    "B     DDD     B", "      EEE      ", "      EEE      ", "      EEE      ",
                                    "      Z~X      " },
                            { "     TTTTT     ", "     ECCCE     ", "     ECCCE     ", "     ECCCE     ",
                                    "     D   D     ", "     ECCCE     ", "     D   D     ", "     ECCCE     ",
                                    "     ECCCE     ", "     ECCCE     ", "     D   D     ", "   BAECCCEAB   ",
                                    "  A  ECCCE  A  ", " A   ECCCE   A ", "BA   D   D   AB", "B    ECCCE    B",
                                    "B    D   D    B", "B    ECCCE    B", "     ECCCE     ", "     ECCCE     ",
                                    "     HHHHH     " },
                            { "    TTTTTTT    ", "    ECCCCCE    ", "    EC   CE    ", "    EC   CE    ",
                                    "    D     D    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ",
                                    "    EC   CE    ", "    EC   CE    ", "    D     D    ", "  BAEC   CEAB  ",
                                    " B  EC   CE  B ", "BB  EC   CE  BB", "BA  D     D  AB", "A   EC   CE   A",
                                    "A   D     D   A", "A   EC   CE   A", "    EC   CE    ", "    EC   CE    ",
                                    "    HHHHHHH    " },
                            { "    TTTTTTT    ", "    ECCCCCE    ", "    EC   CE    ", "    EC   CE    ",
                                    "    D     D    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ",
                                    "    EC   CE    ", "    EC   CE    ", "    D     D    ", "  AAEC   CEAA  ",
                                    " A  EC   CE  A ", "AB  EC   CE  BA", "AA  D     D  AA", "A   EC   CE   A",
                                    "A   D     D   A", "    EC   CE    ", "    EC   CE    ", "    EC   CE    ",
                                    "    HHHHHHH    " },
                            { "    TTTTTTT    ", "    ECCCCCE    ", "    EC   CE    ", "    EC   CE    ",
                                    "    D     D    ", "    EC   CE    ", "    D     D    ", "    EC   CE    ",
                                    "    EC   CE    ", "    EC   CE    ", "    D     D    ", "  BAEC   CEAB  ",
                                    " B  EC   CE  B ", "BB  EC   CE  BB", "BA  D     D  AB", "A   EC   CE   A",
                                    "A   D     D   A", "A   EC   CE   A", "    EC   CE    ", "    EC   CE    ",
                                    "    HHHHHHH    " },
                            { "     TTTTT     ", "     ECCCE     ", "     ECCCE     ", "     ECCCE     ",
                                    "     D   D     ", "     ECCCE     ", "     D   D     ", "     ECCCE     ",
                                    "     ECCCE     ", "     ECCCE     ", "     D   D     ", "   BAECCCEAB   ",
                                    "  A  ECCCE  A  ", " A   ECCCE   A ", "BA   D   D   AB", "B    ECCCE    B",
                                    "B    D   D    B", "B    ECCCE    B", "     ECCCE     ", "     ECCCE     ",
                                    "     HHHHH     " },
                            { "      TTT      ", "      EEE      ", "      EEE      ", "      EEE      ",
                                    "      DDD      ", "      EEE      ", "      DDD      ", "      EEE      ",
                                    "      EEE      ", "      EEE      ", "      DDD      ", "    BAEEEAB    ",
                                    "  AA  EEE  AA  ", " A    EEE    A ", "BA    DDD    AB", "B     EEE     B",
                                    "B     DDD     B", "      EEE      ", "      EEE      ", "      EEE      ",
                                    "      HHH      " },
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "     BAAAB     ",
                                    "   AA     AA   ", " AA         AA ", "BAA         AAB", "B             B",
                                    "A             A", "A             A", "A             A", "A             A",
                                    "A             A" },
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "      BAB      ",
                                    "    AA   AA    ", "  AA       AA  ", " BAA       AAB ", " B           B ",
                                    " A           A ", " A           A ", "               ", "               ",
                                    "               " },
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "      BAB      ", "   AAABBBAAA   ", "  BAAAAAAAAAB  ", "  B         B  ",
                                    "  A         A  ", "  A         A  ", "               ", "               ",
                                    "               " },
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "      BAB      ", "   BBBBABBBB   ", "   BBBAAABBB   ",
                                    "   ABBAAABBA   ", "   A BA AB A   ", "      A A      ", "      A A      ",
                                    "      A A      " }, })
            .addElement(
                    'A',
                    withChannel(
                            "manipulator",
                            StructureUtility.ofBlocksTiered(
                                    craftingTierConverter(),
                                    getAllCraftingTiers(),
                                    0,
                                    GregtechMetaTileEntity_QuantumForceTransformer::setCraftingTier,
                                    GregtechMetaTileEntity_QuantumForceTransformer::getCraftingTier)))
            .addElement(
                    'B',
                    withChannel(
                            "shielding",
                            StructureUtility.ofBlocksTiered(
                                    focusingTierConverter(),
                                    getAllFocusingTiers(),
                                    0,
                                    GregtechMetaTileEntity_QuantumForceTransformer::setFocusingTier,
                                    GregtechMetaTileEntity_QuantumForceTransformer::getFocusingTier)))
            .addElement('C', ofBlock(ModBlocks.blockCasings4Misc, 4))
            .addElement('D', ofBlock(ModBlocks.blockCasings2Misc, 12))
            .addElement('E', lazy(t -> ofBlock(t.getCasingBlock1(), t.getCasingMeta1())))
            .addElement(
                    'H',
                    buildHatchAdder(GregtechMetaTileEntity_QuantumForceTransformer.class)
                            .atLeast(InputBus, InputHatch, Maintenance, Energy.or(ExoticEnergy))
                            .casingIndex(TAE.getIndexFromPage(0, 10)).dot(4)
                            .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 12))))
            .addElement(
                    'T',
                    buildHatchAdder(GregtechMetaTileEntity_QuantumForceTransformer.class)
                            .atLeast(OutputBus, OutputHatch, Maintenance).casingIndex(TAE.getIndexFromPage(0, 10))
                            .dot(5)
                            .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 12))))
            .addElement(
                    'Z',
                    buildHatchAdder(GregtechMetaTileEntity_QuantumForceTransformer.class)
                            .hatchClass(GT_MetaTileEntity_Hatch_Input.class)
                            .adder(GregtechMetaTileEntity_QuantumForceTransformer::addNeptuniumHatch)
                            .casingIndex(TAE.getIndexFromPage(0, 10)).dot(5)
                            .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 12))))
            .addElement(
                    'X',
                    buildHatchAdder(GregtechMetaTileEntity_QuantumForceTransformer.class)
                            .hatchClass(GT_MetaTileEntity_Hatch_Input.class)
                            .adder(GregtechMetaTileEntity_QuantumForceTransformer::addFermiumHatch)
                            .casingIndex(TAE.getIndexFromPage(0, 10)).dot(5)
                            .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 12))))
            .build();

    public GregtechMetaTileEntity_QuantumForceTransformer(final int aID, final String aName,
            final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_QuantumForceTransformer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_QuantumForceTransformer(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Quantum Force Transformer").addInfo("Controller Block for the Quantum Force Transformer")
                .addInfo("Allows Complex chemical lines to be performed instantly in one step")
                .addInfo("Every recipe requires a catalyst, each catalyst adds 1 parallel and lasts forever")
                .addInfo("Accepts TecTech Energy and Laser Hatches")
                .addInfo("All inputs go on the bottom, all outputs go on the top")
                .addInfo("Put a circuit in the controller to specify the focused output")
                .addInfo("Check NEI to see the order of outputs, and which circuit number you need.")
                .addInfo("If separate input busses are enabled put the circuit in the circuit slot of the bus")
                .addInfo("Uses FocusTier*4*sqrt(parallels) Neptunium Plasma if focusing")
                .addInfo("Can use FocusTier*4*sqrt(parallels) Fermium Plasma for additional chance output")
                .addInfo("Use a screwdriver to enable Fluid mode")
                .addInfo(
                        "Fluid mode turns all possible outputs into their fluid variant, those which can't are left as they were.")
                .addInfo("This multi gets improved when all casings of some types are upgraded")
                .addInfo("Casing functions:")
                .addInfo("Pulse Manipulators: Recipe Tier Allowed (check NEI for the tier of each recipe)")
                .addInfo("Shielding Cores: Focusing Tier (equal to or higher than recipe tier to allow focus)")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(15, 21, 15, true)
                .addController("Bottom Center").addCasingInfoMin("Bulk Production Frame", 80, false)
                .addCasingInfoMin("Quantum Force Conductor", 177, false)
                .addCasingInfoMin("Force Field Glass", 224, false)
                .addCasingInfoMin("Neutron Pulse Manipulators", 233, false)
                .addCasingInfoMin("Neutron Shielding Cores", 142, false).addInputBus("Bottom Layer", 4)
                .addInputHatch("Bottom Layer", 4).addOutputHatch("Top Layer", 5).addOutputBus("Top Layer", 5)
                .addEnergyHatch("Bottom Layer", 4).addMaintenanceHatch("Bottom Layer", 4)
                .addStructureInfo("Neptunium Plasma Hatch: Left side of Controller")
                .addStructureInfo("Fermium Plasma Hatch: Right side of Controller").toolTipFinisher(
                        GT_Values.AuthorBlueWeabo + EnumChatFormatting.RESET
                                + EnumChatFormatting.GREEN
                                + " + Steelux"
                                + EnumChatFormatting.RESET
                                + " - [GT++]");
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_QuantumForceTransformer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mCasing = 0;
        if (!checkPiece(MAIN_PIECE, 7, 20, 4)) {
            return false;
        }

        if (mMaintenanceHatches.size() != 1 || mOutputBusses.size() < 1
                || mInputBusses.size() < 1
                || mInputHatches.size() < 1
                || mOutputHatches.size() < 1) {
            return false;
        }

        // Makes sure that the multi can accept only 1 TT Energy Hatch OR up to 2 Normal Energy Hatches. Deform if both
        // present or more than 1 TT Hatch.
        if (mExoticEnergyHatches.isEmpty() && mEnergyHatches.isEmpty()) {
            return false;
        }

        if (mExoticEnergyHatches.size() >= 1) {
            if (!mEnergyHatches.isEmpty()) {
                return false;
            }

            if (mExoticEnergyHatches.size() != 1) {
                return false;
            }
        }

        return mEnergyHatches.size() <= 2;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(MAIN_PIECE, stackSize, hintsOnly, 7, 20, 4);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(MAIN_PIECE, stackSize, 7, 20, 4, elementBudget, env, false, true);
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
                return -1;
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
            return -1;
        };
    }

    public static ITierConverter<Integer> focusingTierConverter() {
        return (block, meta) -> {
            if (block == null) {
                return -1;
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
            return -1;
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

    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(0, 10);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.quantumForceTransformerRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            private int[] chances;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (recipe.mSpecialValue > getCraftingTier()) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                }
                ItemStack catalyst = null;
                for (ItemStack item : recipe.mInputs) {
                    if (ItemUtils.isCatalyst(item)) {
                        catalyst = item;
                        break;
                    }
                }

                if (catalyst == null) {
                    return SimpleCheckRecipeResult.ofFailure("no_catalyst");
                }

                maxParallel = 0;
                for (ItemStack item : inputItems) {
                    if (ItemUtils.isCatalyst(item) && item.isItemEqual(catalyst)) {
                        maxParallel += item.stackSize;
                    }
                }

                doFermium = false;
                doNeptunium = false;

                if (recipe.mSpecialValue <= getFocusingTier()) {
                    if (mFermiumHatch != null && mFermiumHatch.getFluid() != null
                            && mFermiumHatch.getFluid().getFluid() != null
                            && mFermiumHatch.getFluid().getFluid().equals(mFermium)) {
                        doFermium = true;
                    }
                    if (mNeptuniumHatch != null && mNeptuniumHatch.getFluid() != null
                            && mNeptuniumHatch.getFluid().getFluid() != null
                            && mNeptuniumHatch.getFluid().getFluid().equals(mNeptunium)) {
                        doNeptunium = true;
                    }
                }

                chances = getOutputChances(recipe, doNeptunium ? findProgrammedCircuitNumber() : -1);

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
                return super.createParallelHelper(recipe).setCustomItemOutputCalculation(parallel -> {
                    ArrayList<ItemStack> items = new ArrayList<>();
                    if (mFluidMode) {
                        for (int i = 0; i < recipe.mOutputs.length; i++) {
                            ItemStack item = recipe.getOutput(i);
                            if (item == null) continue;
                            ItemData data = getAssociation(item);
                            Materials mat = data == null ? null : data.mMaterial.mMaterial;
                            if (mat != null) {
                                if (mat.getMolten(0) != null) {
                                    continue;
                                } else if (mat.getFluid(0) != null) {
                                    continue;
                                }
                            }
                            ItemStack itemToAdd = item.copy();
                            itemToAdd.stackSize = 0;
                            for (int j = 0; j < parallel; j++) {
                                if (getBaseMetaTileEntity().getRandomNumber(10000) < chances[i]) {
                                    itemToAdd.stackSize += item.stackSize;
                                }
                            }
                            if (itemToAdd.stackSize == 0) {
                                continue;
                            }
                            items.add(itemToAdd);
                        }
                    } else {
                        for (int i = 0; i < recipe.mOutputs.length; i++) {
                            ItemStack item = recipe.getOutput(i);
                            if (item == null) continue;
                            ItemStack itemToAdd = item.copy();
                            itemToAdd.stackSize = 0;
                            for (int j = 0; j < parallel; j++) {
                                if (getBaseMetaTileEntity().getRandomNumber(10000) < chances[i]) {
                                    itemToAdd.stackSize += item.stackSize;
                                }
                            }
                            if (itemToAdd.stackSize == 0) {
                                continue;
                            }
                            items.add(itemToAdd);
                        }
                    }

                    return items.toArray(new ItemStack[0]);
                }).setCustomFluidOutputCalculation(parallel -> {
                    ArrayList<FluidStack> fluids = new ArrayList<>();
                    if (mFluidMode) {
                        for (int i = 0; i < recipe.mOutputs.length; i++) {
                            ItemStack item = recipe.getOutput(i);
                            if (item == null) continue;
                            ItemData data = getAssociation(item);
                            Materials mat = data == null ? null : data.mMaterial.mMaterial;
                            if (mat == null) {
                                continue;
                            }
                            if (mat.getMolten(0) != null) {
                                FluidStack fluid = mat.getMolten(0);
                                for (int j = 0; j < parallel; j++) {
                                    if (getBaseMetaTileEntity().getRandomNumber(10000) < chances[i]) {
                                        fluid.amount += item.stackSize * INGOTS;
                                    }
                                }
                                if (fluid.amount == 0) {
                                    continue;
                                }
                                fluids.add(fluid);
                            } else if (mat.getFluid(0) != null) {
                                FluidStack fluid = mat.getFluid(0);
                                for (int j = 0; j < parallel; j++) {
                                    if (getBaseMetaTileEntity().getRandomNumber(10000) < chances[i]) {
                                        fluid.amount += item.stackSize * BUCKETS;
                                    }
                                }
                                if (fluid.amount == 0) {
                                    continue;
                                }
                                fluids.add(fluid);
                            }
                        }
                    }

                    for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                        FluidStack fluid = recipe.getFluidOutput(i);
                        if (fluid == null) continue;
                        FluidStack fluidToAdd = fluid.copy();
                        fluidToAdd.amount = 0;
                        for (int j = 0; j < parallel; j++) {
                            if (getBaseMetaTileEntity().getRandomNumber(10000) < chances[i + recipe.mOutputs.length]) {
                                fluidToAdd.amount += fluid.amount;
                            }
                        }
                        if (fluidToAdd.amount == 0) {
                            continue;
                        }
                        fluids.add(fluidToAdd);
                    }

                    return fluids.toArray(new FluidStack[0]);
                });
            }

            private int findProgrammedCircuitNumber() {
                if (isInputSeparationEnabled()) {
                    for (ItemStack stack : inputItems) {
                        if (ItemList.Circuit_Integrated.isStackEqual(stack)) {
                            return stack.getItemDamage() - 1;
                        }
                    }
                    return -1;
                } else {
                    final ItemStack controllerStack = getControllerSlot();
                    return controllerStack != null ? controllerStack.getItemDamage() - 1 : -1;
                }
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getAverageInputVoltage());
        logic.setAvailableAmperage(getMaxInputAmps());
    }

    private byte runningTick = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            criticalStopMachine();
            return false;
        }

        if (runningTick % 20 == 0) {
            int amount = (int) (getFocusingTier() * 4 * Math.sqrt(processingLogic.getCurrentParallels()));
            if (doFermium) {
                FluidStack tLiquid = mFermiumHatch.drain(amount, true);
                if (tLiquid == null || tLiquid.amount < amount) {
                    doFermium = false;
                    criticalStopMachine();
                    return false;
                }
            }

            if (doNeptunium) {
                FluidStack tLiquid = mNeptuniumHatch.drain(amount, true);
                if (tLiquid == null || tLiquid.amount < amount) {
                    doNeptunium = false;
                    criticalStopMachine();
                    return false;
                }
            }

            runningTick = 1;
        } else {
            runningTick++;
        }

        return true;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    private int[] getOutputChances(GT_Recipe tRecipe, int aChanceIncreased) {
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
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mFluidMode = !mFluidMode;
        GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("miscutils.machines.QFTFluidMode") + " " + mFluidMode);
    }

    public boolean addNeptuniumHatch(IGregTechTileEntity aTileEntity, short aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = null;
            mNeptuniumHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addFermiumHatch(IGregTechTileEntity aTileEntity, short aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = null;
            mFermiumHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public Block getCasingBlock1() {
        return ModBlocks.blockCasings5Misc;
    }

    public byte getCasingMeta1() {
        return 15;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setBoolean("mFluidMode", mFluidMode);
        aNBT.setBoolean("doFermium", doFermium);
        aNBT.setBoolean("doNeptunium", doNeptunium);
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
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { getCasingTexture(),
                    TextureFactory.builder().addIcon(getActiveOverlay()).extFacing().build() };
            return new ITexture[] { getCasingTexture(),
                    TextureFactory.builder().addIcon(getInactiveOverlay()).extFacing().build() };
        }
        return new ITexture[] { getCasingTexture() };
    }

    private ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(getCasingTextureId());
    }

    @SideOnly(Side.CLIENT)
    private void renderForceField(double x, double y, double z, int side, double minU, double maxU, double minV,
            double maxV) {
        // spotless:off
        Tessellator tes = Tessellator.instance;
        switch (side) {
            case 0 -> {
                tes.addVertexWithUV(x + 3, y, z + 7, maxU, maxV);
                tes.addVertexWithUV(x + 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x - 3, y + 4, z + 7, minU, minV);
                tes.addVertexWithUV(x - 3, y, z + 7, minU, maxV);
                tes.addVertexWithUV(x - 3, y, z + 7, minU, maxV);
                tes.addVertexWithUV(x - 3, y + 4, z + 7, minU, minV);
                tes.addVertexWithUV(x + 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x + 3, y, z + 7, maxU, maxV);
            }
            case 1 -> {
                tes.addVertexWithUV(x + 7, y, z + 4, maxU, maxV);
                tes.addVertexWithUV(x + 7, y + 4, z + 4, maxU, minV);
                tes.addVertexWithUV(x + 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x + 7, y, z - 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y, z - 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x + 7, y + 4, z + 4, maxU, minV);
                tes.addVertexWithUV(x + 7, y, z + 4, maxU, maxV);
            }
            case 2 -> {
                tes.addVertexWithUV(x + 3, y, z - 7, maxU, maxV);
                tes.addVertexWithUV(x + 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x - 3, y + 4, z - 7, minU, minV);
                tes.addVertexWithUV(x - 3, y, z - 7, minU, maxV);
                tes.addVertexWithUV(x - 3, y, z - 7, minU, maxV);
                tes.addVertexWithUV(x - 3, y + 4, z - 7, minU, minV);
                tes.addVertexWithUV(x + 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x + 3, y, z - 7, maxU, maxV);
            }
            case 3 -> {
                tes.addVertexWithUV(x - 7, y, z + 4, maxU, maxV);
                tes.addVertexWithUV(x - 7, y + 4, z + 4, maxU, minV);
                tes.addVertexWithUV(x - 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x - 7, y, z - 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y, z - 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x - 7, y + 4, z + 4, maxU, minV);
                tes.addVertexWithUV(x - 7, y, z + 4, maxU, maxV);
            }
            case 4 -> {
                tes.addVertexWithUV(x - 3, y, z + 7, maxU, maxV);
                tes.addVertexWithUV(x - 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x - 7, y + 4, z + 4, minU, minV);
                tes.addVertexWithUV(x - 7, y, z + 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y, z + 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y + 4, z + 4, minU, minV);
                tes.addVertexWithUV(x - 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x - 3, y, z + 7, maxU, maxV);
            }
            case 5 -> {
                tes.addVertexWithUV(x - 3, y, z - 7, maxU, maxV);
                tes.addVertexWithUV(x - 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x - 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x - 7, y, z - 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y, z - 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x - 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x - 3, y, z - 7, maxU, maxV);
            }
            case 6 -> {
                tes.addVertexWithUV(x + 3, y, z + 7, maxU, maxV);
                tes.addVertexWithUV(x + 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x + 7, y + 4, z + 4, minU, minV);
                tes.addVertexWithUV(x + 7, y, z + 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y, z + 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y + 4, z + 4, minU, minV);
                tes.addVertexWithUV(x + 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x + 3, y, z + 7, maxU, maxV);
            }
            case 7 -> {
                tes.addVertexWithUV(x + 3, y, z - 7, maxU, maxV);
                tes.addVertexWithUV(x + 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x + 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x + 7, y, z - 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y, z - 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x + 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x + 3, y, z - 7, maxU, maxV);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderInWorld(IBlockAccess aWorld, int x, int y, int z, Block block, RenderBlocks renderer) {
        Tessellator tes = Tessellator.instance;
        IIcon forceField = TexturesGtBlock.ForceField.getIcon();
        if (getBaseMetaTileEntity().isActive()) {
            double minU = forceField.getMinU();
            double maxU = forceField.getMaxU();
            double minV = forceField.getMinV();
            double maxV = forceField.getMaxV();
            double xBaseOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetX;
            double zBaseOffset = 3 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
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
            renderForceField(x + xBaseOffset + 0.5, y, z + zBaseOffset + 0.5, 0, minU, maxU, minV, maxV);
            renderForceField(x + xBaseOffset + 0.5, y, z + zBaseOffset + 0.5, 1, minU, maxU, minV, maxV);
            renderForceField(x + xBaseOffset + 0.5, y, z + zBaseOffset + 0.5, 2, minU, maxU, minV, maxV);
            renderForceField(x + xBaseOffset + 0.5, y, z + zBaseOffset + 0.5, 3, minU, maxU, minV, maxV);
            renderForceField(x + xBaseOffset + 0.5, y, z + zBaseOffset + 0.5, 4, minU, maxU, minV, maxV);
            renderForceField(x + xBaseOffset + 0.5, y, z + zBaseOffset + 0.5, 5, minU, maxU, minV, maxV);
            renderForceField(x + xBaseOffset + 0.5, y, z + zBaseOffset + 0.5, 6, minU, maxU, minV, maxV);
            renderForceField(x + xBaseOffset + 0.5, y, z + zBaseOffset + 0.5, 7, minU, maxU, minV, maxV);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
            
        }
        // Needs to be false to render the controller
        return false;
        //spotless:on
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
