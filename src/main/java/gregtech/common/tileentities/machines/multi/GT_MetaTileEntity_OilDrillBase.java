package gregtech.common.tileentities.machines.multi;

import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.lang.Math; //Java was written by idiots

import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.GT_Values.debugDriller;
import static gregtech.common.GT_UndergroundOil.undergroundOil;
import static gregtech.common.GT_UndergroundOil.undergroundOilReadInformation;

public abstract class GT_MetaTileEntity_OilDrillBase extends GT_MetaTileEntity_DrillerBase {

    private boolean completedCycle = false;

    private ArrayList<Chunk> mOilFieldChunks = new ArrayList<Chunk>();
    private int mOilId = 0;

    public GT_MetaTileEntity_OilDrillBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_OilDrillBase(String aName) {
        super(aName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mOilId", mOilId);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mOilId = aNBT.getInteger("mOilId");
    }

    protected String[] getDescriptionInternal(String tierSuffix) {
        String casings = getCasingBlockItem().get(0).getDisplayName();
        return new String[]{
                "Controller Block for the Oil/Gas/Fluid Drilling Rig " + (tierSuffix != null ? tierSuffix : ""),
                "Size(WxHxD): 3x7x3", "Controller (Front middle at bottom)",
                "3x1x3 Base of " + casings,
                "1x3x1 " + casings + " pillar (Center of base)",
                "1x3x1 " + getFrameMaterial().mName + " Frame Boxes (Each pillar side and on top)",
                "1x Output Hatch (One of base casings)",
                "1x Maintenance Hatch (One of base casings)",
                "1x " + VN[getMinTier()] + "+ Energy Hatch (Any bottom layer casing)",
                "Working on " + getRangeInChunks() + " * " + getRangeInChunks() + " chunks",
                "Use Programmed Circuits to ignore near exhausted oil field"};
    }


    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "DrillingRig.png");
    }

    protected abstract int getRangeInChunks();

    @Override
    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mOutputHatches.isEmpty() && !mEnergyHatches.isEmpty();
    }

    @Override
    protected void setElectricityStats() {
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(0, GT_Utility.getTier(getMaxInputVoltage()));
        this.mEUt = -7 << (tier << 1);//(1/4) A of current tier when at bottom (7/8) A of current tier while mining
        this.mMaxProgresstime = Math.max(1,
                (workState == STATE_AT_BOTTOM ?
                        (64 * (getRangeInChunks() * getRangeInChunks()))>>(getMinTier()-1)  :
                        120
                ) >> tier);
    }

    @Override
    protected boolean workingAtBottom(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe, int yHead, int oldYHead) {
        switch (tryLowerPipeState(true)) {
            case 0: workState = STATE_DOWNWARD; setElectricityStats(); return true;
            case 3: workState = STATE_UPWARD; return true;
        }

        if (reachingVoidOrBedrock() && tryFillChunkList()) {
            float speed = .5F+(GT_Utility.getTier(getMaxInputVoltage()) - getMinTier()) *.25F;
            FluidStack tFluid = pumpOil(speed);
            if (tFluid != null && tFluid.amount > getTotalConfigValue()){
                this.mOutputFluids = new FluidStack[]{tFluid};
                return true;
            }
        }
        workState = STATE_UPWARD;
        return true;
    }

    private boolean tryFillChunkList(){
        FluidStack tFluid, tOil;
        if (mOilId <= 0) {
            tFluid = undergroundOilReadInformation(getBaseMetaTileEntity());
            if (tFluid == null) return false;
            mOilId = tFluid.getFluidID();
        }
        if (debugDriller) {
            GT_Log.out.println(
                " Driller on  fluid = " + mOilId
            );
        }

        tOil = new FluidStack(FluidRegistry.getFluid(mOilId), 0);

        if (mOilFieldChunks.isEmpty()) {
            Chunk tChunk = getBaseMetaTileEntity().getWorld().getChunkFromBlockCoords(getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getZCoord());
            int range = getRangeInChunks();
            int xChunk = Math.floorDiv(tChunk.xPosition,range) * range; //Java was written by idiots.  For negative values, / returns rounded towards zero. Fucking morons.
            int zChunk = Math.floorDiv(tChunk.zPosition,range) * range;
            if (debugDriller) {
                GT_Log.out.println(
                    "tChunk.xPosition = " + tChunk.xPosition +
                    " tChunk.zPosition = " + tChunk.zPosition +
                    " xChunk = " + xChunk  +
                    " zChunk = " + zChunk
                );
            }
            for (int i = 0; i < range; i++) {
                for (int j = 0; j < range; j++) {
                    if (debugDriller) {
                        GT_Log.out.println(
                            " getChunkX = " + (xChunk + i) +
                            " getChunkZ = " + (zChunk + j)
                        );
                    }
                    tChunk = getBaseMetaTileEntity().getWorld().getChunkFromChunkCoords(xChunk + i, zChunk + j);
                    tFluid = undergroundOilReadInformation(tChunk);
                    if (debugDriller) {
                        GT_Log.out.println(
                            " Fluid in chunk = " + tFluid.getFluid().getID()
                        );
                    }
                    if (tOil.isFluidEqual(tFluid) && tFluid.amount > 0) {
                        mOilFieldChunks.add(tChunk);
                        if (debugDriller) {
                            GT_Log.out.println(
                                " Matching fluid, quantity = " + tFluid.amount
                            );
                        }
                    }
                }
            }
        }
        if (debugDriller) {
            GT_Log.out.println(
                "mOilFieldChunks.size = " + mOilFieldChunks.size()
            );
        }
        return !mOilFieldChunks.isEmpty();
    }

    private FluidStack pumpOil(float speed){
        if (mOilId <= 0) return null;
        FluidStack tFluid, tOil;
        tOil = new FluidStack(FluidRegistry.getFluid(mOilId), 0);
        if (debugDriller) {
            GT_Log.out.println(
                " pump speed = " + speed
             );
        }

        ArrayList<Chunk> emptyChunks = new ArrayList<Chunk>();
        
        for (Chunk tChunk : mOilFieldChunks) {
            tFluid = undergroundOil(tChunk,speed);
            if (debugDriller) {
                GT_Log.out.println(
                    " chunkX = " + tChunk.getChunkCoordIntPair().chunkXPos + 
                    " chunkZ = " + tChunk.getChunkCoordIntPair().chunkZPos 
                );
                if( tFluid != null ) {
                    GT_Log.out.println(
                        "     Fluid pumped = " + tFluid.amount
                    );
                } else {
                    GT_Log.out.println(
                        "     No fluid pumped " 
                    );
                }
                
            }
            if (tFluid == null || tFluid.amount<1) emptyChunks.add(tChunk);
            if (tOil.isFluidEqual(tFluid)) tOil.amount += tFluid.amount;
        }
        for( Chunk tChunk : emptyChunks) {
            mOilFieldChunks.remove( tChunk );
        }
        return tOil.amount == 0 ? null : tOil;
    }
}
