package com.codingshuttle.razorpay.razorpay.payment.repository;

import com.codingshuttle.razorpay.razorpay.common.enums.OutboxStatus;
import com.codingshuttle.razorpay.razorpay.payment.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent,UUID> {

    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
