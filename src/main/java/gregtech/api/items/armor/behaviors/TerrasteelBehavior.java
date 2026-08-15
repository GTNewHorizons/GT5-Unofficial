package gregtech.api.items.armor.behaviors;

import static gregtech.api.enums.Mods.Botania;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;

public class TerrasteelBehavior implements IArmorBehavior {

    public static final TerrasteelBehavior INSTANCE = new TerrasteelBehavior();

    protected TerrasteelBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.Terrasteel;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;
        EntityPlayer player = context.getPlayer();
        int foodLevel = player.getFoodStats()
            .getFoodLevel();
        if (foodLevel > 0 && foodLevel < 18 && player.shouldHeal() && player.ticksExisted % 80 == 0) {
            player.heal(1F);
        }

        if (Botania.isModLoaded()) {
            BotaniaCompat.dispatchMana(context.getArmorStack(), player);
        }
    }

    @Override
    public void configureArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        context.getArmorState().manaDiscount += 0.2F;
    }
}

class BotaniaCompat {

    public static void dispatchMana(ItemStack stack, EntityPlayer player) {
        vazkii.botania.api.mana.ManaItemHandler.dispatchManaExact(stack, player, 1, true);
    }
}
