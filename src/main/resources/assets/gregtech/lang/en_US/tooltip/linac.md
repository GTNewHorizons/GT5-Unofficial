Accelerates particle beams to higher energies by passing them through an electric field
Electrically neutral §5particles§7 are therefore unaffected
{lang:gtnhlanth.tt.beaminfo}
{gray:{hr}}
Requires §fLength * 1,000 {var:fluidUnit}/s§7 of §bcoolant§7 to run, returns as §chot coolant
{lang:gtnhlanth.tt.coolant.oxygen}
{lang:gtnhlanth.tt.coolant.nitrogen}
{lang:gtnhlanth.tt.coolant.coolant}
{lang:gtnhlanth.tt.coolant.Scoolant}
{gray:{hr}}
Increasing the length/power increases the §9Output Beam Energy§7 but decreases the §2Output Beam Focus
Preserve §2Beam Focus§7 more effectively with lower temperature §bcoolant
{gray:{hr}}
§9Output Beam Energy§f = max(§eV§f, 50) * 10^§eIE
where §eV§f = 0.25 * (Length - 1) * cbrt(§3EU/t Provided§f)
and §eIE§f = 1 + min(§9Input Beam Energy§f, 7500) / §9Max Particle Energy
{gray:{hr}}
§2Output Beam Focus§f = (§2Input Beam Focus§f + §eMachine Focus§f)/2§7 if §2Input Beam Focus§f > §eMachine Focus
§2Output Beam Focus§f = §2Input Beam Focus§f * §eMachine Focus§f/100§7 if §2Input Beam Focus§f <= §eMachine Focus
where §eMachine Focus§f = min(90, max(5, (-0.9 * (Length-1) * 1.1^(0.2 * §bCoolant Temperature§f) + 110)))
