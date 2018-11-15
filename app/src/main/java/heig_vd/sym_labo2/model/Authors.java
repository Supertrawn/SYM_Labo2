package heig_vd.sym_labo2.model;

public final class Authors {

    private int id;
    private String firstName, lastName;

    public Authors(int id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    public String display(){
        return String.format("Deserialization :\n%d) %s %s", this.id, this.firstName, this.lastName);
    }

}



