package gregtech.api.multitileentity;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.mutecore.api.data.Coordinates;
import com.gtnewhorizons.mutecore.shadow.dev.dominion.ecs.api.Entity;

import gregtech.api.multitileentity.data.Structure;

public abstract class StructureHandler implements IAlignment, ISurvivalConstructable {

    Entity entity;

    public StructureHandler(Entity entity) {
        this.entity = entity;
    }

    protected void construct(ItemStack stackSize, int offsetX, int offsetY, int offsetZ, boolean hintsOnly) {
        Coordinates coords = entity.get(Coordinates.class);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return 0;
    }

    protected int survivalConstruct(ItemStack stackSize, int offsetX, int offsetY, int offsetZ, int elementBudget,
        ISurvivalBuildEnvironment env) {
        Coordinates coords = entity.get(Coordinates.class);
        return 0;
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
        return entity.get(Structure.class)
            .getDefinition();
    }
}
