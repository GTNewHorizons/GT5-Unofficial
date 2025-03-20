package gregtech.common.covers;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
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
        return coverData.get();
    }

    public CoverBehavior setVariable(int newValue) {
        this.coverData.set(newValue);
        return this;
    }

    @Override
    public final ISerializableObject.LegacyCoverData initializeData() {
        return new ISerializableObject.LegacyCoverData();
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return true;
    }

    protected static abstract class CoverBehaviorUIFactory extends UIFactory<CoverBehavior> {

        protected CoverBehaviorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        public CoverBehavior adaptCover(Cover cover) {
            if (cover instanceof CoverBehavior adapterCover) {
                return adapterCover;
            }
            return null;
        }
    }
}
