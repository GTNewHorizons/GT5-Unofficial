package gregtech.api.gui;

import gregtech.api.gui.widgets.GT_GuiFakeItemButton;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GT_GUIDialogSelectItem extends GT_GUIScreen {
	public static final int UNSELECTED = -1;
	private static final int cols = 9;
	private static final int rows = 3;
	private final GuiScreen parent;
	private final Consumer<ItemStack> selectedCallback;
	// passed in stack
	private final List<ItemStack> stacks;
	// all slots not including btnCurrent
	private final List<GT_GuiFakeItemButton> slots = new ArrayList<>();
	// the currently selected slot content
	private final GT_GuiFakeItemButton btnCurrent = new GT_GuiFakeItemButton(this, 8, 25, GT_GuiIcon.SLOT_DARKGRAY).setMimicSlot(true);
	private final boolean noDeselect;
	private int selected;
	private int scroll = 0;
	private GT_GuiIconButton btnUp;
	private GT_GuiIconButton btnDown;


	public GT_GUIDialogSelectItem(String header, ItemStack headerItem, GuiScreen parent, Consumer<ItemStack> selectedCallback, List<ItemStack> stacks) {
		this(header, headerItem, parent, selectedCallback, stacks, UNSELECTED);
	}

	public GT_GUIDialogSelectItem(String header, ItemStack headerItem, GuiScreen parent, Consumer<ItemStack> selectedCallback, List<ItemStack> stacks, int selected) {
		this(header, headerItem, parent, selectedCallback, stacks, selected, false);
	}

	/**
	 * Open a dialog to select an item from given list. Given callback may be called zero or more times depending on user action.
	 *  @param header           Header text
	 * @param headerItem       ItemStack to use as Dialog icon
	 * @param parent           open which GUIScreen when this dialog is closed. use null if it has no parent.
	 * @param selectedCallback callback upon selected
	 * @param stacks           list to choose from
	 * @param selected         preselected item. Use {@link #UNSELECTED} for unselected. Invalid selected will be clamped to 0 or highest index
	 * @param noDeselect	   true if player cannot deselect, false otherwise. If this is set to true, selectedCallback is guaranteed to be called with a nonnull stack
	 */
	public GT_GUIDialogSelectItem(String header, ItemStack headerItem, GuiScreen parent, Consumer<ItemStack> selectedCallback, List<ItemStack> stacks, int selected, boolean noDeselect) {
		super(176, 107, header);
		this.noDeselect = noDeselect;
		if (headerItem != null)
			this.headerIcon.setItem(headerItem);
		this.parent = parent;
		this.selectedCallback = selectedCallback;
		this.stacks = stacks;

		if (stacks.size() > rows * cols) {
			btnUp = new GT_GuiIconButton(this, 0, 134, 25, GT_GuiIcon.GREEN_ARROW_UP);
			btnDown = new GT_GuiIconButton(this, 1, 152, 25, GT_GuiIcon.GREEN_ARROW_DOWN);
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				slots.add(new GT_GuiFakeItemButton(this, 8 + 18 * j, 44 + 18 * i, GT_GuiIcon.SLOT_GRAY).setMimicSlot(true));
			}
		}

		setSelected(noDeselect ? Math.max(0, selected) : selected);
		ensureSelectedDisplayed();
	}

	@Override
	protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
		btnCurrent.setX(8 + 2 + fontRendererObj.getStringWidth(StatCollector.translateToLocal("GT5U.gui.select.current")));
	}

	@Override
	public void closeScreen() {
		selectedCallback.accept(getCandidate(getSelected()));
		mc.displayGuiScreen(parent);
		if (parent == null)
			mc.setIngameFocus();
	}

	@Override
	public void buttonClicked(GuiButton button) {
		switch (button.id) {
			case 0:
				setScroll(scroll - 1);
				return;
			case 1:
				setScroll(scroll + 1);
				return;
		}
		super.buttonClicked(button);
	}

	@Override
	public void drawExtras(int mouseX, int mouseY, float parTicks) {
		int y = 25 + (18 - getFontRenderer().FONT_HEIGHT) / 2;
		getFontRenderer().drawString(StatCollector.translateToLocal("GT5U.gui.select.current"), 8, y, 0xff555555);
		super.drawExtras(mouseX, mouseY, parTicks);
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		int mx = x - guiLeft, my = y - guiTop;
		if (button == 0) {
			if (btnCurrent.getBounds().contains(mx, my)) {
				ensureSelectedDisplayed();
				return;
			}

			for (int i = 0, slotsSize = slots.size(); i < slotsSize; i++) {
				GT_GuiFakeItemButton slot = slots.get(i);
				if (slot.getBounds().contains(mx, my)) {
					setSelected(slotIndexToListIndex(i));
					return;
				}
			}
		} else if (button == 1 && getSelected() >= 0) {
			if (btnCurrent.getBounds().contains(mx, my)) {
				setSelected(UNSELECTED);
				return;
			}
			GT_GuiFakeItemButton slot = getSlot(listIndexToSlotIndex(getSelected()));
			if (slot != null && slot.getBounds().contains(mx, my)) {
				setSelected(UNSELECTED);
			}
		}
		super.mouseClicked(x, y, button);
	}

	@Override
	public void onMouseWheel(int x, int y, int delta) {
		if (delta < 0)
			setScroll(scroll + 1);
		else if (delta > 0)
			setScroll(scroll - 1);
	}

	private void fillSlots() {
		for (int i = 0, j = scroll * cols; i < slots.size(); i++, j++) {
			slots.get(i)
					.setItem(getCandidate(j))
					.setBgIcon(j == getSelected() ? GT_GuiIcon.SLOT_DARKGRAY : GT_GuiIcon.SLOT_GRAY);
		}
	}

	private void ensureSelectedDisplayed() {
		if (getSelected() < scroll * cols) {
			setScroll(getSelected() / cols);
		} else if (getSelected() > (scroll + rows) * cols) {
			setScroll((getSelected() - (rows - 1) * cols) / cols);
		} else {
			// called nonetheless to update button enabled states
			setScroll(scroll);
		}
	}

	private int slotIndexToListIndex(int index) {
		int mapped = scroll * cols + index;
		return mapped >= stacks.size() ? UNSELECTED : mapped;
	}

	private int listIndexToSlotIndex(int index) {
		return index - scroll * cols;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		if (selected == this.selected) return;
		int newSelected = GT_Utility.clamp(selected, UNSELECTED, stacks.size() - 1);

		if (noDeselect && newSelected == UNSELECTED)
			return;

		GT_GuiFakeItemButton selectedSlot = getSlot(this.selected);
		if (selectedSlot != null)
			selectedSlot.setBgIcon(GT_GuiIcon.SLOT_GRAY);

		this.selected = newSelected;

		btnCurrent.setItem(getCandidate(this.selected));

		selectedSlot = getSlot(this.selected);
		if (selectedSlot != null)
			selectedSlot.setBgIcon(GT_GuiIcon.SLOT_DARKGRAY);
	}

	private void setScroll(int scroll) {
		if (stacks.size() > rows * cols) {
			int lo = 0;
			int hi = (stacks.size() - rows * cols) / cols + 1;
			this.scroll = GT_Utility.clamp(scroll, lo, hi);
			btnUp.enabled = this.scroll != lo;
			btnDown.enabled = this.scroll != hi;
		}
		fillSlots();
	}

	private ItemStack getCandidate(int listIndex) {
		return listIndex < 0 || listIndex >= stacks.size() ? null : stacks.get(listIndex);
	}

	private GT_GuiFakeItemButton getSlot(int slotIndex) {
		return slotIndex < 0 || slotIndex >= slots.size() ? null : slots.get(slotIndex);
	}
}
