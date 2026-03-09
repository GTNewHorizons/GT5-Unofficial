package kubatech.loaders.item.arcfurnace;

import static kubatech.kubatech.KT;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import kubatech.loaders.ArcFurnaceElectrode;

public class ElectrodeItem extends Item {

    private static final String DURABILITY_TAG = "durability";
    private IIcon electrodeIcon;

    public ElectrodeItem() {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setUnlocalizedName("arc_furnace_electrode");
        setCreativeTab(KT);
    }

    public ItemStack getElectrodeStack(ArcFurnaceElectrode electrode) {
        return new ItemStack(this, 1, electrode.id);
    }

    public ArcFurnaceElectrode getElectrodeFromStack(ItemStack stack) {
        if (stack == null || stack.getItem() != this) return null;
        int meta = stack.getItemDamage();
        return ArcFurnaceElectrode.getById(meta);
    }

    public int usedDurability(ItemStack stack) {
        if (stack.stackTagCompound == null) return 0;
        return stack.stackTagCompound.getInteger(DURABILITY_TAG);
    }

    public int remainingDurability(ItemStack stack) {
        ArcFurnaceElectrode electrode = getElectrodeFromStack(stack);
        if (electrode == null) return 0;
        return electrode.durability - usedDurability(stack);
    }

    // returns true if the electrode is fully used up and should be removed
    public boolean damageElectrode(ItemStack stack, int damage) {
        ArcFurnaceElectrode electrode = getElectrodeFromStack(stack);
        if (electrode == null) return false;
        int used = usedDurability(stack) + damage;
        if (used >= electrode.durability) {
            return true;
        } else {
            if (stack.stackTagCompound == null) {
                stack.stackTagCompound = new NBTTagCompound();
            }
            stack.stackTagCompound.setInteger(DURABILITY_TAG, used);
            return false;
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        electrodeIcon = register.registerIcon("kubatech:arcfurnace/electrode");
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return electrodeIcon;
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
        for (ArcFurnaceElectrode value : ArcFurnaceElectrode.values()) {
            p_150895_3_.add(new ItemStack(this, 1, value.id));
        }
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List<String> p_77624_3_,
        boolean p_77624_4_) {
        int id = p_77624_1_.getItemDamage();
        ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getById(id);
        if (electrode != null) {
            p_77624_3_.add("Durability: " + remainingDurability(p_77624_1_) + " / " + electrode.durability);
            electrode.addInformation(p_77624_3_);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int id = stack.getItemDamage();
        ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getById(id);
        if (electrode != null) {
            return electrode.getUnlocalizedName();
        }
        return super.getUnlocalizedName(stack);
    }
}
