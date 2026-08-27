package tectech.voidcraft.loader;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

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
import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.item.ItemVoidcraftCovers;
import tectech.voidcraft.machine.MTEVoidbaseAssembler;
import tectech.voidcraft.machine.MTEVoidcraftAssembler;
import tectech.voidcraft.machine.MTEVoidcraftComponent;
import tectech.voidcraft.machine.MTEVoidcraftGateway;
import tectech.voidcraft.machine.MTEVoidcraftStorageBay;
import tectech.voidcraft.multiblock.MTEVoidcraftMiningArray;
import tectech.voidcraft.multiblock.MTEVoidcraftMultiblockCasing;
import tectech.voidcraft.multiblock.VoidcraftMultiblockRegistry;
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
     * classic function ships as a cover (see {@link ItemVoidcraftCovers}). The multiblock component entries
     * (mining array controller + casings) register in {@link #registerMultiblockMTEs()}. The entry array is
     * indexed by component meta.
     */
    private static final CustomItemList[] COMPONENT_ENTRIES = { CustomItemList.VoidcraftComponent_Controller, null,
        CustomItemList.VoidcraftComponent_Frame, null, null, null, null, null, null, null, null,
        CustomItemList.VoidcraftMiningArray_Controller, CustomItemList.VoidcraftMiningArray_Casing,
        CustomItemList.VoidcraftMiningArray_Panel };

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
        // PASS 23 (user spec): covers are the primary components — the only classic placeable full blocks are the
        // controller + frame. The cover-only catalog entries (engine, cargo bay, mining centre, starlifter,
        // scanner, fabricator, reactor) get NO MTE and NO item: they cannot be placed, and old builds holding
        // them are rejected at the assembler with voidcraft_cover_only_component (no backwards compatibility —
        // standing directive).
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
        TecTech.LOGGER
            .info("Voidcraft full-block MTEs registered (controller + frame only — all other functions are covers)");
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
        TecTech.LOGGER.info("Voidcraft multiblock components registered (Mining Array 3x3x2)");
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
