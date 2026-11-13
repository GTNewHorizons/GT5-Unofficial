package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BehaviourSpoiledFoodCan extends BehaviourFoodCanBase {

    public BehaviourSpoiledFoodCan() {
        super(2, 0.0f);
    }

    @Override
    protected String getEatSound() {
        return "random.burp";
    }

    @Override
    protected void onCansEaten(EntityPlayer player, int cansEaten) {
        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 100, 1));
        player.addPotionEffect(new PotionEffect(Potion.hunger.id, 2400, 2));
        player.addPotionEffect(new PotionEffect(Potion.poison.id, 40, 2));
    }
}
