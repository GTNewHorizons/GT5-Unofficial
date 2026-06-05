package gregtech.common.gui.modularui.singleblock;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.recipe.BasicUIProperties;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEAutoChisel;

public class MTEAutoChiselGui extends MTEBasicMachineBaseGui<MTEAutoChisel> {

    public MTEAutoChiselGui(MTEAutoChisel machine, BasicUIProperties properties) {
        super(machine, properties);
        useGregTechLogo(true);
    }

    @Override
    protected Widget<? extends Widget<?>> createSpecialSlot() {
        return new PhantomItemSlot().marginRight(9)
            .slot(new ModularSlot(machine.inventoryHandler, machine.getSpecialSlotIndex()).singletonSlotGroup(1000))
            .backgroundOverlay(
                properties.useSpecialSlot ? slotOverlayFunction.apply(0, false, false, true) : IDrawable.NONE)
            .addTooltipStringLines(machine.mTooltipCache.getData("GTPP.machines.chisel_slot.tooltip").text)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
