package kubatech.loaders;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import kubatech.api.utils.ModUtils;
import kubatech.loaders.item.arcfurnace.ElectrodeItem;

public class ArcFurnaceLoader {

    public static final ElectrodeItem ARC_FURNACE_ELECTRODE = new ElectrodeItem();

    public static void load() {
        GameRegistry.registerItem(ARC_FURNACE_ELECTRODE, "arc_furnace_electrode");
        if (ModUtils.isClientSided) GTMod.clientProxy().metaItemRenderer.registerItem(ARC_FURNACE_ELECTRODE);
        ArcFurnaceElectrode.registerElectrodes();
    }

}
