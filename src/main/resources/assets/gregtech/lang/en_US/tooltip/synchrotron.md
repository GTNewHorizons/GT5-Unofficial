Accelerates particle beams until they produce a photon beam from the relativistic Larmor effect
Electrically neutral {dark_purple:particles} and beams with less than 25 {dark_green:Focus} are therefore unaffected
{lang:gtnhlanth.tt.beaminfo}
{hr}
Requires {color_amount:32,000 {var:fluidUnit}/s} of {color_cold:coolant} to run, returns as {color_hot:hot coolant}
{lang:gtnhlanth.tt.coolant.oxygen}
{lang:gtnhlanth.tt.coolant.nitrogen}
{lang:gtnhlanth.tt.coolant.coolant}
{lang:gtnhlanth.tt.coolant.Scoolant}
{hr}
Increasing power increases the §6Output Beam Rate
Preserve {dark_green:Beam Focus} more effectively with lower temperature §bcoolant
Increasing the tier of the {light_purple:Antenna Casings} improves the efficiency of the machine
The {blue:energies} in the following formulae must all be in keV
{hr}
{blue:Output Beam Energy}{color_formula: = {color_note:IR} * {color_note:Power Scale Factor}}
where {color_note:IR}{color_formula: = ({blue:Input Beam Energy})^(1.13 * ({light_purple:Antenna Tier})^(4/9)) / 40,000,000}
and {color_note:Power Scale Factor}{color_formula: = 1 - 0.15^({color_eu_usage:Total {EU/t} Provided} / (30,384 * ({light_purple:Antenna Tier})^2.5))}
{hr}
{dark_green:Output Beam Focus}{color_formula: = ({dark_green:Input Beam Focus} + {color_note:Machine Focus})/2.5} if {dark_green:Input Beam Focus} > {color_note:Machine Focus}
{dark_green:Output Beam Focus}{color_formula: = {dark_green:Input Beam Focus} * {color_note:Machine Focus}/100} if {dark_green:Input Beam Focus} <= {color_note:Machine Focus}
where {color_note:Machine Focus}{color_formula: = max(10, min(90, 1.5^(12 - {color_cold:Coolant Temperature}/40)))}
{hr}
{gold:Output Beam Rate}{color_formula: = floor(2.5^({light_purple:Antenna Tier}) * sqrt({color_eu_usage:Total {EU/t} Provided}) * {gold:Input Beam Rate}/15,000)}
