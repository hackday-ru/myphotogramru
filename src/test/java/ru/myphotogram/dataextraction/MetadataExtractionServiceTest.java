package ru.myphotogram.dataextraction;

import com.drew.lang.GeoLocation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import ru.myphotogram.dataextration.MetadataExtractionService;

import java.util.Date;

import static org.mockito.Mockito.mock;

public class MetadataExtractionServiceTest {

    private static MetadataExtractionService metadataExtractionService;

    @BeforeClass
    public static void setup() {
        metadataExtractionService = mock(MetadataExtractionService.class);
        Mockito.when(metadataExtractionService.getDate())
            .thenReturn(new Date()).thenReturn(null);
        Mockito.when(metadataExtractionService.getGeoLocation())
            .thenReturn(new GeoLocation(1d, 1d)).thenReturn(null);
    }


    @Test
    public void checkGetDate() {
        Date date = metadataExtractionService.getDate();
        Assert.assertNotNull(date);
        date = metadataExtractionService.getDate();
        Assert.assertNull(date);
    }

    @Test
    public void checkGetGeoLocation() {
        GeoLocation geoLocation = metadataExtractionService.getGeoLocation();
        Assert.assertNotNull(geoLocation);
        geoLocation = metadataExtractionService.getGeoLocation();
        Assert.assertNull(geoLocation);
    }
}
