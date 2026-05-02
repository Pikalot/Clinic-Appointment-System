package termproject.cas.model;

public class Provider extends User {
    private long licenseNumber;
    private String type;
    private String title;

    public Provider() {}

    public Provider(long licenseNumber, String type) {
        this.setLicenseNumber(licenseNumber);
        this.setType(type);
    }

    public Provider(
            long id,
            String firstName,
            String middleName,
            String lastName,
            String username,
            String email,
            long licenseNumber,
            String type,
            String title) {
        super(id, firstName, middleName, lastName, username, email);
        this.setLicenseNumber(licenseNumber);
        this.setType(type);
        this.setTitle(title);
    }

    public long getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(long licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
