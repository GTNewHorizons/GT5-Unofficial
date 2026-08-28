package tectech.voidcraft.machine;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.enums.HarvestTool;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.gui.VoidcraftProgramGui;
import tectech.voidcraft.gui.VoidcraftProgramSource;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftCoverRegistry;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.uss.USSCapabilities;
import tectech.voidcraft.uss.USSCommand;
import tectech.voidcraft.uss.USSProgram;
import tectech.voidcraft.uss.USSProgramSync;
import tectech.voidcraft.uss.USSProgramView;

/**
 * A Voidcraft full-block component, as a machine-block meta tile entity.
 *
 * <p>
 * PASS 23 (user spec): covers are the PRIMARY components — all ship functionality comes from the covers. The ONLY
 * placeable full blocks are the <b>Voidcraft Controller</b> (the brain, exactly one per ship) and the
 * <b>Voidcraft Frame</b> (the renamed Utility Block: a mostly-transparent framebox whose purpose is to accept the
 * Voidcraft component covers on its faces). Both live on the standard machine block, so they can be
 * <b>wrenched to a facing</b> and <b>accept Voidcraft covers</b> on any of their six faces.
 *
 * <p>
 * Thrust is back-facing (pass 18/23, pass 24 flip): the ship's nose is the FAR end (grid +Z, away from the
 * assembler), so a {@code THRUSTER_NOZZLE} cover counts toward the ship's single thrust value only when mounted on
 * a cell's BACK face (−Z, the assembler side) (see {@link tectech.voidcraft.ship.VoidcraftBlueprint#computeStats()}).
 *
 * <p>
 * Non-electric, no inventory, no client tick — a pure hull part. Only Voidcraft cover items can be mounted on it.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEVoidcraftComponent extends MetaTileEntity implements VoidcraftProgramSource {

    private final VoidcraftComponent component;
    private final ITexture texture;

    /**
     * The CONTROLLER's stored program (programming framework, Phase C): the instruction-list NBT
     * ({@link USSProgram#writeToNBT()} format — a node list). Frames never carry one. Persisted in the block NBT
     * (the assembler copies it into the digitized ship item at build time; see
     * {@code MTEVoidcraftAssembler#outputAfterRecipe_EM}).
     */
    private NBTTagList program;

    public MTEVoidcraftComponent(int id, String name, String nameRegional, VoidcraftComponent component) {
        super(id, name, nameRegional, 0);
        this.component = component;
        this.texture = VoidcraftTextures.componentTexture(component);
    }

    public MTEVoidcraftComponent(String name, VoidcraftComponent component) {
        super(name, 0);
        this.component = component;
        this.texture = VoidcraftTextures.componentTexture(component);
    }

    /** @return the component this block represents */
    public VoidcraftComponent getComponent() {
        return component;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEVoidcraftComponent(mName, component);
    }

    // ------------------------------------------------------------------
    // Textures
    // ------------------------------------------------------------------

    /**
     * Uniform for now — the component icon on every face. A future art pass can return a different texture per side
     * (e.g. a nozzle on the front face) without changing any other part of the system.
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] { texture };
    }

    // ------------------------------------------------------------------
    // Behaviour
    // ------------------------------------------------------------------

    /** All six facings are valid (wrench-rotatable hull). */
    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    /** Only Voidcraft cover items may be mounted on a hull block. */
    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return VoidcraftCoverRegistry.isCover(coverItem);
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public long maxEUStore() {
        return 0;
    }

    @Override
    public long maxEUInput() {
        return 0;
    }

    @Override
    public long maxEUOutput() {
        return 0;
    }

    /** Hull part — mined with a wrench, like other casing-style blocks. */
    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.WrenchLevel2.toTileEntityBaseType();
    }

    /** Static hull part — the controller's program is the only payload (Phase C). */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        program = null;
        if (component == VoidcraftComponent.CONTROLLER && aNBT.hasKey(VoidcraftNbt.TAG_PROGRAM)) {
            NBTBase tag = aNBT.getTag(VoidcraftNbt.TAG_PROGRAM);
            if (tag instanceof NBTTagList) {
                // Validate on load: a corrupt list is dropped (the ship holds at the origin rather than running it).
                program = (USSProgram.readFromNBT((NBTTagList) tag) == null) ? null : (NBTTagList) tag;
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (component == VoidcraftComponent.CONTROLLER && program != null) {
            NBTBase copy = program.copy();
            if (copy instanceof NBTTagList) {
                aNBT.setTag(VoidcraftNbt.TAG_PROGRAM, (NBTTagList) copy);
            }
        }
    }

    // region program (programming framework, Phase C — controller only)

    /**
     * @return the stored program node list, or null when this block has no program (frames / a fresh controller)
     */
    public NBTTagList getProgramTag() {
        return program;
    }

    /**
     * Replace the stored program (controller only).
     *
     * @param list the node list (validated: a corrupt / over-cap list is rejected and the previous program kept);
     *             null clears it
     * @return true when the program was stored (or cleared)
     */
    public boolean setProgramTag(NBTTagList list) {
        if (component != VoidcraftComponent.CONTROLLER) {
            return false;
        }
        if (list == null) {
            program = null;
            return true;
        }
        if (USSProgram.readFromNBT(list) == null) {
            return false;
        }
        NBTBase copy = list.copy();
        program = (copy instanceof NBTTagList) ? (NBTTagList) copy : null;
        return program != null;
    }

    /**
     * Last server-side note for the GUI footer (a rejection reason from an applied edit, or null) — synced S2C
     * (pass 33 UI, UI-2).
     */
    private String note;

    // endregion

    // region program GUI (pass 33 UI, UI-2 — controller only)

    /**
     * @return the stored program (never null — a fresh / program-less controller gives an empty program)
     */
    public USSProgram getProgram() {
        USSProgram p = program == null ? null : USSProgram.readFromNBT(program);
        return p == null ? USSProgram.empty() : p;
    }

    /** The stored program as flat ROW wire strings (the GUI list-sync content, see {@link USSProgramView}). */
    public List<String> getProgramRows() {
        return USSProgramView.rowsJsonList(getProgram());
    }

    public String getNote() {
        return note == null ? "" : note;
    }

    public void setNote(String note) {
        this.note = note == null ? "" : note;
    }

    /**
     * Server-side: apply one GUI ACTION (JSON, see {@link USSProgramSync}) to the stored program.
     *
     * <p>
     * Accepted → the new program is stored and the note cleared (callers push the sync updates). Rejected → the
     * stored program is kept and the rejection reason becomes the note. NEVER throws (bad action → rejection).
     */
    @Override
    public void applyAction(String actionJson) {
        if (component != VoidcraftComponent.CONTROLLER) {
            setNote("not a controller");
            return;
        }
        USSProgramSync.Outcome outcome = USSProgramSync.handle(getProgram(), actionJson, getCommandCaps());
        if (!outcome.ok) {
            setNote(outcome.message);
            return;
        }
        setProgramTag(outcome.program.writeToNBT());
        setNote(null);
        if (!getBaseMetaTileEntity().isClientSide()) {
            markDirty();
        }
    }

    /**
     * The controller's COMMAND CAPABILITY SET (the capability system): derived from the covers mounted on the
     * controller BLOCK ITSELF (the editor cannot see the rest of the hull). A capability cover (thruster / mining
     * array / scanner dish / star siphon / fabricator / repair bay) adds its bit; a controller with NO capability
     * covers says nothing about the hull, so it reports {@link USSCapabilities#universal()} (everything is
     * offered; the runtime gates the truth — a leg the ship cannot do is refused at leg start).
     */
    @Override
    public USSCapabilities getCommandCaps() {
        if (component != VoidcraftComponent.CONTROLLER) {
            return USSCapabilities.empty();
        }
        long[] stats = mountedCoverStats();
        int bits = 0;
        if (stats[0] > 0L) {
            bits |= USSCapabilities.MOVE;
        }
        if (stats[1] > 0L) {
            bits |= USSCapabilities.MINE;
        }
        if (stats[2] > 0L) {
            bits |= USSCapabilities.SCAN;
        }
        if (stats[3] > 0L) {
            bits |= USSCapabilities.SIPHON;
        }
        if (stats[4] > 0L) {
            bits |= USSCapabilities.CONSTRUCT;
        }
        if (stats[5] > 0L) {
            bits |= USSCapabilities.REPAIR;
        }
        return bits == 0 ? USSCapabilities.universal() : USSCapabilities.of(bits);
    }

    /**
     * The per-command STAT LINE for a command row's tooltip: the power the covers mounted on this controller
     * block contribute (thrust / mining / scan / siphon / construction / repair bays).
     */
    @Override
    public String getCommandStatLine(int commandId) {
        if (component != VoidcraftComponent.CONTROLLER) {
            return "";
        }
        long[] stats = mountedCoverStats();
        switch (commandId) {
            case USSCommand.MOVE:
                return stats[0] > 0L ? "Thrust: " + stats[0] : "";
            case USSCommand.MINE:
                return stats[1] > 0L ? "Mining power: " + stats[1] : "";
            case USSCommand.SCAN:
                return stats[2] > 0L ? "Scan power: " + stats[2] : "";
            case USSCommand.SIPHON:
                return stats[3] > 0L ? "Siphon power: " + stats[3] : "";
            case USSCommand.CONSTRUCT:
                return stats[4] > 0L ? "Construction power: " + stats[4] : "";
            case USSCommand.REPAIR:
                return stats[5] > 0L ? "Repair bays: " + stats[5] : "";
            default:
                return "";
        }
    }

    /**
     * The stats of the covers mounted on this controller's OWN block (all six faces), per
     * {@link VoidcraftCoverComponent}: {@code [0] thrust [1] mining [2] scan [3] siphon [4] construction
     * [5] repair bays}. Zeroes when the base TE is not available (a client read before sync).
     */
    private long[] mountedCoverStats() {
        long[] out = new long[6];
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return out;
        }
        for (int i = 0; i < 6; i++) {
            VoidcraftCoverComponent cover = VoidcraftCoverRegistry
                .byStack(base.getCoverItemAtSide(ForgeDirection.getOrientation(i)));
            if (cover == null) {
                continue;
            }
            out[0] += cover.getThrust();
            out[1] += cover.getMiningPower();
            out[2] += cover.getScanPower();
            out[3] += cover.getStarlifterPower();
            out[4] += cover.getConstructionPower();
            if (cover == VoidcraftCoverComponent.REPAIR_BAY) {
                out[5]++;
            }
        }
        return out;
    }

    /**
     * Right-click (controller only): OPEN the programming GUI (pass 33 UI, UI-2 — the chip toggle from Phase C is
     * replaced by the in-GUI preset buttons Miner / Starlifter / Explorer / Clear).
     */
    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (component != VoidcraftComponent.CONTROLLER) {
            return false;
        }
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        openGui(aPlayer);
        return true;
    }

    // endregion

    // region MUI2 (pass 33 UI, UI-2 — the controller's programming GUI)

    @Override
    protected boolean useMui2() {
        return component == VoidcraftComponent.CONTROLLER;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new VoidcraftProgramGui(this).build(guiData, syncManager, uiSettings);
    }

    @Override
    public GTGuiTheme getGuiTheme() {
        return GTGuiThemes.TECTECH_STANDARD;
    }

    // endregion

    /** No inventory on a hull block. */
    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    /** Static hull part — nothing to tick. */
    @Override
    public boolean needsClientTick() {
        return false;
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    // ------------------------------------------------------------------
    // Tooltip
    // ------------------------------------------------------------------

    /**
     * Item tooltip lines (shown raw because of {@link IMetaTileEntityAnnotation}): the component's stats, mirroring
     * the old component item tooltip, plus the hull-specific hints.
     */
    @Override
    public String[] getDescription() {
        List<String> lines = new ArrayList<>();
        // Pass 23: the frame is the cover-accepting hull; the controller is the brain.
        lines.add(
            component == VoidcraftComponent.FRAME ? translateToLocal("tt.voidcraft.component.frame_hint")
                : translateToLocal("tt.voidcraft.component.controller_hint"));

        if (component.getMass() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.mass",
                    NumberFormatUtil.formatNumber(component.getMass())));
        }
        if (component.getThrust() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.thrust",
                    NumberFormatUtil.formatNumber(component.getThrust())));
        }
        if (component.getCargoSlots() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.cargo",
                    NumberFormatUtil.formatNumber(component.getCargoSlots())));
        }
        if (component.getMiningPower() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.mining",
                    NumberFormatUtil.formatNumber(component.getMiningPower())));
        }
        if (component.getScanPower() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.scan",
                    NumberFormatUtil.formatNumber(component.getScanPower())));
        }
        if (component.getConstructionPower() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.construction",
                    NumberFormatUtil.formatNumber(component.getConstructionPower())));
        }
        if (component.getStarlifterPower() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.starlifter",
                    NumberFormatUtil.formatNumber(component.getStarlifterPower())));
        }
        if (component.getEnergyBuffer() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.buffer",
                    NumberFormatUtil.formatNumber(component.getEnergyBuffer())));
        }
        if (component.getEnergyDraw() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.draw",
                    NumberFormatUtil.formatNumber(component.getEnergyDraw())));
        }
        if (component.getIntegrity() > 0) {
            lines.add(
                translateToLocalFormatted(
                    "tt.voidcraft.item.stat.integrity",
                    NumberFormatUtil.formatNumber(component.getIntegrity())));
        }
        if (component.getTier() > 0) {
            lines.add(translateToLocalFormatted("tt.voidcraft.item.stat.tier", component.getTier()));
        }

        if (component == VoidcraftComponent.CONTROLLER) {
            lines.add(translateToLocal("tt.voidcraft.component.controller.required"));
        }
        return lines.toArray(new String[0]);
    }
}
