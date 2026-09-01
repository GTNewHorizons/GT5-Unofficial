package tectech.voidcraft.loader;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.covers.CoverPlacer;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.Reference;
import tectech.TecTech;
import tectech.thing.CustomItemList;
import tectech.voidcraft.VoidcraftConfig;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.cover.CoverVoidcraftComponent;
import tectech.voidcraft.debug.VoidcraftDebugEffectRegistry;
import tectech.voidcraft.item.ItemUSSController;
import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.item.ItemVoidcraftCovers;
import tectech.voidcraft.item.ItemVoidcraftDebugDysonSwarm;
import tectech.voidcraft.item.ItemVoidcraftDebugInfraShell;
import tectech.voidcraft.item.ItemVoidcraftDebugStarControl;
import tectech.voidcraft.item.ItemVoidcraftDebugStarDepletion;
import tectech.voidcraft.item.ItemVoidcraftInfraComponent;
import tectech.voidcraft.item.ItemVoidcraftSatellite;
import tectech.voidcraft.machine.MTEVoidbaseAssembler;
import tectech.voidcraft.machine.MTEVoidcraftAssembler;
import tectech.voidcraft.machine.MTEVoidcraftComponent;
import tectech.voidcraft.machine.MTEVoidcraftGateway;
import tectech.voidcraft.machine.MTEVoidcraftStorageBay;
import tectech.voidcraft.multiblock.MTEVoidcraftContinuumStabilizer;
import tectech.voidcraft.multiblock.MTEVoidcraftMiningArray;
import tectech.voidcraft.multiblock.MTEVoidcraftMultiblockCasing;
import tectech.voidcraft.multiblock.MTEVoidcraftSatelliteLauncher;
import tectech.voidcraft.multiblock.MTEVoidcraftStabilizationMatrix;
import tectech.voidcraft.multiblock.MTEVoidcraftStellarInjector;
import tectech.voidcraft.multiblock.MTEVoidcraftStellarLens;
import tectech.voidcraft.multiblock.VoidcraftMultiblockRegistry;
import tectech.voidcraft.render.BlockVoidcraftShipRender;
import tectech.voidcraft.render.TileEntityVoidcraftShip;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftCoverRegistry;
import tectech.voidcraft.ship.VoidcraftFuel;
import tectech.voidcraft.uss.MTEUnstableSolarSystem;
import tectech.voidcraft.uss.USSInfraBuild;
import tectech.voidcraft.uss.USSPlanetCatalog;
import tectech.voidcraft.uss.USSStarCatalog;

/**
 * Voidcraft loader (the EoH rework).
 *
 * <p>
 * Wired into {@code MainLoader}:
 * <ul>
 * <li>preLoad — cover item registration + cover item→component registry</li>
 * <li>load — the two full-block MTEs (controller + frame), the 8 covers, the Voidcraft Assembler MTE, and
 * the Unstable Solar System MTE</li>
 * <li>postLoad — the fuel fluids (the engine tank fluids + the reactor launch fee fluids — the load phase runs
 * before BartWorks' Werkstoff fluid registration, which is declared after:tectech)</li>
 * </ul>
 *
 * <p>
 * Covers are the PRIMARY components — all ship functionality comes from them. The only
 * placeable full blocks are the Voidcraft Controller and the Voidcraft Frame (a
 * mostly-transparent framebox). Full blocks live on the standard GT machine block (wrench-facing, per-face
 * texture slots, six cover slots); the assembler scan picks blocks + covers up.
 *
 * <p>
 * Recipes are intentionally absent: creative-mode testing is the working loop (standing user directive —
 * ignore recipes until explicitly asked).
 *
 * <p>
 * Master switch: {@code config/voidcraft.cfg} → {@code enabled=false} unregisters the whole module (see
 * {@link VoidcraftConfig}).
 *
 * <p>
 * No legacy Eye of Harmony class is touched here; this is a fully parallel machine.
 */
public final class VoidcraftLoader {

    /**
     * The invisible ship-hologram block (docked ship above the gateway / ship in flight above the USS anchor). Created
     * in the load phase; the block self-registers with Forge in its constructor.
     */
    public static BlockVoidcraftShipRender sBlockVoidcraftShipRender;

