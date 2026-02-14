package gregtech.api.items.armor;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.items.ItemAugment;
import gregtech.api.items.ItemAugmentCore;
import gregtech.api.items.ItemAugmentFrame;
import gregtech.api.items.armor.AugmentBuilder.AugmentCategory;
import gregtech.api.items.armor.behaviors.ApiaristBehavior;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.CreativeFlightBehavior;
import gregtech.api.items.armor.behaviors.FallProtectionBehavior;
import gregtech.api.items.armor.behaviors.FireImmunityBehavior;
import gregtech.api.items.armor.behaviors.ForceFieldBehavior;
import gregtech.api.items.armor.behaviors.GogglesOfRevealingBehavior;
import gregtech.api.items.armor.behaviors.HazmatBehavior;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.items.armor.behaviors.InertiaCancelingBehavior;
import gregtech.api.items.armor.behaviors.InfiniteEnergyBehavior;
import gregtech.api.items.armor.behaviors.JetpackBehavior;
import gregtech.api.items.armor.behaviors.JetpackHoverBehavior;
import gregtech.api.items.armor.behaviors.JetpackPerfectHoverBehavior;
import gregtech.api.items.armor.behaviors.JumpBoostBehavior;
import gregtech.api.items.armor.behaviors.KnockbackResistBehavior;
import gregtech.api.items.armor.behaviors.NightVisionBehavior;
import gregtech.api.items.armor.behaviors.OmniMovementBehavior;
import gregtech.api.items.armor.behaviors.SpaceSuitBehavior;
import gregtech.api.items.armor.behaviors.SpeedBoostBehavior;
import gregtech.api.items.armor.behaviors.StepAssistBehavior;
import gregtech.api.items.armor.behaviors.SwimSpeedBehavior;
import gregtech.api.items.armor.behaviors.VisDiscountBehavior;
import gregtech.api.items.armor.behaviors.WaterBreathingBehavior;

public class MechArmorAugmentRegistries {

    public static final HashMap<String, Frames> framesMap = new HashMap<>();
    public static final HashMap<String, Cores> coresMap = new HashMap<>();
    public static final HashMap<String, Augments> augmentsMap = new HashMap<>();

    public static void init() {
        for (Frames frame : Frames.values()) {
            frame.registerItem();
        }

        for (Cores core : Cores.values()) {
            core.registerItem();
        }

        for (Augments augment : Augments.values()) {
            augment.registerItem();
        }
    }

    public enum ArmorType {

        Helmet,
        Chestplate,
        Leggings,
        Boots;

        public ItemList getItem() {
            return switch (this) {
                case Helmet -> ItemList.Mechanical_Helmet;
                case Chestplate -> ItemList.Mechanical_Chestplate;
                case Leggings -> ItemList.Mechanical_Leggings;
                case Boots -> ItemList.Mechanical_Boots;
            };
        }
    }

    public enum Frames implements IArmorPart {

        // spotless:off
        Light(ItemList.Armor_Frame_Light, new FrameBuilder()
            .setId("Light")
            .setItemId("armorframelight")
            .setSlotCounts(1, 2, 0, 0)
            .setColor(Materials.Iron.mRGBa)
            .setRarity(EnumRarity.common)
            .setProtection(8)
        ),
        Medium(ItemList.Armor_Frame_Medium, new FrameBuilder()
            .setId("Medium")
            .setItemId("armorframemedium")
            .setSlotCounts(1, 1, 1, 0)
            .setColor(Materials.Bronze.mRGBa)
            .setRarity(EnumRarity.common)
            .setProtection(11)
        ),
        Heavy(ItemList.Armor_Frame_Heavy, new FrameBuilder()
            .setId("Heavy")
            .setItemId("armorframeheavy")
            .setSlotCounts(2, 0, 1, 0)
            .setColor(Materials.Steel.mRGBa)
            .setRarity(EnumRarity.common)
            .setProtection(15)
        ),

