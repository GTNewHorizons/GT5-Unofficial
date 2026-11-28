package gregtech.api.ec.component;

import java.util.Comparator;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IOnServerPostTickComponent extends IComponent {

    ComponentKey.ListKey<IOnServerPostTickComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IOnServerPostTickComponent::getServerTickPriority));

    enum ServerTickPriority {

        STRUCTURE_CHECK(1000),
        MAINTAIN(2000),
        RECIPE(3000);

        public final int priority;

        ServerTickPriority(int priority) {
            this.priority = priority;
        }
    }

    void onServerPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick);

    int getServerTickPriority();
}
