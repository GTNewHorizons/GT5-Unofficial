Для работы нужна {color_name:Турбина} в контроллере
Очищает загрязнение в окрестных чанках {color_range:{var:chunk_size}x{var:chunk_size}}
Во время работы потребляет {color_voltage:{var:eu_usage}} {color_tier:{EU/t}}
{hr}
Каждая {color_name:Выхлопная система} убирает из одного чанка в секунду следующее количество загрязнения
{color_amount:{var:global_multiplier} * B * turbineEff * FLOOR({var:scaling_factor}^mufflerTier)}
- Бонус тира этого механизма (B): {color_amount:{var:tier_bonus}}
- turbineEff: эффективность {color_name:Турбины}
- Тир {color_name:Выхлопной системы} ограничен тиром {color_name:Энерговвода}
{hr}
Подавайте {color_key:Поглощающие фильтры} через {color_name:Входной люк}, чтобы умножить очистку на {color_amount:{var:boost_per_filter}x}
Каждый фильтр работает {color_time:{var:filter_lifespan}} с
Каждая проблема с техобслуживанием снижает очистку на {color_amount:{var:maint_penalty}%}
