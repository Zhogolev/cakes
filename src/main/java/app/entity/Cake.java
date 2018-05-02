package app.entity;

import app.dto.CakeDto;

import javax.persistence.*;

@Entity(name = "Cake")
@Table(name = "cakes")
public class Cake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 500)
    private String name;

    @Column(name = "status", length = 10)
    private Status status;

    private Cake(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public Cake() {
    }

    public Cake(CakeDto item) {
       this.name = item.getName().isEmpty() ? "UNNAMED" : item.getName() ;
       this.status = item.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{ name : " + this.name + ", status : " + this.status + "}";
    }
}
