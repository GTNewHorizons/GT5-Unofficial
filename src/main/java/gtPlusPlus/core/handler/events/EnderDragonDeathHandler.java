package gtPlusPlus.core.handler.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class EnderDragonDeathHandler {

	private static final String mDragonClassName = "chylex.hee.entity.boss.EntityBossDragon";
	private static final boolean mHEE;
	private static final Class mHardcoreDragonClass;
	
	static {
		mHEE = ReflectionUtils.doesClassExist(mDragonClassName);
		mHardcoreDragonClass = (mHEE ? ReflectionUtils.getClass(mDragonClassName) : null);
	}

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		
		boolean aDidDrop = false;
		int aCountTotal = 0;
		
		//HEE Dragon
		if (mHEE) {
			if (mHardcoreDragonClass != null) {
				if (mHardcoreDragonClass.isInstance(event.entityLiving)) {
						for (int y = 0; y < MathUtils.randInt(100, 250); y++) {
							int aAmount = MathUtils.randInt(5, 25);
							event.entityLiving.entityDropItem(ELEMENT.STANDALONE.DRAGON_METAL.getNugget(aAmount), MathUtils.randFloat(0, 1));
							aDidDrop = true;
							aCountTotal =+ aAmount;
						}					
				}
			}
		}
		//Vanilla Dragon or any other dragon that extends it
		else {
			if (event.entityLiving instanceof EntityDragon) {
				for (int y = 0; y < MathUtils.randInt(25, 50); y++) {
					int aAmount = MathUtils.randInt(1, 10);
					event.entityLiving.entityDropItem(ELEMENT.STANDALONE.DRAGON_METAL.getNugget(aAmount), MathUtils.randFloat(0, 1));
					aDidDrop = true;
					aCountTotal =+ aAmount;
				}
			}
		}
		
		if (aDidDrop) {
			PlayerUtils.messageAllPlayers(aCountTotal+" Shards of Dragons Blood have crystalized into a metallic form.");
		}
		
	}

}
