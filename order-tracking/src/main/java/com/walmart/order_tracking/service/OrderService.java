package com.walmart.order_tracking.service;

import com.walmart.order_tracking.dto.CreateOrderItemRequest;
import com.walmart.order_tracking.dto.CreateOrderRequest;
import com.walmart.order_tracking.dto.OrderResponse;
import com.walmart.order_tracking.entity.Customer;
import com.walmart.order_tracking.entity.Order;
import com.walmart.order_tracking.entity.OrderItem;
import com.walmart.order_tracking.entity.OrderStatus;
import com.walmart.order_tracking.repo.CustomerRepository;
import com.walmart.order_tracking.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/* What the service is responsible for

Orchestrates business logic for orders (not just DB calls).

Converts API DTOs → Entities (for saving) and Entities → DTOs (for responses).

Enforces simple rules/validations (e.g., qty ≥ 1).

Relies on JPA cascading so saving an Order also saves its OrderItems.

Runs inside transactions so either everything succeeds or nothing is saved.
 */
@Service
public class OrderService {

    private final CustomerRepository customers;
    private final OrderRepository orders;

 //   @Autowired - to use when we have multiple constructors, then annotate the one spring should use
    public OrderService(CustomerRepository customers, OrderRepository orders){
        this.customers = customers;
        this.orders = orders;
    }

    //@Transactional annotation is typically placed on methods within the service layer,
    // as this is where the core business logic and coordination of multiple repository operations reside.
    //WHY do we NOT put readOnly = true on create()?
    //because create() writes to DB (insert order + items)
    @Transactional
    public OrderResponse create(CreateOrderRequest req){
        // 1) validate request (guards)
        validateCreateRequest(req);

        // 2) load required data
        Customer customer = customers.findById(req.customerId())
                .orElseThrow(()-> new IllegalArgumentException("Customer not found :" + req.customerId()));

        // 3) compute domain values
        long total = computeTotal(req.items());

        // 4) build aggregate (entities)
        Order order = mapToOrder(customer, req, total);

        // 5) persist once (cascade handles children)
        Order saved = orders.save(order);

        // 6) map to response DTO
        return mapToResponse(saved);
    }

    private void validateCreateRequest(CreateOrderRequest req){
        if(req.customerId() == null) throw new IllegalArgumentException("customerId required");
        if(req.items() == null || req.items().isEmpty()) throw new IllegalArgumentException("items required");
        for (var it: req.items()){
            if(it.qty()<=0) throw new IllegalArgumentException("qty must be >=1");
            if (it.unitPriceCents() < 0) throw new IllegalArgumentException("unitPriceCents must be >= 0");
            if (it.sku() == null || it.sku().isBlank()) throw new IllegalArgumentException("sku required");
        }
    }

    private long computeTotal(List<CreateOrderItemRequest> items){
        long total = 0;
        for (var it : items) total += (long) it.qty() * it.unitPriceCents();
        return total;
    }

    //DTO->Entity(for saving) It converts the request DTO (the JSON input) into an Order entity
    // (the object that Hibernate will save to DB).
    private Order mapToOrder(Customer c, CreateOrderRequest req, long total){
        Order ord = new Order();
        ord.setCustomer(c);
        ord.setStatus(OrderStatus.PENDING);
        ord.setCurrency(req.currency() == null ? "USD" : req.currency());
        ArrayList<OrderItem> list = new ArrayList<>();
        for ( var it : req.items()){
            OrderItem oi = new OrderItem();
            oi.setOrder(ord);                    // owning side!
            oi.setSku(it.sku());
            oi.setName(it.name());
            oi.setQty(it.qty());
            oi.setUnitPriceCents(it.unitPriceCents());
            list.add(oi);
        }
        ord.setItems(list);
        ord.setTotalCents(total);
        return ord;
        }

    // takes Entity and builds DTO (for returning in JSON response
    private OrderResponse mapToResponse(Order saved) {
        var items = saved.getItems().stream()
                .map(i -> new OrderResponse.Item(i.getId(), i.getSku(), i.getName(), i.getQty(), i.getUnitPriceCents()))
                .toList();
        return new OrderResponse(
                saved.getId(), saved.getCustomer().getId(), saved.getStatus().name(),
                saved.getTotalCents(), saved.getCurrency(), saved.getCreatedAt(), saved.getUpdatedAt(), items
        );
    }

    //Read-only operations: For methods that only read data from the database and do not modify it,
    // using @Transactional(readOnly = true) can offer performance benefits by optimizing database interactions. However, if no transaction is strictly required, it can be omitted.
    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        var o = orders.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Order not found :" + id));
        var items = o.getItems().stream()
                .map(i -> new OrderResponse.Item(i.getId(), i.getSku(), i.getName(), i.getQty(), i.getUnitPriceCents()))
                .toList();

        return new OrderResponse(
                o.getId(),
                o.getCustomer().getId(),
                o.getStatus().name(),
                o.getTotalCents(),
                o.getCurrency(),
                o.getCreatedAt(),
                o.getUpdatedAt(),
                items
        );
    }

    /* Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setCurrency(req.currency()== null ? "USD" : req.currency());
        order.setItems(new ArrayList<>());

        long total = 0;
        for (var it : req.items()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setSku(it.sku());
            oi.setName(it.name());
            oi.setQty(it.qty());
            oi.setUnitPriceCents(it.unitPriceCents());
            order.getItems().add(oi);
            total += (long) it.qty() * it.unitPriceCents();
        }
        order.setTotalCents(total);

        Order saved = orders.save(order); // cascades items

        var items = saved.getItems().stream()
                .map(i -> new OrderResponse.Item(i.getId(), i.getSku(), i.getName(), i.getQty(), i.getUnitPriceCents()))
                .toList();

        return new OrderResponse(saved.getId(), saved.getCustomer().getId(), saved.getStatus().name(),
                saved.getTotalCents(), saved.getCurrency(), saved.getCreatedAt(), saved.getUpdatedAt(), items);
    }
    public OrderResponse get(Long id) {
        var o = orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        var items = o.getItems().stream()
                .map(i -> new OrderResponse.Item(i.getId(), i.getSku(), i.getName(), i.getQty(), i.getUnitPriceCents()))
                .toList();
        return new OrderResponse(o.getId(), o.getCustomer().getId(), o.getStatus().name(),
                o.getTotalCents(), o.getCurrency(), o.getCreatedAt(), o.getUpdatedAt(), items);
    }
*/
}
