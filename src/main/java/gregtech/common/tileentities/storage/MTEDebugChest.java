package gregtech.common.tileentities.storage;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCHEST_GLOW;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTEDebugChest extends MTEQuantumChest {

    public MTEDebugChest(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEDebugChest(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(translateToLocal("GT5U.machines.digitalchest.debugtooltip"));
        super.addAdditionalTooltipInformation(stack, tooltip);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDebugChest(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && getItemStack() != null) {
            setItemCount(Integer.MAX_VALUE);
            // Void input if it's the same item
            if (GTUtility.areStacksEqual(getItemStack(), mInventory[0], true)) {
                mInventory[0] = null;
            }
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection != facingDirection) return new ITexture[] { MACHINE_CASINGS[14][colorIndex + 1] };
        return new ITexture[] { MACHINE_CASINGS[14][colorIndex + 1], TextureFactory.of(OVERLAY_SCHEST),
            TextureFactory.builder()
                .addIcon(OVERLAY_SCHEST_GLOW)
                .glow()
                .build() };
    }

    @Override
    public int getMaxItemCount() {
        return Integer.MAX_VALUE;
    }
}
