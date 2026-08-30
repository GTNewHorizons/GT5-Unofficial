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
 * The Satellite Rail Launcher — the first planetary/star-scale infrastructure component (7×7×12): one controller
 * block (the stats carrier), nine plain casing layers, and a three-layer panel launch deck.
 *
 * <p>
 * Dormant in-world (its base class runs no recipe); its structure is validated by its own {@link #checkMachine},
 * and the assemblers digitize it (stats included) iff that structure is formed. The 7×7×12 footprint only fits the
 * Voidbase Assembler's scan volume — the component is station-only (a ship build containing it is rejected).
 */
public class MTEVoidcraftSatelliteLauncher extends MTEVoidcraftMultiblockBase {

    public static final IStructureDefinition<MTEVoidcraftSatelliteLauncher> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidcraftSatelliteLauncher>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(SatelliteLauncherStructure.RAW_SHAPE))
        .addElement('A', ofBlock(GregTechAPI.sBlockMachines, MetaTileEntityIDs.VoidcraftSatelliteLauncherCasing.ID))
        .addElement('B', ofBlock(GregTechAPI.sBlockMachines, MetaTileEntityIDs.VoidcraftSatelliteLauncherPanel.ID))
        .build();

    private static final int[] ANCHOR = SatelliteLauncherStructure.ANCHOR;

    public MTEVoidcraftSatelliteLauncher(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, VoidcraftComponent.SATELLITE_LAUNCHER);
    }

    public MTEVoidcraftSatelliteLauncher(String aName) {
        super(aName, VoidcraftComponent.SATELLITE_LAUNCHER);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return newMetaEntity(mName);
    }

    @Override
    protected MTEVoidcraftMultiblockBase newMetaEntity(String aName) {
        return new MTEVoidcraftSatelliteLauncher(aName);
    }

    @Override
    public IStructureDefinition<MTEVoidcraftSatelliteLauncher> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected List<VoidcraftComponent> getCasingComponents() {
        return Arrays.asList(VoidcraftComponent.SATELLITE_LAUNCHER_CASING, VoidcraftComponent.SATELLITE_LAUNCHER_PANEL);
    }

    @Override
    protected int getExpectedCells() {
        return SatelliteLauncherStructure.EXPECTED_CELLS;
    }

    @Override
    protected int[] getAnchorOffset() {
        return ANCHOR;
    }
}
