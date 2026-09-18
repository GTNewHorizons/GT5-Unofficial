package gregtech.common.items.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import forestry.api.lepidopterology.EnumFlutterType;
import forestry.api.lepidopterology.IButterfly;
import forestry.api.lepidopterology.IEntityButterfly;

/**
 * The "left click a butterfly to catch it" half of a scoop, moved out of {@code BehaviourScoop}.
 * <p/>
 * Every method here touches Forestry, so callers must check {@code Forestry.isModLoaded()} before naming this class
 * -- which is what keeps it from being loaded at all when Forestry is absent, exactly as the old behaviour did by
 * only ever instantiating itself in that case.
 */
public final class ScoopActions {

    private ScoopActions() {}

    /**
     * @return whether the click was consumed, which it is for any butterfly whether or not the scoop could pay.
     */
    public static boolean catchButterfly(ToolScoopItem item, ItemStack stack, EntityPlayer player, Entity entity) {
        if (!(entity instanceof IEntityButterfly)) return false;
        if (player.worldObj.isRemote) return true;
        if (player.capabilities.isCreativeMode || item.spendOneUse(stack)) {
            IButterfly butterfly = ((IEntityButterfly) entity).getButterfly();
            butterfly.getGenome()
                .getPrimary()
                .getRoot()
                .getBreedingTracker(entity.worldObj, player.getGameProfile())
                .registerCatch(butterfly);
            player.worldObj.spawnEntityInWorld(
                new EntityItem(
                    player.worldObj,
                    entity.posX,
                    entity.posY,
                    entity.posZ,
                    butterfly.getGenome()
                        .getPrimary()
                        .getRoot()
                        .getMemberStack(butterfly.copy(), EnumFlutterType.BUTTERFLY.ordinal())));
            entity.setDead();
        }
        return true;
    }
}
