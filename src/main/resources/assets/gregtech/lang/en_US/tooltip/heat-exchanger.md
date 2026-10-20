Hot fluids: {color_hot:{fluid:lava}}, {color_hot:{fluid:ic2hotcoolant}}, {color_hot:{fluid:molten.solarsalthot}}
Cooled into: {color_cold:{fluid:ic2pahoehoelava}}, {color_cold:{fluid:ic2coolant}}, {color_cold:{fluid:molten.solarsaltcold}}
Turns {color_name:Distilled Water} into {color_steam:Steam}
Makes half as much {color_steam:Superheated Steam} instead when the hot fluid input reaches the {color_note:SH threshold}
{color_danger:Explodes after {var:explode_seconds} seconds without distilled water}
{hr}
{color_hot:{fluid:lava}}: SH threshold {color_amount:{var:lava_threshold} {L}/s}, max input {color_amount:{var:lava_max_input} {L}/s}, max output {color_steam:{var:lava_max_output} {L}/t} SH Steam
{color_hot:{fluid:ic2hotcoolant}}: SH threshold {color_amount:{var:coolant_threshold} {L}/s}, max input {color_amount:{var:coolant_max_input} {L}/s}, max output {color_steam:{var:coolant_max_output} {L}/t} SH Steam
{color_hot:{fluid:molten.solarsalthot}}: SH threshold {color_amount:{var:salt_threshold} {L}/s}, max input {color_amount:{var:salt_max_input} {L}/s}, max output {color_steam:{var:salt_max_output} {L}/t} SH Steam
{hr}
A {color_name:Programmed Circuit} in the controller lowers the SH threshold by {color_amount:{var:threshold_reduction}%} and steam output by {color_loss:{var:steam_penalty}%} per value above 1
