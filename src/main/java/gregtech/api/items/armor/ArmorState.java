package gregtech.api.items.armor;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTUtility.addSeparatorIfNeeded;
import static gregtech.common.items.armor.MechArmorBase.MECH_CORE_KEY;
import static gregtech.common.items.armor.MechArmorBase.MECH_FRAME_KEY;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.GREEN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

import org.jetbrains.annotations.NotNull;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.items.armor.ArmorContext.ArmorContextImpl;
import gregtech.api.items.armor.AugmentBuilder.AugmentCategory;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Augments;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Cores;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

/// An armor state contains all relevant state values for an individual piece of armor.
public class ArmorState {

    public Cores core;
    public Frames frame;
    /// {category,column: augment}
    public Map<ObjectIntPair<AugmentCategory>, Augments> augments = new HashMap<>();

    public Map<BehaviorName, IArmorBehavior> behaviors = new Object2ObjectOpenHashMap<>();

    public Set<BehaviorName> activeBehaviors = new ObjectLinkedOpenHashSet<>();

    public double charge;

    public int visDiscount;
    public float speedBoost, jumpBoost;

    public void addArmorInformation(ArmorContext context, List<String> tooltip) {
        boolean showAllInfo = ArmorHelper.isShiftPressed();

        if (!showAllInfo) {
            tooltip.add(GRAY + GTUtility.translate("GT5U.armor.tooltip.hold_shift"));
        }

        if (showAllInfo) {
            if (frame != null) {
                tooltip.add(GRAY + "Armor Frame: " + frame.getLocalizedName());
                tooltip.add("");
            }

            if (core != null) {
                tooltip.add(GRAY + "Energy Core: " + core.getLocalizedName());
                tooltip.add("");
            }
        }

        if (showAllInfo) {
            boolean printedHeader = false;

            for (IArmorBehavior behavior : sortBehaviors(behaviors.values())) {
                if (!behavior.hasDisplayName()) continue;

                if (!printedHeader) {
                    printedHeader = true;
                    tooltip.add(GRAY + "Installed Augments:");
                }

                tooltip.add(GRAY + "- " + behavior.getDisplayName());
            }

            addSeparatorIfNeeded(tooltip);

            printedHeader = false;

            for (BehaviorName behavior : sortBehaviorNames(activeBehaviors)) {
                if (!behavior.hasDisplayName()) continue;

                if (!printedHeader) {
                    printedHeader = true;
                    tooltip.add(GREEN + "Active Augments:");
                }

                tooltip.add("- " + behavior.getDisplayName());
            }

            addSeparatorIfNeeded(tooltip);
        }

        for (IArmorBehavior behavior : sortBehaviors(behaviors.values())) {
            behavior.addArmorInformation(context, tooltip);
        }

        if (showAllInfo) addSeparatorIfNeeded(tooltip);

        if (core != null) {
            boolean infinite = this.hasBehavior(BehaviorName.InfiniteEnergy);

            String stored = infinite ? "∞" : formatNumber(Math.round(this.charge));
            String capacity = infinite ? "∞" : formatNumber(core.getChargeMax());
            String voltage = formatNumber(GTValues.V[core.getChargeTier()]);

            tooltip.add(AQUA + GTUtility.translate("item.itemBaseEuItem.tooltip.3", stored, capacity, voltage));
        }

        if (showAllInfo) addSeparatorIfNeeded(tooltip);
    }

    private List<BehaviorName> sortBehaviorNames(Collection<BehaviorName> behaviors) {
        List<BehaviorName> sorted = new ArrayList<>(behaviors);

        sorted.sort(
            Comparator.<BehaviorName>comparingInt(
                behavior -> -behavior.getRarity()
                    .ordinal())
                .thenComparing(BehaviorName::getDisplayName));

        return sorted;
    }

    private List<IArmorBehavior> sortBehaviors(Collection<IArmorBehavior> behaviors) {
        List<IArmorBehavior> sorted = new ArrayList<>(behaviors);

        sorted.sort(
            Comparator.<IArmorBehavior>comparingInt(
                behavior -> -behavior.getName()
                    .getRarity()
                    .ordinal())
                .thenComparing(IArmorBehavior::getDisplayName));

        return sorted;
    }

    public boolean isActive(BehaviorName behavior) {
        return activeBehaviors.contains(behavior);
    }

    public boolean hasBehavior(BehaviorName behavior) {
        return behaviors.containsKey(behavior);
    }

    public void activate(ArmorContext context, BehaviorName name) {
        IArmorBehavior behavior = behaviors.get(name);

        if (behavior == null) return;

        if (activeBehaviors.add(name)) {
            behavior.onBehaviorActivated(context);
        }
    }

    public void deactivate(ArmorContext context, BehaviorName name) {
        if (activeBehaviors.remove(name)) {
            IArmorBehavior behavior = behaviors.get(name);

            if (behavior != null) behavior.onBehaviorDeactivated(context);
        }
    }

