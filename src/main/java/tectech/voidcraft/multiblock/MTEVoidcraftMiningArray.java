package tectech.voidcraft.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import java.util.Arrays;
import java.util.List;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.voidcraft.ship.VoidcraftComponent;

/**
 * The Voidcraft Mining Array — the reference multiblock component (3×3×2): one controller block (the stats
 * carrier) ringed by eight accent panels on its front face, nine plain casing blocks behind.
 *
 * <p>
 * Dormant in-world (its base class runs no recipe); its structure is validated by its own {@link #checkMachine},
 * and the assemblers digitize it (stats included) iff that structure is formed.
 */
public class MTEVoidcraftMiningArray extends MTEVoidcraftMultiblockBase {

    public static final IStructureDefinition<MTEVoidcraftMiningArray> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidcraftMiningArray>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(MiningArrayStructure.RAW_SHAPE))
        .addElement('A', ofBlock(GregTechAPI.sBlockMachines, MetaTileEntityIDs.VoidcraftMiningArrayCasing.ID))
        .addElement('B', ofBlock(GregTechAPI.sBlockMachines, MetaTileEntityIDs.VoidcraftMiningArrayPanel.ID))
        .build();

    private static final int[] ANCHOR = MiningArrayStructure.findAnchor(MiningArrayStructure.RAW_SHAPE);

    public MTEVoidcraftMiningArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, VoidcraftComponent.MINING_ARRAY);
    }

    public MTEVoidcraftMiningArray(String aName) {
        super(aName, VoidcraftComponent.MINING_ARRAY);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return newMetaEntity(mName);
    }

    @Override
    protected MTEVoidcraftMultiblockBase newMetaEntity(String aName) {
        return new MTEVoidcraftMiningArray(aName);
    }

    @Override
    public IStructureDefinition<MTEVoidcraftMiningArray> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected List<VoidcraftComponent> getCasingComponents() {
        return Arrays.asList(VoidcraftComponent.MINING_ARRAY_CASING, VoidcraftComponent.MINING_ARRAY_PANEL);
    }

    @Override
    protected int getExpectedCells() {
        return MiningArrayStructure.EXPECTED_CELLS;
    }

    @Override
    protected int[] getAnchorOffset() {
        return ANCHOR;
    }
}
