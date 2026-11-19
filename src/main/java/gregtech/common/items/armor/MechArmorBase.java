package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.items.armor.ArmorHelper.REGISTER_BOOTS;
import static gregtech.api.items.armor.ArmorHelper.REGISTER_CHEST;
import static gregtech.api.items.armor.ArmorHelper.REGISTER_HELMET;
import static gregtech.api.items.armor.ArmorHelper.REGISTER_LEGS;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.ArrayList;
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
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.IKeyPressedListener;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IArmorApiaristMulti;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods.ModIDs;
import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;
import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorContext.ArmorContextImpl;
import gregtech.api.items.armor.ArmorState;
import gregtech.api.items.armor.MechArmorAugmentRegistries.ArmorType;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Cores;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.items.armor.renderer.ArmorComponent;
import gregtech.api.items.armor.renderer.ArmorComponentRegistry;
import gregtech.api.items.armor.renderer.ArmorRegistry;
import gregtech.api.items.armor.renderer.VoxelArmorRenderer;
import gregtech.api.items.armor.ui.ArmorRadialMenu;
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
    value = { @Interface(iface = "forestry.api.apiculture.IArmorApiaristMulti", modid = ModIDs.FORESTRY),
        @Interface(iface = "thaumcraft.api.IVisDiscountGear", modid = ModIDs.THAUMCRAFT),
        @Interface(iface = "thaumcraft.api.IGoggles", modid = ModIDs.THAUMCRAFT),
        @Interface(iface = "thaumcraft.api.nodes.IRevealer", modid = ModIDs.THAUMCRAFT),
        @Interface(iface = "net.dries007.holoInventory.api.IHoloGlasses", modid = ModIDs.HOLO_INVENTORY),
        @Interface(iface = "vazkii.botania.api.mana.IManaDiscountArmor", modid = ModIDs.BOTANIA) })

