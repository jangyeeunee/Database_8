public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String phoneNumber;
    private String birth;

    public User(String id, String name, String email, String password, String gender, String phoneNumber, String birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getBirth() { return birth; }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
