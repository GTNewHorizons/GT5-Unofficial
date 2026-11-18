Equivalent to 32 Large Heat Exchangers
Inputs are {color_hot:Lava}, {color_hot:Hot Coolant}, or {color_hot:Hot Solar Salt}
Outputs are {color_cold:Pahoehoe Lava}, {color_cold:IC2 Coolant}, or {color_cold:Cold Solar Salt}
Converts distilled water into {color_steam:Steam} or {color_steam:SH Steam} in the process
Outputs {color_steam:SH Steam} if the input rate of hot fluid is above a certain {light_purple:threshold}
Explodes immediately if it runs out of distilled water
{hr}
{color_hot:Lava} | SH Threshold {light_purple:{var:lava_threshold} {L}/s} | Max Input {color_hot:{var:lava_max_input} {L}/s} | Max Output {color_steam:{var:lava_max_output} {L}/t SH Steam}
{color_hot:Hot Coolant} | SH Threshold {light_purple:{var:coolant_threshold} {L}/s} | Max Input {color_hot:{var:coolant_max_input} {L}/s} | Max Output {color_steam:{var:coolant_max_output} {L}/t SH Steam}
{color_hot:Hot Solar Salt} | SH Threshold {light_purple:{var:salt_threshold} {L}/s} | Max Input {color_hot:{var:salt_max_input} {L}/s} | Max Output {color_steam:{var:salt_max_output} {L}/t SH Steam}
{hr}
A circuit in the controller lowers the SH threshold at the cost of steam
{light_purple:-{var:threshold_reduction}%} SH Threshold and {color_loss:-{var:steam_penalty}%} steam output per circuit over {var:min_circuit}
