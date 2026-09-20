Contributing Green Energy towards the future
Surround with rings of {gold:Solar Reflectors}, which determine heat and tier
{yellow:The first ring is required for the Tower to work}
{gray:{hr}}
Converts {gold:Cold Salt} into {gold:Hot Salt}
Every {var:cycle}s heat rises, {gold:Cold Salt} converts to {gold:Hot Salt} at 1:1
{yellow:Heat gain is halved in rain and disabled at night}
Conversion only happens if heat >= {red:{var:threshold}} and efficiency is 100%
Excess {gold:Cold Salt} drains all heat to 0
{gray:{hr}}
Heat efficiency: {aqua:1 - (heat - {var:center})^{var:exp} / {var:coefficient}}, peaks at {aqua:{var:center}}
Heat/cycle: {aqua:heaters * efficiency * ({var:loss} + bonus) - {var:loss}}
Rings 1-5: {gold:+1/+2/+4/+8/+16} bonus, {gold:36/88/156/240/340} reflectors
