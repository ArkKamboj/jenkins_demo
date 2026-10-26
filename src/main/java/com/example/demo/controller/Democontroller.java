package com.example.demo.controller;
 
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/api/demo")
public class Democontroller {
 
    public record Item(Long id, String name, String description) {}
 
    private final Map<Long, Item> items = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();
 
    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }
 
    @GetMapping("/items")
    public Collection<Item> getAllItems() {
        return items.values();
    }
 
    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        Item item = items.get(id);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }
 
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@RequestBody Item request) {
        Long id = idCounter.incrementAndGet();
        Item item = new Item(id, request.name(), request.description());
        items.put(id, item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }
 
    @PutMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item request) {
        if (!items.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        Item updated = new Item(id, request.name(), request.description());
        items.put(id, updated);
        return ResponseEntity.ok(updated);
    }
 
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        return items.remove(id) != null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}