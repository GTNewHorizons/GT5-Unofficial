package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IClientSoundComponent extends IComponent {

    ComponentKey.ListKey<IClientSoundComponent> KEY = ComponentKey.list();

    boolean onDoSound(byte aIndex, double aX, double aY, double aZ);

    boolean onStartSoundLoop(byte aIndex, double aX, double aY, double aZ);
}
