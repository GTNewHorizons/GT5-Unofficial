package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_Hatch_Catalysts extends GT_MetaTileEntity_Hatch_NbtConsumable {
	
    public GT_MetaTileEntity_Hatch_Catalysts(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0, 16, "Dedicated Catalyst Storage", false);
    }

    public GT_MetaTileEntity_Hatch_Catalysts(String aName, String aDescription, ITexture[][][] aTextures) {
        super(aName, 0, 16, aDescription, false, aTextures);
    }
    
    public GT_MetaTileEntity_Hatch_Catalysts(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, 0, 16, aDescription[0], false, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Bus_Catalyst)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Bus_Catalyst)};
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

	@Override
	public Class<? extends GT_MetaTileEntity_Hatch_NbtConsumable> getHatchEntityClass() {
		return GT_MetaTileEntity_Hatch_Catalysts.class;
	}

	@Override
	public String getNameGUI() {
		return "Catalyst Container";
	}

	@Override
	public AutoMap<ItemStack> getItemsValidForUsageSlots() {
		return new AutoMap<ItemStack>();
	}	

	@Override
	public boolean isItemValidForUsageSlot(ItemStack aStack) {
		return ItemUtils.isCatalyst(aStack);
	}

	@Override
	public int getInputSlotCount() {
		return 16;
	}
}
