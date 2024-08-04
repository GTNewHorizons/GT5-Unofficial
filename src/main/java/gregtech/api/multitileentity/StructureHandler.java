package gregtech.api.multitileentity;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.mutecore.api.data.Coordinates;
import com.gtnewhorizons.mutecore.api.data.WorldContainer;
import dev.dominion.ecs.api.Entity;

import gregtech.api.multitileentity.data.Structure;
import gregtech.api.multitileentity.data.TooltipComponent;

public abstract class StructureHandler implements IAlignment, ISurvivalConstructable {

    private final Entity entity;

    public StructureHandler(Entity entity) {
        this.entity = entity;
    }

    protected final void construct(ItemStack stackSize, String piece, int offsetX, int offsetY, int offsetZ, boolean hintsOnly) {
        Coordinates coords = entity.get(Coordinates.class);
        getStructureDefinition().buildOrHints(entity, stackSize, piece, entity.get(WorldContainer.class).getWorld(), entity.get(ExtendedFacing.class), coords.getX(), coords.getY(), coords.getZ(), offsetX, offsetY, offsetZ, hintsOnly);
    }

    protected final int survivalConstruct(ItemStack stackSize, String piece, int offsetX, int offsetY, int offsetZ, int elementBudget,
        ISurvivalBuildEnvironment env) {
        Coordinates coords = entity.get(Coordinates.class);
        return getStructureDefinition().survivalBuild(entity, stackSize, piece, entity.get(WorldContainer.class).getWorld(), entity.get(ExtendedFacing.class), coords.getX(), coords.getY(), coords.getZ(), offsetX, offsetY, offsetZ, elementBudget, env, true);
    }

    @Override
    public final String[] getStructureDescription(ItemStack stackSize) {
        return entity.get(TooltipComponent.class).getTooltip().getStructureHint();
    }

    @Override
    public final ExtendedFacing getExtendedFacing() {
        return entity.get(ExtendedFacing.class);
    }

    @Override
    public final void setExtendedFacing(ExtendedFacing alignment) {
        if (!getAlignmentLimits().isNewExtendedFacingValid(alignment)) return;
        entity.removeType(ExtendedFacing.class);
        entity.add(alignment);
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return IAlignmentLimits.UNLIMITED;
    }

    @Override
    public abstract IStructureDefinition<Entity> getStructureDefinition();
}