        Nimble(ItemList.Armor_Frame_Nimble, new FrameBuilder()
            .setId("Nimble")
            .setItemId("armorframenimble")
            .setSlotCounts(1, 3, 1, 0)
            .setColor(Materials.VibrantAlloy.mRGBa)
            .setRarity(EnumRarity.uncommon)
            .setProtection(10)
        ),
        Adaptive(ItemList.Armor_Frame_Adaptive, new FrameBuilder()
            .setId("Adaptive")
            .setItemId("armorframeadaptive")
            .setSlotCounts(2, 1, 1, 1)
            .setColor(Materials.Titanium.mRGBa)
            .setRarity(EnumRarity.uncommon)
            .setProtection(13)
        ),
        Tough(ItemList.Armor_Frame_Tough, new FrameBuilder()
            .setId("Tough")
            .setItemId("armorframetough")
            .setSlotCounts(3, 0, 2, 0)
            .setColor(Materials.TungstenSteel.mRGBa)
            .setRarity(EnumRarity.uncommon)
            .setProtection(17)
        ),

        Lightning(ItemList.Armor_Frame_Lightning, new FrameBuilder()
            .setId("Lightning")
            .setItemId("armorframelightning")
            .setSlotCounts(1, 4, 1, 1)
            .setColor(Materials.Electrum.mRGBa)
            .setRarity(EnumRarity.rare)
            .setProtection(15)
        ),
        Morphic(ItemList.Armor_Frame_Morphic, new FrameBuilder()
            .setId("Morphic")
            .setItemId("armorframemorphic")
            .setSlotCounts(2, 1, 2, 2)
            .setColor(Materials.RedstoneAlloy.mRGBa)
            .setRarity(EnumRarity.rare)
            .setProtection(17)
        ),
        Bulwark(ItemList.Armor_Frame_Bulwark, new FrameBuilder()
            .setId("Bulwark")
            .setItemId("armorframebulwark")
            .setSlotCounts(4, 1, 1, 1)
            .setColor(Materials.RedSteel.mRGBa)
            .setRarity(EnumRarity.rare)
            .setProtection(20)
        ),

        Infinity(ItemList.Armor_Frame_Infinity, new FrameBuilder()
            .setId("Infinity")
            .setItemId("armorframeinfinity")
            .setSlotCounts(5, 5, 5, 5)
            .setColor(Materials.Infinity.mRGBa)
            .setRarity(EnumRarity.epic)
            .setProtection(30)
        );
        // spotless:on

        private final ItemList item;
        private final FrameBuilder builder;

        Frames(ItemList item, FrameBuilder builder) {
            this.item = item;
            this.builder = builder.finish();
        }

        public void registerItem() {
            item.set(new ItemAugmentFrame(this));
        }

        @Override
        public ItemStack getItem(int amount) {
            return item.get(amount);
        }

        @Override
        public String getId() {
            return this.builder.getId();
        }

        @Override
        public String getItemId() {
            return this.builder.getItemId();
        }

        @Override
        public String getLocalizedName() {
            return this.builder.getRarity().rarityColor + this.builder.getLocalizedName();
        }

        @Override
        public @NotNull EnumRarity getRarity() {
            return this.builder.getRarity();
        }

        @Override
        public boolean hasTooltip() {
            return this.builder.hasTooltip();
        }

        @Override
        public String getTooltip() {
            return this.builder.getTooltip();
        }

        @Override
        public Collection<IArmorBehavior> getProvidedBehaviors() {
            return this.builder.getProvidedBehaviors();
        }

        @Override
        public Collection<BehaviorName> getRequiredBehaviors() {
            return this.builder.getRequiredBehaviors();
        }

        @Override
        public Collection<BehaviorName> getIncompatibleBehaviors() {
            return this.builder.getIncompatibleBehaviors();
        }

        public int getProtectionSlots() {
            return this.builder.getProtectionSlots();
        }

        public int getMovementSlots() {
            return this.builder.getMovementSlots();
        }

        public int getUtilitySlots() {
            return this.builder.getUtilitySlots();
        }

        public int getPrismaticSlots() {
            return this.builder.getPrismaticSlots();
        }

        public short[] getColor() {
            return this.builder.getColor();
        }

        public int getProtection() {
            return this.builder.getProtection();
        }

        static {
            for (Frames f : Frames.values()) framesMap.put(f.builder.getId(), f);
        }
    }

    public enum Cores implements IArmorPart {

