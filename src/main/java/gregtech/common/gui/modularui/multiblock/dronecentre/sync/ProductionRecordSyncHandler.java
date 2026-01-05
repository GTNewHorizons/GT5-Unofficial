package gregtech.common.gui.modularui.multiblock.dronecentre.sync;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.GenericSyncValue;

import gregtech.common.tileentities.machines.multi.drone.production.ProductionRecord;

public class ProductionRecordSyncHandler extends GenericSyncValue<ProductionRecord> {

    public ProductionRecordSyncHandler(@NotNull Supplier<ProductionRecord> getter) {
        super(
            getter,
            null,
            ProductionRecord::deserialize,
            ProductionRecord::serialize,
            ProductionRecord::areEqual,
            null);
    }
}
