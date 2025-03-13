package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.items.armor.ArmorHelper.GOGGLES_OF_REVEALING_KEY;
import static gregtech.api.items.armor.ArmorHelper.VIS_DISCOUNT_KEY;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.ArrayList;
import java.util.List;

import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.core.IC2;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.IKeyPressedListener;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;

public class MechArmorBase extends ItemArmor implements IKeyPressedListener, IElectricItem, IGoggles, IRevealer, IVisDiscountGear {

    protected IIcon coreIcon;
    protected IIcon frameIcon;

    static final int REGISTER_HELMET = 0;
    static final int REGISTER_CHEST = 1;
    static final int REGISTER_LEGS = 2;
    static final int REGISTER_BOOTS = 3;

    static final int SLOT_HELMET = 3;
    static final int SLOT_CHEST = 2;
    static final int SLOT_LEGS = 1;
    static final int SLOT_BOOTS = 0;

    protected String type;

    public static final String MECH_FRAME_KEY = "frame";
    public static final String MECH_CORE_KEY = "core";

    protected List<IArmorBehavior> behaviors = new ArrayList<>();

    public MechArmorBase(int slot) {
        super(ArmorMaterial.IRON, 2, slot);
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
        p_150895_3_.add(getStack());
    }

    public @NotNull ItemStack getStack() {
        ItemStack stack = new ItemStack(this);

        // Set behaviors
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        tag.setInteger(MECH_CORE_KEY, 0);
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
        for (IArmorBehavior behavior : behaviors) {
            if (player instanceof EntityPlayerMP playerMP) {
                for (SyncedKeybind keyBind : behavior.getListenedKeys()) {
                    keyBind.registerPlayerListener(playerMP, this);
                }
            }
            behavior.onArmorEquip(world, player, stack);
        }
    }

    @Override
    public void onKeyPressed(EntityPlayerMP player, SyncedKeybind keyPressed) {
        for (IArmorBehavior behavior : behaviors) {
            if (behavior.getListenedKeys()
                .contains(keyPressed)) {
                behavior.onKeyPressed(player.getCurrentArmor(getEquipmentSlot()), player, keyPressed);
            }
        }
    }

    protected int getEquipmentSlot() {
        return -1;
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

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        NBTTagCompound tag = aStack.getTagCompound();
        if (tag != null) {
            if (tag.hasKey(MECH_CORE_KEY)) {
                aList.add("Installed Core: " + tag.getInteger(MECH_CORE_KEY));
            }
            if (tag.hasKey(MECH_FRAME_KEY)) {
                aList.add("Frame: " + tag.getString(MECH_FRAME_KEY));
            }
            // todo armor toughness tooltip
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

    protected int getCore(ItemStack stack) {
        if (stack.getTagCompound().hasKey(MECH_CORE_KEY)) {
            return (stack.getTagCompound().getInteger(MECH_CORE_KEY));
        }
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getCore(stack) != 0;
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
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
        return 10000 * getCore(itemStack);
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 3;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 1600;
    }
}
