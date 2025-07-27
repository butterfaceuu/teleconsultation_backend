package com.teleconsultation_backend.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;
    
    @OneToMany(mappedBy = "specialtyEntity", cascade = CascadeType.ALL)
    private List<Practitioner> practitioners;
    
    private boolean active = true;
    
    // Constructors
    public Specialty() {}
    
    public Specialty(String name, String description, Domain domain) {
        this.name = name;
        this.description = description;
        this.domain = domain;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Domain getDomain() { return domain; }
    public void setDomain(Domain domain) { this.domain = domain; }
    
    public List<Practitioner> getPractitioners() { return practitioners; }
    public void setPractitioners(List<Practitioner> practitioners) { this.practitioners = practitioners; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