public class MechArmorBase extends ItemArmor implements IKeyPressedListener, ISpecialArmor, ISpecialElectricItem,
    IGoggles, IRevealer, IVisDiscountGear, IArmorApiaristMulti, IHazardProtector, ICustomDamageItem,
    net.dries007.holoInventory.api.IHoloGlasses, vazkii.botania.api.mana.IManaDiscountArmor {

    private static final boolean USE_3D_MODELS = true;

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
        API.setAliases(new ItemStack(this), "gt.alias.mechanical_armor");
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

        if (context.isDirty()) context.save();
    }

    public void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        ArmorContext context = load(world, player, stack);

        if (player instanceof EntityPlayerMP playerMP) {
            for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
                for (SyncedKeybind keyBind : behavior.getListenedKeys(context)) {
                    keyBind.removePlayerListener(playerMP, this);
                }
            }

            ArmorActionManager.getKeybind("open_radial_menu")
                .removePlayerListener(playerMP, this);
        }

        for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
            behavior.onArmorUnequip(context);
        }
    }

    public void onArmorEquip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        ArmorContext context = load(world, player, stack);

        if (player instanceof EntityPlayerMP playerMP) {
            ArmorActionManager.getKeybind("open_radial_menu")
                .registerPlayerListener(playerMP, this);

            for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
                for (SyncedKeybind keyBind : behavior.getListenedKeys(context)) {
                    keyBind.registerPlayerListener(playerMP, this);
                }
            }
        }

        if (world.isRemote) {
            boolean initMessage = false;

            for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
                for (SyncedKeybind keyBind : behavior.getListenedKeys(context)) {
                    if (!initMessage) {
                        GTUtility.sendChatTrans(player, "GT5U.armor.message.systems_online");
                        initMessage = true;
                    }

                    if (keyBind.getKeybinding() != null) {
                        GTUtility.sendChatComp(
                            player,
                            new ChatComponentTranslation(
                                keyBind.getKeybinding()
                                    .getKeyDescription()).appendText(": " + Keyboard.getKeyName(keyBind.getKeyCode())));
                    }
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

        if (keyPressed == ArmorActionManager.getKeybind("open_radial_menu")) {
            if (isDown && ArmorActionManager.isPrimaryArmorPiece(player, this)) {
                ArmorRadialMenu.INSTANCE.open(player);
            }
            return;
        }

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

    @SideOnly(Side.CLIENT)
    private ArmorComponent getBaseComponent(ArmorState state) {
        String frameName = (state != null && state.frame != null) ? state.frame.name()
            .toLowerCase() : "frameless";
        String armorTypeName = getArmorType().name()
            .toLowerCase();

        String componentId = frameName + "_" + armorTypeName;

        return ArmorComponentRegistry.getComponent(componentId);
    }

    @SideOnly(Side.CLIENT)
    private List<ArmorComponent> getOverlayComponents(ArmorState state) {
        List<ArmorComponent> overlays = new ArrayList<>();
        if (state == null) return overlays;

        String armorTypeName = getArmorType().name()
            .toLowerCase();

        if (state.core != null) {
            addComponentIfPresent(
                overlays,
                state.core.name()
                    .toLowerCase() + "_" + armorTypeName);
        }

        if (state.augments != null) {
            for (var augment : state.augments.values()) {
                addComponentIfPresent(
                    overlays,
                    augment.name()
                        .toLowerCase() + "_" + armorTypeName);
            }
        }

        return overlays;
    }

    @SideOnly(Side.CLIENT)
    private static void addComponentIfPresent(List<ArmorComponent> components, String componentId) {
        ArmorComponent component = ArmorComponentRegistry.getComponent(componentId);
        if (component != null) components.add(component);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (getFrame(stack) == null) {
            if (slot == 2) return GregTech.getResourcePath("textures/items/mech_armor/texture_layer_skeleton2.png");
            return GregTech.getResourcePath("textures/items/mech_armor/texture_layer_skeleton1.png");
        }

        if (slot == 2) return GregTech.getResourcePath("textures/items/mech_armor/texture_layer_combined2.png");
        return GregTech.getResourcePath("textures/items/mech_armor/texture_layer_combined1.png");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (!USE_3D_MODELS) return null;

        VoxelArmorRenderer model = getOrCompile3DModel(itemStack, armorSlot);
        if (model == null) return null;

        setupVisibleBodyParts(model, armorSlot);
        copyPoseFromEntity(model, entityLiving);
        return model;
    }

    @SideOnly(Side.CLIENT)
    private VoxelArmorRenderer getOrCompile3DModel(ItemStack itemStack, int armorSlot) {
        ArmorState state = ArmorState.load(itemStack);

        ArmorComponent baseArmor = getBaseComponent(state);
        if (baseArmor == null) return null;

        List<ArmorComponent> overlays = getOverlayComponents(state);
        float scale = (armorSlot == REGISTER_LEGS) ? 0.25F : 0.5F;

        return ArmorRegistry.getOrCompileModel(baseArmor, overlays, scale);
    }

    @SideOnly(Side.CLIENT)
    private static void setupVisibleBodyParts(ModelBiped model, int armorSlot) {
        model.bipedHead.showModel = (armorSlot == REGISTER_HELMET);
        model.bipedBody.showModel = (armorSlot == REGISTER_CHEST || armorSlot == REGISTER_LEGS);
        model.bipedLeftArm.showModel = (armorSlot == REGISTER_CHEST);
        model.bipedRightArm.showModel = (armorSlot == REGISTER_CHEST);
        model.bipedLeftLeg.showModel = (armorSlot == REGISTER_LEGS || armorSlot == REGISTER_BOOTS);
        model.bipedRightLeg.showModel = (armorSlot == REGISTER_LEGS || armorSlot == REGISTER_BOOTS);
    }

    @SideOnly(Side.CLIENT)
    private static void copyPoseFromEntity(ModelBiped model, EntityLivingBase entityLiving) {
        if (entityLiving == null) return;

        model.isSneak = entityLiving.isSneaking();
        model.isRiding = entityLiving.isRiding();
        model.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 : 0;
        model.aimedBow = entityLiving instanceof EntityPlayer player && player.getItemInUse() != null
            && player.getItemInUse()
                .getItem() instanceof ItemBow;
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
        ArmorContext context = load(player, armor);
        if (context.isBehaviorActive(BehaviorName.ForceField) && context.drainEnergy(100000 * damage)) {
            context.save();
            return new ArmorProperties(0, 100, Integer.MAX_VALUE);
        }

        if (source.isUnblockable()) return new ArmorProperties(0, 0, 0);

        if (context.hasBehavior(BehaviorName.InfiniteEnergy)) {
            return new ArmorProperties(0, getDamageReduction(armor), Integer.MAX_VALUE);
        }

        int max = context.getArmorState().charge < damage * 100 ? 0 : Integer.MAX_VALUE;
        return new ArmorProperties(0, getDamageReduction(armor), max);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) (20 * getDamageReduction(armor));
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        ArmorContext context = load(entity, stack);

        if (context.drainEnergy(damage * 100)) context.save();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRender(ItemStack stack) {
        ArmorContext context = load(null, stack);
        return context.isBehaviorActive(BehaviorName.HoloInventory);
    }

    // Thaumcraft compat

    @Override
    public boolean showIngamePopups(ItemStack armor, EntityLivingBase entity) {
        ArmorContext context = load(entity, armor);

        return context.isBehaviorActive(BehaviorName.GogglesOfRevealing);
    }

    @Override
    public boolean showNodes(ItemStack armor, EntityLivingBase entity) {
        ArmorContext context = load(entity, armor);

        return context.isBehaviorActive(BehaviorName.GogglesOfRevealing);
    }

    @Override
    public int getVisDiscount(ItemStack armor, EntityPlayer player, Aspect aspect) {
        ArmorContext context = load(player.getEntityWorld(), player, armor);

        return context.getArmorState().visDiscount;
    }

    // Forestry apiarist compat
    @Override
    public boolean protectEntity(EntityLivingBase entity, ItemStack armor, String cause, boolean doProtect) {
        ArmorContext context = load(entity, armor);
        return context.hasBehavior(BehaviorName.Apiarist);
    }

    @Override
    public boolean protectPlayer(EntityPlayer player, ItemStack armor, String cause, boolean doProtect) {
        return protectEntity(player, armor, cause, doProtect);
    }

    @Override
    public int getProtectionCount(EntityLivingBase entity, ItemStack armor, String cause) {
        return 4;
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

    // Botania Compat
    @Override
    public float getDiscount(ItemStack stack, int slot, EntityPlayer player) {
        ArmorContext context = load(player.getEntityWorld(), player, stack);

        return context.getArmorState().manaDiscount;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

}
