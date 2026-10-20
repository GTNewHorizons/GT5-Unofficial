package kubatech.loaders.item.arcfurnace;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.ruling_0.materiallib.api.ShapeItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kubatech.loaders.ArcFurnaceElectrode;

/// The `electrode` MaterialLib shape, consumed by the Industrial Arc Furnace. A stack's material selects its
/// [ArcFurnaceElectrode], and the durability it has used up is kept in its NBT.
public class ElectrodeItem extends ShapeItem {

    private static final String DURABILITY_TAG = "durability";

    public ElectrodeItem(String modid, String name, String... oreDicts) {
        super(modid, name, oreDicts);
        setMaxStackSize(1);
    }

    public static int usedDurability(ItemStack stack) {
        if (stack.stackTagCompound == null) return 0;
        return stack.stackTagCompound.getInteger(DURABILITY_TAG);
    }

    public static int remainingDurability(ItemStack stack) {
        ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getByStack(stack);
        if (electrode == null) return 0;
        return electrode.durability - usedDurability(stack);
    }

    /// Uses up `damage` durability of `stack`. Returns true when the electrode is spent and should be removed.
    public static boolean damageElectrode(ItemStack stack, int damage) {
        ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getByStack(stack);
        if (electrode == null) return false;
        int used = Math.max(0, usedDurability(stack) + damage);
        if (used >= electrode.durability) return true;
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        stack.stackTagCompound.setInteger(DURABILITY_TAG, used);
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean advanced) {
        super.addInformation(stack, player, lines, advanced);
        ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getByStack(stack);
        if (electrode != null) {
            electrode.addInformation(lines, remainingDurability(stack));
        }
    }
}
