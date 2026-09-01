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
 * The Continuum Stabilizer (5×5×7) — a Voidcraft multiblock component: one controller block (the stats carrier) and
 * 174 plain casing blocks. Dormant in-world (its base class runs no recipe); its structure is validated by its own
 * {@link #checkMachine}, and the assemblers digitize it (stats included) iff that structure is formed. The ripple
 * stabilization the component provides is an internal of the Unstable Solar System, contributed by the base that
 * carries it — never an in-world machine. Station-only: a ship build containing it is rejected.
 */
public class MTEVoidcraftContinuumStabilizer extends MTEVoidcraftMultiblockBase {

    public static final IStructureDefinition<MTEVoidcraftContinuumStabilizer> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidcraftContinuumStabilizer>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(ContinuumStabilizerStructure.RAW_SHAPE))
        .addElement('A', ofBlock(GregTechAPI.sBlockMachines, MetaTileEntityIDs.VoidcraftContinuumStabilizerCasing.ID))
        .build();

    private static final int[] ANCHOR = ContinuumStabilizerStructure.ANCHOR;

    public MTEVoidcraftContinuumStabilizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, VoidcraftComponent.CONTINUUM_STABILIZER);
    }

    public MTEVoidcraftContinuumStabilizer(String aName) {
        super(aName, VoidcraftComponent.CONTINUUM_STABILIZER);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return newMetaEntity(mName);
    }

    @Override
    protected MTEVoidcraftMultiblockBase newMetaEntity(String aName) {
        return new MTEVoidcraftContinuumStabilizer(aName);
    }

    @Override
    public IStructureDefinition<MTEVoidcraftContinuumStabilizer> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected List<VoidcraftComponent> getCasingComponents() {
        return Collections.singletonList(VoidcraftComponent.CONTINUUM_STABILIZER_CASING);
    }

    @Override
    protected int getExpectedCells() {
        return ContinuumStabilizerStructure.EXPECTED_CELLS;
    }

    @Override
    protected int[] getAnchorOffset() {
        return ANCHOR;
    }

    @Override
    protected String componentTooltipHint() {
        return "tt.voidcraft_component.continuum_stabilizer_hint";
    }
}
