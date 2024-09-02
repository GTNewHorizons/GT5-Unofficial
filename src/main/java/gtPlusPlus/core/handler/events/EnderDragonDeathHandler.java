package gtPlusPlus.core.handler.events;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class EnderDragonDeathHandler {

    private static final String mDragonClassName = "chylex.hee.entity.boss.EntityBossDragon";
    private static final boolean mHEE;
    private static final Class mHardcoreDragonClass;

    private static final String mChaosDragonClassName = "com.brandon3055.draconicevolution.common.entity.EntityCustomDragon";
    private static final boolean mDE;
    private static final Class mChaoseDragonClass;

    static {
        mHEE = ReflectionUtils.doesClassExist(mDragonClassName);
        mHardcoreDragonClass = (mHEE ? ReflectionUtils.getClass(mDragonClassName) : null);
        mDE = ReflectionUtils.doesClassExist(mChaosDragonClassName);
        mChaoseDragonClass = (mDE ? ReflectionUtils.getClass(mChaosDragonClassName) : null);
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {

        int aCountTotal = 0;

        if (mHEE && mHardcoreDragonClass != null && mHardcoreDragonClass.isInstance(event.entityLiving)) {
            for (int y = 0; y < MathUtils.randInt(100, 250); y++) {
                int aAmount = MathUtils.randInt(5, 25);
                event.entityLiving.entityDropItem(
                    MaterialsElements.STANDALONE.DRAGON_METAL.getNugget(aAmount),
                    MathUtils.randFloat(0, 1));
                aCountTotal = +aAmount;
            }
        } else if (mDE && mChaoseDragonClass != null && mChaoseDragonClass.isInstance(event.entityLiving)) {
            for (int y = 0; y < MathUtils.randInt(100, 200); y++) {
                int aAmount = MathUtils.randInt(1, 5);
                event.entityLiving.entityDropItem(
                    MaterialsElements.STANDALONE.DRAGON_METAL.getIngot(aAmount),
                    MathUtils.randFloat(0, 1));
                aCountTotal = +aAmount;
            }
        } else if (event.entityLiving instanceof EntityDragon) {
            for (int y = 0; y < MathUtils.randInt(25, 50); y++) {
                int aAmount = MathUtils.randInt(1, 10);
                event.entityLiving.entityDropItem(
                    MaterialsElements.STANDALONE.DRAGON_METAL.getNugget(aAmount),
                    MathUtils.randFloat(0, 1));
                aCountTotal = +aAmount;
            }
        }
        if (aCountTotal > 0) {
            PlayerUtils
                .messageAllPlayers(aCountTotal + " Shards of Dragons Blood have crystalized into a metallic form.");
        }
    }
}
