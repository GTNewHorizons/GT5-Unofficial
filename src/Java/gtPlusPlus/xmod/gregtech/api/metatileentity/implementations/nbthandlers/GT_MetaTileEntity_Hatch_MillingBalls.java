package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_Hatch_MillingBalls extends GT_MetaTileEntity_Hatch_NbtConsumable {
	
    public GT_MetaTileEntity_Hatch_MillingBalls(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 6, 4, "Dedicated Milling Ball Storage", true);
    }

    public GT_MetaTileEntity_Hatch_MillingBalls(String aName, String aDescription, ITexture[][][] aTextures) {
        super(aName, 6, 4, aDescription, true, aTextures);
    }
    
    public GT_MetaTileEntity_Hatch_MillingBalls(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, 6, 4, aDescription[0], true, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Bus_Milling_Balls)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Bus_Milling_Balls)};
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

	@Override
	public Class<? extends GT_MetaTileEntity_Hatch_NbtConsumable> getHatchEntityClass() {
		return GT_MetaTileEntity_Hatch_MillingBalls.class;
	}

	@Override
	public String getNameGUI() {
		return "Ball Housing";
	}

	@Override
	public AutoMap<ItemStack> getItemsValidForUsageSlots() {
		return new AutoMap<ItemStack>();
	}	

	@Override
	public boolean isItemValidForUsageSlot(ItemStack aStack) {
		return ItemUtils.isMillingBall(aStack);
	}

	@Override
	public int getInputSlotCount() {
		return 4;
	}
}
