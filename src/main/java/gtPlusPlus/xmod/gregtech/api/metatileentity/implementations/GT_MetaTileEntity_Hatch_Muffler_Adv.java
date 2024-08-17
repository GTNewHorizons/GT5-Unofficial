package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.GT_Mod;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.common.GT_Pollution;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_Hatch_Muffler_Adv extends GT_MetaTileEntity_Hatch_Muffler implements IAddGregtechLogo {

    protected int SLOT_FILTER = 0;

    @Override
    public void onConfigLoad(GT_Config aConfig) {
        super.onConfigLoad(aConfig);
        try {
            int a1 = GT_Mod.gregtechproxy.mPollutionSmogLimit;
            if (a1 > 0) {
                mPollutionSmogLimit = a1;
            }
        } catch (Throwable t) {
            mPollutionSmogLimit = 500000;
        }
    }

    private int mPollutionSmogLimit = 500000;

    public GT_MetaTileEntity_Hatch_Muffler_Adv(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[] { "" });
    }

    public GT_MetaTileEntity_Hatch_Muffler_Adv(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    final String[] mDescription = new String[] { "Outputs pollution from a multiblock", "DO NOT OBSTRUCT THE OUTPUT!",
        "Requires 3 Air Blocks in front of the exhaust face",
        mTier < 5 ? "Requires an Air Filter"
            : "Requires an Air Filter " + EnumChatFormatting.WHITE + "[Tier 2]" + EnumChatFormatting.GRAY,
        "Can take Air Filters from an input bus of the multiblock",
        "Reduces Pollution to " + calculatePollutionReduction(100, true) + "%",
        "Recovers " + (100 - calculatePollutionReduction(100, true)) + "% of CO2/CO/SO2", CORE.GT_Tooltip.get() };

    @Override
    public String[] getDescription() {
        return mDescription;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Muffler_Adv) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Hatch_Muffler_Adv) };
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex == SLOT_FILTER;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return (aIndex == this.SLOT_FILTER && isAirFilter(aStack));
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Muffler_Adv(this.mName, this.mTier, mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public boolean polluteEnvironment(MetaTileEntity parentTileEntity) {
        if (!airCheck()) return false; // Muffler obstructed.

        int emission = 10000;
        if (damageAirFilter(parentTileEntity)) {
            // damageAirFilter already checks that we have a valid filter.
            emission = calculatePollutionReduction(emission, true);
        } else {
            // Revert to reduction of the basic muffler.
            emission = super.calculatePollutionReduction(emission);
        }
        GT_Pollution.addPollution(getBaseMetaTileEntity(), emission);
        return true;
    }

    @Override
    public int calculatePollutionReduction(int aPollution) {
        // This is called by EBF to calculate exhaust gas amounts, we need to check the filter.
        return calculatePollutionReduction(aPollution, false);
    }

    /**
     * Calculates pollution reduction.
     *
     * @param aPollution   Amount of pollution to be reduced.
     * @param ignoreFilter If this is true, assumes that a valid filter is present without checking (for example, to
     *                     build tooltips).
     * @return Amount of pollution after reduction.
     */
    protected int calculatePollutionReduction(int aPollution, boolean ignoreFilter) {
        if (!ignoreFilter && !hasAirFilter()) {
            // Without a filter, downgrade to basic muffler reduction.
            return super.calculatePollutionReduction(aPollution);
        }

        // Special case to be always better than a basic muffler.
        if (mTier < 2) return (int) (aPollution * 0.95);
        if (mTier > 8) return 0;

        return (int) (aPollution * Math.pow(0.64D, mTier - 1));
    }

    /**
     *
     * @return True if enough blocks in front of the muffler are air.
     */
    private boolean airCheck() {
        IGregTechTileEntity bmte = getBaseMetaTileEntity();
        ForgeDirection facing = bmte.getFrontFacing();
        return bmte.getAirAtSide(facing) && bmte.getAirAtSideAndDistance(facing, 1)
            && bmte.getAirAtSideAndDistance(facing, 2);
    }

    /**
     * Try to damage an air filter. Will first try to find a valid filter in the hatch's own inventory, then in the
     * input buses of the parent multiblock. If the filter is destroyed, will try to replace it from the parent
     * multiblock's input buses again.
     *
     * @param parentTileEntity Which multiblock this hatch is a part of. If this is null, only checks inventory of the
     *                         muffler.
     * @return True if the filter has been successfully damaged.
     */
    private boolean damageAirFilter(MetaTileEntity parentTileEntity) {
        if (!findAirFilter(parentTileEntity)) return false; // No filter available.

        ItemStack filter = mInventory[SLOT_FILTER];
        if (filter == null) return false; // This should never happen if findAirFilter() above succeeded.

        long currentDamage = ItemAirFilter.getFilterDamage(filter);
        if (currentDamage < ItemAirFilter.getFilterMaxDamage(filter) - 1) {
            // Damage filter by one step.
            ItemAirFilter.setFilterDamage(filter, currentDamage + 1);
            return true;
        } else {
            // Destroy the filter.
            mInventory[SLOT_FILTER] = null;

            // Try to find a new one.
            findAirFilter(parentTileEntity);

            // Regardless of whether we have a new filter or not, *this* operation succeeded.
            return true;
        }
    }

    /**
     * Try to find a valid air filter in the input buses of the parent multiblock.
     *
     * @param parentTileEntity Which multiblock this hatch is a part of. If this is null, only checks inventory of the
     *                         muffler.
     * @return True if the inventory of the muffler already contains an air filter, or if one was retrieved from the
     *         parent multiblock.
     */
    private boolean findAirFilter(MetaTileEntity parentTileEntity) {
        if (hasAirFilter()) return true; // Has a filter in inventory.
        if (mInventory[SLOT_FILTER] != null) return false; // Has a non-filter item in inventory.
        if (parentTileEntity == null) return false; // Unknown parent multiblock.

        if (parentTileEntity instanceof GT_MetaTileEntity_MultiBlockBase GTMultiBase) {
            for (var inputBus : GTMultiBase.mInputBusses) {
                for (ItemStack stack : inputBus.mInventory) {
                    if (isAirFilter(stack)) {
                        ItemStack stackCopy = stack.copy();
                        if (GTMultiBase.depleteInput(stack)) {
                            mInventory[SLOT_FILTER] = stackCopy;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     *
     * @return True if the item currently in the inventory is an air filter valid for this tier of muffler hatch.
     */
    private boolean hasAirFilter() {
        return isAirFilter(mInventory[SLOT_FILTER]);
    }

    /**
     *
     * @param filter
     * @return True if the argument is an air filter valid for this tier of muffler hatch.
     */
    public boolean isAirFilter(ItemStack filter) {
        if (filter == null) return false;
        if (filter.getItem() instanceof ItemAirFilter) {
            if (mTier < 5) {
                // Accept any filter.
                return true;
            } else {
                // Accept only T2 filter.
                return filter.getItemDamage() == 1;
            }
        }
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isClientSide()) {
            if (this.getBaseMetaTileEntity()
                .isActive()) {
                String aParticleName;
                if ((aTick % 2) == 0) {
                    aParticleName = "cloud";
                } else {
                    aParticleName = "smoke";
                }
                this.pollutionParticles(
                    this.getBaseMetaTileEntity()
                        .getWorld(),
                    aParticleName);
            }
        }
    }

    @Override
    public void pollutionParticles(World aWorld, String name) {
        float ran1 = CORE.RANDOM.nextFloat();
        float ran2 = 0.0F;
        float ran3 = 0.0F;
        boolean chk1 = ran1 * 100.0F < (float) this.calculatePollutionReduction(100);
        boolean chk2;
        boolean chk3;
        int aPollutionAmount = PollutionUtils.getPollution(getBaseMetaTileEntity());
        if (aPollutionAmount >= mPollutionSmogLimit) {
            ran2 = CORE.RANDOM.nextFloat();
            ran3 = CORE.RANDOM.nextFloat();
            chk2 = ran2 * 100.0F < (float) this.calculatePollutionReduction(100);
            chk3 = ran3 * 100.0F < (float) this.calculatePollutionReduction(100);
            if (!chk1 && !chk2 && !chk3) {
                return;
            }
        } else {
            if (!chk1) {
                return;
            }

            chk3 = false;
            chk2 = false;
        }

        IGregTechTileEntity aMuffler = this.getBaseMetaTileEntity();
        ForgeDirection aDir = aMuffler.getFrontFacing();
        float xPos = (float) aDir.offsetX * 0.76F + (float) aMuffler.getXCoord() + 0.25F;
        float yPos = (float) aDir.offsetY * 0.76F + (float) aMuffler.getYCoord() + 0.25F;
        float zPos = (float) aDir.offsetZ * 0.76F + (float) aMuffler.getZCoord() + 0.25F;
        float ySpd = (float) aDir.offsetY * 0.1F + 0.2F + 0.1F * CORE.RANDOM.nextFloat();
        float xSpd;
        float zSpd;
        if (aDir.offsetY == -1) {
            float temp = CORE.RANDOM.nextFloat() * 2.0F * CORE.PI;
            xSpd = (float) Math.sin((double) temp) * 0.1F;
            zSpd = (float) Math.cos((double) temp) * 0.1F;
        } else {
            xSpd = (float) aDir.offsetX * (0.1F + 0.2F * CORE.RANDOM.nextFloat());
            zSpd = (float) aDir.offsetZ * (0.1F + 0.2F * CORE.RANDOM.nextFloat());
        }

        if (chk1) {
            aWorld.spawnParticle(
                name,
                (double) (xPos + ran1 * 0.5F),
                (double) (yPos + CORE.RANDOM.nextFloat() * 0.5F),
                (double) (zPos + CORE.RANDOM.nextFloat() * 0.5F),
                (double) xSpd,
                (double) ySpd,
                (double) zSpd);
        }

        if (chk2) {
            aWorld.spawnParticle(
                name,
                (double) (xPos + ran2 * 0.5F),
                (double) (yPos + CORE.RANDOM.nextFloat() * 0.5F),
                (double) (zPos + CORE.RANDOM.nextFloat() * 0.5F),
                (double) xSpd,
                (double) ySpd,
                (double) zSpd);
        }

        if (chk3) {
            aWorld.spawnParticle(
                name,
                (double) (xPos + ran3 * 0.5F),
                (double) (yPos + CORE.RANDOM.nextFloat() * 0.5F),
                (double) (zPos + CORE.RANDOM.nextFloat() * 0.5F),
                (double) xSpd,
                (double) ySpd,
                (double) zSpd);
        }
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {}

    @Override
    public GUITextureSet getGUITextureSet() {
        return new GUITextureSet().setMainBackground(GTPP_UITextures.BACKGROUND_YELLOW)
            .setItemSlot(GTPP_UITextures.SLOT_ITEM_YELLOW)
            .setTitleTab(
                GTPP_UITextures.TAB_TITLE_YELLOW,
                GTPP_UITextures.TAB_TITLE_DARK_YELLOW,
                GTPP_UITextures.TAB_TITLE_ANGULAR_YELLOW);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inventoryHandler, 0).setFilter(stack -> stack.getItem() instanceof ItemAirFilter)
                .setBackground(getGUITextureSet().getItemSlot())
                .setPos(79, 34));
    }
}
