package gregtech.common.gui.modularui.widget;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public class CheckRecipeResultSyncer extends FakeSyncWidget<CheckRecipeResult> {

    public CheckRecipeResultSyncer(Supplier<CheckRecipeResult> getter, Consumer<CheckRecipeResult> setter) {
        super(getter, setter, (buffer, result) -> {
            NetworkUtils.writeStringSafe(buffer, result.getID());
            result.encode(buffer);
        }, buffer -> {
            String id = NetworkUtils.readStringSafe(buffer);
            CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(id)
                .newInstance();
            result.decode(buffer);
            return result;
        });
    }
}
