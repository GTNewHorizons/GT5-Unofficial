package gtPlusPlus.core.item.chemistry;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.util.ColorUtil;
import gregtech.api.util.StringUtils;
import gtPlusPlus.core.item.base.misc.BaseItemParticle;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.material.Particle.ElementaryGroup;

public class StandardBaseParticles extends BaseItemParticle {

    public static HashMap<String, Integer> NameToMetaMap = new HashMap<>();
    public static HashMap<Integer, String> MetaToNameMap = new HashMap<>();

    public StandardBaseParticles() {
        super("Base", aTypes.length, EnumRarity.rare);
    }

    private static final String[] aTypes = new String[] { "Graviton", "Up", "Down", "Charm", "Strange", "Top", "Bottom",
        "Electron", "Electron Neutrino", "Muon", "Muon Neutrino", "Tau", "Tau Neutrino", "Gluon", "Photon", "Z Boson",
        "W Boson", "Higgs Boson", "Proton", "Neutron", "Lambda", "Omega", "Pion", "ETA Meson", "Unknown" };

    public IIcon[] icons = new IIcon[aTypes.length];

    static {
        // Generate Ions
        int key = 0;

        for (String s : aTypes) {
            // Map names to Meta
            NameToMetaMap.put(StringUtils.sanitizeString(s.toLowerCase()), key);
            MetaToNameMap.put(key, StringUtils.sanitizeString(s.toLowerCase()));
            for (Particle o : Particle.aMap) {
                int aColour = 0;
                if (o.mParticleName.equalsIgnoreCase(s)) {
                    if (o.mParticleType == ElementaryGroup.BARYON) {
                        aColour = ColorUtil.toRGB(174, 226, 156);
                        aColourMap.put(key++, aColour);
                    } else if (o.mParticleType == ElementaryGroup.BOSON) {
                        if (o == Particle.HIGGS_BOSON) {
                            aColour = ColorUtil.toRGB(226, 196, 104);
                            aColourMap.put(key++, aColour);
                        } else {
                            aColour = ColorUtil.toRGB(226, 52, 66);
                            aColourMap.put(key++, aColour);
                        }
                    } else if (o.mParticleType == ElementaryGroup.LEPTON) {
                        aColour = ColorUtil.toRGB(126, 226, 95);
                        aColourMap.put(key++, aColour);
                    } else if (o.mParticleType == ElementaryGroup.MESON) {
                        aColour = ColorUtil.toRGB(90, 154, 226);
                        aColourMap.put(key++, aColour);
                    } else {
                        aColour = ColorUtil.toRGB(188, 61, 226);
                        aColourMap.put(key++, aColour);
                    }
                }
            }
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
        return "item.particle.base" + "." + aTypes[itemStack.getItemDamage()];
    }

    public static Particle getParticle(ItemStack aStack) {
        ArrayList<Particle> g = Particle.aMap;
        for (Particle p : g) {
            String aPartName = StringUtils.sanitizeString(p.mParticleName.toLowerCase());
            String expectedPart = StringUtils.sanitizeString(aTypes[aStack.getItemDamage()].toLowerCase());
            if (aPartName.equals(expectedPart)) {
                return p;
            }
        }
        return Particle.UNKNOWN;
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int colorRGB) {
        // return ColorUtil.toRGB(200, 200, 200);
        return super.getColorFromParentClass(stack, colorRGB);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        Particle aCharge = getParticle(stack);
        EnumChatFormatting aColour = EnumChatFormatting.GRAY;
        if (aCharge != null) {
            String aGroup = aCharge.mParticleType.name()
                .toLowerCase();
            if (aGroup.toLowerCase()
                .contains("quark")) {
                aColour = EnumChatFormatting.LIGHT_PURPLE;
            } else if (aGroup.toLowerCase()
                .contains("lepton")) {
                    aColour = EnumChatFormatting.GREEN;
                } else if (aCharge == Particle.HIGGS_BOSON) {
                    aColour = EnumChatFormatting.YELLOW;
                } else if (aGroup.toLowerCase()
                    .contains("boson")) {
                        aColour = EnumChatFormatting.RED;
                    } else if (aGroup.toLowerCase()
                        .contains("baryon")) {
                            aColour = EnumChatFormatting.BLUE;
                        } else if (aGroup.toLowerCase()
                            .contains("meson")) {
                                aColour = EnumChatFormatting.WHITE;
                            }
            String aGroupKey = "gtpp.tooltip.base_particles.type." + aGroup;
            String aState = aColour + StatCollector.translateToLocal(aGroupKey) + EnumChatFormatting.RESET;
            list.add(
                EnumChatFormatting.GRAY
                    + StatCollector.translateToLocalFormatted("gtpp.tooltip.base_particles.type", aState));
        }
        super.addInformation(stack, player, list, bool);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = reg.registerIcon(GTPlusPlus.ID + ":particle/" + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }
}
