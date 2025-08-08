package tectech.thing.metaTileEntity.pipe;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTEPipeBlockLaser extends MTEPipeLaser {

    public MTEPipeBlockLaser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPipeBlockLaser(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEPipeBlockLaser(mName);
    }

    @Override
    public float getThickness() {
        return 1f;
    }

    @Override
    public float getCollisionThickness() {
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
