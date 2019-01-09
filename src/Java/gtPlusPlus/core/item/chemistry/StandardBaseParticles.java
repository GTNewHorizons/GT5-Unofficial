package gtPlusPlus.core.item.chemistry;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.base.misc.BaseItemParticle;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.material.Particle.ElementaryGroup;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class StandardBaseParticles extends BaseItemParticle {

	public StandardBaseParticles() {
		super("Base", aTypes.length, EnumRarity.rare);
	}
	
	private static final String[] aTypes = new String[] { "Graviton", "Up", "Down", "Charm", "Strange", "Top",
			"Bottom", "Electron", "Electron Neutrino", "Muon", "Muon Neutrino", "Tau", "Tau Neutrino", "Gluon",
			"Photon", "Z Boson", "W Boson", "Higgs Boson", "Proton", "Neutron", "Lambda", "Omega", "Pion",
			"ETA Meson", };
	
	static {
		//Generate Ions
		int key = 0;
		for (String s : aTypes) {
			Particle p;
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

}
