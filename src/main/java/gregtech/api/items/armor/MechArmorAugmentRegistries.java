package gregtech.api.items.armor;

import static gregtech.loaders.ExtraIcons.creativeFlightAugment;
import static gregtech.loaders.ExtraIcons.forceFieldAugment;
import static gregtech.loaders.ExtraIcons.holoInventoryAugment;
import static gregtech.loaders.ExtraIcons.jetpackAugment;
import static gregtech.loaders.ExtraIcons.nightVisionAugment;
import static gregtech.loaders.ExtraIcons.rebreatherAugment;
import static gregtech.loaders.ExtraIcons.revealingAugment;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

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
import gregtech.api.items.armor.behaviors.HoloInventoryBehavior;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.items.armor.behaviors.InertiaCancelingBehavior;
import gregtech.api.items.armor.behaviors.InfiniteEnergyBehavior;
import gregtech.api.items.armor.behaviors.JetpackBehavior;
import gregtech.api.items.armor.behaviors.JetpackHoverBehavior;
import gregtech.api.items.armor.behaviors.JetpackPerfectHoverBehavior;
import gregtech.api.items.armor.behaviors.JumpBoostBehavior;
import gregtech.api.items.armor.behaviors.KnockbackResistBehavior;
import gregtech.api.items.armor.behaviors.MilkInfusionBehavior;
import gregtech.api.items.armor.behaviors.NightVisionBehavior;
import gregtech.api.items.armor.behaviors.OmniMovementBehavior;
import gregtech.api.items.armor.behaviors.SoulboundBehavior;
import gregtech.api.items.armor.behaviors.SpaceSuitBehavior;
import gregtech.api.items.armor.behaviors.SpeedBoostBehavior;
import gregtech.api.items.armor.behaviors.StepAssistBehavior;
import gregtech.api.items.armor.behaviors.SwimSpeedBehavior;
import gregtech.api.items.armor.behaviors.TerrasteelBehavior;
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
            .setColor(new short[] {103, 90, 104, 0})
            .setRarity(EnumRarity.common)
            .setProtection(0.7f)
        ),
        Medium(ItemList.Armor_Frame_Medium, new FrameBuilder()
            .setId("Medium")
            .setItemId("armorframemedium")
            .setSlotCounts(1, 0, 2, 0)
            .setColor(new short[] {146, 153, 171, 0})
            .setRarity(EnumRarity.common)
            .setProtection(0.75f)
        ),
        Heavy(ItemList.Armor_Frame_Heavy, new FrameBuilder()
            .setId("Heavy")
            .setItemId("armorframeheavy")
            .setSlotCounts(2, 0, 1, 0)
            .setColor(new short[] {74, 78, 87, 0})
            .setRarity(EnumRarity.common)
            .setProtection(0.8f)
        ),

        Nimble(ItemList.Armor_Frame_Nimble, new FrameBuilder()
            .setId("Nimble")
            .setItemId("armorframenimble")
            .setSlotCounts(1, 3, 1, 0)
            .setColor(new short[] {182, 35, 40, 0})
            .setRarity(EnumRarity.uncommon)
            .setProtection(0.8f)
        ),
        Adaptive(ItemList.Armor_Frame_Adaptive, new FrameBuilder()
            .setId("Adaptive")
            .setItemId("armorframeadaptive")
            .setSlotCounts(2, 1, 1, 1)
            .setColor(new short[] {93, 115, 35, 0})
            .setRarity(EnumRarity.uncommon)
            .setProtection(0.85f)
        ),
        Tough(ItemList.Armor_Frame_Tough, new FrameBuilder()
            .setId("Tough")
            .setItemId("armorframetough")
            .setSlotCounts(3, 0, 2, 0)
            .setColor(new short[] {44, 33, 35, 0})
            .setRarity(EnumRarity.uncommon)
            .setProtection(0.9f)
        ),

        Lightning(ItemList.Armor_Frame_Lightning, new FrameBuilder()
            .setId("Lightning")
            .setItemId("armorframelightning")
            .setSlotCounts(1, 4, 1, 1)
            .setColor(new short[] {121, 84, 206, 0})
            .setRarity(EnumRarity.rare)
            .setProtection(0.9f)
        ),
        Morphic(ItemList.Armor_Frame_Morphic, new FrameBuilder()
            .setId("Morphic")
            .setItemId("armorframemorphic")
            .setSlotCounts(2, 1, 2, 2)
            .setColor(new short[] {156, 212, 227, 0})
            .setRarity(EnumRarity.rare)
            .setProtection(0.95f)
        ),
        Bulwark(ItemList.Armor_Frame_Bulwark, new FrameBuilder()
            .setId("Bulwark")
            .setItemId("armorframebulwark")
            .setSlotCounts(4, 1, 1, 1)
            .setColor(new short[] {113, 88, 78, 0})
            .setRarity(EnumRarity.rare)
            .setProtection(0.99f)
        ),

        Infinity(ItemList.Armor_Frame_Infinity, new FrameBuilder()
            .setId("Infinity")
            .setItemId("armorframeinfinity")
            .setSlotCounts(5, 5, 5, 5)
            .setColor(Materials.Infinity.mRGBa)
            .setRarity(EnumRarity.epic)
            .setProtection(1)
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

        @Override
        public Collection<ItemList> getIncompatibleAugments() {
            return this.builder.getIncompatibleAugments();
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

        public float getProtection() {
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

        @Override
        public Collection<ItemList> getIncompatibleAugments() {
            return this.builder.getIncompatibleAugments();
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

        // When adding new augments please sort them here according to tier and category.
        // This sorts the registration order and therefore the order in which they appear in NEI.

        // Tier 1 - Protection
        Hazmat(ItemList.Augment_Hazmat, new AugmentBuilder()
            .setId("Hazmat")
            .setItemId("augmenthazmat")
            .providesBehaviors(HazmatBehavior.INSTANCE)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Protection)
        ),
        FallProtection(ItemList.Augment_FallProtection, new AugmentBuilder()
            .setId("FallProtection")
            .setItemId("augmentfallprotection")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(FallProtectionBehavior.INSTANCE)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Protection)
        ),
        SpaceSuit(ItemList.Augment_SpaceSuit, new AugmentBuilder()
            .setId("SpaceSuit")
            .setItemId("augmentspacesuit")
            .providesBehaviors(SpaceSuitBehavior.INSTANCE)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Protection)
        ),
        WaterBreathing(ItemList.Augment_WaterBreathing, new AugmentBuilder()
            .setId("WaterBreathing")
            .setItemId("augmentwaterbreathing")
            .fitsInto(ArmorType.Helmet)
            .setTexture(ArmorType.Helmet, () -> rebreatherAugment)
            .providesBehaviors(WaterBreathingBehavior.INSTANCE)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Protection)
        ),

        // Tier 1 - Movement
        Jetpack(ItemList.Augment_Jetpack, new AugmentBuilder()
            .setId("Jetpack")
            .setItemId("augmentjetpack")
            .fitsInto(ArmorType.Chestplate)
            .setTexture(ArmorType.Chestplate, () -> jetpackAugment)
            .providesBehaviors(JetpackBehavior.INSTANCE, JetpackHoverBehavior.INSTANCE)
            .incompatibleBehaviors(BehaviorName.CreativeFlight)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Movement)
        ),
        SwimSpeed(ItemList.Augment_SwimSpeed, new AugmentBuilder()
            .setId("SwimSpeed")
            .setItemId("augmentswimspeed")
            .fitsInto(ArmorType.Leggings)
            .providesBehaviors(SwimSpeedBehavior.INSTANCE)
            .setCategory(AugmentCategory.Movement)
            .setMinimumCoreTier(1)
            .setMaxStack(2)
        ),
        ApprenticeStriders(ItemList.Augment_ApprenticeStriders, new AugmentBuilder()
            .setId("ApprenticeStriders")
            .setItemId("augmentapprenticestriders")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(
                new SpeedBoostBehavior(3.0F),
                new JumpBoostBehavior(3.0F),
                StepAssistBehavior.INSTANCE,
                new VisDiscountBehavior(4)
            )
            .incompatibleAugments(
                ItemList.Augment_ArchmageStriders,
                ItemList.Augment_EldritchStriders,
                ItemList.Augment_StepAssist
            )
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Movement)
        ),

        // Tier 1 - Utility
        Soulbound(ItemList.Augment_Soulbound, new AugmentBuilder()
            .setId("Soulbound")
            .setItemId("augmentsoulbound")
            .providesBehaviors(SoulboundBehavior.INSTANCE)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Utility)
        ),
        NightVision(ItemList.Augment_NightVision, new AugmentBuilder()
            .setId("NightVision")
            .setItemId("augmentnightvision")
            .fitsInto(ArmorType.Helmet)
            .setTexture(ArmorType.Helmet, () -> nightVisionAugment)
            .providesBehaviors(NightVisionBehavior.INSTANCE)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Utility)
        ),
        GogglesOfRevealing(ItemList.Augment_GogglesOfRevealing, new AugmentBuilder()
            .setId("GogglesOfRevealing")
            .setItemId("augmentgogglesofrevealing")
            .fitsInto(ArmorType.Helmet)
            .setTexture(ArmorType.Helmet, () -> revealingAugment)
            .providesBehaviors(GogglesOfRevealingBehavior.INSTANCE, new VisDiscountBehavior(7))
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Utility)
        ),
        Apiarist(ItemList.Augment_Apiarist, new AugmentBuilder()
            .setId("Apiarist")
            .setItemId("augmentapiarist")
            .providesBehaviors(ApiaristBehavior.INSTANCE)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Utility)
        ),
        HoloInventory(ItemList.Augment_HoloInventory, new AugmentBuilder()
            .setId("HoloInventory")
            .setItemId("augmentholoinventory")
            .providesBehaviors(HoloInventoryBehavior.INSTANCE)
            .setTexture(ArmorType.Helmet,() -> holoInventoryAugment)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Utility)
        ),
        TerrasteelAugment(ItemList.Augment_Terrasteel, new AugmentBuilder()
            .setId("Terrasteel")
            .setItemId("augmentterrasteel")
            .providesBehaviors(TerrasteelBehavior.INSTANCE)
            .setMinimumCoreTier(1)
            .setCategory(AugmentCategory.Utility)
        ),

        // Tier 2 - Protection
        FireImmunity(ItemList.Augment_FireImmunity, new AugmentBuilder()
            .setId("FireImmunity")
            .setItemId("augmentfireimmunity")
            .fitsInto(ArmorType.Leggings)
            .providesBehaviors(FireImmunityBehavior.INSTANCE)
            .setMinimumCoreTier(2)
            .setCategory(AugmentCategory.Protection)
        ),
        KnockbackResistance(ItemList.Augment_KnockbackResistance, new AugmentBuilder()
            .setId("KnockbackResistance")
            .setItemId("augmentknockbackresistance")
            .fitsInto(ArmorType.Leggings)
            .providesBehaviors(KnockbackResistBehavior.INSTANCE)
            .setMinimumCoreTier(2)
            .setCategory(AugmentCategory.Protection)
        ),

        // Tier 2 - Movement
        JetpackPerfectHover(ItemList.Augment_Jetpack_PerfectHover, new AugmentBuilder()
            .setId("JetpackPerfectHover")
            .setItemId("augmentjetpackperfecthover")
            .fitsInto(ArmorType.Chestplate)
            .providesBehaviors(JetpackPerfectHoverBehavior.INSTANCE)
            .requiresBehaviors(BehaviorName.Jetpack)
            .setMinimumCoreTier(2)
            .setCategory(AugmentCategory.Movement)
        ),
        StepAssist(ItemList.Augment_StepAssist, new AugmentBuilder()
            .setId("StepAssist")
            .setItemId("augmentstepassist")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(StepAssistBehavior.INSTANCE)
            .incompatibleAugments(
                ItemList.Augment_ArchmageStriders,
                ItemList.Augment_ApprenticeStriders,
                ItemList.Augment_EldritchStriders
            )
            .setMinimumCoreTier(2)
            .setCategory(AugmentCategory.Movement)
        ),
        SpeedBoost(ItemList.Augment_SpeedBoost, new AugmentBuilder()
            .setId("SpeedBoost")
            .setItemId("augmentspeedboost")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(SpeedBoostBehavior.MECH_ARMOR_INSTANCE)
            .incompatibleAugments(ItemList.Augment_EldritchStriders)
            .setMinimumCoreTier(2)
            .setMaxStack(2)
            .setCategory(AugmentCategory.Movement)
        ),
        JumpBoost(ItemList.Augment_JumpBoost, new AugmentBuilder()
            .setId("JumpBoost")
            .setItemId("augmentjumpboost")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(JumpBoostBehavior.MECH_ARMOR_INSTANCE)
            .incompatibleAugments(ItemList.Augment_EldritchStriders)
            .setMinimumCoreTier(2)
            .setMaxStack(2)
            .setCategory(AugmentCategory.Movement)
        ),
        OmniMovement(ItemList.Augment_OmniMovement, new AugmentBuilder()
            .setId("OmniMovement")
            .setItemId("augmentomnimovement")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(OmniMovementBehavior.INSTANCE)
            .requiresBehaviors(BehaviorName.SpeedBoost)
            .setMinimumCoreTier(2)
            .setCategory(AugmentCategory.Movement)
        ),
        ArchmageStriders(ItemList.Augment_ArchmageStriders, new AugmentBuilder()
            .setId("ArchmageStriders")
            .setItemId("augmentarchmagestriders")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(
                new SpeedBoostBehavior(5.0F),
                new JumpBoostBehavior(4.0F),
                StepAssistBehavior.INSTANCE,
                new VisDiscountBehavior(5)
            )
            .incompatibleAugments(
                ItemList.Augment_ApprenticeStriders,
                ItemList.Augment_EldritchStriders,
                ItemList.Augment_StepAssist
            )
            .setMinimumCoreTier(2)
            .setCategory(AugmentCategory.Movement)
        ),

        // Tier 2 - Utility
        MilkInfusion(ItemList.Augment_MilkInfusion, new AugmentBuilder()
            .setId("MilkInfusion")
            .setItemId("augmentmilkinfusion")
            .fitsInto(ArmorType.Helmet)
            .providesBehaviors(MilkInfusionBehavior.INSTANCE)
            .setMinimumCoreTier(2)
            .setCategory(AugmentCategory.Utility)
        ),

        // Tier 3 - Protection
        ForceField(ItemList.Augment_ForceField, new AugmentBuilder()
            .setId("ForceField")
            .setItemId("augmentforcefield")
            .fitsInto(ArmorType.Chestplate)
            .setTexture(ArmorType.Chestplate,() ->forceFieldAugment)
            .providesBehaviors(ForceFieldBehavior.INSTANCE)
            .setMinimumCoreTier(3)
            .setCategory(AugmentCategory.Protection)
        ),

        // Tier 3 - Movement
        CreativeFlight(ItemList.Augment_CreativeFlight, new AugmentBuilder()
            .setId("CreativeFlight")
            .setItemId("augmentcreativeflight")
            .fitsInto(ArmorType.Chestplate)
            .setTexture(ArmorType.Chestplate, () -> creativeFlightAugment)
            .providesBehaviors(CreativeFlightBehavior.INSTANCE)
            .incompatibleBehaviors(BehaviorName.Jetpack)
            .setMinimumCoreTier(3)
            .setCategory(AugmentCategory.Movement)
        ),
        InertiaCanceling(ItemList.Augment_InertiaCanceling, new AugmentBuilder()
            .setId("InertiaCanceling")
            .setItemId("augmentinertiacanceling")
            .fitsInto(ArmorType.Chestplate)
            .providesBehaviors(InertiaCancelingBehavior.INSTANCE)
            .requiresBehaviors(BehaviorName.CreativeFlight)
            .setMinimumCoreTier(3)
            .setCategory(AugmentCategory.Movement)
        ),
        EldritchStriders(ItemList.Augment_EldritchStriders, new AugmentBuilder()
            .setId("EldritchStriders")
            .setItemId("augmenteldritchstriders")
            .fitsInto(ArmorType.Boots)
            .providesBehaviors(
                new SpeedBoostBehavior(10.0F),
                new JumpBoostBehavior(5.0F),
                StepAssistBehavior.INSTANCE,
                new VisDiscountBehavior(10)
            )
            .incompatibleAugments(
                ItemList.Augment_ApprenticeStriders,
                ItemList.Augment_ArchmageStriders,
                ItemList.Augment_SpeedBoost,
                ItemList.Augment_JumpBoost,
                ItemList.Augment_StepAssist
            )
            .setMinimumCoreTier(3)
            .setCategory(AugmentCategory.Movement)
        );

        // Tier 3 - Utility

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

        public ItemList getListItem() {
            return item;
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

        @Override
        public Collection<ItemList> getIncompatibleAugments() {
            return this.builder.getIncompatibleAugments();
        }

        public IIcon getTexture(ArmorType armorType) {
            return this.builder.getTexture(armorType);
        }

        public AugmentCategory getCategory() {
            return this.builder.getCategory();
        }

        public Cores getMinimumCore() {
            int minTier = this.builder.getMinimumCoreTier();
            for (Cores core : Cores.values()) {
                if (core.getTier() == minTier) return core;
            }
            return Cores.Nano;
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
