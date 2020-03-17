package com.vharia.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.vharia.coronavirustracker.models.LocationStats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * CoronaVirusDataService
 */
@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    private List<LocationStats> allStats = new ArrayList<>();
    private String asOfDate;
    private String prevDate;
    
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        String asOfDate = "";
        String prevDate = "";

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(VIRUS_DATA_URL))
                                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        StringReader csvBodyReader = new StringReader(response.body());        

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {

            LocationStats locationStat = new LocationStats();

            locationStat.setState(record.get("Province/State"));

            locationStat.setCountry(record.get("Country/Region"));

            int latestCases = Integer.parseInt(record.get(record.size() - 1));

            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));

            locationStat.setLatestTotalCases(latestCases);

            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
            //System.out.println(locationStat);
            newStats.add(locationStat);

        }

        csvBodyReader.reset();
        Iterable<CSVRecord> recordsWithHeader = CSVFormat.DEFAULT.parse(csvBodyReader);
        for(CSVRecord headerRecord : recordsWithHeader){
            asOfDate = headerRecord.get(headerRecord.size() - 1);
            prevDate = headerRecord.get(headerRecord.size() - 2);
            break;
        }

        

        this.allStats = newStats;
        this.asOfDate = asOfDate;
        this.prevDate = prevDate;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public String getAsOfDate() {
        return asOfDate;
    }

    public String getPrevDate() {
        return prevDate;
    }

}