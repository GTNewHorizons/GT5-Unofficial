package gregtech.common.tileentities.machines.basic;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

import static gregtech.common.GT_UndergroundOil.undergroundOilReadInformation;


public class GT_MetaTileEntity_AdvSeismicProspector extends GT_MetaTileEntity_BasicMachine {
    boolean ready = false;
    int radius;
    int near;
    int middle;
    int step;
    int cX;
    int cZ;

    public GT_MetaTileEntity_AdvSeismicProspector(int aID, String aName, String aNameRegional, int aTier, int aRadius, int aStep) {
        super(aID, aName, aNameRegional, aTier, 1, // amperage
                "",
                1, // input slot count
                1, // output slot count
                "Default.png", // GUI name
                "", // NEI name
                new ITexture[] { new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER_ACTIVE),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_ACTIVE),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE),
                        new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER) });
        radius = aRadius;
        near = radius / 3;
        near = near + near % 2; // making near value even;
        middle = near * 2;
        step = aStep;
    }

    public String[] getDescription() {
        return new String[]{
        		"Place, activate with explosives",
                 "2 Powderbarrels, "
                     + "4 Glyceryl Trinitrate, "
                     + "16 TNT, or "
                     + "8 ITNT", 
                "Use Data Stick, Scan Data Stick, Print Data Stick, Bind Pages into Book",
                "Ore prospecting area = " 
                    + radius*2
                    + "x"
                    + radius*2
                    + " ONLY blocks below prospector",
                "Near < " + near,
                "Middle < " + middle,
                "Far >= " + middle,
                "Oil prospecting area 3x3 oilfields"};
    }

    protected GT_MetaTileEntity_AdvSeismicProspector(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
            String aGUIName, String aNEIName, int aNear, int aMiddle, int aRadius, int aStep) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
        radius = aRadius;
        near = aNear;
        middle = aMiddle;
        step = aStep;
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_AdvSeismicProspector(this.mName, this.mTier, this.mDescriptionArray, this.mTextures,
                this.mGUIName, this.mNEIName, this.near, this.middle, this.radius, this.step);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isServerSide()) {
            ItemStack aStack = aPlayer.getCurrentEquippedItem();

            if (!ready && (GT_Utility.consumeItems(aPlayer, aStack, Item.getItemFromBlock(Blocks.tnt), 16)
                    || GT_Utility.consumeItems(aPlayer, aStack, Ic2Items.industrialTnt.getItem(), 8)
                    || GT_Utility.consumeItems(aPlayer, aStack, Materials.Glyceryl, 4)
                    || GT_Utility.consumeItems(aPlayer, aStack, ItemList.Block_Powderbarrel.getItem(), 2) )) {

                this.ready = true;
                this.mMaxProgresstime = (aPlayer.capabilities.isCreativeMode ? 20 : 800);

            } else if (ready && mMaxProgresstime == 0
                    && aStack != null && aStack.stackSize == 1
                    && aStack.getItem() == ItemList.Tool_DataStick.getItem()) {
                this.ready = false;

                // prospecting ores
                HashMap<String, Integer> tNearOres = new HashMap<String, Integer>();
                HashMap<String, Integer> tMiddleOres = new HashMap<String, Integer>();
                HashMap<String, Integer> tFarOres = new HashMap<String, Integer>();
                prospectOres(tNearOres, tMiddleOres, tFarOres);

                // prospecting oils
                ArrayList<String> tOils = new ArrayList<String>();
                prospectOils(tOils);

                GT_Utility.ItemNBT.setAdvancedProspectionData(mTier,
                    aStack,
                    this.getBaseMetaTileEntity().getXCoord(),
                    this.getBaseMetaTileEntity().getYCoord(),
                    this.getBaseMetaTileEntity().getZCoord(),
                    this.getBaseMetaTileEntity().getWorld().provider.dimensionId,
                    tOils,
                    GT_Utility.sortByValueToList(tNearOres),
                    GT_Utility.sortByValueToList(tMiddleOres),
                    GT_Utility.sortByValueToList(tFarOres),
                    near, middle, radius);
            }
        }

        return true;
    }

    private void prospectOils(ArrayList<String> aOils) {

        FluidStack tFluid;

        Chunk tChunk = getBaseMetaTileEntity().getWorld().getChunkFromBlockCoords(getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getZCoord());
        int oilfieldSize = 8;
        int xChunk = (tChunk.xPosition / oilfieldSize) * oilfieldSize - ((tChunk.xPosition < 0 && tChunk.xPosition % oilfieldSize != 0) ? oilfieldSize : 0);
        int zChunk = (tChunk.zPosition / oilfieldSize) * oilfieldSize - ((tChunk.zPosition < 0 && tChunk.zPosition % oilfieldSize != 0) ? oilfieldSize : 0);

        LinkedHashMap<ChunkCoordIntPair, FluidStack> tFluids = new LinkedHashMap<>();
        int oilFieldCount = 0;

        try {
            for (int z = -1; z <= 1; ++z) {
                for (int x = -1; x <= 1; ++x) {
                    ChunkCoordIntPair cInts = getBaseMetaTileEntity().getWorld().getChunkFromChunkCoords(x, z).getChunkCoordIntPair();
                    ArrayList<Integer> minMaxValue = new ArrayList<>();

                    for (int i = 0; i < oilfieldSize; i++) {
                        for (int j = 0; j < oilfieldSize; j++) {
                            tChunk = getBaseMetaTileEntity().getWorld().getChunkFromChunkCoords(
                                    xChunk + i + x * oilfieldSize,
                                    zChunk + j + z * oilfieldSize);
                            tFluid = undergroundOilReadInformation(tChunk);
                            if (tFluid != null) {
                                minMaxValue.add(tFluid.amount);
                                if (!tFluids.containsKey(cInts)) {
                                    tFluids.put(cInts, tFluid);
                                }
                            }
                        }
                    }

                    int min = Collections.min(minMaxValue);
                    int max = Collections.max(minMaxValue);
                    aOils.add(++oilFieldCount + "," + min + "-" + max + "," + tFluids.get(cInts).getLocalizedName());
                }
            }
        } catch (Exception e) {/*Do nothing*/}
    }

    //private void putOil(int x, int z, HashMap<String, Integer> aOils) {//TODO Old method??
    //    FluidStack tFluid = GT_Utility.undergroundOil(getBaseMetaTileEntity().getWorld(),x,z,false,0);
    //    if (tFluid.amount / 5000 > 0)
    //        aOils.put(x + "," + z + "," + (tFluid.amount / 5000) + "," + tFluid.getLocalizedName(), tFluid.amount / 5000);
    //}

    private void prospectOres(Map<String, Integer> aNearOres, Map<String, Integer> aMiddleOres, Map<String, Integer> aFarOres) {
        int tLeftXBound = this.getBaseMetaTileEntity().getXCoord() - radius;
        int tRightXBound = tLeftXBound + 2*radius;

        int tLeftZBound = this.getBaseMetaTileEntity().getZCoord() - radius;
        int tRightZBound = tLeftZBound + 2*radius;

        for (int i = tLeftXBound; i <= tRightXBound; i += step)
            for (int k = tLeftZBound; k <= tRightZBound; k += step) {
                int di = Math.abs(i - this.getBaseMetaTileEntity().getXCoord());
                int dk = Math.abs(k - this.getBaseMetaTileEntity().getZCoord());

                cX = (i/16)*16;
                cZ = (k/16)*16;

                if (di <= near && dk <= near)
                    prospectHole(i, k, aNearOres);
                else if (di <= middle && dk <= middle)
                    prospectHole(i, k, aMiddleOres);
                else
                    prospectHole(i, k, aFarOres);
            }
    }

    private void prospectHole(int i, int k, Map<String, Integer> aOres) {
        String tFoundOre;
        for (int j = this.getBaseMetaTileEntity().getYCoord(); j > 0; j--) {
            tFoundOre = checkForOre(i, j, k);
            if (tFoundOre != null)
                countOre(aOres, tFoundOre, cX, cZ);
        }
    }

    private String checkForOre(int x, int y, int z) {
        Block tBlock = this.getBaseMetaTileEntity().getBlock(x, y, z);

        if (tBlock instanceof GT_Block_Ores_Abstract) {
            TileEntity tTileEntity = getBaseMetaTileEntity().getWorld().getTileEntity(x, y, z);

            if ((tTileEntity instanceof GT_TileEntity_Ores)
                && (((GT_TileEntity_Ores) tTileEntity).mMetaData < 16000)) { // Filtering small ores
                Materials tMaterial
                    = GregTech_API.sGeneratedMaterials[((GT_TileEntity_Ores) tTileEntity).mMetaData % 1000];

                if ((tMaterial != null) && (tMaterial != Materials._NULL))
                    return tMaterial.mDefaultLocalName;
            }
        } else {
            int tMetaID = getBaseMetaTileEntity().getWorld().getBlockMetadata(x, y, z);
            ItemData tAssotiation = GT_OreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));

            if ((tAssotiation != null) && (tAssotiation.mPrefix.toString().startsWith("ore")))
                return tAssotiation.mMaterial.mMaterial.mDefaultLocalName;
        }

        return null;
    }

    private static void countOre(Map<String, Integer> map, String ore, int cCX, int cCZ) {
        ore = ore + " at " + (cCX +8)+ "," + (cCZ + 8);
        Integer oldCount = map.get(ore);
        oldCount = (oldCount == null) ? 0 : oldCount;

        map.put(ore, oldCount + 1);
    }
}
