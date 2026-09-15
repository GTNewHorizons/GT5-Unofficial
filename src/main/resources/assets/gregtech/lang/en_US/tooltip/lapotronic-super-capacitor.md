Loses energy equal to 1% of the total capacity every 24 hours
Capped at {red:{var:maxPassiveDrain}} EU/t passive loss per {var:tierColorName}§7 capacitor
The passive loss increases {dark_red:100}-fold for every capacitor tier above
Passive loss is multiplied by the number of maintenance issues present
{gray:{hr}}
Glass shell has to be Tier - 3 of the highest capacitor tier
Add more or better capacitors to increase capacity
{gray:{hr}}
Wireless mode can be enabled by right clicking with a screwdriver
This mode can only be enabled if you have a {var:tierColorName}§7+ capacitor in the multiblock.
When enabled every {blue:{var:rebalanceTicks}} ticks the LSC will attempt to re-balance against your
wireless EU network.
If there is less than {red:{var:wirelessEuCap}}({var:tierColorName}§7) EU in the LSC
it will withdraw from the network and add to the LSC.
If there is more it will add {dark_red:{bold:{underline:all excess}}} EU to the network, removing it from the LSC
This can potentially brick your base, be careful
The threshold increases {dark_red:100}-fold for every capacitor tier above
