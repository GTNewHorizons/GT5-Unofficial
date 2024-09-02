package goodgenerator.blocks.tileEntity.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public abstract class MTETooltipMultiBlockBaseEM extends TTMultiblockBase implements ISecondaryDescribable {

    private static final Map<Integer, MultiblockTooltipBuilder> tooltips = new ConcurrentHashMap<>();

    protected MTETooltipMultiBlockBaseEM(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTETooltipMultiBlockBaseEM(String aName) {
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
