package gregtech.common.tileentities.machines.multi.Solidifier;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import ggfab.api.GGFabRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.objects.GTDualInputPattern;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;

enum Modules {
    EFFICIENT_OC,
    POWER_EFFICIENT_SUBSYSTEMS,
    TRANSCENDENT_REINFORCEMENT,
    EXTRA_CASTING_BASINS,
    HYPERCOOLER,
    STREAMLINED_CASTERS
}

public class MTEModularSolidifier extends MTEExtendedPowerMultiBlockBase<MTEModularSolidifier>
    implements ISurvivalConstructable {

    private static int horizontaloffset = 4;
    private static int verticaloffset = 2;

    private final float speedmodifierbase = 2F;
    private final float EUEFFbase = 0.9F;
    private final int parallelscalebase = 8;

    // offsets, for building the structure, redirect to build the bottom left corner of the structure piece at
    // Controller pos + offsets.
    private int[] module1Offset = { 3, 3, -2 };
    private int[] module2Offset = { 3, 3, -6 };
    private int[] module3Offset = { 3, 3, -10 };
    private int[] module4Offset = { 3, 3, -14 };
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_HYPERCOOLER = "hypercooler";
    private static final String STRUCTURE_EFFICIENT_OC = "efficientOC";
    private static final String STRUCTURE_TRANSCENDENT_REINFORCEMENT = "transcendent_reinforcement";
    private static final String STRUCTURE_EXTRA_CASTING_BASINS = "extra_casting_basins";
    private static final String STRUCTURE_STREAMLINED_CASTERS = "streamlined_casters";

    // Hypercooler is limited to 1, either dont read the second one or strucure check fail

    private static final IStructureDefinition<MTEModularSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEModularSolidifier>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(
                new String[][]{
                    {"         ","         ","d       d","d       d","d       d","         ","d       d","d       d","d       d","         ","d       d","d       d","d       d","         ","d       d","d       d","d       d","         ","         "},
                    {" ABBBBBA ","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A"," ABBBBBA "},
                    {" AAA~AAA ","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A"," AAAAAAA "},
                    {" AAAAAAA ","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA"," AAAAAAA "}
                }))
        .addShape(STRUCTURE_TRANSCENDENT_REINFORCEMENT,transpose(new String[][]{
            {"CCCDCCC","CCCDCCC","CCCDCCC"}}
        ))
        .addShape(STRUCTURE_HYPERCOOLER, transpose(new String[][]{
            {"EEEFEEE","EEFGFEE","EEEFEEE"}
        }))
        //spotless:on
        .addElement(
            'A',
            buildHatchAdder(MTEModularSolidifier.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(15))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTEModularSolidifier::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings13, 15))))
        .addElement('B', chainAllGlasses())
        .addElement('C', ofBlock(GregTechAPI.sBlockMetal9, 4))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings13, 11))
        .addElement('d', ofBlock(GregTechAPI.sBlockCasings10, 2))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings5, 11))
        .addElement('F', ofBlock(GregTechAPI.sBlockGlass1, 3))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings13, 13))
        .build();

    public MTEModularSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEModularSolidifier(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEModularSolidifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModularSolidifier(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 15)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier, Tool Casting Machine")
            .addInfo("100% faster than singleblock machines of the same voltage")
            .addInfo("Gains 12 parallels per voltage tier")
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .addOutputHatch("Any Wooden Casing", 1)
            .addEnergyHatch("Any Wooden Casing", 1)
            .addMaintenanceHatch("Any Wooden Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontaloffset, verticaloffset, 0);
        buildPiece(
            STRUCTURE_TRANSCENDENT_REINFORCEMENT,
            stackSize,
            hintsOnly,
            module1Offset[0],
            module1Offset[1],
            module1Offset[2]);
        buildPiece(STRUCTURE_HYPERCOOLER, stackSize, hintsOnly, module2Offset[0], module2Offset[1], module2Offset[2]);
        buildPiece(
            STRUCTURE_TRANSCENDENT_REINFORCEMENT,
            stackSize,
            hintsOnly,
            module3Offset[0],
            module3Offset[1],
            module3Offset[2]);
        buildPiece(STRUCTURE_HYPERCOOLER, stackSize, hintsOnly, module4Offset[0], module4Offset[1], module4Offset[2]);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = 0;
        built += survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontaloffset,
            verticaloffset,
            0,
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            STRUCTURE_TRANSCENDENT_REINFORCEMENT,
            stackSize,
            module1Offset[0],
            module1Offset[1],
            module1Offset[2],
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            STRUCTURE_HYPERCOOLER,
            stackSize,
            module2Offset[0],
            module2Offset[1],
            module1Offset[2],
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            STRUCTURE_TRANSCENDENT_REINFORCEMENT,
            stackSize,
            module3Offset[0],
            module3Offset[1],
            module3Offset[2],
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            STRUCTURE_HYPERCOOLER,
            stackSize,
            module4Offset[0],
            module4Offset[1],
            module4Offset[2],
            elementBudget,
            env,
            false,
            true);
        return built;
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontaloffset, verticaloffset, 0) && mCasingAmount >= 14;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            RecipeMap<?> currentRecipeMap = RecipeMaps.fluidSolidifierRecipes;

            // Override is needed so that switching recipe maps does not stop recipe locking.
            @Override
            protected RecipeMap<?> getCurrentRecipeMap() {
                lastRecipeMap = currentRecipeMap;
                return currentRecipeMap;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                currentRecipeMap = RecipeMaps.fluidSolidifierRecipes;
                CheckRecipeResult result = super.process();
                if (result.wasSuccessful()) return result;

                currentRecipeMap = GGFabRecipeMaps.toolCastRecipes;
                return super.process();
            }

            // for cribuffers/proxies?
            @Override
            public boolean tryCachePossibleRecipesFromPattern(IDualInputInventoryWithPattern inv) {
                if (dualInvWithPatternToRecipeCache.containsKey(inv)) {
                    activeDualInv = inv;
                    return true;
                }

                GTDualInputPattern inputs = inv.getPatternInputs();
                setInputItems(inputs.inputItems);
                setInputFluids(inputs.inputFluid);
                Set<GTRecipe> recipes = findRecipeMatches(RecipeMaps.fluidSolidifierRecipes)
                    .collect(Collectors.toSet());
                if (recipes.isEmpty())
                    recipes = findRecipeMatches(GGFabRecipeMaps.toolCastRecipes).collect(Collectors.toSet());
                if (!recipes.isEmpty()) {
                    dualInvWithPatternToRecipeCache.put(inv, recipes);
                    activeDualInv = inv;
                    return true;
                }
                return false;
            }

        }.setSpeedBonus(1F / speedmodifierbase)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (parallelscalebase * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public @NotNull Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.fluidSolidifierRecipes, GGFabRecipeMaps.toolCastRecipes);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidSolidifierRecipes;
    }

    /*
     * things to consider with processing math
     * Things get added and multiplied(parallel,eu/t, speed bonus)
     * Order of operations: ADD/SUB First, MUL/DIV After
     * OC Factor changes (overclock calculator can deal with this)
     * Hypercooler adds OC's based on fluid supplied
     */
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

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    // mui2 stuff

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    protected @NotNull MTEModularSolidifierGui getGui() {
        return new MTEModularSolidifierGui(this);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }
}
