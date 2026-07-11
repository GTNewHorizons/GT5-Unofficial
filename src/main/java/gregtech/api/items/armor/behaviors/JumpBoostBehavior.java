package gregtech.api.items.armor.behaviors;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.GTNHLib;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import bartworks.util.MathUtils;
import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorState;
import gregtech.api.util.GTUtility;

public class JumpBoostBehavior implements IArmorBehavior {

    public static final JumpBoostBehavior MECH_ARMOR_INSTANCE = new JumpBoostBehavior(2.0F);

    private final float jumpMaxMulti;

    private final float JUMP_INCREMENT = 0.25f;

    public JumpBoostBehavior(float boost) {
        this.jumpMaxMulti = boost;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        ArmorState state = context.getArmorState();

        if (keyPressed == ArmorActionManager.getAction("jump_increase")
            .getKeybind()) {
            state.jumpBoostMulti += JUMP_INCREMENT;
        } else if (keyPressed == ArmorActionManager.getAction("jump_decrease")
            .getKeybind()) {
                state.jumpBoostMulti -= JUMP_INCREMENT;
            }

        state.jumpBoostMulti = MathUtils.clamp(state.jumpBoostMulti, 1, jumpMaxMulti);

        String text = GTUtility.translate("GT5U.armor.message.jump_boost_set", state.jumpBoostMulti);
        GTNHLib.proxy.printMessageAboveHotbar(text, 60, true, true);
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.JumpBoost;
    }

    @Override
    public void configureArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        float savedBoost = stackTag.getFloat("jumpBoostMulti");
        context.getArmorState().jumpBoostMulti = Math.max(savedBoost, 1.0F);
    }

    @Override
    public void saveArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        stackTag.setFloat("jumpBoostMulti", context.getArmorState().jumpBoostMulti);
    }

    @Override
    public @NotNull IArmorBehavior merge(@NotNull IArmorBehavior other) {
        if (!(other instanceof JumpBoostBehavior jumpBoost)) return this;

        return new JumpBoostBehavior(this.jumpMaxMulti + jumpBoost.jumpMaxMulti - 1.0f);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }
}
