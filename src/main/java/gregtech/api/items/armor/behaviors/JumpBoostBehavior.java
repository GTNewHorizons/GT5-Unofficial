package gregtech.api.items.armor.behaviors;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;

public class JumpBoostBehavior implements IArmorBehavior {

    public static final JumpBoostBehavior MECH_ARMOR_INSTANCE = new JumpBoostBehavior(0.2F);

    private final float boost;

    protected JumpBoostBehavior(float boost) {
        this.boost = boost;
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.JumpBoost;
    }

    @Override
    public void configureArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        context.getArmorState().jumpBoost = boost;
    }

    @Override
    public @NotNull IArmorBehavior merge(@NotNull IArmorBehavior other) {
        if (!(other instanceof JumpBoostBehavior jumpBoost)) return null;

        return new JumpBoostBehavior(this.boost + jumpBoost.boost);
    }
}