        // spotless:off
        Nano(ItemList.Armor_Core_T1, new CoreBuilder()
            .setId("Nano")
            .setItemId("armorcore1")
            .setTier(1)
            .setRarity(EnumRarity.common)
            .setChargeMax(1_000_000)
            .setChargeTier(VoltageIndex.HV)
        ),
        Quantum(ItemList.Armor_Core_T2, new CoreBuilder()
            .setId("Quantum")
            .setItemId("armorcore2")
            .setTier(2)
            .setRarity(EnumRarity.uncommon)
            .setChargeMax(10_000_000)
            .setChargeTier(VoltageIndex.IV)
        ),
        Living(ItemList.Armor_Core_T3, new CoreBuilder()
            .setId("Living")
            .setItemId("armorcore3")
            .setTier(3)
            .setRarity(EnumRarity.rare)
            .setChargeMax(100_000_000)
            .setChargeTier(VoltageIndex.ZPM)
        ),
        Singularity(ItemList.Armor_Core_T4, new CoreBuilder()
            .setId("Singularity")
            .setItemId("armorcore4")
            .setTier(4)
            .setRarity(EnumRarity.epic)
            .setChargeMax(0)
            .setChargeTier(VoltageIndex.UHV)
            .providesBehaviors(InfiniteEnergyBehavior.INSTANCE)
        );
        // spotless:on

        private final ItemList item;
        private final CoreBuilder builder;

        Cores(ItemList item, CoreBuilder builder) {
            this.item = item;
            this.builder = builder.finish();
        }

        void registerItem() {
            item.set(new ItemAugmentCore(this));
        }

        @Override
        public ItemStack getItem(int amount) {
            return item.get(amount);
        }

        @Override
        public String getId() {
            return this.builder.getId();
        }

        @Override
        public String getItemId() {
            return this.builder.getItemId();
        }

        @Override
        public String getLocalizedName() {
            return "§s" + getRarity().rarityColor + this.builder.getLocalizedName() + "§t";
        }

        @Override
        public @NotNull EnumRarity getRarity() {
            return this.builder.getRarity();
        }

        @Override
        public boolean hasTooltip() {
            return this.builder.hasTooltip();
        }

        @Override
        public String getTooltip() {
            return this.builder.getTooltip();
        }

        @Override
        public Collection<IArmorBehavior> getProvidedBehaviors() {
            return this.builder.getProvidedBehaviors();
        }

        @Override
        public Collection<BehaviorName> getRequiredBehaviors() {
            return this.builder.getRequiredBehaviors();
        }

        @Override
        public Collection<BehaviorName> getIncompatibleBehaviors() {
            return this.builder.getIncompatibleBehaviors();
        }

        public int getTier() {
            return builder.getTier();
        }

        public int getChargeTier() {
            return builder.getChargeTier();
        }

        public int getChargeMax() {
            return builder.getChargeMax();
        }

        static {
            for (Cores c : Cores.values()) coresMap.put(c.builder.getId(), c);
        }
    }

    public enum Augments implements IArmorPart {

