package ru.myphotogram.dataextration.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import ru.myphotogram.dataextration.MetadataExtractionService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * extract Date and geolocation if this information is available;
 */
public class MetadataExtractionServiceImpl implements MetadataExtractionService {

    final Metadata metadata;

    public MetadataExtractionServiceImpl(InputStream inputStream) throws IOException, ImageProcessingException {
        Objects.requireNonNull(inputStream, "InputStream for metadata extraction is null!");
        metadata = ImageMetadataReader.readMetadata(inputStream);
    }

    /**
     * @return Date if exists in metadata, null if no date is found in metadata
     */
    public Date getDate() {
        Collection<ExifSubIFDDirectory> exifDirectories = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);
        if (exifDirectories != null)
            for (ExifSubIFDDirectory exifDirectory : exifDirectories) {
                Date date = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (date != null) {
                    return date;
                }
            }
        return null;
    }

    /**
     * @return GeoLocation if exists in metadata, null if no geotags are found in metadata
     */
    public GeoLocation getGeoLocation() {
        Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
        if (gpsDirectories != null)
            for (GpsDirectory gpsDirectory : gpsDirectories) {
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                if (geoLocation != null && !geoLocation.isZero()) {
                    return geoLocation;
                }
            }
        return null;
    }
}
