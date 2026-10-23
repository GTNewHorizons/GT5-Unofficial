package gregtech.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

/**
 * Server-only class responsible for structure updates, to handle disconnected structures
 */
public class GTStructureUpdater {

    private Map<World, PerWorldStructureUpdater> updaters = new HashMap();

    public void addCoord(IMachineBlockUpdateable controller, World world, long packedCoord) {
        updaters.computeIfAbsent(world, PerWorldStructureUpdater::new)
            .addCoord(controller, packedCoord);
    }

    public void removeCoord(IMachineBlockUpdateable controller, World world, long packedCoord) {
        updaters.get(world)
            .removeCoord(controller, packedCoord);
    }

    private static class PerWorldStructureUpdater implements IWorldAccess {

        private Long2ObjectOpenHashMap<List<IMachineBlockUpdateable>> map = new Long2ObjectOpenHashMap<>();

        private PerWorldStructureUpdater(World world) {
            world.addWorldAccess(this);
        }

        private void addCoord(IMachineBlockUpdateable controller, long packedCoord) {
            map.computeIfAbsent(packedCoord, $ -> new ArrayList<>())
                .add(controller);
        }

        private void removeCoord(IMachineBlockUpdateable controller, long packedCoord) {
            List<IMachineBlockUpdateable> controllers = map.get(packedCoord);
            controllers.remove(controller);
            if (controllers.isEmpty()) {
                map.remove(packedCoord);
            }
        }

        @Override
        public void markBlockForUpdate(int x, int y, int z) {
            long packed = CoordinatePacker.pack(x, y, z);
            List<IMachineBlockUpdateable> controllers = map.get(packed);
            if (controllers == null) return;
            for (IMachineBlockUpdateable controller : controllers) {
                controller.onMachineBlockUpdate();
            }
        }

        @Override
        public void markBlockForRenderUpdate(int p_147588_1_, int p_147588_2_, int p_147588_3_) {

        }

        @Override
        public void markBlockRangeForRenderUpdate(int p_147585_1_, int p_147585_2_, int p_147585_3_, int p_147585_4_,
            int p_147585_5_, int p_147585_6_) {

        }

        @Override
        public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {

        }

        @Override
        public void playSoundToNearExcept(EntityPlayer p_85102_1_, String p_85102_2_, double p_85102_3_,
            double p_85102_5_, double p_85102_7_, float p_85102_9_, float p_85102_10_) {

        }

        @Override
        public void spawnParticle(String p_72708_1_, double p_72708_2_, double p_72708_4_, double p_72708_6_,
            double p_72708_8_, double p_72708_10_, double p_72708_12_) {

        }

        @Override
        public void onEntityCreate(Entity p_72703_1_) {

        }

        @Override
        public void onEntityDestroy(Entity p_72709_1_) {

        }

        @Override
        public void playRecord(String p_72702_1_, int p_72702_2_, int p_72702_3_, int p_72702_4_) {

        }

        @Override
        public void broadcastSound(int p_82746_1_, int p_82746_2_, int p_82746_3_, int p_82746_4_, int p_82746_5_) {

        }

        @Override
        public void playAuxSFX(EntityPlayer p_72706_1_, int p_72706_2_, int p_72706_3_, int p_72706_4_, int p_72706_5_,
            int p_72706_6_) {

        }

        @Override
        public void destroyBlockPartially(int p_147587_1_, int p_147587_2_, int p_147587_3_, int p_147587_4_,
            int p_147587_5_) {

        }

        @Override
        public void onStaticEntitiesChanged() {

        }
    }
}
