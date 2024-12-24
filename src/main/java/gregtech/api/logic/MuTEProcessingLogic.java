package gregtech.api.logic;

import javax.annotation.Nonnull;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.gtnewhorizons.mutecore.MuTECore;

import gregtech.api.multitileentity.data.ProcessingData;

/**
 * Processing logic class, dedicated for MultiTileEntities.
 */
public abstract class MuTEProcessingLogic<P extends MuTEProcessingLogic<P>> extends EntitySystem {

    @Override
    public final void update(float deltaTime) {
        ImmutableArray<Entity> results = MuTECore.ENGINE.getEntitiesFor(
            Family.one(getProcessingDataClass())
                .get());
        for (Entity entity : results) {
            if (!validateEntityComponents(entity)) continue;
            process(entity);
        }
    }

    protected abstract Class<? extends ProcessingData> getProcessingDataClass();

    protected boolean validateEntityComponents(@Nonnull Entity entity) {
        return true;
    }

    protected abstract void process(@Nonnull Entity entity);

    // #endregion
}
