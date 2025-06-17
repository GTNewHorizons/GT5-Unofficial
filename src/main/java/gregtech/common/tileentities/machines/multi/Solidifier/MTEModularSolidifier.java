package gregtech.common.tileentities.machines.multi.Solidifier;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
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

    UNSET("Unset", "Unset", ""),
    ACTIVE_TIME_DILATION_SYSTEM("Active Time Dilation System", "A.T.D.S", "atds"),
    EFFICIENT_OC("Efficient Overclocking System", "E.O.C", "eff_oc"),
    POWER_EFFICIENT_SUBSYSTEMS("Power Efficient Subsytems", "P.E.S", "power_efficient_subsystems"),
    TRANSCENDENT_REINFORCEMENT("Transcendent Reinforcement", "TrRe", "transcendent_reinforcement"),
    EXTRA_CASTING_BASINS("Extra Casting Basins", "E.C.B", "extra_casting_basins"),
    HYPERCOOLER("Hypercooler", "HC", "hypercooler"),
    STREAMLINED_CASTERS("Streamlined Casters", "S.L.C", "streamlined_casters");

    public final String displayName;
    public final String shorthand;
    public final String structureID;

    Modules(String display, String shortname, String structid) {
        this.displayName = display;
        this.shorthand = shortname;
        this.structureID = structid;
    }
}