    /**
     * The placeable full blocks have creative-tab items (controller + the four frame tiers) — every other
     * classic function ships as a cover (see {@link ItemVoidcraftCovers}). The multiblock component entries
     * (mining array controller + casings) register in {@link #registerMultiblockMTEs()}. The entry array is
     * indexed by component meta.
     */
    private static final CustomItemList[] COMPONENT_ENTRIES = { CustomItemList.VoidcraftComponent_Controller, null,
        CustomItemList.VoidcraftComponent_Frame, null, null, null, null, null, null, null, null,
        CustomItemList.VoidcraftMiningArray_Controller, CustomItemList.VoidcraftMiningArray_Casing,
        CustomItemList.VoidcraftMiningArray_Panel, null, CustomItemList.VoidcraftSatelliteLauncher_Controller,
        CustomItemList.VoidcraftSatelliteLauncher_Casing, CustomItemList.VoidcraftSatelliteLauncher_Panel,
        CustomItemList.VoidcraftComponent_Frame2, CustomItemList.VoidcraftComponent_Frame3,
        CustomItemList.VoidcraftComponent_Frame4, null, null, null, null, null, null,
        CustomItemList.VoidcraftStellarInjector_Controller, CustomItemList.VoidcraftStellarInjector_Casing,
        CustomItemList.VoidcraftContinuumStabilizer_Controller, CustomItemList.VoidcraftContinuumStabilizer_Casing,
        CustomItemList.VoidcraftStellarLens_Controller, CustomItemList.VoidcraftStellarLens_Casing,
        CustomItemList.VoidcraftStabilizationMatrix_Controller, CustomItemList.VoidcraftStabilizationMatrix_Casing };

    private VoidcraftLoader() {}

    /**
     * The machine-block item of a placeable component (the classic full blocks + the multiblock components) —
     * for the voidbase assembler's parts list (the "block.<entry>" keys).
     *
     * @param component the catalog entry
     * @return the placed block's item, or null for cover-only entries (their parts list keys resolve through the
     *         cover item instead)
     */
    @Nullable
    public static ItemStack blockItem(VoidcraftComponent component) {
        if (component == null) {
            return null;
        }
        int meta = component.getMeta();
        CustomItemList entry = (meta >= 0 && meta < COMPONENT_ENTRIES.length) ? COMPONENT_ENTRIES[meta] : null;
        if (entry == null) {
            return null;
        }
        return entry.get(1);
    }

    // region things (preLoad)

    public static void preLoad() {
        // Master switch first: config/voidcraft.cfg → enabled=false unregisters the whole module.
        VoidcraftConfig.init();
        if (!VoidcraftConfig.enabled) {
            TecTech.LOGGER.info("Voidcraft rework disabled by config — skipping all voidcraft registrations");
            return;
        }

        // The digitized ship item (non-stackable, NBT payload)
        ItemVoidcraft.run();
        CustomItemList.Voidcraft.set(ItemVoidcraft.INSTANCE);
        TecTech.LOGGER.info("Voidcraft item registered");

        // The digitized Voidbase blueprint item (non-stackable, REUSABLE, NBT payload)
        ItemVoidbaseBlueprint.run();
        CustomItemList.VoidbaseBlueprint.set(ItemVoidbaseBlueprint.INSTANCE);
        TecTech.LOGGER.info("Voidbase blueprint item registered");

        // Cover parts (one subtype per cover) + item→component registry (covers are placed on hull blocks in the load
        // phase)
        ItemVoidcraftCovers.run();
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            VoidcraftCoverRegistry.register(ItemVoidcraftCovers.stack(cover), cover);
        }
        VoidcraftCoverRegistry.markReady();
        TecTech.LOGGER.info("Voidcraft covers registered");

        // The Power Satellite — the Satellite Rail Launcher's infrastructure payload (a plain item that rides
        // ship cargo; no recipe, creative loop).
        ItemVoidcraftSatellite.run();
        CustomItemList.PowerSatellite.set(ItemVoidcraftSatellite.INSTANCE);
        TecTech.LOGGER.info("Power Satellite item registered");

        // The infrastructure components — the constructor-built shells' builder payloads (plain items that ride
        // ship cargo; no recipe, creative loop).
        ItemVoidcraftInfraComponent.run();
        TecTech.LOGGER.info("Infrastructure component items registered");

