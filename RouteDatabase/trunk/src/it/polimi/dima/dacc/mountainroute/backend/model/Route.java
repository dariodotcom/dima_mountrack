package it.polimi.dima.dacc.mountainroute.backend.model;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class Route implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    // Attributes
    @Attribute
    private String name;

    @Attribute(lob = true)
    private List<GeoPoint> path;

    @Attribute
    private int durationInMinutes;

    @Attribute
    private Difficulty difficulty;

    @Attribute
    private int lenghtInMeters;

    @Attribute
    private int gapInMeters;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GeoPoint> getPath() {
        return path;
    }

    public void setPath(List<GeoPoint> path) {
        this.path = path;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int duration) {
        this.durationInMinutes = duration;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getLenghtInMeters() {
        return lenghtInMeters;
    }

    public void setLenghtInMeters(int lenghtInMeters) {
        this.lenghtInMeters = lenghtInMeters;
    }

    public int getGapInMeters() {
        return gapInMeters;
    }

    public void setGapInMeters(int gapInMeters) {
        this.gapInMeters = gapInMeters;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Route other = (Route) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }
}
