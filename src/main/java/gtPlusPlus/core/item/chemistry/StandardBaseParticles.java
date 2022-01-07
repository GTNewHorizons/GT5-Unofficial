package gtPlusPlus.core.item.chemistry;

import java.util.HashMap;
import java.util.List;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.base.misc.BaseItemParticle;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.material.Particle.ElementaryGroup;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class StandardBaseParticles extends BaseItemParticle {

	public static HashMap<String, Integer> NameToMetaMap = new HashMap<String, Integer>();
	public static HashMap<Integer, String> MetaToNameMap = new HashMap<Integer, String>();
	
	public StandardBaseParticles() {
		super("Base", aTypes.length, EnumRarity.rare);
	}
	
	private static final String[] aTypes = new String[] { "Graviton", "Up", "Down", "Charm", "Strange", "Top",
			"Bottom", "Electron", "Electron Neutrino", "Muon", "Muon Neutrino", "Tau", "Tau Neutrino", "Gluon",
			"Photon", "Z Boson", "W Boson", "Higgs Boson", "Proton", "Neutron", "Lambda", "Omega", "Pion",
			"ETA Meson", "Unknown" };
	
	public IIcon[] icons = new IIcon[aTypes.length];
	
	static {
		//Generate Ions
		int key = 0;
		
		
		for (String s : aTypes) {
			//Map names to Meta
			NameToMetaMap.put(Utils.sanitizeString(s.toLowerCase()), key);
			MetaToNameMap.put(key, Utils.sanitizeString(s.toLowerCase()));
			for (Particle o : Particle.aMap) {
				int aColour = 0;
				if (o.mParticleName.toLowerCase().equals(s.toLowerCase())) {
					if (o.mParticleType == ElementaryGroup.BARYON) {
						aColour = Utils.rgbtoHexValue(174, 226, 156);			
						aColourMap.put(key++, aColour);
					}
					else if (o.mParticleType == ElementaryGroup.BOSON) {
						if (o == Particle.HIGGS_BOSON) {
							aColour = Utils.rgbtoHexValue(226, 196, 104);			
							aColourMap.put(key++, aColour);
						}
						else {
							aColour = Utils.rgbtoHexValue(226, 52, 66);			
							aColourMap.put(key++, aColour);
						}						
					}
					else if (o.mParticleType == ElementaryGroup.LEPTON) {
						aColour = Utils.rgbtoHexValue(126, 226, 95);			
						aColourMap.put(key++, aColour);
					}
					else if (o.mParticleType == ElementaryGroup.MESON) {
						aColour = Utils.rgbtoHexValue(90, 154, 226);			
						aColourMap.put(key++, aColour);
					}
					else {
						aColour = Utils.rgbtoHexValue(188, 61, 226);			
						aColourMap.put(key++, aColour);
					}
				}
			}
		}
		
	}

	@Override
	public String[] getAffixes() {
		return new String[] {"", ""};
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
		AutoMap<Particle> g = Particle.aMap;
		for (Particle p : g) {
			String aPartName = Utils.sanitizeString(p.mParticleName.toLowerCase());
			String expectedPart = Utils.sanitizeString(aTypes[aStack.getItemDamage()].toLowerCase());
			if (aPartName.equals(expectedPart)) {
				return p;
			}
		}
		return Particle.UNKNOWN;	
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		//return Utils.rgbtoHexValue(200, 200, 200);
		return super.getColorFromParentClass(stack, HEX_OxFFFFFF);
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {		
		Particle aCharge = getParticle(stack);
		EnumChatFormatting aColour = EnumChatFormatting.GRAY;
		String aState = aColour+"Unknown"+EnumChatFormatting.RESET;			
		if (aCharge != null) {
			String aGroup = aCharge.mParticleType.name().toLowerCase();
			if (aGroup.toLowerCase().contains("quark")) {
				aColour = EnumChatFormatting.LIGHT_PURPLE;
			}
			else if (aGroup.toLowerCase().contains("lepton")) {
				aColour = EnumChatFormatting.GREEN;
			}
			else if (aCharge == Particle.HIGGS_BOSON) {
				aColour = EnumChatFormatting.YELLOW;
			}
			else if (aGroup.toLowerCase().contains("boson")) {
				aColour = EnumChatFormatting.RED;
			}
			else if (aGroup.toLowerCase().contains("baryon")) {
				aColour = EnumChatFormatting.BLUE;
			}
			else if (aGroup.toLowerCase().contains("meson")) {
				aColour = EnumChatFormatting.WHITE;
			}
			else {
				aColour = EnumChatFormatting.GRAY;
			}			
			String aFirstLet = aGroup.substring(0, 1).toUpperCase();
			aGroup = aGroup.replaceFirst(aGroup.substring(0, 1), aFirstLet);
			aState = aColour+aGroup+EnumChatFormatting.RESET;
			list.add(EnumChatFormatting.GRAY + "Type: "+aState);
		}		
		super.addInformation(stack, player, list, bool);
	}

	@Override
	public void registerIcons(IIconRegister reg) {		
		for (int i = 0; i < this.icons.length; i++) {
			this.icons[i] = reg.registerIcon(CORE.MODID + ":" + "particle/new/"+i);			
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.icons[meta];
	}

}
