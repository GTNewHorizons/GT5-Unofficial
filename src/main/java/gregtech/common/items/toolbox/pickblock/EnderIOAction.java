package gregtech.common.items.toolbox.pickblock;

import crazypants.enderio.conduit.TileConduitBundle;
import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolboxSlot;

public class EnderIOAction extends DependentAction {

    public EnderIOAction() {
        super(Mods.EnderIO, new SimpleAction<>(TileConduitBundle.class, ToolboxSlot.WRENCH));
    }
}
