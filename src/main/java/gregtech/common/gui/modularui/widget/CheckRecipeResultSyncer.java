package gregtech.common.gui.modularui.widget;

// public class CheckRecipeResultSyncer extends FakeSyncWidget<CheckRecipeResult> {
//
// public CheckRecipeResultSyncer(Supplier<CheckRecipeResult> getter, Consumer<CheckRecipeResult> setter) {
// super(getter, setter, (buffer, result) -> {
// NetworkUtils.writeStringSafe(buffer, result.getID());
// result.encode(buffer);
// }, buffer -> {
// String id = NetworkUtils.readStringSafe(buffer);
// CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(id)
// .newInstance();
// result.decode(buffer);
// return result;
// });
// }
// }
