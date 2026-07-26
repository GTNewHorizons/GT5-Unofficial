package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.StringUtils;
import gregtech.common.config.Client;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;

public class BaseItemComponent extends Item {

    /// Null for every surviving construction path (cell generation from a bare fluid name never had a
    /// material to associate); retained as a field, rather than deleted outright, so the null-checked reads
    /// below stay meaningful if a future caller supplies one.
    public final com.ruling_0.materiallib.api.Material componentMaterial;
    public final String materialName;
    public final String unlocalName;
    public final String materialKey;
    public final ComponentTypes componentType;
    public final int componentColour;
    public short[] extraData;

    @SideOnly(Side.CLIENT)
    protected IIcon iconBase;
    @SideOnly(Side.CLIENT)
    protected IIcon iconOverlay;

    // For Cell Generation
    public BaseItemComponent(final String unlocalName, final Fluid fluid, final String localName, final short[] RGBA) {

        // Handles .'s from fluid internal names.
        String aFormattedNameForFluids;
        if (unlocalName.contains(".")) {
            aFormattedNameForFluids = StringUtils.splitAndUppercase(unlocalName);
        } else {
            aFormattedNameForFluids = unlocalName;
        }
        this.componentMaterial = null;
        this.unlocalName = "itemCell" + aFormattedNameForFluids;
        this.materialName = localName;
        this.materialKey = fluid.getUnlocalizedName();
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
    }

    public final String getMaterialName() {
        return this.materialName;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (componentMaterial != null) {
            return componentType.getGtOrePrefix()
                .getLocalizedNameForItem(MU.internalName(componentMaterial));
        }
        return OrePrefixes.getLocalizedNameForItemForKey(componentType.getName(), "@", materialKey);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list,
        final boolean bool) {

        try {
            if (this.materialName != null && !this.materialName.isEmpty() && (this.componentMaterial != null)) {

                MU.addTooltipsOf(this.componentMaterial, list);

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
                    if (GuiScreen.isCtrlKeyDown()) {
                        String type = MU.iconSet(this.componentMaterial).mSetName;
                        String output = type.substring(0, 1)
                            .toUpperCase() + type.substring(1);
                        list.add(
                            EnumChatFormatting.GRAY
                                + StatCollector.translateToLocalFormatted("GTPP.tooltip.material.type", output));
                        Integer radiationLevel = this.componentMaterial
                            .getProperty(GTMaterialProperties.RADIATION_LEVEL);
                        list.add(
                            EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                                "GTPP.tooltip.material.radioactivity",
                                radiationLevel == null ? 0 : radiationLevel));
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
            Integer radiationLevel = this.componentMaterial.getProperty(GTMaterialProperties.RADIATION_LEVEL);
            EntityUtils.applyRadiationDamageToEntity(
                iStack.stackSize,
                radiationLevel == null ? 0 : radiationLevel,
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

            if (MU.rgba(this.componentMaterial)[3] <= 1) {
                return this.componentColour;
            } else {
                // Animated materials ship baked animated textures; render them untinted.
                return Utils.rgbtoHexValue(255, 255, 255);
            }

        } catch (Exception ignored) {}
        return this.componentColour;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        if (pass == 0) {
            return iconBase;
        }
        return iconOverlay;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister i) {
        String metType = null;
        if (this.componentMaterial != null) {
            TextureSet u = MU.iconSet(this.componentMaterial);
            if (u != null) {
                metType = u.mSetName;
            }
        }
        metType = (metType == null ? "METALLIC" : metType);
        IIconContainer container = Textures.ItemIcons
            .textureSetWithRegister(metType, "/" + this.componentType.getOreDictName(), i);
        iconBase = container.getIcon();
        iconOverlay = container.getOverlayIcon();
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
