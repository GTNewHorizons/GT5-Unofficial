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

    private SpaceBodyType mType;
    private StarType mStarType;

    SolarSystem(SpaceBodyType aType) {
        this(aType, NotAStar);
    }

    SolarSystem(SpaceBodyType aType, StarType aStarType) {
        mStarType = aStarType;
        mType = aType;
        SpaceProjectManager.addLocation(this);
    }

    @Override
    public StarType getStarType() {
        return mStarType;
    }

    @Override
    public SpaceBodyType getType() {
        return mType;
    }

    @Override
    public String getName() {
        return name();
    }
}
