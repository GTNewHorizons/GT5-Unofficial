Burns certain Plasma to generate power
{hr}
Set the {color_name:Plasma Drain Rate} in the Controller, up to each Plasma's maximum, drained at the start of each {color_amount:Cycle}
Set the {color_name:Scale Factor} to proportionally increase {color_amount:Cycle Time} and {color_amount:Drain Amount}
Each Plasma has a unique {color_voltage:Density ({EU}/{L})} and provides {color_name:Buffs} to all burned Plasma
{hr}
{gold:Force}, {blue:Runite}, and {dark_purple:Celestial Tungsten} Buffs scale with {color_amount:Drain Rate}: {color_mechanic:Effective Buff =} {white:Base Buff * (Current / Max)}
{color_amount:{var:force_max_dr} {L}/s} of {gold:Force} : {color_voltage:{var:force_density}} : {color_mechanic:+{var:force_max_boost}%} {EU}/{L}
{color_amount:{var:runite_max_dr} {L}/s} of {blue:Runite} : {color_voltage:{var:runite_density}} : {color_mechanic:{var:runite_max_boost}%} chance to not consume Plasma
{color_amount:{var:celestial_max_dr} {L}/s} of {dark_purple:Celestial Tungsten} : {color_voltage:{var:celestial_density}} : {color_mechanic:+{var:celestial_max_boost}%} {EU}/{L}, {color_mechanic:+{var:celestial_max_boost}%} chance to not consume Plasma
{color_amount:{var:orikalkum_max_dr} {L}/s} of {dark_red:Orikalkum} : {color_voltage:{var:orikalkum_density}} : {color_mechanic:Buff Multiplier =} {white:max(1, {var:orikalkum_max_multiplier} * Current / Max)}
{hr}
If {gold:Force}, {blue:Runite}, and {dark_purple:Celestial Tungsten} Plasmas are supplied:
Periodically outputs {dark_aqua:Collision Residue} at a rate of {var:residue_output_amount}{L} per {var:residue_divisor}{L} of Plasma burned
{color_note:Consumed Plasmas are not returned in molten form}
{hr}
