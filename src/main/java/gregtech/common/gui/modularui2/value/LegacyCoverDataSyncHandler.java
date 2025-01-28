package gregtech.common.gui.modularui2.value;

import gregtech.api.gui.modularui2.CoverGuiData;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.ISerializableObject;

public class LegacyCoverDataSyncHandler extends CoverDataSyncHandler<ISerializableObject.LegacyCoverData> {

    public LegacyCoverDataSyncHandler(CoverBehavior coverBehavior, CoverGuiData guiData) {
        super(coverBehavior, guiData);
    }
}
