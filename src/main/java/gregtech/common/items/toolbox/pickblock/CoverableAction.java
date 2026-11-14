package gregtech.common.items.toolbox.pickblock;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.ToolboxSlot;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

/**
 * Action that picks the crowbar if you look at a coverable side, or the supplied mining tool otherwise.
 *
 * @param <V> any {@link IMetaTileEntity}
 */
public class CoverableAction<V extends IMetaTileEntity> extends SimpleAction<V> {

    public CoverableAction(Class<V> targetClass, ToolboxSlot miningTool) {
        super(
            targetClass,
            (metaTileEntity, side) -> metaTileEntity.getBaseMetaTileEntity()
                .hasCoverAtSide(side) ? ImmutableList.of(ToolboxSlot.CROWBAR, miningTool)
                    : ImmutableList.of(miningTool));
    }
}
