package gtPlusPlus.core.item.chemistry;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.base.misc.BaseItemParticle;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class IonParticles extends BaseItemParticle {

	public IonParticles() {
		super("Ion", aElements.length, EnumRarity.rare);
	}
	
	private static final String[] aElements = new String[]{"Hydrogen", "Helium", "Lithium", "Beryllium", "Boron", "Carbon", "Nitrogen", "Oxygen", "Fluorine", "Neon", "Sodium", "Magnesium", "Aluminum", "Silicon", "Phosphorus", "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium", "Scandium", "Titanium", "Vanadium", "Chromium", "Manganese", "Iron", "Cobalt", "Nickel", "Copper", "Zinc", "Gallium", "Germanium", "Arsenic", "Selenium", "Bromine", "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium", "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium", "Palladium", "Silver", "Cadmium", "Indium", "Tin", "Antimony", "Tellurium", "Iodine", "Xenon", "Cesium", "Barium", "Lanthanum", "Cerium", "Praseodymium", "Neodymium", "Promethium", "Samarium", "Europium", "Gadolinium", "Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium", "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium", "Osmium", "Iridium", "Platinum", "Gold", "Mercury", "Thallium", "Lead", "Bismuth", "Polonium", "Astatine", "Radon", "Francium", "Radium", "Actinium", "Thorium", "Protactinium", "Uranium", "Neptunium", "Plutonium", "Americium", "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium", "Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium", "Seaborgium", "Bohrium", "Hassium", "Meitnerium", "Darmstadtium", "Roentgenium", "Copernicium", "Nihonium", "Flerovium", "Moscovium", "Livermorium", "Tennessine", "Oganesson"};
	
	static {
		//Generate Ions
		int key = 0;
		for (String s : aElements) {
			Materials m = Materials.get(s);
			int aColour = 0;
			if (m == null) {
				aColour = Utils.rgbtoHexValue(128, 128, 128);
			}
			else {
				aColour = Utils.rgbtoHexValue(m.mRGBa[0], m.mRGBa[1], m.mRGBa[2]);
			}
			aColourMap.put(key++, aColour);
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
        return "item.particle.ion" + "." + aElements[itemStack.getItemDamage()];
    }

}
