package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.NIGHT_VISION_KEYBIND;
import static gregtech.loaders.ExtraIcons.nightVisionAugment;

import java.util.Collections;
import java.util.Set;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorContext;

public class NightVisionBehavior implements IArmorBehavior {

    public static final NightVisionBehavior INSTANCE = new NightVisionBehavior();

    @Override
    public IIcon getModularArmorTexture() {
        return nightVisionAugment;
    }

    protected NightVisionBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.NightVision;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.getArmorState()
            .toggle(context, BehaviorName.NightVision);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return Collections.singleton(NIGHT_VISION_KEYBIND);
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        if (context.isBehaviorActive(BehaviorName.NightVision) && context.drainEnergy(2)) {
            context.getPlayer()
                .removePotionEffect(Potion.blindness.id);
            context.getPlayer()
                .addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, true));
        } else {
            context.getPlayer()
                .removePotionEffect(Potion.nightVision.id);
        }
    }

    @Override
    public void onArmorUnequip(@NotNull ArmorContext context) {
        context.getPlayer()
            .removePotionEffect(Potion.nightVision.id);
    }
}
