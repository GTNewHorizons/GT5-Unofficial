package gregtech.api.metatileentity.implementations;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;

public class MTEHatchBulkCatalystHousing extends MTEHatchNonConsumableBase {

    private int catalystCapacity = 0;

    public MTEHatchBulkCatalystHousing(int ID, String name, String nameRegional, int tier, int itemCapacity) {
        super(ID, name, nameRegional, tier, "Dedicated Catalyst Storage");
        catalystCapacity = itemCapacity;
    }

    public MTEHatchBulkCatalystHousing(String name, String[] description, ITexture[][][] textures, int tier,
        int itemCapacity) {
        super(name, tier, description, textures);
        catalystCapacity = itemCapacity;
    }

    @Override
    public int getItemCapacity() {
        return catalystCapacity;
    }

    public int getStoredCatalystMeta() {
        if (getItemStack() == null) return -1;
        return getItemStack().getItemDamage();
    }

    @Override
    public boolean isValidItem(ItemStack item) {
        return MTEChemicalPlant.isCatalyst(item);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(TexturesGtBlock.Overlay_Bus_Catalyst)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_Bus_Catalyst)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(TexturesGtBlock.Overlay_Bus_Catalyst)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_Bus_Catalyst)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchBulkCatalystHousing(mName, mDescriptionArray, mTextures, mTier, catalystCapacity);
    }
}
