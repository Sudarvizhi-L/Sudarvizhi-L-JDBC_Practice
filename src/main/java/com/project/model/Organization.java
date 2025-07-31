package com.project.model;

public record Organization(
        Integer id,
        String name,
        String certificateid,
        String type,
        String email,
        String phone,
        String ceo
){}
