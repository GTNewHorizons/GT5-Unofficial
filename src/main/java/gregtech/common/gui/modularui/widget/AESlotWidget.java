package gregtech.common.gui.modularui.widget;

// public class AESlotWidget extends SlotWidget {
//
// public AESlotWidget(BaseSlot slot) {
// super(slot);
// }
//
// @Override
// @SideOnly(Side.CLIENT)
// protected void drawSlot(Slot slotIn) {
// final AppEngRenderItem aeRenderItem = new AppEngRenderItem();
// final RenderItem pIR = this.setItemRender(aeRenderItem);
// try {
// aeRenderItem.setAeStack(Platform.getAEStackInSlot(slotIn));
// super.drawSlot(slotIn, false);
// } catch (final Exception err) {
// AELog.warn("[AppEng] AE prevented crash while drawing slot: " + err);
// }
// this.setItemRender(pIR);
// }
//
// @SideOnly(Side.CLIENT)
// private RenderItem setItemRender(final RenderItem item) {
// final RenderItem ri = ModularGui.getItemRenderer();
// ModularGui.setItemRenderer(item);
// return ri;
// }
// }
