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

public class MTEWireTracerModule extends MTENanochipAssemblyModuleBase<MTEWireTracerModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int WIRE_OFFSET_X = 3;
    protected static final int WIRE_OFFSET_Y = 5;
    protected static final int WIRE_OFFSET_Z = 0;
    protected static final String[][] WIRE_STRING = new String[][] {
        { "       ", "  BBB  ", "  BAB  ", "  BAB  ", " BBABB " },
        { " DAAAD ", " E D E ", " E A E ", " E   E ", "BEDDDEB" },
        { " AEEEA ", "B     B", "B     B", "B     B", "BD   DB" },
        { " AEEEA ", "BD   DB", "AA C AA", "A  B  A", "AD B DA" },
        { " AEEEA ", "B     B", "B     B", "B     B", "BD   DB" },
        { " DAAAD ", " E D E ", " E A E ", " E   E ", "BEDDDEB" },
        { "       ", "  BBB  ", "  BAB  ", "  BAB  ", " BBABB " } };
    public static final IStructureDefinition<MTEWireTracerModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEWireTracerModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, WIRE_STRING)
        // White casing block
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // UEV Machine Casings
        .addElement('C', ofBlock(GregTechAPI.sBlockCasingsNH, 10))
        // Radox polymer frame
        .addElement('D', ofFrame(Materials.Vinteum))
        // Black glass
        .addElement('E', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        .build();

    public MTEWireTracerModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEWireTracerModule(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEWireTracerModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, WIRE_OFFSET_X, WIRE_OFFSET_Y, WIRE_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            WIRE_OFFSET_X,
            WIRE_OFFSET_Y,
            WIRE_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, WIRE_OFFSET_X, WIRE_OFFSET_Y, WIRE_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addInfo("Traces your Wire " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEWireTracerModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Traced " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipWireTracer;
    }
}
