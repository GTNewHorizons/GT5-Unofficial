package gregtech.common.items.toolbox.pickblock;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.ToolboxSlot;

public interface IDeciderAction {

    boolean isValid(Object obj);

    List<ToolboxSlot> apply(Object obj, ForgeDirection side);
}
