package gregtech.common.items.toolbox.pickblock;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolboxSlot;
import mrtjp.projectred.integration.GatePart;

public class ProjectRedAction extends DependentAction {

    public ProjectRedAction() {
        super(Mods.ProjectRedIntegration, new SimpleAction<>(TileMultipart.class, ProjectRedAction::onApply));
    }

    private static List<ToolboxSlot> onApply(TileMultipart tile, ForgeDirection side) {
        for (TMultiPart part : tile.jPartList()) {
            if (part instanceof GatePart) {
                return ImmutableList.of(ToolboxSlot.SCREWDRIVER);
            }
        }

        return ImmutableList.of();
    }
}
