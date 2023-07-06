package gregtech.common.modularui.widget;

/**
 * Watches specific ItemStack and pulls changes from it. Player cannot interact with slot, other than viewing NEI recipe
 * or adding bookmark.
 */
// public class ItemWatcherSlotWidget extends SlotWidget {
//
// private ItemStack lastItem;
// private Supplier<ItemStack> getter;
//
// public ItemWatcherSlotWidget() {
// super(BaseSlot.phantom(new ItemStackHandler(), 0));
// disableInteraction();
// }
//
// public ItemWatcherSlotWidget setGetter(Supplier<ItemStack> getter) {
// this.getter = getter;
// return this;
// }
//
// @Override
// public void detectAndSendChanges(boolean init) {
// ItemStack target = getter.get();
// if (init || !GT_Utility.areStacksEqual(lastItem, target)) {
// ItemStack toPut;
// if (target != null) {
// toPut = target.copy();
// toPut.stackSize = 1;
// } else {
// toPut = null;
// }
// ((IItemHandlerModifiable) getMcSlot().getItemHandler()).setStackInSlot(0, toPut);
// lastItem = target;
// getMcSlot().onSlotChanged();
// }
// super.detectAndSendChanges(init);
// }
// }
