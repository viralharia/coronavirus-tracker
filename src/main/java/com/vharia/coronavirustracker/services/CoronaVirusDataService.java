package com.vharia.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
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

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
        
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
            StringBuilder debugLog = new StringBuilder();

            LocationStats locationStat = new LocationStats();

            locationStat.setState(record.get("Province/State"));

            locationStat.setCountry(record.get("Country/Region"));

            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            locationStat.setLatestTotalCases(latestCases);

            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);

            int prevDayCasesP1 = Integer.parseInt(record.get(record.size() - 3));
            int prevDiff1 = prevDayCases - prevDayCasesP1;

            int prevDayCasesP2 = Integer.parseInt(record.get(record.size() - 4));
            int prevDiff2 = prevDayCasesP1 - prevDayCasesP2;

            int prevDayCasesP3 = Integer.parseInt(record.get(record.size() - 5));
            int prevDiff3 = prevDayCasesP2 - prevDayCasesP3;

            float avg = (prevDiff1 + prevDiff2 + prevDiff3)/3.0f;
            locationStat.setLast3DaysAvgChange(round(avg, 2));

            // debugLog.append(locationStat.getCountry()).append("-").append(locationStat.getState()).append("-");
            // debugLog.append(prevDiff1).append("-").append(prevDiff2).append("-").append(prevDiff3);

            // System.out.println(debugLog.toString());
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

    public static float round(float value, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++) {
            pow *= 10;
        }
        float tmp = value * pow;
        float tmpSub = tmp - (int) tmp;
    
        return ( (float) ( (int) (
                value >= 0
                ? (tmpSub >= 0.5f ? tmp + 1 : tmp)
                : (tmpSub >= -0.5f ? tmp : tmp - 1)
                ) ) ) / pow;
    
        // Below will only handles +ve values
        // return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
    }

}