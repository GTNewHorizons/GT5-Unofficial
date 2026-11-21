通过电场加速粒子束，使其获得更高能量
因此电中性§5粒子§7不受影响
{lang:gtnhlanth.tt.beaminfo}
{gray:{hr}}
运行需要消耗§f长度 * 1,000 {var:fluidUnit}/s§7的§b冷却剂§7，并返回§c热冷却剂
{lang:gtnhlanth.tt.coolant.oxygen}
{lang:gtnhlanth.tt.coolant.nitrogen}
{lang:gtnhlanth.tt.coolant.coolant}
{lang:gtnhlanth.tt.coolant.Scoolant}
{gray:{hr}}
增加结构长度/能量输入会提升§9输出束流能量§7，但会降低§2输出束流聚焦
使用温度更低的§b冷却液§7能降低§2束流聚焦§7的损耗
{gray:{hr}}
§9输出束流能量§f = max(§eV§f， 50) * 10^§eIE
其中 §eV§f = 0.25 * (长度 - 1) * cbrt(§3输入电压§f)
且 §eIE§f = 1 + min(§9输入束流能量§f, 7500) / §9最大粒子能量
{gray:{hr}}
若 §2输入束流聚焦§f > §e机器聚焦度§f，则 §2输出束流聚焦§f = (§2输入束流聚焦§f + §e机器聚焦度§f) / 2
若 §2输入束流聚焦§f ≤ §e机器聚焦度§f，则 §2输出束流聚焦§f = §2输入束流聚焦§f * §e机器聚焦度§f / 100
其中§e机器聚焦度§f = min(90, max(5, (-0.9 * (长度-1) * 1.1^(0.2 * §b冷却剂温度§f) + 110)))
