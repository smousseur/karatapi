package com.smousseur.karatapi.example.api.repository;

import com.smousseur.karatapi.example.api.model.entity.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
  List<Order> findByStatus(String status);

  @Modifying
  @Query("update Order set status = :status where uuid = :uuid")
  void updateStatusByUuid(String uuid, String status);

  Optional<Order> findByUuid(String uuid);
}
