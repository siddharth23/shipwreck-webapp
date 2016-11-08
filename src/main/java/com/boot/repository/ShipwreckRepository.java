package com.boot.repository;

import com.boot.model.Shipwreck;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by upgundecha on 02/04/16.
 */
public interface ShipwreckRepository extends JpaRepository<Shipwreck, Long> {
}
