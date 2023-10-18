package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;

public class BaseItemComponent extends Item {

    private static final Class<TextureSet> mTextureSetPreload;

    static {
        mTextureSetPreload = TextureSet.class;
    }

    public final Material componentMaterial;
    public final String materialName;
    public final String unlocalName;
    public final String translatedMaterialName;
    public final ComponentTypes componentType;
    public final int componentColour;
    public Object extraData;

    protected IIcon base;
    protected IIcon overlay;

    public BaseItemComponent(final Material material, final ComponentTypes componentType) {
        this.componentMaterial = material;
        this.unlocalName = "item" + componentType.COMPONENT_NAME + material.getUnlocalizedName();
        this.materialName = material.getLocalizedName();
        this.translatedMaterialName = material.getTranslatedName();
        this.componentType = componentType;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(this.unlocalName);
        this.setMaxStackSize(64);
        // this.setTextureName(this.getCorrectTextures());
        this.componentColour = material.getRgbAsHex();
        GameRegistry.registerItem(this, this.unlocalName);

        // if (componentType != ComponentTypes.DUST)

        GT_OreDictUnificator.registerOre(
                componentType.getOreDictName() + material.getUnlocalizedName(),
                ItemUtils.getSimpleStack(this));
        if (componentType == ComponentTypes.GEAR) {
            GT_OreDictUnificator.registerOre("gear" + material.getUnlocalizedName(), ItemUtils.getSimpleStack(this));
        }
        registerComponent();

        GT_LanguageManager.addStringLocalization("gtplusplus.item." + unlocalName + ".name", getFormattedLangName());
    }

    // For Cell Generation
    public BaseItemComponent(final String unlocalName, final String localName, final short[] RGBA) {

        // Handles .'s from fluid internal names.
        String aFormattedNameForFluids;
        if (unlocalName.contains(".")) {
            aFormattedNameForFluids = StringUtils.splitAndUppercase(unlocalName, ".");
        } else {
            aFormattedNameForFluids = unlocalName;
        }
        Material aTempMaterial = Material.mMaterialCache.get(localName.toLowerCase());
        Logger.INFO("Attempted to get " + localName + " cell material from cache. Valid? " + (aTempMaterial != null));
        this.componentMaterial = aTempMaterial;
        this.unlocalName = "itemCell" + aFormattedNameForFluids;
        this.materialName = localName;
        this.translatedMaterialName = getFluidName("fluid." + this.materialName.toLowerCase().replace(" ", ""));
        this.componentType = ComponentTypes.CELL;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(aFormattedNameForFluids);
        this.setMaxStackSize(64);
        this.componentColour = MathUtils.getRgbAsHex(RGBA);
        this.extraData = RGBA;

        this.setTextureName(GTPlusPlus.ID + ":" + "item" + ComponentTypes.CELL.COMPONENT_NAME);
        GameRegistry.registerItem(this, aFormattedNameForFluids);
        GT_OreDictUnificator.registerOre(
                ComponentTypes.CELL.getOreDictName() + Utils.sanitizeStringKeepBrackets(localName),
                ItemUtils.getSimpleStack(this));
        registerComponent();

        GT_LanguageManager
                .addStringLocalization("gtplusplus.item." + this.unlocalName + ".name", getFormattedLangName());
    }

    private String getFormattedLangName() {
        return componentType.getName().replace("@", "%material");
    }

