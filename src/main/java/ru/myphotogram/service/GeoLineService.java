package ru.myphotogram.service;

import com.drew.lang.GeoLocation;
import org.springframework.stereotype.Service;
import ru.myphotogram.domain.Photo;
import ru.myphotogram.domain.User;
import ru.myphotogram.repository.PhotoRepository;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GeoLineService {

    @Inject
    PhotoRepository photoRepository;

    private final GeoLocationComparator comparator = new GeoLocationComparator();

    public Map<String, List<Photo>> photos(User user) {
        List<Photo> photos = photoRepository.findPhotos(user);
        return limit5(reduce(photos));
    }


    private Map<String, List<Photo>> reduce(List<Photo> photos) {
        Map<GeoLocation, List<Photo>> result = new HashMap<>();
        for (Photo photo : photos) {
            if (hasNoGeoTag(photo)) continue;
            GeoLocation geoLocation = new GeoLocation(photo.getLatitude().doubleValue(), photo.getLongitude().doubleValue());
            if (!isKnownLocation(result.keySet(), geoLocation)) {
                result.put(geoLocation, Stream.of(photo).collect(Collectors.toList()));
            } else {
                result.get(geoLocation).add(photo);
            }
        }

        return result.entrySet().stream().collect(Collectors.toMap(
            key -> String.valueOf(key.hashCode()).substring(0, 5),
            Map.Entry::getValue));
    }

    private boolean isKnownLocation(Set<GeoLocation> locations, GeoLocation l) {
        for (GeoLocation location : locations) {
            if (comparator.compare(location, l) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNoGeoTag(Photo p) {
        return p.getLatitude() == null || p.getLongitude() == null;
    }

    private Map<String, List<Photo>> limit5(Map<String, List<Photo>> collect) {
        return collect.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> e.getValue().stream().limit(5).collect(Collectors.toList())));
    }

    private static class GeoLocationComparator implements Comparator<GeoLocation> {

        @Override
        public int compare(GeoLocation o1, GeoLocation o2) {
            double distance = distanceTo(o1, o2);
            if (distance > 1) return -1;
            else return 0;
        }

        private double distanceTo(GeoLocation gp1, GeoLocation gp2) {
            if (gp1 != null && gp2 != null) {
                int radius = 6371; // Километры
                double dLat = Math.toRadians(gp2.getLatitude() - gp1.getLatitude());
                double dLong = Math.toRadians(gp2.getLongitude() - gp1.getLongitude());
                double lat1 = Math.toRadians(gp1.getLatitude());
                double lat2 = Math.toRadians(gp2.getLatitude());
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                return c * radius;
            }
            return 2d;
        }
    }

}

