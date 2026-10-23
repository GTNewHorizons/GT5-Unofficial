package gregtech.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
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

        private final Long2ObjectOpenHashMap<BlockRecord> map = new Long2ObjectOpenHashMap<>();
        private final World world;

        private PerWorldStructureUpdater(World world) {
            this.world = world;
            world.addWorldAccess(this);
        }

        private void addCoord(IMachineBlockUpdateable controller, long packedCoord) {
            map.computeIfAbsent(packedCoord, packed -> new BlockRecord(world, packed)).controllers.add(controller);
        }

        private void removeCoord(IMachineBlockUpdateable controller, long packedCoord) {
            BlockRecord record = map.get(packedCoord);
            record.controllers.remove(controller);
            if (record.controllers.isEmpty()) {
                map.remove(packedCoord);
            }
        }

        @Override
        public void markBlockForUpdate(int x, int y, int z) {
            long packed = CoordinatePacker.pack(x, y, z);
            BlockRecord record = map.get(packed);
            if (record == null) return;
            if (!record.checkForUpdate(world, packed)) return;
            for (IMachineBlockUpdateable controller : record.controllers) {
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

        private static class BlockRecord {

            private Block block;
            private int metadata;
            private short mid;
            private final List<IMachineBlockUpdateable> controllers = new ArrayList<>();

            private BlockRecord(World world, long packed) {
                checkForUpdate(world, packed);
            }

            boolean checkForUpdate(World world, long packed) {
                int x = CoordinatePacker.unpackX(packed);
                int y = CoordinatePacker.unpackY(packed);
                int z = CoordinatePacker.unpackZ(packed);
                if (!world.blockExists(x, y, z)) return false;
                boolean updated = false;
                Block newBlock = world.getBlock(x, y, z);
                if (block != newBlock) {
                    block = newBlock;
                    updated = true;
                }
                int newMeta = world.getBlockMetadata(x, y, z);
                if (metadata != newMeta) {
                    metadata = newMeta;
                    updated = true;
                }
                TileEntity newTileEntity = world.getTileEntity(x, y, z);
                if (newTileEntity instanceof IGregTechTileEntity igte) {
                    int newMID = igte.getMetaTileID();
                    if (mid != newMID) {
                        mid = (short) newMID;
                        updated = true;
                    }
                }
                return updated;
            }

        }
    }
}
