package gtPlusPlus.core.handler;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTLog;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;

public class AchievementHandler {

    public ConcurrentHashMap<String, Achievement> achievementList = new ConcurrentHashMap<>();

    public int adjX = 5;
    public int adjY = 9;

    private static final String aBaseAchievementName = "gtpp.start";

    public AchievementHandler() {

        Logger.INFO("Initializing GT++ achievements");
        GTLog.out.println("Initializing GT++ achievements");

        // register first
        this.registerAchievement(
            aBaseAchievementName,
            0,
            0,
            MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(
                MetaGeneratedGregtechTools.ANGLE_GRINDER,
                1,
                Materials.Osmium,
                Materials.Osmium,
                null),
            "",
            true);

        this.registerAchievement(
            "hatch.dynamo.buffered",
            2,
            -2,
            GregtechItemList.Hatch_Buffer_Dynamo_IV.get(1),
            aBaseAchievementName,
            false);
        // First multi anyone really needs
        this.registerAchievement(
            "multi.abs",
            -4,
            -2,
            GregtechItemList.Industrial_AlloyBlastSmelter.get(1),
            aBaseAchievementName,
            true);

        // Material Advancement
        this.registerAchievement("dust.potin", 0, 2, MaterialsAlloy.POTIN.getDust(1), aBaseAchievementName, false);
        this.registerAchievement("dust.eglin", 0, 4, MaterialsAlloy.EGLIN_STEEL.getDust(1), "dust.potin", false);
        this.registerAchievement("dust.staballoy", 0, 6, MaterialsAlloy.STABALLOY.getDust(1), "dust.eglin", false);
        this.registerAchievement("dust.quantum", 0, 8, MaterialsAlloy.QUANTUM.getDust(1), "dust.staballoy", true);
        this.registerAchievement(
            "dust.hypogen",
            0,
            10,
            MaterialsElements.STANDALONE.HYPOGEN.getDust(1),
            "dust.quantum",
            true);

        // Blocks
        this.registerAchievement(
            "block.fishtrap",
            -2,
            2,
            ItemUtils.getSimpleStack(ModBlocks.blockFishTrap),
            "dust.potin",
            false);
        this.registerAchievement(
            "block.withercage",
            -2,
            4,
            ItemUtils.getSimpleStack(ModBlocks.blockWitherGuard),
            "dust.eglin",
            false);

        // Machines (-10/-8/-6)
        this.registerAchievement("rtg", -16, -10, GregtechItemList.RTG.get(1), aBaseAchievementName, false);
        this.registerAchievement(
            "dehydrate",
            -15,
            -10,
            GregtechItemList.GT_Dehydrator_HV.get(1),
            aBaseAchievementName,
            false);
        this.registerAchievement(
            "semifluid",
            -14,
            -10,
            GregtechItemList.Generator_SemiFluid_HV.get(1),
            aBaseAchievementName,
            false);
        this.registerAchievement(
            "earlywasher",
            -13,
            -10,
            GregtechItemList.SimpleDustWasher_ULV.get(1),
            aBaseAchievementName,
            false);
        this.registerAchievement(
            "advancedsteam",
            -12,
            -10,
            GregtechItemList.Boiler_Advanced_MV.get(1),
            aBaseAchievementName,
            false);
        if (PollutionUtils.isPollutionEnabled()) {
            this.registerAchievement(
                "pollutionremoval",
                -11,
                -10,
                GregtechItemList.Pollution_Cleaner_IV.get(1),
                aBaseAchievementName,
                false);
        }
        this.registerAchievement(
            "hiampxform",
            -10,
            -10,
            GregtechItemList.Transformer_HA_HV_MV.get(1),
            aBaseAchievementName,
            false);

        // Multis (-4/-2/0)
        this.registerAchievement("multi.pss", -16, -7, GregtechItemList.PowerSubStation.get(1), "multi.abs", false);
        this.registerAchievement("multi.cyclo", -15, -7, GregtechItemList.COMET_Cyclotron.get(1), "multi.abs", false);
        this.registerAchievement(
            "multi.sifter",
            -14,
            -7,
            GregtechItemList.Industrial_Sifter.get(1),
            "dust.eglin",
            false);
        this.registerAchievement(
            "multi.cokeoven",
            -13,
            -7,
            GregtechItemList.Industrial_CokeOven.get(1),
            "multi.abs",
            false);
        this.registerAchievement(
            "multi.boiler.thermal",
            -12,
            -7,
            GregtechItemList.GT4_Thermal_Boiler.get(1),
            "multi.abs",
            false);
        this.registerAchievement(
            "multi.zhuhai",
            -11,
            -7,
            GregtechItemList.Industrial_FishingPond.get(1),
            aBaseAchievementName,
            false);

        // Casings
        this.registerAchievement(
            "casing.abs",
            2,
            -10,
            GregtechItemList.Casing_Coil_BlastSmelter.get(1),
            aBaseAchievementName,
            false);
        this.registerAchievement(
            "casing.cyclotron.coil",
            3,
            -10,
            GregtechItemList.Casing_Cyclotron_Coil.get(1),
            aBaseAchievementName,
            false);
        this.registerAchievement(
            "casing.multiuse",
            4,
            -10,
            GregtechItemList.Casing_Multi_Use.get(1),
            aBaseAchievementName,
            false);
        this.registerAchievement(
            "casing.containment",
            5,
            -10,
            GregtechItemList.Casing_Containment.get(1),
            aBaseAchievementName,
            false);

        // Radioactive
        this.registerAchievement(
            "decay.neptunium238",
            11,
            8,
            ItemUtils.getSimpleStack(ModItems.dustNeptunium238),
            "multi.cyclo",
            false);
        this.registerAchievement(
            "decay.radium226",
            12,
            8,
            ItemUtils.getSimpleStack(ModItems.dustRadium226),
            "multi.cyclo",
            false);
        this.registerAchievement(
            "decay.molybdenum99",
            13,
            8,
            ItemUtils.getSimpleStack(ModItems.dustMolybdenum99),
            "multi.cyclo",
            false);

        AchievementPage.registerAchievementPage(
            new AchievementPage(
                "GT++",
                this.achievementList.values()
                    .toArray(new Achievement[0])));
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    public Achievement registerAchievement(String textId, int x, int y, ItemStack icon, String requirement,
        boolean special) {
        Achievement achievement = new Achievement(
            textId,
            textId,
            this.adjX + x,
            this.adjY + y,
            icon,
            this.getAchievement(requirement));
        if (special) {
            achievement.setSpecial();
        }

        achievement.registerStat();
        if (GTPPCore.DEVENV) {
            GTLog.out.println("achievement." + textId + "=");
            GTLog.out.println("achievement." + textId + ".desc=");
        }

        this.achievementList.put(textId, achievement);
        return achievement;
    }

    public void issueAchievement(EntityPlayer entityplayer, String textId) {
        if (entityplayer != null) {
            entityplayer.triggerAchievement(this.achievementList.get(textId));
        }
    }

    public Achievement getAchievement(String textId) {
        return this.achievementList.get(textId);
    }

    /**
     * A generic handler that will give an achievement for an item. Useful to only write this once, then call it from
     * all handlers.
     *
     * @param aStack  - The Itemstack to check for achievements.
     * @param aPlayer - The player to unlock for.
     */
    private void handleAchivement(ItemStack aStack, EntityPlayer aPlayer) {

        if (aPlayer != null && aStack != null) {
            /*
             * Copy this to all events because I am lazy - Alk 2019
             */

            // Safe name
            String aUnlocalName = ItemUtils.getUnlocalizedItemName(aStack);

            boolean isValid = false;
            // Check if valid name // mod
            String aModID = ItemUtils.getModId(aStack);

            if (aModID == null || aModID.isEmpty()) {
                return;
            }

            if (ItemUtils.getModId(aStack)
                .equals(GTPlusPlus.ID)
                || ItemUtils.getModId(aStack)
                    .equalsIgnoreCase(GregTech.ID)) {
                isValid = true;
            }
            if (!isValid) {
                return;
            }

            // Should unlock base achievement from *ANY* GT++ item. (Too lazy to special case GT machineBlocks though)
            if (ItemUtils.getModId(aStack)
                .equals(GTPlusPlus.ID)) {
                this.issueAchievement(aPlayer, aBaseAchievementName);
            }

            if (aUnlocalName.contains("item.")) {
                aUnlocalName = aUnlocalName.substring(5);
            } else if (aUnlocalName.contains("tile.")) {
                aUnlocalName = aUnlocalName.substring(5);
            }

            /*
             * Misc Blocks
             */
            if (aUnlocalName.equals("blockFishTrap")) {
                this.issueAchievement(aPlayer, "block.fishtrap");
            }
            if (aUnlocalName.equals("blockBlackGate")) {
                this.issueAchievement(aPlayer, "block.withercage");
            }

            /*
             * Decayables
             */
            if (aUnlocalName.equals("dustNeptunium238")) {
                this.issueAchievement(aPlayer, "decay.neptunium238");
            } else if (aUnlocalName.equals("dustRadium226")) {
                this.issueAchievement(aPlayer, "decay.radium226");
            } else if (aUnlocalName.equals("dustMolybdenum99")) {
                this.issueAchievement(aPlayer, "decay.molybdenum99");
            } else if (aUnlocalName.equals("dustTechnetium99M")) {
                this.issueAchievement(aPlayer, "decay.technetium99m");
            } else if (aUnlocalName.equals("dustTechnetium99")) {
                this.issueAchievement(aPlayer, "decay.technetium99");
            }

            /*
             * Random Materials worthy of Achievements
             */
            else if (aUnlocalName.equals("itemDustPotin")) {
                this.issueAchievement(aPlayer, "dust.potin");
            } else if (aUnlocalName.equals("itemDustEglinSteel")) {
                this.issueAchievement(aPlayer, "dust.eglin");
            } else if (aUnlocalName.equals("itemDustStaballoy")) {
                this.issueAchievement(aPlayer, "dust.staballoy");
            } else if (aUnlocalName.equals("itemDustQuantum")) {
                this.issueAchievement(aPlayer, "dust.quantum");
            } else if (aUnlocalName.equals("itemDustHypogen")) {
                this.issueAchievement(aPlayer, "dust.hypogen");
            }

            /*
             * Machines
             */
            else if (aUnlocalName.startsWith("gt.blockmachines.")) {

                // Readability
                String aStartsWith = "gt.blockmachines.";

                /*
                 * Single Blocks
                 */

                // RTG
                if (aUnlocalName.startsWith(aStartsWith + "basicgenerator.rtg")) {
                    this.issueAchievement(aPlayer, "rtg");
                }
                // Dehydrator
                else if (aUnlocalName.startsWith(aStartsWith + "machine.dehydrator.tier.")) {
                    this.issueAchievement(aPlayer, "dehydrate");
                }
                // SemiFluids
                else if (aUnlocalName.startsWith(aStartsWith + "basicgenerator.semifluid.tier.")) {
                    this.issueAchievement(aPlayer, "semifluid");
                }
                // Simple Washer
                else if (aUnlocalName.startsWith(aStartsWith + "simplewasher.01.tier.")) {
                    this.issueAchievement(aPlayer, "earlywasher");
                }
                // Advanced Boilers
                else if (aUnlocalName.startsWith(aStartsWith + "electricboiler.")) {
                    this.issueAchievement(aPlayer, "advancedsteam");
                }
                // Scrubers
                else if (aUnlocalName.startsWith(aStartsWith + "pollutioncleaner.01.tier.")) {
                    this.issueAchievement(aPlayer, "pollutionremoval");
                }
                // High-amp xformers
                else if (aUnlocalName.startsWith(aStartsWith + "transformer.ha.tier.")) {
                    this.issueAchievement(aPlayer, "hiampxform");
                }
                // Buffered Dynamos
                else if (aUnlocalName.startsWith(aStartsWith + "hatch.dynamo.buffer.tier.")) {
                    this.issueAchievement(aPlayer, "hatch.dynamo.buffered");
                }
                // Control Core Hatch
                else if (aUnlocalName.startsWith(aStartsWith + "hatch.control.adv")) {
                    this.issueAchievement(aPlayer, "hatch.control");
                }

                /*
                 * Multis
                 */

                // ABS
                else if (aUnlocalName.equals(aStartsWith + "industrialsalloyamelter.controller.tier.single")) {
                    this.issueAchievement(aPlayer, "multi.abs");
                }
                // PSS
                else if (aUnlocalName.equals(aStartsWith + "substation.01.input.single")) {
                    this.issueAchievement(aPlayer, "multi.pss");
                }
                // Cyclotron
                else if (aUnlocalName.startsWith(aStartsWith + "cyclotron.tier.single")) {
                    this.issueAchievement(aPlayer, "multi.cyclo");
                }
                // Sifter
                else if (aUnlocalName.equals(aStartsWith + "industrialsifter.controller.tier.single")) {
                    this.issueAchievement(aPlayer, "multi.sifter");
                }
                // Coke Oven
                else if (aUnlocalName.equals(aStartsWith + "industrialcokeoven.controller.tier.single")) {
                    this.issueAchievement(aPlayer, "multi.cokeoven");
                }
                // Thermal Boiler
                else if (aUnlocalName.equals(aStartsWith + "gtplusplus.thermal.boiler")) {
                    this.issueAchievement(aPlayer, "multi.boiler.thermal");
                }
                // Zhuhai
                else if (aUnlocalName.equals(aStartsWith + "industrial.fishpond.controller.tier.single")) {
                    this.issueAchievement(aPlayer, "multi.zhuhai");
                }

            }

            /*
             * Casings
             */
            else if (aUnlocalName.equals("gtplusplus.blockcasings.14")) {
                this.issueAchievement(aPlayer, "casing.abs");
            } else if (aUnlocalName.equals("gtplusplus.blockcasings.2.9")) {
                this.issueAchievement(aPlayer, "casing.cyclotron.coil");
            } else if (aUnlocalName.equals("gtplusplus.blockcasings.3.2")) {
                this.issueAchievement(aPlayer, "casing.multiuse");
            } else if (aUnlocalName.equals("gtplusplus.blockcasings.3.15")) {
                this.issueAchievement(aPlayer, "casing.containment");
            }
        }
    }

    /*
     * Handle achievements for all vanilla types of obtianment.
     */

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        ItemStack stack = event.crafting;
        if (player != null && stack != null) {
            handleAchivement(stack, player);
        }
    }

    @SubscribeEvent
    public void onSmelting(ItemSmeltedEvent event) {
        EntityPlayer player = event.player;
        ItemStack stack = event.smelting;
        if (player != null && stack != null) {
            handleAchivement(stack, player);
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack stack = event.item.getEntityItem();
        if (player != null && stack != null) {
            handleAchivement(stack, player);
        }
    }
}