public class MTEModularSolidifier extends MTEExtendedPowerMultiBlockBase<MTEModularSolidifier>
    implements ISurvivalConstructable {

    private static int horizontalOffset = 4;
    private static int verticalOffset = 2;

    private int mTier = 3; // 1 - base , 2 - ~UEV, 3 - ~UMV
    private final float speedModifierBase = 2F;
    private final float euEffBase = 0.9F;
    private final int parallelScaleBase = 8;
    private final float ocFactorBase = 2.0F;

    private boolean uevRecipesEnabled = false;
    private boolean hypercoolerPresent = false;

    // modified values for display and calculations
    private float ocFactorAdditive = 0.0F;
    private float speedAdditive = 0.0F;
    private float speedMultiplier = 1.0F;
    private float speedModifierAdj = speedModifierBase;

    private float euEffAdditive = 0.0F;
    private float euEffMultiplier = 1.0F;
    private float euEffAdj = euEffBase;

    private int parallelScaleAdditive = 0;
    private float parallelScaleMultiplier = 1.0F;
    private float parallelScaleAdj = parallelScaleBase;

    // offsets, for building the structure, redirect to build the bottom left corner of the structure piece at
    // Controller pos + offsets.
    private Modules[] lookupArray = Modules.values();
    private Modules[] modules = new Modules[4];
    private int[] module1Offset = { 3, 3, -2 };
    private int[] module2Offset = { 3, 3, -6 };
    private int[] module3Offset = { 3, 3, -10 };
    private int[] module4Offset = { 3, 3, -14 };
    private static final String STRUCTURE_PIECE_MAIN = "main";
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
        .addShape(Modules.TRANSCENDENT_REINFORCEMENT.structureID, transpose(new String[][]{
            {"CCCDCCC","CCCDCCC","CCCDCCC"}}
        ))
        .addShape(Modules.HYPERCOOLER.structureID, transpose(new String[][]{
            {"EEEFEEE","EEFGFEE","EEEFEEE"}
        }))
        //spotless:on
        .addElement(
            'A',
            buildHatchAdder(MTEModularSolidifier.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, ExoticEnergy)
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
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffset, verticalOffset, 0);
        buildPiece(
            Modules.TRANSCENDENT_REINFORCEMENT.structureID,
            stackSize,
            hintsOnly,
            module1Offset[0],
            module1Offset[1],
            module1Offset[2]);
        buildPiece(
            Modules.HYPERCOOLER.structureID,
            stackSize,
            hintsOnly,
            module2Offset[0],
            module2Offset[1],
            module2Offset[2]);
        buildPiece(
            Modules.TRANSCENDENT_REINFORCEMENT.structureID,
            stackSize,
            hintsOnly,
            module3Offset[0],
            module3Offset[1],
            module3Offset[2]);
        buildPiece(
            Modules.HYPERCOOLER.structureID,
            stackSize,
            hintsOnly,
            module4Offset[0],
            module4Offset[1],
            module4Offset[2]);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = 0;
        built += survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffset,
            verticalOffset,
            0,
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            Modules.TRANSCENDENT_REINFORCEMENT.structureID,
            stackSize,
            module1Offset[0],
            module1Offset[1],
            module1Offset[2],
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            Modules.HYPERCOOLER.structureID,
            stackSize,
            module2Offset[0],
            module2Offset[1],
            module1Offset[2],
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            Modules.TRANSCENDENT_REINFORCEMENT.structureID,
            stackSize,
            module3Offset[0],
            module3Offset[1],
            module3Offset[2],
            elementBudget,
            env,
            false,
            true);
        built += survivalBuildPiece(
            Modules.HYPERCOOLER.structureID,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, 0) && mCasingAmount >= 14;
    }

    private void resetParameters() {
        ocFactorAdditive = 0.0F;

        speedAdditive = 0.0F;
        speedMultiplier = 1.0F;

        euEffAdditive = 0.0F;
        euEffMultiplier = 1.0F;

        parallelScaleAdditive = 0;
        parallelScaleMultiplier = 1.0F;

        hypercoolerPresent = false;
        uevRecipesEnabled = false;
    }

    public void checkModules() {
        resetParameters();
        // loop through each module. based on tier. 2 - 4 modules.
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            Modules checkedModule = modules[i];
            switch (checkedModule) {
                case UNSET:
                    break;
                case HYPERCOOLER:
                    hypercoolerPresent = true;
                    break;
                case TRANSCENDENT_REINFORCEMENT:
                    uevRecipesEnabled = true;
                    break;
                case EFFICIENT_OC:
                    ocFactorAdditive += 0.2F;
                    break;
                case ACTIVE_TIME_DILATION_SYSTEM:
                    euEffMultiplier *= 6;
                    speedMultiplier *= 6;
                    break;
                case STREAMLINED_CASTERS:
                    parallelScaleMultiplier *= 0.5F;
                    speedMultiplier *= 1.5F;
                    break;
                case EXTRA_CASTING_BASINS:
                    parallelScaleAdditive += 12;
                    break;
                case POWER_EFFICIENT_SUBSYSTEMS:
                    euEffAdditive -= 0.2F;
                    speedMultiplier *= (2F / 3F);
                    break;
            }
        }
        calculateNewStats();
    }

    private void calculateNewStats() {
        parallelScaleAdj = (parallelScaleBase + parallelScaleAdditive) * parallelScaleMultiplier;
        speedModifierAdj = (speedModifierBase + speedAdditive) * speedMultiplier;
        euEffAdj = (euEffBase + euEffAdditive) * euEffMultiplier;

    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        checkModules();
        logic.setSpeedBonus(1F / speedMultiplier);
        logic.setMaxParallel((int) (Math.floor(parallelScaleAdj) * GTUtility.getTier(this.getMaxInputVoltage())));
        logic.setEuModifier(euEffAdj);
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips(); //this might cause an issue later, idk the difference between OC and tier skip.
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

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {

                //TODO: check for fluid if hyppercooler present and if recipe is greater than uev
                return super.validateRecipe(recipe);
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

        };
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

    // getters/setters for mui syncing
    public String[] getModuleNames(int index) {
        // just in case
        if (index > Modules.values().length - 1) index = 0;

        Modules modulegiven = modules[index];
        return new String[] { modulegiven.displayName, modulegiven.shorthand, modulegiven.structureID };
    }

    public void setModule(int index, int ordinal) {
        // just in case, shouldn't be possible
        if (index > modules.length - 1 || ordinal > lookupArray.length - 1) return;
        Modules moduleToAdd = lookupArray[index];
        if (moduleToAdd == Modules.HYPERCOOLER) {
            checkModules();
            if (hypercoolerPresent) return;
        }
        modules[index] = moduleToAdd;
    }

    public String getSpeedStr() {
        return (speedModifierAdj - 1) * 100 + "%";
    }

    public double getOCFactor() {
        double oc = 2.0;
        for (Modules m : modules) {
            if (m == Modules.EFFICIENT_OC) oc += 0.2;
        }
        return oc;
    }
}
