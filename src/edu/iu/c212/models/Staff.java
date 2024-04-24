package edu.iu.c212.models;

public class Staff {
    private String fullName;
    private int age;
    private String role;    //full word like 'Manager' instead of M;
    private String availability;
    public Staff(String name, int age, String role, String av){
        this.fullName = name;
        this.age = age;
        this.role = role;
        /*switch (role){
            case "M":
                this.role="Manager";
                break;
            case "C":
                this.role="Cashier";
                break;
            case "G":
                this.role="Gardening Expert";
                break;
            default:
                throw new RuntimeException("ERROR: Invalid Role. Please try again with M | C | G.");
        }*/
        this.availability = (av!=null ? av : "");
    }
    public String getName(){ return this.fullName;}
    public int getAge(){ return this.age;}

    public String getRole(){return this.role;}

    public String getAvailability(){return this.availability;}
    public void setRole(String role){this.role = role;}

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.fullName).append(" ").append(this.age).append(" ").append(this.role);
        if(this.availability!=""){
            sb.append(" ").append(this.availability);
        }
        return sb.toString();
    }
}
