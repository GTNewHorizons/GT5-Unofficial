package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.ArrayList;
import java.util.List;

import com.gtnewhorizon.gtnhlib.keybind.IKeyPressedListener;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MechArmorBase extends ItemArmor implements IKeyPressedListener {

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
        tag.setInteger("core", 0);
        tag.setString("frame", "None");
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
            if (behavior.getListenedKeys().contains(keyPressed)) {
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
            if (tag.hasKey("core")) {
                aList.add("Installed Core: " + tag.getInteger("core"));
            }
            if (tag.hasKey("frame")) {
                aList.add("Frame: " + tag.getString("frame"));
            }
            // todo armor toughness tooltip
            for (IArmorBehavior behavior : behaviors) {
                behavior.addInformation(aStack, aList);
            }
        }
    }
}
