Accelerates particle beams to higher energies by passing them through an electric field
Electrically neutral {color_mechanic:particles} are therefore unaffected
{lang:gtnhlanth.tt.beaminfo}
{hr}
Requires {white:Length * 1,000 {var:fluidUnit}/s} of {aqua:coolant} to run, returns as {red:hot coolant}
{lang:gtnhlanth.tt.coolant.oxygen}
{lang:gtnhlanth.tt.coolant.nitrogen}
{lang:gtnhlanth.tt.coolant.coolant}
{lang:gtnhlanth.tt.coolant.Scoolant}
{hr}
Increasing the length/power increases the {blue:Output Beam Energy} but decreases the {dark_green:Output Beam Focus}
Preserve {dark_green:Beam Focus} more effectively with lower temperature {aqua:coolant}
{hr}
{blue:Output Beam Energy}{color_formula: = max(}{color_note:V}{white:, 50) * 10^}{color_note:IE}
where {color_note:V}{color_formula: = 0.25 * (Length - 1) * cbrt({color_eu_usage:{EU/t} Provided})}
and {color_note:IE}{color_formula: = 1 + min({blue:Input Beam Energy}, 7500) / }{blue:Max Particle Energy}
{hr}
{dark_green:Output Beam Focus}{color_formula: = (dark_green:Input Beam Focus} + {color_note:Machine Focus})/2} if {dark_green:Input Beam Focus}{color_formula: > {color_note:Machine Focus}}
{dark_green:Output Beam Focus}{color_formula: = {dark_green:Input Beam Focus} * {color_note:Machine Focus}/100} if {dark_green:Input Beam Focus}{color_formula: <= {color_note:Machine Focus}}
where {color_note:Machine Focus}{color_formula: = min(90, max(5, (-0.9 * (Length-1) * 1.1^(0.2 * {aqua:Coolant Temperature}) + 110)))}
