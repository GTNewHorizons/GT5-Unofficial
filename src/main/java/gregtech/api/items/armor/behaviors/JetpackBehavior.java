package gregtech.api.items.armor.behaviors;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;
import static gregtech.loaders.ExtraIcons.jetpackAugment;

import java.util.Set;

import gregtech.api.util.GTUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorHelper;
import gregtech.api.items.armor.ArmorKeybinds;
import gregtech.api.items.armor.JetpackStats;

public class JetpackBehavior implements IArmorBehavior {

    public static final JetpackBehavior INSTANCE = new JetpackBehavior(JetpackStats.ADVANCED);
    private final JetpackStats jetpackStats;

    protected JetpackBehavior(JetpackStats stats) {
        jetpackStats = stats;
    }

    @Override
    public IIcon getModularArmorTexture() {
        return jetpackAugment;
    }

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (!tag.hasKey(ArmorHelper.JETPACK_KEY)) return;
        boolean hover = tag.getBoolean(ArmorHelper.JETPACK_HOVER_KEY);
        if (tag.getBoolean(ArmorHelper.JETPACK_KEY)) performFlying(player, stack, hover);
    }

    @Override
    public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (!tag.hasKey(ArmorHelper.JETPACK_KEY)) return;

        if (keyPressed == ArmorKeybinds.JETPACK_HOVER_KEYBIND) {
            boolean wasHover = tag.getBoolean(ArmorHelper.JETPACK_HOVER_KEY);
            tag.setBoolean(ArmorHelper.JETPACK_HOVER_KEY, !wasHover);

            if (wasHover) {
                GTUtility.sendChatToPlayer(
                    player,
                    StatCollector.translateToLocalFormatted(
                        "GT5U.armor.message.disabled",
                        StatCollector.translateToLocal("GT5U.armor.behavior.jetpackhover")));
            } else {
                GTUtility.sendChatToPlayer(
                    player,
                    StatCollector.translateToLocalFormatted(
                        "GT5U.armor.message.enabled",
                        StatCollector.translateToLocal("GT5U.armor.behavior.jetpackhover")));
            }
        } else if (keyPressed == ArmorKeybinds.JETPACK_KEYBIND) {
            boolean wasActive = tag.getBoolean(ArmorHelper.JETPACK_KEY);
            tag.setBoolean(ArmorHelper.JETPACK_KEY, !wasActive);

            if (wasActive) {
                GTUtility.sendChatToPlayer(
                    player,
                    StatCollector.translateToLocalFormatted("GT5U.armor.message.disabled", getBehaviorName()));
            } else {
                GTUtility.sendChatToPlayer(
                    player,
                    StatCollector.translateToLocalFormatted("GT5U.armor.message.enabled", getBehaviorName()));
            }
        }
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys() {
        return ImmutableSet.of(ArmorKeybinds.JETPACK_HOVER_KEYBIND, ArmorKeybinds.JETPACK_KEYBIND);
    }

    @Override
    public void addBehaviorNBT(@NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.JETPACK_KEY, false);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.JETPACK_KEY;
    }

    /*
     * Called every tick, performs flying if the correct keys are pressed.
     * Logic from SimplyJetpacks2:
     * https://github.com/Tomson124/SimplyJetpacks2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/ItemJetpack.java
     */
    private void performFlying(@NotNull EntityPlayer player, @NotNull ItemStack stack, boolean hover) {
        double currentAccel = jetpackStats.getVerticalAcceleration() * (player.motionY < 0.3D ? 2.5D : 1.0D);
        double currentSpeedVertical = jetpackStats.getVerticalSpeed() * (player.isInWater() ? 0.4D : 1.0D);
        boolean flyKeyDown = ArmorKeybinds.VANILLA_JUMP.isKeyDown(player);
        boolean descendKeyDown = ArmorKeybinds.VANILLA_SNEAK.isKeyDown(player);

        if (flyKeyDown || hover && !player.onGround) {
            if (!player.isInWater() && ArmorHelper.drainArmor(stack, 20)) {
                if (flyKeyDown) {
                    if (!hover) {
                        player.motionY = Math.min(player.motionY + currentAccel, currentSpeedVertical);
                    } else {
                        if (descendKeyDown) player.motionY = Math
                            .min(player.motionY + currentAccel, jetpackStats.getVerticalHoverSlowSpeed());
                        else player.motionY = Math
                            .min(player.motionY + currentAccel, jetpackStats.getVerticalHoverSpeed());
                    }
                } else if (descendKeyDown) {
                    player.motionY = Math.min(player.motionY + currentAccel, -jetpackStats.getVerticalHoverSpeed());
                } else {
                    player.motionY = Math.min(
                        player.motionY + currentAccel,
                        stack.getTagCompound()
                            .hasKey(ArmorHelper.JETPACK_PERFECT_HOVER_KEY) ? 0
                                : -jetpackStats.getVerticalHoverSlowSpeed());
                }
                float speedSideways = (float) (player.isSneaking() ? jetpackStats.getSidewaysSpeed() * 0.5f
                    : jetpackStats.getSidewaysSpeed());
                float speedForward = (float) (player.isSprinting()
                    ? speedSideways * jetpackStats.getSprintSpeedModifier()
                    : speedSideways);

                if (ArmorKeybinds.VANILLA_FORWARD.isKeyDown(player)) player.moveFlying(0, speedForward, speedForward);
                if (ArmorKeybinds.VANILLA_BACK.isKeyDown(player))
                    player.moveFlying(0, -speedSideways, speedSideways * 0.8f);
                if (ArmorKeybinds.VANILLA_LEFT.isKeyDown(player)) player.moveFlying(speedSideways, 0, speedSideways);
                if (ArmorKeybinds.VANILLA_RIGHT.isKeyDown(player)) player.moveFlying(-speedSideways, 0, speedSideways);
                if (!player.getEntityWorld().isRemote) {
                    player.fallDistance = 0;
                }
                // spawnParticle(player.getEntityWorld(), player, jetpackStats.getParticle());
            }
        }
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.jetpack");
    }
}
