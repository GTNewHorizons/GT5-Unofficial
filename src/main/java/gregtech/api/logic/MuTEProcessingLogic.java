package gregtech.api.logic;

import javax.annotation.Nonnull;

import com.gtnewhorizons.mutecore.MuTECore;
import com.gtnewhorizons.mutecore.api.registry.MultiTileContainer.FakeEntity;
import dev.dominion.ecs.api.Entity;
import dev.dominion.ecs.api.Results;
import dev.dominion.ecs.api.Results.With1;

/**
 * Processing logic class, dedicated for MultiTileEntities.
 */
public abstract class MuTEProcessingLogic<P extends MuTEProcessingLogic<P>> implements Runnable {

    public final void run() {
        Results<? extends With1<?>> results = MuTECore.ENGINE.findEntitiesWith(getProcessingDataClass());
        for (With1<?> result : results) {
            Entity entity = result.entity();
            if (entity.has(FakeEntity.class)) continue;
            if (!validateEntityComponents(entity)) continue;
            process(entity);
        }
    }

    protected abstract Class<?> getProcessingDataClass();

    protected boolean validateEntityComponents(@Nonnull Entity entity) {
        return true;
    }
    public void process(@Nonnull Entity entity) {}

    // #endregion
}
