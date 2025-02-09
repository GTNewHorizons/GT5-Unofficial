package gregtech.api.factory.artificialorganisms;

import gregtech.api.factory.standard.StandardFactoryNetwork;
import gregtech.api.objects.ArtificialOrganism;

import java.util.HashSet;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

public class AOFactoryNetwork extends StandardFactoryNetwork<AOFactoryNetwork, AOFactoryElement, AOFactoryGrid> {

    /**
     * Queries for AO output components. If there are none, or if there are more than one, this method will return null.
     * If there is exactly one AO output, returns its current species.
     */
    public ArtificialOrganism getAO() {
        IAOStorageComponent[] outputs = getComponents(IAOStorageComponent.class).toArray(new IAOStorageComponent[0]);
        return (outputs.length == 1) ? outputs[0].getAO() : null;
    }

    public void initiateHavoc() {
        // Copy the set first so that any network updates don't cause concurrent modification
        HashSet<AOFactoryElement> oldElements = new HashSet<>(elements);
        for (AOFactoryElement e : oldElements) {
            if (XSTR_INSTANCE.nextInt(4) == 0) e.havocEvent();
        }
    }
}
