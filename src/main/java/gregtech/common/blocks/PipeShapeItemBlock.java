package gregtech.common.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ILocalizedMetaPipeEntity;
import gregtech.api.material.MaterialUtils;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.util.GTItsNotMyFaultException;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.crossmod.backhand.Backhand;

/// The item form of a [PipeShapeBlock]: damage is the material's global index. Display names go through the
/// `gt.oreprefix.*` localization keys with grammatical inflection, tooltips reuse the canonical
/// material-agnostic MTE's description primed with the stack's material, and placement mirrors
/// [ItemMachines#placeBlockAt] with the shape's fixed MTE id.
public class PipeShapeItemBlock extends ItemBlock {

    /// Display-name overrides keyed by legacy internal material name, replacing the material's own name in
    /// the prefix format (the legacy `renameMaterial` names). The wooden and High Pressure names only occur
    /// on fluid pipes and PVC only on item pipes, so one table serves both families.
    private static final Map<String, String> MATERIAL_DISPLAY_OVERRIDES = Map.of(
        "Wood",
        "Wooden",
        "Redstone",
        "High Pressure",
        "Polybenzimidazole",
        "PBI",
        "Plastic",
        "Plastic",
        "Polytetrafluoroethylene",
        "PTFE",
        "PolyvinylChloride",
        "PVC");

    private final PipeShapeBlock shape;

    public PipeShapeItemBlock(Block block) {
        super(block);
        if (!(block instanceof PipeShapeBlock pipeShape)) {
            throw new IllegalArgumentException("PipeShapeItemBlock must back a PipeShapeBlock, got " + block);
        }
        this.shape = pipeShape;
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public PipeShapeBlock getShape() {
        return shape;
    }

    /// The language key overriding the material's display name in a pipe display-name format, or null when
    /// the material keeps its own name. `suffix` picks the per-family key (`.fluidpipe.newname` or
    /// `.itempipe.newname`), matching the keys the legacy pipe registrations put the same names under.
    public static String overrideKeyFor(Material material, String suffix) {
        if (material == null) return null;
        String display = MATERIAL_DISPLAY_OVERRIDES.get(MaterialUtils.internalName(material));
        if (display == null) return null;
        String key = MaterialUtils.localizedNameKey(material) + suffix;
        GTLanguageManager.addStringLocalization(key, display);
        return key;
    }

    /// Whether a material's pipes hide the material tooltip (only the High Pressure pipes do).
    public static boolean skipsMaterialTooltip(Material material) {
        return material != null && "Redstone".equals(MaterialUtils.internalName(material));
    }

    private MetaPipeEntity prototype() {
        return GTUtility.getIndexSafe(GregTechAPI.METATILEENTITIES, shape.getMteId()) instanceof MetaPipeEntity pipe
            ? pipe
            : null;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        final MetaPipeEntity prototype = prototype();
        final Material material = MaterialLibAPI.getMaterialByIndex(stack.getItemDamage());
        if (prototype == null || material == null) {
            return super.getItemStackDisplayName(stack);
        }
        return prototype.withShapeMaterial(material, () -> ((ILocalizedMetaPipeEntity) prototype).getLocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        final MetaPipeEntity prototype = prototype();
        final Material material = MaterialLibAPI.getMaterialByIndex(stack.getItemDamage());
        if (prototype != null && material != null) {
            prototype.withShapeMaterial(material, () -> {
                ((ILocalizedMetaPipeEntity) prototype).addMaterialTooltip(list);
                String[] description = prototype.getDescription();
                if (description != null) Collections.addAll(list, description);
                return null;
            });
        }
        final NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            CoverableTileEntity.addInstalledCoversInformation(nbt, list);
            if (nbt.hasKey("mColor") && nbt.getByte("mColor") != -1) {
                list.add(
                    GTUtility.translate(
                        "gt.tileentity.colored",
                        Dyes.get(nbt.getByte("mColor") - 1).formatting,
                        Dyes.get(nbt.getByte("mColor") - 1).mName));
            }
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int ordinalSide,
        float hitX, float hitY, float hitZ, int meta) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        final int index = stack.getItemDamage();
        if (!world.setBlock(x, y, z, this.field_150939_a, index, 3)) {
            return false;
        }
        if (world.getBlock(x, y, z) != this.field_150939_a) {
            throw new GTItsNotMyFaultException("Failed to place Block even though World.setBlock returned true.");
        }
        if (world.getBlockMetadata(x, y, z) != index) {
            throw new GTItsNotMyFaultException(
                "Failed to set the MetaValue of the Block even though World.setBlock returned true.");
        }
        final IGregTechTileEntity tileEntity = (IGregTechTileEntity) world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            tileEntity.setInitialValuesAsNBT(
                tileEntity.isServerSide() ? stack.getTagCompound() : null,
                (short) shape.getMteId());
            if (player != null) {
                tileEntity.setOwnerName(player.getDisplayName());
                tileEntity.setOwnerUuid(player.getUniqueID());
            }
            tileEntity.setFrontFacing(
                BaseTileEntity.getSideForPlayerPlacing(player, ForgeDirection.UP, tileEntity.getValidFacings()));
            final ForgeDirection oppositeSide = side.getOpposite();
            if (tileEntity.getMetaTileEntity() instanceof IConnectable connectable) {
                connectable.connect(oppositeSide);
                if (player != null && Mods.Backhand.isModLoaded() && connectable instanceof MTEFluidPipe pipe) {
                    pipeDirectionOffhand(player, pipe, side);
                }
            }
            tileEntity.getMetaTileEntity()
                .initDefaultModes(stack.getTagCompound());
        }
        this.field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
        this.field_150939_a.onPostBlockPlaced(world, x, y, z, index);
        return true;
    }

    /// The Backhand offhand-wrench input-blocking shortcut, mirroring `ItemMachines#pipeDirectionOffhand`.
    private static void pipeDirectionOffhand(EntityPlayer player, MTEFluidPipe pipe, ForgeDirection side) {
        ItemStack offHand = Backhand.getOffhandItem(player);
        if (GTUtility.isStackInList(offHand, GregTechAPI.sWrenchList)) {
            ForgeDirection oppositeSide = side.getOpposite();
            if (player.isSneaking()) {
                TileEntity adjTile = pipe.getBaseMetaTileEntity()
                    .getTileEntityAtSide(oppositeSide);
                if (adjTile instanceof IGregTechTileEntity adjGTile
                    && adjGTile.getMetaTileEntity() instanceof MTEFluidPipe adjPipe) {
                    adjPipe.mDisableInput |= (byte) side.flag;
                }
            } else {
                pipe.mDisableInput |= (byte) oppositeSide.flag;
            }
            GTModHandler.damageOrDechargeItem(offHand, 1, 1000, player);
        }
    }
}
