package gregtech.common.pollution;

import net.minecraft.world.World;

public class PollutionHelper {

    /**
     * Helper for emitting pollution from basic furnace blocks while they burn
     */
    public static void furnaceAddPollutionOnUpdate(World world, int x, int z, int pollutionAmount) {
        if (!world.isRemote && (world.getTotalWorldTime() % 20) == 0) {
            Pollution.addPollution(world.getChunkFromBlockCoords(x, z), pollutionAmount);
        }
    }
}
