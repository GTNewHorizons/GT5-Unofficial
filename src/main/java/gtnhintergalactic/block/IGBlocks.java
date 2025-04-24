package gtnhintergalactic.block;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gtnhintergalactic.item.ItemBlockSpaceElevatorCable;
import gtnhintergalactic.item.ItemCasingDysonSwarm;

/**
 * List of all blocks of this mod
 */
public class IGBlocks {

    /**
     * Initialize the blocks of this mod
     */
    public static void init() {
        GregTechAPI.sSpaceElevatorCable = new BlockSpaceElevatorCable();
        GameRegistry
            .registerBlock(GregTechAPI.sSpaceElevatorCable, ItemBlockSpaceElevatorCable.class, "spaceelevatorcable");

        GregTechAPI.sBlockCasingsSE = new BlockCasingSpaceElevator();
        GregTechAPI.sBlockCasingsSEMotor = new BlockCasingSpaceElevatorMotor();

        GregTechAPI.sBlockCasingsDyson = new BlockCasingDysonSwarm();
        GameRegistry.registerBlock(GregTechAPI.sBlockCasingsDyson, ItemCasingDysonSwarm.class, "dysonswarmparts");

        GregTechAPI.sBlockCasingsSiphon = new BlockCasingGasSiphon();
        GameRegistry.registerBlock(GregTechAPI.sBlockCasingsSiphon, "gassiphoncasing");
    }
}
