package com.github.technus.tectech.thing.metaTileEntity.pipe;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_PipeBlock_Energy extends GT_MetaTileEntity_Pipe_Energy {

    public GT_MetaTileEntity_PipeBlock_Energy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PipeBlock_Energy(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_PipeBlock_Energy(mName);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
    }

    @Override
    public float getThickNess() {
        return 1f;
    }

    @Override
    public float getExplosionResistance(ForgeDirection side) {
        return 1000.0f;
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(
                super.getDescription(),
                EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("gt.blockmachines.pipe.desc.4"));
    }
}
