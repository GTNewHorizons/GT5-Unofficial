package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.items.armor.ArmorHelper.APIARIST_KEY;
import static gregtech.api.items.armor.ArmorHelper.FORCE_FIELD_KEY;
import static gregtech.api.items.armor.ArmorHelper.GOGGLES_OF_REVEALING_KEY;
import static gregtech.api.items.armor.ArmorHelper.INFINITE_ENERGY_KEY;
import static gregtech.api.items.armor.ArmorHelper.JETPACK_KEY;
import static gregtech.api.items.armor.ArmorHelper.SLOT_CHEST;
import static gregtech.api.items.armor.ArmorHelper.SLOT_LEGS;
import static gregtech.api.items.armor.ArmorHelper.VIS_DISCOUNT_KEY;
import static gregtech.api.items.armor.ArmorHelper.drainArmor;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.coresMap;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.framesMap;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.IKeyPressedListener;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IArmorApiarist;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Cores;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Frames;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.util.GTUtility;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;

@Optional.InterfaceList(
    value = { @Optional.Interface(iface = "forestry.api.apiculture.IArmorApiarist", modid = Mods.ModIDs.FORESTRY),
        @Optional.Interface(iface = "thaumcraft.api.IVisDiscountGear", modid = Mods.ModIDs.THAUMCRAFT),
        @Optional.Interface(iface = "thaumcraft.api.IGoggles", modid = Mods.ModIDs.THAUMCRAFT),
        @Optional.Interface(iface = "thaumcraft.api.nodes.IRevealer", modid = Mods.ModIDs.THAUMCRAFT), })

