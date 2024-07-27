package gregtech.api.multitileentity;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.mutecore.api.data.Coordinates;
import com.gtnewhorizons.mutecore.api.data.WorldContainer;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Entity;

import gregtech.api.multitileentity.data.Structure;
import net.minecraft.item.ItemStack;

public class StructureHandler implements IAlignment, ISurvivalConstructable {

    Entity entity;

    public StructureHandler(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        Coordinates coords = entity.get(Coordinates.class);
        getStructureDefinition().buildOrHints(entity, stackSize, entity.get(WorldContainer.class).getWorld(), getExtendedFacing(), coords.getX(), coords.getY(), coords.getZ(), hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return ISurvivalConstructable.super.survivalConstruct(stackSize, elementBudget, env);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return null;
    }

    @Override
    public ExtendedFacing getExtendedFacing() {
        return entity.get(ExtendedFacing.class);
    }

    @Override
    public void setExtendedFacing(ExtendedFacing alignment) {
        entity.removeType(ExtendedFacing.class);
        entity.add(alignment);
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return IAlignmentLimits.UNLIMITED;
    }

    @Override
    public IStructureDefinition<Entity> getStructureDefinition() {
        return entity.get(Structure.class).getDefinition();
    }
}
