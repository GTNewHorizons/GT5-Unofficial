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
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gtPlusPlus.core.material.MaterialsElements;

public class OpticalOrganizer extends MTENanochipAssemblyModuleBase<OpticalOrganizer> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int OPTICAL_OFFSET_X = 3;
    protected static final int OPTICAL_OFFSET_Y = 7;
    protected static final int OPTICAL_OFFSET_Z = 0;
    protected static final String[][] OPTICAL_STRING = new String[][] {
        { "       ", " BBCBB ", " AACAA ", "   C   ", "   C   ", "   C   ", " AACAA " },
        { " AAAAA ", "BBBCBBB", "ABD DBA", " BD DB ", " BD DB ", " BD DB ", "ABD DBA" },
        { " A   A ", "BBCCCBB", "AD   DA", " DCCCD ", " D   D ", " DCCCD ", "AD   DA" },
        { " A   A ", "CCCBCCC", "C  C  C", "C CCC C", "C     C", "C CCC C", "C  C  C" },
        { " A   A ", "BBCCCBB", "AD   DA", " DCC D ", " D   D ", " DCCCD ", "AD   DA" },
        { " AAAAA ", "BBBCBBB", "ABD DBA", " BD DB ", " BD DB ", " BD DB ", "ABD DBA" },
        { "       ", " BBCBB ", " AACAA ", "   C   ", "   C   ", "   C   ", " AACAA " } };
    public static final IStructureDefinition<OpticalOrganizer> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<OpticalOrganizer>builder()
        .addShape(STRUCTURE_PIECE_MAIN, OPTICAL_STRING)
        // Hypogen Frame Box
        .addElement(
            'A',
            lazy(
                t -> ofBlock(
                    Block.getBlockFromItem(
                        MaterialsElements.STANDALONE.HYPOGEN.getFrameBox(1)
                            .getItem()),
                    0)))
        // White casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Non-Photonic Matter Exclusion Glass
        .addElement('D', ofBlock(GregTechAPI.sBlockGlass1, 3))
        .build();

    public OpticalOrganizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected OpticalOrganizer(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<OpticalOrganizer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, OPTICAL_OFFSET_X, OPTICAL_OFFSET_Y, OPTICAL_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            OPTICAL_OFFSET_X,
            OPTICAL_OFFSET_Y,
            OPTICAL_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, OPTICAL_OFFSET_X, OPTICAL_OFFSET_Y, OPTICAL_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo(NAC_MODULE)
            .addInfo("Optimizes your Optical " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new OpticalOrganizer(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Optimized " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipOpticalOrganizer;
    }
}
