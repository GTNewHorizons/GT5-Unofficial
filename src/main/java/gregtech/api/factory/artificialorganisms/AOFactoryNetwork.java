package gregtech.api.factory.artificialorganisms;

import java.util.ArrayList;

import gregtech.api.factory.standard.StandardFactoryNetwork;
import gregtech.api.objects.ArtificialOrganism;

public class AOFactoryNetwork extends StandardFactoryNetwork<AOFactoryNetwork, AOFactoryElement, AOFactoryGrid> {

    private final ArrayList<ArtificialOrganism> species = new ArrayList<>();
    public Boolean valid = false;

    public ArtificialOrganism getSpecies() {
        return species.get(0);
    }

    public void addSpecies(ArtificialOrganism species) {
        this.species.add(species);
        valid = this.species.size() == 1;
    }
}
