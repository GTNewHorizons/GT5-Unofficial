package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTModHandler;
import gregtech.common.items.tools.ScrewdriverActions;

/**
 * Lets a {@code MetaGeneratedTool} adjust repeaters and comparators.
 * <p/>
 * The screwdriver itself is a standalone item now and does not go through the behaviour registry, but the soldering
 * iron still does and still acts as one, so this remains. The logic it used to hold lives in
 * {@link ScrewdriverActions}, shared by both.
 */
public class BehaviourScrewdriver extends BehaviourNone {

    private final int mVanillaCosts;
    private final int mEUCosts;

    public BehaviourScrewdriver(int aVanillaCosts, int aEUCosts) {
        this.mVanillaCosts = aVanillaCosts;
        this.mEUCosts = aEUCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        return ScrewdriverActions.use(
            aWorld,
            aX,
            aY,
            aZ,
            hitX,
            hitY,
            hitZ,
            () -> GTModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer));
    }
}
