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
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
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
                                    "   AA     AA   ", " AA         AA ", "BAA          AB", "B             B",
                                    "A             A", "A             A", "A             A", "A             A",
                                    "A             A" },
                            { "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "               ",
                                    "               ", "               ", "               ", "      BAB      ",
                                    "    AA   AA    ", "  AA       AA  ", " BA         AB ", " B           B ",
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
        return new ArrayList<Pair<Block, Integer>>() {

            {
                add(Pair.of(ModBlocks.blockCasings5Misc, 7));
                add(Pair.of(ModBlocks.blockCasings5Misc, 8));
                add(Pair.of(ModBlocks.blockCasings5Misc, 9));
                add(Pair.of(ModBlocks.blockCasings5Misc, 10));
            }
        };
    }

    public static List<Pair<Block, Integer>> getAllFocusingTiers() {
        return new ArrayList<Pair<Block, Integer>>() {

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
                    case 7:
                        return 1;
                    case 8:
                        return 2;
                    case 9:
                        return 3;
                    case 10:
                        return 4;
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
                    case 11:
                        return 1;
                    case 12:
                        return 2;
                    case 13:
                        return 3;
                    case 14:
                        return 4;
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
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTPP_Recipe.GTPP_Recipe_Map.sQuantumForceTransformerRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return true;
    }

    private int mCurrentParallel = 0;

    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        mCurrentParallel = 0;
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = null;
        this.mOutputFluids = null;
        doFermium = false;
        doNeptunium = false;
        FluidStack[] tFluidList = getStoredFluids().toArray(new FluidStack[0]);
        if (inputSeparation) {
            ArrayList<ItemStack> tInputList = new ArrayList<>();
            for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
                for (int i = tBus.getSizeInventory() - 1; i >= 0; i--) {
                    if (tBus.getStackInSlot(i) != null) {
                        tInputList.add(tBus.getStackInSlot(i));
                    }
                }

                ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
                if (processRecipe(tInputs, tFluidList, getRecipeMap(), tBus.mInventory[tBus.getCircuitSlot()])) {
                    return true;
                } else tInputList.clear();
            }
        } else {
            ItemStack[] tInputList = getStoredInputs().toArray(new ItemStack[0]);
            return processRecipe(tInputList, tFluidList, getRecipeMap(), aStack);
        }
        this.mEfficiency = 0;
        this.mEfficiencyIncrease = 0;
        return false;
    }

    private boolean processRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
            GT_Recipe.GT_Recipe_Map aRecipeMap, ItemStack aStack) {
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(getAverageInputVoltage()));
        GT_Recipe tRecipe = aRecipeMap.findRecipe(
                getBaseMetaTileEntity(),
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                aFluidInputs,
                aItemInputs);

        if (tRecipe != null && tRecipe.mSpecialValue <= getCraftingTier()) {
            ItemStack aRecipeCatalyst = null;
            for (ItemStack tItem : tRecipe.mInputs) {
                if (ItemUtils.isCatalyst(tItem)) {
                    aRecipeCatalyst = tItem;
                    break;
                }
            }

            if (aRecipeCatalyst == null) {
                return false;
            }

            int mCurrentMaxParallel = 0;
            int mMaxParallel = 64;
            for (ItemStack tItem : aItemInputs) {
                if (ItemUtils.isCatalyst(tItem) && tItem.isItemEqual(aRecipeCatalyst)) {
                    mCurrentMaxParallel += tItem.stackSize;
                }

                if (mCurrentMaxParallel >= mMaxParallel) {
                    mCurrentMaxParallel = mMaxParallel;
                    break;
                }
            }

            if (mFermiumHatch != null && tRecipe.mSpecialValue <= getFocusingTier()) {
                doFermium = mFermiumHatch.getFluid() != null
                        && mFermiumHatch.getFluid().isFluidEqual(new FluidStack(mFermium, 1));
            } else {
                doFermium = false;
            }

            GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                    .setFluidInputs(aFluidInputs).setAvailableEUt(getMaxInputAmps() * getAverageInputVoltage())
                    .setMaxParallel(mCurrentMaxParallel).setController(this).enableConsumption();

            if (batchMode) {
                helper.enableBatchMode(128);
            }

            helper.build();

            if (helper.getCurrentParallel() == 0) {
                return false;
            }

            GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(tRecipe.mEUt)
                    .setEUt(getAverageInputVoltage()).setAmperage(getMaxInputAmps()).setDuration(tRecipe.mDuration)
                    .setParallel(Math.min(mMaxParallel, helper.getCurrentParallel())).calculate();
            lEUt = -calculator.getConsumption();
            mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplier());

            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;

            if (mMaxProgresstime == Integer.MAX_VALUE - 1 || lEUt == Long.MAX_VALUE - 1) return false;

            if (this.lEUt > 0) {
                this.lEUt = (-this.lEUt);
            }

            int[] tChances;
            if (aStack == null || aStack.getItemDamage() == 0
                    || mNeptuniumHatch.getFluid() == null
                    || !mNeptuniumHatch.getFluid().isFluidEqual(new FluidStack(mNeptunium, 1))
                    || tRecipe.mSpecialValue > getFocusingTier()) {
                doNeptunium = false;
                tChances = GetChanceOutputs(tRecipe, -1);
            } else {
                doNeptunium = true;
                tChances = GetChanceOutputs(tRecipe, aStack.getItemDamage() - 1);
            }

            ArrayList<ItemStack> tItemOutputs = new ArrayList<>();
            ArrayList<FluidStack> tFluidOutputs = new ArrayList<>();
            mCurrentParallel = helper.getCurrentParallel();

            if (mFluidMode) {
                for (int i = 0; i < tChances.length; i++) {
                    if (getBaseMetaTileEntity().getRandomNumber(10000) < tChances[i]) {
                        ItemData data = getAssociation(tRecipe.getOutput(i));
                        Materials mat = data == null ? null : data.mMaterial.mMaterial;
                        if (i < tRecipe.mOutputs.length) {
                            if (mat != null) {
                                if (mat.getMolten(0) != null) {
                                    tFluidOutputs.add(
                                            mat.getMolten(tRecipe.getOutput(i).stackSize * 144L * mCurrentParallel));
                                } else if (mat.getFluid(0) != null) {
                                    tFluidOutputs.add(
                                            mat.getFluid(tRecipe.getOutput(i).stackSize * 1000L * mCurrentParallel));
                                } else {
                                    ItemStack aItem = tRecipe.getOutput(i);
                                    tItemOutputs.add(
                                            GT_Utility.copyAmountUnsafe(
                                                    (long) aItem.stackSize * mCurrentParallel,
                                                    aItem));
                                }
                            } else {
                                ItemStack aItem = tRecipe.getOutput(i);
                                tItemOutputs.add(
                                        GT_Utility.copyAmountUnsafe((long) aItem.stackSize * mCurrentParallel, aItem));
                            }
                        } else {
                            FluidStack aFluid = tRecipe.getFluidOutput(i - tRecipe.mOutputs.length);
                            tFluidOutputs.add(new FluidStack(aFluid, aFluid.amount * mCurrentParallel));
                        }
                    }
                }
            } else {
                for (int i = 0; i < tChances.length; i++) {
                    if (getBaseMetaTileEntity().getRandomNumber(10000) < tChances[i]) {
                        if (i < tRecipe.mOutputs.length) {
                            ItemStack aItem = tRecipe.getOutput(i).copy();
                            aItem.stackSize *= mCurrentParallel;
                            tItemOutputs.add(aItem);
                        } else {
                            FluidStack aFluid = tRecipe.getFluidOutput(i - tRecipe.mOutputs.length).copy();
                            aFluid.amount *= mCurrentParallel;
                            tFluidOutputs.add(aFluid);
                        }
                    }
                }
            }

            mOutputItems = tItemOutputs.toArray(new ItemStack[0]);
            mOutputFluids = tFluidOutputs.toArray(new FluidStack[0]);
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
            updateSlots();

            return true;
        }
        return false;
    }

    private byte runningTick = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            criticalStopMachine();
            return false;
        }

        if (runningTick % 20 == 0) {
            if (doFermium) {
                FluidStack tFluid = new FluidStack(
                        mFermium,
                        (int) (getFocusingTier() * 4 * Math.sqrt(mCurrentParallel)));
                FluidStack tLiquid = mFermiumHatch.drain(tFluid.amount, true);
                if (tLiquid == null || tLiquid.amount < tFluid.amount) {
                    doFermium = false;
                    criticalStopMachine();
                    return false;
                }
            }

            if (doNeptunium) {
                FluidStack tFluid = new FluidStack(
                        mNeptunium,
                        (int) (getFocusingTier() * 4 * Math.sqrt(mCurrentParallel)));
                FluidStack tLiquid = mNeptuniumHatch.drain(tFluid.amount, true);
                if (tLiquid == null || tLiquid.amount < tFluid.amount) {
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

    public int getMaxParallelRecipes() {
        return 64;
    }

    public int getEuDiscountForParallelism() {
        return 0;
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

    public int getAmountOfOutputs() {
        return 2;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    private int[] GetChanceOutputs(GT_Recipe tRecipe, int aChanceIncreased) {
        int difference = getFocusingTier() - tRecipe.mSpecialValue;
        int aOutputsAmount = tRecipe.mOutputs.length + tRecipe.mFluidOutputs.length;
        int aChancePerOutput = 10000 / aOutputsAmount;
        int[] tChances = new int[aOutputsAmount];
        Arrays.fill(tChances, aChancePerOutput);

        switch (difference) {
            case 0:
                for (int i = 0; i < tChances.length; i++) {
                    if (doNeptunium) {
                        if (i == aChanceIncreased) {
                            tChances[i] = aChancePerOutput / 2 * (aOutputsAmount - 1);
                        } else {
                            tChances[i] /= 2;
                        }
                    }

                    if (doFermium) {
                        tChances[i] += (10000 - tChances[i]) / 4;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < tChances.length; i++) {
                    if (doNeptunium) {
                        if (i == aChanceIncreased) {
                            tChances[i] = aChancePerOutput * 3 / 4 * (aOutputsAmount - 1);
                        } else {
                            tChances[i] /= 4;
                        }
                    }

                    if (doFermium) {
                        tChances[i] += (10000 - tChances[i]) / 3;
                    }
                }
                break;
            case 2:
            case 3:
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
                break;
        }
        return tChances;
    }

    public boolean onWireCutterRightclick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
            float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        inputSeparation = !inputSeparation;
        GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
        return true;
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
            case 0:
                tes.addVertexWithUV(x + 3, y    , z + 7, maxU, maxV);
                tes.addVertexWithUV(x + 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x - 3, y + 4, z + 7, minU, minV);
                tes.addVertexWithUV(x - 3, y    , z + 7, minU, maxV);
                tes.addVertexWithUV(x - 3, y    , z + 7, minU, maxV);
                tes.addVertexWithUV(x - 3, y + 4, z + 7, minU, minV);
                tes.addVertexWithUV(x + 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x + 3, y    , z + 7, maxU, maxV);
                break;
            case 1:
                tes.addVertexWithUV(x + 7, y    , z + 4, maxU, maxV);
                tes.addVertexWithUV(x + 7, y + 4, z + 4, maxU, minV);
                tes.addVertexWithUV(x + 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x + 7, y    , z - 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y    , z - 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x + 7, y + 4, z + 4, maxU, minV);
                tes.addVertexWithUV(x + 7, y    , z + 4, maxU, maxV);
                break;
            case 2:
                tes.addVertexWithUV(x + 3, y    , z - 7, maxU, maxV);
                tes.addVertexWithUV(x + 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x - 3, y + 4, z - 7, minU, minV);
                tes.addVertexWithUV(x - 3, y    , z - 7, minU, maxV);
                tes.addVertexWithUV(x - 3, y    , z - 7, minU, maxV);
                tes.addVertexWithUV(x - 3, y + 4, z - 7, minU, minV);
                tes.addVertexWithUV(x + 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x + 3, y    , z - 7, maxU, maxV);
                break;
            case 3:
                tes.addVertexWithUV(x - 7, y    , z + 4, maxU, maxV);
                tes.addVertexWithUV(x - 7, y + 4, z + 4, maxU, minV);
                tes.addVertexWithUV(x - 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x - 7, y    , z - 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y    , z - 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x - 7, y + 4, z + 4, maxU, minV);
                tes.addVertexWithUV(x - 7, y    , z + 4, maxU, maxV); 
                break;
            case 4:
                tes.addVertexWithUV(x - 3, y    , z + 7, maxU, maxV);
                tes.addVertexWithUV(x - 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x - 7, y + 4, z + 4, minU, minV);
                tes.addVertexWithUV(x - 7, y    , z + 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y    , z + 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y + 4, z + 4, minU, minV);
                tes.addVertexWithUV(x - 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x - 3, y    , z + 7, maxU, maxV);
                break;
            case 5:
                tes.addVertexWithUV(x - 3, y    , z - 7, maxU, maxV);
                tes.addVertexWithUV(x - 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x - 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x - 7, y    , z - 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y    , z - 4, minU, maxV);
                tes.addVertexWithUV(x - 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x - 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x - 3, y    , z - 7, maxU, maxV);
                break;
            case 6:
                tes.addVertexWithUV(x + 3, y    , z + 7, maxU, maxV);
                tes.addVertexWithUV(x + 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x + 7, y + 4, z + 4, minU, minV);
                tes.addVertexWithUV(x + 7, y    , z + 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y    , z + 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y + 4, z + 4, minU, minV);
                tes.addVertexWithUV(x + 3, y + 4, z + 7, maxU, minV);
                tes.addVertexWithUV(x + 3, y    , z + 7, maxU, maxV);
                break;
            case 7:
                tes.addVertexWithUV(x + 3, y    , z - 7, maxU, maxV);
                tes.addVertexWithUV(x + 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x + 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x + 7, y    , z - 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y    , z - 4, minU, maxV);
                tes.addVertexWithUV(x + 7, y + 4, z - 4, minU, minV);
                tes.addVertexWithUV(x + 3, y + 4, z - 7, maxU, minV);
                tes.addVertexWithUV(x + 3, y    , z - 7, maxU, maxV);
                break;
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
