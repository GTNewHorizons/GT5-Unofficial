Produces {white:{var:steam_1_coal} {var:fluid_unit}} {white:Steam} per {blue:Coal} at {blue:{var:steam_per_sec} {var:fluid_unit}/s}
Has an added formula for long time burn fuels, {aqua:1 + log(BurnTime / 10) * 0.025}
A {white:Programmed Circuit} in the controller reduces output by {green:{var:throttle_amount}{var:fluid_unit}/s} per circuit number
Burning solid and liquid fuel at the same time gives a {green:{var:multi_fuel_boost}} speed boost
Takes {green:{var:heat_seconds}} seconds to heat up
{yellow:Fuels with a low burn value will not work}
