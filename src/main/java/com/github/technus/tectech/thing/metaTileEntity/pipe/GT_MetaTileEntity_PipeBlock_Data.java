package com.github.technus.tectech.thing.metaTileEntity.pipe;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class GT_MetaTileEntity_PipeBlock_Data extends GT_MetaTileEntity_Pipe_Data {

    public GT_MetaTileEntity_PipeBlock_Data(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PipeBlock_Data(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_PipeBlock_Data(mName);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        return AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX + 1, aY + 1, aZ + 1);
    }

    @Override
    public float getThickNess() {
        return 1f;
    }
}