    public boolean registerComponent() {
        if (this.componentMaterial == null) {
            return false;
        }
        // Register Component
        Map<String, ItemStack> aMap = Material.mComponentMap.get(componentMaterial.getUnlocalizedName());
        if (aMap == null) {
            aMap = new HashMap<>();
        }
        String aKey = componentType.getGtOrePrefix().name();
        ItemStack x = aMap.get(aKey);
        if (x == null) {
            aMap.put(aKey, ItemUtils.getSimpleStack(this));
            Logger.MATERIALS(
                    "Registering a material component. Item: [" + componentMaterial.getUnlocalizedName()
                            + "] Map: ["
                            + aKey
                            + "]");
            Material.mComponentMap.put(componentMaterial.getUnlocalizedName(), aMap);
            if (componentType == ComponentTypes.PLATE) {
                GregTech_API.registerCover(
                        componentMaterial.getPlate(1),
                        new GT_RenderedTexture(
                                componentMaterial.getTextureSet().mTextures[71],
                                componentMaterial.getRGBA(),
                                false),
                        null);
            } else if (componentType == ComponentTypes.PLATEDOUBLE) {
                GregTech_API.registerCover(
                        componentMaterial.getPlateDouble(1),
                        new GT_RenderedTexture(
                                componentMaterial.getTextureSet().mTextures[72],
                                componentMaterial.getRGBA(),
                                false),
                        null);
            }
            return true;
        } else {
            // Bad
            Logger.MATERIALS("Tried to double register a material component. ");
            return false;
        }
    }

    public String getCorrectTextures() {
        if (!CORE.ConfigSwitches.useGregtechTextures) {
            return GTPlusPlus.ID + ":" + "item" + this.componentType.COMPONENT_NAME;
        }
        String metType = "9j4852jyo3rjmh3owlhw9oe";
        if (this.componentMaterial != null) {
            TextureSet u = this.componentMaterial.getTextureSet();
            if (u != null) {
                metType = u.mSetName;
            }
        }
        metType = (metType.equals("9j4852jyo3rjmh3owlhw9oe") ? "METALLIC" : metType);
        return GregTech.ID + ":" + "materialicons/" + metType + "/" + this.componentType.getOreDictName();

        // return GregTech.ID + ":" + "materialicons/"+metType+"/" + this.componentType.COMPONENT_NAME.toLowerCase();
    }

    /*
     * @Override public String getItemStackDisplayName(final ItemStack p_77653_1_) { if (this.componentType ==
     * ComponentTypes.SMALLGEAR){ return "Small " + this.materialName+" Gear"; } if (this.componentMaterial != null) {
     * return (this.componentMaterial.getLocalizedName()+this.componentType.DISPLAY_NAME); } return
     * this.materialName+" Cell"; }
     */

    public final String getMaterialName() {
        return this.materialName;
    }

