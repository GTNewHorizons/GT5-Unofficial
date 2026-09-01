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
 * The Stellar Lens (7×7×12) — a Voidcraft multiblock component: one controller block (the stats carrier) and 587
 * plain casing blocks. Dormant in-world (its base class runs no recipe); its structure is validated by its own
 * {@link #checkMachine}, and the assemblers digitize it (stats included) iff that structure is formed. The visual it
 * provides around the star is an internal of the Unstable Solar System, contributed by the base that carries it —
 * never an in-world machine. Station-only: a ship build containing it is rejected.
 */
public class MTEVoidcraftStellarLens extends MTEVoidcraftMultiblockBase {

    public static final IStructureDefinition<MTEVoidcraftStellarLens> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidcraftStellarLens>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(StellarLensStructure.RAW_SHAPE))
        .addElement('A', ofBlock(GregTechAPI.sBlockMachines, MetaTileEntityIDs.VoidcraftStellarLensCasing.ID))
        .build();

    private static final int[] ANCHOR = StellarLensStructure.ANCHOR;

    public MTEVoidcraftStellarLens(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, VoidcraftComponent.STELLAR_LENS);
    }

    public MTEVoidcraftStellarLens(String aName) {
        super(aName, VoidcraftComponent.STELLAR_LENS);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return newMetaEntity(mName);
    }

    @Override
    protected MTEVoidcraftMultiblockBase newMetaEntity(String aName) {
        return new MTEVoidcraftStellarLens(aName);
    }

    @Override
    public IStructureDefinition<MTEVoidcraftStellarLens> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected List<VoidcraftComponent> getCasingComponents() {
        return Collections.singletonList(VoidcraftComponent.STELLAR_LENS_CASING);
    }

    @Override
    protected int getExpectedCells() {
        return StellarLensStructure.EXPECTED_CELLS;
    }

    @Override
    protected int[] getAnchorOffset() {
        return ANCHOR;
    }

    @Override
    protected String componentTooltipHint() {
        return "tt.voidcraft_component.stellar_lens_hint";
    }
}
