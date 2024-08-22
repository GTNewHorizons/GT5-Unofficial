package goodgenerator.blocks.tileEntity.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;

import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.util.MultiblockTooltipBuilder;

public abstract class GT_MetaTileEntity_TooltipMultiBlockBase_EM extends GT_MetaTileEntity_MultiblockBase_EM
    implements ISecondaryDescribable {

    private static final Map<Integer, MultiblockTooltipBuilder> tooltips = new ConcurrentHashMap<>();

    protected GT_MetaTileEntity_TooltipMultiBlockBase_EM(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_TooltipMultiBlockBase_EM(String aName) {
        super(aName);
    }

    protected MultiblockTooltipBuilder getTooltip() {
        int tId = getBaseMetaTileEntity().getMetaTileID();
        MultiblockTooltipBuilder tooltip = tooltips.get(tId);
        if (tooltip == null) {
            tooltip = createTooltip();
            tooltips.put(tId, tooltip);
        }
        return tooltip;
    }

    protected abstract MultiblockTooltipBuilder createTooltip();

    @Override
    public String[] getDescription() {
        return getCurrentDescription();
    }

    @Override
    public boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    public String[] getPrimaryDescription() {
        return getTooltip().getInformation();
    }

    public String[] getSecondaryDescription() {
        return getTooltip().getStructureInformation();
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return getPollutionPerSecond(itemStack) / 20;
    }
}
