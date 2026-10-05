燃烧！燃烧！燃烧！
使{yellow:助燃剂}与{gold:燃气}、{gold:柴油}或{gold:火箭燃料}反应发电，效率最高可达{aqua:150%}
输出功率没有软上限或硬上限，仅受{white:动力仓}限制
没有助燃剂时不会产生能量
若{white:动力仓}已满或输出超过动力仓的承载能力，多余的能量将会消失
{gray:{hr}}
效率取决于{yellow:助燃剂}与{gold:燃料}的比例(R)
助燃剂比例越高，效率越高
遵循指数曲线{aqua:exp(-C/R) * {var:efficiency_ceiling}}，其中{white:C}是与燃料类型有关的常数
{gold:燃气/柴油}：{white:C={var:gas_coefficient}} | {gold:火箭燃料}：{white:C={var:rocket_coefficient}}
{gray:{hr}}
每秒产生{dark_purple:sqrt(EU/t)}点污染
