package com.project.model;

import java.time.LocalDateTime;

public record TemperatureData(
        Integer temperature,
        LocalDateTime date_time,
        Integer sensor_id
) {
}
