package gregtech.common.items.toolbox.pickblock;

import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolboxSlot;
import mods.railcraft.common.blocks.tracks.TileTrack;

public class RailcraftAction extends DependentAction {

    public RailcraftAction() {
        super(Mods.Railcraft, () -> new SimpleAction<>(TileTrack.class, ToolboxSlot.CROWBAR));
    }
}
