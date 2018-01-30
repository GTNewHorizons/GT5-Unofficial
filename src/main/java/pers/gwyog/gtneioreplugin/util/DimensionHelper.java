package pers.gwyog.gtneioreplugin.util;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class DimensionHelper {
	
	public static String[] DimName =
		{
	"EndAsteroid",
    "GalacticraftCore_Moon",
    "GalacticraftMars_Asteroids",
    "GalacticraftMars_Mars",
    "GalaxySpace_BarnardC",
    "GalaxySpace_BarnardE",
    "GalaxySpace_BarnardF",
    "GalaxySpace_Callisto",
    "GalaxySpace_CentauriA",
    "GalaxySpace_Ceres",
    "GalaxySpace_Deimos",
    "GalaxySpace_Enceladus",
    "GalaxySpace_Europa",
    "GalaxySpace_Ganymede",
    "GalaxySpace_Haumea",
    "GalaxySpace_Io",
    "GalaxySpace_Kuiperbelt",
    "GalaxySpace_MakeMake",
    "GalaxySpace_Mercury",
    "GalaxySpace_Miranda",
    "GalaxySpace_Oberon",
    "GalaxySpace_Phobos",
    "GalaxySpace_Pluto",
    "GalaxySpace_Proteus",
    "GalaxySpace_TcetiE",
    "GalaxySpace_Titan",
    "GalaxySpace_Triton",
    "GalaxySpace_VegaB",
    "GalaxySpace_Venus",
    "Nether",
    "Overworld",
    "TheEnd",
    "Vanilla_EndAsteroids",
    "Twilight",
    "Underdark"};
	
	public static String[] DimNameDisplayed =
		{// first 2 letters if one word else 1 letter of every word, execpt capital letter in name, then 1rst + capital Moon = Mo, BarnardC = BC, EndAsteroid = EA
	"EA",
    "Mo",
    "As",
    "Ma",
    "BC",
    "BE",
    "BF",
    "Ca",
    "CA",
    "Ce",
    "De",
    "En",
    "Eu",
    "Ga",
    "Ha",
    "Io",
    "KB",
    "MM",
    "Me",
    "Mi",
    "Ob",
    "Ph",
    "Pl",
    "Pr",
    "TE",
    "Ti",
    "Tr",
    "VB",
    "Ve",
    "Ne",
    "Ow",
    "EN",//End = EN bc En = Encalus
    "VA",
    "TF",
    "DD"};
}