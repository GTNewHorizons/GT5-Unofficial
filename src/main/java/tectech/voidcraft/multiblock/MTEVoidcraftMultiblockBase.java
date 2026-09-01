package tectech.voidcraft.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.ship.VoidcraftComponent;

/**
 * A Voidcraft multiblock component — a GT multiblock that is a SHIP PART, not a processing machine.
 *
 * <p>
 * User spec: the component is defined as a GT multiblock (StructureLib structure), it takes no hatches or buses —
 * only "dumb" casing blocks — and it validates its OWN structure. The assemblers find this machine in the scan
 * volume and digitize the component (stats included) iff the structure is formed (the assemblers force this
 * machine's own structure check during the scan). The component carries its full stats in its catalog entry; the
 * casing entries contribute mass only.
 *
 * <p>
 * In-world the machine is dormant: no recipe, no energy flow — its only job is to exist as a formed structure the
 * assemblers can digitize (and to maintain that state, re-checking every ~50 ticks).
 *
 * <p>
 * Each component is its own subclass: its own {@code STRUCTURE_DEFINITION} (autobuild + preview work for any
 * multiblock with a standard StructureLib definition), its own catalog entry, its own MTE id.
 */
public abstract class MTEVoidcraftMultiblockBase extends TTMultiblockBase implements ISurvivalConstructable {

    protected static final String STRUCTURE_PIECE_MAIN = "main";

    private final VoidcraftComponent controller;
    private final ITexture texture;

    protected MTEVoidcraftMultiblockBase(int aID, String aName, String aNameRegional, VoidcraftComponent controller) {
        super(aID, aName, aNameRegional);
        this.controller = controller;
        this.texture = VoidcraftTextures.componentTexture(controller);
    }

    protected MTEVoidcraftMultiblockBase(String aName, VoidcraftComponent controller) {
        super(aName);
        this.controller = controller;
        this.texture = VoidcraftTextures.componentTexture(controller);
    }

    /** @return the catalog entry this machine represents (the stats-carrying controller) */
    public VoidcraftComponent getControllerComponent() {
        return controller;
    }

    /** @return the casing catalog entries this structure is built from (zero-stat filler blocks) */
    protected abstract List<VoidcraftComponent> getCasingComponents();

    /** @return the total cell count of the structure (1 controller + all casings) */
    protected abstract int getExpectedCells();

    /** @return the anchor ({@code ~}) cell's offset from the machine position, as (x, y, z) */
    protected abstract int[] getAnchorOffset();

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return newMetaEntity(mName);
    }

    protected abstract MTEVoidcraftMultiblockBase newMetaEntity(String aName);

    // ------------------------------------------------------------------
    // Structure — the machine's whole check: its own shape (no hatches, no energy)
    // ------------------------------------------------------------------

    @Override
    public void checkMachine(IGregTechTileEntity mte, ItemStack stack, List<StructureError> errors) {
        int[] anchor = getAnchorOffset();
        checkPiece(STRUCTURE_PIECE_MAIN, anchor[0], anchor[1], anchor[2], errors);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        // No energy flow, nothing to service.
        return false;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    // ------------------------------------------------------------------
    // Texture / covers / block behaviour
    // ------------------------------------------------------------------

    /** The component icon on every face (uniform for now). */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] { texture };
    }

    /** Multiblock component blocks take no covers. */
    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder().addMachineType("Voidcraft Multiblock Component")
            .addSupportAny()
            .addInfo(translateToLocal("tt.voidcraft_component.station_only"));
        String hint = translateToLocal(componentTooltipHint());
        if (!hint.isEmpty() && !hint.equals(componentTooltipHint())) {
            tt = tt.addInfo(hint);
        }
        return tt.toolTipFinisher();
    }

    /**
     * @return the lang key of the component's tooltip hint (what it does once built into a Voidbase — empty for none)
     */
    protected String componentTooltipHint() {
        return "";
    }

    // ------------------------------------------------------------------
    // Autobuild (NEI + survival)
    // ------------------------------------------------------------------

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int[] anchor = getAnchorOffset();
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, anchor[0], anchor[1], anchor[2]);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) {
            return -1;
        }
        int[] anchor = getAnchorOffset();
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            anchor[0],
            anchor[1],
            anchor[2],
            elementBudget,
            source,
            actor,
            false,
            true);
    }
}
