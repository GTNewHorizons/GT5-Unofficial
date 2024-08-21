package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.multi.nanochip.GT_MetaTileEntity_NanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class AssemblyMatrix extends GT_MetaTileEntity_NanochipAssemblyModuleBase<AssemblyMatrix> {

    protected static final int STRUCTURE_OFFSET_X = 1;
    protected static final int STRUCTURE_OFFSET_Y = 5;
    protected static final int STRUCTURE_OFFSET_Z = 0;

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String[][] structure = new String[][] { { "   ", "   ", " B ", " B ", " A " },
        { " B ", " B ", "BAB", "BAB", "AAA" }, { "   ", "   ", " B ", " B ", " A " } };

    public static final IStructureDefinition<AssemblyMatrix> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<AssemblyMatrix>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement('A', ofBlock(GregTech_API.sBlockCasings8, 10))
        .addElement('B', ofFrame(Materials.Adamantium))
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
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return new GT_Multiblock_Tooltip_Builder().toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AssemblyMatrix(this.mName);
    }

    public static void registerLocalName(ItemStack stack, CircuitComponent component) {
        component.fallbackLocalizedName = stack.getDisplayName();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipAssemblyMatrixRecipes;
    }
}
