package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.joml.Vector3i;

import com.mojang.authlib.GameProfile;

import gregtech.api.util.Lazy;
import gregtech.common.items.matterManipulator.NBTState.Config;
import gregtech.common.items.matterManipulator.NBTState.Location;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;

public class BlockAnalyzer {

    private BlockAnalyzer() {}

    public static @Nullable TileAnalysisResult analyze(IBlockAnalysisContext context) {
        TileEntity te = context.getTileEntity();

        if (te == null) {
            return null;
        }

        TileAnalysisResult result = new TileAnalysisResult(context.getFakePlayer(), te);

        return result.doesAnything() ? result : null;
    }

    public static RegionAnalysis analyzeRegion(World world, Location a, Location b) {
        if (a == null || b == null || world.provider.dimensionId != a.worldId || a.worldId != b.worldId) return null;

        RegionAnalysis analysis = new RegionAnalysis();

        Vector3i deltas = Config.getRegionDeltas(a, b);
        analysis.deltas = deltas;

        analysis.blocks = new ArrayList<>();

        BlockAnalysisContext context = new BlockAnalysisContext(world);

        for (Vector3i voxel : Config.getBlocksInBB(a, deltas)) {
            PendingBlock pending = PendingBlock.fromBlock(world, voxel.x, voxel.y, voxel.z);

            if (pending == null) {
                continue;
            }

            context.voxel = voxel;
            TileAnalysisResult tile = analyze(context);

            if (tile != null && tile.doesAnything()) {
                pending.tileData = tile;
            }

            pending.x -= a.x;
            pending.y -= a.y;
            pending.z -= a.z;

            analysis.blocks.add(pending);
        }

        return analysis;
    }

    public static class RegionAnalysis {

        public Vector3i deltas;
        public List<PendingBlock> blocks;
    }

    public static interface IBlockAnalysisContext {

        public Supplier<EntityPlayer> getFakePlayer();

        public TileEntity getTileEntity();
    }

    public static class BlockAnalysisContext implements IBlockAnalysisContext {

        public World world;
        public final Lazy<FakePlayer> fakePlayer;
        public Vector3i voxel;

        public BlockAnalysisContext(World world) {
            this.world = world;
            fakePlayer = new Lazy<>(
                () -> new FakePlayer(
                    (WorldServer) world,
                    new GameProfile(UUID.randomUUID(), "BlockAnalyzer Fake Player")));
        }

        @Override
        public Supplier<EntityPlayer> getFakePlayer() {
            return fakePlayer::get;
        }

        @Override
        public TileEntity getTileEntity() {
            return world.getTileEntity(voxel.x, voxel.y, voxel.z);
        }
    }

    public static interface IBlockApplyContext extends IBlockAnalysisContext {

        public EntityPlayer getPlacingPlayer();

        public boolean tryApplyAction(double complexity);

        public boolean tryConsumeItems(ItemStack... items);

        public void givePlayerItems(ItemStack... items);

        public void givePlayerFluids(FluidStack... fluids);
    }

    public static class BlockApplyContext implements IBlockApplyContext {

        public World world;
        public int x, y, z;
        public EntityPlayer player;
        public PendingBuild build;
        public ItemStack manipulator;
        public Lazy<FakePlayer> fakePlayer;

        public static final double EU_PER_ACTION = 8192;

        @Override
        public Supplier<EntityPlayer> getFakePlayer() {
            if (fakePlayer == null) {
                fakePlayer = new Lazy<>(
                    () -> new FakePlayer((WorldServer) player.getEntityWorld(), player.getGameProfile()));
            }

            return fakePlayer::get;
        }

        @Override
        public TileEntity getTileEntity() {
            return world.getTileEntity(x, y, z);
        }

        @Override
        public EntityPlayer getPlacingPlayer() {
            return player;
        }

        @Override
        public boolean tryApplyAction(double complexity) {
            return build.tryConsumePower(manipulator, x, y, z, EU_PER_ACTION * complexity);
        }

        @Override
        public boolean tryConsumeItems(ItemStack... items) {
            if (build == null) {
                for (ItemStack item : items) System.out.println("consume: " + item);
                return true;
            } else {
                return build.tryConsumeItems(items);
            }
        }

        @Override
        public void givePlayerItems(ItemStack... items) {
            if (build == null) {
                for (ItemStack item : items) System.out.println("give: " + item);
            } else {
                build.givePlayerItems(items);
            }
        }

        @Override
        public void givePlayerFluids(FluidStack... fluids) {
            build.givePlayerFluids(fluids);
        }
    }

    static ForgeDirection nullIfUnknown(ForgeDirection dir) {
        return dir == ForgeDirection.UNKNOWN ? null : dir;
    }

    static void emptyInventory(IBlockApplyContext context, IInventory inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null && stack.getItem() != null) {
                inv.setInventorySlotContents(i, null);

                context.givePlayerItems(stack);
            }
        }

        inv.markDirty();
    }
}
