package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import forestry.api.lepidopterology.EnumFlutterType;
import forestry.api.lepidopterology.IButterfly;
import forestry.api.lepidopterology.IEntityButterfly;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_LanguageManager;

public class Behaviour_Scoop extends Behaviour_None {

    private final int mCosts;
    private final String mTooltip = GT_LanguageManager.addStringLocalization(
            "gt.behaviour.scoop",
            "Catches Butterflies on Leftclick");

    public Behaviour_Scoop(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onLeftClickEntity(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        if ((aEntity instanceof IEntityButterfly)) {
            if (aPlayer.worldObj.isRemote) {
                return true;
            }
            if ((aPlayer.capabilities.isCreativeMode)
                    || (((GT_MetaGenerated_Tool) aItem).doDamage(aStack, this.mCosts))) {
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
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
