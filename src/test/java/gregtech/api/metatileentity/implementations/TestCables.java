package gregtech.api.metatileentity.implementations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import gregtech.common.blocks.PipeShapeBlock;

/// Builds uninsulated [MTECable]s with fixed loss, amperage and voltage for tests that run without MaterialLib.
///
/// A cable's stats come from its shape's material, which tests have none of, so each cable is a Mockito spy with
/// the stat getters stubbed. A spy keeps the runtime class [MTECable], so [MTECable#isBuiltinCable] holds as it
/// does for real cables; a test-only subclass would be treated as an addon cable. Subclasses that are addon cables
/// on purpose build on [#wireShape].
public final class TestCables {

    private TestCables() {}

    /// A mocked wire shape to construct an [MTECable] with.
    public static PipeShapeBlock wireShape() {
        PipeShapeBlock shape = mock(PipeShapeBlock.class);
        when(shape.getFamily()).thenReturn(PipeShapeBlock.PipeFamily.WIRE);
        when(shape.getThickness()).thenReturn(0.5F);
        return shape;
    }

    public static MTECable cable(String name, long cableLoss, long amperage, long voltage) {
        return withStats(spy(new MTECable(name, wireShape())), cableLoss, amperage, voltage);
    }

    /// Stubs the stats of `cable`, which must be a spy, and makes its [MTECable#newMetaEntity] copies carry them.
    public static MTECable withStats(MTECable cable, long cableLoss, long amperage, long voltage) {
        doReturn(cableLoss).when(cable)
            .getCableLoss();
        doReturn(amperage).when(cable)
            .getAmperage();
        doReturn(voltage).when(cable)
            .getVoltage();
        doReturn(false).when(cable)
            .canShock();
        String name = cable.mName;
        doAnswer(call -> cable(name, cableLoss, amperage, voltage)).when(cable)
            .newMetaEntity(any());
        return cable;
    }
}
