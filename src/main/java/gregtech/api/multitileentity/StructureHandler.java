package gregtech.api.multitileentity;

import net.minecraft.item.ItemStack;

import com.badlogic.ashley.core.Entity;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.mutecore.api.data.Coordinates;
import com.gtnewhorizons.mutecore.api.data.WorldContainer;

import gregtech.api.multitileentity.data.ExtendedFacingComponent;
import gregtech.api.multitileentity.data.TooltipComponent;

public abstract class StructureHandler implements IAlignment, ISurvivalConstructable {

    private final Entity entity;

    public StructureHandler(Entity entity) {
        this.entity = entity;
    }

    protected final void construct(ItemStack stackSize, String piece, int offsetX, int offsetY, int offsetZ,
        boolean hintsOnly) {
        Coordinates coords = entity.getComponent(Coordinates.class);
        getStructureDefinition().buildOrHints(
            entity,
            stackSize,
            piece,
            entity.getComponent(WorldContainer.class)
                .getWorld(),
            entity.getComponent(ExtendedFacingComponent.class)
                .getExtendedFacing(),
            coords.getX(),
            coords.getY(),
            coords.getZ(),
            offsetX,
            offsetY,
            offsetZ,
            hintsOnly);
    }

    protected final int survivalConstruct(ItemStack stackSize, String piece, int offsetX, int offsetY, int offsetZ,
        int elementBudget, ISurvivalBuildEnvironment env) {
        Coordinates coords = entity.getComponent(Coordinates.class);
        return getStructureDefinition().survivalBuild(
            entity,
            stackSize,
            piece,
            entity.getComponent(WorldContainer.class)
                .getWorld(),
            entity.getComponent(ExtendedFacingComponent.class)
                .getExtendedFacing(),
            coords.getX(),
            coords.getY(),
            coords.getZ(),
            offsetX,
            offsetY,
            offsetZ,
            elementBudget,
            env,
            true);
    }

    @Override
    public final String[] getStructureDescription(ItemStack stackSize) {
        return entity.getComponent(TooltipComponent.class)
            .getTooltip()
            .getStructureHint();
    }

    @Override
    public final ExtendedFacing getExtendedFacing() {
        return entity.getComponent(ExtendedFacingComponent.class)
            .getExtendedFacing();
    }

    @Override
    public final void setExtendedFacing(ExtendedFacing alignment) {
        if (!getAlignmentLimits().isNewExtendedFacingValid(alignment)) return;
        entity.remove(ExtendedFacingComponent.class);
        entity.add(new ExtendedFacingComponent(alignment));
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return IAlignmentLimits.UNLIMITED;
    }

    @Override
    public abstract IStructureDefinition<Entity> getStructureDefinition();
}
