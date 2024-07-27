package gregtech.api.multitileentity.data;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Entity;

public class Structure {


    private IStructureDefinition<Entity> definition;

    public Structure(IStructureDefinition<Entity> definition) {
        this.definition = definition;
    }

    public IStructureDefinition<Entity> getDefinition() {
        return definition;
    }
}