        // Debug tool (item → effect registry): right-clicked on the UnstableSolarSystem machine, it injects a
        // fixed fraction of the star's satellite capacity into the Dyson Swarm (no resource cost).
        ItemVoidcraftDebugDysonSwarm.run();
        VoidcraftDebugEffectRegistry.register(ItemVoidcraftDebugDysonSwarm.INSTANCE, (machine, player) -> {
            long capacity = machine.starSatelliteCapacity();
            long added = machine.debugAddDysonSatellites(ItemVoidcraftDebugDysonSwarm.injectAmountFor(capacity));
            if (added > 0L) {
                player.addChatMessage(
                    new ChatComponentText(
                        StatCollector.translateToLocalFormatted(
                            "tt.voidcraft_debug_dyson_swarm.feedback",
                            added,
                            machine.starSatelliteCount(),
                            capacity)));
            } else if (capacity <= 0L) {
                player.addChatMessage(
                    new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_dyson_swarm.no_star")));
            } else {
                player.addChatMessage(
                    new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_dyson_swarm.saturated")));
            }
        });
        TecTech.LOGGER.info("Dyson Swarm debug item registered");

        // Debug tool (item → effect registry): right-clicked on the UnstableSolarSystem machine, each injects a
        // fixed fraction of its shell's capacity into the matching infrastructure shell (no resource cost) — the
        // Stabilizer one also reveals the ripple it builds on (the first not-fully-built shell's ripple).
        ItemVoidcraftDebugInfraShell.run();
        VoidcraftDebugEffectRegistry
            .register(ItemVoidcraftDebugInfraShell.INJECTOR, (machine, player) -> debugInjectorEffect(player, machine));
        VoidcraftDebugEffectRegistry.register(
            ItemVoidcraftDebugInfraShell.STABILIZER,
            (machine, player) -> debugInfraShellInject(player, machine, USSInfraBuild.STABILIZER, "stabilizer"));
        VoidcraftDebugEffectRegistry.register(
            ItemVoidcraftDebugInfraShell.LENS,
            (machine, player) -> debugInfraShellInject(player, machine, USSInfraBuild.LENS, "lens"));
        TecTech.LOGGER.info("Infrastructure shell debug items registered");

        // Debug tool (item → effect registry): right-clicked on the UnstableSolarSystem machine, it depletes a
        // fixed fraction of the star's PRIMARY material reserve — the stellar evolution's depletion read (no
        // resource cost).
        ItemVoidcraftDebugStarDepletion.run();
        VoidcraftDebugEffectRegistry.register(ItemVoidcraftDebugStarDepletion.INSTANCE, (machine, player) -> {
            long depleted = machine.debugDepleteStarPrimary(ItemVoidcraftDebugStarDepletion.DEPLETE_FRACTION);
            if (depleted > 0L) {
                player.addChatMessage(
                    new ChatComponentText(
                        StatCollector.translateToLocalFormatted(
                            "tt.voidcraft_debug_star_depletion.feedback",
                            depleted,
                            String.format("%.0f%%", machine.starPrimaryFraction() * 100.0))));
            } else if (machine.starSize() <= 0.0) {
                player.addChatMessage(
                    new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_star_depletion.no_star")));
            } else {
                player.addChatMessage(
                    new ChatComponentText(
                        StatCollector.translateToLocal("tt.voidcraft_debug_star_depletion.depleted")));
            }
        });
        TecTech.LOGGER.info("Star depletion debug item registered");

        // Debug tools (item → effect registry): right-clicked on the UnstableSolarSystem machine — reveal random
        // ripple points, set the star's size to the injector's cap, set the remaining lifespan, or force the
        // star's expiry now (no resource cost).
        ItemVoidcraftDebugStarControl.run();
        VoidcraftDebugEffectRegistry.register(
            ItemVoidcraftDebugStarControl.RIPPLE_SCAN,
            (machine, player) -> debugRippleScanEffect(player, machine));
        VoidcraftDebugEffectRegistry.register(
            ItemVoidcraftDebugStarControl.STAR_SIZE_MAX,
            (machine, player) -> debugStarSizeEffect(player, machine));
        VoidcraftDebugEffectRegistry.register(
            ItemVoidcraftDebugStarControl.LIFESPAN,
            (machine, player) -> debugLifespanEffect(player, machine));
        VoidcraftDebugEffectRegistry.register(
            ItemVoidcraftDebugStarControl.FORCE_EXPIRY,
            (machine, player) -> debugForceExpiryEffect(player, machine));
        TecTech.LOGGER.info("Star control debug items registered");

        // Star + planet DEFINITION catalogs (the registration-based passes): populate the bare-JVM registries that
        // USSPlanets.generate / USSPlanets.sampleStarSize / USSRipples.generate / USSShipCargo consult for planet
        // count, planet pools, star size ranges and ripple ranges. MUST run before any USS is ignited, scanned, or
        // rendered — an empty USSPlanetRegistry makes USSPlanets.generate throw (no planets) and an empty
        // USSStarRegistry degrades star size/ripple range to defensive defaults. (The unit tests register these in
        // setUp; production has to do it here.)
        USSStarCatalog.registerAll();
        USSPlanetCatalog.registerAll();
        TecTech.LOGGER.info("Voidcraft star + planet catalogs registered");
    }

    /**
     * One infrastructure-shell debug effect: injects 10% of the shell's capacity into the matching shell (the
     * star's shell for the Injector / Lens, the next ripple's shell for the Stabilizer — which the effect
     * reveals) and reports the result to the player.
     *
     * @param player  the player who right-clicked
     * @param machine the machine right-clicked
     * @param type    the infrastructure shell's type
     * @param langKey the debug item's lang key prefix (name stem)
     */
    private static void debugInfraShellInject(EntityPlayer player, MTEUnstableSolarSystem machine, int type,
        String langKey) {
        boolean starShell = type != USSInfraBuild.STABILIZER;
        int index = starShell ? -1 : machine.debugStabilizerTargetRipple();
        long capacity = starShell ? machine.infraShellCapacity(type, USSInfraBuild.TARGET_STAR, -1)
            : machine.infraShellCapacity(type, USSInfraBuild.TARGET_RIPPLE, index);
        if (capacity <= 0L) {
            // 0 capacity = no ignited star (infraShellCapacity is 0 unless the star is ignited).
            player.addChatMessage(
                new ChatComponentText(
                    StatCollector.translateToLocal("tt.voidcraft_debug_" + langKey + "_shell.no_star")));
            return;
        }
        if (!starShell && index < 0) {
            // Stabilizer with an ignited star but no next ripple: every ripple's shell is already full.
            player.addChatMessage(
                new ChatComponentText(
                    StatCollector.translateToLocal("tt.voidcraft_debug_" + langKey + "_shell.saturated")));
            return;
        }
        long added = starShell
            ? machine.debugAddStarShellUnits(type, ItemVoidcraftDebugInfraShell.injectAmountFor(capacity))
            : machine.debugAddStabilizerToNextRipple(ItemVoidcraftDebugInfraShell.injectAmountFor(capacity));
        if (added > 0L) {
            long count = starShell ? machine.infraShellCount(type, USSInfraBuild.TARGET_STAR, -1)
                : machine.infraShellCount(type, USSInfraBuild.TARGET_RIPPLE, index);
            if (starShell) {
                player.addChatMessage(
                    new ChatComponentText(
                        StatCollector.translateToLocalFormatted(
                            "tt.voidcraft_debug_" + langKey + "_shell.feedback",
                            added,
                            count,
                            capacity)));
            } else {
                player.addChatMessage(
                    new ChatComponentText(
                        StatCollector.translateToLocalFormatted(
                            "tt.voidcraft_debug_" + langKey + "_shell.feedback",
                            index,
                            added,
                            count,
                            capacity)));
            }
        } else {
            player.addChatMessage(
                new ChatComponentText(
                    StatCollector.translateToLocal("tt.voidcraft_debug_" + langKey + "_shell.saturated")));
        }
    }

    /**
     * The Stellar Injector's debug effect: while the star's shell is not fully built, one click injects 10% of the
     * shell's capacity (the usual shell debug path); once the shell is fully built, each click runs ONE cycle of
     * the size step — the star's size jumps one step toward the 1.5× cap (the injector's buffer is bypassed; the
     * debug item is free).
     *
     * @param player  the player who right-clicked
     * @param machine the machine right-clicked
     */
    private static void debugInjectorEffect(EntityPlayer player, MTEUnstableSolarSystem machine) {
        long capacity = machine.infraShellCapacity(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1);
        if (capacity <= 0L) {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_injector_shell.no_star")));
            return;
        }
        if (machine.infraShellCount(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1) < capacity) {
            debugInfraShellInject(player, machine, USSInfraBuild.INJECTOR, "injector");
            return;
        }
        double newSize = machine.debugStepInjector();
        if (newSize > 0.0) {
            player.addChatMessage(
                new ChatComponentText(
                    StatCollector.translateToLocalFormatted(
                        "tt.voidcraft_debug_injector_shell.step",
                        String.format("%.2f", newSize),
                        String.format("%.2f", machine.starSizeCap()))));
        } else {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_injector_shell.at_cap")));
        }
    }

    /**
     * The ripple scanner's debug effect: reveals up to {@link ItemVoidcraftDebugStarControl#RIPPLES_PER_CLICK}
     * random unrevealed ripple points and reports the result to the player.
     *
     * @param player  the player who right-clicked
     * @param machine the machine right-clicked
     */
    private static void debugRippleScanEffect(EntityPlayer player, MTEUnstableSolarSystem machine) {
        int revealed = machine.debugScanRandomRipples(ItemVoidcraftDebugStarControl.RIPPLES_PER_CLICK);
        if (revealed > 0) {
            player.addChatMessage(
                new ChatComponentText(
                    StatCollector.translateToLocalFormatted(
                        "tt.voidcraft_debug_ripple_scan.feedback",
                        revealed,
                        machine.rippleRevealedCount(),
                        machine.ripplePointCount())));
        } else if (machine.starSize() <= 0.0) {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_ripple_scan.no_star")));
        } else {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_ripple_scan.revealed")));
        }
    }

    /**
     * The star-size setter's debug effect: sets the star's size to the injector's cap and reports the result to
     * the player.
     *
     * @param player  the player who right-clicked
     * @param machine the machine right-clicked
     */
    private static void debugStarSizeEffect(EntityPlayer player, MTEUnstableSolarSystem machine) {
        double newSize = machine.debugSetStarSizeToCap();
        if (newSize > 0.0) {
            player.addChatMessage(
                new ChatComponentText(
                    StatCollector.translateToLocalFormatted(
                        "tt.voidcraft_debug_star_size.feedback",
                        String.format("%.2f", newSize))));
        } else if (machine.starSize() <= 0.0) {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_star_size.no_star")));
        } else {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_star_size.at_cap")));
        }
    }

    /**
     * The lifespan setter's debug effect: sets the star's remaining lifespan to
     * {@link ItemVoidcraftDebugStarControl#LIFESPAN_SECONDS} seconds and reports the result to the player.
     *
     * @param player  the player who right-clicked
     * @param machine the machine right-clicked
     */
    private static void debugLifespanEffect(EntityPlayer player, MTEUnstableSolarSystem machine) {
        long ticks = machine.debugSetLifespanTicks(ItemVoidcraftDebugStarControl.LIFESPAN_SECONDS * 20L);
        if (ticks > 0L) {
            player.addChatMessage(
                new ChatComponentText(
                    StatCollector.translateToLocalFormatted("tt.voidcraft_debug_lifespan.feedback", ticks / 20L)));
        } else {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_lifespan.no_star")));
        }
    }

    /**
     * The expiry trigger's debug effect: forces the star's expiry on the spot (the expiry pipeline runs
     * immediately) and reports the result to the player.
     *
     * @param player  the player who right-clicked
     * @param machine the machine right-clicked
     */
    private static void debugForceExpiryEffect(EntityPlayer player, MTEUnstableSolarSystem machine) {
        if (machine.debugForceExpiry()) {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_force_expiry.feedback")));
        } else {
            player.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tt.voidcraft_debug_force_expiry.no_star")));
        }
    }

    // endregion

    // region machines (load)

    public static void load() {
        if (!VoidcraftConfig.enabled) {
            return;
        }

        // Resolve every component/cover texture NOW (load phase, before the icon registration phase) so the
        // icon containers get registered. Any later lookup (placed blocks via newMetaEntity, mounted covers)
        // must hit this cache — re-resolving by name after GTClient.onLoadComplete's cleanup() would create
        // unregistered containers and NPE at render time.
        VoidcraftTextures.resolveAll();

        // The full-block MTEs (machine block) — controller (id 32058) + the four frame tiers (id 32060 / 32076 /
        // 32077 / 32078); everything else is a cover
        registerComponentMTEs();

        // The multiblock components (GT multiblocks; machine-block MTEs, id = 32058 + catalog meta)
        registerMultiblockMTEs();

        // The Voidcraft Assembler multiblock
        CustomItemList.Machine_Multi_VoidcraftAssembler.set(
            new MTEVoidcraftAssembler(
                gregtech.api.enums.MetaTileEntityIDs.VoidcraftAssembler.ID,
                "multimachine.em.voidcraft_assembler",
                "Voidcraft Assembler").getStackForm(1L));
        TecTech.LOGGER.info("Voidcraft Assembler MTE registered");

        // The Voidbase Assembler multiblock (15x15x15 build volume -> reusable Voidbase blueprint item)
        CustomItemList.Machine_Multi_VoidbaseAssembler.set(
            new MTEVoidbaseAssembler(
                gregtech.api.enums.MetaTileEntityIDs.VoidbaseAssembler.ID,
                "multimachine.em.voidbase_assembler",
                "Voidbase Assembler").getStackForm(1L));
        TecTech.LOGGER.info("Voidbase Assembler MTE registered");

        // The Unstable Solar System multiblock (parallel to the legacy Eye of Harmony) + its ignition
        // controller item. Reserved ID 32057 (MetaTileEntityIDs.UnstableSolarSystem).
        ItemUSSController.run();
        TecTech.LOGGER.info("USS controller item registered");

        CustomItemList.Machine_Multi_UnstableSolarSystem.set(
            new MTEUnstableSolarSystem(
                gregtech.api.enums.MetaTileEntityIDs.UnstableSolarSystem.ID,
                "multimachine.em.unstable_solar_system",
                "Unstable Solar System").getStackForm(1L));
        TecTech.LOGGER.info("Unstable Solar System MTE registered");

        // The launch gateway (ship slot in the center) and the shared cargo storage bay, plus the
        // invisible ship-hologram block + its tile entity (the actual digitized ship, rendered as a 3D model).
        CustomItemList.Machine_Multi_VoidcraftGateway.set(
            new MTEVoidcraftGateway(
                gregtech.api.enums.MetaTileEntityIDs.VoidcraftGateway.ID,
                "multimachine.em.voidcraft_gateway",
                "Voidcraft Gateway").getStackForm(1L));
        CustomItemList.Machine_Multi_VoidcraftStorageBay.set(
            new MTEVoidcraftStorageBay(
                gregtech.api.enums.MetaTileEntityIDs.VoidcraftStorageBay.ID,
                "multimachine.em.voidcraft_storage_bay",
                "Voidcraft Storage Bay").getStackForm(1L));
        TecTech.LOGGER.info("Voidcraft Gateway + Storage Bay MTEs registered");

        sBlockVoidcraftShipRender = new BlockVoidcraftShipRender();
        GameRegistry.registerTileEntity(TileEntityVoidcraftShip.class, Reference.MODID + ":VoidcraftShipRenderBlock");
        TecTech.LOGGER.info("Voidcraft ship render block + tile entity registered");

        // Voidcraft covers (placement restricted to component blocks)
        registerCovers();
    }

    private static void registerComponentMTEs() {
        // Covers are the primary components — the only classic placeable full blocks are the
        // controller + frame. The cover-only catalog entries (engine, cargo bay, mining centre, starlifter,
        // scanner, fabricator, reactor) get NO MTE and NO item: they cannot be placed, and builds holding
        // them as full blocks are rejected at the assembler with voidcraft_cover_only_component (no backwards
        // compatibility — standing directive).
        MTEVoidcraftComponent controller = new MTEVoidcraftComponent(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Controller.ID,
            "voidcraft_component_controller",
            VoidcraftComponent.CONTROLLER.getDisplayName(),
            VoidcraftComponent.CONTROLLER);
        COMPONENT_ENTRIES[VoidcraftComponent.CONTROLLER.getMeta()].set(controller.getStackForm(1L));
        MTEVoidcraftComponent frame = new MTEVoidcraftComponent(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Frame.ID,
            "voidcraft_component_frame",
            VoidcraftComponent.FRAME.getDisplayName(),
            VoidcraftComponent.FRAME);
        COMPONENT_ENTRIES[VoidcraftComponent.FRAME.getMeta()].set(frame.getStackForm(1L));
        MTEVoidcraftComponent frame2 = new MTEVoidcraftComponent(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Frame2.ID,
            "voidcraft_component_frame_2",
            VoidcraftComponent.FRAME_2.getDisplayName(),
            VoidcraftComponent.FRAME_2);
        COMPONENT_ENTRIES[VoidcraftComponent.FRAME_2.getMeta()].set(frame2.getStackForm(1L));
        MTEVoidcraftComponent frame3 = new MTEVoidcraftComponent(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Frame3.ID,
            "voidcraft_component_frame_3",
            VoidcraftComponent.FRAME_3.getDisplayName(),
            VoidcraftComponent.FRAME_3);
        COMPONENT_ENTRIES[VoidcraftComponent.FRAME_3.getMeta()].set(frame3.getStackForm(1L));
        MTEVoidcraftComponent frame4 = new MTEVoidcraftComponent(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Frame4.ID,
            "voidcraft_component_frame_4",
            VoidcraftComponent.FRAME_4.getDisplayName(),
            VoidcraftComponent.FRAME_4);
        COMPONENT_ENTRIES[VoidcraftComponent.FRAME_4.getMeta()].set(frame4.getStackForm(1L));
        TecTech.LOGGER.info(
            "Voidcraft full-block MTEs registered (controller + the four frame tiers — all other functions are covers)");
    }

    private static void registerMultiblockMTEs() {
        // Multiblock components — each is its own GT multiblock MTE with its own structure definition, and its
        // catalog entry carries ALL of the component's stats (the existing catalog workflow). Registering one
        // component: instantiate the MTEs (they self-register by id), set their creative-tab items, and wire the
        // allowed-components table.
        MTEVoidcraftMiningArray miningArray = new MTEVoidcraftMiningArray(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftMiningArrayController.ID,
            "multimachine.em.voidcraft_mining_array",
            VoidcraftComponent.MINING_ARRAY.getDisplayName());
        COMPONENT_ENTRIES[VoidcraftComponent.MINING_ARRAY.getMeta()].set(miningArray.getStackForm(1L));
        MTEVoidcraftMultiblockCasing casing = new MTEVoidcraftMultiblockCasing(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftMiningArrayCasing.ID,
            "voidcraft_mining_array_casing",
            VoidcraftComponent.MINING_ARRAY_CASING.getDisplayName(),
            VoidcraftComponent.MINING_ARRAY_CASING);
        COMPONENT_ENTRIES[VoidcraftComponent.MINING_ARRAY_CASING.getMeta()].set(casing.getStackForm(1L));
        MTEVoidcraftMultiblockCasing panel = new MTEVoidcraftMultiblockCasing(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftMiningArrayPanel.ID,
            "voidcraft_mining_array_panel",
            VoidcraftComponent.MINING_ARRAY_PANEL.getDisplayName(),
            VoidcraftComponent.MINING_ARRAY_PANEL);
        COMPONENT_ENTRIES[VoidcraftComponent.MINING_ARRAY_PANEL.getMeta()].set(panel.getStackForm(1L));
        VoidcraftMultiblockRegistry.register(miningArray);

        // The Satellite Rail Launcher (station multiblock, 7x7x12): controller + its two casing blocks. Station-only
        // (the ship assembler rejects it); the 7x7x12 footprint only fits the Voidbase Assembler's 15x15x15 volume.
        MTEVoidcraftSatelliteLauncher launcher = new MTEVoidcraftSatelliteLauncher(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftSatelliteLauncherController.ID,
            "multimachine.em.voidcraft_satellite_launcher",
            VoidcraftComponent.SATELLITE_LAUNCHER.getDisplayName());
        COMPONENT_ENTRIES[VoidcraftComponent.SATELLITE_LAUNCHER.getMeta()].set(launcher.getStackForm(1L));
        MTEVoidcraftMultiblockCasing launcherCasing = new MTEVoidcraftMultiblockCasing(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftSatelliteLauncherCasing.ID,
            "voidcraft_satellite_launcher_casing",
            VoidcraftComponent.SATELLITE_LAUNCHER_CASING.getDisplayName(),
            VoidcraftComponent.SATELLITE_LAUNCHER_CASING);
        COMPONENT_ENTRIES[VoidcraftComponent.SATELLITE_LAUNCHER_CASING.getMeta()].set(launcherCasing.getStackForm(1L));
        MTEVoidcraftMultiblockCasing launcherPanel = new MTEVoidcraftMultiblockCasing(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftSatelliteLauncherPanel.ID,
            "voidcraft_satellite_launcher_panel",
            VoidcraftComponent.SATELLITE_LAUNCHER_PANEL.getDisplayName(),
            VoidcraftComponent.SATELLITE_LAUNCHER_PANEL);
        COMPONENT_ENTRIES[VoidcraftComponent.SATELLITE_LAUNCHER_PANEL.getMeta()].set(launcherPanel.getStackForm(1L));
        VoidcraftMultiblockRegistry.register(launcher);

        // The star-infrastructure structures: the Stellar Injector (7x7x12), Continuum Stabilizer (5x5x7), Stellar
        // Lens (7x7x12) and Stabilization Matrix (7x7x10), each a controller + one casing. Station-only, like the
        // launcher: digitized by the Voidbase Assembler into a base blueprint, rejected from ship builds; the
        // capability each provides (the injector / stabilizer / lens / matrix) is an internal of the Unstable
        // Solar System, contributed by the base that carries it.
        MTEVoidcraftStellarInjector injector = new MTEVoidcraftStellarInjector(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftStellarInjector.ID,
            "multimachine.em.voidcraft_stellar_injector",
            VoidcraftComponent.STELLAR_INJECTOR.getDisplayName());
        COMPONENT_ENTRIES[VoidcraftComponent.STELLAR_INJECTOR.getMeta()].set(injector.getStackForm(1L));
        MTEVoidcraftMultiblockCasing injectorCasing = new MTEVoidcraftMultiblockCasing(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftStellarInjectorCasing.ID,
            "voidcraft_stellar_injector_casing",
            VoidcraftComponent.STELLAR_INJECTOR_CASING.getDisplayName(),
            VoidcraftComponent.STELLAR_INJECTOR_CASING);
        COMPONENT_ENTRIES[VoidcraftComponent.STELLAR_INJECTOR_CASING.getMeta()].set(injectorCasing.getStackForm(1L));
        VoidcraftMultiblockRegistry.register(injector);

        MTEVoidcraftContinuumStabilizer stabilizer = new MTEVoidcraftContinuumStabilizer(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftContinuumStabilizer.ID,
            "multimachine.em.voidcraft_continuum_stabilizer",
            VoidcraftComponent.CONTINUUM_STABILIZER.getDisplayName());
        COMPONENT_ENTRIES[VoidcraftComponent.CONTINUUM_STABILIZER.getMeta()].set(stabilizer.getStackForm(1L));
        MTEVoidcraftMultiblockCasing stabilizerCasing = new MTEVoidcraftMultiblockCasing(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftContinuumStabilizerCasing.ID,
            "voidcraft_continuum_stabilizer_casing",
            VoidcraftComponent.CONTINUUM_STABILIZER_CASING.getDisplayName(),
            VoidcraftComponent.CONTINUUM_STABILIZER_CASING);
        COMPONENT_ENTRIES[VoidcraftComponent.CONTINUUM_STABILIZER_CASING.getMeta()]
            .set(stabilizerCasing.getStackForm(1L));
        VoidcraftMultiblockRegistry.register(stabilizer);

        MTEVoidcraftStellarLens lens = new MTEVoidcraftStellarLens(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftStellarLens.ID,
            "multimachine.em.voidcraft_stellar_lens",
            VoidcraftComponent.STELLAR_LENS.getDisplayName());
        COMPONENT_ENTRIES[VoidcraftComponent.STELLAR_LENS.getMeta()].set(lens.getStackForm(1L));
        MTEVoidcraftMultiblockCasing lensCasing = new MTEVoidcraftMultiblockCasing(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftStellarLensCasing.ID,
            "voidcraft_stellar_lens_casing",
            VoidcraftComponent.STELLAR_LENS_CASING.getDisplayName(),
            VoidcraftComponent.STELLAR_LENS_CASING);
        COMPONENT_ENTRIES[VoidcraftComponent.STELLAR_LENS_CASING.getMeta()].set(lensCasing.getStackForm(1L));
        VoidcraftMultiblockRegistry.register(lens);

        MTEVoidcraftStabilizationMatrix matrix = new MTEVoidcraftStabilizationMatrix(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftStabilizationMatrix.ID,
            "multimachine.em.voidcraft_stabilization_matrix",
            VoidcraftComponent.STABILIZATION_MATRIX.getDisplayName());
        COMPONENT_ENTRIES[VoidcraftComponent.STABILIZATION_MATRIX.getMeta()].set(matrix.getStackForm(1L));
        MTEVoidcraftMultiblockCasing matrixCasing = new MTEVoidcraftMultiblockCasing(
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftStabilizationMatrixCasing.ID,
            "voidcraft_stabilization_matrix_casing",
            VoidcraftComponent.STABILIZATION_MATRIX_CASING.getDisplayName(),
            VoidcraftComponent.STABILIZATION_MATRIX_CASING);
        COMPONENT_ENTRIES[VoidcraftComponent.STABILIZATION_MATRIX_CASING.getMeta()].set(matrixCasing.getStackForm(1L));
        VoidcraftMultiblockRegistry.register(matrix);

        TecTech.LOGGER.info(
            "Voidcraft multiblock components registered (Mining Array 3x3x2, Satellite Launcher 7x7x12, Stellar Injector 7x7x12, Continuum Stabilizer 5x5x7, Stellar Lens 7x7x12, Stabilization Matrix 7x7x10)");
    }

    private static void registerCovers() {
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            ITexture texture = VoidcraftTextures.componentTexture(cover.getMirroredComponent());
            CoverRegistry.registerCover(
                ItemVoidcraftCovers.stack(cover),
                texture,
                CoverVoidcraftComponent::new,
                CoverPlacer.builder()
                    .onlyPlaceIf(
                        (side, item, coverable) -> coverable instanceof IGregTechTileEntity igte
                            && igte.getMetaTileEntity() instanceof MTEVoidcraftComponent)
                    .build());
        }
        TecTech.LOGGER.info("Voidcraft cover placements registered");
    }

    // endregion

    // region fuel fluids (postLoad)

    /**
     * The fuel fluids (the engine tanks + the reactor launch fees): the POST-init phase — BartWorks registers its
     * Werkstoff fluids (Xenon) during its own init, which is declared {@code after:tectech}, so the load phase runs
     * too early (WerkstoffLoader.fluids is still empty there). Both sides — the tooltips resolve fluid names
     * client-side.
     */
    public static void postLoad() {
        if (!VoidcraftConfig.enabled) {
            return;
        }
        VoidcraftFuel.init();
        TecTech.LOGGER.info("Voidcraft fuel fluids resolved");
    }

    // endregion
}
