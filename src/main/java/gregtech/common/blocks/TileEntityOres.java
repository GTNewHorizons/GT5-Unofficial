package gregtech.common.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.StoneType;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.OreInfo;

public class TileEntityOres extends TileEntity {

    public short mMetaData = 0;
    public boolean mNatural = false;

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        this.mMetaData = aNBT.getShort("m");
        this.mNatural = aNBT.getBoolean("n");
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        aNBT.setShort("m", this.mMetaData);
        aNBT.setBoolean("n", this.mNatural);
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    private boolean recursed = false;

    @Override
    public void updateEntity() {
        if (!recursed) {
            recursed = true;

            try(OreInfo<Materials> info = OreInfo.getNewInfo()) {
                info.stoneType = StoneType.STONE_TYPES.get(mMetaData % 16000 / 1000);
                info.material = GregTechAPI.sGeneratedMaterials[mMetaData % 1000];
                info.isSmall = mMetaData >= 16000;
                info.isNatural = mNatural;

                var p = GTOreAdapter.INSTANCE.getBlock(info);

                worldObj.setBlock(xCoord, yCoord, zCoord, p.left(), p.rightInt(), 3);
            }
        }
    }
}
