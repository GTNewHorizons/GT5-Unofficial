package gregtech.common.blocks.rubbertree;

import net.minecraft.world.World;

import gregtech.api.util.GTUtility;

public final class RubberTreeEffects {

    private RubberTreeEffects() {}

    public static void playResinHarvestSound(World world, int x, int y, int z) {
        GTUtility.sendSoundToPlayers(
            world,
            "gregtech:resin.harvest",
            0.55F,
            0.96F + world.rand.nextFloat() * 0.10F,
            x + 0.5D,
            y + 0.5D,
            z + 0.5D);
    }
}