    public String getFluidName(String aKey) {
        String trans;
        trans = GT_LanguageManager.getTranslation(aKey);
        if (!trans.equals(aKey)) return trans;
        aKey = "fluid." + aKey;
        trans = GT_LanguageManager.getTranslation(aKey);
        if (!trans.equals(aKey)) return trans;
        return GT_LanguageManager.addStringLocalization(
                "gtplusplus.fluid." + this.materialName.toLowerCase().replace(" ", ""),
                this.materialName);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return GT_LanguageManager.getTranslation("gtplusplus.item." + unlocalName + ".name").replace("%s", "%temp")
                .replace("%material", translatedMaterialName).replace("%temp", "%s");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list,
            final boolean bool) {

        try {

            if (this.componentMaterial == null) {
                if (this.materialName != null) {
                    // list.add(Utils.sanitizeStringKeepBrackets(materialName));
                }
            }

            if ((this.materialName != null) && (this.materialName != "")
                    && !this.materialName.equals("")
                    && (this.componentMaterial != null)) {

                if (this.componentMaterial != null) {
                    if (!this.componentMaterial.vChemicalFormula.contains("?")) {
                        list.add(Utils.sanitizeStringKeepBrackets(this.componentMaterial.vChemicalFormula));
                    } else if (this.componentMaterial.vChemicalFormula.contains("?")) {
                        String temp = componentMaterial.vChemicalFormula;
                        temp = temp.replace(" ", "");
                        temp = temp.replace("-", "");
                        temp = temp.replace("_", "");
                        temp = temp.replace("!", "");
                        temp = temp.replace("@", "");
                        temp = temp.replace("#", "");
                        temp = temp.replace(" ", "");
                        list.add(temp);
                    }

                    if (this.componentMaterial.isRadioactive) {
                        list.add(CORE.GT_Tooltip_Radioactive.get());
                    }

                    if (this.componentType == ComponentTypes.INGOT || this.componentType == ComponentTypes.HOTINGOT) {
                        if ((this.materialName != null) && (this.materialName != "")
                                && !this.materialName.equals("")
                                && this.unlocalName.toLowerCase().contains("hot")) {
                            list.add(
                                    EnumChatFormatting.GRAY + "Warning: "
                                            + EnumChatFormatting.RED
                                            + "Very hot! "
                                            + EnumChatFormatting.GRAY
                                            + " Avoid direct handling..");
                        }
                    }
                } else {
                    String aChemicalFormula = Material.sChemicalFormula.get(materialName.toLowerCase());
                    if (aChemicalFormula != null && aChemicalFormula.length() > 0) {
                        list.add(Utils.sanitizeStringKeepBrackets(aChemicalFormula));
                    }
                }

                // Hidden Tooltip
                if (KeyboardUtils.isCtrlKeyDown()) {
                    if (this.componentMaterial != null) {
                        String type = this.componentMaterial.getTextureSet().mSetName;
                        String output = type.substring(0, 1).toUpperCase() + type.substring(1);
                        list.add(EnumChatFormatting.GRAY + "Material Type: " + output + ".");
                        list.add(
                                EnumChatFormatting.GRAY + "Material State: "
                                        + this.componentMaterial.getState().name()
                                        + ".");
                        list.add(
                                EnumChatFormatting.GRAY + "Radioactivity Level: "
                                        + this.componentMaterial.vRadiationLevel
                                        + ".");
                    }
                } else {
                    list.add(EnumChatFormatting.DARK_GRAY + "Hold Ctrl to show additional info.");
                }
            }
        } catch (Throwable t) {}

        super.addInformation(stack, aPlayer, list, bool);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
            final boolean p_77663_5_) {
        if (this.componentMaterial != null) {
            if (entityHolding instanceof EntityPlayer) {
                if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode) {
                    EntityUtils.applyRadiationDamageToEntity(
                            iStack.stackSize,
                            this.componentMaterial.vRadiationLevel,
                            world,
                            entityHolding);
                }
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
    public boolean requiresMultipleRenderPasses() {
        return (CORE.ConfigSwitches.useGregtechTextures ? true : false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {

        if (this.componentType == ComponentTypes.CELL || this.componentType == ComponentTypes.PLASMACELL) {
            if (renderPass == 0 && !CORE.ConfigSwitches.useGregtechTextures) {
                return Utils.rgbtoHexValue(255, 255, 255);
            }
            if (renderPass == 1 && CORE.ConfigSwitches.useGregtechTextures) {
                return Utils.rgbtoHexValue(255, 255, 255);
            }
        }

        try {
            if (this.componentMaterial == null) {
                if (extraData != null) {
                    if (short.class.isInstance(extraData)) {
                        short[] abc = (short[]) extraData;
                        return Utils.rgbtoHexValue(abc[0], abc[1], abc[2]);
                    }
                }
                return this.componentColour;
            }

            if (this.componentMaterial.getRGBA()[3] <= 1) {
                return this.componentColour;
            } else {
                // Mild Glow Effect
                if (this.componentMaterial.getRGBA()[3] == 2) {
                    // 4 sec cycle, 200 control point. 20ms interval.
                    int currentFrame = (int) ((System.nanoTime() % 4_000_000_000L) / 20_000_000L);
                    int value = currentFrame < 50 ? currentFrame + 1
                            : currentFrame < 100 ? 50 : currentFrame < 150 ? 149 - currentFrame : 0;
                    return Utils.rgbtoHexValue(
                            Math.min(255, Math.max(componentMaterial.getRGBA()[0] + value, 0)),
                            Math.min(255, Math.max(componentMaterial.getRGBA()[1] + value, 0)),
                            Math.min(255, Math.max(componentMaterial.getRGBA()[2] + value, 0)));
                }

                // Rainbow Hue Cycle
                else if (this.componentMaterial.getRGBA()[3] == 3) {
                    return Color.HSBtoRGB((float) (System.nanoTime() % 8_000_000_000L) / 8_000_000_000f, 1, 1);
                }
            }

        } catch (Throwable t) {

        }
        return this.componentColour;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        if (CORE.ConfigSwitches.useGregtechTextures) {
            if (pass == 0) {
                return this.base;
            }
            return this.overlay;
        }
        return this.base;
    }

    @Override
    public void registerIcons(final IIconRegister i) {

        if (CORE.ConfigSwitches.useGregtechTextures) {
            this.base = i.registerIcon(getCorrectTextures());
            this.overlay = i.registerIcon(getCorrectTextures() + "_OVERLAY");
        } else {
            this.base = i.registerIcon(getCorrectTextures());
            // this.overlay = i.registerIcon(getCorrectTextures() + "_OVERLAY");
        }
    }

    public static enum ComponentTypes {

        DUST("Dust", "@ Dust", "dust", OrePrefixes.dust),
        DUSTSMALL("DustSmall", "Small Pile of @ Dust", "dustSmall", OrePrefixes.dustSmall),
        DUSTTINY("DustTiny", "Tiny Pile of @ Dust", "dustTiny", OrePrefixes.dustTiny),
        INGOT("Ingot", "@ Ingot", "ingot", OrePrefixes.ingot),
        HOTINGOT("HotIngot", "Hot @ Ingot", "ingotHot", OrePrefixes.ingotHot),
        PLATE("Plate", "@ Plate", "plate", OrePrefixes.plate),
        PLATEDOUBLE("PlateDouble", "Double @ Plate", "plateDouble", OrePrefixes.plateDouble),
        ROD("Rod", "@ Rod", "stick", OrePrefixes.stick),
        RODLONG("RodLong", "Long @ Rod", "stickLong", OrePrefixes.stickLong),
        GEAR("Gear", "@ Gear", "gearGt", OrePrefixes.gearGt),
        SMALLGEAR("SmallGear", "Small @ Gear", "gearGtSmall", OrePrefixes.gearGtSmall), // TODO
        SCREW("Screw", "@ Screw", "screw", OrePrefixes.screw),
        BOLT("Bolt", "@ Bolt", "bolt", OrePrefixes.bolt),
        ROTOR("Rotor", "@ Rotor", "rotor", OrePrefixes.rotor),
        RING("Ring", "@ Ring", "ring", OrePrefixes.ring),
        FOIL("Foil", "@ Foil", "foil", OrePrefixes.foil),
        PLASMACELL("CellPlasma", "@ Plasma Cell", "cellPlasma", OrePrefixes.cellPlasma),
        CELL("Cell", "@ Cell", "cell", OrePrefixes.cell),
        NUGGET("Nugget", "@ Nugget", "nugget", OrePrefixes.nugget),
        SPRING("Spring", "@ Spring", "spring", OrePrefixes.spring),
        SMALLSPRING("SmallSpring", "Small @ Spring", "springSmall", OrePrefixes.springSmall),
        FINEWIRE("FineWire", "Fine @ Wire", "wireFine", OrePrefixes.wireFine),
        PLATEDENSE("PlateDense", "Dense @ Plate", "plateDense", OrePrefixes.plateDense),;

        private final String COMPONENT_NAME;
        private final String DISPLAY_NAME;
        private final String OREDICT_NAME;
        private final OrePrefixes a_GT_EQUAL;

        private ComponentTypes(final String LocalName, final String DisplayName, final String OreDictName,
                final OrePrefixes aPrefix) {
            this.COMPONENT_NAME = LocalName;
            this.DISPLAY_NAME = DisplayName;
            this.OREDICT_NAME = OreDictName;
            this.a_GT_EQUAL = aPrefix;
        }

        public String getComponent() {
            return this.COMPONENT_NAME;
        }

        public String getName() {
            return this.DISPLAY_NAME;
        }

        public String getOreDictName() {
            return this.OREDICT_NAME;
        }

        public OrePrefixes getGtOrePrefix() {
            return this.a_GT_EQUAL;
        }
    }
}