        // spotless:off
        NightVision(ItemList.Augment_NightVision, new AugmentBuilder()
            .setId("NightVision")
            .setItemId("augmentnightvision")
            .fitsInto(ArmorType.Helmet)
            .providesBehaviors(NightVisionBehavior.INSTANCE)
            .setCategory(AugmentCategory.Utility)
            .setRarity(EnumRarity.common)
        ),
        CreativeFlight(ItemList.Augment_CreativeFlight, new AugmentBuilder()
            .setId("CreativeFlight")
            .setItemId("augmentcreativeflight")
            .fitsInto(ArmorType.Chestplate)
            .providesBehaviors(CreativeFlightBehavior.INSTANCE)
            .incompatibleBehaviors(BehaviorName.Jetpack)
            .setCategory(AugmentCategory.Movement)
            .setMinimumCore(2)
            .setRarity(EnumRarity.epic)
        ),
        Jetpack(ItemList.Augment_Jetpack, new AugmentBuilder()
            .setId("Jetpack")
            .setItemId("augmentjetpack")
            .fitsInto(ArmorType.Chestplate)
            .providesBehaviors(JetpackBehavior.INSTANCE, JetpackHoverBehavior.INSTANCE)
            .incompatibleBehaviors(BehaviorName.CreativeFlight)
            .setCategory(AugmentCategory.Movement)
            .setRarity(EnumRarity.uncommon)
        ),
        JetpackPerfectHover(ItemList.Augment_Jetpack_PerfectHover, new AugmentBuilder()
            .setId("JetpackPerfectHover")
            .setItemId("augmentjetpackperfecthover")
            .fitsInto(ArmorType.Chestplate)
            .providesBehaviors(JetpackPerfectHoverBehavior.INSTANCE)
            .requiresBehaviors(BehaviorName.Jetpack)
            .setCategory(AugmentCategory.Movement)
            .setMinimumCore(1)
            .setRarity(EnumRarity.rare)
        ),
        FireImmunity(ItemList.Augment_FireImmunity, new AugmentBuilder()
            .setId("FireImmunity")
            .setItemId("augmentfireimmunity")
            .fitsInto(ArmorType.Leggings)
            .providesBehaviors(FireImmunityBehavior.INSTANCE)
            .setCategory(AugmentCategory.Protection)
            .setMinimumCore(1)
            .setRarity(EnumRarity.common)
        ),
        StepAssist(ItemList.Augment_StepAssist, new AugmentBuilder()
            .setId("StepAssist")
            .setItemId("augmentstepassist")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(StepAssistBehavior.INSTANCE)
            .setCategory(AugmentCategory.Movement)
            .setMinimumCore(1)
            .setRarity(EnumRarity.common)
        ),
        GogglesOfRevealing(ItemList.Augment_GogglesOfRevealing, new AugmentBuilder()
            .setId("GogglesOfRevealing")
            .setItemId("augmentgogglesofrevealing")
            .fitsInto(ArmorType.Helmet)
            .providesBehaviors(GogglesOfRevealingBehavior.INSTANCE, new VisDiscountBehavior(7))
            .setCategory(AugmentCategory.Utility)
            .setRarity(EnumRarity.uncommon)
        ),
        InertiaCanceling(ItemList.Augment_InertiaCanceling, new AugmentBuilder()
            .setId("InertiaCanceling")
            .setItemId("augmentinertiacanceling")
            .fitsInto(ArmorType.Chestplate)
            .providesBehaviors(InertiaCancelingBehavior.INSTANCE)
            .requiresBehaviors(BehaviorName.CreativeFlight)
            .setCategory(AugmentCategory.Movement)
            .setMinimumCore(2)
            .setRarity(EnumRarity.rare)
        ),
        Hazmat(ItemList.Augment_Hazmat, new AugmentBuilder()
            .setId("Hazmat")
            .setItemId("augmenthazmat")
            .providesBehaviors(HazmatBehavior.INSTANCE)
            .setCategory(AugmentCategory.Protection)
            .setRarity(EnumRarity.common)
        ),
        Apiarist(ItemList.Augment_Apiarist, new AugmentBuilder()
            .setId("Apiarist")
            .setItemId("augmentapiarist")
            .fitsInto(ArmorType.Leggings)
            .providesBehaviors(ApiaristBehavior.INSTANCE)
            .setCategory(AugmentCategory.Utility)
            .setRarity(EnumRarity.uncommon)
        ),
        SwimSpeed(ItemList.Augment_SwimSpeed, new AugmentBuilder()
            .setId("SwimSpeed")
            .setItemId("augmentswimspeed")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(SwimSpeedBehavior.INSTANCE)
            .setCategory(AugmentCategory.Movement)
            .setMaxStack(2)
            .setRarity(EnumRarity.common)
        ),
        KnockbackResistance(ItemList.Augment_KnockbackResistance, new AugmentBuilder()
            .setId("KnockbackResistance")
            .setItemId("augmentknockbackresistance")
            .fitsInto(ArmorType.Leggings)
            .providesBehaviors(KnockbackResistBehavior.INSTANCE)
            .setCategory(AugmentCategory.Protection)
            .setRarity(EnumRarity.uncommon)
        ),
        SpeedBoost(ItemList.Augment_SpeedBoost, new AugmentBuilder()
            .setId("SpeedBoost")
            .setItemId("augmentspeedboost")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(SpeedBoostBehavior.MECH_ARMOR_INSTANCE)
            .setCategory(AugmentCategory.Movement)
            .setMaxStack(3)
            .setRarity(EnumRarity.common)
        ),
        JumpBoost(ItemList.Augment_JumpBoost, new AugmentBuilder()
            .setId("JumpBoost")
            .setItemId("augmentjumpboost")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(JumpBoostBehavior.MECH_ARMOR_INSTANCE)
            .setCategory(AugmentCategory.Movement)
            .setMaxStack(2)
            .setRarity(EnumRarity.common)
        ),
        FallProtection(ItemList.Augment_FallProtection, new AugmentBuilder()
            .setId("FallProtection")
            .setItemId("augmentfallprotection")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(FallProtectionBehavior.INSTANCE)
            .setCategory(AugmentCategory.Protection)
            .setRarity(EnumRarity.uncommon)
        ),
        SpaceSuit(ItemList.Augment_SpaceSuit, new AugmentBuilder()
            .setId("SpaceSuit")
            .setItemId("augmentspacesuit")
            .providesBehaviors(SpaceSuitBehavior.INSTANCE)
            .setMinimumCore(1)
            .setCategory(AugmentCategory.Protection)
            .setRarity(EnumRarity.rare)
        ),
        ForceField(ItemList.Augment_ForceField, new AugmentBuilder()
            .setId("ForceField")
            .setItemId("augmentforcefield")
            .fitsInto(ArmorType.Chestplate)
            .providesBehaviors(ForceFieldBehavior.INSTANCE)
            .setCategory(AugmentCategory.Protection)
            .setRarity(EnumRarity.epic)
        ),
        OmniMovement(ItemList.Augment_OmniMovement, new AugmentBuilder()
            .setId("OmniMovement")
            .setItemId("augmentomnimovement")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(OmniMovementBehavior.INSTANCE)
            .requiresBehaviors(BehaviorName.SpeedBoost)
            .setMinimumCore(1)
            .setCategory(AugmentCategory.Movement)
            .setRarity(EnumRarity.rare)
        ),
        WaterBreathing(ItemList.Augment_WaterBreathing, new AugmentBuilder()
            .setId("WaterBreathing")
            .setItemId("augmentwaterbreathing")
            .fitsInto(ArmorType.Helmet)
            .providesBehaviors(WaterBreathingBehavior.INSTANCE)
            .setCategory(AugmentCategory.Utility)
            .setRarity(EnumRarity.uncommon)
        );
        // spotless:on

