package gregtech.api.structure;

import gregtech.api.casing.ICasingGroup;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;

public interface ISuperChestAcceptor {

    boolean onSuperChestAdded(ICasingGroup group, MTEDigitalChestBase chest, int tier);
}
