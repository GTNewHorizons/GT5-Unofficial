package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import forestry.api.lepidopterology.EnumFlutterType;
import forestry.api.lepidopterology.IButterfly;
import forestry.api.lepidopterology.IEntityButterfly;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTLanguageManager;

public class BehaviourScoop extends BehaviourNone {

    private final int mCosts;
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.scoop", "Catches Butterflies on Leftclick");

    public BehaviourScoop(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onLeftClickEntity(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        if ((aEntity instanceof IEntityButterfly)) {
            if (aPlayer.worldObj.isRemote) {
                return true;
            }
            if ((aPlayer.capabilities.isCreativeMode) || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                IButterfly tButterfly = ((IEntityButterfly) aEntity).getButterfly();
                tButterfly.getGenome()
                    .getPrimary()
                    .getRoot()
                    .getBreedingTracker(aEntity.worldObj, aPlayer.getGameProfile())
                    .registerCatch(tButterfly);
                aPlayer.worldObj.spawnEntityInWorld(
                    new EntityItem(
                        aPlayer.worldObj,
                        aEntity.posX,
                        aEntity.posY,
                        aEntity.posZ,
                        tButterfly.getGenome()
                            .getPrimary()
                            .getRoot()
                            .getMemberStack(tButterfly.copy(), EnumFlutterType.BUTTERFLY.ordinal())));
                aEntity.setDead();
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
