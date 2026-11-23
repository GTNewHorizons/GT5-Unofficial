package gregtech.loaders;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExtraIcons {

    private static final String LARGE_FLUID_CELL_CUSTOM = "large_fluid_cell_custom";
    public static IIcon steelLargeCellInner;
    public static IIcon aluminiumLargeCellInner;
    public static IIcon stainlesssteelLargeCellInner;
    public static IIcon tungstensteelLargeCellInner;
    public static IIcon titaniumLargeCellInner;
    public static IIcon chromiumLargeCellInner;
    public static IIcon iridiumLargeCellInner;
    public static IIcon osmiumLargeCellInner;
    public static IIcon neutroniumLargeCellInner;
    public static IIcon circuitComponentOverlay;

    // Modular Augments
    public static IIcon nightVisionAugment;
    public static IIcon holoInventoryAugment;
    public static IIcon jetpackAugment;
    public static IIcon creativeFlightAugment;
    public static IIcon forceFieldAugment;
    public static IIcon rebreatherAugment;
    public static IIcon revealingAugment;
    public static IIcon hazmatHelmetAugment;
    public static IIcon hazmatChestplateAugment;
    public static IIcon hazmatLeggingsAugment;
    public static IIcon hazmatBootsAugment;
    public static IIcon spaceSuitHelmetAugment;
    public static IIcon spaceSuitChestplateAugment;
    public static IIcon spaceSuitLeggingsAugment;
    public static IIcon spaceSuitBootsAugment;
    public static IIcon soulboundHelmetAugment;
    public static IIcon soulboundChestplateAugment;
    public static IIcon soulboundLeggingsAugment;
    public static IIcon soulboundBootsAugment;
    public static IIcon apiaristHelmetAugment;
    public static IIcon apiaristChestplateAugment;
    public static IIcon apiaristLeggingsAugment;
    public static IIcon apiaristBootsAugment;
    public static IIcon milkInfusionHelmetAugment;
    public static IIcon terrasteelChestplateAugment;
    public static IIcon inertiaCancelingChestplateAugment;
    public static IIcon swimSpeedLeggingsAugment;
    public static IIcon fireImmunityLeggingsAugment;
    public static IIcon knockbackResistanceLeggingsAugment;
    public static IIcon fallProtectionBootsAugment;
    public static IIcon stepAssistBootsAugment;
    public static IIcon speedBoostBootsAugment;
    public static IIcon jumpBoostBootsAugment;
    public static IIcon omniMovementBootsAugment;
    public static IIcon apprenticeStridersBootsAugment;
    public static IIcon archmageStridersBootsAugment;
    public static IIcon eldritchStridersBootsAugment;

    @SubscribeEvent
    public void regIcons(TextureStitchEvent.Pre event) {
        TextureMap reg = event.map;
        if (reg.getTextureType() == 1) { // are for items
            steelLargeCellInner = reg.registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "steel_inner"));
            aluminiumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "aluminium_inner"));
            stainlesssteelLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "stainlesssteel_inner"));
            tungstensteelLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "tungstensteel_inner"));
            titaniumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "titanium_inner"));
            chromiumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "chromium_inner"));
            iridiumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "iridium_inner"));
            osmiumLargeCellInner = reg.registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "osmium_inner"));
            neutroniumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "neutronium_inner"));
            // Modular Augments
            nightVisionAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_nightvision"));
            rebreatherAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_rebreather"));
            revealingAugment = reg.registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_revealing"));
            holoInventoryAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_holoinventory"));
            jetpackAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_jetpack"));
            creativeFlightAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_creativeflight"));
            forceFieldAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_forcefield"));
            hazmatHelmetAugment = reg.registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_hazmat"));
            hazmatChestplateAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_hazmat"));
            hazmatLeggingsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/leggings/leggings_augment_hazmat"));
            hazmatBootsAugment = reg.registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_hazmat"));
            spaceSuitHelmetAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_spacesuit"));
            spaceSuitChestplateAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_spacesuit"));
            spaceSuitLeggingsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/leggings/leggings_augment_spacesuit"));
            spaceSuitBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_spacesuit"));
            soulboundHelmetAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_soulbound"));
            soulboundChestplateAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_soulbound"));
            soulboundLeggingsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/leggings/leggings_augment_soulbound"));
            soulboundBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_soulbound"));
            apiaristHelmetAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_apiarist"));
            apiaristChestplateAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_apiarist"));
            apiaristLeggingsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/leggings/leggings_augment_apiarist"));
            apiaristBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_apiarist"));
            milkInfusionHelmetAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/helmet/helmet_augment_milkinfusion"));
            terrasteelChestplateAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_terrasteel"));
            inertiaCancelingChestplateAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/chestplate/chestplate_augment_inertiacanceling"));
            swimSpeedLeggingsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/leggings/leggings_augment_swimspeed"));
            fireImmunityLeggingsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/leggings/leggings_augment_fireimmunity"));
            knockbackResistanceLeggingsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/leggings/leggings_augment_knockbackresistance"));
            fallProtectionBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_fallprotection"));
            stepAssistBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_stepassist"));
            speedBoostBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_speedboost"));
            jumpBoostBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_jumpboost"));
            omniMovementBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_omnimovement"));
            apprenticeStridersBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_apprenticestriders"));
            archmageStridersBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_archmagestriders"));
            eldritchStridersBootsAugment = reg
                .registerIcon(GregTech.getResourcePath("mech_armor/boots/boots_augment_eldritchstriders"));
            circuitComponentOverlay = reg
                .registerIcon(GregTech.getResourcePath("gt.circuitcomponent", "circuit_component_overlay"));
        }
    }
}
