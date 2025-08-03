package com.project.model;

public record Building(
        Integer id,
        String name,
        String latitude,
        String longitude,
        Integer height,
        Integer area,
        Integer location_id) {
}
