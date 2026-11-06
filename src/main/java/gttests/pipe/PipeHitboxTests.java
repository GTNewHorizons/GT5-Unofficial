package gttests.pipe;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;

import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.blocks.ItemMachines;

/**
 * Covers on a pipe must not turn its hitbox into a full block, so the machine the pipe feeds into can still be clicked
 * through the gaps between the pipe core, its connections and its covers.
 */
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class PipeHitboxTests {

    private static final TestPos HATCH = at(1, 0, 1);
    private static final TestPos PIPE = at(1, 1, 1);
    private static final TestPos EAST_OF_PIPE = at(2, 1, 1);

    // Ticks for the pipe's connections to sync to its tile entity, which the hitbox reads
    private static final int SETTLE_TICKS = 5;

    @GameTest(batch = "gt5.pipe")
    public static void clickBesideCoveredPipeReachesHatch(GameTestHelper helper) {
        final FakePlayer player = placePipeOnHatch(helper);
        placeCover(helper, player, ForgeDirection.WEST);
        placeCover(helper, player, ForgeDirection.EAST);

        helper.startSequence()
            .thenIdle(SETTLE_TICKS)
            .thenExecute(() -> {
                final double gap = gapBesideCore(helper);
                // Looking down between the west cover and the pipe core, and between the core and the east cover
                assertHit(helper, rayTraceDown(helper, gap, 0.5), HATCH, ForgeDirection.UP, "west of the core");
                assertHit(helper, rayTraceDown(helper, 1 - gap, 0.5), HATCH, ForgeDirection.UP, "east of the core");
                // Looking down north of the core, between the two covers
                assertHit(helper, rayTraceDown(helper, 0.5, 0.1), HATCH, ForgeDirection.UP, "north of the core");
            })
            .thenSucceed();
    }

    @GameTest(batch = "gt5.pipe")
    public static void clickPastCoverOppositeConnectionReachesBlockBehind(GameTestHelper helper) {
        final FakePlayer player = placePipeOnHatch(helper);
        placeCover(helper, player, ForgeDirection.UP);
        helper.setBlock(EAST_OF_PIPE, Blocks.stone);

        helper.startSequence()
            .thenIdle(SETTLE_TICKS)
            .thenExecute(() -> {
                final double gap = gapBesideCore(helper);
                // Looking east through the pipe's block, beside the core and below the up cover
                assertHit(
                    helper,
                    rayTraceEast(helper, 0.5, gap),
                    EAST_OF_PIPE,
                    ForgeDirection.WEST,
                    "north of the core");
                assertHit(
                    helper,
                    rayTraceEast(helper, 1 - gap, 0.5),
                    EAST_OF_PIPE,
                    ForgeDirection.WEST,
                    "between the core and the up cover");
            })
            .thenSucceed();
    }

    @GameTest(batch = "gt5.pipe")
    public static void clickOnPipePartsHitsPipe(GameTestHelper helper) {
        final FakePlayer player = placePipeOnHatch(helper);
        placeCover(helper, player, ForgeDirection.WEST);
        placeCover(helper, player, ForgeDirection.EAST);

        helper.startSequence()
            .thenIdle(SETTLE_TICKS)
            .thenExecute(() -> {
                assertHit(helper, rayTraceDown(helper, 0.5, 0.5), PIPE, ForgeDirection.UP, "pipe core");
                assertHit(helper, rayTraceDown(helper, 0.05, 0.5), PIPE, ForgeDirection.UP, "west cover");
                assertHit(helper, rayTraceEast(helper, 0.5, 0.5), PIPE, ForgeDirection.WEST, "west cover side-on");
            })
            .thenSucceed();
    }

    @GameTest(batch = "gt5.pipe")
    public static void walkingCollisionFollowsPipeParts(GameTestHelper helper) {
        final FakePlayer player = placePipeOnHatch(helper);
        placeCover(helper, player, ForgeDirection.WEST);
        placeCover(helper, player, ForgeDirection.EAST);

        helper.startSequence()
            .thenIdle(SETTLE_TICKS)
            .thenExecute(() -> {
                final double gap = gapBesideCore(helper);
                final TestPos pipe = helper.absolute(PIPE);

                // A thin column in the gap between the west cover and the core
                final List<AxisAlignedBB> inGap = collisionBoxes(
                    helper,
                    AxisAlignedBB.getBoundingBox(
                        pipe.x() + gap - 0.01,
                        pipe.y(),
                        pipe.z() + 0.45,
                        pipe.x() + gap + 0.01,
                        pipe.y() + 1,
                        pipe.z() + 0.55));
                helper.assertTrue(inGap.isEmpty(), "Something collides in the gap beside the pipe core: " + inGap);

                // Standing on top of the west cover
                final List<AxisAlignedBB> onCover = collisionBoxes(
                    helper,
                    AxisAlignedBB.getBoundingBox(
                        pipe.x(),
                        pipe.y() + 0.9,
                        pipe.z(),
                        pipe.x() + 0.05,
                        pipe.y() + 1.1,
                        pipe.z() + 1));
                helper.assertEquals(1, onCover.size(), "Collision boxes under the west cover: " + onCover);
                helper.assertEquals(pipe.y() + 1.0, onCover.get(0).maxY, 1e-6, "Top of the west cover");
            })
            .thenSucceed();
    }

    /** Places a pipe on top of an input hatch, connected down into it, and returns the player who placed them. */
    private static FakePlayer placePipeOnHatch(GameTestHelper helper) {
        // Machines remember who placed them, and the input hatch looks up its owner's settings
        final FakePlayer player = helper.spawnFakePlayer("PipeHitboxTester");
        placeMachine(helper, player, HATCH, ItemList.Hatch_Input_LV.get(1));
        // Placed against the top of the hatch, so it connects down into it
        placeMachine(helper, player, PIPE, GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 1));
        helper.assertTrue(pipe(helper).isConnectedAtSide(ForgeDirection.DOWN), "Pipe did not connect to the hatch");
        return player;
    }

    private static void placeMachine(GameTestHelper helper, FakePlayer player, TestPos relativePos, ItemStack stack) {
        helper.assertNotNull(stack, "Missing item for " + relativePos);
        final TestPos pos = helper.absolute(relativePos);
        final boolean placed = ((ItemMachines) stack.getItem()).placeBlockAt(
            stack,
            player,
            helper.getWorld(),
            pos.x(),
            pos.y(),
            pos.z(),
            ForgeDirection.UP.ordinal(),
            0.5f,
            0.5f,
            0.5f,
            0);
        helper.assertTrue(placed, "Failed to place " + stack.getDisplayName() + " at " + relativePos);
    }

    private static void placeCover(GameTestHelper helper, FakePlayer player, ForgeDirection side) {
        final ItemStack cover = ItemList.Cover_Shutter.get(1);
        final IGregTechTileEntity tile = pipeTile(helper);
        helper.assertTrue(
            CoverRegistry.getCoverPlacer(cover)
                .isCoverPlaceable(side, cover, tile),
            "Cover can't be placed on the " + side + " side of the pipe");
        CoverRegistry.getCoverPlacer(cover)
            .placeCover(player, cover, tile, side);
        helper.assertTrue(tile.hasCoverAtSide(side), "No cover on the " + side + " side of the pipe");
    }

    private static IGregTechTileEntity pipeTile(GameTestHelper helper) {
        return helper.gtnh()
            .gtTile(PIPE);
    }

    private static MetaPipeEntity pipe(GameTestHelper helper) {
        return helper.assertInstanceOf(MetaPipeEntity.class, pipeTile(helper).getMetaTileEntity());
    }

    /**
     * Offset from the side of the pipe's block that lies between a cover plate and the pipe core.
     */
    private static double gapBesideCore(GameTestHelper helper) {
        final double coreStart = (1 - pipe(helper).getCollisionThickness()) / 2;
        final double coverDepth = Math.min(1.0 / 8.0, coreStart);
        helper.assertTrue(coverDepth < coreStart, "Pipe is too thick to leave a gap beside its core");
        return (coverDepth + coreStart) / 2;
    }

    /**
     * Traces a ray straight down through the pipe's block, at the given offsets into it, like clicking below it.
     */
    private static MovingObjectPosition rayTraceDown(GameTestHelper helper, double x, double z) {
        final TestPos pipe = helper.absolute(PIPE);
        return helper.getWorld()
            .rayTraceBlocks(
                Vec3.createVectorHelper(pipe.x() + x, pipe.y() + 2, pipe.z() + z),
                Vec3.createVectorHelper(pipe.x() + x, pipe.y() - 0.5, pipe.z() + z));
    }

    /**
     * Traces a ray from the west straight east through the pipe's block, at the given offsets into it.
     */
    private static MovingObjectPosition rayTraceEast(GameTestHelper helper, double y, double z) {
        final TestPos pipe = helper.absolute(PIPE);
        return helper.getWorld()
            .rayTraceBlocks(
                Vec3.createVectorHelper(pipe.x() - 1, pipe.y() + y, pipe.z() + z),
                Vec3.createVectorHelper(pipe.x() + 1.5, pipe.y() + y, pipe.z() + z));
    }

    private static void assertHit(GameTestHelper helper, MovingObjectPosition hit, TestPos expectedRelativePos,
        ForgeDirection expectedSide, String where) {
        final TestPos expected = helper.absolute(expectedRelativePos);
        helper.assertNotNull(hit, "Ray " + where + " hit nothing");
        final TestPos actual = at(hit.blockX, hit.blockY, hit.blockZ);
        helper.assertEquals(expected, actual, "Ray " + where + " hit the wrong block");
        helper.assertEquals(
            expectedSide,
            ForgeDirection.getOrientation(hit.sideHit),
            "Ray " + where + " hit the wrong side");
    }

    private static List<AxisAlignedBB> collisionBoxes(GameTestHelper helper, AxisAlignedBB area) {
        final TestPos pipe = helper.absolute(PIPE);
        final Block block = helper.getWorld()
            .getBlock(pipe.x(), pipe.y(), pipe.z());
        final List<AxisAlignedBB> boxes = new ArrayList<>();
        block.addCollisionBoxesToList(helper.getWorld(), pipe.x(), pipe.y(), pipe.z(), area, boxes, null);
        return boxes;
    }
}
