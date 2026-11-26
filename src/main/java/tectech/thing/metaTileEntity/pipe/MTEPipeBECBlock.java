package tectech.thing.metaTileEntity.pipe;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

@IMetaTileEntity.SkipGenerateDescription
public class MTEPipeBECBlock extends MTEPipeBEC {

    public MTEPipeBECBlock(int aID, String aName) {
        super(aID, aName);
    }

    protected MTEPipeBECBlock(MTEPipeBECBlock prototype) {
        super(prototype);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEPipeBECBlock(this);
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