public class MechArmorBase extends ItemArmor implements IKeyPressedListener, ISpecialArmor, IElectricItem, IGoggles,
    IRevealer, IVisDiscountGear, IArmorApiarist {

    protected IIcon coreIcon;
    protected IIcon frameIcon;

    protected String type;

    public static final String MECH_FRAME_KEY = "frame";
    public static final String MECH_CORE_KEY = "core";

    protected List<IArmorBehavior> behaviors = new ArrayList<>();

    private int slot;

    public MechArmorBase(int slot, String type, int register) {
        super(ArmorMaterial.IRON, 2, register);
        this.slot = slot;
        this.type = type;
    }

    public void addBehavior(IArmorBehavior behavior) {
        behaviors.add(behavior);
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

    public List<IArmorBehavior> getBehaviors() {
        return behaviors;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        for (IArmorBehavior behavior : behaviors) {
            behavior.onArmorTick(world, player, itemStack);
        }
    }

    public void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        for (IArmorBehavior behavior : behaviors) {
            if (player instanceof EntityPlayerMP playerMP) {
                for (SyncedKeybind keyBind : behavior.getListenedKeys()) {
                    keyBind.removePlayerListener(playerMP, this);
                }
            }
            behavior.onArmorUnequip(world, player, stack);
        }
    }

    public void onArmorEquip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        boolean initMessage = false;
        for (IArmorBehavior behavior : behaviors) {
            if (player instanceof EntityPlayerMP playerMP) {
                for (SyncedKeybind keyBind : behavior.getListenedKeys()) {
                    keyBind.registerPlayerListener(playerMP, this);
                    if (getOrCreateNbtCompound(stack).hasKey(behavior.getMainNBTTag())) {
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
            behavior.onArmorEquip(world, player, stack);
        }
    }

    @Override
    public void onKeyPressed(EntityPlayerMP player, SyncedKeybind keyPressed, boolean isDown) {
        for (IArmorBehavior behavior : behaviors) {
            if (behavior.getListenedKeys()
                .contains(keyPressed)) {
                behavior.onKeyPressed(player.getCurrentArmor(slot), player, keyPressed, isDown);
            }
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
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        NBTTagCompound tag = aStack.getTagCompound();
        if (tag != null) {
            if (tag.hasKey(MECH_FRAME_KEY)) {
                aList.add("Armor Frame: " + tag.getString(MECH_FRAME_KEY));
            }
            if (tag.hasKey(MECH_CORE_KEY)) {
                aList.add("Energy Core: " + tag.getString(MECH_CORE_KEY));
            }
            for (IArmorBehavior behavior : behaviors) {
                behavior.addInformation(aStack, aList);
            }
        }
        if (Loader.isModLoaded(Thaumcraft.ID)) {
            int visDiscount = this.getVisDiscount(aStack, aPlayer, (Aspect) null);
            if (visDiscount > 0) {
                aList.add("");
                aList.add(
                    EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount")
                        + ": "
                        + visDiscount
                        + "%");
            }
        }
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

        model.jettank1.showModel = (armorSlot == SLOT_CHEST && itemStack.getTagCompound()
            .hasKey(JETPACK_KEY));

        Cores core = getCore(itemStack);
        if (core != null) {
            switch (core.tier) {
                case 1 -> model.core1.showModel = true;
                case 2 -> model.core2.showModel = true;
                case 3 -> model.core3.showModel = true;
                case 4 -> model.core4.showModel = true;
            }
        }
        return model;
    }

    protected Cores getCore(ItemStack stack) {
        String core = stack.getTagCompound()
            .getString(MECH_CORE_KEY);
        if (coresMap.containsKey(core)) {
            return (coresMap.get(core));
        }
        return null;
    }

    protected Frames getFrame(ItemStack stack) {
        String frame = stack.getTagCompound()
            .getString(MECH_FRAME_KEY);
        if (framesMap.containsKey(frame)) {
            return (framesMap.get(frame));
        }
        return null;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        Cores core = getCore(stack);
        if (core == null) return false;
        return (!stack.getTagCompound()
            .hasKey(INFINITE_ENERGY_KEY));
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
            return frame.protection * getProtectionShare();
        }
        return 0;
    }

    // Special armor settings

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
        int slot) {
        if (source.isUnblockable()) return new ArmorProperties(0, getDamageReduction(armor) / 100D, 15);
        if (armor.getTagCompound()
            .getBoolean(FORCE_FIELD_KEY) && drainArmor(armor, 100000 * damage))
            return new ArmorProperties(0, 100, Integer.MAX_VALUE);
        if (source.isDamageAbsolute() || source.isMagicDamage() || ElectricItem.manager.getCharge(armor) < damage * 100)
            return new ArmorProperties(0, getDamageReduction(armor) / 100D, 15);
        return new ArmorProperties(0, getDamageReduction(armor) / 24.5D, 1000);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) getDamageReduction(armor);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        drainArmor(stack, damage * 100);
    }

    // Thaumcraft compat

    @Override
    public boolean showIngamePopups(ItemStack item, EntityLivingBase var2) {
        return (getOrCreateNbtCompound(item).getBoolean(GOGGLES_OF_REVEALING_KEY));
    }

    @Override
    public boolean showNodes(ItemStack item, EntityLivingBase var2) {
        return (getOrCreateNbtCompound(item).getBoolean(GOGGLES_OF_REVEALING_KEY));
    }

    @Override
    public int getVisDiscount(ItemStack item, EntityPlayer var2, Aspect var3) {
        return (getOrCreateNbtCompound(item).getInteger(VIS_DISCOUNT_KEY));
    }

    // Forestry apiarist compat
    @Override
    public boolean protectEntity(EntityLivingBase entity, ItemStack armor, String cause, boolean doProtect) {
        return (getOrCreateNbtCompound(armor).getBoolean(APIARIST_KEY));
    }

    @Override
    public boolean protectPlayer(EntityPlayer player, ItemStack armor, String cause, boolean doProtect) {
        return (getOrCreateNbtCompound(armor).getBoolean(APIARIST_KEY));
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
        if (core != null) return getCore(itemStack).chargeMax;
        return 0;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        Cores core = getCore(itemStack);
        if (core != null) return getCore(itemStack).chargeTier;
        return -1;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        Cores core = getCore(itemStack);
        if (core != null) return GTValues.V[getCore(itemStack).chargeTier];
        return 0;
    }
}
