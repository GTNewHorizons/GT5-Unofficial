package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.items.armor.ArmorHelper.SLOT_CHEST;
import static gregtech.api.items.armor.ArmorHelper.SLOT_LEGS;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.IKeyPressedListener;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IArmorApiarist;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods.ModIDs;
import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorContext.ArmorContextImpl;
import gregtech.api.items.armor.ArmorState;
import gregtech.api.items.armor.MechArmorAugmentRegistries.ArmorType;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Cores;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.common.misc.NoTooltipElectricItemManager;
import ic2.api.item.ICustomDamageItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;

@InterfaceList(
    value = { @Interface(iface = "forestry.api.apiculture.IArmorApiarist", modid = ModIDs.FORESTRY),
        @Interface(iface = "thaumcraft.api.IVisDiscountGear", modid = ModIDs.THAUMCRAFT),
        @Interface(iface = "thaumcraft.api.IGoggles", modid = ModIDs.THAUMCRAFT),
        @Interface(iface = "thaumcraft.api.nodes.IRevealer", modid = ModIDs.THAUMCRAFT), })

public class MechArmorBase extends ItemArmor implements IKeyPressedListener, ISpecialArmor, ISpecialElectricItem,
    IGoggles, IRevealer, IVisDiscountGear, IArmorApiarist, IHazardProtector, ICustomDamageItem {

    protected IIcon coreIcon;
    protected IIcon frameIcon;

    protected String type;

    public static final String MECH_FRAME_KEY = "frame";
    public static final String MECH_CORE_KEY = "core";

    private final int slot;

    public MechArmorBase(int slot, String type, int register) {
        super(ArmorMaterial.IRON, 2, register);
        this.slot = slot;
        this.type = type;
        this.setMaxDamage(0);
        this.setHasSubtypes(false);
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
        p_150895_3_.add(getStack());
    }

    public @NotNull ItemStack getStack() {
        ItemStack stack = new ItemStack(this);

        // Set behaviors
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        tag.setString(MECH_CORE_KEY, "None");
        tag.setString(MECH_FRAME_KEY, "None");
        return stack;
    }

    public static ArmorContext load(EntityLivingBase entity, ItemStack stack) {
        if (entity instanceof EntityPlayer player) {
            return load(player.getEntityWorld(), player, stack);
        } else {
            ArmorContextImpl context = new ArmorContextImpl(stack, null);

            ArmorState.load(context);

            return context;
        }
    }

    public static ArmorContext load(World world, EntityPlayer player, ItemStack stack) {
        ArmorContextImpl context = new ArmorContextImpl(world, player, stack, null);

        ArmorState.load(context);

        return context;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        ArmorContext context = load(world, player, stack);

        for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
            behavior.onArmorTick(context);
        }

        context.save();
    }

    public void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        ArmorContext context = load(world, player, stack);

        for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
            if (player instanceof EntityPlayerMP playerMP) {
                for (SyncedKeybind keyBind : behavior.getListenedKeys(context)) {
                    keyBind.removePlayerListener(playerMP, this);
                }
            }

            behavior.onArmorUnequip(context);
        }
    }

    public void onArmorEquip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        ArmorContext context = load(world, player, stack);

        if (player instanceof EntityPlayerMP playerMP) {
            boolean initMessage = false;

            for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
                for (SyncedKeybind keyBind : behavior.getListenedKeys(context)) {
                    keyBind.registerPlayerListener(playerMP, this);

                    if (!initMessage) {
                        GTUtility.sendChatToPlayer(player, "Armor Systems Online... Active keybindings: ");
                        initMessage = true;
                    }

                    GTUtility.sendChatToPlayer(
                        playerMP,
                        StatCollector.translateToLocal(
                            keyBind.getKeybinding()
                                .getKeyDescription())
                            + ": "
                            + Keyboard.getKeyName(keyBind.getKeyCode()));
                }
            }
        }

        for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
            behavior.onArmorEquip(context);
        }

        context.save();
    }

    @Override
    public void onKeyPressed(EntityPlayerMP player, SyncedKeybind keyPressed, boolean isDown) {
        ItemStack stack = player.getCurrentArmor(slot);

        if (stack == null) return;
        if (stack.getItem() != this) return;

        ArmorContext context = load(player.getEntityWorld(), player, stack);

        boolean didSomething = false;

        for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
            if (!behavior.getListenedKeys(context)
                .contains(keyPressed)) continue;

            didSomething = true;
            behavior.onKeyPressed(context, keyPressed, isDown);
        }

        if (didSomething) {
            ArmorState.save(context);
        }
    }

    public IIcon getCoreIcon() {
        return coreIcon;
    }

    public IIcon getFrameIcon() {
        return frameIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        itemIcon = aIconRegister.registerIcon(GregTech.getResourcePath("mech_armor/" + type, type + "_skeleton"));
        frameIcon = aIconRegister.registerIcon(GregTech.getResourcePath("mech_armor/" + type, type + "_frame"));
        coreIcon = aIconRegister.registerIcon(GregTech.getResourcePath("mech_armor/" + type, type + "_core"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean aF3_H) {
        ArmorContext context = load(player.getEntityWorld(), player, stack);

        context.getArmorState()
            .addArmorInformation(context, tooltip);

        tooltip.replaceAll(GTUtility::processFormatStacks);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (getFrame(stack) == null) {
            if (slot == 2) return GregTech.getResourcePath("textures/items/mech_armor/texture_layer_skeleton2.png");
            return GregTech.getResourcePath("textures/items/mech_armor/texture_layer_skeleton1.png");
        }
        if (slot == 2) return GregTech.getResourcePath("textures/items/mech_armor/texture_layer2.png");
        return GregTech.getResourcePath("textures/items/mech_armor/texture_layer1.png");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        ModelMechArmor model;
        if (armorSlot == SLOT_LEGS) model = new ModelMechArmor(0.25F);
        else model = new ModelMechArmor(0.5F);
        model.bipedHead.showModel = (armorType == 0);
        model.bipedHeadwear.showModel = (armorType == 0);
        model.bipedBody.showModel = ((armorType == 1) || (armorType == 2));
        model.bipedLeftArm.showModel = (armorType == 1);
        model.bipedRightArm.showModel = (armorType == 1);
        model.bipedLeftLeg.showModel = (armorType == 2 || armorType == 3);
        model.bipedRightLeg.showModel = (armorType == 2 || armorType == 3);

        ArmorState state = ArmorState.load(itemStack);

        model.jettank1.showModel = (armorSlot == SLOT_CHEST && state.hasBehavior(BehaviorName.Jetpack));

        if (state.core != null) {
            switch (state.core.getTier()) {
                case 1 -> model.core1.showModel = true;
                case 2 -> model.core2.showModel = true;
                case 3 -> model.core3.showModel = true;
                case 4 -> model.core4.showModel = true;
            }
        }

        return model;
    }

    protected Cores getCore(ItemStack stack) {
        ArmorState state = ArmorState.load(stack);

        return state == null ? null : state.core;
    }

    protected Frames getFrame(ItemStack stack) {
        ArmorState state = ArmorState.load(stack);

        return state == null ? null : state.frame;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        ArmorState state = ArmorState.load(stack);

        if (state == null || state.core == null) return false;
        return !state.hasBehavior(BehaviorName.InfiniteEnergy);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    protected float getProtectionShare() {
        return switch (armorType) {
            case 0, 3 -> 0.15F;
            case 1 -> 0.40F;
            case 2 -> 0.30F;
            default -> 0;
        };
    }

    private float getDamageReduction(ItemStack stack) {
        Frames frame = getFrame(stack);
        if (frame != null) {
            return frame.getProtection() * getProtectionShare();
        }
        return 0;
    }

    // Special armor settings

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
        int slot) {
        if (source.isUnblockable()) return new ArmorProperties(0, getDamageReduction(armor) / 100D, 15);

        ArmorContext context = load(player, armor);

        if (context.isBehaviorActive(BehaviorName.ForceField) && context.drainEnergy(100000 * damage)) {
            context.save();
            return new ArmorProperties(0, 100, Integer.MAX_VALUE);
        }

        if (source.isDamageAbsolute() || source.isMagicDamage() || context.getArmorState().charge < damage * 100) {
            return new ArmorProperties(0, getDamageReduction(armor) / 100D, 15);
        }

        return new ArmorProperties(0, getDamageReduction(armor) / 24.5D, 1000);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) getDamageReduction(armor);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        ArmorContext context = load(entity, stack);

        context.drainEnergy(damage * 100);
    }

    // Thaumcraft compat

    @Override
    public boolean showIngamePopups(ItemStack armor, EntityLivingBase entity) {
        ArmorContext context = load(entity, armor);

        return context.hasBehavior(BehaviorName.GogglesOfRevealing);
    }

    @Override
    public boolean showNodes(ItemStack armor, EntityLivingBase entity) {
        ArmorContext context = load(entity, armor);

        return context.hasBehavior(BehaviorName.GogglesOfRevealing);
    }

    @Override
    public int getVisDiscount(ItemStack armor, EntityPlayer player, Aspect aspect) {
        ArmorContext context = load(player.getEntityWorld(), player, armor);

        return context.getArmorState().visDiscount;
    }

    // Forestry apiarist compat
    @Override
    public boolean protectEntity(EntityLivingBase entity, ItemStack armor, String cause, boolean doProtect) {
        ItemStack leggings = GTDataUtils.getIndexSafe(entity.getLastActiveItems(), SLOT_LEGS);

        if (leggings == null) return false;

        ArmorContext context = load(entity, leggings);

        return context.hasBehavior(BehaviorName.Apiarist);
    }

    @Override
    public boolean protectPlayer(EntityPlayer player, ItemStack armor, String cause, boolean doProtect) {
        return protectEntity(player, armor, cause, doProtect);
    }

    // Hazards

    @Override
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        ArmorContext context = load(null, itemStack);

        for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
            if (behavior.protectsAgainst(context, hazard)) return true;
        }

        return false;
    }

    @Override
    public boolean protectsAgainstFully(@Nullable EntityLivingBase entity, ItemStack itemStack, Hazard hazard) {
        ArmorContext context = load(null, itemStack);

        for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
            if (behavior.protectsAgainstFully(context, hazard)) return true;
        }

        return false;
    }

    // IC2 electric api

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        Cores core = getCore(itemStack);
        return core != null ? core.getChargeMax() : 0;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        Cores core = getCore(itemStack);
        return core != null ? core.getChargeTier() : 0;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        Cores core = getCore(itemStack);
        return core != null ? GTValues.V[core.getChargeTier()] : 0;
    }

    @Override
    public IElectricItemManager getManager(ItemStack itemStack) {
        return NoTooltipElectricItemManager.INSTANCE;
    }

    public ArmorType getArmorType() {
        return switch (armorType) {
            case 0 -> ArmorType.Helmet;
            case 1 -> ArmorType.Chestplate;
            case 2 -> ArmorType.Leggings;
            case 3 -> ArmorType.Boots;
            default -> throw new IllegalStateException("Unexpected value: " + armorType);
        };
    }

    @Override
    public int getCustomDamage(ItemStack itemStack) {
        // do nothing
        return 0;
    }

    @Override
    public int getMaxCustomDamage(ItemStack itemStack) {
        // do nothing
        return 0;
    }

    /// Disables the meta modifications in [ic2.core.item.ElectricItemManager] charge/discharge by ignoring this method
    /// call (which is called by [ic2.core.item.DamageHandler]). This prevents constant armor equipped notifications
    /// from [gregtech.api.items.armor.ArmorEventHandlers].
    @Override
    public void setCustomDamage(ItemStack itemStack, int i) {
        // do nothing
    }

    @Override
    public boolean applyCustomDamage(ItemStack itemStack, int i, EntityLivingBase entityLivingBase) {
        // do nothing
        return false;
    }
}
