package gtPlusPlus.core.common;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import org.apache.commons.lang3.tuple.Pair;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import galaxyspace.core.entity.mob.EntityEvolvedColdBlaze;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.config.ASMConfiguration;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.InternalEntityRegistry;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.CompatHandler;
import gtPlusPlus.core.handler.CompatIntermodStaging;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.core.handler.events.EnderDragonDeathHandler;
import gtPlusPlus.core.handler.events.EntityDeathHandler;
import gtPlusPlus.core.handler.events.MolecularTransformerTooltipNotice;
import gtPlusPlus.core.handler.events.PlayerSleepEventHandler;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.bauble.BaseBauble;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.tileentities.ModTileEntities;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.xmod.gregtech.common.modularui2.GTPPGuiTextures;
import gtPlusPlus.xmod.ic2.CustomInternalName;

public class CommonProxy implements IFuelHandler {

    public CommonProxy() {
        // Should Register Gregtech Materials I've Made
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void preInit(final FMLPreInitializationEvent e) {
        Logger.INFO("Doing some house cleaning.");
        if (ASMConfiguration.debug.debugMode) {
            Logger.INFO("Development mode enabled.");
        } else {
            Logger.WARNING("Development mode not enabled.");
        }

        AddToCreativeTab.initialiseTabs();
        CustomInternalName.init();

        ModItems.init();
        ModBlocks.init();
        GTPPFluids.init();

        CompatIntermodStaging.preInit(e);
        BookHandler.run();
        // Registration of entities and renderers
        Logger.INFO("[Proxy] Calling Entity registration.");
        registerEntities();
        Logger.INFO("[Proxy] Calling Tile Entity registration.");
        registerTileEntities();

        Logger.INFO("[Proxy] Calling Render registration.");
        registerRenderThings();

        GTPPGuiTextures.init();
    }

    public void init(final FMLInitializationEvent e) {
        if (e.getSide()
            .isClient() && Mods.AdvancedSolarPanel.isModLoaded()) {
            MinecraftForge.EVENT_BUS.register(new MolecularTransformerTooltipNotice());
        }
        // Handles Sleep Benefits
        PlayerSleepEventHandler.init();

        MinecraftForge.EVENT_BUS.register(new EnderDragonDeathHandler());
        MinecraftForge.EVENT_BUS.register(new EntityDeathHandler());

        // Compat Handling
        CompatHandler.registerMyModsOreDictEntries();
        CompatHandler.intermodOreDictionarySupport();
        CompatIntermodStaging.init(e);
    }

    public void postInit(final FMLPostInitializationEvent e) {
        GameRegistry.registerFuelHandler(this);
        // Compat Handling
        Logger.INFO("Initialising Handler, Then Adding Recipes");
        CompatHandler.InitialiseHandlerThenAddRecipes();
        Logger.INFO("Loading Intermod staging.");
        CompatIntermodStaging.postInit(e);
        // Moved last, to prevent recipes being generated post initialisation.
        Logger.INFO("Loading Gregtech API recipes.");
        CompatHandler.startLoadingGregAPIBasedRecipes();
        Logger.INFO("Loading queued recipes.");
        CompatHandler.runQueuedRecipes();
        Logger.INFO("Registering custom mob drops.");
        registerCustomMobDrops();
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        CompatIntermodStaging.onLoadComplete(event);
    }

    public void registerNetworkStuff() {
        GuiHandler.init();
    }

    public void registerEntities() {
        InternalEntityRegistry.registerEntities();
    }

    public void registerTileEntities() {
        ModTileEntities.init();
    }

    public void registerRenderThings() {}

    public int addArmor(final String armor) {
        return 0;
    }

    public void registerCustomMobDrops() {

        // Blazes
        if (Mods.COFHCore.isModLoaded()) {
            EntityUtils.registerDropsForMob(EntityBlaze.class, Materials.Pyrotheum.getDust(1), 1, 10);
            EntityUtils.registerDropsForMob(EntityBlaze.class, Materials.Pyrotheum.getDust(1), 1, 10);
        }

        // GalaxySpace Support
        if (Mods.GalaxySpace.isModLoaded()) {
            ItemStack aBlizz = Materials.Blizz.getDust(1);
            ItemStack aCryo = Materials.Cryotheum.getDust(1);
            EntityUtils.registerDropsForMob(
                EntityEvolvedColdBlaze.class,
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Blizz, 1),
                1,
                2500);
            if (aBlizz != null) {
                EntityUtils.registerDropsForMob(EntityEvolvedColdBlaze.class, aBlizz, 1, 5000);
            }
            if (aCryo != null) {
                EntityUtils.registerDropsForMob(EntityEvolvedColdBlaze.class, aCryo, 1, 200);
            }
        }
    }

    public World getClientWorld() {
        return null;
    }

    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    @Override
    public int getBurnTime(ItemStack aStack) {
        for (Pair<Integer, ItemStack> temp : GTPPCore.burnables) {
            int aStackID = Item.getIdFromItem(aStack.getItem());
            int burnID = Item.getIdFromItem(
                temp.getValue()
                    .getItem());
            if (aStackID == burnID) {
                int burn = temp.getKey();
                ItemStack fuel = temp.getValue();
                ItemStack testItem = GTUtility.copyAmount(aStack.stackSize, fuel);

                if (aStack.isItemEqual(testItem)) {
                    return burn;
                }
            }
        }
        return 0;
    }

    @Optional.Method(modid = Mods.ModIDs.BAUBLES)
    @SubscribeEvent
    public void onPlayerAttacked(LivingAttackEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer player)) {
            return;
        }
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
        if (baubles == null) {
            return;
        }
        final ItemStack bauble1 = baubles.getStackInSlot(1);
        if (bauble1 != null && bauble1.getItem() instanceof BaseBauble gtBauble
            && gtBauble.getDamageNegations()
                .contains(event.source.damageType)) {
            event.setCanceled(true);
            return;
        }
        final ItemStack bauble2 = baubles.getStackInSlot(2);
        if (bauble2 != null && bauble2.getItem() instanceof BaseBauble gtBauble
            && gtBauble.getDamageNegations()
                .contains(event.source.damageType)) {
            event.setCanceled(true);
        }
    }
}
