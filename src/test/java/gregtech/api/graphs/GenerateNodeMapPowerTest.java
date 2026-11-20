package gregtech.api.graphs;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.junit.jupiter.api.Test;

class GenerateNodeMapPowerTest {

    @Test
    void crossChunkIc2EmitterIsReadOnlyWhenItsChunkIsLoaded() {
        World world = mock(World.class);
        TileEntity emitter = mock(TileEntity.class);
        TileEntity sink = mock(TileEntity.class);
        sink.xCoord = 15;
        sink.yCoord = 64;
        when(sink.getWorldObj()).thenReturn(world);
        when(world.blockExists(16, 64, 0)).thenReturn(true);
        when(world.getTileEntity(16, 64, 0)).thenReturn(emitter);

        assertSame(emitter, GenerateNodeMapPower.getAdjacentTileEntityIfLoaded(sink, ForgeDirection.EAST));
        verify(world).getTileEntity(16, 64, 0);

        clearInvocations(world);
        when(world.blockExists(16, 64, 0)).thenReturn(false);
        assertNull(GenerateNodeMapPower.getAdjacentTileEntityIfLoaded(sink, ForgeDirection.EAST));
        verify(world, never()).getTileEntity(16, 64, 0);
    }
}
