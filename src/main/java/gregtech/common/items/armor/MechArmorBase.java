package gregtech.common.items.armor;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import static gregtech.api.enums.Mods.GregTech;

public class MechArmorBase extends ItemArmor {

    protected IIcon coreIcon;
    protected IIcon frameIcon;

    static final int SLOT_HELMET = 0;
    static final int SLOT_CHEST = 1;
    static final int SLOT_LEGS = 2;
    static final int SLOT_BOOTS = 3;

    protected String type;

    public MechArmorBase(int slot) {
        super(ArmorMaterial.IRON, 2, slot);
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
        }
    }
}
