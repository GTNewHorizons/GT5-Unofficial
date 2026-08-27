package tectech.voidcraft.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

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

/**
 * A "dumb" casing block of a Voidcraft multiblock component — a plain machine-block MTE with no GUI, no covers,
 * no stats of its own beyond the mass of its catalog entry. It exists so the component's structure can be built
 * (and prebuilt) in-world, and so the assemblers can whitelist and count its cells.
 */
public class MTEVoidcraftMultiblockCasing extends MetaTileEntity {

    private final VoidcraftComponent component;
    private final ITexture texture;

    public MTEVoidcraftMultiblockCasing(int aID, String aName, String aNameRegional, VoidcraftComponent component) {
        super(aID, aName, aNameRegional, 0);
        this.component = component;
        this.texture = VoidcraftTextures.componentTexture(component);
    }

    public MTEVoidcraftMultiblockCasing(String aName, VoidcraftComponent component) {
        super(aName, 0);
        this.component = component;
        this.texture = VoidcraftTextures.componentTexture(component);
    }

    /** @return the catalog entry this casing block represents */
    public VoidcraftComponent getComponent() {
        return component;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEVoidcraftMultiblockCasing(mName, component);
    }

    /** The component icon on every face (uniform for now). */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] { texture };
    }

    /** All six facings are valid (wrench-rotatable). */
    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    /** Multiblock component blocks take no covers. */
    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return false;
    }

    /** Hull part — mined with a wrench, like the other Voidcraft blocks. */
    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.WrenchLevel2.toTileEntityBaseType();
    }

    /** No inventory on a casing block. */
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

    /** No payload on a casing block. */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {}

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {}

    @Override
    public String[] getDescription() {
        return new String[] { translateToLocal("tt.voidcraft.multiblock_casing_hint"),
            translateToLocalFormatted(
                "tt.voidcraft.item.stat.mass",
                NumberFormatUtil.formatNumber(component.getMass())), };
    }
}
