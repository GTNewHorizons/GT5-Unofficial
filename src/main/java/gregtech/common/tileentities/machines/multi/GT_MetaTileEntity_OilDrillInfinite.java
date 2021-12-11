package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.GT_Values.VN;

public class GT_MetaTileEntity_OilDrillInfinite extends GT_MetaTileEntity_OilDrillBase{
    public GT_MetaTileEntity_OilDrillInfinite(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_OilDrillInfinite(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        String casings = getCasingBlockItem().get(0).getDisplayName();

        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Pump")
                .addInfo("Controller Block for the Infinite Oil/Gas/Fluid Drilling Rig ")
                .addInfo("Works on " + getRangeInChunks() + "x" + getRangeInChunks() + " chunks")
                .addSeparator()
                .beginStructureBlock(3, 7, 3, false)
                .addController("Front bottom")
                .addStructureInfo(casings + " form the 3x1x3 Base")
                .addOtherStructurePart(casings, " 1x3x1 pillar above the center of the base (2 minimum total)")
                .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
                .addEnergyHatch(VN[getMinTier()] + "+, Any base casing", 1)
                .addMaintenanceHatch("Any base casing", 1)
                .addInputBus("Mining Pipes or Circuits, optional, any base casing", 1)
                .addOutputHatch("Any base casing", 1)
                .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_OilDrillInfinite(mName);
    }
    @Override
    protected FluidStack pumpOil(float speed){
        FluidStack baseRate = super.pumpOil(-1);
        baseRate.amount *= 1+(GT_Utility.getTier(getMaxInputVoltage()) - getMinTier());
        return baseRate;
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_MiningNeutronium;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Neutronium;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 178;
    }

    @Override
    protected int getRangeInChunks() {
        return 4;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ){
        return;
    }

    @Override
    protected int getMinTier() {
        return 9;
    }
}
