package termproject.cas.model;

public class Provider extends User {
    private long licenseNumber;
    private String type;

    public Provider() {}

    public Provider(long licenseNumber, String type) {
        this.setLicenseNumber(licenseNumber);
        this.setType(type);
    }

    public Provider(
            long id,
            String firstName,
            String lastName,
            String username,
            String email,
            boolean isAdmin,
            long licenseNumber,
            String type) {
        super(id, firstName, lastName, username, email, isAdmin);
        this.setLicenseNumber(licenseNumber);
        this.setType(type);
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
}
