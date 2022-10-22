package gregtech.common.gui;

import gregtech.api.gui.GT_Container_SpecialFilter;
import gregtech.api.gui.GT_GUIContainer_SpecialFilter;
import gregtech.api.gui.widgets.GT_GuiSlotTooltip;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_RecipeFilter extends GT_GUIContainer_SpecialFilter {

	private static final String REPRESENTATION_SLOT_TOOLTIP = "GT5U.recipe_filter.representation_slot.tooltip";

	public GT_GUIContainer_RecipeFilter(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	@Override
	protected void setupTooltips() {
		addToolTip(new GT_GuiSlotTooltip(((GT_Container_SpecialFilter) mContainer).getSpecialSlot(), mTooltipCache.getData(REPRESENTATION_SLOT_TOOLTIP)));
	}
}
