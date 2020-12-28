package com.tgilonis.coronavirustracker.services;

import com.tgilonis.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService
{
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats()
    {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *") //scheduled for the first hour of each day
    public void fetchVirusData() throws IOException, InterruptedException
    {
        List<LocationStats> newStat = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records)
        {
            int latestCases = Integer.parseInt(record.get(record.size()-1));
            int prevDayCases = Integer.parseInt(record.get(record.size()-2));
            LocationStats locationStat = setUpLocationStats(record);
            locationStat.setDiffFromPrevDay(latestCases-prevDayCases);
            //System.out.println(locationStat);
            newStat.add(locationStat);
        }
        this.allStats = newStat;
    }

    private LocationStats setUpLocationStats(CSVRecord record)
    {
        return new LocationStats(
                record.get("Province/State"),
                record.get("Country/Region"),
                Integer.parseInt(record.get(record.size()-1)));
    }


}
