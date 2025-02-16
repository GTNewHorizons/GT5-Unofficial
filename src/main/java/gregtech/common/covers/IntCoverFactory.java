package gregtech.common.covers;

import gregtech.api.covers.CoverFactoryBase;
import gregtech.api.util.ISerializableObject;

public class IntCoverFactory extends CoverFactoryBase<ISerializableObject.LegacyCoverData> {

    @Override
    public final ISerializableObject.LegacyCoverData createDataObject() {
        return new ISerializableObject.LegacyCoverData();
    }
}
