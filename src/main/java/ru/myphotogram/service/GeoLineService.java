package ru.myphotogram.service;

import com.drew.lang.GeoLocation;
import org.springframework.stereotype.Service;
import ru.myphotogram.repository.PhotoRepository;

import javax.inject.Inject;
import java.util.Comparator;

@Service
public class GeoLineService {

    @Inject
    PhotoRepository photoRepository;

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

