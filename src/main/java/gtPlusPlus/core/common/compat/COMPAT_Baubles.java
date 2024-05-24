package gtPlusPlus.core.common.compat;

import static gregtech.api.enums.Mods.Baubles;
import static gregtech.client.GT_TooltipHandler.Tier.EV;
import static gregtech.client.GT_TooltipHandler.Tier.HV;
import static gregtech.client.GT_TooltipHandler.Tier.IV;
import static gregtech.client.GT_TooltipHandler.Tier.LV;
import static gregtech.client.GT_TooltipHandler.Tier.LuV;
import static gregtech.client.GT_TooltipHandler.Tier.MV;
import static gregtech.client.GT_TooltipHandler.Tier.UHV;
import static gregtech.client.GT_TooltipHandler.Tier.UV;
import static gregtech.client.GT_TooltipHandler.Tier.ZPM;
import static gregtech.client.GT_TooltipHandler.registerTieredTooltip;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.bauble.BatteryPackBaseBauble;
import gtPlusPlus.core.item.bauble.FireProtectionBauble;
import gtPlusPlus.core.item.general.ItemCloakingDevice;
import gtPlusPlus.core.item.general.ItemHealingDevice;

public class COMPAT_Baubles {

    public static void run() {
        if (Baubles.isModLoaded()) {
            baublesLoaded();
        }
    }

    public static void baublesLoaded() {
        Logger.INFO("Baubles Found - Loading Wearables.");
        ModItems.itemPersonalCloakingDevice = new ItemCloakingDevice(0);
        ModItems.itemPersonalHealingDevice = new ItemHealingDevice();
        ModItems.itemSupremePizzaGloves = new FireProtectionBauble();

        ModItems.itemChargePack_Low_1 = new BatteryPackBaseBauble(1);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_Low_1, 1, OreDictionary.WILDCARD_VALUE), LV);
        ModItems.itemChargePack_Low_2 = new BatteryPackBaseBauble(2);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_Low_2, 1, OreDictionary.WILDCARD_VALUE), MV);
        ModItems.itemChargePack_Low_3 = new BatteryPackBaseBauble(3);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_Low_3, 1, OreDictionary.WILDCARD_VALUE), HV);
        ModItems.itemChargePack_Low_4 = new BatteryPackBaseBauble(4);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_Low_4, 1, OreDictionary.WILDCARD_VALUE), EV);
        ModItems.itemChargePack_Low_5 = new BatteryPackBaseBauble(5);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_Low_5, 1, OreDictionary.WILDCARD_VALUE), IV);
        ModItems.itemChargePack_High_1 = new BatteryPackBaseBauble(6);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_High_1, 1, OreDictionary.WILDCARD_VALUE), LuV);
        ModItems.itemChargePack_High_2 = new BatteryPackBaseBauble(7);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_High_2, 1, OreDictionary.WILDCARD_VALUE), ZPM);
        ModItems.itemChargePack_High_3 = new BatteryPackBaseBauble(8);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_High_3, 1, OreDictionary.WILDCARD_VALUE), UV);
        ModItems.itemChargePack_High_4 = new BatteryPackBaseBauble(9);
        registerTieredTooltip(new ItemStack(ModItems.itemChargePack_High_4, 1, OreDictionary.WILDCARD_VALUE), UHV);
    }
}
