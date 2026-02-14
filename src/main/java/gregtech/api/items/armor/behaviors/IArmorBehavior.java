package gregtech.api.items.armor.behaviors;

import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.GTMod;
import gregtech.api.hazards.Hazard;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.util.GTUtility;

public interface IArmorBehavior {
    /*
     * Armor behavior system adapted from GTCEU
     */

    /**
     * Gets this behavior's name. This is used to check for behavior presence and equality.
     */
    BehaviorName getName();

    /**
     * Used in renderItem to get the texture that will be drawn onto modular armor. Ignore if this behavior is not
     * intended to dynamically change a texture.
     */
    default IIcon getModularArmorTexture() {
        return null;
    }

    /**
     * Called every tick that this behavior's armor item is equipped.
     */
    default void onArmorTick(@NotNull ArmorContext context) {}

    /**
     * Called when this behavior's armor item is unequipped.
     */
    default void onArmorUnequip(@NotNull ArmorContext context) {}

    /**
     * Called when this behavior's armor item is equipped, as well as when the player logs into the world or is cloned.
     */
    default void onArmorEquip(@NotNull ArmorContext context) {}

    /**
     * Called when this behavior is activated from any source.
     */
    default void onBehaviorActivated(@NotNull ArmorContext context) {
        if (hasDisplayName()) {
            GTUtility.sendChatToPlayer(
                context.getPlayer(),
                GTUtility
                    .processFormatStacks(GRAY + GTUtility.translate("GT5U.armor.message.enabled", getDisplayName())));
        }
    }

    /**
     * Called when this behavior is deactivated from any source.
     */
    default void onBehaviorDeactivated(@NotNull ArmorContext context) {
        if (hasDisplayName()) {
            GTUtility.sendChatToPlayer(
                context.getPlayer(),
                GTUtility
                    .processFormatStacks(GRAY + GTUtility.translate("GT5U.armor.message.disabled", getDisplayName())));
        }
    }

    /**
     * Modifies the ArmorState and loads any values from the stack, if necessary. Called whenever an ArmorState is
     * loaded.
     */
    default void configureArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {

    }

    /**
     * Saves this behavior's state (in the ArmorState) into the NBT tag, if needed.
     */
    default void saveArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {

    }

    /**
     * Checks if this behavior provides protection against a hazard.
     */
    default boolean protectsAgainst(@NotNull ArmorContext context, Hazard hazard) {
        return false;
    }

    /**
     * Checks if this behavior provides protection against a hazard. Protects the player regardless of whether the other
     * pieces provide protection.
     */
    default boolean protectsAgainstFully(@NotNull ArmorContext context, Hazard hazard) {
        return false;
    }

    /**
     * Merges two behaviors with the same name ({@link #getName()}). The resulting behavior must be the 'summation' of
     * this behavior and the given behavior. If this behavior cannot be stacked, this may remain unimplemented. If this
     * behavior may be stacked, this must return a valid behavior.
     */
    @NotNull
    default IArmorBehavior merge(@NotNull IArmorBehavior other) {
        GTMod.GT_FML_LOGGER.warn("Tried to merge armor behavior that does not support stacking! {} -> {}", other, this);
        return this;
    }

    /**
     * Adds to the augment/core/frame item tooltip.
     */
    default void addPartInformation(List<String> desc, ItemStack augmentStack, EntityPlayer player) {

    }

    /**
     * Add to the armor item tooltip.
     */
    default void addArmorInformation(@NotNull ArmorContext context, @NotNull List<String> tooltip) {

    }

    /**
     * Sets the list of keybinds which will call onKeyPressed for this behavior
     */
    default Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return Collections.emptySet();
    }

    /**
     * Called whenever one of this behavior's listened keys is pressed (on equipped armor)
     */
    default void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {}

    /**
     * Return the localized name of this behavior for use in tooltips and chat messages
     */
    default String getDisplayName() {
        return getName().getDisplayName();
    }

    /**
     * Returns true when this behavior has a name and should be displayed in the various behavior tooltip lists
     */
    default boolean hasDisplayName() {
        return getName().hasDisplayName();
    }
}
