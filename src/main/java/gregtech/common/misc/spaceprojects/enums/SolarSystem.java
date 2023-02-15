package gregtech.common.misc.spaceprojects.enums;

import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.*;
import static gregtech.common.misc.spaceprojects.enums.StarType.*;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;

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

    private SpaceBodyType spaceBody;
    private StarType star;

    SolarSystem(SpaceBodyType aType) {
        this(aType, NotAStar);
    }

    SolarSystem(SpaceBodyType aType, StarType aStarType) {
        star = aStarType;
        spaceBody = aType;
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
}
