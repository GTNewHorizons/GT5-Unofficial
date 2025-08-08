package gregtech.common.tileentities.machines.basic;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;

public class MTEReplicator extends MTEBasicMachine {

    public MTEReplicator(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            MachineType.REPLICATOR.tooltipDescription(),
            1,
            1,
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_SIDE_REPLICATOR_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon(
                            "basicmachines/replicator/OVERLAY_SIDE_REPLICATOR_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_SIDE_REPLICATOR")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_SIDE_REPLICATOR_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_FRONT_REPLICATOR_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon(
                            "basicmachines/replicator/OVERLAY_FRONT_REPLICATOR_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_FRONT_REPLICATOR")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_FRONT_REPLICATOR_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_TOP_REPLICATOR_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon(
                            "basicmachines/replicator/OVERLAY_TOP_REPLICATOR_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_TOP_REPLICATOR")),
                TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_TOP_REPLICATOR_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_BOTTOM_REPLICATOR_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon(
                            "basicmachines/replicator/OVERLAY_BOTTOM_REPLICATOR_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_BOTTOM_REPLICATOR")),
                TextureFactory.builder()
                    .addIcon(
                        new Textures.BlockIcons.CustomIcon("basicmachines/replicator/OVERLAY_BOTTOM_REPLICATOR_GLOW"))
                    .glow()
                    .build()));
    }

    public MTEReplicator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEReplicator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.replicatorRecipes;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)
            && ItemList.Cell_Empty.isStackEqual(aStack);
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return aFluid.isFluidEqual(Materials.UUMatter.getFluid(1L));
    }

    @Override
    public int getCapacity() {
        return 3000;
    }
}
