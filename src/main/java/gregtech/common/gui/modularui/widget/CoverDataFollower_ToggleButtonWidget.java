package gregtech.common.gui.modularui.widget;

// public class CoverDataFollower_ToggleButtonWidget<T extends ISerializableObject> extends CoverCycleButtonWidget
// implements IDataFollowerWidget<T, Boolean> {
//
// private Function<T, Boolean> dataToStateGetter;
//
// public CoverDataFollower_ToggleButtonWidget() {
// super();
// setGetter(() -> 0); // fake getter; used only for init
// setSynced(false, false);
// setLength(2);
// }
//
// @Override
// public CoverDataFollower_ToggleButtonWidget<T> setDataToStateGetter(Function<T, Boolean> dataToStateGetter) {
// this.dataToStateGetter = dataToStateGetter;
// return this;
// }
//
// @Override
// public CoverDataFollower_ToggleButtonWidget<T> setStateSetter(Consumer<Boolean> setter) {
// super.setSetter(val -> setter.accept(val == 1));
// return this;
// }
//
// @Override
// public void updateState(T data) {
// setState(dataToStateGetter.apply(data) ? 1 : 0, false, false);
// }
//
// public CoverDataFollower_ToggleButtonWidget<T> setToggleTexture(IDrawable active, IDrawable inactive) {
// setTextureGetter(state -> state == 1 ? active : inactive);
// return this;
// }
//
// public static <T extends ISerializableObject> CoverDataFollower_ToggleButtonWidget<T> ofCheckAndCross() {
// return new CoverDataFollower_ToggleButtonWidget<T>()
// .setToggleTexture(GT_UITextures.OVERLAY_BUTTON_CHECKMARK, GT_UITextures.OVERLAY_BUTTON_CROSS);
// }
//
// public static <T extends ISerializableObject> CoverDataFollower_ToggleButtonWidget<T> ofCheck() {
// return new CoverDataFollower_ToggleButtonWidget<T>()
// .setToggleTexture(GT_UITextures.OVERLAY_BUTTON_CHECKMARK, GT_UITextures.TRANSPARENT);
// }
//
// public static <T extends ISerializableObject> CoverDataFollower_ToggleButtonWidget<T> ofRedstone() {
// return new CoverDataFollower_ToggleButtonWidget<T>()
// .setToggleTexture(GT_UITextures.OVERLAY_BUTTON_REDSTONE_ON, GT_UITextures.OVERLAY_BUTTON_REDSTONE_OFF);
// }
//
// public static <T extends ISerializableObject> CoverDataFollower_ToggleButtonWidget<T> ofDisableable() {
// return new CoverDataFollower_DisableableToggleButtonWidget<>();
// }
//
// /**
// * Disables clicking if button is already pressed.
// */
// public static class CoverDataFollower_DisableableToggleButtonWidget<T extends ISerializableObject>
// extends CoverDataFollower_ToggleButtonWidget<T> {
//
// public CoverDataFollower_DisableableToggleButtonWidget() {
// super();
// }
//
// @Override
// protected boolean canClick() {
// return getState() == 0;
// }
//
// @Override
// public IDrawable[] getBackground() {
// if (!canClick()) return new IDrawable[] { GT_UITextures.BUTTON_COVER_NORMAL_DISABLED };
// return super.getBackground();
// }
// }
// }
