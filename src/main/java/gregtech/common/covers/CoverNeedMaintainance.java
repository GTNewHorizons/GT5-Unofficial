package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.mui1.cover.NeedMaintainanceUIFactory;

public class CoverNeedMaintainance extends CoverLegacyData {

    public CoverNeedMaintainance(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public static boolean isRotor(ItemStack rotor) {
        return (rotor != null && rotor.getItem() instanceof MetaGeneratedTool
            && rotor.getItemDamage() >= 170
            && rotor.getItemDamage() <= 176);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        boolean needsRepair = false;
        if (coverable instanceof IGregTechTileEntity tTileEntity) {
            final IMetaTileEntity mTileEntity = tTileEntity.getMetaTileEntity();
            if (mTileEntity instanceof MTEMultiBlockBase multi) {
                final int ideal = multi.getIdealStatus();
                final int real = multi.getRepairStatus();
                final ItemStack tRotor = multi.getRealInventory()[1];
                final int coverVar = this.coverData >>> 1;
                if (coverVar < 5) {
                    if (ideal - real > coverVar) needsRepair = true;
                } else if (coverVar == 5 || coverVar == 6) {
                    if (isRotor(tRotor)) {
                        long tMax = MetaGeneratedTool.getToolMaxDamage(tRotor);
                        long tCur = MetaGeneratedTool.getToolDamage(tRotor);
                        if (coverVar == 5) {
                            needsRepair = (tCur >= tMax * 8 / 10);
                        } else {
                            long mExpectedDamage = Math.round(
                                Math.min(
                                    multi.mEUt / multi.damageFactorLow,
                                    Math.pow(multi.mEUt, multi.damageFactorHigh)));
                            needsRepair = tCur + mExpectedDamage * 2 >= tMax;
                        }
                    } else {
                        needsRepair = true;
                    }
                }
            }
        }
        if (this.coverData % 2 == 0) {
            needsRepair = !needsRepair;
        }

        coverable.setOutputRedstoneSignal(coverSide, (byte) (needsRepair ? 0 : 15));
        coverable.setOutputRedstoneSignal(coverSide.getOpposite(), (byte) (needsRepair ? 0 : 15));
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = (this.coverData + (aPlayer.isSneaking() ? -1 : 1)) % 14;
        if (this.coverData < 0) {
            this.coverData = 13;
        }
        switch (this.coverData) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("056", "Emit if 1 Maintenance Needed"));
            case 1 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("057", "Emit if 1 Maintenance Needed(inverted)"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("058", "Emit if 2 Maintenance Needed"));
            case 3 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("059", "Emit if 2 Maintenance Needed(inverted)"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("060", "Emit if 3 Maintenance Needed"));
            case 5 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("061", "Emit if 3 Maintenance Needed(inverted)"));
            case 6 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("062", "Emit if 4 Maintenance Needed"));
            case 7 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("063", "Emit if 4 Maintenance Needed(inverted)"));
            case 8 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("064", "Emit if 5 Maintenance Needed"));
            case 9 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("065", "Emit if 5 Maintenance Needed(inverted)"));
            case 10 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("066", "Emit if rotor needs maintenance low accuracy mod"));
            case 11 -> GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.trans("067", "Emit if rotor needs maintenance low accuracy mod(inverted)"));
            case 12 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.trans("068", "Emit if rotor needs maintenance high accuracy mod"));
            case 13 -> GTUtility.sendChatToPlayer(
                aPlayer,
                GTUtility.trans("069", "Emit if rotor needs maintenance high accuracy mod(inverted)"));
        }
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 60;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new NeedMaintainanceUIFactory(buildContext).createWindow();
    }

}
