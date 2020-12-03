public class Coordinates {
    double latitude;
    double longitude;

    Coordinates(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    double getLat() {
        return latitude;
    }

    double getLon() {
        return longitude;
    }
}
