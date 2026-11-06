package gregtech.api.metatileentity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTModHandler;
import gregtech.common.blocks.BlockMachines;

/**
 * Checks that covers on a pipe don't turn its hitbox into a full block, so the blocks behind the pipe can still be
 * clicked through the gaps between its core, connections and covers.
 */
public class MetaPipeEntityHitboxTest {

    private static final int X = 10, Y = 64, Z = -3;
    // Pipe core spans 0.3125 to 0.6875, cover plates are 0.125 deep
    private static final float THICKNESS = 0.375f;

    private BlockMachines block;
    private World world;
    private BaseMetaPipeEntity baseTE;
    private MetaPipeEntity pipe;

    @BeforeEach
    void setUp() {
        try (MockedStatic<GameRegistry> gameRegistry = mockStatic(GameRegistry.class);
            MockedStatic<GTModHandler> modHandler = mockStatic(GTModHandler.class)) {
            modHandler.when(() -> GTModHandler.getIC2Coolant(anyLong()))
                .thenReturn(mock(FluidStack.class));
            block = new BlockMachines();
        }

        pipe = mock(MetaPipeEntity.class, CALLS_REAL_METHODS);
        baseTE = mock(BaseMetaPipeEntity.class);
        doReturn(THICKNESS).when(pipe)
            .getCollisionThickness();
        doReturn(baseTE).when(pipe)
            .getBaseMetaTileEntity();
        // Not holding a tool. The real check needs a running client.
        doReturn(false).when(pipe)
            .boundingBoxShouldBeFullBlock();
        when(baseTE.canAccessData()).thenReturn(true);
        when(baseTE.getMetaTileEntity()).thenReturn(pipe);

        world = mock(World.class);
        when(world.getTileEntity(X, Y, Z)).thenReturn(baseTE);
    }

    private void connect(ForgeDirection... sides) {
        for (ForgeDirection side : sides) baseTE.mConnections |= side.flag;
    }

    private void cover(ForgeDirection... sides) {
        for (ForgeDirection side : sides) when(baseTE.hasCoverAtSide(side)).thenReturn(true);
    }

    /** Raytraces a straight line between two points given relative to the pipe's block. */
    private MovingObjectPosition rayTrace(double x1, double y1, double z1, double x2, double y2, double z2) {
        return block.collisionRayTrace(
            world,
            X,
            Y,
            Z,
            Vec3.createVectorHelper(X + x1, Y + y1, Z + z1),
            Vec3.createVectorHelper(X + x2, Y + y2, Z + z2));
    }

    /** Raytraces straight down through the pipe's block, like clicking the block below it. */
    private MovingObjectPosition rayTraceDown(double x, double z) {
        return rayTrace(x, 2, z, x, -1, z);
    }

    @Test
    void opposingCoversLeaveGapsOpen() {
        connect(ForgeDirection.DOWN);
        cover(ForgeDirection.WEST, ForgeDirection.EAST);

        assertNull(rayTraceDown(0.2, 0.5), "gap between west cover and core");
        assertNull(rayTraceDown(0.8, 0.5), "gap between core and east cover");
        assertNull(rayTraceDown(0.5, 0.1), "gap north of core");
    }

    @Test
    void coverOppositeConnectionLeavesGapsOpen() {
        connect(ForgeDirection.DOWN);
        cover(ForgeDirection.UP);

        // Horizontal rays from west to east, beside the core and between the down arm and the up cover
        assertNull(rayTrace(-1, 0.5, 0.1, 2, 0.5, 0.1), "beside the core");
        assertNull(rayTrace(-1, 0.8, 0.5, 2, 0.8, 0.5), "between the core and the up cover");
    }

    @Test
    void hitsCore() {
        connect(ForgeDirection.DOWN);
        cover(ForgeDirection.WEST, ForgeDirection.EAST);

        MovingObjectPosition hit = rayTraceDown(0.5, 0.5);
        assertNotNull(hit);
        assertEquals(MovingObjectPosition.MovingObjectType.BLOCK, hit.typeOfHit);
        assertEquals(X, hit.blockX);
        assertEquals(Y, hit.blockY);
        assertEquals(Z, hit.blockZ);
        assertEquals(ForgeDirection.UP.ordinal(), hit.sideHit);
        assertEquals(Y + 0.6875, hit.hitVec.yCoord, 1e-6);
    }

    @Test
    void hitsConnection() {
        connect(ForgeDirection.DOWN);

        // From the west at the height of the down arm, below the core
        MovingObjectPosition hit = rayTrace(-1, 0.1, 0.5, 2, 0.1, 0.5);
        assertNotNull(hit);
        assertEquals(ForgeDirection.WEST.ordinal(), hit.sideHit);
        assertEquals(X + 0.3125, hit.hitVec.xCoord, 1e-6);
    }

    @Test
    void hitsCoverPlate() {
        connect(ForgeDirection.DOWN);
        cover(ForgeDirection.WEST, ForgeDirection.EAST);

        MovingObjectPosition hit = rayTraceDown(0.05, 0.9);
        assertNotNull(hit);
        assertEquals(ForgeDirection.UP.ordinal(), hit.sideHit);
        assertEquals(Y + 1, hit.hitVec.yCoord, 1e-6);
    }

    @Test
    void hitsNearestPart() {
        cover(ForgeDirection.WEST);

        // From the west through the middle, the cover plate is in front of the core
        MovingObjectPosition hit = rayTrace(-1, 0.5, 0.5, 2, 0.5, 0.5);
        assertNotNull(hit);
        assertEquals(ForgeDirection.WEST.ordinal(), hit.sideHit);
        assertEquals(X, hit.hitVec.xCoord, 1e-6);

        // From the east, the core is in front of the cover plate
        hit = rayTrace(2, 0.5, 0.5, -1, 0.5, 0.5);
        assertNotNull(hit);
        assertEquals(ForgeDirection.EAST.ordinal(), hit.sideHit);
        assertEquals(X + 0.6875, hit.hitVec.xCoord, 1e-6);
    }

    @Test
    void fullBlockPipeIsHitAnywhere() {
        // Holding a tool, or a pipe as thick as a block
        doReturn(true).when(pipe)
            .boundingBoxShouldBeFullBlock();
        connect(ForgeDirection.DOWN);
        cover(ForgeDirection.WEST, ForgeDirection.EAST);

        MovingObjectPosition hit = rayTraceDown(0.05, 0.05);
        assertNotNull(hit);
        assertEquals(ForgeDirection.UP.ordinal(), hit.sideHit);
        assertEquals(Y + 1, hit.hitVec.yCoord, 1e-6);
    }

    @Test
    void collisionLeavesGapsOpen() {
        connect(ForgeDirection.DOWN);
        cover(ForgeDirection.WEST, ForgeDirection.EAST);

        List<AxisAlignedBB> boxes = new ArrayList<>();
        // A thin column in the gap between the west cover and the core
        AxisAlignedBB gap = AxisAlignedBB.getBoundingBox(X + 0.15, Y, Z + 0.4, X + 0.25, Y + 1, Z + 0.6);
        block.addCollisionBoxesToList(world, X, Y, Z, gap, boxes, null);
        assertTrue(boxes.isEmpty(), "gap should not collide, got " + boxes);

        // Standing on top of the west cover
        AxisAlignedBB feet = AxisAlignedBB.getBoundingBox(X, Y + 0.9, Z, X + 0.1, Y + 1.1, Z + 1);
        block.addCollisionBoxesToList(world, X, Y, Z, feet, boxes, null);
        assertEquals(1, boxes.size());
        assertEquals(Y + 1, boxes.get(0).maxY, 1e-6);
    }
}
