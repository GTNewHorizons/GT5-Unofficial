package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.GTValues.VN;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEOilDrillInfinite extends MTEOilDrillBase {

    public MTEOilDrillInfinite(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOilDrillInfinite(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        String casings = getCasingBlockItem().get(0)
            .getDisplayName();

        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Pump")
            .addInfo("Controller Block for the Infinite Oil/Gas/Fluid Drilling Rig ")
            .addInfo("Works on " + getRangeInChunks() + "x" + getRangeInChunks() + " chunks")
            .addSeparator()
            .beginStructureBlock(3, 7, 3, false)
            .addController("Front bottom")
            .addOtherStructurePart(casings, "form the 3x1x3 Base")
            .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
            .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
            .addEnergyHatch("1x " + VN[getMinTier()] + "+, Any base casing", 1)
            .addMaintenanceHatch("Any base casing", 1)
            .addInputBus("Mining Pipes or Circuits, optional, any base casing", 1)
            .addOutputHatch("Any base casing", 1)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOilDrillInfinite(mName);
    }

    @Override
    protected FluidStack pumpOil(float speed, boolean simulate) {
        // always simulate to not deplete vein
        return super.pumpOil(speed, true);
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
        return 8;
    }

    @Override
    protected float computeSpeed() {
        return .5F + (GTUtility.getTier(getMaxInputVoltage()) - getMinTier() + 5) * .25F;
    }

    @Override
    protected int getMinTier() {
        return 9;
    }
}
