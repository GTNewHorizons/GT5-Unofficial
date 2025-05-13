package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gtPlusPlus.core.material.MaterialsElements;

public class CuttingChamber extends MTENanochipAssemblyModuleBase<CuttingChamber> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int CUTTING_OFFSET_X = 3;
    protected static final int CUTTING_OFFSET_Y = 5;
    protected static final int CUTTING_OFFSET_Z = 0;
    protected static final String[][] CUTTING_STRUCTURE = new String[][] {
        { "       ", "       ", "       ", " A   A ", " A   A " },
        { "  CCC  ", " BBBBB ", " CEEEC ", "ACEEECA", "ACEDECA" },
        { " CCDCC ", "B     B", "B     B", "B     B", "B  D  B" },
        { " DDDDD ", "BCCCCCB", "EAAAAAE", "E     E", "EDDDDDE" },
        { " CCDCC ", "B     B", "B     B", "B     B", "B  D  B" },
        { "  CCC  ", " BBBBB ", " CEEEC ", "ACEEECA", "ACEDECA" },
        { "       ", "       ", "       ", " A   A ", " A   A " } };

    public static final IStructureDefinition<CuttingChamber> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<CuttingChamber>builder()
        .addShape(STRUCTURE_PIECE_MAIN, CUTTING_STRUCTURE)
        // Celestial Tungsten Framebox
        .addElement(
            'A',
            lazy(
                t -> ofBlock(
                    Block.getBlockFromItem(
                        MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFrameBox(1)
                            .getItem()),
                    0)))
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
