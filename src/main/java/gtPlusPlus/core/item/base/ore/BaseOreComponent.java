package gtPlusPlus.core.item.base.ore;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.material.MU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.StringUtils;
import gregtech.common.config.Client;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class BaseOreComponent extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon iconBase;
    @SideOnly(Side.CLIENT)
    private IIcon iconOverlay;

    public final Material componentMaterial;
    public final String materialName;
    public final String unlocalName;
    public final ComponentTypes componentType;
    public final int componentColour;

    public BaseOreComponent(final Material material, final ComponentTypes componentType) {
        this.componentMaterial = material;
        this.unlocalName = componentType.COMPONENT_NAME + material.getUnlocalizedName();
        this.materialName = material.getDefaultLocalName();
        this.componentType = componentType;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(this.unlocalName);
        this.setMaxStackSize(64);
        this.componentColour = material.getRgbAsHex();
        GameRegistry.registerItem(this, this.unlocalName);
        registerComponent();
        // Materials outside the gtpp reconstruction gate (e.g. a base gregtech Materials enum constant that
        // gained this part's shape purely from a stage-11 codegen name-merge, such as milled ore for
        // Sphalerite) never skip this constructor -- unlike MaterialGenerator's own cutOver() gate for
        // reconstructed materials. Registering this item into the same oredict name MaterialLib already owns
        // would create a second entry that races the MaterialLib one across launches (see
        // MaterialReconstruction census nondeterminism, stage 11 commit 4); keep the item itself (legacy
        // saves/oredict-name lookups still work through MaterialLib), just skip the duplicate association.
        if (!MU.isCutOver(componentType.getOrePrefixEnum(), MaterialUtils.getMaterial(material.getUnlocalizedName()))) {
            GTOreDictUnificator
                .registerOre(componentType.getOrePrefix() + material.getUnlocalizedName(), new ItemStack(this, 1));
        }
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
        String aKey = "Invalid";
        switch (componentType) {
            case CRUSHED -> aKey = OrePrefixes.crushed.getName();
            case CRUSHEDCENTRIFUGED -> aKey = OrePrefixes.crushedCentrifuged.getName();
            case CRUSHEDPURIFIED -> aKey = OrePrefixes.crushedPurified.getName();
            case DUST -> aKey = OrePrefixes.dust.getName();
            case DUSTIMPURE -> aKey = OrePrefixes.dustImpure.getName();
            case DUSTPURE -> aKey = OrePrefixes.dustPure.getName();
            case MILLED -> aKey = OrePrefixes.milled.getName();
            case RAWORE -> aKey = OrePrefixes.rawOre.getName();
        }

        ItemStack x = aMap.get(aKey);
        if (x == null) {
            aMap.put(aKey, new ItemStack(this));
            Material.mComponentMap.put(componentMaterial.getUnlocalizedName(), aMap);
            return true;
        } else {
            return false;
        }
    }

    /*
     * @Override public String getItemStackDisplayName(final ItemStack p_77653_1_) { return
     * (this.componentType.getPrefix()+this.componentMaterial.getLocalizedName()+this.componentType.DISPLAY_NAME); }
     */

    public final String getMaterialName() {
        return this.materialName;
    }

    @Override
    public final void addInformation(final ItemStack stack, final EntityPlayer player, final List<String> tooltip,
        final boolean adv) {
        if (this.materialName != null && !this.materialName.isEmpty()) {
            if (this.componentMaterial != null) {
                componentMaterial.addTooltips(tooltip);
            } else {
                if (Client.tooltip.showFormula) {
                    String aChemicalFormula = Material.sChemicalFormula.get(materialName.toLowerCase());
                    if (aChemicalFormula != null && !aChemicalFormula.isEmpty()) {
                        tooltip.add(StringUtils.sanitizeStringKeepBrackets(aChemicalFormula));
                    }
                }
            }
        }
        super.addInformation(stack, player, tooltip, adv);
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
     * Rendering Related
     *
     * @author Alkalus
     *
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return this.componentType.hasOverlay();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister) {
        if (this.componentType == ComponentTypes.MILLED) {
            this.iconBase = par1IconRegister.registerIcon(GTPlusPlus.ID + ":" + "processing/MilledOre/milled");
            if (this.componentType.hasOverlay()) {
                this.iconOverlay = par1IconRegister
                    .registerIcon(GTPlusPlus.ID + ":" + "processing/MilledOre/milled_OVERLAY");
            }
        } else {
            IIconContainer container = Textures.ItemIcons
                .textureSetWithRegister("METALLIC", "/" + this.componentType.COMPONENT_NAME, par1IconRegister);
            iconBase = container.getIcon();
            iconOverlay = container.getOverlayIcon();
        }
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (this.componentType == ComponentTypes.MILLED) {
            if (renderPass == 1) {
                return Utils.rgbtoHexValue(230, 230, 230);
            }
        } else {
            if (renderPass == 1) {
                return Utils.rgbtoHexValue(230, 230, 230);
            }
        }
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

    public enum ComponentTypes {

        DUST("dust", OrePrefixes.dust, "", " Dust", true),
        DUSTIMPURE("dustImpure", OrePrefixes.dustImpure, "Impure ", " Dust", true),
        DUSTPURE("dustPure", OrePrefixes.dustPure, "Purified ", " Dust", true),
        CRUSHED("crushed", OrePrefixes.crushed, "Crushed ", " Ore", true),
        CRUSHEDCENTRIFUGED("crushedCentrifuged", OrePrefixes.crushedCentrifuged, "Centrifuged Crushed ", " Ore", true),
        CRUSHEDPURIFIED("crushedPurified", OrePrefixes.crushedPurified, "Purified Crushed ", " Ore", true),
        RAWORE("oreRaw", OrePrefixes.rawOre, "Raw ", " Ore", true),
        MILLED("milled", OrePrefixes.milled, "Milled ", " Ore", true);

        private final String COMPONENT_NAME;
        private final String PREFIX;
        private final String DISPLAY_NAME;
        private final boolean HAS_OVERLAY;
        private final String orePrefix;
        private final OrePrefixes orePrefixEnum;

        ComponentTypes(final String LocalName, final OrePrefixes orePrefix, final String prefix,
            final String DisplayName, final boolean overlay) {
            this.COMPONENT_NAME = LocalName;
            this.orePrefixEnum = orePrefix;
            this.orePrefix = orePrefix.getName();
            this.PREFIX = prefix;
            this.DISPLAY_NAME = DisplayName;
            this.HAS_OVERLAY = overlay;
            // dust + Dirty, Impure, Pure, Refined
            // crushed + centrifuged, purified
        }

        public String getComponent() {
            return this.COMPONENT_NAME;
        }

        public String getOrePrefix() {
            return orePrefix;
        }

        public OrePrefixes getOrePrefixEnum() {
            return orePrefixEnum;
        }

        public String getName() {
            return this.DISPLAY_NAME;
        }

        public boolean hasOverlay() {
            return this.HAS_OVERLAY;
        }

        public String getPrefix() {
            return this.PREFIX;
        }
    }
}
