package tectech.voidcraft.uss;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;
import tectech.voidcraft.item.ItemUSSController;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.loader.VoidcraftLoader;
import tectech.voidcraft.machine.MTEVoidcraftGateway;
import tectech.voidcraft.machine.MTEVoidcraftStorageBay;
import tectech.voidcraft.render.TileEntityVoidcraftShip;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.ship.VoidcraftRole;

/**
 * Unstable Solar System (EoH rework, Phase 2 vertical slice).
 *
 * <p>
 * A new machine parallel to the legacy Eye of Harmony (the legacy code stays untouched, plan §1.1): the shell is
 * the legacy sphere scaled 2× about its center (pass 12: 65×65×65 — same casings and field generators, same hatch
 * rules, same per-type block counts, anchor at the scaled {@code ~} slot 32,32,0) — but instead of mining planets
 * for star matter it hosts an <em>ignitable star</em>:
 * <ul>
 * <li>COLD until a {@link ItemUSSController} is inserted into the controller slot;</li>
 * <li>IGNITED — the star burns: the render block is placed and the lifespan from {@link USSConstants} is counted
 * down every machine tick. The spacetime compression field tier decides the star class and the lifespan;</li>
 * <li>lifespan reaches zero — the star burns out: render block removed, the controller item is consumed (one
 * controller = one star life), state returns to COLD.</li>
 * </ul>
 *
 * <p>
 * This slice deliberately has <b>no energy consumption</b> (the global energy model lands in Phase 6;
 * {@link USSConstants#starDrawEUt(int)} holds the placeholder table) and no recipes
 * ({@link #checkProcessing_EM()} returns NO_RECIPE — this is a state machine, not a recipe machine). The legacy EoH
 * keeps working unchanged in the same world.
 *
 * @see VoidcraftUSS
 * @see USSConstants
 * @see ItemUSSController
 * @see docs/Voidcraft_Implementation_Plan.md
 */
@SuppressWarnings("SpellCheckingInspection")
@IMetaTileEntity.SkipGenerateDescription
public class MTEUnstableSolarSystem extends TTMultiblockBase implements ISurvivalConstructable, USSPilotWorld {

    /** Structure piece name (same as the legacy EoH — the shell is identical). */
    protected static final String STRUCTURE_PIECE_MAIN = "main";

    // Region variables (same field names as the legacy EoH — the structure callbacks below set them).
    private static IIconContainer ScreenOFF;
    private static IIconContainer ScreenON;

    private int spacetimeCompressionFieldMetadata = -1;
    private int timeAccelerationFieldMetadata = -1;
    private int stabilisationFieldMetadata = -1;

    /** Render-block animation on/off (screwdriver toggle, persisted in NBT). */
    private boolean animationsEnabled = true;

    /** Star lifecycle model (persisted in NBT — the state survives chunk/world reloads). */
    private VoidcraftUSS uss = VoidcraftUSS.cold();

    /**
     * Infrastructure build progress (Phase 4 pass 2 — Constructor missions apply their loadouts here). Persists in
     * NBT across reloads and star burnouts (the infrastructure belongs to the system, not to the current star).
     */
    private USSInfrastructure infrastructure = new USSInfrastructure();

    // Region mining mission (Phase 4 pass 5 — up to USSConstants.MAX_SHIPS_PER_USS ships in flight per USS; a large
    // fleet (dozens–hundreds) rendered by ONE fleet anchor block, not one block per ship).

    /**
     * The ships in flight; list index = ship SLOT (launch order). Each ship carries its own cargo (built when its
     * MINING leg completes) and its own return targets (captured at launch), so a mission from any gateway/bay pair
     * is routed back to its own launchers.
     */
    private final List<VoidcraftActiveShip> activeShips = new ArrayList<>();

    /**
     * The pilots — one per in-flight ship (programming framework, Phase C), index-parallel to {@link #activeShips}.
     * Each pilot runs its ship's program (the controller's instruction list) against this MTE (the
     * {@link USSPilotWorld} game seam) and decides the ship's legs; the ships themselves are passive leg drivers.
     */
    private final List<USSShipPilot> pilots = new ArrayList<>();

    /**
     * The state id last pushed to each slot's ship render TE (avoids per-tick description packets). Starts at -1
     * ("nothing pushed yet"); on NBT load it is set to each restored ship's current state — the slot's render TE
     * already holds it (it is a separate world block with its own NBT), so no re-push is needed after a reload.
     */
    private final int[] lastPushedShipStates = initLastPushedShipStates();

    private static int[] initLastPushedShipStates() {
        int[] states = new int[USSConstants.MAX_SHIPS_PER_USS];
        Arrays.fill(states, -1);
        return states;
    }

    /**
     * The leg id last pushed to each slot's ship render TE (programming framework, Phase C): the leg ID
     * ({@link VoidcraftActiveShip#getLegId()}) is pushed alongside the state so a leg of the SAME state as the
     * previous one (a program doing MOVE → MOVE) still animates from its own start — the client resets its
     * leg-progress phase when the leg id changes.
     */
    private final int[] lastPushedLegIds = initLastPushedLegIds();

    private static int[] initLastPushedLegIds() {
        int[] ids = new int[USSConstants.MAX_SHIPS_PER_USS];
        Arrays.fill(ids, -1);
        return ids;
    }

    /**
     * Fleet anchor dirty flag (Phase 4 pass 5): set when any ship's state changes during a tick — the WHOLE fleet
     * is pushed to its one render block at most once per tick (a full-fleet description packet beats N per-ship
     * packets once the fleet has dozens–hundreds of ships).
     */
    private boolean fleetDirty;

    /**
     * Once per MTE lifetime (in-memory; the check is idempotent) — see {@link #cleanupLegacyShipRender()}.
     */
    private boolean legacyShipRenderCleaned;

    /**
     * Plain log4j logger at INFO (NOT {@code GTLog.conditionalLogger}, which forces Level.OFF) — mission lifecycle
     * must actually reach the game log.
     */
    private static final Logger LOGGER = LogManager.getLogger("Voidcraft USS");

    /**
     * Pass 26 (user: "add logging for the different missions and movements, that displays the calculated times and
     * progress"): the interval in WORLD TICKS between per-ship progress heartbeats (200 ticks = 10 in-game
     * seconds). Keeps the game log informative (a line per in-flight ship every 10 s) without per-tick spam.
     */
    private static final long PROGRESS_LOG_INTERVAL = 200L;

    // endregion

    // NBT tag names (voidcraft "vc_" naming convention).
    private static final String USS_NBT_TAG = "vc_uss";
    private static final String ANIMATIONS_ENABLED_NBT_TAG = "vc_animations_enabled";
    /** Phase 4 pass 4: NBTTagList of in-flight ships (slot order); the render anchors are derived, not stored. */
    private static final String ACTIVE_SHIPS_NBT_TAG = "vc_active_ships";
    private static final String INFRASTRUCTURE_NBT_TAG = "vc_uss_infrastructure";

