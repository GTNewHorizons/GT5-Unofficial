package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.thing.machineTT;
import com.github.technus.tectech.thing.metaTileEntity.pipe.iConnectsToDataPipe;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import static com.github.technus.tectech.elementalMatter.commonValues.moveAt;
import static gregtech.api.enums.Dyes.MACHINE_METAL;

/**
 * Created by danie_000 on 11.12.2016.
 */
public abstract class GT_MetaTileEntity_Hatch_DataConnector extends GT_MetaTileEntity_Hatch implements iConnectsToDataPipe, machineTT {
    private static Textures.BlockIcons.CustomIcon EM_D_SIDES;
    private static Textures.BlockIcons.CustomIcon EM_D_ACTIVE;
    private static Textures.BlockIcons.CustomIcon EM_D_CONN;

    public short id = -1;
    public int data = 0;
    public byte timeout=2;

    public GT_MetaTileEntity_Hatch_DataConnector(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
    }

    public GT_MetaTileEntity_Hatch_DataConnector(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_D_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_D_ACTIVE");
        EM_D_SIDES = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_D_SIDES");
        EM_D_CONN = new Textures.BlockIcons.CustomIcon("iconsets/EM_DATA_CONN");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_D_ACTIVE, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())), new GT_RenderedTexture(EM_D_CONN)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_D_SIDES, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())), new GT_RenderedTexture(EM_D_CONN)};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setShort("eID", id);
        aNBT.setInteger("eDATA",data);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        id = aNBT.getShort("eID");
        data=aNBT.getInteger("eDATA");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (moveAt == aTick % 20) {
                if(timeout>0) timeout--;
                else data=0;
                moveAround(aBaseMetaTileEntity);
                getBaseMetaTileEntity().setActive(data>0);
            }
        }
    }

    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (id > 0) return new String[]{
                "ID: "+ EnumChatFormatting.AQUA +id,
                "Computation: "+ EnumChatFormatting.AQUA +data
        };
        return new String[]{"Computation: "+ EnumChatFormatting.AQUA +data};
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                mDescription,
                "High speed fibre optics connector.",
                EnumChatFormatting.AQUA + "Must be painted to work"
        };
    }
}
