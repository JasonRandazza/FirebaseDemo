package aydin.firebasedemo;

public class Person {
    private String name;
    private int age;
    private String phoneNumber;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    //Name getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Age getter and setter
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //PhoneNumber getter and setter
    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

}