    // Multiblock structure.
    /**
     * The legacy 33×33×33 EoH sphere (transposed into the structurelib layout) — the base shape for pass 12.
     */
    private static final String[][] LEGACY_EOH_SHAPE = transpose(
        new String[][] {
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "               C C               ", "            CCCCCCCCC            ",
                "               C C               ", "            CCCCCCCCC            ",
                "               C C               ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "               C C               ", "               C C               ",
                "               C C               ", "              DDDDD              ",
                "             DDCDCDD             ", "         CCCCDCCDCCDCCCC         ",
                "             DDDDDDD             ", "         CCCCDCCDCCDCCCC         ",
                "             DDCDCDD             ", "              DDDDD              ",
                "               C C               ", "               C C               ",
                "               C C               ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "               C C               ", "               C C               ",
                "                D                ", "                D                ",
                "             DDDDDDD             ", "            DD     DD            ",
                "            D  EEE  D            ", "       CCC  D EAAAE D  CCC       ",
                "          DDD EAAAE DDD          ", "       CCC  D EAAAE D  CCC       ",
                "            D  EEE  D            ", "            DD     DD            ",
                "             DDDDDDD             ", "                D                ",
                "                D                ", "               C C               ",
                "               C C               ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "                D                ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "      CC                 CC      ",
                "        DD             DD        ", "      CC                 CC      ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                D                ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "              CCCCC              ", "                D                ",
                "                A                ", "                A                ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "      C                   C      ", "     CC                   CC     ",
                "      CDAA             AADC      ", "     CC                   CC     ",
                "      C                   C      ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                A                ",
                "                A                ", "                D                ",
                "              CCCCC              ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "             SEEAEES             ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "       S                 S       ",
                "       E                 E       ", "    CC E                 E CC    ",
                "      DA                 AD      ", "    CC E                 E CC    ",
                "       E                 E       ", "       S                 S       ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "             SEEAEES             ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "              CCCCC              ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "    C                       C    ", "   CC                       CC   ",
                "    CDA                   ADC    ", "   CC                       CC   ",
                "    C                       C    ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "              CCCCC              ", "               C C               ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "             SEEAEES             ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     S                     S     ",
                "     E                     E     ", "  CC E                     E CC  ",
                "    DA                     AD    ", "  CC E                     E CC  ",
                "     E                     E     ", "     S                     S     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "             SEEAEES             ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "               C C               ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "  C                           C  ",
                "   DA                       AD   ", "  C                           C  ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "               C C               ", "                                 ",
                "                                 " },
            { "                                 ", "               C C               ",
                "               C C               ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " CC                           CC ",
                "   DA                       AD   ", " CC                           CC ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "               C C               ", "               C C               ",
                "                                 " },
            { "                                 ", "               C C               ",
                "                D                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " C                             C ",
                "  D                           D  ", " C                             C ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                D                ", "               C C               ",
                "                                 " },
            { "                                 ", "               C C               ",
                "                D                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " C                             C ",
                "  D                           D  ", " C                             C ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                D                ", "               C C               ",
                "                                 " },
            { "             CCCCCCC             ", "               C C               ",
                "             DDDDDDD             ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "  D                           D  ",
                "  D                           D  ", "CCD                           DCC",
                "  D                           D  ", "CCD                           DCC",
                "  D                           D  ", "  D                           D  ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "             DDDDDDD             ", "               C C               ",
                "               C C               " },
            { "            CCHHHHHCC            ", "              DDDDD              ",
                "            DD     DD            ", "                                 ",
                "                                 ", "       S                 S       ",
                "                                 ", "     S                     S     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "  D                           D  ", "  D                           D  ",
                " D                             D ", "CD                             DC",
                " D                             D ", "CD                             DC",
                " D                             D ", "  D                           D  ",
                "  D                           D  ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     S                     S     ",
                "                                 ", "       S                 S       ",
                "                                 ", "                                 ",
                "            DD     DD            ", "              DDDDD              ",
                "               C C               " },
            { "            CHHHHHHHC            ", "             DDCDCDD             ",
                "            D  EEE  D            ", "                                 ",
                "      C                   C      ", "       E                 E       ",
                "    C                       C    ", "     E                     E     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "  D                           D  ", " D                             D ",
                " D                             D ", "CCE                           ECC",
                " DE                           ED ", "CCE                           ECC",
                " D                             D ", " D                             D ",
                "  D                           D  ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     E                     E     ",
                "    C                       C    ", "       E                 E       ",
                "      C                   C      ", "                                 ",
                "            D  EEE  D            ", "             DDCDCDD             ",
                "               C C               " },
            { "            CHHCCCHHC            ", "         CCCCDCCDCCDCCCC         ",
                "       CCC  D EAAAE D  CCC       ", "      CC                 CC      ",
                "     CC                   CC     ", "    CC E                 E CC    ",
                "   CC                       CC   ", "  CC E                     E CC  ",
                "  C                           C  ", " CC                           CC ",
                " C                             C ", " C                             C ",
                "CCD                           DCC", "CD                             DC",
                "CCE                           ECC", "CCA                           ACC",
                "CDA                           ADC", "CCA                           ACC",
                "CCE                           ECC", "CD                             DC",
                "CCD                           DCC", " C                             C ",
                " C                             C ", " CC                           CC ",
                "  C                           C  ", "  CC E                     E CC  ",
                "   CC                       CC   ", "    CC E                 E CC    ",
                "     CC                   CC     ", "      CC                 CC      ",
                "       CCC  D EAAAE D  CCC       ", "         CCCCDCCDCCDCCCC         ",
                "            CCCCCCCCC            " },
            { "            CHHC~CHHC            ", "             DDDDDDD             ",
                "          DDD EAAAE DDD          ", "        DD             DD        ",
                "      CDAA             AADC      ", "      DA                 AD      ",
                "    CDA                   ADC    ", "    DA                     AD    ",
                "   DA                       AD   ", "   DA                       AD   ",
                "  D                           D  ", "  D                           D  ",
                "  D                           D  ", " D                             D ",
                " DE                           ED ", "CDA                           ADC",
                " DA                           AD ", "CDA                           ADC",
                " DE                           ED ", " D                             D ",
                "  D                           D  ", "  D                           D  ",
                "  D                           D  ", "   DA                       AD   ",
                "   DA                       AD   ", "    DA                     AD    ",
                "    CDA                   ADC    ", "      DA                 AD      ",
                "      CDAA             AADC      ", "        DD             DD        ",
                "          DDD EAAAE DDD          ", "             DDDDDDD             ",
                "               C C               " },
            { "            CHHCCCHHC            ", "         CCCCDCCDCCDCCCC         ",
                "       CCC  D EAAAE D  CCC       ", "      CC                 CC      ",
                "     CC                   CC     ", "    CC E                 E CC    ",
                "   CC                       CC   ", "  CC E                     E CC  ",
                "  C                           C  ", " CC                           CC ",
                " C                             C ", " C                             C ",
                "CCD                           DCC", "CD                             DC",
                "CCE                           ECC", "CCA                           ACC",
                "CDA                           ADC", "CCA                           ACC",
                "CCE                           ECC", "CD                             DC",
                "CCD                           DCC", " C                             C ",
                " C                             C ", " CC                           CC ",
                "  C                           C  ", "  CC E                     E CC  ",
                "   CC                       CC   ", "    CC E                 E CC    ",
                "     CC                   CC     ", "      CC                 CC      ",
                "       CCC  D EAAAE D  CCC       ", "         CCCCDCCDCCDCCCC         ",
                "            CCCCCCCCC            " },
            { "            CHHHHHHHC            ", "             DDCDCDD             ",
                "            D  EEE  D            ", "                                 ",
                "      C                   C      ", "       E                 E       ",
                "    C                       C    ", "     E                     E     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "  D                           D  ", " D                             D ",
                " D                             D ", "CCE                           ECC",
                " DE                           ED ", "CCE                           ECC",
                " D                             D ", " D                             D ",
                "  D                           D  ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     E                     E     ",
                "    C                       C    ", "       E                 E       ",
                "      C                   C      ", "                                 ",
                "            D  EEE  D            ", "             DDCDCDD             ",
                "               C C               " },
            { "            CCHHHHHCC            ", "              DDDDD              ",
                "            DD     DD            ", "                                 ",
                "                                 ", "       S                 S       ",
                "                                 ", "     S                     S     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "  D                           D  ", "  D                           D  ",
                " D                             D ", "CD                             DC",
                " D                             D ", "CD                             DC",
                " D                             D ", "  D                           D  ",
                "  D                           D  ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     S                     S     ",
                "                                 ", "       S                 S       ",
                "                                 ", "                                 ",
                "            DD     DD            ", "              DDDDD              ",
                "               C C               " },
            { "             CCCCCCC             ", "               C C               ",
                "             DDDDDDD             ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "  D                           D  ",
                "  D                           D  ", "CCD                           DCC",
                "  D                           D  ", "CCD                           DCC",
                "  D                           D  ", "  D                           D  ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "             DDDDDDD             ", "               C C               ",
                "               C C               " },
            { "                                 ", "               C C               ",
                "                D                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " C                             C ",
                "  D                           D  ", " C                             C ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                D                ", "               C C               ",
                "                                 " },
            { "                                 ", "               C C               ",
                "                D                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " C                             C ",
                "  D                           D  ", " C                             C ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                D                ", "               C C               ",
                "                                 " },
            { "                                 ", "               C C               ",
                "               C C               ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " CC                           CC ",
                "   DA                       AD   ", " CC                           CC ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "               C C               ", "               C C               ",
                "                                 " },
            { "                                 ", "                                 ",
                "               C C               ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "  C                           C  ",
                "   DA                       AD   ", "  C                           C  ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "               C C               ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "             SEEAEES             ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     S                     S     ",
                "     E                     E     ", "  CC E                     E CC  ",
                "    DA                     AD    ", "  CC E                     E CC  ",
                "     E                     E     ", "     S                     S     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "             SEEAEES             ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "              CCCCC              ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "    C                       C    ", "   CC                       CC   ",
                "    CDA                   ADC    ", "   CC                       CC   ",
                "    C                       C    ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "              CCCCC              ", "               C C               ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "             SEEAEES             ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "       S                 S       ",
                "       E                 E       ", "    CC E                 E CC    ",
                "      DA                 AD      ", "    CC E                 E CC    ",
                "       E                 E       ", "       S                 S       ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "             SEEAEES             ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "              CCCCC              ", "                D                ",
                "                A                ", "                A                ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "      C                   C      ", "     CC                   CC     ",
                "      CDAA             AADC      ", "     CC                   CC     ",
                "      C                   C      ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                A                ",
                "                A                ", "                D                ",
                "              CCCCC              ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "                D                ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "      CC                 CC      ",
                "        DD             DD        ", "      CC                 CC      ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                D                ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "               C C               ", "               C C               ",
                "                D                ", "                D                ",
                "             DDDDDDD             ", "            DD     DD            ",
                "            D  EEE  D            ", "       CCC  D EAAAE D  CCC       ",
                "          DDD EAAAE DDD          ", "       CCC  D EAAAE D  CCC       ",
                "            D  EEE  D            ", "            DD     DD            ",
                "             DDDDDDD             ", "                D                ",
                "                D                ", "               C C               ",
                "               C C               ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "               C C               ", "               C C               ",
                "               C C               ", "              DDDDD              ",
                "             DDCDCDD             ", "         CCCCDCCDCCDCCCC         ",
                "             DDDDDDD             ", "         CCCCDCCDCCDCCCC         ",
                "             DDCDCDD             ", "              DDDDD              ",
                "               C C               ", "               C C               ",
                "               C C               ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "               C C               ", "            CCCCCCCCC            ",
                "               C C               ", "            CCCCCCCCC            ",
                "               C C               ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " } });

    /**
     * Pass 12 (user: "the old EoH structure is a bit small for the increased scope of Voidcraft — 2× bigger
     * radius"): the Voidcraft structure — the legacy sphere scaled uniformly 2× about its center (65×65×65). Same
     * shape and block composition — every block simply moves to 2× its distance from the sphere center, so the
     * per-type counts are unchanged — and the `~` controller slot lands at (32,32,0), the new anchor.
     */
    private static final String[][] STRUCTURE_SHAPE = scaleShape2x(LEGACY_EOH_SHAPE);

    private static final IStructureDefinition<MTEUnstableSolarSystem> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEUnstableSolarSystem>builder()
        .addShape(STRUCTURE_PIECE_MAIN, STRUCTURE_SHAPE)
        .addElement(
            'A',
            GTStructureChannels.EOH_COMPRESSION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.SpacetimeCompressionFieldGenerators ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 0),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 1),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 2),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 3),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 4),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 5),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 6),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 7),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 8)),
                    -1,
                    (t, meta) -> t.spacetimeCompressionFieldMetadata = meta,
                    t -> t.spacetimeCompressionFieldMetadata)))
        .addElement(
            'S',
            GTStructureChannels.EOH_STABILISATION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.StabilisationFieldGenerators ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 0),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 1),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 2),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 3),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 4),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 5),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 6),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 7),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 8)),
                    -1,
                    (t, meta) -> t.stabilisationFieldMetadata = meta,
                    t -> t.stabilisationFieldMetadata)))
        .addElement('C', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 11))
        .addElement('D', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10))
        .addElement(
            'H',
            buildHatchAdder(MTEUnstableSolarSystem.class)
                .atLeast(InputBus, InputHatch, InputHatch, OutputBus, OutputHatch)
                .casingIndex(Casings.InfiniteSpacetimeEnergyBoundaryCasing.getTextureId())
                .hint(1)
                .buildAndChain(Casings.InfiniteSpacetimeEnergyBoundaryCasing.asElement()))
        .addElement(
            'E',
            GTStructureChannels.EOH_DILATION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.TimeAccelerationFieldGenerator ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 0),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 1),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 2),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 3),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 4),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 5),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 6),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 7),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)),
                    -1,
                    (t, meta) -> t.timeAccelerationFieldMetadata = meta,
                    t -> t.timeAccelerationFieldMetadata)))
        .build();

    /**
     * Pass 12 (user: "2x bigger radius"): uniform 2x point-scale of the legacy 33x33x33 shape about its center.
     *
     * <p>
     * Every non-space character at (i, j, k) is re-placed at (2i, 2j, 2k) of the output 65x65x65 grid: the shape
     * center maps 16 -&gt; 32, the {@code ~} controller slot maps (16,16,0) -&gt; (32,32,0), and every per-type block
     * count is unchanged - this is a point-scale (same one-block-thick shell, twice the radius), not a thickening.
     * The output grid is space-filled; the scaling commutes with {@code transpose}, so the legacy literal stays
     * readable and only the derived constant grows.
     */
    private static String[][] scaleShape2x(String[][] shape) {
        final int n = shape.length;
        if (n != 33) {
            throw new IllegalArgumentException("scaleShape2x expects a 33x33x33 shape, got " + n + "x...");
        }
        // A 33-cell axis (indices 0–32) scales to indices 0–64 = 65 cells, NOT 66 (2·33 would add a phantom space
        // slice).
        final int m = 2 * n - 1;
        final char[][][] grid = new char[m][m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                Arrays.fill(grid[i][j], ' ');
            }
        }
        for (int i = 0; i < n; i++) {
            final String[] rows = shape[i];
            if (rows.length != n) {
                throw new IllegalArgumentException(
                    "scaleShape2x expects 33x33x33, got " + n + "x" + rows.length + "x...");
            }
            for (int j = 0; j < n; j++) {
                final String row = rows[j];
                if (row.length() != n) {
                    throw new IllegalArgumentException("scaleShape2x expects 33-char rows, got " + row.length());
                }
                for (int k = 0; k < n; k++) {
                    final char ch = row.charAt(k);
                    if (ch != ' ') {
                        grid[2 * i][2 * j][2 * k] = ch;
                    }
                }
            }
        }
        final String[][] out = new String[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                out[i][j] = new String(grid[i][j]);
            }
        }
        return out;
    }

    // Region machine logic.

    @Override
    public IStructureDefinition<MTEUnstableSolarSystem> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public MTEUnstableSolarSystem(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEUnstableSolarSystem(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEUnstableSolarSystem(mName);
    }

    /**
     * Same structural rules as the legacy EoH (copied verbatim): 32,32,0 anchor (pass 12: the legacy 16,16,0 scaled
     * 2× with the 65×65×65 shape), no CRIb, no energy hatches, exactly 1 non-stocking input bus, 2 non-stocking
     * input hatches, 1 output bus, 1 output hatch.
     */
    @Override
    public void checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack,
        List<StructureError> errors) {
        spacetimeCompressionFieldMetadata = -1;
        timeAccelerationFieldMetadata = -1;
        stabilisationFieldMetadata = -1;

        // Check structure of multi.
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 32, 32, 0, errors)) return;

        // Make sure there are no Crafting Input Buffers/Buses/Slaves.
        if (!mDualInputHatches.isEmpty()) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.crib_not_allowed"));
        }

        // Make sure there are no energy hatches (the Phase 6 energy model uses the global energy map, not hatches).
        if (!mEnergyHatches.isEmpty() || !mExoticEnergyHatches.isEmpty()) {
            errors.add(StructureErrorRegistry.NO_ENERGY_HATCH_NEEDED);
        }

        // Check there is 1 input bus, and it is not a stocking input bus.
        {
            if (mInputBusses.size() != 1) {
                errors.add(StructureErrors.hatchCount(ErrorType.NOT_MATCH, InputBus, mInputBusses.size(), 1));
            } else if (mInputBusses.get(0) instanceof MTEHatchInputBusME) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.stocking_input_bus_not_allowed"));
            }
        }

        if (mInputHatches.size() != 2) {
            errors.add(StructureErrors.hatchCount(ErrorType.NOT_MATCH, InputHatch, mInputHatches.size(), 2));
        }
        for (MTEHatchInput inputHatch : mInputHatches) {
            if (inputHatch instanceof MTEHatchInputME) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.stocking_input_hatch_not_allowed"));
                break;
            }
        }
        checkOneOutputBus(errors);
        checkOneOutputHatch(errors);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        animationsEnabled = !animationsEnabled;
        if (uss != null && uss.isIgnited()) {
            if (animationsEnabled) {
                createRenderBlock(uss.getTier());
            } else {
                destroyRenderBlock();
            }
        }
        GTUtility.sendChatTrans(aPlayer, "GT5U.machines.animations." + (animationsEnabled ? "enabled" : "disabled"));
    }

    /**
     * Insert the USS Controller into the empty controller slot (sneak or plain right-click with the item in hand).
     */
    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (getControllerSlot() == null) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (heldItem != null && heldItem.getItem() == ItemUSSController.INSTANCE) {
                mInventory[getControllerSlotIndex()] = heldItem.copy();
                mInventory[getControllerSlotIndex()].stackSize = 1;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem, 1));
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public CheckRecipeResult checkProcessing_EM() {
        // Not a recipe machine — the star lifecycle runs in onPostTick. (NO_RECIPE = normal idle.)
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    // Region star state machine.

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        cleanupLegacyShipRender();
        if (!mMachine) return;
        if (uss == null) {
            uss = VoidcraftUSS.cold();
        }
        if (uss.isIgnited()) {
            // The star burns: one machine tick of lifespan per tick (no power draw yet — Phase 6).
            long remaining = uss.getLifespanRemaining() - 1;
            if (remaining <= 0) {
                starBurnsOut();
            } else {
                uss = uss.withLifespan(remaining);
                tickShips();
            }
        } else if (getControllerSlot() != null) {
            // COLD + controller in slot + structure valid → ignite.
            igniteStar();
        }
    }

    /**
     * Ignite the star with the star class carried by the controller item in the controller slot (Phase 4 pass 1 —
     * the ITEM decides the star type; the structure's tier sets the star's rendering size, and the star type
     * decides the planet system a Miner can work). An unrecognized item in the slot leaves the star cold and keeps
     * the item.
     */
    private void igniteStar() {
        USSStarType starType = ItemUSSController.starTypeOf(getControllerSlot());
        if (starType == null) {
            return; // not a controller — the star stays COLD, the slot keeps whatever is in it
        }
        int tier = USSConstants.clampTier(spacetimeCompressionFieldMetadata);
        uss = VoidcraftUSS.ignite(tier, starType, System.currentTimeMillis());
        if (animationsEnabled) {
            createRenderBlock(tier);
        }
    }

    /**
     * The star has burned out: remove the render block, consume the controller (one controller = one star life) and
     * return to COLD.
     */
    private void starBurnsOut() {
        // The star dies with its ships: every mission in flight is lost (the design choice for this slice).
        discardAllShips();
        uss = uss.toCold();
        destroyRenderBlock();
        if (mInventory[getControllerSlotIndex()] != null) {
            mInventory[getControllerSlotIndex()] = null;
        }
        updateSlots();
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        if (uss != null && uss.isIgnited()) {
            uss = uss.toCold();
        }
        discardAllShips();
        destroyRenderBlock();
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        discardAllShips();
        destroyRenderBlock();
    }

    // endregion

    /**
     * Place the render block 32 blocks behind the controller (pass 12: the legacy 16-block star offset scaled 2×
     * with the 65×65×65 shape). The shared {@link TileEntityEyeOfHarmony} render TE is used as-is, configured for
     * the star tier AND for the doubled space-shell radius (legacy EoH keeps the 12.95 default).
     */
    private void createRenderBlock(int tier) {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        if (gregTechTileEntity == null) return;

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        gregTechTileEntity.getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        gregTechTileEntity.getWorld()
            .setBlock(
                (int) (x + xOffset),
                (int) (y + yOffset),
                (int) (z + zOffset),
                TTCasingsContainer.eyeOfHarmonyRenderBlock);
        TileEntityEyeOfHarmony rendererTileEntity = (TileEntityEyeOfHarmony) gregTechTileEntity.getWorld()
            .getTileEntity((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset));

        if (rendererTileEntity != null) {
            rendererTileEntity.setTier(tier);
            // Star size: 0.5·√(sampled size), a pure function of the star type + ignition timestamp (the
            // mechanics pass — see starSizeFor).
            rendererTileEntity.setStarSize(starSizeFor(uss.getStarType(), uss.getIgnitedAt()));
            // Pass 12: the Voidcraft structure is 2× the legacy radius, so the space shell doubles with it
            // (star and planet sizes stay unchanged).
            rendererTileEntity.setDomeRadius(USSConstants.SPACE_SHELL_RADIUS);
            // Phase 4 pass 3: the system's PLANETS — deterministic (star type + ignition timestamp), so the legacy
            // orbit renderer draws exactly the bodies the miner works (see getPlanets / USSPlanets).
            rendererTileEntity.setPlanets(planetSpecsFor(getPlanets()));
        }
        // Push the TE data (tier/size/planets) to the client now — in 1.7.10 nothing else does until a chunk reload
        // (markBlockForUpdate sends the description packet to nearby players; see
        // TileEntityVoidcraftShip.syncToClient).
        int sx = (int) (x + xOffset);
        int sy = (int) (y + yOffset);
        int sz = (int) (z + zOffset);
        gregTechTileEntity.getWorld()
            .markBlockForUpdate(sx, sy, sz);
    }

    /**
     * Convert the system's planets into render specs (hologram texture + orbit parameters + fallback tint + ring
     * texture) for the shared EoH render TE. The texture is the planet's bundled {@code stitched.png} (the renderer
     * binds it and draws the shared cube); the ring texture is the planet's orbit-ring image when it has one
     * (empty otherwise). The color from {@link USSPlanetColor} is the tinted-sphere fallback tint if a texture is
     * missing. The specs keep the exact orbit math the ship hover/beam track (USSFleetOrbit).
     */
    private static List<TileEntityEyeOfHarmony.PlanetSpec> planetSpecsFor(List<USSPlanets.USSPlanet> planets) {
        List<TileEntityEyeOfHarmony.PlanetSpec> specs = new ArrayList<>();
        for (USSPlanets.USSPlanet planet : planets) {
            final String ringTexture = planet.hasRing && planet.ringVariant > 0
                ? "textures/uss/rings/" + planet.definition.getTier()
                    .ringDir() + "/" + planet.ringVariant + ".png"
                : "";
            specs.add(
                new TileEntityEyeOfHarmony.PlanetSpec(
                    planet.definition.getTexture(),
                    (float) planet.distance,
                    (float) planet.scale,
                    (float) planet.orbitSpeed,
                    (float) planet.rotationSpeed,
                    (float) planet.xAngle,
                    (float) planet.zAngle,
                    USSPlanetColor.colorFor(planet.definition),
                    ringTexture));
        }
        return specs;
    }

    /**
     * The star's rendered size (the mechanics pass: 0.5·√(sampled size), the sampled size being a pure function of
     * the star type + ignition timestamp — see {@link USSPlanets#starRenderSize(double)} and
     * {@link USSPlanets#sampleStarSize(USSStarType, long)}). Pass 7: the fleet TE carries this value so the client
     * computes the planet orbit radii EXACTLY like {@code EOHRenderingUtils.renderUSSOrbits} (radius = 0.2 +
     * distance + 0.2·starSize) — ships hover precisely above the rendered planets.
     */
    private static float starSizeFor(USSStarType starType, long seed) {
        return USSPlanets.starRenderSize(USSPlanets.sampleStarSize(starType, seed));
    }

    /**
     * Remove the render block (if present) at the standard offset — pass 12: both the new 32-block offset and the
     * legacy 16-block offset (one-time cleanup of render blocks left by pre-pass-12 33×33×33 builds).
     */
    private void destroyRenderBlock() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        if (gregTechTileEntity == null) return;

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        for (int offset : new int[] { 16, 32 }) {
            double xOffset = offset * getExtendedFacing().getRelativeBackInWorld().offsetX;
            double zOffset = offset * getExtendedFacing().getRelativeBackInWorld().offsetZ;
            double yOffset = offset * getExtendedFacing().getRelativeBackInWorld().offsetY;

            gregTechTileEntity.getWorld()
                .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        }
    }

    // Region mining mission (Phase 3).

    /**
     * @return true if the star is currently ignited (the gateway's radius scan uses this).
     */
    public boolean isStarIgnited() {
        return uss != null && uss.isIgnited();
    }

    /** @return true if at least one ship is currently in flight. */
    public boolean hasActiveShip() {
        return !activeShips.isEmpty();
    }

    /**
     * @return true when the USS can accept another launch (Phase 4 pass 4 — up to
     *         {@code USSConstants.MAX_SHIPS_PER_USS} ships fly simultaneously). The gateway's radius scan skips
     *         FULL systems, so a rejected Constructor launch (which would already have consumed its loadout) can
     *         only happen on a capacity race, not on the normal "already busy" path.
     */
    public boolean hasFreeShipSlot() {
        return activeShips.size() < USSConstants.MAX_SHIPS_PER_USS;
    }

    /** @return the number of ships currently in flight (0…{@code MAX_SHIPS_PER_USS}). */
    public int getActiveShipCount() {
        return activeShips.size();
    }

    /** @return the ships in flight, in launch order (read-only). */
    public List<VoidcraftActiveShip> getActiveShips() {
        return Collections.unmodifiableList(activeShips);
    }

    /**
     * @return the infrastructure build progress (never null; the gateway reads the first incomplete project from
     *         it when preparing a Constructor launch)
     */
    public USSInfrastructure getInfrastructure() {
        return infrastructure != null ? infrastructure : (infrastructure = new USSInfrastructure());
    }

    /**
     * The system's planets (Phase 4 pass 3): a deterministic pure function of the star's TYPE and ignition
     * timestamp — the same list the Miner works (cargo), the render TE draws (its explicit planet system) and the
     * infodata displays. Empty while the star is COLD.
     *
     * @see USSPlanets#generate(USSStarType, long)
     */
    public List<USSPlanets.USSPlanet> getPlanets() {
        if (uss == null || !uss.isIgnited()) {
            return Collections.emptyList();
        }
        return USSPlanets.generate(uss.getStarType(), uss.getIgnitedAt());
    }

    /**
     * The system's spacetime-ripple FIELD (the Explorer pass): a deterministic pure function of the star's TYPE and
     * ignition timestamp — the same 343-point grid the Explorers scan (mechanics) and the client renders (the three
     * shells). The SCAN STATE (which points are revealed) lives on the {@link VoidcraftUSS} model (see
     * {@link #uss}), not here. Null while the star is COLD.
     *
     * @see USSRipples#generate(USSStarType, long)
     */
    public USSRippleField getRippleField() {
        if (uss == null || !uss.isIgnited()) {
            return null;
        }
        return USSRipples.generate(uss.getStarType(), uss.getIgnitedAt());
    }

    /**
     * The REVEALED spacetime-ripple positions (the Explorer pass): the ripple points that have been SCANNED (revealed)
     * and ARE ripples, as {@code [x, y, z]} in fleet-anchor blocks (the same frame the client star/ship renderer
     * draws in). Hidden ripples (not yet scanned) and revealed NON-ripples are absent — only revealed ripples render.
     *
     * @return the revealed ripple positions (never null; empty when nothing is revealed)
     */
    private List<float[]> revealedRipplePositions() {
        List<float[]> out = new ArrayList<>();
        USSRippleField field = getRippleField();
        if (field == null || uss == null) {
            return out;
        }
        for (int index : uss.getScannedRipples()) {
            if (index >= 0 && index < field.size() && field.isRipple(index)) {
                USSPosition p = field.positionOf(index);
                out.add(new float[] { (float) p.x(), (float) p.y(), (float) p.z() });
            }
        }
        return out;
    }

    /**
     * Launch a mission with the given ship payload (called by the {@code MTEVoidcraftGateway} after validating the
     * ship).
     *
     * <p>
     * Programming framework (Phase C): the ship no longer gets a mission TARGET at launch — it gets a PILOT that
     * runs the ship's PROGRAM (the controller's instruction list, carried in the payload's
     * {@code vc_program} tag). The ship starts HOLDING at its launch origin (the gateway, in fleet-anchor
     * coordinates) and moves only when its program tells it to. A ship without a program holds forever (the
     * program UI is the next pass).
     *
     * @param payload    the ship payload — the item's tag compound (blueprint + denormalized stats, vc_* keys at its
     *                   top level, + the optional vc_program), as written by the Assembler and read back by
     *                   {@code VoidcraftNbt}
     * @param gatewayPos gateway world position (the OUTBOUND/RETURNING endpoint + the surviving ship's
     *                   re-emission target) — carried by the SHIP, so each mission routes back to its own launcher
     * @param bayPos     storage-bay world position (the cargo delivery target) — carried by the SHIP
     * @return true when the ship is in flight; false when the USS rejects the launch (cold star, full, or invalid
     *         payload)
     */
    public boolean launchShip(NBTTagCompound payload, int[] gatewayPos, int[] bayPos) {
        if (uss == null || !uss.isIgnited() || activeShips.size() >= USSConstants.MAX_SHIPS_PER_USS) {
            return false;
        }
        if (payload == null || VoidcraftNbt.read(payload) == null) {
            return false;
        }
        String uuid = payload.hasKey(VoidcraftNbt.TAG_UUID) ? payload.getString(VoidcraftNbt.TAG_UUID) : "voidcraft";
        String name = payload.hasKey(VoidcraftNbt.TAG_NAME) ? payload.getString(VoidcraftNbt.TAG_NAME) : uuid;
        double speed = VoidcraftNbt.readDouble(payload, VoidcraftNbt.TAG_SPEED);
        long mining = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_MINING);
        // The integrity time limit (user design): the ship enters the USS at its MAXIMUM (the blueprint's total)
        // and it counts down 1 per second until it is either gone (lost) or back (survived).
        long integrity = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_INTEGRITY);
        int slot = activeShips.size();
        // Pass 5.1: a fresh per-launch identity seed — duplicated ship items share the item UUID, so the client's
        // per-ship animation phases + swarm spread must be keyed on this (unique per flight), not the UUID.
        int seed = new Random().nextInt();
        // Phase C: the launch origin — the gateway in fleet-anchor coordinates (the same rel() the client's
        // TAG_ENTRY_GW_REL uses, so the client draws the fresh ship exactly where the server has it). A null
        // gateway (defensive) degrades to the anchor itself.
        IGregTechTileEntity base = getBaseMetaTileEntity();
        int[] anchor = (base != null) ? shipAnchorPos(base) : new int[] { 0, 0, 0 };
        int[] gatewayWorld = (gatewayPos != null) ? gatewayPos : anchor;
        int[] gwRel = rel(anchor, gatewayWorld);
        USSPosition origin = USSPosition.of(gwRel[0], gwRel[1], gwRel[2]);
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch(uuid, name, speed, mining, payload, gatewayPos, bayPos, seed, origin);
        // Phase C: the ship's program (the controller's instruction list) + the pilot that runs it. A corrupt
        // program degrades to an empty one (the ship HOLDS at the origin — never a half-run).
        NBTTagList programTag = payload.hasKey(VoidcraftNbt.TAG_PROGRAM)
            ? payload.getTagList(VoidcraftNbt.TAG_PROGRAM, 10)
            : null;
        USSProgram program = USSProgram.readFromNBT(programTag);
        USSShipPilot pilot = USSShipPilot.create(ship, program, this, seed);
        activeShips.add(ship);
        pilots.add(pilot);
        // Pass 26 (user: "add logging for the different missions and movements, that displays the calculated times
        // and progress"): one line at launch with the ship's identity and its program — the leg durations are
        // calculated per leg by the pilot (the legs no longer exist at launch; they are the program's).
        try {
            int roles = VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ROLES);
            int instructions = program == null ? 0 : program.nodeCount();
            LOGGER.info(
                "[Voidcraft] LAUNCH {} — roles=0x{}, origin={} blocks, speed={}, program={} instruction(s), "
                    + "integrity time limit={}s",
                name,
                Integer.toHexString(roles),
                String.format("%.3f", origin.x()) + ","
                    + String.format("%.3f", origin.y())
                    + ","
                    + String.format("%.3f", origin.z()),
                String.format("%.3f", speed),
                instructions,
                integrity);
        } catch (Throwable ignored) {}
        lastPushedShipStates[slot] = -1;
        lastPushedLegIds[slot] = -1;
        syncFleetRenderBlock(); // Phase 4 pass 5: the whole fleet (now including this ship) goes into ONE anchor block
        return true;
    }

    /**
     * Advance every ship in flight one tick (called from {@link #onPostTick} while the star is ignited). Ships
     * that end this tick — COMPLETED (their program's HOME leg just finished) or LOST (integrity reached 0) —
     * are finished AFTER the tick loop, in one index-safe reverse pass (completed → {@link #completeShip}, lost →
     * {@link #loseShip}).
     *
     * <p>
     * Programming framework (Phase C): the loop is now a PILOT loop — each pilot ticks its ship (the ship's leg
     * countdown + the program executor) and reports when a HOME leg completes (the mission is over —
     * {@link #completeShip} delivers). The work leg's yield (cargo / the Explorer reveal) is applied by the pilot
     * exactly once, through {@link #onWorkComplete}.
     *
     * <p>
     * Integrity time limit (user design): every ship's integrity drops by 1 per second
     * ({@link VoidcraftActiveShip#tickIntegrity()})
     * — a ship that hits 0 is removed immediately and its cargo is discarded.
     */
    private void tickShips() {
        if (activeShips.isEmpty()) {
            return;
        }
        List<Integer> completed = new ArrayList<>();
        List<Integer> lost = new ArrayList<>();
        for (int slot = 0; slot < activeShips.size(); slot++) {
            VoidcraftActiveShip ship = activeShips.get(slot);
            // The integrity time limit: 1 per second (the ship counts its own ticks — even while HOLDING). At 0
            // the ship is LOST: removed below, its cargo discarded (no delivery, no drop, no re-emission).
            if (ship.tickIntegrity()) {
                lost.add(slot);
                continue;
            }
            USSShipPilot pilot = pilots.get(slot);
            if (pilot.tick()) {
                // HOME leg complete — the mission is over (delivery + re-emission below).
                completed.add(slot);
                continue;
            }
            // Mark the fleet dirty when a ship's state OR leg id changes — pushed ONCE per tick at the end (no
            // per-tick packets, and one full-fleet push instead of one per ship — Phase 4 pass 5). The leg id
            // (Phase C) makes consecutive legs of the SAME state (MOVE → MOVE) animate from their own start.
            int stateId = ship.getState()
                .getId();
            int legId = ship.getLegId();
            if (lastPushedShipStates[slot] != stateId || lastPushedLegIds[slot] != legId) {
                // Diagnostic (pass 7): one line per state transition. If a ship appears to "turn back without
                // mining", the server's truth is right here — OUTBOUND → MINING → RETURNING in order, with the
                // MINING leg's duration in ticks (a long gap before RETURNING = the mission logic ran correctly).
                try {
                    LOGGER.info(
                        "[Voidcraft] {} mission: {} ({} ticks left, target={})",
                        ship.getName(),
                        ship.getState()
                            .name(),
                        ship.getTicksRemaining(),
                        ship.getTargetPlanet());
                } catch (Throwable ignored) {}
                lastPushedShipStates[slot] = stateId;
                lastPushedLegIds[slot] = legId;
                fleetDirty = true;
            }
            // Pass 26 (user: "…displays the calculated times and progress"): a periodic heartbeat — every
            // PROGRESS_LOG_INTERVAL world ticks, one line per in-flight ship showing the current leg and its
            // PROGRESS fraction (the ship's ticks-remaining against the leg's total calculated duration), so the
            // movement's progress is visible in the game log. The state-transition log above already prints the
            // leg's total (its "ticks left" at the transition); this adds the running progress in between.
            try {
                IGregTechTileEntity progressBase = getBaseMetaTileEntity();
                if (progressBase != null && progressBase.getWorld() != null) {
                    long worldTick = progressBase.getWorld()
                        .getWorldTime();
                    if (worldTick % PROGRESS_LOG_INTERVAL == 0L) {
                        long legTotal = USSConstants.legTicks(
                            ship.getState(),
                            ship.getTravelDistance(),
                            ship.getSpeed(),
                            ship.getMiningPower(),
                            ship.getRoles(),
                            ship.getScanPower());
                        double progress = legTotal > 0 ? (1.0 - (double) ship.getTicksRemaining() / (double) legTotal)
                            : 1.0;
                        LOGGER.info(
                            "[Voidcraft] PROGRESS {} — {} {}% ({} ticks left of {})",
                            ship.getName(),
                            ship.getState()
                                .name(),
                            String.format("%.0f", progress * 100.0),
                            ship.getTicksRemaining(),
                            legTotal);
                    }
                }
            } catch (Throwable ignored) {}
        }
        // Resolve the tick's endings (completions + losses) in ONE index-safe reverse pass — a ship cannot be
        // both (a lost ship is skipped before the pilot tick), but the two slot lists must not shift each other's
        // indices as the lists are mutated.
        List<Integer> ending = new ArrayList<>(completed);
        for (Integer s : lost) {
            if (!ending.contains(s)) {
                ending.add(s);
            }
        }
        ending.sort(java.util.Collections.reverseOrder());
        for (int slot : ending) {
            if (completed.contains(slot)) {
                completeShip(slot);
            } else {
                loseShip(slot);
            }
        }
        if (fleetDirty || !completed.isEmpty() || !lost.isEmpty()) {
            syncFleetRenderBlock(); // one push for every state change + ending of this tick
        }
    }

    /**
     * Ship LOST (the integrity time limit hit 0): it is removed from the USS and its cargo is DISCARDED — no
     * delivery to the bay, no drop at the USS, no re-emission. The fleet anchor is resynced by the CALLER (one
     * push for the whole fleet).
     */
    private void loseShip(int slot) {
        if (slot < 0 || slot >= activeShips.size()) {
            return;
        }
        VoidcraftActiveShip lostShip = activeShips.get(slot);
        activeShips.remove(slot);
        if (slot < pilots.size()) {
            pilots.remove(slot);
        }
        lastPushedShipStates[slot] = -1;
        lastPushedLegIds[slot] = -1;
        try {
            LOGGER.info(
                "[Voidcraft] LOST {} (slot {}) — integrity reached 0: ship removed, cargo discarded",
                lostShip.getName(),
                slot);
        } catch (Throwable ignored) {}
    }

    /**
     * Whether the given mission is a Phase 4 pass 2 Constructor mission (the gateway set the flag at launch after
     * loading the ship's payload).
     */
    private boolean isConstructorMission(VoidcraftActiveShip ship) {
        if (ship == null || ship.getPayload() == null) {
            return false;
        }
        NBTTagCompound payload = ship.getPayload();
        return payload.getBoolean(VoidcraftNbt.TAG_CONSTRUCTOR_MISSION)
            && VoidcraftRole.CONSTRUCTOR.isActive(ship.getRoles());
    }

    /**
     * Build the cargo of a completed MINER mission (the mechanics pass): the ship mines its ONE target planet
     * (pass 7 — {@link VoidcraftActiveShip#getTargetPlanet()}), and the cargo is that planet's registered ores,
     * weighed by their weights, each capped by the planet's remaining reserve. The reserve is initialized from the
     * planet definition on the first mine ({@code ore.amount × planetSize²}) and then decremented — so ores deplete
     * over the planet's lifetime (the reserve lives on the {@link VoidcraftUSS} model and is persisted).
     *
     * @param ship the miner ship (its target planet + mining power)
     * @return the cargo compound (never null; empty when the planet is absent or fully depleted)
     */
    private NBTTagCompound buildMinerCargo(VoidcraftActiveShip ship, int target) {
        List<USSPlanets.USSPlanet> planets = getPlanets();
        if (ship == null || planets.isEmpty() || target < 0 || target >= planets.size()) {
            return new NBTTagCompound(); // no planet to mine (defensive)
        }
        USSPlanets.USSPlanet planet = planets.get(target);
        VoidcraftUSS.PlanetReserve currentReserve = uss.getPlanetReserve(target);
        USSShipCargo.MinerResult result = USSShipCargo.minePlanet(planet, ship.getMiningPower(), currentReserve);
        // Persist the updated reserve (the planet depletes).
        uss = uss.withPlanetReserve(target, result.newReserve);
        return result.cargo;
    }

    // region USSPilotWorld (programming framework, Phase C — the game seam the pilots run against)

    /**
     * A ship's WORK leg just completed (the pilot calls this EXACTLY ONCE per work leg — its side-effect fires
     * here). The yield depends on the body the ship worked (the MOVE target that preceded the WORK):
     * <ul>
     * <li>a RIPPLE point (an Explorer scan) — the point is REVEALED (the yield is the reveal itself, not cargo);</li>
     * <li>the STAR with a STARLIFTER role — star cargo (dwarf-matter dust + Stellar Plasma);</li>
     * <li>a PLANET — that planet's registered ores (the reserve depletes), clamped by the ship's hold;</li>
     * <li>anything else (a SHIP rendezvous, a STAR worked by a non-starlifter, a WORK with no MOVE) — no cargo.</li>
     * </ul>
     * A Constructor mission carries no cargo either (its loadout is applied to the infrastructure project at
     * completion).
     */
    @Override
    public void onWorkComplete(VoidcraftActiveShip ship, String targetKind, int targetIndex) {
        NBTTagCompound cargo = null;
        boolean rippleScan = USSProgramDefaults.TARGET_RIPPLE.equals(targetKind)
            || USSProgramDefaults.TARGET_RIPPLE_UNSCANNED.equals(targetKind);
        boolean star = USSProgramDefaults.TARGET_STAR.equals(targetKind);
        boolean planet = USSProgramDefaults.TARGET_PLANET.equals(targetKind)
            || USSProgramDefaults.TARGET_NEAREST_PLANET.equals(targetKind)
            || USSProgramDefaults.TARGET_RANDOM_PLANET.equals(targetKind);
        if (rippleScan) {
            // EXPLORER: the scan leg finished — mark the ripple point as REVEALED. The point is marked exactly once
            // (a re-scanned point is a no-op) and the fleet is resynced so the client starts rendering it.
            if (targetIndex >= 0 && uss != null && !uss.isRippleScanned(targetIndex)) {
                uss = uss.withRippleScanned(targetIndex);
                boolean isRipple = getRippleField() != null && getRippleField().isRipple(targetIndex);
                try {
                    LOGGER.info(
                        "[Voidcraft] Explorer {} revealed ripple point {} (ripple={})",
                        ship.getName(),
                        targetIndex,
                        isRipple);
                } catch (Throwable ignored) {}
                syncFleetRenderBlock();
            }
            return; // no cargo for a scan (the reveal is the yield)
        }
        if (isConstructorMission(ship)) {
            return; // no-op: the constructor "mines" (builds) during the leg; the loadout applies at completion
        }
        if (star) {
            if (VoidcraftRole.STARLIFTER.isActive(ship.getRoles())) {
                cargo = USSShipCargo.buildForStarlifter(uss.getStarType(), ship.getMiningPower(), uss.getIgnitedAt());
            }
        } else if (planet) {
            cargo = buildMinerCargo(ship, targetIndex);
        }
        if (cargo == null) {
            return;
        }
        // CARGO CAPACITY (the cargo-capacity pass): the ship's internal hold is filled with the yield, clamped by
        // the hold's capacity — "mining fills their internal cargo capacity, and they cannot mine if it is full"
        // (a full hold simply accepts 0 more). The deliverable cargo is then derived from the hold (the source of
        // truth).
        CargoHold hold = USSShipCargo.fillHold(ship.getHold(), cargo);
        ship.setHold(hold);
        ship.setCargo(USSShipCargo.cargoFromHold(hold));
    }

    @Override
    public String readVar(int slot) {
        if (uss == null) {
            return "";
        }
        return uss.getVariables()
            .get(slot);
    }

    @Override
    public void writeVar(int slot, String value) {
        if (uss == null) {
            return;
        }
        // The variable space is fleet-SHARED data (ships' in/out channel) — immutable update on the model.
        uss = uss.withVariables(
            uss.getVariables()
                .set(slot, value));
    }

    @Override
    public int unscannedRipples() {
        USSRippleField field = getRippleField();
        if (field == null || uss == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < field.size(); i++) {
            if (!uss.isRippleScanned(i)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public USSTargetResult resolveTarget(String target, int index, VoidcraftActiveShip ship) {
        if (target == null || ship == null) {
            return null;
        }
        if (USSProgramDefaults.TARGET_STAR.equals(target)) {
            return new USSTargetResult(USSFleetOrbit.starPosition(), USSTargetResult.INDEX_STAR);
        }
        if (USSProgramDefaults.TARGET_PLANET.equals(target)) {
            if (index < 0) {
                return null;
            }
            USSPosition dest = destinationFor(index, ship.getSeed());
            if (dest == null) {
                return null; // out of range / no planets
            }
            return new USSTargetResult(dest, index);
        }
        if (USSProgramDefaults.TARGET_NEAREST_PLANET.equals(target)) {
            List<USSPlanets.USSPlanet> planets = getPlanets();
            if (planets == null || planets.isEmpty()) {
                return null;
            }
            USSPosition shipPos = ship.getPosition();
            float time = worldTimeTicks();
            float starSize = starSizeFor(uss.getStarType(), uss.getIgnitedAt());
            int nearest = -1;
            double best = Double.MAX_VALUE;
            for (int i = 0; i < planets.size(); i++) {
                USSPosition center = USSFleetOrbit.planetPosition(planets.get(i), starSize, time);
                double d = (shipPos == null) ? 0.0 : shipPos.distanceTo(center);
                if (d < best) {
                    best = d;
                    nearest = i;
                }
            }
            USSPosition dest = destinationFor(nearest, ship.getSeed());
            return (dest == null) ? null : new USSTargetResult(dest, nearest);
        }
        if (USSProgramDefaults.TARGET_RANDOM_PLANET.equals(target)) {
            // A random planet of THIS USS (pass-33 UI helper): one-shot pick at resolution (the same pattern as
            // RIPPLE_UNSCANNED); the RESOLVED index is what the WORK leg then mines.
            List<USSPlanets.USSPlanet> planets = getPlanets();
            if (planets == null || planets.isEmpty()) {
                return null;
            }
            int idx = new Random().nextInt(planets.size());
            USSPosition dest = destinationFor(idx, ship.getSeed());
            return (dest == null) ? null : new USSTargetResult(dest, idx);
        }
        if (USSProgramDefaults.TARGET_RIPPLE.equals(target)) {
            USSRippleField field = getRippleField();
            if (field == null || index < 0 || index >= field.size()) {
                return null;
            }
            // Fixed point (the points do not orbit) — the client hovers here exactly.
            return new USSTargetResult(field.positionOf(index), index, true);
        }
        if (USSProgramDefaults.TARGET_RIPPLE_UNSCANNED.equals(target)) {
            int point = pickUnscannedRipplePoint();
            if (point >= 0) {
                USSRippleField field = getRippleField();
                if (field != null) {
                    return new USSTargetResult(field.positionOf(point), point, true);
                }
            }
            // Nothing left to scan — the ship works the star instead (the old pickMissionTarget fallback).
            return new USSTargetResult(USSFleetOrbit.starPosition(), USSTargetResult.INDEX_STAR);
        }
        if (USSProgramDefaults.TARGET_SHIP.equals(target)) {
            if (index < 0 || index >= activeShips.size()) {
                return null;
            }
            USSPosition base = activeShips.get(index)
                .getPosition();
            if (base == null) {
                return null;
            }
            // A small deterministic offset (per the waiting ship's seed) so two ships never occupy the same point.
            return new USSTargetResult(
                USSFleetOrbit.nudge(base, 2.0, ship.getSeed()),
                USSTargetResult.INDEX_SHIP,
                true);
        }
        // TARGET_HOME is resolved by the pilot itself (its launch origin) — it never reaches this seam.
        return null;
    }

    @Override
    public long legTicks(boolean work, VoidcraftActiveShip ship, double distance) {
        if (ship == null) {
            return 0L;
        }
        // The same tables the client animates with (USSConstants) — server and client agree on every leg's length.
        USSShipState state = work ? USSShipState.MINING : USSShipState.OUTBOUND;
        long ticks = USSConstants
            .legTicks(state, distance, ship.getSpeed(), ship.getMiningPower(), ship.getRoles(), ship.getScanPower());
        return ticks > 0 ? ticks : 1L;
    }

    @Override
    public void log(VoidcraftActiveShip ship, String message) {
        try {
            LOGGER.info("[Voidcraft] {} — {}", ship != null ? ship.getName() : "ship", message);
        } catch (Throwable ignored) {}
    }

    // endregion

    /**
     * Mission complete for ONE ship (slot) — the ship SURVIVED its integrity time limit (it is here with integrity
     * still &gt; 0): a Constructor mission applies its loadout to the infrastructure project; any other mission
     * delivers the cargo to ITS OWN bay (captured at launch). Then: re-emit the surviving ship into ITS OWN
     * gateway slot (or drop it) — its integrity is back at maximum for the next flight (the item carries the
     * blueprint's full integrity). The fleet anchor is resynced by the CALLER (one push for the whole fleet —
     * Phase 4 pass 5). Other ships in flight are untouched.
     */
    private void completeShip(int slot) {
        if (slot < 0 || slot >= activeShips.size()) {
            return;
        }
        VoidcraftActiveShip completedShip = activeShips.get(slot);
        activeShips.remove(slot);
        if (slot < pilots.size()) {
            pilots.remove(slot);
        }
        lastPushedShipStates[slot] = -1;
        lastPushedLegIds[slot] = -1;

        String shipName = completedShip.getName();
        boolean constructorMission = isConstructorMission(completedShip);
        NBTTagCompound cargo = completedShip.getCargo();
        NBTTagList items = cargo != null ? USSShipCargo.readItems(cargo) : new NBTTagList();
        NBTTagList fluids = cargo != null ? USSShipCargo.readFluids(cargo) : new NBTTagList();
        long integrity = completedShip.getIntegrity(); // > 0 — the ship made it back inside its time limit
        ItemStack shipItem = rebuildShipItem(completedShip);

        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        float dropX = base.getXCoord() + 0.5f;
        float dropY = base.getYCoord() + 0.5f;
        float dropZ = base.getZCoord() + 0.5f;

        if (constructorMission) {
            // Phase 4 pass 2: the loadout was already pulled from the gateway's input buses/hatches at launch —
            // apply it to the infrastructure project (the materials are consumed by the build, not delivered).
            applyConstructorLoadout(completedShip);
        } else if (items.tagCount() > 0 || fluids.tagCount() > 0) {
            // Cargo → the bay captured at launch; if the bay is gone, drop the cargo at the USS (no silent loss).
            MTEVoidcraftStorageBay bay = mteAt(world, completedShip.getBayPos(), MTEVoidcraftStorageBay.class);
            if (bay != null && bay.mMachine) {
                bay.deliver(items);
                if (fluids.tagCount() > 0) {
                    bay.deliverFluids(fluids); // Phase 4 pass 1: Starlifter fluid cargo through the same three-tier
                                               // contract
                }
            } else {
                List<ItemStack> stacks = new ArrayList<>();
                for (int i = 0; i < items.tagCount(); i++) {
                    NBTTagCompound entry = items.getCompoundTagAt(i);
                    if (entry == null) continue;
                    ItemStack stack = ItemStack.loadItemStackFromNBT(entry);
                    if (stack != null) {
                        stacks.add(stack);
                    }
                }
                if (!stacks.isEmpty()) {
                    GTUtility.dropItemsOrClusters(world, dropX, dropY, dropZ, stacks);
                }
                if (fluids.tagCount() > 0) {
                    // A fluid cannot be dropped as an entity — the bay (a required launch target) is gone, so this
                    // is the one loud-loss escape hatch (the player is told, the cargo does not vanish silently).
                    try {
                        LOGGER.warn(
                            "[Voidcraft] Mission complete but the storage bay is gone: {} unit(s) of fluid cargo lost",
                            fluids.tagCount());
                    } catch (Throwable ignored) {}
                }
            }
        }

        // The ship SURVIVED (integrity > 0 at completion) → back into the gateway slot (the gateway re-holograms
        // it docked), with its integrity back at maximum for the next flight.
        if (shipItem != null) {
            MTEVoidcraftGateway gateway = mteAt(world, completedShip.getGatewayPos(), MTEVoidcraftGateway.class);
            if (gateway != null && gateway.mMachine && gateway.getControllerSlot() == null) {
                gateway.mInventory[gateway.getControllerSlotIndex()] = shipItem;
                gateway.updateSlots();
            } else {
                GTUtility
                    .dropItemsOrClusters(world, dropX, dropY, dropZ, java.util.Collections.singletonList(shipItem));
            }
        }

        updateSlots();
        try {
            LOGGER.info(
                "[Voidcraft] USS mission complete (slot {}): ship '{}' survived (integrity {}s left), cargo {} items",
                slot,
                shipName,
                integrity,
                items.tagCount());
        } catch (Throwable ignored) {}
    }

    /**
     * Phase 4 pass 2 — apply a completed Constructor mission's loadout (carried in the ship payload, written by the
     * gateway at launch) to its infrastructure project. Overflow beyond the project's costs is NOT credited (the
     * project finishes exactly at its cost table). The progress is permanent and persisted (chunk reloads, star
     * burnouts).
     */
    private void applyConstructorLoadout(VoidcraftActiveShip ship) {
        NBTTagCompound payload = ship.getPayload();
        if (payload == null) {
            return;
        }
        int projectId = payload.getInteger(VoidcraftNbt.TAG_PROJECT);
        USSProject project = USSProject.byId(projectId);
        if (project == null) {
            LOGGER.warn(
                "[Voidcraft] constructor mission finished with an unknown project id {} — loadout dropped",
                projectId);
            return;
        }
        NBTTagCompound loadout = payload.hasKey(VoidcraftNbt.TAG_LOADOUT)
            ? payload.getCompoundTag(VoidcraftNbt.TAG_LOADOUT)
            : null;
        if (loadout == null) {
            return; // no loadout (defensive — the gateway always writes one for a Constructor mission)
        }

        // Each loadout entry carries its own material name (the gateway writes it), so no reverse item → material
        // lookup is needed; entries that do not belong to the project are simply ignored by the apply below.
        java.util.Map<String, Long> amounts = new java.util.LinkedHashMap<>();
        NBTTagList items = USSShipCargo.readItems(loadout);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound entry = items.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            String materialName = entry.getString(USSShipCargo.ITEM_ENTRY_MATERIAL);
            if (materialName.isEmpty()) {
                continue; // not a constructor loadout entry (defensive)
            }
            long amount = entry.getInteger(USSShipCargo.ENTRY_AMOUNT);
            if (amount > 0L) {
                amounts.merge(materialName, (long) amount, Long::sum);
            }
        }
        NBTTagList fluids = USSShipCargo.readFluids(loadout);
        for (int i = 0; i < fluids.tagCount(); i++) {
            NBTTagCompound entry = fluids.getCompoundTagAt(i);
            if (entry == null) {
                continue;
            }
            String materialName = entry.getString(USSShipCargo.FLUID_ENTRY_MATERIAL);
            if (materialName.isEmpty()) {
                continue;
            }
            long amount = entry.getLong(USSShipCargo.FLUID_ENTRY_AMOUNT);
            if (amount > 0L) {
                amounts.merge(materialName, amount, Long::sum);
            }
        }
        if (amounts.isEmpty()) {
            return;
        }

        long applied = getInfrastructure().apply(projectId, amounts);
        try {
            getBaseMetaTileEntity().markDirty();
        } catch (Throwable ignored) {}
        try {
            boolean complete = getInfrastructure().isComplete(projectId);
            LOGGER.info(
                "[Voidcraft] constructor mission applied {} units to infrastructure project {} ({}{})",
                applied,
                projectId,
                amounts,
                complete ? ", project COMPLETE" : "");
        } catch (Throwable ignored) {}
    }

    /**
     * Give up ALL missions in flight without delivering (star burnout / structure teardown: every ship is lost,
     * the fleet anchor removed).
     */
    private void discardAllShips() {
        activeShips.clear();
        Arrays.fill(lastPushedShipStates, -1);
        fleetDirty = false;
        syncFleetRenderBlock(); // empty fleet → the anchor block is cleared
    }

    /**
     * Rebuild the ship item from the payload captured at launch (the payload IS the item NBT written by the
     * assembler).
     */
    private ItemStack rebuildShipItem(VoidcraftActiveShip ship) {
        NBTTagCompound payload = ship.getPayload();
        if (payload == null) {
            return null;
        }
        ItemStack item = new ItemStack(ItemVoidcraft.INSTANCE);
        item.setTagCompound((NBTTagCompound) payload.copy());
        return item;
    }

    /**
     * The MTE of the given type at a world position (null-safe).
     */
    private <T> T mteAt(World world, int[] pos, Class<T> type) {
        if (world == null || pos == null) {
            return null;
        }
        IMetaTileEntity mte = GTUtility.getMetaTileEntity(world.getTileEntity(pos[0], pos[1], pos[2]));
        if (mte == null) {
            return null;
        }
        return type.isInstance(mte) ? type.cast(mte) : null;
    }

    /**
     * Pick a still-hidden (unscanned) ripple point for an Explorer (the Explorer pass): a random point index (0..342)
     * that is NOT yet in the system's scanned set. {@code -1} when every point is already revealed (nothing left to
     * scan) or the field is absent.
     *
     * @return an unscanned ripple point index, or {@code -1} when none remain
     */
    private int pickUnscannedRipplePoint() {
        USSRippleField field = getRippleField();
        if (field == null || uss == null) {
            return -1;
        }
        int size = field.size();
        // Collect the unscanned indices (all-but-the-revealed) and pick one at random.
        java.util.List<Integer> unscanned = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (!uss.isRippleScanned(i)) {
                unscanned.add(i);
            }
        }
        if (unscanned.isEmpty()) {
            return -1;
        }
        return unscanned.get(new Random().nextInt(unscanned.size()));
    }

    /**
     * A planet's work point in the solar system (programming framework, Phase C — used by
     * {@link #resolveTarget}): a random SPHERICAL-SHELL point around the planet's LIVE position ("a random target
     * position around the orbit of the planet, within a spherical shell — not only above it, but on any side").
     *
     * <p>
     * Deterministic in {@code seed} (the ship's per-launch seed) — the server's destination and the client's
     * render agree on the same point.
     *
     * @param planet the planet index into {@link #getPlanets()}
     * @param seed   the ship's per-launch seed
     * @return the destination, or null when the planet is absent
     */
    private USSPosition destinationFor(int planet, int seed) {
        List<USSPlanets.USSPlanet> planets = getPlanets();
        if (planets == null || planet < 0 || planet >= planets.size()) {
            return null; // out of range (defensive) — the caller reports an unresolvable target
        }
        USSPlanets.USSPlanet world = planets.get(planet);
        float time = worldTimeTicks();
        float starSize = starSizeFor(uss.getStarType(), uss.getIgnitedAt());
        USSPosition planetCenter = USSFleetOrbit.planetPosition(world, starSize, time);
        // The hover distance: the planet's rendered radius (scale · 0.375, the legacy EoH body half) + 0.5, so the
        // ship hovers just off the planet's surface on ANY side (the shell radius).
        double hoverRadius = 0.5 + 0.375 * world.scale;
        return USSFleetOrbit.shellPoint(planetCenter, hoverRadius, seed);
    }

    /**
     * The shared render clock in TICKS (the world time), for the orbit math — the SAME time base the client render
     * and {@code USSFleetOrbit.planetAnchorPosition} expect (the client renders at {@code getTotalWorldTime() +
     * partialTicks}, both in ticks). Pass 37 (server-authoritative planet positions): this used to divide by 20 to
     * produce "seconds", but {@code planetAnchorPosition} interprets its time argument as TICKS — so the server's
     * planet positions moved 20x slower than the rendered planets (the "USS treats planets as static / distances
     * don't match visually" symptom). Returning raw ticks makes the server's planet position and the client's
     * rendered one agree exactly (same law, same constant, same time base), so a ship's server-resolved
     * destination and the visual path length are consistent.
     */
    private float worldTimeTicks() {
        try {
            IGregTechTileEntity base = getBaseMetaTileEntity();
            if (base != null && base.getWorld() != null) {
                return (float) base.getWorld()
                    .getWorldTime();
            }
        } catch (Throwable ignored) {}
        return 0.0f;
    }

    /**
     * The fleet-hologram anchor (Phase 4 pass 5 — ONE per USS, not per ship): the EoH render position, 32 behind
     * the controller (pass 12: the legacy 16 offset scaled 2× with the 65×65×65 shape), plus two blocks up so it
     * does not collide with the star render block. The WHOLE fleet (dozens–hundreds of ships) lives in this one
     * block's TE as an entry list; the ships themselves hover at their per-mission target CLIENT-side (see
     * {@link USSFleetOrbit}), so no per-ship world blocks are needed anymore.
     */
    private int[] shipAnchorPos(IGregTechTileEntity base) {
        ForgeDirection back = getExtendedFacing().getRelativeBackInWorld();
        return new int[] { (int) (base.getXCoord() + 32 * back.offsetX),
            (int) (base.getYCoord() + 32 * back.offsetY) + 2, (int) (base.getZCoord() + 32 * back.offsetZ) };
    }

    private static int[] rel(int[] from, int[] to) {
        if (to == null) {
            return new int[] { 0, 0, 0 };
        }
        return new int[] { to[0] - from[0], to[1] - from[1], to[2] - from[2] };
    }

    /**
     * Build the fleet entry list for the render TE: one entry per ship in flight — payload (blueprint + stats +
     * UUID) + state + seed (the per-launch identity key, pass 5.1) + mission target (pass 7) + the
     * anchor-relative gateway (each mission routes back to its own launcher/bay, Phase 4 pass 4).
     *
     * <p>
     * Pass 7: the DESTINATION is no longer in the entry — it is derived CLIENT-side from the target: a planet
     * index → that planet's live rendered position (the system specs + star size ride along in the TE, see
     * {@link #syncFleetRenderBlock}); {@code -1} → the star center (Starlifters hover 2.5 above it). The old
     * static role hover point is gone — it was the "destination in the outer reaches of the system" the user
     * flagged. The per-ship swarm spread around the hover point is computed CLIENT-side from the seed
     * ({@link USSFleetOrbit}).
     */
    private List<NBTTagCompound> buildFleetEntries(IGregTechTileEntity base) {
        List<NBTTagCompound> entries = new ArrayList<NBTTagCompound>(activeShips.size());
        if (base == null) {
            return entries;
        }
        int[] anchor = shipAnchorPos(base);
        for (VoidcraftActiveShip ship : activeShips) {
            NBTTagCompound payload = ship.getPayload();
            if (payload == null) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_PAYLOAD, payload);
            entry.setInteger(
                TileEntityVoidcraftShip.TAG_ENTRY_STATE,
                ship.getState()
                    .getId());
            // Pass 5.1: per-launch identity — the client keys this ship's animation phase + swarm spread on it
            // (duplicated ship items share the item UUID; the seed does not).
            entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_SEED, ship.getSeed());
            // Pass 7: the mission target — a planet index, a ripple-point index (the Explorer pass), or -1 (the
            // star). The client resolves it against the system specs this TE carries.
            entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET, ship.getTargetPlanet());
            // The Explorer pass: the ship's RESOLVED destination (the planet shell point, the star center, or the
            // ripple point's position). The client uses it directly — an Explorer's target is a ripple-point index,
            // not a planet index, so the client's targetBody() cannot resolve it (the server already did).
            if (ship.getDestination() != null) {
                NBTTagCompound destTag = new NBTTagCompound();
                ship.getDestination()
                    .writeToNBT(destTag);
                entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST, destTag);
            }
            // Pass 26 (the travel-time rendering fix): the ACTUAL travel distance, so the client animates each
            // OUTBOUND/RETURNING leg for the ship's real duration instead of the minimum floor (the client used to
            // read vc_tdist off the payload, where it was never written → distance 0 → minimum time).
            entry.setDouble(TileEntityVoidcraftShip.TAG_ENTRY_TDIST, ship.getTravelDistance());
            // Phase C (the programming framework): the ship's CURRENT position (fleet-anchor coordinates — its
            // launch origin, then the last leg's endpoint). The client renders the ship EXACTLY here while it
            // HOLDS (a fresh ship at the gateway, or a finished program at its last body).
            if (ship.getPosition() != null) {
                NBTTagCompound posTag = new NBTTagCompound();
                ship.getPosition()
                    .writeToNBT(posTag);
                entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_POS, posTag);
            }
            // Phase C: static hover — true when the ship's hover body is a FIXED point (ripple / ship rendezvous)
            // and the client must hover the resolved DESTINATION exactly (not the body's live position).
            entry.setBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC, ship.isBodyStatic());
            // Phase C: the leg identity — the client resets its leg-progress phase when this changes, so legs of
            // the SAME state (MOVE → MOVE) animate from their own start.
            entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_LEG_ID, ship.getLegId());
            int[] gatewayWorld = ship.getGatewayPos() != null ? ship.getGatewayPos()
                : new int[] { anchor[0], anchor[1], anchor[2] };
            entry.setIntArray(TileEntityVoidcraftShip.TAG_ENTRY_GW_REL, rel(anchor, gatewayWorld));
            entries.add(entry);
        }
        return entries;
    }

    /**
     * Push the WHOLE fleet to its one render anchor (Phase 4 pass 5 — replaces pass 4's per-slot blocks): creates
     * or adopts the anchor block, rebuilds its entry list, and syncs it ONCE; with an empty fleet it clears the
     * anchor. Called at most once per MTE tick (launch / state change / completion / discard / one-time cleanup) —
     * one full-fleet description packet instead of one per ship.
     */
    private void syncFleetRenderBlock() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        if (world == null || world.isRemote) {
            return;
        }
        int[] anchor = shipAnchorPos(base);
        if (activeShips.isEmpty()) {
            if (world.getBlock(anchor[0], anchor[1], anchor[2]) == VoidcraftLoader.sBlockVoidcraftShipRender) {
                world.setBlockToAir(anchor[0], anchor[1], anchor[2]);
                try {
                    LOGGER.info("[Voidcraft] USS fleet anchor removed @ {},{},{}", anchor[0], anchor[1], anchor[2]);
                } catch (Throwable ignored) {}
            }
            return;
        }
        Block atAnchor = world.getBlock(anchor[0], anchor[1], anchor[2]);
        if (atAnchor != VoidcraftLoader.sBlockVoidcraftShipRender) {
            if (atAnchor != Blocks.air) {
                return; // occupied by something else — the fleet is invisible (rare; missions still run)
            }
            world.setBlock(anchor[0], anchor[1], anchor[2], VoidcraftLoader.sBlockVoidcraftShipRender);
        }
        // else: a (possibly stale) fleet anchor from an earlier state already sits here — adopt it below.
        TileEntity te = world.getTileEntity(anchor[0], anchor[1], anchor[2]);
        if (!(te instanceof TileEntityVoidcraftShip)) {
            return;
        }
        TileEntityVoidcraftShip fleetTe = (TileEntityVoidcraftShip) te;
        fleetTe.setShips(buildFleetEntries(base));
        // Pass 7: the system's planet specs + star size ride with the fleet so the client can resolve each ship's
        // mission target to the planet's live rendered position (no world lookups client-side).
        fleetTe.setSystem(planetSpecsFor(getPlanets()), starSizeFor(uss.getStarType(), uss.getIgnitedAt()));
        // The Explorer pass: the REVEALED ripple positions (the ripple field ∩ the scanned set) — the client renders
        // each as a pulsating dark-blue transparent triangle. Only ripples that have been scanned ride here (hidden
        // ripples + revealed non-ripples stay absent).
        fleetTe.setRevealedRipples(revealedRipplePositions());
        // 1.7.10: updateEntity() is a tick hook — the real client push is markBlockForUpdate (see syncToClient).
        fleetTe.syncToClient();
    }

    /**
     * Once per MTE lifetime (in-memory; the check is idempotent):
     * <ul>
     * <li>clear legacy per-slot anchors from pass 4 (slots 1–2 of the old 2-blocks-per-slot lateral geometry —
     * still present in older test worlds; pass 5 uses ONE fleet anchor at the old slot-0 position), and any
     * stray anchor left with the fleet empty (older builds could leave one behind with stale state, rendering
     * a frozen ship forever and blocking future launches);</li>
     * <li>pass 12: the fleet anchor moved 16 → 32 behind the controller (the structure doubled) — clear the
     * old 16-offset anchor and its lateral slots from pre-pass-12 test worlds;</li>
     * <li>make sure the fleet anchor EXISTS and holds the current fleet when ships are in flight (covers a
     * load-time edge case where the anchor block was lost while the MTE's NBT survived).</li>
     * </ul>
     */
    private void cleanupLegacyShipRender() {
        if (legacyShipRenderCleaned) {
            return;
        }
        legacyShipRenderCleaned = true;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        int[] anchor = shipAnchorPos(base);
        // Pass-4 lateral slot anchors: side = up × back = (back.offsetZ, 0, -back.offsetX); 2 blocks per slot.
        int sideX = getExtendedFacing().getRelativeBackInWorld().offsetZ;
        int sideZ = -getExtendedFacing().getRelativeBackInWorld().offsetX;
        if (sideX == 0 && sideZ == 0) {
            sideX = 1; // back is parallel to up (facing straight down) — any fixed horizontal axis works
        }
        // Pass 12: the fleet anchor now sits 32 behind the controller; pre-pass-12 test worlds have the old
        // 16-offset anchor (and its lateral slots) orphaned — clear them (one-time, idempotent).
        ForgeDirection back = getExtendedFacing().getRelativeBackInWorld();
        int oldAnchorX = (int) (base.getXCoord() + 16 * back.offsetX);
        int oldAnchorY = (int) (base.getYCoord() + 16 * back.offsetY) + 2;
        int oldAnchorZ = (int) (base.getZCoord() + 16 * back.offsetZ);
        for (int slot = 0; slot <= 2; slot++) {
            int px = oldAnchorX + sideX * 2 * slot;
            int pz = oldAnchorZ + sideZ * 2 * slot;
            if (world.getBlock(px, oldAnchorY, pz) == VoidcraftLoader.sBlockVoidcraftShipRender) {
                world.setBlockToAir(px, oldAnchorY, pz);
                try {
                    LOGGER.info(
                        "[Voidcraft] USS removed pre-pass-12 ship-render anchor (slot {}) @ {},{},{}",
                        slot,
                        px,
                        oldAnchorY,
                        pz);
                } catch (Throwable ignored) {}
            }
        }
        for (int slot = 1; slot <= 2; slot++) {
            int px = anchor[0] + sideX * 2 * slot;
            int pz = anchor[2] + sideZ * 2 * slot;
            if (world.getBlock(px, anchor[1], pz) == VoidcraftLoader.sBlockVoidcraftShipRender) {
                world.setBlockToAir(px, anchor[1], pz);
                try {
                    LOGGER.info(
                        "[Voidcraft] USS removed legacy ship-render anchor (slot {}) @ {},{},{}",
                        slot,
                        px,
                        anchor[1],
                        pz);
                } catch (Throwable ignored) {}
            }
        }
        if (activeShips.isEmpty()) {
            if (world.getBlock(anchor[0], anchor[1], anchor[2]) == VoidcraftLoader.sBlockVoidcraftShipRender) {
                world.setBlockToAir(anchor[0], anchor[1], anchor[2]);
                try {
                    LOGGER
                        .info("[Voidcraft] USS removed stray fleet anchor @ {},{},{}", anchor[0], anchor[1], anchor[2]);
                } catch (Throwable ignored) {}
            }
        } else {
            syncFleetRenderBlock(); // ensure the anchor exists and holds the current fleet
        }
    }

    // endregion

    // Region info data & misc.

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add("tt.voidcraft_uss.infodata.header");
        VoidcraftUSS model = uss == null ? VoidcraftUSS.cold() : uss;
        str.add(
            IGregTechDeviceInformation.encode(
                "tt.voidcraft_uss.state.label",
                IGregTechDeviceInformation.translatable(
                    model.getState()
                        .getLangKey())));
        if (model.isIgnited()) {
            str.add(
                IGregTechDeviceInformation
                    .encode("tt.voidcraft_uss.tier.label", "" + YELLOW + (model.getTier() + 1) + RESET));
            str.add(
                IGregTechDeviceInformation.encode(
                    "tt.voidcraft_uss.star.label",
                    IGregTechDeviceInformation.translatable(
                        model.getStarType()
                            .getLangKey())));
            str.add(
                IGregTechDeviceInformation
                    .encode("tt.voidcraft_uss.lifespan.label", "" + YELLOW + model.getLifespanRemaining() + RESET));
            // Phase 4 pass 3: the system's PLANETS — what a Miner can work here (planet → its registered ores).
            str.add("tt.voidcraft_uss.planets.header");
            for (USSPlanets.USSPlanet planet : getPlanets()) {
                StringBuilder oreList = new StringBuilder();
                for (USSPlanetOre ore : planet.definition.getOres()) {
                    if (oreList.length() > 0) {
                        oreList.append(", ");
                    }
                    oreList.append(displayNameForMaterial(ore.getOreType()));
                }
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.planet.line",
                        IGregTechDeviceInformation.translatable("tt.voidcraft_uss.planet." + planet.definition.getId())
                            + " — "
                            + oreList));
            }
        }
        str.add(
            IGregTechDeviceInformation.encode(
                "tt.voidcraft_uss.controller.label",
                IGregTechDeviceInformation.translatable(
                    getControllerSlot() != null ? "tt.voidcraft_uss.controller.present"
                        : "tt.voidcraft_uss.controller.absent")));
        // Phase 4 pass 5: the fleet in flight (dozens–hundreds possible) — the COUNT plus a few sample lines,
        // not a 100-line list.
        if (!activeShips.isEmpty()) {
            str.add(
                IGregTechDeviceInformation.encode("tt.voidcraft_uss.ships.header", String.valueOf(activeShips.size())));
            int shown = 0;
            for (VoidcraftActiveShip ship : activeShips) {
                if (shown++ >= 3) {
                    str.add(
                        IGregTechDeviceInformation
                            .encode("tt.voidcraft_uss.ships.more", String.valueOf(activeShips.size() - 3)));
                    break;
                }
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.ship.line",
                        ship.getName() + " — "
                            + IGregTechDeviceInformation.translatable(
                                ship.getState()
                                    .getLangKey())
                            + " "
                            + YELLOW
                            + ship.getTicksRemaining()
                            + RESET
                            + "t"));
            }
        }
        // Phase 4 pass 2: infrastructure projects (Constructor build progress — the first incomplete project is
        // what the next Constructor mission works on).
        USSInfrastructure infrastructure = getInfrastructure();
        str.add("tt.voidcraft_uss.infodata.infrastructure.header");
        for (USSProject project : USSProject.CATALOG) {
            boolean complete = infrastructure.isComplete(project.id);
            StringBuilder detail = new StringBuilder();
            for (USSProject.Cost cost : project.costs) {
                long consumed = Math.min(cost.amount, infrastructure.consumed(project.id, cost.materialName));
                if (detail.length() > 0) {
                    detail.append(", ");
                }
                detail.append(displayNameForCost(cost));
                detail.append(" ")
                    .append(YELLOW)
                    .append(consumed)
                    .append(RESET)
                    .append('/')
                    .append(cost.amount);
            }
            str.add(
                IGregTechDeviceInformation.encode(
                    "tt.voidcraft_uss.project.line",
                    IGregTechDeviceInformation.translatable(project.langKey) + " — "
                        + (complete
                            ? IGregTechDeviceInformation.translatable("tt.voidcraft_uss.project.status.complete")
                            : IGregTechDeviceInformation.translatable("tt.voidcraft_uss.project.status.in_progress"))
                        + (detail.length() > 0 ? " (" + detail + ")" : "")));
        }
        return str.toArray(new String[0]);
    }

    /**
     * The in-game display name of a planet ore material (same convention as {@link #displayNameForCost}: the dust
     * stack's localized name; the raw material name is the fallback).
     */
    private static String displayNameForMaterial(Materials material) {
        if (material != null && material != Materials._NULL) {
            ItemStack dust = material.getDust(1);
            if (dust != null) {
                return dust.getDisplayName();
            }
        }
        return material == null ? "?" : material.getName();
    }

    /**
     * The in-game display name of a project cost material (same convention as the storage bay infodata: the dust
     * stack's / the fluid's localized name; the raw material name is the fallback).
     */
    private static String displayNameForCost(USSProject.Cost cost) {
        Materials material = Materials.get(cost.materialName);
        if (material != null && material != Materials._NULL) {
            if (cost.kind == USSProject.Kind.ITEM) {
                ItemStack dust = material.getDust(1);
                if (dust != null) {
                    return dust.getDisplayName();
                }
            } else {
                FluidStack fluid = material.getFluid(1);
                if (fluid != null) {
                    return fluid.getLocalizedName();
                }
            }
        }
        return cost.materialName;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.spacetime_manipulator"))
            .addMarkdown(new ResourceLocation("gregtech", "unstable-solar-system"))
            .beginStructureBlock(65, 65, 65, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_17th_layer"))
            .addCasing("896", new ItemStack(TTCasingsContainer.sBlockCasingsBA0, 1, 11).getDisplayName(), false)
            .addCasing("534", new ItemStack(TTCasingsContainer.sBlockCasingsBA0, 1, 10).getDisplayName(), false)
            .addCasing("168", TTCasingsContainer.TimeAccelerationFieldGenerator.getLocalizedName(), true)
            .addCasing("138", TTCasingsContainer.SpacetimeCompressionFieldGenerators.getLocalizedName(), true)
            .addCasing("48", TTCasingsContainer.StabilisationFieldGenerators.getLocalizedName(), true)
            .addCasing("31", Casings.InfiniteSpacetimeEnergyBoundaryCasing.getLocalizedName(), false)
            .addInputBus("1", StatCollector.translateToLocal("GT5U.tooltip.eye-of-harmony.boundary-no-stocking-bus"), 1)
            .addInputHatch("2", StatCollector.translateToLocal("GT5U.tooltip.eye-of-harmony.boundary-no-stocking-hatch"), 1)
            .addOutputBus("1", StatCollector.translateToLocal("GT5U.tooltip.eye-of-harmony.any-boundary-casing"), 1)
            .addOutputHatch("1", StatCollector.translateToLocal("GT5U.tooltip.eye-of-harmony.any-boundary-casing"), 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.EOH_STABILISATION)
            .addSubChannel(GTStructureChannels.EOH_DILATION)
            .addSubChannel(GTStructureChannels.EOH_COMPRESSION)
            .toolTipFinisher(EnumChatFormatting.GOLD, 87, GTAuthors.AuthorColen);
        // spotless:on
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = Textures.BlockIcons.custom("iconsets/EM_BHG");
        ScreenON = Textures.BlockIcons.custom("iconsets/EM_BHG_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][12],
                new TTRenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][12] };
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        // Same loop sound as the legacy EoH (the file is shared).
        return SoundResource.GT_MACHINES_EYE_OF_HARMONY_LOOP;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 32, 32, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5); // 200 blocks max
                                                                                                  // per placement.
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 32, 32, 0, realBudget, source, actor, false, true);
    }

    // Region NBT.

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (uss != null) {
            NBTTagCompound ussTag = new NBTTagCompound();
            uss.writeToNBT(ussTag);
            aNBT.setTag(USS_NBT_TAG, ussTag);
        }
        aNBT.setBoolean(ANIMATIONS_ENABLED_NBT_TAG, animationsEnabled);
        if (!activeShips.isEmpty()) {
            NBTTagList ships = new NBTTagList();
            for (int i = 0; i < activeShips.size(); i++) {
                NBTTagCompound shipTag = activeShips.get(i)
                    .writeToNBT();
                // Phase C: the pilot's state (the executor's cursor + the in-flight leg's bookkeeping) nests
                // under the SHIP's tag — a ship saved mid-leg / mid-loop resumes exactly once after a reload.
                if (i < pilots.size()) {
                    shipTag.setTag(
                        USSShipPilot.TAG_PILOT,
                        pilots.get(i)
                            .writeToNBT());
                }
                ships.appendTag(shipTag);
            }
            aNBT.setTag(ACTIVE_SHIPS_NBT_TAG, ships);
        }
        if (infrastructure != null) {
            NBTTagCompound infraTag = new NBTTagCompound();
            infrastructure.writeToNBT(infraTag); // writes nothing when the progress is empty
            if (infraTag.hasKey(USSInfrastructure.TAG_PROJECTS)) {
                aNBT.setTag(INFRASTRUCTURE_NBT_TAG, infraTag); // keep a fresh USS lean
            }
        }
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey(USS_NBT_TAG)) {
            VoidcraftUSS loaded = VoidcraftUSS.readFromNBT(aNBT.getCompoundTag(USS_NBT_TAG));
            uss = loaded != null ? loaded : VoidcraftUSS.cold();
        }
        animationsEnabled = !aNBT.hasKey(ANIMATIONS_ENABLED_NBT_TAG) || aNBT.getBoolean(ANIMATIONS_ENABLED_NBT_TAG);
        // Phase 4 pass 4/5: the fleet (slot order). Corrupt entries are skipped; the list is capped at capacity.
        // The fleet render anchor is a pure function of (block position, facing), so nothing else to restore —
        // the fleet TE persists itself in the world (and the one-time cleanup re-creates it if it was lost).
        activeShips.clear();
        pilots.clear();
        Arrays.fill(lastPushedShipStates, -1);
        Arrays.fill(lastPushedLegIds, -1);
        if (aNBT.hasKey(ACTIVE_SHIPS_NBT_TAG)) {
            NBTTagList ships = aNBT.getTagList(ACTIVE_SHIPS_NBT_TAG, 10);
            for (int i = 0; i < ships.tagCount() && activeShips.size() < USSConstants.MAX_SHIPS_PER_USS; i++) {
                NBTTagCompound shipTag = ships.getCompoundTagAt(i);
                VoidcraftActiveShip ship = VoidcraftActiveShip.readFromNBT(shipTag);
                if (ship != null) {
                    activeShips.add(ship);
                    // Phase C: re-attach the pilot (its program comes from the ship's payload; its cursor + leg
                    // bookkeeping from the nested vc_pilot tag — a missing one degrades to a fresh pilot, a corrupt
                    // one fails safe to a COMPLETED program → the ship holds).
                    pilots.add(USSShipPilot.attach(ship, this, shipTag));
                    // The slot's render TE already holds the state (its own NBT) — mark it pushed, no re-push.
                    lastPushedShipStates[activeShips.size() - 1] = ship.getState()
                        .getId();
                    lastPushedLegIds[activeShips.size() - 1] = ship.getLegId();
                }
            }
        }
        // Phase 4 pass 2: infrastructure progress (absent tag = fresh/empty; corrupt = dropped, no migration path).
        if (aNBT.hasKey(INFRASTRUCTURE_NBT_TAG)) {
            infrastructure = USSInfrastructure.readFromNBT(aNBT.getCompoundTag(INFRASTRUCTURE_NBT_TAG));
        } else {
            infrastructure = new USSInfrastructure();
        }
        super.loadNBTData(aNBT);
    }

    // endregion
}
