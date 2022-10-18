package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import gregtech.api.gui.ModularUI.GT_UITextures;
import gregtech.api.util.ISerializableObject;

public class CoverDataFollower_ToggleButtonWidget<T extends ISerializableObject>
        extends CoverDataFollower_CycleButtonWidget<T> {

    public CoverDataFollower_ToggleButtonWidget() {
        super();
        setLength(2);
    }

    public CoverDataFollower_ToggleButtonWidget<T> setToggleTexture(IDrawable active, IDrawable inactive) {
        setTextureGetter(state -> state == 1 ? active : inactive);
        return this;
    }

    public static <T extends ISerializableObject> CoverDataFollower_ToggleButtonWidget<T> ofCheckAndCross() {
        return new CoverDataFollower_ToggleButtonWidget<T>()
                .setToggleTexture(GT_UITextures.OVERLAY_BUTTON_CHECKMARK, GT_UITextures.OVERLAY_BUTTON_CROSS);
    }

    public static <T extends ISerializableObject> CoverDataFollower_ToggleButtonWidget<T> ofCheck() {
        return new CoverDataFollower_ToggleButtonWidget<T>()
                .setToggleTexture(GT_UITextures.OVERLAY_BUTTON_CHECKMARK, GT_UITextures.SLOT_TRANSPARENT);
    }

    public static <T extends ISerializableObject> CoverDataFollower_ToggleButtonWidget<T> ofRedstone() {
        return new CoverDataFollower_ToggleButtonWidget<T>()
                .setToggleTexture(GT_UITextures.OVERLAY_BUTTON_REDSTONE_ON, GT_UITextures.OVERLAY_BUTTON_REDSTONE_OFF);
    }

    public static <T extends ISerializableObject> CoverDataFollower_ToggleButtonWidget<T> ofDisableable() {
        return new CoverDataFollower_DisableableToggleButtonWidget<T>();
    }

    /**
     * Disables clicking if button is already pressed.
     */
    public static class CoverDataFollower_DisableableToggleButtonWidget<T extends ISerializableObject>
            extends CoverDataFollower_ToggleButtonWidget<T> {

        public CoverDataFollower_DisableableToggleButtonWidget() {
            super();
        }

        @Override
        protected boolean canClick() {
            return getState() == 0;
        }

        @Override
        public IDrawable[] getBackground() {
            if (!canClick()) return new IDrawable[] {GT_UITextures.BUTTON_COVER_NORMAL_DISABLED};
            return super.getBackground();
        }
    }
}
