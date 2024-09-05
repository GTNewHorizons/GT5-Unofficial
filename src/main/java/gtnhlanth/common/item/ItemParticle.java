package gtnhlanth.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtnhlanth.Tags;
import gtnhlanth.common.beamline.Particle;

public class ItemParticle extends Item {

    public static final int NUMBER_OF_SUBTYPES = Particle.values().length;

    private static String[] names = new String[NUMBER_OF_SUBTYPES];

    static {
        populateNamesArray();
    }

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public ItemParticle() {

        this.setHasSubtypes(true);
        this.setMaxDamage(0);

    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        int j = MathHelper.clamp_int(damage, 0, NUMBER_OF_SUBTYPES - 1);
        return this.iconArray[j];
    }

    public String getUnlocalizedName(ItemStack stack) {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, NUMBER_OF_SUBTYPES - 1);
        return super.getUnlocalizedName() + "." + names[i];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < NUMBER_OF_SUBTYPES; ++i) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.iconArray = new IIcon[NUMBER_OF_SUBTYPES];

        for (int i = 0; i < NUMBER_OF_SUBTYPES; ++i) {
            this.iconArray[i] = register.registerIcon(Tags.MODID + ":" + "particle/" + names[i]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, NUMBER_OF_SUBTYPES - 1);

        Particle particle = Particle.values()[i];

        return particle.getLocalisedName();

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {

        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, NUMBER_OF_SUBTYPES - 1);

        Particle particle = Particle.values()[i];

        float restMass = particle.getMass();

        float charge = particle.getCharge();

        String chargeSpecial = particle.getChargeSpecial();

        String chargeStringToAppend;
        if (chargeSpecial != null) {

            chargeStringToAppend = chargeSpecial;

        } else {

            if (charge > 0) chargeStringToAppend = "+" + charge;
            else chargeStringToAppend = "" + charge;
        }

        list.add("Rest Mass: " + restMass + " MeV");
        list.add("Charge: " + chargeStringToAppend + "e");

    }

    private static void populateNamesArray() {

        for (int i = 0; i < NUMBER_OF_SUBTYPES; i++) {

            Particle particle = Particle.values()[i];

            names[i] = particle.getName();

        }

    }

}
