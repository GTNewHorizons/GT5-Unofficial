package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gregtech.common.config.Client;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;

public class BaseItemComponent extends Item {

    public final Material componentMaterial;
    public final String materialName;
    public final String unlocalName;
    public final Supplier<String> translatedMaterialName;
    public final ComponentTypes componentType;
    public final int componentColour;
    public short[] extraData;

    protected IIcon base;
    protected IIcon overlay;

    public BaseItemComponent(final Material material, final ComponentTypes componentType) {
        this.componentMaterial = material;
        this.unlocalName = "item" + componentType.COMPONENT_NAME + material.getUnlocalizedName();
        this.materialName = material.getDefaultLocalName();
        this.translatedMaterialName = material::getLocalizedName;
        this.componentType = componentType;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(this.unlocalName);
        this.setMaxStackSize(64);
        this.componentColour = material.getRgbAsHex();
        GameRegistry.registerItem(this, this.unlocalName);

        GTOreDictUnificator
            .registerOre(componentType.getOreDictName() + material.getUnlocalizedName(), new ItemStack(this));
        if (componentType == ComponentTypes.GEAR) {
            GTOreDictUnificator.registerOre("gear" + material.getUnlocalizedName(), new ItemStack(this));
        }
        registerComponent();
    }

    // For Cell Generation
    public BaseItemComponent(final String unlocalName, final Fluid fluid, final String localName, final short[] RGBA) {

        // Handles .'s from fluid internal names.
        String aFormattedNameForFluids;
        if (unlocalName.contains(".")) {
            aFormattedNameForFluids = StringUtils.splitAndUppercase(unlocalName);
        } else {
            aFormattedNameForFluids = unlocalName;
        }
        Material aTempMaterial = Material.mMaterialCache.get(localName.toLowerCase());
        Logger.INFO("Attempted to get " + localName + " cell material from cache. Valid? " + (aTempMaterial != null));
        this.componentMaterial = aTempMaterial;
        this.unlocalName = "itemCell" + aFormattedNameForFluids;
        this.materialName = localName;
        this.translatedMaterialName = () -> GTUtility.getFluidName(fluid, true);
        this.componentType = ComponentTypes.CELL;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(aFormattedNameForFluids);
        this.setMaxStackSize(64);
        this.componentColour = MathUtils.getRgbAsHex(RGBA);
        this.extraData = RGBA;

        this.setTextureName(GTPlusPlus.ID + ":" + "item" + ComponentTypes.CELL.COMPONENT_NAME);
        GameRegistry.registerItem(this, aFormattedNameForFluids);
        GTOreDictUnificator.registerOre(
            ComponentTypes.CELL.getOreDictName() + StringUtils.sanitizeStringKeepBrackets(localName),
            new ItemStack(this));
        registerComponent();
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
        String aKey = componentType.getGtOrePrefix()
            .getName();
        ItemStack x = aMap.get(aKey);
        if (x == null) {
            aMap.put(aKey, new ItemStack(this));
            Logger.MATERIALS(
                "Registering a material component. Item: [" + componentMaterial.getUnlocalizedName()
                    + "] Map: ["
                    + aKey
                    + "]");
            Material.mComponentMap.put(componentMaterial.getUnlocalizedName(), aMap);
            if (componentType == ComponentTypes.PLATE) {
                CoverRegistry.registerDecorativeCover(
                    componentMaterial.getPlate(1),
                    TextureFactory.of(componentMaterial.getTextureSet().mTextures[71], componentMaterial.getRGBA()));
            } else if (componentType == ComponentTypes.PLATEDOUBLE) {
                CoverRegistry.registerDecorativeCover(
                    componentMaterial.getPlateDouble(1),
                    TextureFactory.of(componentMaterial.getTextureSet().mTextures[72], componentMaterial.getRGBA()));
            }
            return true;
        } else {
            // Bad
            Logger.MATERIALS("Tried to double register a material component. ");
            return false;
        }
    }

    public String getCorrectTextures() {
        String metType = null;
        if (this.componentMaterial != null) {
            TextureSet u = this.componentMaterial.getTextureSet();
            if (u != null) {
                metType = u.mSetName;
            }
        }
        metType = (metType == null ? "METALLIC" : metType);
        return GregTech.ID + ":" + "materialicons/" + metType + "/" + this.componentType.getOreDictName();
    }

    public final String getMaterialName() {
        return this.materialName;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return OrePrefixes.getLocalizedNameForItem(componentType.getName(), "@", translatedMaterialName.get());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list,
        final boolean bool) {

        try {
            if (this.materialName != null && !this.materialName.isEmpty() && (this.componentMaterial != null)) {

                this.componentMaterial.addTooltips(list);

                if (Client.tooltip.showHotIngotText) {
                    if (this.componentType == ComponentTypes.INGOT || this.componentType == ComponentTypes.HOTINGOT) {
                        if (this.unlocalName.toLowerCase()
                            .contains("hot")) {
                            list.add(StatCollector.translateToLocal("gtpp.tooltip.ingot.very_hot"));
                        }
                    }
                }

                if (Client.tooltip.showCtrlText) {
                    // Hidden Tooltip
                    if (KeyboardUtils.isCtrlKeyDown()) {
                        String type = this.componentMaterial.getTextureSet().mSetName;
                        String output = type.substring(0, 1)
                            .toUpperCase() + type.substring(1);
                        list.add(
                            EnumChatFormatting.GRAY
                                + StatCollector.translateToLocalFormatted("GTPP.tooltip.material.type", output));
                        list.add(
                            EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                                "GTPP.tooltip.material.state",
                                this.componentMaterial.getState()
                                    .name()));
                        list.add(
                            EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                                "GTPP.tooltip.material.radioactivity",
                                this.componentMaterial.vRadiationLevel));
                    } else {
                        list.add(
                            EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("GTPP.tooltip.hold_ctrl"));
                    }
                }
            }
        } catch (Exception t) {}

        super.addInformation(stack, aPlayer, list, bool);
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (this.componentMaterial != null) {
            EntityUtils.applyRadiationDamageToEntity(
                iStack.stackSize,
                this.componentMaterial.vRadiationLevel,
                world,
                entityHolding);
        }
    }

    /**
     * Handle Custom Rendering
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {

        if (this.componentType == ComponentTypes.CELL || this.componentType == ComponentTypes.PLASMACELL) {
            if (renderPass == 1) {
                return Utils.rgbtoHexValue(255, 255, 255);
            }
        }

        try {
            if (renderPass != 0) {
                return Utils.rgbtoHexValue(255, 255, 255);
            }

            if (this.componentMaterial == null) {
                if (extraData != null) {
                    return Utils.rgbtoHexValue(extraData[0], extraData[1], extraData[2]);
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

        } catch (Exception t) {

        }
        return this.componentColour;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        if (pass == 0) {
            return this.base;
        }
        return this.overlay;
    }

    @Override
    public void registerIcons(final IIconRegister i) {
        this.base = i.registerIcon(getCorrectTextures());
        this.overlay = i.registerIcon(getCorrectTextures() + "_OVERLAY");
    }

    public enum ComponentTypes {

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
        PLATEDENSE("PlateDense", "Dense @ Plate", "plateDense", OrePrefixes.plateDense),
        PLATESUPERDENSE("PlateSuperDense", "Superdense @ Plate", "plateSuperdense", OrePrefixes.plateSuperdense);

        private final String COMPONENT_NAME;
        private final String DISPLAY_NAME;
        private final String OREDICT_NAME;
        private final OrePrefixes a_GT_EQUAL;

        ComponentTypes(final String LocalName, final String DisplayName, final String OreDictName,
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
