package models;

import servidor.ExternalRIAC;
import java.security.InvalidParameterException;
import java.util.Objects;

// This class represents a pet in the system
public class Pet {
    String name;
    String description;
    String address;
    Owner owner;
    String RIAC;
    String securePolicy;
    int id;

    public Pet(){
    }
    public Pet(String name, String description, String address, String RIAC, String securePolicy, int id) {
            if (Objects.equals(name, "")) {
                throw new InvalidParameterException();
            }else this.name = name;
            this.description = description;
            if (Objects.equals(address, "")) {
                throw new InvalidParameterException();
            } else this.address = address;
            if (checkRiac(RIAC)==null) {
                throw new InvalidParameterException();
            }else this.RIAC = RIAC;
            this.securePolicy=securePolicy;
            this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {return address;}

    public Owner getOwner() {
        return owner;
    }

    public String getRIAC() {
        return RIAC;
    }

    public String getSecurePolicy() {
        return securePolicy;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address){this.address=address;}

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setRIAC(String RIAC) {
        this.RIAC = RIAC;
    }

    public void setSecurePolicy(String securePolicy) {
        this.securePolicy = securePolicy;
    }

    public boolean isExotic(){
        return false;
    }

    public String checkRiac(String RIAC){
        if (ExternalRIAC.RIAC(RIAC)){
            return RIAC;
        }else return null;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", owner=" + owner.username +
                ", RIAC='" + RIAC + '\'' +
                ", securePolicy='" + securePolicy + '\'' +
                '}';
    }

    public String toStringCare() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", owner=" + owner.username +
                '}';
    }
}
