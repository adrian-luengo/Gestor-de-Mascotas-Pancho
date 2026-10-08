package models;

import java.awt.*;
import java.time.LocalDateTime;

public class ExoticPet extends Pet{
    String legalProcedure;
    String healthCertificate;


    public ExoticPet() {}

    public ExoticPet(String name, String description, String address, String RIAC, String securePolicy, int id, String legalProcedure, String healthCertificate){
        super(name,description,address,RIAC,securePolicy, id);
        this.legalProcedure=legalProcedure;
        this.healthCertificate=healthCertificate;
    }

    public String getLegalProcedure() {
        return legalProcedure;
    }

    public String getHealthCertificate() {
        return healthCertificate;
    }

    public void setLegalProcedure(String legalProcedure) {
        this.legalProcedure = legalProcedure;
    }

    public void setHealthCertificate(String healthCertificate) {
        this.healthCertificate = healthCertificate;
    }

    public boolean isExotic(){
        return true;
    }
    @Override
    public String toString() {
        return "ExoticPet{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", owner=" + owner.username +
                ", RIAC='" + RIAC + '\'' +
                ", securePolicy='" + securePolicy + '\'' +
                "legalProcedure='" + legalProcedure + '\'' +
                ", healthCertificate='" + healthCertificate + '\'' +
                '}';
    }
}
