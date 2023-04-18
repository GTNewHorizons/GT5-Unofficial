package gregtech.common.tileentities.machines.basic;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.render.TextureFactory;

public class GT_MetaTileEntity_CuringOven extends GT_MetaTileEntity_BasicMachine implements IAddUIWidgets {

    public GT_MetaTileEntity_CuringOven(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            "Heats tools for hardening",
            1,
            1,
            "E_Oven.png",
            "",
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_SIDE_ACTIVE")),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_SIDE")),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_FRONT_ACTIVE")),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_FRONT")),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_TOP_ACTIVE")),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_TOP")),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_BOTTOM_ACTIVE")),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_BOTTOM")));
    }

    public GT_MetaTileEntity_CuringOven(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
        String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_CuringOven(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_CuringOven(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures,
            this.mGUIName,
            this.mNEIName);
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return (super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack))
            && ItemList.Cell_Empty.isStackEqual(aStack);
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            for (ItemStack tStack : mInventory) if (tStack != null && tStack.getItem() instanceof GT_MetaGenerated_Tool
                && getBaseMetaTileEntity().getStoredEU() > 0) {
                    getBaseMetaTileEntity().decreaseStoredEnergyUnits(24, true);
                    NBTTagCompound aNBT = tStack.getTagCompound();
                    if (aNBT != null) {
                        int tHeat = 300;
                        long tWorldTime = getBaseMetaTileEntity().getWorld()
                            .getTotalWorldTime();
                        aNBT = aNBT.getCompoundTag("GT.ToolStats");
                        if (aNBT != null && aNBT.hasKey("Heat")) {
                            tHeat = aNBT.getInteger("Heat");
                            if (aNBT.hasKey("HeatTime")) {
                                long tHeatTime = aNBT.getLong("HeatTime");
                                if (tWorldTime > (tHeatTime + 10)) {
                                    tHeat = (int) (tHeat - ((tWorldTime - tHeatTime) / 10));
                                    if (tHeat < 300) tHeat = 300;
                                }
                            }
                        }
                        tHeat++;
                        if (aNBT != null) {
                            aNBT.setInteger("Heat", tHeat);
                            aNBT.setLong("HeatTime", tWorldTime);
                        }
                        if (tHeat > GT_MetaGenerated_Tool.getPrimaryMaterial(tStack).mMeltingPoint) {
                            mInventory[0] = null;
                        }
                    }
                }
        }
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        getBaseMetaTileEntity().add1by1Slot(builder);
    }
}
