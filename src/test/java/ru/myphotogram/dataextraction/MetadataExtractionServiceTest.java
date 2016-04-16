package ru.myphotogram.dataextraction;

import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import org.junit.Assert;
import org.junit.Test;
import ru.myphotogram.dataextration.MetadataExtractionService;
import ru.myphotogram.dataextration.impl.MetadataExtractionServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class MetadataExtractionServiceTest {

    final InputStream inputStream = new ByteArrayInputStream("1234567".getBytes());
    final MetadataExtractionService metadataExtractionService = new MetadataExtractionServiceImpl(inputStream);

    public MetadataExtractionServiceTest() throws IOException, ImageProcessingException {
    }

    @Test
    public void checkGetDate() {
        Date date = metadataExtractionService.getDate();
        Assert.assertNull(date);
    }

    @Test
    public void checkGetGeoLocation() {
        GeoLocation geoLocation = metadataExtractionService.getGeoLocation();
        Assert.assertNull(geoLocation);
    }
}
