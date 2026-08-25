package tectech.voidcraft.loader;

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
import tectech.voidcraft.item.ItemUSSController;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.item.ItemVoidcraftCovers;
import tectech.voidcraft.machine.MTEVoidcraftAssembler;
import tectech.voidcraft.machine.MTEVoidcraftComponent;
import tectech.voidcraft.machine.MTEVoidcraftGateway;
import tectech.voidcraft.machine.MTEVoidcraftStorageBay;
import tectech.voidcraft.render.BlockVoidcraftShipRender;
import tectech.voidcraft.render.TileEntityVoidcraftShip;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftCoverRegistry;
import tectech.voidcraft.uss.MTEUnstableSolarSystem;
import tectech.voidcraft.uss.USSPlanetCatalog;
import tectech.voidcraft.uss.USSStarCatalog;

/**
 * Voidcraft loader (EoH rework, Phase 0+1+1.5+2).
 *
 * <p>
 * Wired into {@code MainLoader}:
 * <ul>
 * <li>preLoad — cover item registration + cover item→component registry</li>
 * <li>load — the two full-block MTEs (controller + frame, pass 23), the 8 covers, the Voidcraft Assembler MTE, and
 * the Phase 2 Unstable Solar System MTE</li>
 * </ul>
 *
 * <p>
 * PASS 23 (user spec): covers are the PRIMARY components — all ship functionality comes from them. The only
 * placeable full blocks are the Voidcraft Controller and the Voidcraft Frame (renamed Utility Block, a
 * mostly-transparent framebox). Full blocks live on the standard GT machine block (wrench-facing, per-face
 * texture slots, six cover slots); the assembler scan picks blocks + covers up.
 *
 * <p>
 * Recipes are intentionally absent: creative-mode testing is the working loop for now (standing user directive —
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
     * PASS 23: only the two placeable full blocks have creative-tab items (controller + frame) — every other
     * function ships as a cover (see {@link ItemVoidcraftCovers}). The entry array is indexed by component meta.
     */
    private static final CustomItemList[] COMPONENT_ENTRIES = { CustomItemList.VoidcraftComponent_Controller, null,
        CustomItemList.VoidcraftComponent_Frame, null, null, null, null, null, null };

    private VoidcraftLoader() {}

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

        // Cover parts (8 subtypes) + item→component registry (covers are placed on hull blocks in the load phase)
        ItemVoidcraftCovers.run();
        for (VoidcraftCoverComponent cover : VoidcraftCoverComponent.ALL) {
            VoidcraftCoverRegistry.register(ItemVoidcraftCovers.stack(cover), cover);
        }
        VoidcraftCoverRegistry.markReady();
        TecTech.LOGGER.info("Voidcraft covers registered");

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

        // The two full-block MTEs (machine block) — controller (id 32058) + frame (id 32060); pass 23: everything
        // else is a cover
        registerComponentMTEs();

        // The Voidcraft Assembler multiblock
        CustomItemList.Machine_Multi_VoidcraftAssembler.set(
            new MTEVoidcraftAssembler(
                gregtech.api.enums.MetaTileEntityIDs.VoidcraftAssembler.ID,
                "multimachine.em.voidcraft_assembler",
                "Voidcraft Assembler").getStackForm(1L));
        TecTech.LOGGER.info("Voidcraft Assembler MTE registered");

        // Phase 2 — the Unstable Solar System multiblock (parallel to the legacy Eye of Harmony) + its ignition
        // controller item. Reserved ID 32057 (MetaTileEntityIDs.UnstableSolarSystem).
        ItemUSSController.run();
        TecTech.LOGGER.info("USS controller item registered");

        CustomItemList.Machine_Multi_UnstableSolarSystem.set(
            new MTEUnstableSolarSystem(
                gregtech.api.enums.MetaTileEntityIDs.UnstableSolarSystem.ID,
                "multimachine.em.unstable_solar_system",
                "Unstable Solar System").getStackForm(1L));
        TecTech.LOGGER.info("Unstable Solar System MTE registered");

        // Phase 3 — the launch gateway (ship slot in the center) and the shared cargo storage bay, plus the
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
        // PASS 23 (user spec): covers are the primary components — register ONLY the two placeable full blocks
        // (controller + frame). The cover-only catalog entries (engine, cargo bay, mining centre, starlifter,
        // scanner, fabricator, reactor) get NO MTE and NO item: they cannot be placed, and old builds holding
        // them are rejected at the assembler with voidcraft_cover_only_component (no backwards compatibility —
        // standing directive).
        int[] ids = { gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Controller.ID,
            gregtech.api.enums.MetaTileEntityIDs.VoidcraftComponent_Frame.ID };
        String[] names = { "voidcraft_component_controller", "voidcraft_component_frame" };
        int i = 0;
        for (VoidcraftComponent component : VoidcraftComponent.PLACEABLE) {
            MTEVoidcraftComponent mte = new MTEVoidcraftComponent(
                ids[i],
                names[i],
                component.getDisplayName(),
                component);
            COMPONENT_ENTRIES[component.getMeta()].set(mte.getStackForm(1L));
            i++;
        }
        TecTech.LOGGER
            .info("Voidcraft full-block MTEs registered (controller + frame only — all other functions are covers)");
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
}
