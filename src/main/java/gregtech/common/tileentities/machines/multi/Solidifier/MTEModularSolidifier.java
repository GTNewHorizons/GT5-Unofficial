package gregtech.common.tileentities.machines.multi.Solidifier;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
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
import static gregtech.api.util.GTUtility.getTier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import bartworks.common.tileentities.multis.mega.MTEMegaVacuumFreezer;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.OverclockCalculator;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import net.minecraftforge.fluids.FluidStack;
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

import javax.annotation.Nonnull;

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

    private static class CoolingFluid{
        public Materials material;
        public int grantedOC;
        public int amount;

        public CoolingFluid(Materials material, int grantedOC, int amount)
        {
            this.material = material;
            this.grantedOC = grantedOC;
            this.amount = amount;
        }
        public FluidStack getStack()
        {
            FluidStack stack = material.getFluid(amount);
            //shoutout penguin, i think i get why you were upset with this code here :^)
            if (stack == null){
                return material.getMolten(amount);
            }
            return stack;
        }
    }

    private static final ArrayList<CoolingFluid> COOLING_FLUIDS = new ArrayList<>(Arrays.asList
        (new CoolingFluid(MaterialsUEVplus.SpaceTime,1,100),
        new CoolingFluid(MaterialsUEVplus.Space, 2, 50),
        new CoolingFluid(MaterialsUEVplus.Eternity, 3, 25)));

    private CoolingFluid currentCoolingFluid = null;
    private static int horizontalOffset = 4;
    private static int verticalOffset = 2;

    private int mTier = 3; // 1 - base , 2 - ~UEV, 3 - ~UMV
    private final float speedModifierBase = 2F;
    private final float euEffBase = 0.9F;
    private final int parallelScaleBase = 8;
    private final float ocFactorBase = 2.0F;
    private int additionaloverclocks = 0;
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
    private Modules[] modules = {lookupArray[4],lookupArray[4],lookupArray[4],lookupArray[4]};

    //these are all the same as of right now, but MAY change with diff structure
    private int[] moduleHorizontalOffsets = {3,3,3,3};
    private int[] moduleVerticalOffsets = {3,3,3,3};
    private int[] moduleDepthOffsets = {-2,-6,-10,-14};
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
        .addShape(Modules.UNSET.structureID, transpose(new String[][]{
            {"       ","       ","       "}
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
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            Modules m = modules[i];
            if (m != Modules.UNSET)
            {
                buildPiece(
                    m.structureID,
                    stackSize,
                    hintsOnly,
                    moduleHorizontalOffsets[i],
                    moduleVerticalOffsets[i],
                    moduleDepthOffsets[i]);
            }
        }
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
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            Modules m = modules[i];
            if (m != Modules.UNSET)
            {
                built += survivalBuildPiece(
                    m.structureID,
                    stackSize,
                    moduleHorizontalOffsets[i],
                    moduleVerticalOffsets[i],
                    moduleDepthOffsets[i],
                    elementBudget,
                    env,
                    false,
                    true);
            }
        }

        return built;
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mTier = 0;
        //todo: tiered structure 1 - 3
        if(checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, 0) && mCasingAmount >= 14)
        {
            mTier = 3;
            return structCheckModules();
        }
       /* if(checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, 0) && mCasingAmount >= 14)
        {
            mTier = 2;
            return structCheckModules();
        }
        if(checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffset, verticalOffset, 0) && mCasingAmount >= 14)
        {
            mTier = 1;
            return structCheckModules();
        }*/
        return false;


    }

    private boolean structCheckModules() {
        for (int i = 0; i < 2 + (mTier - 1); i++) {
            Modules m = modules[i];
            if(!checkPiece(m.structureID,moduleHorizontalOffsets[i],moduleVerticalOffsets[i],moduleDepthOffsets[i])) return false;
        }
        return true;

    }

    public CoolingFluid findCoolingFluid()
    {
        for (MTEHatchInput hatch : mInputHatches)
        {
            Optional<CoolingFluid> fluid = COOLING_FLUIDS.stream()
                .filter(candidate -> drain(hatch, candidate.getStack(), false))
                .findFirst();
            if (fluid.isPresent()) return fluid.get();
        }
        return null;
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
    @Nonnull
    @Override
    protected CheckRecipeResult checkRecipeForCustomHatches(CheckRecipeResult lastResult) {
        for (MTEHatchInput solidifierHatch : mInputHatches) {
            if (solidifierHatch instanceof MTEHatchSolidifier hatch) {
                List<ItemStack> items = hatch.getNonConsumableItems();
                FluidStack fluid = solidifierHatch.getFluid();

                if (items != null && fluid != null) {
                    processingLogic.setInputItems(items);
                    processingLogic.setInputFluids(fluid);

                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        lastResult = foundResult;
                    }
                }
            }
        }
        processingLogic.clear();
        return lastResult;
    }
    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected  CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                additionaloverclocks = 0;

                if (hypercoolerPresent)
                {
                    currentCoolingFluid = findCoolingFluid();
                    if(currentCoolingFluid == null)
                    {
                        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                    }
                    additionaloverclocks = currentCoolingFluid.grantedOC ;
                }

                if (GTUtility.getTier(recipe.mEUt) >= VoltageIndex.UEV && uevRecipesEnabled)
                {
                   // return CheckRecipeResultRegistry.insufficientVoltage(recipe.mEUt);
                    return CheckRecipeResultRegistry.insufficientStartupPower(recipe.mEUt);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setMaxRegularOverclocks(additionaloverclocks + (getTier(getAverageInputVoltage()) - getTier(recipe.mEUt)))
                    .setDurationDecreasePerOC(ocFactorBase+ocFactorAdditive);

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

}
