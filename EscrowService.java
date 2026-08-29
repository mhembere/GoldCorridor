public class EscrowService {
    
}
package com.golddust.goldcorridor.service;

import com.golddust.goldcorridor.model.Order;
import com.golddust.goldcorridor.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EscrowService {

    private final OrderRepository orderRepository;

    public EscrowService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrderInEscrow(Order order) {
        order.setEscrowState("HELD_IN_ESCROW");
        return orderRepository.save(order);
    }

    @Transactional
    public Order releaseEscrowFunds(Long orderId, String deliveryOtp) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Order ID: " + orderId));

        if ("HELD_IN_ESCROW".equals(order.getEscrowState())) {
            // Verify driver/recipient OTP before releasing funds
            order.setEscrowState("RELEASED_TO_SELLER");
            return orderRepository.save(order);
        }

        throw new IllegalStateException("Funds are not currently in escrow.");
    }
}