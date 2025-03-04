package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.texture.IIconRegister;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MechChestplate extends MechArmorBase {

    public MechChestplate() {
        super(SLOT_CHEST);
        type = "chestplate";
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag != null) {
            player.capabilities.allowFlying = tag.getInteger("core") >= 3;
        }
        super.onArmorTick(world, player, itemStack);
    }
}
