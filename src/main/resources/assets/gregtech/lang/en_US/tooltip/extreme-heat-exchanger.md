Inputs are {color_hot:Lava}, {color_hot:Hot Coolant}, {color_hot:Hot Solar Salt}, or {color_hot:Plasma}
Outputs are {color_cold:Pahoehoe Lava}, {color_cold:IC2 Coolant}, {color_cold:Cold Solar Salt}, or {color_cold:Molten Metal}
Converts distilled water into {color_steam:SH Steam} or {color_steam:SC Steam} in the process
Outputs {color_steam:SC Steam} if the input rate of hot fluid is above a certain {color_mechanic:threshold}
{color_danger:Explodes immediately if it runs out of distilled water}
{hr}
{color_hot:Lava} | SC Threshold {color_mechanic:80,000 {L}/s} | Max Input {color_hot:160,000 {L}/s} | Max Output {color_steam:640,000 {L}/t SC Steam}
{color_hot:Hot Coolant} | SC Threshold {color_mechanic:8,000 {L}/s} | Max Input {color_hot:128,000 {L}/s} | Max Output {color_steam:1,280,000 {L}/t SC Steam}
{color_hot:Hot Solar Salt} | SC Threshold {color_mechanic:1,600 {L}/s} | Max Input {color_hot:3,200 {L}/s} | Max Output {color_steam:160,000 {L}/t SC Steam}
{hr}
{color_hot:Plasma} always outputs {color_steam:Dense SC Steam} regardless of input rate
The max input and output rates depend on the plasma's density ({EU}/{L})
{hr}
A circuit in the controller lowers the SC threshold at the cost of steam
{color_mechanic:-150 {L}/s} SC Threshold and {color_steam:-1.5%} steam output per circuit over 1
