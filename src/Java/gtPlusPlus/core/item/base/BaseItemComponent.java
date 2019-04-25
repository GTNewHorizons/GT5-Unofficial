package gtPlusPlus.core.item.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BaseItemComponent extends Item{

	private final static Class<TextureSet> mTextureSetPreload;

	static {
		mTextureSetPreload = TextureSet.class;
	}

	public final Material componentMaterial;
	public final String materialName;
	public final String unlocalName;
	public final ComponentTypes componentType;
	public final int componentColour;
	public Object extraData;

	protected IIcon base;
	protected IIcon overlay;

	public BaseItemComponent(final Material material, final ComponentTypes componentType) {
		this.componentMaterial = material;
		this.unlocalName = "item"+componentType.COMPONENT_NAME+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.componentType = componentType;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(this.unlocalName);
		this.setMaxStackSize(64);
		//this.setTextureName(this.getCorrectTextures());
		this.componentColour = material.getRgbAsHex();
		GameRegistry.registerItem(this, this.unlocalName);

		//if (componentType != ComponentTypes.DUST)

		GT_OreDictUnificator.registerOre(componentType.getOreDictName()+material.getUnlocalizedName(), ItemUtils.getSimpleStack(this));
		if (LoadedMods.Thaumcraft) {
			//ThaumcraftUtils.addAspectToItem(ItemUtils.getSimpleStack(this), TC_Aspect_Wrapper.generate(TC_Aspects.METALLUM.mAspect), 1);
			if (componentMaterial.isRadioactive) {
				//ThaumcraftUtils.addAspectToItem(ItemUtils.getSimpleStack(this), TC_Aspect_Wrapper.generate(TC_Aspects.RADIO.mAspect), componentMaterial.vRadiationLevel);				
			}
		}
		registerComponent();		
	}

	//For Cell Generation
	public BaseItemComponent(final String unlocalName, final String localName, final short[] RGBA) {
		
		// Handles .'s from fluid internal names.
		String aFormattedNameForFluids;
		if (unlocalName.contains(".")) {			
			aFormattedNameForFluids = StringUtils.splitAndUppercase(unlocalName, ".");						
		}
		else {
			aFormattedNameForFluids = unlocalName;
		}		
		 	
		this.componentMaterial = null;
		this.unlocalName = "itemCell"+aFormattedNameForFluids;
		this.materialName = localName;
		this.componentType = ComponentTypes.CELL;
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(aFormattedNameForFluids);
		this.setMaxStackSize(64);
		this.componentColour = MathUtils.getRgbAsHex(RGBA);
		this.extraData = RGBA;
		this.setTextureName(CORE.MODID + ":" + "item"+ComponentTypes.CELL.COMPONENT_NAME);
		GameRegistry.registerItem(this, aFormattedNameForFluids);
		GT_OreDictUnificator.registerOre(ComponentTypes.CELL.getOreDictName()+aFormattedNameForFluids, ItemUtils.getSimpleStack(this));
		registerComponent();
	}

	public boolean registerComponent() {		
		if (this.componentMaterial == null) {
			return false;
		}		
		//Register Component
		Map<String, ItemStack> aMap = Material.mComponentMap.get(componentMaterial.getUnlocalizedName());
		if (aMap == null) {
			aMap = new HashMap<String, ItemStack>();
		}
		String aKey = componentType.getGtOrePrefix().name();
		ItemStack x = aMap.get(aKey);
		if (x == null) {
			aMap.put(aKey, ItemUtils.getSimpleStack(this));
			Logger.MATERIALS("Registering a material component. Item: ["+componentMaterial.getUnlocalizedName()+"] Map: ["+aKey+"]");
			Material.mComponentMap.put(componentMaterial.getUnlocalizedName(), aMap);
			return true;
		}
		else {
			//Bad
			Logger.MATERIALS("Tried to double register a material component. ");
			return false;
		}
	}

	public String getCorrectTextures(){
		if (!CORE.ConfigSwitches.useGregtechTextures){
			return CORE.MODID + ":" + "item"+this.componentType.COMPONENT_NAME;
		}
		String metType = "9j4852jyo3rjmh3owlhw9oe"; 
		if (this.componentMaterial != null) {
			TextureSet u = this.componentMaterial.getTextureSet();
			if (u != null) {
				metType = u.mSetName;				
			}
		}		
		metType = (metType.equals("9j4852jyo3rjmh3owlhw9oe") ? "METALLIC" : metType);		
		return "gregtech" + ":" + "materialicons/"+metType+"/" + this.componentType.getOreDictName();



		//return "gregtech" + ":" + "materialicons/"+metType+"/" + this.componentType.COMPONENT_NAME.toLowerCase();
	}

	/*@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		if (this.componentType == ComponentTypes.SMALLGEAR){
			return "Small " + this.materialName+" Gear";
		}

		if (this.componentMaterial != null) {
			return (this.componentMaterial.getLocalizedName()+this.componentType.DISPLAY_NAME);
		}
		return this.materialName+" Cell";
	}*/

	public final String getMaterialName() {
		return this.materialName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

		try {
			if ((this.materialName != null) && (this.materialName != "") && !this.materialName.equals("") && (this.componentMaterial != null)){


				if (this.componentMaterial != null){
					if ((!this.componentMaterial.vChemicalFormula.equals("??")) && (!this.componentMaterial.vChemicalFormula.equals("?")) && (this.componentMaterial.getState() != MaterialState.PURE_LIQUID)) {
						list.add(Utils.sanitizeStringKeepBrackets(this.componentMaterial.vChemicalFormula));
					}

					if (this.componentMaterial.isRadioactive){
						list.add(CORE.GT_Tooltip_Radioactive);
					}

					if (this.componentType == ComponentTypes.INGOT || this.componentType == ComponentTypes.HOTINGOT){
						if ((this.materialName != null) && (this.materialName != "") && !this.materialName.equals("") && this.unlocalName.toLowerCase().contains("hot")){
							list.add(EnumChatFormatting.GRAY+"Warning: "+EnumChatFormatting.RED+"Very hot! "+EnumChatFormatting.GRAY+" Avoid direct handling..");
						}
					}
				}

				//Hidden Tooltip
				if (KeyboardUtils.isCtrlKeyDown()) {
					if (this.componentMaterial != null) {
						String type = this.componentMaterial.getTextureSet().mSetName;
						String output = type.substring(0, 1).toUpperCase() + type.substring(1);
						list.add(EnumChatFormatting.GRAY+"Material Type: "+output+".");
						list.add(EnumChatFormatting.GRAY+"Material State: "+this.componentMaterial.getState().name()+".");
						list.add(EnumChatFormatting.GRAY+"Radioactivity Level: "+this.componentMaterial.vRadiationLevel+".");
					}
				}
				else {
					list.add(EnumChatFormatting.DARK_GRAY+"Hold Ctrl to show additional info.");				
				}			

			}
		}
		catch (Throwable t) {}

		super.addInformation(stack, aPlayer, list, bool);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		if (this.componentMaterial != null){
			if (entityHolding instanceof EntityPlayer){
				if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode){
					EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.componentMaterial.vRadiationLevel, world, entityHolding);	
				}
			}
		}

		if (extraData != null) {
			if (componentMaterial != null && componentMaterial.getRGBA()[3] > 1) {				
				if (((Map<Integer, Short[]>) extraData).get(9999) == null) {
					((Map<Integer, Short[]>) extraData).put(9999, new Short[] {0});
				}				
				Short aCurrentFrame = ((Map<Integer, Short[]>) extraData).get(9999)[0];
				short fC = (short) (aCurrentFrame >= Short.MAX_VALUE ? 0 : aCurrentFrame+1);
				((Map<Integer, Short[]>) extraData).put((int) 9999, new Short[] {(short) (fC), 0});
				((Map<Integer, Short[]>) extraData).put((int) 9998, new Short[] {aCurrentFrame, 0});

			}
		}
	}


	/**
	 * 
	 * Handle Custom Rendering
	 *
	 */

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return (CORE.ConfigSwitches.useGregtechTextures ? true : false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getColorFromItemStack(final ItemStack stack, final int renderPass) {


		if (renderPass == 0 && !CORE.ConfigSwitches.useGregtechTextures){
			return Utils.rgbtoHexValue(255, 255, 255);
		}
		if (renderPass == 1 && CORE.ConfigSwitches.useGregtechTextures){
			return Utils.rgbtoHexValue(255, 255, 255);
		}



		try {
			if (this.componentMaterial == null) {
				if (extraData != null) {
					if (short.class.isInstance(extraData)){
						short[] abc = (short[]) extraData;
						return Utils.rgbtoHexValue(abc[0], abc[1], abc[2]);
					}
				}
				return this.componentColour;
			}

			if (this.componentMaterial.getRGBA()[3] <= 1) {
				return this.componentColour;			
			}
			else {		

				//Mild Glow Effect
				if (this.componentMaterial.getRGBA()[3] == 2) {
					if (extraData == null) {
						extraData = new HashMap<Integer, Short[]>();
						((Map<Integer, Short[]>) extraData).put((int) Short.MAX_VALUE, new Short[] {0});
						((Map<Integer, Short[]>) extraData).put((int) Short.MAX_VALUE-1, new Short[] {0});
						short[] er = this.componentMaterial.getRGBA();
						short value = 1;
						Short[] ht = new Short[] {er[0], er[1], er[2]};
						for (int y = 0; y < 40; y++) {
							if (y < 20) {
								value = 1;
							}
							else {
								value = -1;
							}
							short r = (short) (ht[0] + value);
							short g = (short) (ht[1] + value);
							short b = (short) (ht[2] + value);
							Short[] qq = new Short[] {(short) Math.min(255, r), (short) Math.min(255, g), (short) Math.min(255, b)};
							ht = qq;
							((Map<Integer, Short[]>) extraData).put(y, qq);					
						}
					}
					if (extraData != null) {

						Short aCurrentFrame = ((Map<Integer, Short[]>) extraData).get((int) Short.MAX_VALUE)[0];
						Short aSize = 40;
						short nextFrame = (short) ((aCurrentFrame < aSize) ? (aCurrentFrame+1) : 0);
						Short[] aCurrentFrameRGB = ((Map<Integer, Short[]>) extraData).get(aCurrentFrame < aSize ? (int) aCurrentFrame : 0);
						((Map<Integer, Short[]>) extraData).put((int) Short.MAX_VALUE, new Short[] {nextFrame});
						return Utils.rgbtoHexValue(aCurrentFrameRGB[0], aCurrentFrameRGB[1], aCurrentFrameRGB[2]);	


						/*Short aCurrentFrame = ((Map<Integer, Short[]>) extraData).get((int) Short.MAX_VALUE)[0];
						Short[] aCurrentFrameRGB = ((Map<Integer, Short[]>) extraData).get((int) aCurrentFrame);
						short nextFrame = aCurrentFrame < 40 ? aCurrentFrame++ : 0;
						((Map<Integer, Short[]>) extraData).put((int) Short.MAX_VALUE, new Short[] {nextFrame});
						return Utils.rgbtoHexValue(aCurrentFrameRGB[0], aCurrentFrameRGB[1], aCurrentFrameRGB[2]);*/			
					}			
				}
				
				//Rainbow Hue Cycle
				else if (this.componentMaterial.getRGBA()[3] == 3) {
					if (extraData == null) {
						extraData = new HashMap<Integer, Short[]>();
						((Map<Integer, Short[]>) extraData).put((int) Short.MAX_VALUE, new Short[] {0});
						((Map<Integer, Short[]>) extraData).put((int) Short.MAX_VALUE-1, new Short[] {0});
						int aSlot = 0;

						//Let's say you're starting with green:
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255,   0});
						//Slowly start adding in some red to get to yellow:
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] { 51, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] { 51, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] { 51, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {102, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {102, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {102, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {153, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {153, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {153, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {204, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {204, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {204, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 255,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 255,   0});
						//Then, take out the green to get to red:
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 204,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 204,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 204,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 153,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 153,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 153,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 102,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 102,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255, 102,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,  51,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,  51,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,  51,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0,   0});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0,   0});
						//Now, add blue to get to purple:
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0,  51});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0,  51});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0,  51});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 102});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 102});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 102});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 153});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 153});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 153});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 204});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 204});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 204});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {255,   0, 255});
						//Then, remove red to get to blue:
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {204,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {204,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {204,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {153,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {153,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {153,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {102,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {102,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {102,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] { 51,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] { 51,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] { 51,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0,   0, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0,   0, 255});
						//Add the green back in to get to cyan:
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0,  51, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0,  51, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0,  51, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 102, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 102, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 102, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 153, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 153, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 153, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 204, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 204, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 204, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 255});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 255});
						//And finally remove the blue to get back to green:
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 204});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 204});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 204});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 153});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 153});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 153});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 102});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 102});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255, 102});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255,  51});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255,  51});
						((Map<Integer, Short[]>) extraData).put(aSlot++, new Short[] {  0, 255,  51});
						((Map<Integer, Short[]>) extraData).put((int) Byte.MAX_VALUE, new Short[] {(short) (((Map<Integer, Short[]>) extraData).size()-1)});


					}
					if (extraData != null) {
						Short aCurrentFrame = ((Map<Integer, Short[]>) extraData).get((int) Short.MAX_VALUE)[0];
						Short aSize = (short) (((Map<Integer, Short[]>) extraData).size() - 3);				
						short nextFrame = (short) ((aCurrentFrame < aSize) ? (aCurrentFrame+1) : 0);
						Short[] aCurrentFrameRGB = ((Map<Integer, Short[]>) extraData).get(aCurrentFrame < aSize ? (int) aCurrentFrame : 0);
						((Map<Integer, Short[]>) extraData).put((int) Short.MAX_VALUE, new Short[] {nextFrame});				
						return Utils.rgbtoHexValue(aCurrentFrameRGB[0], aCurrentFrameRGB[1], aCurrentFrameRGB[2]);			
					}			
				}
			}


		}
		catch (Throwable t) {

		}
		return this.componentColour;





	}

	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		if (CORE.ConfigSwitches.useGregtechTextures) {
			if(pass == 0) {
				return this.base;
			}
			return this.overlay;			
		}
		return this.base;
	}

	@Override
	public void registerIcons(final IIconRegister i) {

		if (CORE.ConfigSwitches.useGregtechTextures){
			this.base = i.registerIcon(getCorrectTextures());
			this.overlay = i.registerIcon(getCorrectTextures() + "_OVERLAY");
		}
		else {
			this.base = i.registerIcon(getCorrectTextures());
			//this.overlay = i.registerIcon(getCorrectTextures() + "_OVERLAY");
		}
	}




	public static enum ComponentTypes {
		DUST("Dust", " Dust", "dust", OrePrefixes.dust),
		DUSTSMALL("DustSmall", " Dust", "dustSmall", OrePrefixes.dustSmall),
		DUSTTINY("DustTiny", " Dust", "dustTiny", OrePrefixes.dustTiny),
		INGOT("Ingot", " Ingot", "ingot", OrePrefixes.ingot),
		HOTINGOT("HotIngot", " Hot Ingot", "ingotHot", OrePrefixes.ingotHot),
		PLATE("Plate", " Plate", "plate", OrePrefixes.plate),
		PLATEDOUBLE("PlateDouble", " Double Plate", "plateDouble", OrePrefixes.plateDouble),
		ROD("Rod", " Rod", "stick", OrePrefixes.stick),
		RODLONG("RodLong", " Long Rod", "stickLong", OrePrefixes.stickLong),
		GEAR("Gear", " Gear", "gearGt", OrePrefixes.gearGt),
		SMALLGEAR("SmallGear", " Gear", "gearGtSmall", OrePrefixes.gearGtSmall), //TODO
		SCREW("Screw", " Screw", "screw", OrePrefixes.screw),
		BOLT("Bolt", " Bolt", "bolt", OrePrefixes.bolt),
		ROTOR("Rotor", " Rotor", "rotor", OrePrefixes.rotor),
		RING("Ring", " Ring", "ring", OrePrefixes.ring),
		FOIL("Foil", " Foil", "foil", OrePrefixes.foil),
		PLASMACELL("CellPlasma", " Plasma Cell", "cellPlasma", OrePrefixes.cellPlasma),
		CELL("Cell", " Cell", "cell", OrePrefixes.cell),
		NUGGET("Nugget", " Nugget", "nugget", OrePrefixes.nugget),
		PLATEHEAVY("HeavyPlate", " Heavy Plate", "plateHeavy", OrePrefixes.plateDense);

		private String COMPONENT_NAME;
		private String DISPLAY_NAME;
		private String OREDICT_NAME;
		private OrePrefixes a_GT_EQUAL;
		private ComponentTypes (final String LocalName, final String DisplayName, final String OreDictName, final OrePrefixes aPrefix){
			this.COMPONENT_NAME = LocalName;
			this.DISPLAY_NAME = DisplayName;
			this.OREDICT_NAME = OreDictName;
			this.a_GT_EQUAL = aPrefix;
		}

		public String getComponent(){
			return this.COMPONENT_NAME;
		}

		public String getName(){
			return this.DISPLAY_NAME;
		}

		public String getOreDictName(){
			return this.OREDICT_NAME;
		}

		public OrePrefixes getGtOrePrefix() {
			return this.a_GT_EQUAL;
		}

	}

}


