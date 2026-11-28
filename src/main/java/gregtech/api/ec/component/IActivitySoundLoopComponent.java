package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;

public interface IActivitySoundLoopComponent extends IUniqueComponent {

    ComponentKey.UniqueKey<IActivitySoundLoopComponent> KEY = ComponentKey.unique();

    /// Cleans up old sound loops and starts new ones if needed. Called once per tick.
    void startActivitySound();

    void restartActivitySound();
}
