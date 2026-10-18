Для работы нужна {white:Турбина} в контроллере
Очищает загрязнение в окрестных чанках {green:{var:chunk_size}x{var:chunk_size}}
Во время работы потребляет фиксированные {aqua:{var:eu_usage} EU/t}
{gray:{hr}}
Каждая {white:Выхлопная система} убирает из одного чанка в секунду следующее количество загрязнения
{green:{var:global_multiplier} * B * turbineEff * FLOOR({var:scaling_factor}^mufflerTier)}
- Бонус тира этого механизма (B) = {green:{var:tier_bonus}}
- turbineEff = эффективность {white:Турбины}
- Эффективный тир {white:Выхлопной системы} ограничен тиром {white:Энерговвода}
{gray:{hr}}
Подавайте {gold:Поглощающие фильтры} через {white:Входной люк}, чтобы удвоить очистку ({gold:{var:filter_lifespan}} с на фильтр)
Каждая проблема с техобслуживанием снижает очистку на {green:{var:maint_penalty}%}
