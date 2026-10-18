Inputs are {red:Lava}, {red:Hot Coolant}, {red:Hot Solar Salt}, or {red:Plasma}
Outputs are {blue:Pahoehoe Lava}, {blue:IC2 Coolant}, {blue:Cold Solar Salt}, or {blue:Molten Metal}
Converts distilled water into {white:SH Steam} or {white:SC Steam} in the process
Outputs {white:SC Steam} if the input rate of hot fluid is above a certain {color_mechanic:threshold}
Explodes immediately if it runs out of distilled water
{hr}
{red:Lava} | SC Threshold {color_mechanic:80,000 {var:unit}/s} | Max Input {red:160,000 {var:unit}/s} | Max Output {white:640,000 {var:unit}/t SC Steam}
{red:Hot Coolant} | SC Threshold {color_mechanic:8,000 {var:unit}/s} | Max Input {red:128,000 {var:unit}/s} | Max Output {white:1,280,000 {var:unit}/t SC Steam}
{red:Hot Solar Salt} | SC Threshold {color_mechanic:1,600 {var:unit}/s} | Max Input {red:3,200 {var:unit}/s} | Max Output {white:160,000 {var:unit}/t SC Steam}
{hr}
{red:Plasma} always outputs {white:Dense SC Steam} regardless of input rate
The max input and output rates depend on the plasma's density (EU/L)
{hr}
A circuit in the controller lowers the SC threshold at the cost of steam
{color_mechanic:-150 {var:unit}/s} SC Threshold and {white:-1.5%} steam output per circuit over 1
