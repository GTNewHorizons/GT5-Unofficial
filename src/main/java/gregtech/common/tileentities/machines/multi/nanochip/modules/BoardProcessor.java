package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.BOARD_OFFSET_X;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.BOARD_OFFSET_Y;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.BOARD_OFFSET_Z;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.BOARD_STRING;
import static gtPlusPlus.xmod.thermalfoundation.block.TFBlocks.blockFluidEnder;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class BoardProcessor extends MTENanochipAssemblyModuleBase<BoardProcessor> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String[][] structure = BOARD_STRING;
    public static final IStructureDefinition<BoardProcessor> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<BoardProcessor>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Transcendent metal frame
        .addElement('A', ofFrame(MaterialsUEVplus.TranscendentMetal))
        // White casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Black glass
        .addElement('D', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        // Source block of flowing ender
        .addElement('E', lazy(unused -> ofBlock(blockFluidEnder, 0)))
        .build();

    public BoardProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected BoardProcessor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<BoardProcessor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, BOARD_OFFSET_X, BOARD_OFFSET_Y, BOARD_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            BOARD_OFFSET_X,
            BOARD_OFFSET_Y,
            BOARD_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, BOARD_OFFSET_X, BOARD_OFFSET_Y, BOARD_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo(NAC_MODULE)
            .addInfo("Processes your Board " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Input Hatch")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new BoardProcessor(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = unprocessedName + " Die";
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipBoardProcessorRecipes;
    }
}
