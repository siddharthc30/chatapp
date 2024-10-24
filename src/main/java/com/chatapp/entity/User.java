package com.chatapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    private String username;
    private String preferredLanguage;  // e.g., "en", "es", "fr"
}