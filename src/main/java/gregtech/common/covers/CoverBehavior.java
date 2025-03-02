package gregtech.common.covers;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.ISerializableObject;

/**
 * For Covers with a special behavior. Has fixed storage format of 4 byte. Not very convenient...
 */
public abstract class CoverBehavior extends CoverBehaviorBase<ISerializableObject.LegacyCoverData> {

    protected CoverBehavior(CoverContext context) {
        super(context, ISerializableObject.LegacyCoverData.class, null);
    }

    protected CoverBehavior(CoverContext context, ITexture coverTexture) {
        super(context, ISerializableObject.LegacyCoverData.class, coverTexture);
    }

    protected static int convert(ISerializableObject.LegacyCoverData data) {
        return data == null ? 0 : data.get();
    }

    @Override
    public final ISerializableObject.LegacyCoverData createDataObject() {
        return new ISerializableObject.LegacyCoverData();
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return true;
    }

}
