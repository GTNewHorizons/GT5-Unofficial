package gtPlusPlus.core.item.base.ore;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;

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
        this.unlocalName = componentType.COMPONENT_NAME + MaterialUtils.internalName(material);
        this.materialName = MaterialUtils.localName(material);
        this.componentType = componentType;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(this.unlocalName);
        this.setMaxStackSize(64);
        this.componentColour = MathUtils.getRgbAsHex(MaterialUtils.rgba(material));
        GameRegistry.registerItem(this, this.unlocalName);
        // A material outside the gtpp reconstruction gate (e.g. a base gregtech-declared material that
        // gained this part's shape purely from the gtpp name-merge, such as milled ore for Sphalerite) never
        // skips this constructor -- registering this item into the same oredict name MaterialLib already owns
        // would create a second entry that races the MaterialLib one across launches; keep the item itself
        // (legacy saves/oredict-name lookups still work through MaterialLib), just skip the duplicate
        // association.
        if (!MaterialParts.isCutOver(componentType.getOrePrefixEnum(), material)) {
            GTOreDictUnificator.registerOre(
                componentType.getOrePrefix() + MaterialUtils.internalName(material),
                new ItemStack(this, 1));
        }
    }

    public final String getMaterialName() {
        return this.materialName;
    }

    @Override
    public final void addInformation(final ItemStack stack, final EntityPlayer player, final List<String> tooltip,
        final boolean adv) {
        MaterialUtils.addTooltips(componentMaterial, tooltip);
        super.addInformation(stack, player, tooltip, adv);
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        Integer radiationLevel = componentMaterial.getProperty(GTMaterialProperties.RADIATION_LEVEL);
        EntityUtils.applyRadiationDamageToEntity(
            iStack.stackSize,
            radiationLevel == null ? 0 : radiationLevel,
            world,
            entityHolding);
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
            this.iconBase = par1IconRegister.registerIcon(Mods.GTPlusPlus.ID + ":" + "processing/MilledOre/milled");
            if (this.componentType.hasOverlay()) {
                this.iconOverlay = par1IconRegister
                    .registerIcon(Mods.GTPlusPlus.ID + ":" + "processing/MilledOre/milled_OVERLAY");
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
        if (renderPass == 1) {
            return MathUtils.getRgbAsHex(new short[] { 230, 230, 230, 0 });
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
