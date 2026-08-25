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
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.enums.HarvestTool;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.covers.Cover;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.cover.CoverVoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftCoverRegistry;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.uss.USSProgram;
import tectech.voidcraft.uss.USSProgramDefaults;

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
public class MTEVoidcraftComponent extends MetaTileEntity {

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
     * Derive the default chip from the covers mounted on THIS block (user spec: "default program chips that apply
     * a basic program to the controller when right-clicking the controller. For miner, starlifter, explorer") —
     * the block's covers declare what the ship IS.
     *
     * @return true when any of the chip covers is mounted
     */
    public boolean hasCover(VoidcraftCoverComponent cover) {
        if (!(getBaseMetaTileEntity() instanceof ICoverable)) {
            return false;
        }
        ICoverable coverable = (ICoverable) getBaseMetaTileEntity();
        for (int side = 0; side < 6; side++) {
            Cover c = coverable.getCoverAtSide(ForgeDirection.getOrientation(side));
            if (c instanceof CoverVoidcraftComponent vc && vc.getComponent() == cover) {
                return true;
            }
        }
        return false;
    }

    // endregion

    // region right-click — default program chips (controller only)

    /**
     * Right-click (controller only): applies the default program chip derived from this block's covers
     * (SCANNER_DISH → Explorer, STAR_SIPHON → Starlifter, else Miner — user spec: "chips that apply a basic
     * program to the controller when right-clicking").
     *
     * <p>
     * The clear is a TOGGLE (GT's plumbing never delivers a sneaking tool-less right-click to the MTE — see
     * {@code BaseMetaPipeEntity#onRightclick}): when the block already holds exactly that chip, a right-click
     * removes it again (a ship built from it HOLDS at the launch origin), otherwise it applies it.
     */
    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (component != VoidcraftComponent.CONTROLLER) {
            return false;
        }
        USSProgram chip = USSProgramDefaults.chip(
            hasCover(VoidcraftCoverComponent.SCANNER_DISH),
            hasCover(VoidcraftCoverComponent.STAR_SIPHON),
            hasCover(VoidcraftCoverComponent.MINING_ARRAY));
        USSProgram current = USSProgram.readFromNBT(program);
        if (current != null && current.equals(chip)) {
            setProgramTag(null);
            aPlayer.addChatComponentMessage(
                new ChatComponentText(translateToLocal("tt.voidcraft.controller.program_cleared")));
        } else if (setProgramTag(chip.writeToNBT())) {
            aPlayer.addChatComponentMessage(
                new ChatComponentText(translateToLocal("tt.voidcraft.controller.program_applied")));
        }
        return false;
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
