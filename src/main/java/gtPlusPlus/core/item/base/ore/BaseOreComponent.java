package gtPlusPlus.core.item.base.ore;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;

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
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseOreComponent extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon base;

    @SideOnly(Side.CLIENT)
    private IIcon overlay;

    public final Material componentMaterial;
    public final String materialName;
    public final String unlocalName;
    public final ComponentTypes componentType;
    public final int componentColour;
    public Object extraData;

    public BaseOreComponent(final Material material, final ComponentTypes componentType) {
        this.componentMaterial = material;
        this.unlocalName = componentType.COMPONENT_NAME + material.getUnlocalizedName();
        this.materialName = material.getLocalizedName();
        this.componentType = componentType;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(this.unlocalName);
        this.setMaxStackSize(64);
        this.componentColour = material.getRgbAsHex();
        GameRegistry.registerItem(this, this.unlocalName);
        registerComponent();
        GTOreDictUnificator
            .registerOre(componentType.getComponent() + material.getUnlocalizedName(), ItemUtils.getSimpleStack(this));
    }

    public boolean registerComponent() {
        Logger.MATERIALS("Attempting to register " + this.getUnlocalizedName() + ".");
        if (this.componentMaterial == null) {
            Logger.MATERIALS("Tried to register " + this.getUnlocalizedName() + " but the material was null.");
            return false;
        }
        // Register Component
        Map<String, ItemStack> aMap = Material.mComponentMap.get(componentMaterial.getUnlocalizedName());
        if (aMap == null) {
            aMap = new HashMap<>();
        }
        String aKey = "Invalid";
        switch (componentType) {
            case CRUSHED -> aKey = OrePrefixes.crushed.name();
            case CRUSHEDCENTRIFUGED -> aKey = OrePrefixes.crushedCentrifuged.name();
            case CRUSHEDPURIFIED -> aKey = OrePrefixes.crushedPurified.name();
            case DUST -> aKey = OrePrefixes.dust.name();
            case DUSTIMPURE -> aKey = OrePrefixes.dustImpure.name();
            case DUSTPURE -> aKey = OrePrefixes.dustPure.name();
            case MILLED -> aKey = OrePrefixes.milled.name();
            case RAWORE -> aKey = OrePrefixes.rawOre.name();
        }

        ItemStack x = aMap.get(aKey);
        if (x == null) {
            aMap.put(aKey, ItemUtils.getSimpleStack(this));
            Logger.MATERIALS(
                "Registering a material component. Item: [" + componentMaterial.getUnlocalizedName()
                    + "] Map: ["
                    + aKey
                    + "]");
            Material.mComponentMap.put(componentMaterial.getUnlocalizedName(), aMap);
            return true;
        } else {
            // Bad
            Logger.MATERIALS("Tried to double register a material component. ");
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list,
        final boolean bool) {
        if (this.materialName != null && !this.materialName.equals("")) {
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
                    list.add(
                        GTPPCore.GT_Tooltip_Radioactive.get() + " | Level: " + this.componentMaterial.vRadiationLevel);
                }
            } else {
                String aChemicalFormula = Material.sChemicalFormula.get(materialName.toLowerCase());
                if (aChemicalFormula != null && aChemicalFormula.length() > 0) {
                    list.add(Utils.sanitizeStringKeepBrackets(aChemicalFormula));
                }
            }
        }
        super.addInformation(stack, aPlayer, list, bool);
    }

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
     * Rendering Related
     *
     * @author Alkalus
     *
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        if (this.componentType.hasOverlay()) {
            return true;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IconRegister) {
        if (this.componentType == ComponentTypes.MILLED) {
            this.base = par1IconRegister.registerIcon(GTPlusPlus.ID + ":" + "processing/MilledOre/milled");
            if (this.componentType.hasOverlay()) {
                this.overlay = par1IconRegister
                    .registerIcon(GTPlusPlus.ID + ":" + "processing/MilledOre/milled_OVERLAY");
            }
        } else if (GTPPCore.ConfigSwitches.useGregtechTextures) {
            // Logger.MATERIALS(this.componentType.getPrefix()+this.componentMaterial.getLocalizedName()+this.componentType.DISPLAY_NAME+"
            // is using `"+GregTech.ID + ":" + "materialicons/METALLIC/" + this.componentType.COMPONENT_NAME+"' as the
            // layer 0 texture path.");
            this.base = par1IconRegister
                .registerIcon(GregTech.ID + ":" + "materialicons/METALLIC/" + this.componentType.COMPONENT_NAME);
            if (this.componentType.hasOverlay()) {
                // Logger.MATERIALS(this.componentType.getPrefix()+this.componentMaterial.getLocalizedName()+this.componentType.DISPLAY_NAME+"
                // is using `"+GregTech.ID + ":" + "materialicons/METALLIC/" +
                // this.componentType.COMPONENT_NAME+"_OVERLAY"+"' as the layer 1 texture path.");
                this.overlay = par1IconRegister.registerIcon(
                    GregTech.ID + ":" + "materialicons/METALLIC/" + this.componentType.COMPONENT_NAME + "_OVERLAY");
            }
        } else {
            this.base = par1IconRegister.registerIcon(GTPlusPlus.ID + ":" + "item" + this.componentType.getComponent());
            if (this.componentType.hasOverlay()) {
                this.overlay = par1IconRegister
                    .registerIcon(GTPlusPlus.ID + ":" + "item" + this.componentType.getComponent() + "_Overlay");
            }
        }
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (this.componentType == ComponentTypes.MILLED) {
            if (renderPass == 1) {
                return Utils.rgbtoHexValue(230, 230, 230);
            }
            return this.componentColour;
        } else {
            if (renderPass == 0 && !GTPPCore.ConfigSwitches.useGregtechTextures) {
                return this.componentColour;
            }
            if (renderPass == 1 && GTPPCore.ConfigSwitches.useGregtechTextures) {
                return Utils.rgbtoHexValue(230, 230, 230);
            }
            return this.componentColour;
        }
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        if (pass == 0) {
            return this.base;
        }
        return this.overlay;
    }

    public static enum ComponentTypes {

        DUST("dust", "", " Dust", true),
        DUSTIMPURE("dustImpure", "Impure ", " Dust", true),
        DUSTPURE("dustPure", "Purified ", " Dust", true),
        CRUSHED("crushed", "Crushed ", " Ore", true),
        CRUSHEDCENTRIFUGED("crushedCentrifuged", "Centrifuged Crushed ", " Ore", true),
        CRUSHEDPURIFIED("crushedPurified", "Purified Crushed ", " Ore", true),
        RAWORE("oreRaw", "Raw ", " Ore", true),
        MILLED("milled", "Milled ", " Ore", true);

        private final String COMPONENT_NAME;
        private final String PREFIX;
        private final String DISPLAY_NAME;
        private final boolean HAS_OVERLAY;

        private ComponentTypes(final String LocalName, final String prefix, final String DisplayName,
            final boolean overlay) {
            this.COMPONENT_NAME = LocalName;
            this.PREFIX = prefix;
            this.DISPLAY_NAME = DisplayName;
            this.HAS_OVERLAY = overlay;
            // dust + Dirty, Impure, Pure, Refined
            // crushed + centrifuged, purified
        }

        public String getComponent() {
            return this.COMPONENT_NAME;
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
