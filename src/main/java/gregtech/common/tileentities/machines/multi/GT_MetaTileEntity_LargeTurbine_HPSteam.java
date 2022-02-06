package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

public class GT_MetaTileEntity_LargeTurbine_HPSteam extends GT_MetaTileEntity_LargeTurbine {

    public boolean achievement = false;
    private boolean looseFit = false;

    public GT_MetaTileEntity_LargeTurbine_HPSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeTurbine_HPSteam(String aName) {
        super(aName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{MACHINE_CASINGS[1][aColorIndex + 1],
                aFacing == aSide ?
                        (aActive ? TextureFactory.builder().addIcon(LARGETURBINE_TI_ACTIVE5).extFacing().build() : hasTurbine() ? TextureFactory.builder().addIcon(LARGETURBINE_TI5).extFacing().build() : TextureFactory.builder().addIcon(LARGETURBINE_TI_EMPTY5).extFacing().build())
                        : casingTexturePages[0][59]};
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Steam Turbine")
                .addInfo("Controller block for the Large High Pressure Steam Turbine")
                .addInfo("Needs a Turbine, place inside controller")
                .addInfo("Outputs Steam as well as producing power")
                .addInfo("Power output depends on turbine and fitting")
                .addInfo("Use screwdriver to adjust fitting of turbine")
                .addSeparator()
                .beginStructureBlock(3, 3, 4, true)
                .addController("Front center")
                .addCasingInfo("Titanium Turbine Casing", 24)
                .addDynamoHatch("Back center", 1)
                .addMaintenanceHatch("Side centered", 2)
                .addInputHatch("Superheated Steam, Side centered", 2)
                .addOutputHatch("Steam, Side centered", 2)
                .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeTurbine_HPSteam(mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public byte getCasingMeta() {
        return 11;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 59;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff) {
        if (looseFit) {
            aOptFlow *= 4;
            if (aBaseEff > 10000) {
                aOptFlow *= Math.pow(1.1f, ((aBaseEff - 7500) / 10000F) * 20f);
                aBaseEff = 7500;
            } else if (aBaseEff > 7500) {
                aOptFlow *= Math.pow(1.1f, ((aBaseEff - 7500) / 10000F) * 20f);
                aBaseEff *= 0.75f;
            } else {
                aBaseEff *= 0.75f;
            }
        }
        int tEU = 0;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow = 0;
        int remainingFlow = GT_Utility.safeInt((long) (aOptFlow * 1.25f)); // Allowed to use up to 125% of optimal flow.  Variable required outside of loop for multi-hatch scenarios.
        this.realOptFlow = aOptFlow;

        storedFluid = 0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            final FluidStack aFluidStack = aFluids.get(i);
            if (GT_ModHandler.isSuperHeatedSteam(aFluidStack)) {
                flow = Math.min(aFluidStack.amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                depleteInput(new FluidStack(aFluidStack, flow)); // deplete that amount
                this.storedFluid += aFluidStack.amount;
                remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                totalFlow += flow; // track total input used
                if (!achievement) {
                    try {
                        GT_Mod.achievements.issueAchievement(this.getBaseMetaTileEntity().getWorld().getPlayerEntityByName(this.getBaseMetaTileEntity().getOwnerName()), "efficientsteam");
                    } catch (Exception ignored) {
                    }
                    achievement = true;
                }
            } else if (GT_ModHandler.isAnySteam(aFluidStack)) {
                depleteInput(new FluidStack(aFluidStack, aFluidStack.amount));
            }
        }
        if (totalFlow <= 0) return 0;
        tEU = totalFlow;
        addOutput(GT_ModHandler.getSteam(totalFlow));
        if (totalFlow == aOptFlow) {
            tEU = GT_Utility.safeInt((long) tEU * (long) aBaseEff / 10000L);
        } else {
            float efficiency = 1.0f - Math.abs((totalFlow - aOptFlow) / (float) aOptFlow);
            //if(totalFlow>aOptFlow){efficiency = 1.0f;}
            tEU *= efficiency;
            tEU = Math.max(1, GT_Utility.safeInt((long) tEU * (long) aBaseEff / 10000L));
        }

        return tEU;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GT_Utility.sendChatToPlayer(aPlayer, looseFit ? GT_Utility.trans("500", "Fitting: Loose - More Flow") : GT_Utility.trans("501", "Fitting: Tight - More Efficiency"));
        }
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (looseFit && XSTR_INSTANCE.nextInt(4) == 0) ? 0 : 1;
    }


    @Override
    public String[] getInfoData() {
        super.looseFit = looseFit;
        return super.getInfoData();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("turbineFitting", looseFit);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        looseFit = aNBT.getBoolean("turbineFitting");
    }
}
