package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;

public class SMDProcessor extends MTENanochipAssemblyModuleBase<SMDProcessor> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int SMD_OFFSET_X = 3;
    protected static final int SMD_OFFSET_Y = 3;
    protected static final int SMD_OFFSET_Z = 0;
    protected static final String[][] SMD_STRING = new String[][] { { " B   B ", " EA AE ", " BB BB " },
        { "BBDDDBB", "ECA ACE", "BBB BBB" }, { " D   D ", "AAA AAA", "BBBDBBB" }, { " D   D ", "       ", "  DDD  " },
        { " D   D ", "AAA AAA", "BBBDBBB" }, { "BBDDDBB", "ECA ACE", "BBB BBB" }, { " B   B ", " EA AE ", " BB BB " } };
    public static final IStructureDefinition<SMDProcessor> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<SMDProcessor>builder()
        .addShape(STRUCTURE_PIECE_MAIN, SMD_STRING)
        // White casing block
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // UEV Machine Casings
        .addElement('C', ofBlock(GregTechAPI.sBlockCasingsNH, 10))
        // Radox polymer frame
        .addElement('D', ofFrame(Materials.RadoxPolymer))
        // Black glass
        .addElement('E', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        .build();

    public SMDProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected SMDProcessor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<SMDProcessor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, SMD_OFFSET_X, SMD_OFFSET_Y, SMD_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            SMD_OFFSET_X,
            SMD_OFFSET_Y,
            SMD_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, SMD_OFFSET_X, SMD_OFFSET_Y, SMD_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo(NAC_MODULE)
            .addInfo("Processes your SMD " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new SMDProcessor(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        // Processed SMDs can be given a name like 'SMD Inductor Tray'
        component.fallbackLocalizedName = unprocessedName + " Tray";
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipSMDProcessorRecipes;
    }
}
