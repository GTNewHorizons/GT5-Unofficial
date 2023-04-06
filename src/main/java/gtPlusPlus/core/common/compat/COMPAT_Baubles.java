package gtPlusPlus.core.common.compat;

import static gregtech.client.GT_TooltipHandler.Tier.*;
import static gregtech.client.GT_TooltipHandler.registerTieredTooltip;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.bauble.BatteryPackBaseBauble;
import gtPlusPlus.core.item.bauble.FireProtectionBauble;
import gtPlusPlus.core.item.bauble.MonsterKillerBaseBauble;
import gtPlusPlus.core.item.general.ItemCloakingDevice;
import gtPlusPlus.core.item.general.ItemHealingDevice;
import gtPlusPlus.core.item.general.ItemSlowBuildingRing;
import gtPlusPlus.core.lib.LoadedMods;

public class COMPAT_Baubles {

    public static void run() {
        if (LoadedMods.Baubles) {
            baublesLoaded();
        } else {
            baublesNotLoaded();
        }
    }

    public static void baublesLoaded() {
        Logger.INFO("Baubles Found - Loading Wearables.");
        ModItems.itemPersonalCloakingDevice = new ItemCloakingDevice(0);
        ModItems.itemPersonalHealingDevice = new ItemHealingDevice();
        ModItems.itemPersonalFireProofDevice = new FireProtectionBauble();

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

        ModItems.itemAmuletMonsterKiller_Zombie = new MonsterKillerBaseBauble(
                new Class[] { EntityZombie.class },
                "Zombie",
                3);
        ModItems.itemAmuletMonsterKiller_Skeleton = new MonsterKillerBaseBauble(
                new Class[] { EntitySkeleton.class },
                "Skeleton",
                3);
        ModItems.itemAmuletMonsterKiller_Spider = new MonsterKillerBaseBauble(
                new Class[] { EntitySpider.class },
                "Spider",
                3);
        ModItems.itemAmuletMonsterKiller_Creeper = new MonsterKillerBaseBauble(
                new Class[] { EntityCreeper.class },
                "Creeper",
                4);
        ModItems.itemAmuletMonsterKiller_Enderman = new MonsterKillerBaseBauble(
                new Class[] { EntityEnderman.class },
                "Enderman",
                4);
        ModItems.itemAmuletMonsterKiller_Nether = new MonsterKillerBaseBauble(
                new Class[] { EntityPigZombie.class, EntityGhast.class, EntityMagmaCube.class, EntityBlaze.class },
                "Hellish",
                6);

        if (LoadedMods.PlayerAPI) {
            ModItems.itemSlowBuildingRing = new ItemSlowBuildingRing();
        }
    }

    public static void baublesNotLoaded() {
        Logger.INFO("Baubles Not Found - Skipping Resources.");
    }
}
