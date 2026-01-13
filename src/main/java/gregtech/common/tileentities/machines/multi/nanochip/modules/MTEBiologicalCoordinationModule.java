package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

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

public class MTEBiologicalCoordinationModule extends MTENanochipAssemblyModuleBase<MTEBiologicalCoordinationModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int BIO_OFFSET_X = 3;
    protected static final int BIO_OFFSET_Y = 5;
    protected static final int BIO_OFFSET_Z = 0;
    protected static final String[][] BIO_STRING = new String[][] {
        { "       ", " AAAAA ", "  C C  ", "  C C  ", "  C C  ", "   ~   " },
        { "  CCC  ", "AABBBAA", " ABDBA ", " ABDBA ", " ABDBA ", "       " },
        { " C   C ", "ABDDDBA", "CB   BC", "CB   BC", "CB   BC", "       " },
        { " C   C ", "ABDADBA", " D   D ", " D   D ", " D   D ", "       " },
        { " C   C ", "ABDDDBA", "CB   BC", "CB   BC", "CB   BC", "       " },
        { "  CCC  ", "AABBBAA", " ABDBA ", " ABDBA ", " ABDBA ", "       " },
        { "       ", " AAAAA ", "  C C  ", "  C C  ", "  C C  ", "       " } };
    public static final IStructureDefinition<MTEBiologicalCoordinationModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEBiologicalCoordinationModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, BIO_STRING)
        // Nanochip Primary Casing
        .addElement('A', Casings.NanochipPrimaryCasing.asElement())
        // Nanochip Secondary Casing
        .addElement('B', Casings.NanochipSecondaryCasing.asElement())
        // Tritanium Frame Box
        .addElement('C', ofFrame(Materials.Tritanium))
        // Circuit Complex Glass
        .addElement('D', Casings.NanochipGlass.asElement())
        .build();

    public MTEBiologicalCoordinationModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEBiologicalCoordinationModule(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEBiologicalCoordinationModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, BIO_OFFSET_X, BIO_OFFSET_Y, BIO_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            BIO_OFFSET_X,
            BIO_OFFSET_Y,
            BIO_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, BIO_OFFSET_X, BIO_OFFSET_Y, BIO_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addInfo("Coordinates your living " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBiologicalCoordinationModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Controlled " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipBiologicalCoordinator;
    }
}
