package gregtech.common.covers;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.ISerializableObject;

/**
 * For Covers with a special behavior. Has fixed storage format of 4 byte. Not very convenient...
 */
public abstract class CoverBehavior extends CoverBehaviorBase<ISerializableObject.LegacyCoverData> {

    protected CoverBehavior(CoverContext context) {
        super(context, null);
    }

    protected CoverBehavior(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public int getVariable() {
        return convert(coverData);
    }

    public CoverBehavior setVariable(int newValue) {
        this.coverData.set(newValue);
        return this;
    }

    protected static int convert(ISerializableObject.LegacyCoverData data) {
        return data == null ? 0 : data.get();
    }

    @Override
    public final ISerializableObject.LegacyCoverData initializeData() {
        return new ISerializableObject.LegacyCoverData();
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return true;
    }

    public static CoverBehavior adaptCover(Cover cover) {
        if (cover instanceof CoverBehavior adapterCover) {
            return adapterCover;
        }
        return null;
    }
}
