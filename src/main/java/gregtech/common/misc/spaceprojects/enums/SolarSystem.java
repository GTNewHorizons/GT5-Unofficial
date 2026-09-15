package gregtech.common.misc.spaceprojects.enums;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.AsteroidBelt;
import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.DwarfPlanet;
import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.GasGiant;
import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.IceGiant;
import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.NaturalSatellite;
import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.Planet;
import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.Star;
import static gregtech.common.misc.spaceprojects.enums.StarType.GClass;
import static gregtech.common.misc.spaceprojects.enums.StarType.NotAStar;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;

/**
 * An enum of all space bodies in the Sol Solar System. Or to be exact the more important ones
 *
 * @author BlueWeabo
 */
public enum SolarSystem implements ISpaceBody {

    Sol(Star, GClass),
    Overworld(Planet),
    Moon(NaturalSatellite),
    Mars(Planet),
    Deimos(NaturalSatellite),
    Phobos(NaturalSatellite),
    Mercury(Planet),
    Venus(Planet),
    Jupiter(GasGiant),
    Io(NaturalSatellite),
    Ganymede(NaturalSatellite),
    Europa(NaturalSatellite),
    Callisto(NaturalSatellite),
    Saturn(GasGiant),
    Mimas(NaturalSatellite),
    Enceladus(NaturalSatellite),
    Tethys(NaturalSatellite),
    Rhea(NaturalSatellite),
    Titan(NaturalSatellite),
    Hyperion(NaturalSatellite),
    Iapetus(NaturalSatellite),
    Phoebe(NaturalSatellite),
    Uranus(IceGiant),
    Miranda(NaturalSatellite),
    Ariel(NaturalSatellite),
    Umbriel(NaturalSatellite),
    Titania(NaturalSatellite),
    Oberon(NaturalSatellite),
    Neptune(IceGiant),
    Proteus(NaturalSatellite),
    Triton(NaturalSatellite),
    Nereid(NaturalSatellite),
    Ceres(DwarfPlanet),
    Pluto(DwarfPlanet),
    Arrokoth(DwarfPlanet),
    MakeMake(DwarfPlanet),
    KuiperBelt(AsteroidBelt),
    NONE(SpaceBodyType.NONE);

    private final SpaceBodyType spaceBody;
    private final StarType star;
    private final UITexture texture;

    SolarSystem(SpaceBodyType aType) {
        this(aType, NotAStar);
    }

    SolarSystem(SpaceBodyType aType, StarType aStarType) {
        star = aStarType;
        spaceBody = aType;
        texture = UITexture.fullImage(GregTech.ID, "solarsystem/" + getName());
        SpaceProjectManager.addLocation(this);
    }

    @Override
    public StarType getStarType() {
        return star;
    }

    @Override
    public SpaceBodyType getType() {
        return spaceBody;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public UITexture getTexture() {
        return texture;
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.solar.system." + getName().toLowerCase();
    }
}