    public boolean toggle(ArmorContext context, BehaviorName name) {
        if (isActive(name)) {
            deactivate(context, name);
            return false;
        } else {
            activate(context, name);
            return true;
        }
    }

    public boolean drainEnergy(double amount) {
        if (hasBehavior(BehaviorName.InfiniteEnergy)) return true;

        if (charge >= amount) {
            charge -= amount;
            return true;
        } else {
            return false;
        }
    }

    private void addBehavior(IArmorBehavior behavior) {
        IArmorBehavior existing = behaviors.get(behavior.getName());

        if (existing != null) {
            behavior = existing.merge(behavior);
        }

        behaviors.put(behavior.getName(), behavior);
    }

    public static ArmorState load(ItemStack stack) {
        ArmorContextImpl context = new ArmorContextImpl(stack, null);

        load(context);

        return context.getArmorState();
    }

    @NotNull
    public static ArmorState load(ArmorContext context) {
        ArmorState state = new ArmorState();

        NBTTagCompound tag = context.getArmorStack()
            .getTagCompound();

        if (tag == null) return state;

        context.setArmorState(state);

        state.frame = MechArmorAugmentRegistries.framesMap.get(tag.getString(MECH_FRAME_KEY));
        state.core = MechArmorAugmentRegistries.coresMap.get(tag.getString(MECH_CORE_KEY));
        state.charge = tag.getDouble("charge");

        NBTTagCompound augmentTag = tag.getCompoundTag("augments");

        if (augmentTag != null) {
            for (AugmentCategory category : AugmentCategory.values()) {
                NBTTagCompound categoryTag = augmentTag.getCompoundTag(Integer.toString(category.ordinal()));

                if (categoryTag == null) continue;

                // noinspection unchecked
                for (var e : ((Map<String, NBTTagString>) categoryTag.tagMap).entrySet()) {
                    int slot;

                    try {
                        slot = Integer.parseInt(e.getKey());
                    } catch (NumberFormatException ex) {
                        continue;
                    }

                    Augments augment = MechArmorAugmentRegistries.augmentsMap.get(
                        e.getValue()
                            .func_150285_a_());

                    if (augment == null) continue;

                    state.augments.put(ObjectIntPair.of(category, slot), augment);
                }
            }
        }

        Consumer<IArmorBehavior> addBehavior = state::addBehavior;

        if (state.frame != null) {
            state.frame.getProvidedBehaviors()
                .forEach(addBehavior);
        }

        if (state.core != null) {
            state.core.getProvidedBehaviors()
                .forEach(addBehavior);
        }

        for (Augments augment : state.augments.values()) {
            augment.getProvidedBehaviors()
                .forEach(addBehavior);
        }

        // noinspection unchecked
        for (NBTTagString str : (List<NBTTagString>) tag.getTagList("active", NBT.TAG_STRING).tagList) {
            try {
                state.activeBehaviors.add(BehaviorName.valueOf(str.func_150285_a_()));
            } catch (IllegalArgumentException e) {
                GTMod.GT_FML_LOGGER.error("Could not load active behavior: {}", str.func_150285_a_(), e);
            }
        }

        for (IArmorBehavior behavior : state.behaviors.values()) {
            behavior.configureArmorState(context, tag);
        }

        return state;
    }

    private static final String[] KEPT_TAGS = { "display", "ench" };

    public static void save(ArmorContext context) {
        NBTTagCompound oldTag = context.getArmorStack()
            .getTagCompound();

        NBTTagCompound tag = new NBTTagCompound();
        context.getArmorStack()
            .setTagCompound(tag);

        if (oldTag != null) {
            for (String name : KEPT_TAGS) {
                NBTBase inner = oldTag.getTag(name);

                if (inner != null) tag.setTag(name, inner.copy());
            }
        }

        ArmorState state = context.getArmorState();

        tag.setString(MECH_FRAME_KEY, state.frame == null ? "None" : state.frame.getId());
        tag.setString(MECH_CORE_KEY, state.core == null ? "None" : state.core.getId());
        tag.setDouble("charge", state.charge);

        NBTTagCompound augmentTag = new NBTTagCompound();
        tag.setTag("augments", augmentTag);

        for (AugmentCategory category : AugmentCategory.values()) {
            augmentTag.setTag(Integer.toString(category.ordinal()), new NBTTagCompound());
        }

        for (var e : state.augments.entrySet()) {
            int row = e.getKey()
                .left()
                .ordinal();
            int col = e.getKey()
                .rightInt();

            augmentTag.getCompoundTag(Integer.toString(row))
                .setTag(
                    Integer.toString(col),
                    new NBTTagString(
                        e.getValue()
                            .getId()));
        }

        NBTTagList active = new NBTTagList();
        tag.setTag("active", active);

        for (BehaviorName name : state.activeBehaviors) {
            active.appendTag(new NBTTagString(name.name()));
        }

        for (IArmorBehavior behavior : state.behaviors.values()) {
            behavior.saveArmorState(context, tag);
        }
    }
}
