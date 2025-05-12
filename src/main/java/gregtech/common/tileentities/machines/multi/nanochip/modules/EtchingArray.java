package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.ETCHING_OFFSET_X;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.ETCHING_OFFSET_Y;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.ETCHING_OFFSET_Z;
import static gregtech.common.tileentities.machines.multi.nanochip.util.AssemblyComplexStructureString.ETCHING_STRUCTURE;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;

public class EtchingArray extends MTENanochipAssemblyModuleBase<EtchingArray> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String[][] structure = ETCHING_STRUCTURE;

    public static final IStructureDefinition<EtchingArray> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<EtchingArray>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // White casing block
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Infinity Cooled Casings
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 14))
        // Particle Beam Guidance Pipe Casing
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 14))
        // Enriched Holmium Frame box
        .addElement('E', ofFrame(Materials.EnrichedHolmium))
        // Non-Photonic Matter Exclusion Glass
        .addElement('F', ofBlock(GregTechAPI.sBlockGlass1, 3))
        // Black glass
        .addElement('G', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        .addElement(
            'H',
            buildHatchAdder(EtchingArray.class).adder(EtchingArray::addDynamoHatch)
                .hatchClass(MTEHatchDynamoTunnel.class)
                .dot(1)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(1))
                .build())
        .build();

    private MTEHatchDynamoTunnel sourceHatch;

    private boolean addDynamoHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (!(aMetaTileEntity instanceof MTEHatchDynamoTunnel)) return false;

        sourceHatch = (MTEHatchDynamoTunnel) aMetaTileEntity;
        sourceHatch.updateTexture(aBaseCasingIndex);
        return true;
    }

    public EtchingArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected EtchingArray(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<EtchingArray> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, ETCHING_OFFSET_X, ETCHING_OFFSET_Y, ETCHING_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            ETCHING_OFFSET_X,
            ETCHING_OFFSET_Y,
            ETCHING_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, ETCHING_OFFSET_X, ETCHING_OFFSET_Y, ETCHING_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addInfo(NAC_MODULE)
            .addInfo("Etches your Chip " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .addOtherStructurePart(StatCollector.translateToLocal("GT5U.tooltip.structure.laser_source_hatch"), "x1", 1)
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new EtchingArray(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Etched " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipEtchingArray;
    }
}
