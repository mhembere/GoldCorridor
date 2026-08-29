package com.golddust.goldcorridor.controller;

import com.golddust.goldcorridor.model.Order;
import com.golddust.goldcorridor.service.EscrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final EscrowService escrowService;

    public OrderController(EscrowService escrowService) {
        this.escrowService = escrowService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createCrossBorderOrder(@RequestBody Order orderRequest) {
        Order createdOrder = escrowService.createOrderInEscrow(orderRequest);
        return ResponseEntity.ok(createdOrder);
    }

    @PostMapping("/{orderId}/release")
    public ResponseEntity<Order> releaseFunds(
            @PathVariable Long orderId,
            @RequestParam String deliveryOtp) {
        Order updatedOrder = escrowService.releaseEscrowFunds(orderId, deliveryOtp);
        return ResponseEntity.ok(updatedOrder);
    }
}