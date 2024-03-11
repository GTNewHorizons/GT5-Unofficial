package gregtech.common.gui.modularui.widget;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public class ShutDownReasonSyncer extends FakeSyncWidget<ShutDownReason> {

    public ShutDownReasonSyncer(Supplier<ShutDownReason> getter, Consumer<ShutDownReason> setter) {
        super(getter, setter, (buffer, result) -> {
            NetworkUtils.writeStringSafe(buffer, result.getID());
            result.encode(buffer);
        }, buffer -> {
            String id = NetworkUtils.readStringSafe(buffer);
            ShutDownReason result = ShutDownReasonRegistry.getSampleFromRegistry(id)
                .newInstance();
            result.decode(buffer);
            return result;
        });
    }
}
