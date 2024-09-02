package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_GLOW;
import static gregtech.common.UndergroundOil.undergroundOilReadInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockOresAbstract;
import gregtech.common.blocks.TileEntityOres;
import ic2.core.Ic2Items;

public class MTEAdvSeismicProspector extends MTEBasicMachine {

    boolean ready = false;
    final int radius;
    final int step;
    int cX;
    int cZ;

    public MTEAdvSeismicProspector(int aID, String aName, String aNameRegional, int aTier, int aRadius, int aStep) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1, // amperage
            "",
            1, // input slot count
            1, // output slot count
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_ROCK_BREAKER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_ROCK_BREAKER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_ROCK_BREAKER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_ROCK_BREAKER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_ROCK_BREAKER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ROCK_BREAKER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_ROCK_BREAKER_GLOW)
                    .glow()
                    .build()));
        radius = aRadius;
        step = aStep;
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Place, activate with explosives",
            "2 Powderbarrels, " + "4 Glyceryl Trinitrate, " + "16 TNT, or " + "8 ITNT",
            "Use Data Stick, Scan Data Stick, Print Data Stick, Bind Pages into Book",
            "Ore prospecting area = " + radius * 2 + "x" + radius * 2 + " ONLY blocks below prospector",
            "Oil prospecting area 3x3 oilfields, each is 8x8 chunks" };
    }

    protected MTEAdvSeismicProspector(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        int aRadius, int aStep) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1);
        radius = aRadius;
        step = aStep;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvSeismicProspector(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures,
            this.radius,
            this.step);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isServerSide()) {
            ItemStack aStack = aPlayer.getCurrentEquippedItem();

            if (!ready && (GTUtility.consumeItems(aPlayer, aStack, Item.getItemFromBlock(Blocks.tnt), 16)
                || GTUtility.consumeItems(aPlayer, aStack, Ic2Items.industrialTnt.getItem(), 8)
                || GTUtility.consumeItems(aPlayer, aStack, Materials.Glyceryl, 4)
                || GTUtility.consumeItems(aPlayer, aStack, ItemList.Block_Powderbarrel.getItem(), 2))) {

                this.ready = true;
                this.mMaxProgresstime = (aPlayer.capabilities.isCreativeMode ? 20 : 800);

            } else if (ready && mMaxProgresstime == 0
                && aStack != null
                && aStack.stackSize == 1
                && aStack.getItem() == ItemList.Tool_DataStick.getItem()) {
                    this.ready = false;

                    // prospecting ores
                    HashMap<String, Integer> tOres = new HashMap<>(36);

                    prospectOres(tOres);

                    // prospecting oils
                    ArrayList<String> tOils = new ArrayList<>();
                    prospectOils(tOils);

                    GTUtility.ItemNBT.setAdvancedProspectionData(
                        mTier,
                        aStack,
                        this.getBaseMetaTileEntity()
                            .getXCoord(),
                        this.getBaseMetaTileEntity()
                            .getYCoord(),
                        this.getBaseMetaTileEntity()
                            .getZCoord(),
                        this.getBaseMetaTileEntity()
                            .getWorld().provider.dimensionId,
                        tOils,
                        GTUtility.sortByValueToList(tOres),
                        radius);
                }
        }

        return true;
    }

    private void prospectOils(ArrayList<String> aOils) {

        int xChunk = (getBaseMetaTileEntity().getXCoord() >> 7) << 3; // oil field aligned chunk coords
        int zChunk = (getBaseMetaTileEntity().getZCoord() >> 7) << 3;

        LinkedHashMap<ChunkCoordIntPair, FluidStack> tFluids = new LinkedHashMap<>();
        int oilFieldCount = 0;

        try {
            final int oilfieldSize = 8;
            for (int z = -1; z <= 1; ++z) {
                for (int x = -1; x <= 1; ++x) {
                    ChunkCoordIntPair cInts = new ChunkCoordIntPair(x, z);
                    int min = Integer.MAX_VALUE;
                    int max = Integer.MIN_VALUE;

                    for (int i = 0; i < oilfieldSize; i++) {
                        for (int j = 0; j < oilfieldSize; j++) {
                            Chunk tChunk = getBaseMetaTileEntity().getWorld()
                                .getChunkFromChunkCoords(xChunk + i + x * oilfieldSize, zChunk + j + z * oilfieldSize);
                            FluidStack tFluid = undergroundOilReadInformation(tChunk);
                            if (tFluid != null) {
                                if (tFluid.amount > max) max = tFluid.amount;
                                if (tFluid.amount < min) min = tFluid.amount;
                                if (!tFluids.containsKey(cInts)) {
                                    tFluids.put(cInts, tFluid);
                                }
                            }
                        }
                    }

                    aOils.add(
                        ++oilFieldCount + ","
                            + min
                            + "-"
                            + max
                            + ","
                            + tFluids.get(cInts)
                                .getLocalizedName());
                }
            }
        } catch (Exception ignored) {}
    }

    private void prospectOres(Map<String, Integer> aOres) {
        int tLeftXBound = this.getBaseMetaTileEntity()
            .getXCoord() - radius;
        int tRightXBound = tLeftXBound + 2 * radius;

        int tLeftZBound = this.getBaseMetaTileEntity()
            .getZCoord() - radius;
        int tRightZBound = tLeftZBound + 2 * radius;

        for (int i = tLeftXBound; i <= tRightXBound; i += step) {
            if (Math.abs(i >> 4) % 3 != 1) continue;
            for (int k = tLeftZBound; k <= tRightZBound; k += step) {
                if (Math.abs(k >> 4) % 3 != 1) continue;

                cX = (i >> 4) << 4;
                cZ = (k >> 4) << 4;

                String separator = (cX + 8) + "," + (cZ + 8) + " --------";
                aOres.put(separator, 1);
                prospectHole(i, k, aOres);
            }
        }
    }

    private void prospectHole(int i, int k, Map<String, Integer> aOres) {
        String tFoundOre;
        for (int j = this.getBaseMetaTileEntity()
            .getYCoord(); j > 0; j--) {
            tFoundOre = checkForOre(i, j, k);
            if (tFoundOre != null) countOre(aOres, tFoundOre, cX, cZ);
        }
    }

    private String checkForOre(int x, int y, int z) {
        Block tBlock = this.getBaseMetaTileEntity()
            .getBlock(x, y, z);

        if (tBlock instanceof BlockOresAbstract) {
            TileEntity tTileEntity = getBaseMetaTileEntity().getWorld()
                .getTileEntity(x, y, z);

            if ((tTileEntity instanceof TileEntityOres) && (((TileEntityOres) tTileEntity).mMetaData < 16000)) { // Filtering
                                                                                                                 // small
                                                                                                                 // ores
                Materials tMaterial = GregTechAPI.sGeneratedMaterials[((TileEntityOres) tTileEntity).mMetaData % 1000];

                if ((tMaterial != null) && (tMaterial != Materials._NULL)) return tMaterial.mDefaultLocalName;
            }
        } else {
            int tMetaID = getBaseMetaTileEntity().getWorld()
                .getBlockMetadata(x, y, z);
            ItemStack is = new ItemStack(tBlock, 1, tMetaID);
            ItemData association = GTOreDictUnificator.getAssociation(is);
            if ((association != null) && (association.mPrefix.toString()
                .startsWith("ore"))) return association.mMaterial.mMaterial.mDefaultLocalName;
            else if (GTUtility.isOre(tBlock, tMetaID)) return tBlock.getLocalizedName();
        }
        return null;
    }

    private static void countOre(Map<String, Integer> map, String ore, int cCX, int cCZ) {
        ore = (cCX + 8) + "," + (cCZ + 8) + " has " + ore;
        Integer oldCount = map.get(ore);
        oldCount = (oldCount == null) ? 0 : oldCount;

        map.put(ore, oldCount + 1);
    }
}
