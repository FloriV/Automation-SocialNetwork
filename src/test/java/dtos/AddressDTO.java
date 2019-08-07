package dtos;

public class AddressDTO {
    private String street;
    private String suite;
    private String city;
    private String zipCode;
    private GeoDTO geo;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public GeoDTO getGeo() {
        return geo;
    }

    public void setGeo(GeoDTO geo) {
        this.geo = geo;
    }
}
