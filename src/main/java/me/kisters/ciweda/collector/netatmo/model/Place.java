package me.kisters.ciweda.collector.netatmo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Objects;

public class Place {
    private final String timezone;
    private final String country;
    private final int altitude;
    private final List<Double> location;
    private final String city;
    private final String street;

    @JsonCreator
    public Place(@JsonProperty("timezone") String timezone,@JsonProperty("country")  String country, @JsonProperty("altitude")  int altitude, @JsonProperty("location")  List<Double> location, @JsonProperty("city")  String city, @JsonProperty("street")  String street) {
        this.timezone = timezone;
        this.country = country;
        this.altitude = altitude;
        this.location = location;
        this.city = city;
        this.street = street;
    }
    public double getLatitude() {
        return location.getLast();
    }

    public double getLongitude () {
        return location.getFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return altitude == place.altitude &&
                Objects.equals(timezone, place.timezone) &&
                Objects.equals(country, place.country) &&
                Objects.equals(street, place.street) &&
                Objects.equals(city, place.city) &&
                Objects.equals(getLatitude(), place.getLatitude()) &&
                Objects.equals(getLongitude(), place.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(timezone, country, altitude, street, city, getLatitude(), getLongitude());
    }

    public Point toGeoPoint() {
        return new GeometryFactory().createPoint(new Coordinate(getLongitude(), getLatitude()));
    }

    @Override
    public String toString() {
        return "Place{" +
                "timezone='" + timezone + '\'' +
                ", country='" + country + '\'' +
                ", altitude=" + altitude +
                ", location={" +
                "latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                "}" +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
