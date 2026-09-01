package tectech.voidcraft.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import java.util.Collections;
import java.util.List;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.voidcraft.ship.VoidcraftComponent;

/**
 * The Stabilization Matrix (7×7×10) — a Voidcraft multiblock component: one controller block (the stats carrier) and
 * 489 plain casing blocks. Dormant in-world (its base class runs no recipe); its structure is validated by its own
 * {@link #checkMachine}, and the assemblers digitize it (stats included) iff that structure is formed. The
 * stabilization it provides is an internal of the Unstable Solar System, contributed by the base that carries it —
 * never an in-world machine. Station-only: a ship build containing it is rejected.
 */
public class MTEVoidcraftStabilizationMatrix extends MTEVoidcraftMultiblockBase {

    public static final IStructureDefinition<MTEVoidcraftStabilizationMatrix> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidcraftStabilizationMatrix>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(StabilizationMatrixStructure.RAW_SHAPE))
        .addElement('A', ofBlock(GregTechAPI.sBlockMachines, MetaTileEntityIDs.VoidcraftStabilizationMatrixCasing.ID))
        .build();

    private static final int[] ANCHOR = StabilizationMatrixStructure.ANCHOR;

    public MTEVoidcraftStabilizationMatrix(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, VoidcraftComponent.STABILIZATION_MATRIX);
    }

    public MTEVoidcraftStabilizationMatrix(String aName) {
        super(aName, VoidcraftComponent.STABILIZATION_MATRIX);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return newMetaEntity(mName);
    }

    @Override
    protected MTEVoidcraftMultiblockBase newMetaEntity(String aName) {
        return new MTEVoidcraftStabilizationMatrix(aName);
    }

    @Override
    public IStructureDefinition<MTEVoidcraftStabilizationMatrix> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected List<VoidcraftComponent> getCasingComponents() {
        return Collections.singletonList(VoidcraftComponent.STABILIZATION_MATRIX_CASING);
    }

    @Override
    protected int getExpectedCells() {
        return StabilizationMatrixStructure.EXPECTED_CELLS;
    }

    @Override
    protected int[] getAnchorOffset() {
        return ANCHOR;
    }

    @Override
    protected String componentTooltipHint() {
        return "tt.voidcraft_component.stabilization_matrix_hint";
    }
}
