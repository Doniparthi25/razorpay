package com.codingshuttle.razorpay.razorpay.operations.repository;

import com.codingshuttle.razorpay.razorpay.operations.entity.DlqEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DlqEventRepository extends JpaRepository<DlqEvent, UUID> {
}
