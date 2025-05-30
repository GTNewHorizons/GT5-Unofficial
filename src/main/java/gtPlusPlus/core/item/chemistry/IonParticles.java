package gtPlusPlus.core.item.chemistry;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.base.misc.BaseItemParticle;
import gtPlusPlus.core.util.Utils;

public class IonParticles extends BaseItemParticle {

    public static HashMap<String, Integer> NameToMetaMap = new HashMap<>();
    public static HashMap<Integer, String> MetaToNameMap = new HashMap<>();

    public IonParticles() {
        super("Ion", ions.length, EnumRarity.rare);
    }

    public static final String[] ions = new String[] { "Hydrogen", "Helium" };

    public IIcon[] texture = new IIcon[ions.length];

    static {
        // Generate Ions
        int key = 0;
        for (String s : ions) {
            // Map names to Meta
            NameToMetaMap.put(Utils.sanitizeString(s.toLowerCase()), key);
            MetaToNameMap.put(key, Utils.sanitizeString(s.toLowerCase()));
            Materials m = Materials.get(s);
            int aColour = 0;
            aColour = Utils.rgbtoHexValue(m.mRGBa[0], m.mRGBa[1], m.mRGBa[2]);
            aColourMap.put(key++, aColour);
        }
    }

    @Override
    public String[] getAffixes() {
        return new String[] { "", "" };
    }

    @Override
    public String getUnlocalizedName() {
        return "";
    }

    @Override
    public String getUnlocalizedName(final ItemStack itemStack) {
        return "item.particle.ion." + ions[itemStack.getItemDamage()];
    }

    private static boolean createNBT(ItemStack rStack) {
        final NBTTagCompound tagMain = new NBTTagCompound();
        final NBTTagCompound tagNBT = new NBTTagCompound();
        tagNBT.setLong("Charge", 0);
        tagMain.setTag("Ion", tagNBT);
        rStack.setTagCompound(tagMain);
        return true;
    }

    public static long getChargeState(final ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("Ion");
            if (aNBT != null) {
                return aNBT.getLong("Charge");
            }
        } else {
            createNBT(aStack);
        }
        return 0L;
    }

    public static boolean setChargeState(final ItemStack aStack, final long aCharge) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("Ion");
            if (aNBT != null) {
                aNBT.setLong("Charge", aCharge);
                return true;
            }
        }
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            createNBT(stack);
        }
        return (double) getChargeState(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        long aCharge = getChargeState(stack);
        String ionName = MetaToNameMap.get(stack.getItemDamage());
        // State not set
        if (aCharge == 0) {
            list.add(
                EnumChatFormatting.GRAY
                    + StatCollector.translateToLocalFormatted("gtpp.tooltip.ion_particles.state_not_set", ionName));
        } else {
            if (aCharge > 0) {
                list.add(
                    EnumChatFormatting.GRAY + StatCollector
                        .translateToLocalFormatted("gtpp.tooltip.ion_particles.charge_positive", ionName, aCharge));
            } else {
                list.add(
                    EnumChatFormatting.GRAY + StatCollector
                        .translateToLocalFormatted("gtpp.tooltip.ion_particles.charge_negative", ionName, aCharge));
            }
        }
        super.addInformation(stack, player, list, bool);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        for (int i = 0; i < this.texture.length; i++) {
            this.texture[i] = reg.registerIcon(GTPlusPlus.ID + ":ion" + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.texture[meta];
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int aMeta, int aPass) {
        return this.texture[aMeta];
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 2;
    }
}
