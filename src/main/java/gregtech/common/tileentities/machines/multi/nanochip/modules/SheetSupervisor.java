package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataInput;

public class SheetSupervisor extends MTENanochipAssemblyModuleBase<SheetSupervisor> {

    private static final int COMPUTATION_TO_BE_DRAINED_PER_SECOND = 100000;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int SHEET_OFFSET_X = 3;
    protected static final int SHEET_OFFSET_Y = 6;
    protected static final int SHEET_OFFSET_Z = 0;
    protected static final String[][] SHEET_STRING = new String[][] {
        { "       ", "  DED  ", "  DED  ", " CDEDC ", " CDEDC ", " CDEDC " },
        { "  BBB  ", " A   A ", " A C A ", "CA   AC", "CA C AC", "CA   AC" },
        { " BAAAB ", "D     D", "D AAA D", "D     D", "D AAA D", "D     D" },
        { " BAAAB ", "E     E", "ECAAACE", "E     E", "ECAAACE", "E     E" },
        { " BAAAB ", "D     D", "D AAA D", "D     D", "D AAA D", "D     D" },
        { "  BBB  ", " A   A ", " A C A ", "CA   AC", "CA C AC", "CA   AC" },
        { "       ", "  DED  ", "  DED  ", " CDEDC ", " CDEDC ", " CDEDC " } };
    public static final IStructureDefinition<SheetSupervisor> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<SheetSupervisor>builder()
        .addShape(STRUCTURE_PIECE_MAIN, SHEET_STRING)
        // White casing block
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Quantium Frame Box
        .addElement('C', ofFrame(Materials.Quantium))
        // Black glass
        .addElement('D', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        // Compuation input hatch
        .addElement(
            'E',
            // TODO: Add correct textureIndex when the new blocks get made
            GTStructureUtility
                .ofHatchAdderOptional(SheetSupervisor::addDataHatch, 0, 1, GregTechAPI.sBlockCasings8, 10))
        .build();

    private Set<MTEHatchDataInput> dataInputHatchList = new HashSet<>();

    private boolean addDataHatch(IGregTechTileEntity mte, int baseCasingIndex) {
        if (mte.getMetaTileEntity() instanceof MTEHatchDataInput dataInput) {
            dataInput.updateTexture(baseCasingIndex);
            dataInputHatchList.add(dataInput);
            return true;
        }
        return false;
    }

    public SheetSupervisor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected SheetSupervisor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<SheetSupervisor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, SHEET_OFFSET_X, SHEET_OFFSET_Y, SHEET_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            SHEET_OFFSET_X,
            SHEET_OFFSET_Y,
            SHEET_OFFSET_Z,
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
        return checkPiece(STRUCTURE_PIECE_MAIN, SHEET_OFFSET_X, SHEET_OFFSET_Y, SHEET_OFFSET_Z);
    }

    private int ticker = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            return false;
        }

        if (ticker % 20 == 0) {
            if (!hasEnoughComputation()) {
                stopMachine(ShutDownReasonRegistry.outOfStuff("Computation", COMPUTATION_TO_BE_DRAINED_PER_SECOND));
                return false;
            }
            ticker = 0;
        }

        ticker++;

        return true;
    }

    protected long getAvailableData() {
        long result = 0;
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        Vec3Impl pos = new Vec3Impl(
            baseMetaTileEntity.getXCoord(),
            baseMetaTileEntity.getYCoord(),
            baseMetaTileEntity.getZCoord());
        for (MTEHatchDataInput in : dataInputHatchList) {
            if (in.q != null) {
                Long value = in.q.contentIfNotInTrace(pos);
                if (value != null) {
                    result += value;
                }
            }
        }
        return result;
    }

    private boolean hasEnoughComputation() {
        long availableData = getAvailableData();
        return availableData >= SheetSupervisor.COMPUTATION_TO_BE_DRAINED_PER_SECOND;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addInfo("Atomically Supervises your Sheet " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new SheetSupervisor(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Observed " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipSheetSupervisor;
    }

}
