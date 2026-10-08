package models;

public class Care {

    Pet pet;
    String description;
    float priceHour;
    String startHour;
    String finishHour;

    public Care (Pet pet,String description, float priceHour, String startHour, String finishHour){
        this.pet = pet;
        this.description = description;
        this.priceHour = priceHour;
        this.startHour = startHour;
        this.finishHour = finishHour;
    }

    public Pet getPet() {
        return pet;
    }

    @Override
    public String toString() {
        return "Care{" +
                ", pet=" + pet.toStringCare() +
                ", description='" + description + '\'' +
                ", Price per hour='" + priceHour+" €/h" + '\'' +
                ", startHour='" + startHour + '\'' +
                ", finishHour='" + finishHour + '\'' +
                '}';
    }
}