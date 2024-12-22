package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;

import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.items.IDMetaItem01;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class AssemblyMatrix extends MTENanochipAssemblyModuleBase<AssemblyMatrix> {

    protected static final int STRUCTURE_OFFSET_X = 3;
    protected static final int STRUCTURE_OFFSET_Y = 3;
    protected static final int STRUCTURE_OFFSET_Z = -2;

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String[][] structure = new String[][] { { "  AAA  ", "  AAA  ", "  AAA  " },
        { "  AAA  ", "  A A  ", "  AAA  " }, { "  AAA  ", "  AAA  ", "  AAA  " } };

    public static final IStructureDefinition<AssemblyMatrix> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<AssemblyMatrix>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings4, 0))
        .build();

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
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            hintsOnly,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            STRUCTURE_OFFSET_X,
            STRUCTURE_OFFSET_Y,
            STRUCTURE_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_OFFSET_X, STRUCTURE_OFFSET_Y, STRUCTURE_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AssemblyMatrix(this.mName);
    }

    @Override
    public @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
        int recipeTier = GTUtility.getTier(recipe.mEUt);
        int machineTier = getMachineTier();
        if (machineTier >= recipeTier) return CheckRecipeResultRegistry.SUCCESSFUL;
        return CheckRecipeResultRegistry.insufficientMachineTier(recipeTier);
    }

    private int getMachineTier() {
        // Determine tier of machine based on tier of stack of robot arms in the controller.
        ItemStack stack = this.getControllerSlot();
        if (stack == null || stack.stackSize != 64) return 0;
        int meta = stack.getItemDamage() - 32000;
        // In the ID list, LV-UEV is a separate range from UIV-MAX
        if (meta >= IDMetaItem01.Robot_Arm_LV.ID && meta <= IDMetaItem01.Robot_Arm_UEV.ID) {
            // LV is tier 1
            return meta - IDMetaItem01.Robot_Arm_LV.ID + 1;
        } else if (meta >= IDMetaItem01.Robot_Arm_UIV.ID && meta <= IDMetaItem01.Robot_Arm_MAX.ID) {
            // UIV is tier 11
            return meta - IDMetaItem01.Robot_Arm_UIV.ID + 11;
        }
        return 0;
    }

    public static void registerLocalName(ItemStack stack, CircuitComponent component) {
        component.fallbackLocalizedName = stack.getDisplayName();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipAssemblyMatrixRecipes;
    }
}
