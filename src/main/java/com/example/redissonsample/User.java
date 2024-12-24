package com.example.redissonsample;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private int id;
    private int remainingResources;

    public User(int id) {
        this.id = id;
        this.remainingResources = 10;
    }

    public int consumeResource(int consumeCount) {
        return this.remainingResources -= consumeCount;
    }
}
