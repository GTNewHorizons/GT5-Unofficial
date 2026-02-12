package kubatech.loaders.item.arcfurnace;

import static kubatech.kubatech.KT;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import kubatech.loaders.ArcFurnaceElectrode;

public class ElectrodeItem extends Item {

    private static final int ELECTRODE_SET_OFFSET = 1000;
    private IIcon electrodeIcon;
    private IIcon electrodeSetIcon;

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

    public ItemStack getElectrodeSetStack(ArcFurnaceElectrode electrode) {
        return new ItemStack(this, 1, electrode.id + ELECTRODE_SET_OFFSET);
    }

    boolean isElectrodeSet(int meta) {
        return meta >= ELECTRODE_SET_OFFSET;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        electrodeIcon = register.registerIcon("kubatech:arcfurnace/electrode");
        electrodeSetIcon = register.registerIcon("kubatech:arcfurnace/electrodeSet");
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        if (isElectrodeSet(meta)) return electrodeSetIcon;
        else return electrodeIcon;
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
        for (ArcFurnaceElectrode value : ArcFurnaceElectrode.values()) {
            p_150895_3_.add(new ItemStack(this, 1, value.id + ELECTRODE_SET_OFFSET));
            p_150895_3_.add(new ItemStack(this, 1, value.id));
        }
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List<String> p_77624_3_,
        boolean p_77624_4_) {
        int id = p_77624_1_.getItemDamage();
        if (isElectrodeSet(id)) {
            ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getById(id - ELECTRODE_SET_OFFSET);
            if (electrode != null) {
                electrode.addInformation(p_77624_3_);
            }
        } else {
            ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getById(id);
            if (electrode != null) {
                electrode.addInformation(p_77624_3_);
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int id = stack.getItemDamage();
        if (isElectrodeSet(id)) {
            ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getById(id - ELECTRODE_SET_OFFSET);
            if (electrode != null) {
                return electrode.getSetUnlocalizedName();
            }
        } else {
            ArcFurnaceElectrode electrode = ArcFurnaceElectrode.getById(id);
            if (electrode != null) {
                return electrode.getUnlocalizedName();
            }
        }
        return super.getUnlocalizedName(stack);
    }
}
