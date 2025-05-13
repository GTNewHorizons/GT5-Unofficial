package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class AssemblyMatrix extends MTENanochipAssemblyModuleBase<AssemblyMatrix> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int ASSEMBLY_OFFSET_X = 3;
    protected static final int ASSEMBLY_OFFSET_Y = 4;
    protected static final int ASSEMBLY_OFFSET_Z = 0;
    protected static final String[][] ASSEMBLY_STRING = new String[][] { { "       ", "  BFB  ", " DDFDD ", "  BFB  " },
        { "  CFC  ", " D   D ", "DB   BD", " DEEED " }, { " CFFFC ", "B     B", "D     D", "BE   EB" },
        { " FFDFF ", "F  E  F", "F  A  F", "FE   EF" }, { " CFFFC ", "B     B", "D     D", "BE   EB" },
        { "  CFC  ", " D   D ", "DB   BD", " DEEED " }, { "       ", "  BFB  ", " DDFDD ", "  BFB  " } };
    private int machineTier = 0;
    public static final IStructureDefinition<AssemblyMatrix> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<AssemblyMatrix>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ASSEMBLY_STRING)
        // Iron bar
        .addElement('A', ofBlock(Blocks.iron_bars, 0))
        // CoAL casing
        .addElement(
            'B',
            ofBlocksTiered(
                (block, meta) -> block == Loaders.componentAssemblylineCasing ? meta : null,
                IntStream.range(0, 14)
                    .mapToObj(i -> Pair.of(Loaders.componentAssemblylineCasing, i))
                    .collect(Collectors.toList()),
                -1,
                AssemblyMatrix::setCasingTier,
                AssemblyMatrix::getCasingTier))
        // White casing block
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Naquadah Alloy Frame Box
        .addElement('E', ofFrame(Materials.NaquadahAlloy))
        // Black glass
        .addElement('F', ofBlock(GregTechAPI.sBlockTintedGlass, 3))

        .build();

    private void setCasingTier(int tier) {
        this.machineTier = tier;
    }

    private int getCasingTier() {
        return this.machineTier;
    }

    /**
     * Create new nanochip assembly module
     *
     * @param aID           ID of this module
     * @param aName         Name of this module
     * @param aNameRegional Localized name of this module
     */
    public AssemblyMatrix(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected AssemblyMatrix(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<AssemblyMatrix> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, ASSEMBLY_OFFSET_X, ASSEMBLY_OFFSET_Y, ASSEMBLY_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            ASSEMBLY_OFFSET_X,
            ASSEMBLY_OFFSET_Y,
            ASSEMBLY_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check base structure
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        // Now check module structure
        return checkPiece(STRUCTURE_PIECE_MAIN, ASSEMBLY_OFFSET_X, ASSEMBLY_OFFSET_Y, ASSEMBLY_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo(NAC_MODULE)
            .addInfo("Assembles your Circuit Part " + TOOLTIP_CC + "s into Circuit " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the color of the first input in NEI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AssemblyMatrix(this.mName);
    }

    @Override
    public @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        int recipeTier = GTUtility.getTier(recipe.mEUt);
        int machineTier = getCasingTier();
        if (machineTier >= recipeTier) return CheckRecipeResultRegistry.SUCCESSFUL;
        return CheckRecipeResultRegistry.insufficientMachineTier(recipeTier);
    }

    public static void registerLocalName(ItemStack stack, CircuitComponent component) {
        component.fallbackLocalizedName = stack.getDisplayName();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipAssemblyMatrixRecipes;
    }
}
