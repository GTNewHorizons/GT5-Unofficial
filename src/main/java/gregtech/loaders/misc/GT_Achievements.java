package gregtech.loaders.misc;

import static gregtech.api.enums.Mods.Thaumcraft;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.items.ID_MetaTool_01;
import ic2.core.Ic2Items;
import thaumcraft.api.ThaumcraftApiHelper;

public class GT_Achievements {

    public static int oreReg = -1;
    public static int assReg = -1;
    public ConcurrentHashMap<String, Achievement> achievementList;
    public ConcurrentHashMap<String, Boolean> issuedAchievements;
    public int adjX = 5;
    public int adjY = 9;

    public GT_Achievements() {
        this.achievementList = new ConcurrentHashMap<>();
        this.issuedAchievements = new ConcurrentHashMap<>();

        for (GT_Recipe recipe : RecipeMaps.assemblylineVisualRecipes.getAllRecipes()) {
            registerAssAchievement(recipe);
        }

        registerAchievement(
            "flintpick",
            0,
            0,
            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(2, 1, Materials.Flint, Materials.Wood, null),
            "",
            false);
        registerAchievement("crops", -4, 0, GT_ModHandler.getIC2Item("crop", 1L), "flintpick", false);
        registerAchievement("havestlead", -4, 2, ItemList.Crop_Drop_Plumbilia.get(1), "crops", false);
        registerAchievement("havestcopper", -2, 1, ItemList.Crop_Drop_Coppon.get(1), "crops", false);
        registerAchievement("havesttin", -2, -1, ItemList.Crop_Drop_Tine.get(1), "crops", false);
        registerAchievement("havestoil", -4, -4, ItemList.Crop_Drop_OilBerry.get(1), "crops", false);
        registerAchievement("havestiron", -2, -3, ItemList.Crop_Drop_Ferru.get(1), "crops", false);
        registerAchievement("havestgold", -2, -6, ItemList.Crop_Drop_Aurelia.get(1), "havestiron", false);
        registerAchievement("havestsilver", -4, -5, ItemList.Crop_Drop_Argentia.get(1), "havestiron", false);
        registerAchievement("havestemeralds", -2, -8, ItemList.Crop_Drop_BobsYerUncleRanks.get(1), "havestgold", false);

        registerAchievement(
            "tools",
            0,
            4,
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(ID_MetaTool_01.HARDHAMMER.ID, 1, Materials.Iron, Materials.Wood, null),
            "flintpick",
            false);
        registerAchievement(
            "driltime",
            2,
            4,
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(ID_MetaTool_01.DRILL_LV.ID, 1, Materials.BlueSteel, Materials.StainlessSteel, null),
            "tools",
            false);
        registerAchievement(
            "brrrr",
            2,
            6,
            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                ID_MetaTool_01.CHAINSAW_LV.ID,
                1,
                Materials.BlueSteel,
                Materials.StainlessSteel,
                null),
            "driltime",
            false);
        registerAchievement(
            "highpowerdrill",
            3,
            5,
            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                ID_MetaTool_01.DRILL_HV.ID,
                1,
                Materials.TungstenSteel,
                Materials.TungstenSteel,
                null),
            "driltime",
            false);
        registerAchievement(
            "hammertime",
            3,
            7,
            GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                ID_MetaTool_01.JACKHAMMER.ID,
                1,
                Materials.TungstenSteel,
                Materials.TungstenSteel,
                null),
            "highpowerdrill",
            false);

        registerAchievement(
            "unitool",
            -2,
            4,
            GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(ID_MetaTool_01.UNIVERSALSPADE.ID, 1, Materials.Steel, Materials.Iron, null),
            "tools",
            false);
        registerAchievement("recycling", -4, 4, ItemList.Machine_LV_ArcFurnace.get(1), "unitool", false);

        registerAchievement(
            "crushed",
            0,
            6,
            GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Tin, 1L),
            "tools",
            false);
        registerAchievement(
            "cleandust",
            0,
            10,
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L),
            "crushed",
            false);
        registerAchievement(
            "washing",
            -2,
            6,
            GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Iron, 1L),
            "crushed",
            false);
        registerAchievement(
            "spinit",
            -4,
            6,
            GT_OreDictUnificator.get(OrePrefixes.crushedCentrifuged, Materials.Redstone, 1L),
            "crushed",
            false);

        registerAchievement("newfuel", -4, 8, ItemList.ThoriumCell_4.get(1), "spinit", false);
        registerAchievement(
            "newmetal",
            -4,
            10,
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lutetium, 1L),
            "newfuel",
            false);
        registerAchievement("reflect", -2, 9, ItemList.Neutron_Reflector.get(1), "newfuel", false);

        registerAchievement(
            "bronze",
            2,
            0,
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bronze, 1L),
            "flintpick",
            false);
        registerAchievement(
            "simplyeco",
            2,
            2,
            ItemList.Machine_Bronze_Boiler_Solar.get(1, ItemList.Machine_HP_Solar.get(1L)),
            "bronze",
            false);
        registerAchievement("firststeam", 2, -2, ItemList.Machine_Bronze_Boiler.get(1), "bronze", false);
        registerAchievement("alloysmelter", 2, -4, ItemList.Machine_Bronze_AlloySmelter.get(1), "firststeam", false);
        registerAchievement("macerator", 0, -2, ItemList.Machine_Bronze_Macerator.get(1), "firststeam", false);
        registerAchievement("extract", 0, -4, ItemList.Machine_Bronze_Extractor.get(1), "alloysmelter", false);

        registerAchievement("smallparts", 0, -5, ItemList.Circuit_Primitive.get(1), "alloysmelter", false);
        registerAchievement("gtbasiccircuit", 0, -8, Ic2Items.electronicCircuit.copy(), "smallparts", false);
        registerAchievement("bettercircuits", 0, -9, ItemList.Circuit_Good.get(1), "gtbasiccircuit", false);
        registerAchievement("stepforward", -2, -9, Ic2Items.advancedCircuit.copy(), "bettercircuits", false);
        registerAchievement("gtmonosilicon", -5, -10, ItemList.Circuit_Silicon_Ingot.get(1), "stepforward", false);
        registerAchievement("gtlogicwafer", -7, -10, ItemList.Circuit_Wafer_ILC.get(1), "gtmonosilicon", false);
        registerAchievement("gtlogiccircuit", -9, -10, ItemList.Circuit_Basic.get(1), "gtlogicwafer", false);
        registerAchievement("gtcleanroom", -11, -10, ItemList.Machine_Multi_Cleanroom.get(1), "gtlogiccircuit", false);
        registerAchievement("energyflow", -13, -10, ItemList.Circuit_Nanoprocessor.get(1), "gtcleanroom", false);
        registerAchievement(
            "gtquantumprocessor",
            -13,
            -12,
            ItemList.Circuit_Quantumprocessor.get(1),
            "energyflow",
            false);
        registerAchievement(
            "gtcrystalprocessor",
            -11,
            -12,
            ItemList.Circuit_Crystalprocessor.get(1),
            "gtquantumprocessor",
            false);
        registerAchievement("gtwetware", -9, -12, ItemList.Circuit_Neuroprocessor.get(1), "gtcrystalprocessor", false);
        registerAchievement("gtwetmain", -7, -12, ItemList.Circuit_Wetwaremainframe.get(1), "gtwetware", false);

        registerAchievement("orbs", -10, -14, ItemList.Energy_LapotronicOrb.get(1), "energyflow", false);
        registerAchievement("thatspower", -8, -14, ItemList.Energy_LapotronicOrb2.get(1), "orbs", false);
        registerAchievement("datasaving", -2, -12, ItemList.Tool_DataOrb.get(1), "stepforward", false);
        registerAchievement("superbuffer", 0, -12, ItemList.Automation_SuperBuffer_LV.get(1), "datasaving", false);
        registerAchievement("newstorage", -2, -14, ItemList.Quantum_Chest_HV.get(1), "superbuffer", false);
        registerAchievement("whereistheocean", 2, -14, ItemList.Quantum_Tank_IV.get(1), "superbuffer", false);
        registerAchievement("luck", 2, -6, ItemList.ZPM.get(1), "", false);

        registerAchievement(
            "steel",
            4,
            0,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L),
            "bronze",
            false);
        registerAchievement("highpressure", 4, 2, ItemList.Machine_Steel_Boiler.get(1), "steel", false);
        registerAchievement(
            "extremepressure",
            4,
            4,
            ItemList.Machine_Multi_LargeBoiler_Steel.get(1),
            "highpressure",
            false);
        registerAchievement("cheapermac", 6, 1, ItemList.Machine_LV_Hammer.get(1), "steel", false);
        registerAchievement(
            "complexalloys",
            6,
            3,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.BlueSteel, 1L),
            "cheapermac",
            false);

        registerAchievement(
            "magneticiron",
            4,
            -2,
            GT_OreDictUnificator.get(OrePrefixes.stick, Materials.IronMagnetic, 1L),
            "steel",
            false);
        registerAchievement("lvmotor", 4, -6, ItemList.Electric_Motor_LV.get(1), "magneticiron", false);
        registerAchievement("pumpcover", 2, -8, ItemList.Electric_Pump_LV.get(1), "lvmotor", false);
        registerAchievement("closeit", 2, -10, ItemList.Cover_Shutter.get(1), "pumpcover", false);
        registerAchievement("slurp", 2, -12, ItemList.Pump_HV.get(1), "closeit", false);
        registerAchievement("transport", 4, -10, ItemList.Conveyor_Module_LV.get(1), "lvmotor", false);
        registerAchievement("manipulation", 4, -12, ItemList.Cover_Controller.get(1), "transport", false);
        registerAchievement("buffer", 4, -14, ItemList.Automation_ChestBuffer_LV.get(1), "manipulation", false);
        registerAchievement("complexmachines", 6, -9, ItemList.Robot_Arm_LV.get(1), "lvmotor", false);
        registerAchievement("avengers", 8, -11, ItemList.Machine_LV_Assembler.get(1), "complexmachines", false);
        registerAchievement("filterregulate", 10, -11, ItemList.Component_Filter.get(1), "avengers", false);

        registerAchievement("steampower", 6, -6, ItemList.Generator_Steam_Turbine_LV.get(1), "lvmotor", false);
        registerAchievement("batterys", 6, -4, ItemList.Battery_Buffer_2by2_MV.get(1), "steampower", false);
        registerAchievement("badweather", 6, -8, ItemList.Casing_FireHazard.get(1), "steampower", false);
        registerAchievement("electricproblems", 7, -7, ItemList.Casing_ExplosionHazard.get(1), "steampower", false);
        registerAchievement("ebf", 8, -6, ItemList.Machine_Multi_BlastFurnace.get(1), "steampower", false);
        registerAchievement("energyhatch", 12, -6, ItemList.Hatch_Energy_LV.get(1), "ebf", false);

        registerAchievement(
            "gtaluminium",
            8,
            0,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Aluminium, 1L),
            "steel",
            false);
        registerAchievement("highpowersmelt", 8, 2, ItemList.Machine_Multi_Furnace.get(1), "gtaluminium", false);
        registerAchievement("oilplant", 8, 4, ItemList.Distillation_Tower.get(1), "highpowersmelt", false);
        registerAchievement("factory", 8, 6, ItemList.Processing_Array.get(1), "oilplant", false);
        registerAchievement("upgradeebf", 8, -2, ItemList.Hatch_Energy_MV.get(1), "gtaluminium", false);
        registerAchievement("maintainance", 10, -2, ItemList.Hatch_Maintenance.get(1), "upgradeebf", false);

        registerAchievement("upgrade", 10, 0, ItemList.Casing_Coil_Kanthal.get(1), "gtaluminium", false);
        registerAchievement(
            "titan",
            14,
            0,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 1L),
            "upgrade",
            false);
        registerAchievement("magic", 14, 3, ItemList.MagicEnergyConverter_LV.get(1), "titan", false);
        registerAchievement("highmage", 10, 3, ItemList.MagicEnergyAbsorber_HV.get(1), "magic", false);
        registerAchievement("artificaldia", 12, 2, ItemList.IC2_Industrial_Diamond.get(1), "titan", false);
        registerAchievement("muchsteam", 13, 1, ItemList.LargeSteamTurbine.get(1), "titan", false);
        registerAchievement("efficientsteam", 11, 1, ItemList.LargeSteamTurbine.get(1), "muchsteam", false);

        registerAchievement("upgrade2", 16, 0, ItemList.Casing_Coil_Nichrome.get(1), "titan", false);
        registerAchievement(
            "tungsten",
            16,
            2,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tungsten, 1L),
            "upgrade2",
            false);
        registerAchievement(
            "osmium",
            16,
            -2,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Osmium, 1L),
            "upgrade2",
            false);
        registerAchievement("hightech", 15, -3, ItemList.Field_Generator_LV.get(1), "osmium", false);
        registerAchievement("amplifier", 16, -5, ItemList.Machine_LV_Amplifab.get(1), "hightech", false);
        registerAchievement("scanning", 13, -3, ItemList.Machine_HV_Scanner.get(1), "hightech", false);
        registerAchievement("alienpower", 14, -5, ItemList.Generator_Naquadah_Mark_I.get(1), "hightech", false);
        registerAchievement("universal", 15, -6, ItemList.Machine_LV_Massfab.get(1), "hightech", false);
        registerAchievement("replication", 17, -6, ItemList.Machine_LV_Replicator.get(1), "universal", false);

        registerAchievement(
            "tungstensteel",
            16,
            4,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L),
            "tungsten",
            false);
        registerAchievement("upgrade3", 15, 5, ItemList.Casing_Coil_TungstenSteel.get(1), "tungstensteel", false);
        registerAchievement(
            "hssg",
            13,
            5,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSG, 1L),
            "upgrade3",
            false);
        registerAchievement("upgrade4", 11, 5, ItemList.Casing_Coil_HSSG.get(1), "hssg", false);
        registerAchievement(
            "stargatematerial",
            11,
            7,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1L),
            "upgrade4",
            false);
        registerAchievement("conducting", 14, 6, ItemList.Casing_Coil_Superconductor.get(1), "upgrade3", false);
        registerAchievement("fusion", 15, 7, ItemList.FusionComputer_LuV.get(1), "tungstensteel", false);
        registerAchievement("higherefficency", 15, 9, ItemList.Generator_Plasma_EV.get(1), "fusion", false);
        registerAchievement("advancing", 13, 7, ItemList.FusionComputer_ZPMV.get(1), "fusion", false);

        registerAchievement("stargateliquid", 11, 9, ItemList.Generator_Plasma_IV.get(1), "advancing", false);
        registerAchievement("tothelimit", 13, 9, ItemList.Generator_Plasma_IV.get(1), "advancing", false);
        registerAchievement("fullefficiency", 12, 10, ItemList.Generator_Plasma_LuV.get(1), "tothelimit", false);
        registerAchievement("upgrade5", 9, 9, ItemList.Casing_Coil_Naquadah.get(1), "stargateliquid", false);
        registerAchievement(
            "alienmetallurgy",
            9,
            7,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.NaquadahAlloy, 1L),
            "upgrade5",
            false);
        registerAchievement("over9000", 7, 7, ItemList.Casing_Coil_NaquadahAlloy.get(1), "alienmetallurgy", false);
        registerAchievement(
            "finalpreparations",
            7,
            9,
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadria, 1L),
            "over9000",
            false);
        registerAchievement("denseaspossible", 6, 10, ItemList.FusionComputer_UV.get(1), "finalpreparations", false);
        if (GregTech_API.sOPStuff.get(ConfigCategories.Recipes.gregtechrecipes, "EnableZPMandUVBatteries", false)) {
            registerAchievement("zpmage", 8, 10, ItemList.Energy_Module.get(1), "denseaspossible", false);
            registerAchievement("uvage", 10, 10, ItemList.Energy_Cluster.get(1), "zpmage", false);
            registerAchievement("whatnow", 12, 10, ItemList.ZPM2.get(1), "uvage", false);
        } else {
            registerAchievement("whatnow", 8, 10, ItemList.ZPM2.get(1), "denseaspossible", false);
        }

        if (GT_Mod.gregtechproxy.mAchievements) {
            AchievementPage.registerAchievementPage(
                new AchievementPage(
                    "GregTech 5",
                    this.achievementList.values()
                        .toArray(new Achievement[0])));
            MinecraftForge.EVENT_BUS.register(this);
            FMLCommonHandler.instance()
                .bus()
                .register(this);
        }
    }

    public static void registerOre(Materials aMaterial, int min, int max, int chance, boolean overworld, boolean nether,
        boolean end) {}

    public Achievement registerAchievement(String textId, int x, int y, ItemStack icon, Achievement requirement,
        boolean special) {
        if (!GT_Mod.gregtechproxy.mAchievements) {
            return null;
        }
        Achievement achievement = new Achievement(textId, textId, this.adjX + x, this.adjY + y, icon, requirement);
        if (special) {
            achievement.setSpecial();
        }
        ((StatBase) achievement).registerStat();
        if (GT_Values.D2) {
            GT_Log.out.println("achievement." + textId + "=");
            GT_Log.out.println("achievement." + textId + ".desc=");
        }
        this.achievementList.put(textId, achievement);
        return achievement;
    }

    public Achievement registerAchievement(String textId, int x, int y, ItemStack icon, String requirement,
        boolean special) {
        if (!GT_Mod.gregtechproxy.mAchievements) {
            return null;
        }
        Achievement achievement = new Achievement(
            textId,
            textId,
            this.adjX + x,
            this.adjY + y,
            icon,
            getAchievement(requirement));
        if (special) {
            achievement.setSpecial();
        }
        ((StatBase) achievement).registerStat();
        if (GT_Values.D2) {
            GT_Log.out.println("achievement." + textId + "=");
            GT_Log.out.println("achievement." + textId + ".desc=");
        }
        this.achievementList.put(textId, achievement);
        return achievement;
    }

    public Achievement registerOreAchievement(Materials aMaterial) {
        return null;
    }

    public Achievement registerAssAchievement(GT_Recipe recipe) {
        if (recipe == null) {
            GT_Mod.GT_FML_LOGGER.error("Invalid achievement registration attempt for null recipe", new Exception());
            return null;
        }
        if (recipe.getOutput(0) == null) {
            GT_Mod.GT_FML_LOGGER
                .error("Invalid achievement registration attempt for recipe with null output", new Exception());
            return null;
        }
        if (this.achievementList.get(
            recipe.getOutput(0)
                .getUnlocalizedName())
            == null) {
            assReg++;
            return registerAchievement(
                recipe.getOutput(0)
                    .getUnlocalizedName(),
                -(11 + assReg % 5),
                ((assReg) / 5) - 8,
                recipe.getOutput(0),
                AchievementList.openInventory,
                false);
        }
        return null;
    }

    public void issueAchievement(EntityPlayer entityplayer, String textId) {
        if (entityplayer == null || !GT_Mod.gregtechproxy.mAchievements) {
            return;
        }
        entityplayer.triggerAchievement(this.achievementList.get(textId));
    }

    public Achievement getAchievement(String textId) {
        if (this.achievementList.containsKey(textId)) {
            return this.achievementList.get(textId);
        }
        return null;
    }

    public void issueAchivementHatch(EntityPlayer player, ItemStack stack) {
        if (player == null || stack == null) {
            return;
        }
        ItemData data = GT_OreDictUnificator.getItemData(stack);

        if ((data != null) && (data.mPrefix == OrePrefixes.ingot)) {
            if (data.mMaterial.mMaterial == Materials.Aluminium) {
                issueAchievement(player, "gtaluminium");
            } else if (data.mMaterial.mMaterial == Materials.Titanium) {
                issueAchievement(player, "titan");
            } else if (data.mMaterial.mMaterial == Materials.BlueSteel) {
                issueAchievement(player, "complexalloys");
            } else if (data.mMaterial.mMaterial == Materials.Tungsten) {
                issueAchievement(player, "tungsten");
            } else if (data.mMaterial.mMaterial == Materials.Osmium) {
                issueAchievement(player, "osmium");
            } else if (data.mMaterial.mMaterial == Materials.TungstenSteel) {
                issueAchievement(player, "tungstensteel");
            } else if (data.mMaterial.mMaterial == Materials.HSSG) {
                issueAchievement(player, "hssg");
            } else if (data.mMaterial.mMaterial == Materials.Naquadah) {
                issueAchievement(player, "stargatematerial");
            } else if (data.mMaterial.mMaterial == Materials.NaquadahAlloy) {
                issueAchievement(player, "alienmetallurgy");
            } else if (data.mMaterial.mMaterial == Materials.Naquadria) {
                issueAchievement(player, "finalpreparations");
            }
        }
        if (stack.getUnlocalizedName()
            .equals("ic2.itemPartIndustrialDiamond")) {
            issueAchievement(player, "artificaldia");
            issueAchievement(player, "buildCoalDiamond");
        }
    }

    public void issueAchivementHatchFluid(EntityPlayer player, FluidStack fluid) {
        if (player == null || fluid == null) {
            return;
        }
        switch (fluid.getFluid()
            .getUnlocalizedName()) {
            case "fluid.plasma.helium" -> issueAchievement(player, "fusion");
            case "fluid.molten.europium" -> issueAchievement(player, "advancing");
            case "fluid.molten.naquadah" -> issueAchievement(player, "stargateliquid");
            case "fluid.molten.americium" -> issueAchievement(player, "tothelimit");
            case "fluid.molten.neutronium" -> issueAchievement(player, "denseaspossible");
            case "fluid.plasma.nitrogen" -> issueAchievement(player, "higherefficency");
        }
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        ItemStack stack = event.crafting;
        if (player == null || stack == null) {
            return;
        }
        ItemData data = GT_OreDictUnificator.getItemData(stack);
        if (data != null) {
            if (data.mPrefix == OrePrefixes.dust && data.mMaterial.mMaterial == Materials.Bronze) {
                issueAchievement(player, "bronze");
            }
        }
        switch (stack.getUnlocalizedName()) {
            case "gt.metaitem.01.2300" -> issueAchievement(player, "bronze");
            case "gt.metaitem.01.32700" -> issueAchievement(player, "smallparts");
            case "gt.metaitem.01.32702" -> issueAchievement(player, "bettercircuits");
            case "gt.metaitem.01.23354" -> issueAchievement(player, "magneticiron");
            case "gt.metaitem.01.32600" -> {
                issueAchievement(player, "lvmotor");
                issueAchievement(player, "buildCable");
            }
            case "gt.metaitem.01.32610" -> issueAchievement(player, "pumpcover");
            case "gt.metaitem.01.32630" -> issueAchievement(player, "transport");
            case "gt.metaitem.01.32650" -> issueAchievement(player, "complexmachines");
            case "gt.metaitem.01.32670" -> issueAchievement(player, "hightech");
            case "ic2.blockCrop" -> issueAchievement(player, "crops");
            case "ic2.itemPartCircuit" -> issueAchievement(player, "gtbasiccircuit");
            case "ic2.itemPartCircuitAdv" -> issueAchievement(player, "stepforward");
            case "gt.blockmachines.boiler.solar" -> issueAchievement(player, "simplyeco");
            case "gt.blockmachines.boiler.bronze" -> issueAchievement(player, "firststeam");
            case "gt.blockmachines.boiler.steel" -> issueAchievement(player, "highpressure");
            case "gt.blockmachines.bronzemachine.macerator" -> {
                issueAchievement(player, "macerator");
                issueAchievement(player, "buildMacerator");
            }
            case "gt.blockmachines.bronzemachine.alloysmelter.tier.3" -> issueAchievement(player, "buildIndFurnace");
            case "gt.blockmachines.bronzemachine.alloysmelter" -> {
                issueAchievement(player, "alloysmelter");
                issueAchievement(player, "buildElecFurnace");
            }

            case "gt.blockmachines.bronzemachine.extractor" -> {
                issueAchievement(player, "extract");
                issueAchievement(player, "buildCompressor");
                issueAchievement(player, "buildExtractor");
            }
            case "gt.blockmachines.basicmachine.pump.tier.03" -> issueAchievement(player, "slurp");
            case "gt.blockmachines.multimachine.blastfurnace" -> issueAchievement(player, "ebf");
            case "gt.blockmachines.hatch.energy.tier.02" -> issueAchievement(player, "upgradeebf");
            case "gt.blockmachines.multimachine.multifurnace" -> issueAchievement(player, "highpowersmelt");
            case "gt.blockmachines.hatch.energy.tier.01" -> issueAchievement(player, "energyhatch");
            case "gt.blockmachines.multimachine.processingarray" -> issueAchievement(player, "factory");
            case "gt.blockmachines.basicgenerator.magicenergyconverter.tier.01" -> issueAchievement(player, "magic");
            case "gt.blockmachines.basicgenerator.magicenergyabsorber.tier.03" -> issueAchievement(player, "highmage");
            case "gt.blockmachines.basicgenerator.plasmagenerator.tier.07" -> issueAchievement(
                player,
                "fullefficiency");
            case "gt.blockmachines.multimachine.largeturbine" -> issueAchievement(player, "muchsteam");
            case "gt.blockmachines.multimachine.largehpturbine" -> issueAchievement(player, "efficientsteam");
            case "gt.blockmachines.multimachine.cleanroom" -> issueAchievement(player, "gtcleanroom");
            case "gt.neutronreflector" -> issueAchievement(player, "reflect");
            case "gt.blockcasings5.1" -> issueAchievement(player, "upgrade");
            case "gt.blockcasings5.2" -> issueAchievement(player, "upgrade2");
            case "gt.blockcasings5.3" -> issueAchievement(player, "upgrade3");
            case "gt.blockcasings5.4" -> issueAchievement(player, "upgrade4");
            case "gt.blockcasings5.5" -> issueAchievement(player, "upgrade5");
            case "gt.blockcasings5.6" -> issueAchievement(player, "over9000");
            case "gt.blockcasings.15" -> issueAchievement(player, "conducting");
        }

        if (!stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.")) {
            return;
        }

        // from here, it only concerns "gt.blockmachines." kind of achievements
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicmachine.arcfurnace.tier.")) {
            issueAchievement(player, "recycling");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicmachine.disassembler.tier.")) {
            issueAchievement(player, "repair");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.automation.superbuffer.tier.")) {
            issueAchievement(player, "superbuffer");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.quantum.tank.tier.")) {
            issueAchievement(player, "whereistheocean");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.quantum.chest.tier.")) {
            issueAchievement(player, "newstorage");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicmachine.hammer.tier.")) {
            issueAchievement(player, "cheapermac");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.automation.chestbuffer.tier.")) {
            issueAchievement(player, "buffer");
            issueAchievement(player, "buildBatBox");
            if (stack.getUnlocalizedName()
                .startsWith("gt.blockmachines.automation.chestbuffer.tier.3")) {
                issueAchievement(player, "buildMFE");
            }
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicgenerator.steamturbine.tier.")) {
            issueAchievement(player, "steampower");
            issueAchievement(player, "buildGenerator");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicmachine.assembler.tier.")) {
            issueAchievement(player, "avengers");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.batterybuffer.")) {
            issueAchievement(player, "batterys");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicmachine.amplifab.tier.")) {
            issueAchievement(player, "amplifier");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicmachine.massfab.tier.")) {
            issueAchievement(player, "universal");
            issueAchievement(player, "buildMassFab");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicgenerator.naquadah.tier.")) {
            issueAchievement(player, "alienpower");
            return;
        }
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockmachines.basicmachine.replicator.tier.")) {
            issueAchievement(player, "replication");
            return;
        }

    }

    @SubscribeEvent
    public void onSmelting(ItemSmeltedEvent event) {
        EntityPlayer player = event.player;
        ItemStack stack = event.smelting;
        if (player == null || stack == null) {
            return;
        }
        if (stack.getItem() == Items.bread) {
            event.player.triggerAchievement(AchievementList.makeBread);
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack stack = event.item.getEntityItem();
        if (player == null || stack == null) {
            return;
        }
        ItemData data = GT_OreDictUnificator.getItemData(stack);
        if (data != null && data.mPrefix != null) {
            if (data.mPrefix == OrePrefixes.dust) {
                if (data.mMaterial.mMaterial == Materials.Lutetium) {
                    issueAchievement(player, "newmetal");
                }
                if (data.mMaterial.mMaterial != Materials.Gunpowder) {
                    issueAchievement(player, "cleandust");
                }
            } else if (data.mPrefix.name()
                .startsWith("ore")) {
                    int data_getAllMaterialStacks_sS = data.getAllMaterialStacks()
                        .size();
                    for (int i = 0; i < data_getAllMaterialStacks_sS; i++) {
                        issueAchievement(
                            player,
                            data.getAllMaterialStacks()
                                .get(i).mMaterial.mName);
                        if (data.getAllMaterialStacks()
                            .get(i).mMaterial == Materials.AnyIron) {
                            issueAchievement(player, "iron");
                        }
                        if (data.getAllMaterialStacks()
                            .get(i).mMaterial == Materials.Copper
                            || data.getAllMaterialStacks()
                                .get(i).mMaterial == Materials.Tin) {
                            issueAchievement(event.entityPlayer, "mineOre");
                        }
                    }
                } else if (data.mPrefix == OrePrefixes.crushed) {
                    issueAchievement(player, "crushed");
                } else if (data.mPrefix == OrePrefixes.crushedPurified) {
                    issueAchievement(player, "washing");
                } else if (data.mPrefix == OrePrefixes.crushedCentrifuged) {
                    issueAchievement(player, "spinit");
                } else if (data.mMaterial.mMaterial == Materials.Steel) {
                    if (data.mPrefix == OrePrefixes.ingot && stack.stackSize == stack.getMaxStackSize()) {
                        issueAchievement(player, "steel");
                    } else if (data.mPrefix == OrePrefixes.nugget && Thaumcraft.isModLoaded()
                        && ThaumcraftApiHelper.isResearchComplete(player.getDisplayName(), "GT_IRON_TO_STEEL")) {
                            issueAchievement(player, "steel");
                        }
                }
        }
        // GT_FML_LOGGER.info(stack.getUnlocalizedName());
        switch (stack.getUnlocalizedName()) {
            case "gt.metaitem.02.32500" -> issueAchievement(player, "havestlead");
            case "gt.metaitem.02.32501" -> issueAchievement(player, "havestsilver");
            case "gt.metaitem.02.32503" -> issueAchievement(player, "havestiron");
            case "gt.metaitem.02.32504" -> issueAchievement(player, "havestgold");
            case "gt.metaitem.02.32530" -> issueAchievement(player, "havestcopper");
            case "gt.metaitem.02.32540" -> issueAchievement(player, "havesttin");
            case "gt.metaitem.02.32510" -> issueAchievement(player, "havestoil");
            case "gt.metaitem.02.32511" -> issueAchievement(player, "havestemeralds");
            case "gt.metaitem.03.32082" -> issueAchievement(player, "energyflow");
            case "gt.metaitem.01.32702" -> issueAchievement(player, "bettercircuits");
            case "gt.metaitem.01.32707" -> issueAchievement(player, "datasaving");
            case "gt.metaitem.01.32597" -> issueAchievement(player, "orbs");
            case "gt.metaitem.01.32599" -> issueAchievement(player, "thatspower");
            case "gt.metaitem.01.32598" -> issueAchievement(player, "luck");
            case "gt.metaitem.01.32749" -> issueAchievement(player, "closeit");
            case "gt.metaitem.01.32730" -> issueAchievement(player, "manipulation");
            case "gt.metaitem.01.32729" -> issueAchievement(player, "filterregulate");
            case "gt.metaitem.01.32605" -> issueAchievement(player, "whatnow");
            case "gt.metaitem.01.32736" -> issueAchievement(player, "zpmage");
            case "gt.metaitem.01.32737" -> issueAchievement(player, "uvage");
            case "gt.metaitem.03.32030" -> issueAchievement(player, "gtmonosilicon");
            case "gt.metaitem.03.32036" -> issueAchievement(player, "gtlogicwafer");
            case "gt.metaitem.01.32701" -> issueAchievement(player, "gtlogiccircuit");
            case "gt.metaitem.03.32085" -> issueAchievement(player, "gtquantumprocessor");
            case "gt.metaitem.03.32089" -> issueAchievement(player, "gtcrystalprocessor");
            case "gt.metaitem.03.32092" -> issueAchievement(player, "gtwetware");
            case "gt.metaitem.03.32095" -> issueAchievement(player, "gtwetmain");
            case "gt.Thoriumcell" -> issueAchievement(player, "newfuel");
            case "ic2.itemPartCircuitAdv" -> issueAchievement(player, "stepforward");
            case "gt.blockcasings5.1" -> issueAchievement(player, "upgrade");
            case "gt.blockcasings5.2" -> issueAchievement(player, "upgrade2");
            case "gt.blockcasings5.3" -> issueAchievement(player, "upgrade3");
            case "gt.blockcasings5.4" -> issueAchievement(player, "upgrade4");
            case "gt.blockcasings5.5" -> issueAchievement(player, "upgrade5");
            case "gt.blockcasings5.6" -> issueAchievement(player, "over9000");
            case "gt.blockcasings.15" -> issueAchievement(player, "conducting");
            case "gt.metaitem.01.32761" -> { // Debug Scanner pickup shows all assline recipes.
                if (player.capabilities.isCreativeMode) {
                    for (GT_Recipe recipe : RecipeMaps.assemblylineVisualRecipes.getAllRecipes()) {
                        issueAchievement(
                            player,
                            recipe.getOutput(0)
                                .getUnlocalizedName());
                        recipe.mHidden = false;
                    }
                }
            }
        }

        if ((stack.getItem() == Ic2Items.quantumBodyarmor.getItem())
            || (stack.getItem() == Ic2Items.quantumBoots.getItem())
            || (stack.getItem() == Ic2Items.quantumHelmet.getItem())
            || (stack.getItem() == Ic2Items.quantumLeggings.getItem())) {
            issueAchievement(player, "buildQArmor");
        }

        for (GT_Recipe recipe : RecipeMaps.assemblylineVisualRecipes.getAllRecipes()) {
            if (recipe.getOutput(0)
                .getUnlocalizedName()
                .equals(stack.getUnlocalizedName())) {
                issueAchievement(
                    player,
                    recipe.getOutput(0)
                        .getUnlocalizedName());
                recipe.mHidden = false;
            }
        }
    }
}
