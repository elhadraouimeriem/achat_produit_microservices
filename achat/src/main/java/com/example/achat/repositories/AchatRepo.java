package com.example.achat.repositories;

import com.example.achat.entities.Achat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchatRepo extends JpaRepository<Achat, Long> {

}
