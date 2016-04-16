package ru.myphotogram.dataextration;

import com.drew.lang.GeoLocation;

import java.util.Date;

/**
 * Created by juliaryabinina on 16/04/16.
 */
public interface MetadataExtractionService {

    Date getDate();
    GeoLocation getGeoLocation();
}
