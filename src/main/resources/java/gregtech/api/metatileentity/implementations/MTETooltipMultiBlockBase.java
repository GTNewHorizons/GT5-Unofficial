package gregtech.api.metatileentity.implementations;

import java.util.concurrent.atomic.AtomicReferenceArray;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

/**
 * A multiblock with tooltip {@link MultiblockTooltipBuilder}
 */
public abstract class MTETooltipMultiBlockBase extends MTEMultiBlockBase implements ISecondaryDescribable {

    private static final AtomicReferenceArray<MultiblockTooltipBuilder> tooltips = new AtomicReferenceArray<>(
        GregTechAPI.METATILEENTITIES.length);

    static {
        if (GTUtility.isClient()) {
            addTooltipResetListener();
        }
    }

    public MTETooltipMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTETooltipMultiBlockBase(String aName) {
        super(aName);
    }

    @SideOnly(Side.CLIENT)
    private static void addTooltipResetListener() {
        SimpleReloadableResourceManager manager = (SimpleReloadableResourceManager) Minecraft.getMinecraft()
            .getResourceManager();

        manager.reloadListeners.add(resourceManager -> {
            final int len = tooltips.length();

            for (int i = 0; i < len; i++) {
                tooltips.set(i, null);
            }
        });
    }

    protected abstract MultiblockTooltipBuilder createTooltip();

    protected final MultiblockTooltipBuilder getTooltip() {
        int tId = getBaseMetaTileEntity().getMetaTileID();
        MultiblockTooltipBuilder tooltip = tooltips.get(tId);
        if (tooltip == null) {
            tooltip = createTooltip();
            tooltips.set(tId, tooltip);
        }
        return tooltip;
    }

    @Override
    public final String[] getDescription() {
        return getCurrentDescription();
    }

    @Override
    public final String[] getPrimaryDescription() {
        return getTooltip().getInformation();
    }

    @Override
    public final String[] getSecondaryDescription() {
        return getTooltip().getStructureInformation();
    }

    @Override
    public final boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }
}
