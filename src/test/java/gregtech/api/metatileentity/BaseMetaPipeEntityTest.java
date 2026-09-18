package gregtech.api.metatileentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import net.minecraft.world.World;

import org.junit.jupiter.api.Test;

import gregtech.api.enums.HarvestTool;

class BaseMetaPipeEntityTest {

    @Test
    void managedCableUnloadsAndReactivatesWhenChunkInstanceIsReused() {
        World world = mock(World.class);
        TrackingCable cable = new TrackingCable();
        cable.setWorldObj(world);

        BaseMetaPipeEntity.clearManagedCables();
        try {
            cable.validate();
            BaseMetaPipeEntity.unloadManagedCables(world);
            assertEquals(1, cable.unloads);
            assertTrue(cable.isDead);

            cable.onChunkLoad();
            assertFalse(cable.isDead);
            BaseMetaPipeEntity.unloadManagedCables(world);
            assertEquals(2, cable.unloads);

            BaseMetaPipeEntity.unloadManagedCables(world);
            assertEquals(2, cable.unloads);
        } finally {
            BaseMetaPipeEntity.clearManagedCables();
        }
    }

    private static final class TrackingCable extends BaseMetaPipeEntity {

        private int unloads;

        private TrackingCable() {
            super(HarvestTool.CutterLevel0.toTileEntityBaseType());
        }

        @Override
        public void onUnload() {
            unloads++;
            super.onUnload();
        }
    }
}
