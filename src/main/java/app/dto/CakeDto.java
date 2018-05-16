package app.dto;

import app.entity.Cake;
import app.entity.Status;

/**
 * Data transfer class for Cakes entity
 */
public class CakeDto {

    private Long id;

    private String name;

    private Status status;

    public CakeDto(Cake cake) {
        this.id = cake.getId();
        this.name = cake.getName();
        this.status = cake.getStatus();
    }

    public CakeDto() {
    }

    @Override
    public String toString() {
        return "{name : " + this.name + ",status : " + this.status + " }";
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
    public boolean equals(Object obj) {

        if (!(obj instanceof CakeDto))
            return false;

        else {
            CakeDto cake2 = (CakeDto) obj;

            return this.status.equals(cake2.status) &&
                    this.name.equals(cake2.name);

        }

    }
}
