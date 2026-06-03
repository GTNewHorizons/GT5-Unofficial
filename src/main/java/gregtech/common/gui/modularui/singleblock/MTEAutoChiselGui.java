package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.widget.Widget;

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
        return super.createSpecialSlot().tooltip(
            t -> t.clearText()
                .addStringLines(machine.mTooltipCache.getData("GTPP.machines.chisel_slot.tooltip").text));
    }
}
