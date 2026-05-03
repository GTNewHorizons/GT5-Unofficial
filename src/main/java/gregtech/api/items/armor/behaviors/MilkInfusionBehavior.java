package gregtech.api.items.armor.behaviors;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import gregtech.api.items.armor.ArmorContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

import static gregtech.api.items.armor.ArmorKeybinds.OMNI_MOVEMENT_KEYBIND;

public class MilkInfusionBehavior implements IArmorBehavior {

    public static MilkInfusionBehavior INSTANCE = new MilkInfusionBehavior();

    @Override
    public BehaviorName getName() { return BehaviorName.MilkInfusion; }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        EntityPlayer player = context.getPlayer();

        for (Object obj: player.getActivePotionEffects()){
            if (obj instanceof PotionEffect effect) {
                int effectID = effect.getPotionID();

                if (effectID >= 0 && effectID < Potion.potionTypes.length) {
                    Potion potion = Potion.potionTypes[effectID];

                    if (potion != null && potion.isBadEffect()) {
                        if (context.drainEnergy(5000)) {
                            player.removePotionEffect(effectID);

                            player.worldObj.playSoundAtEntity(player, "random.drink", 1.0F, 1.1F);
                        }
                    }
                }
            }
        }
    }
}
