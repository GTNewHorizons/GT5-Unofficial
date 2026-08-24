package tectech.voidcraft.machine;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.enums.HarvestTool;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverRegistry;

/**
 * A Voidcraft component block, as a machine-block meta tile entity.
 *
 * <p>
 * Components live on the standard machine block (like every other GT machine), which means they can be
 * <b>wrenched to a facing</b>, <b>render different textures per face</b>, and — the important part —
 * <b>accept Voidcraft covers</b> on any of their six faces. Covers are the compact way to add more parts to a
 * ship: a hull block with six covers carries up to six components worth of stats.
 *
 * <p>
 * Engines are directional: the exhaust leaves the <em>front</em> face, so the thrust pushes the ship in the
 * opposite direction (see {@link tectech.voidcraft.ship.VoidcraftBlueprint#computeStats()}). Wrench a hull block to
 * aim its engine; thruster covers push away from the face they are mounted on.
 *
 * <p>
 * Non-electric, no inventory, no client tick — a pure hull part. Only Voidcraft cover items can be mounted on it.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEVoidcraftComponent extends MetaTileEntity {

    private final VoidcraftComponent component;
    private final ITexture texture;

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

    /** Static hull part — no custom NBT payload. */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

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
        lines.add(translateToLocal("tt.voidcraft.component.base_hint"));

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
        if (component == VoidcraftComponent.ENGINE) {
            lines.add(translateToLocal("tt.voidcraft.component.engine_direction"));
        }
        return lines.toArray(new String[0]);
    }
}
