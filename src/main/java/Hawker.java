public class Hawker {
    String name;
    String address;
    String postal;
    double latitude;
    double longitude;

    Hawker(String n, String add, String p, double lat, double lon) {
        name = n;
        address = add;
        postal = p;
        latitude = lat;
        longitude = lon;
    }

    double getLat() {
        return latitude;
    }

    double getLon() {
        return longitude;
    }

    String getPostal() { return postal; }

    @Override
    public String toString() {
        return String.format("Name: %s\nAddress: %s, \n/%s\n", name, address, postal);
    }
}