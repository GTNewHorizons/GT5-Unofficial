package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.CUTTING_OFFSET_X;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.CUTTING_OFFSET_Y;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.CUTTING_OFFSET_Z;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.CUTTING_STRUCTURE;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class CuttingChamber extends MTENanochipAssemblyModuleBase<CuttingChamber> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String[][] structure = CUTTING_STRUCTURE;

    public static final IStructureDefinition<CuttingChamber> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<CuttingChamber>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Infinity Catalyst Framebox
        .addElement('A', ofFrame(Materials.InfinityCatalyst))
        // White casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Bulk Production Frame
        .addElement('D', Casings.BulkProductionFrame.asElement(null))
        // Black glass
        .addElement('E', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        .build();

    public CuttingChamber(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected CuttingChamber(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<CuttingChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, CUTTING_OFFSET_X, CUTTING_OFFSET_Y, CUTTING_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            CUTTING_OFFSET_X,
            CUTTING_OFFSET_Y,
            CUTTING_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, CUTTING_OFFSET_X, CUTTING_OFFSET_Y, CUTTING_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo(NAC_MODULE)
            .addInfo("Cuts your Wafer " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Input Hatch")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new CuttingChamber(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Cut " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipCuttingChamber;
    }
}
