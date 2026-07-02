package com.enginertugrul.iottemperaturemonitor.security.ingestion;

public interface SensorIngestionTokenGenerator {

    GeneratedSensorIngestionToken generate();

    String hash(String rawToken);

}