        private final ItemList item;
        private final AugmentBuilder builder;

        Augments(ItemList item, AugmentBuilder builder) {
            this.item = item;
            this.builder = builder.finish();
        }

        void registerItem() {
            item.set(new ItemAugment(this));
        }

        @Override
        public ItemStack getItem(int amount) {
            return item.get(amount);
        }

        @Override
        public String getId() {
            return this.builder.getId();
        }

        @Override
        public String getItemId() {
            return this.builder.getItemId();
        }

        @Override
        public String getLocalizedName() {
            return "§s" + getRarity().rarityColor + this.builder.getLocalizedName() + "§t";
        }

        @Override
        public @NotNull EnumRarity getRarity() {
            return this.builder.getRarity();
        }

        @Override
        public boolean hasTooltip() {
            return this.builder.hasTooltip();
        }

        @Override
        public String getTooltip() {
            return this.builder.getTooltip();
        }

        @Override
        public Collection<IArmorBehavior> getProvidedBehaviors() {
            return this.builder.getProvidedBehaviors();
        }

        @Override
        public Collection<BehaviorName> getRequiredBehaviors() {
            return this.builder.getRequiredBehaviors();
        }

        @Override
        public Collection<BehaviorName> getIncompatibleBehaviors() {
            return this.builder.getIncompatibleBehaviors();
        }

        public AugmentCategory getCategory() {
            return this.builder.getCategory();
        }

        public Cores getMinimumCore() {
            return Cores.values()[this.builder.getMinimumCore()];
        }

        public int getMaxStack() {
            return this.builder.getMaxStack();
        }

        public Collection<ArmorType> getAllowedArmorTypes() {
            return this.builder.getAllowedArmorTypes();
        }

        static {
            for (Augments a : Augments.values()) {
                augmentsMap.put(a.builder.getId(), a);
            }
        }
    }
}
