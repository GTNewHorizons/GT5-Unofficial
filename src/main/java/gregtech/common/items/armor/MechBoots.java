package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MechBoots extends MechArmorBase {

    public MechBoots() {
        super(SLOT_BOOTS);
        type = "boots";
    }
}
