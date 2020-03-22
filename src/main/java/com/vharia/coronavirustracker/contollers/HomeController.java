package com.vharia.coronavirustracker.contollers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vharia.coronavirustracker.models.LocationStats;
import com.vharia.coronavirustracker.services.ContinentCountryMappingDataService;
import com.vharia.coronavirustracker.services.CoronaVirusDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController
 */
@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService service;

    @Autowired
    ContinentCountryMappingDataService continentDataService;

    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allStats = service.getAllStats();
        Collections.sort(allStats, (LocationStats o1, LocationStats o2) -> o1.getCountry().compareTo(o2.getCountry()));
        
        int totalReportedCases =0;
        int totalNewCases = 0;
        double totalAvgChanges = 0.0d;
        for(LocationStats locationStat : allStats){
            totalReportedCases+= locationStat.getLatestTotalCases();
            totalNewCases+= locationStat.getDiffFromPrevDay();
            totalAvgChanges+= locationStat.getLast3DaysAvgChange();
        }
        
        model.addAttribute("locationStats", allStats);

        NumberFormat myFormat = NumberFormat.getInstance();
        model.addAttribute("totalReportedCases", myFormat.format(totalReportedCases));

        model.addAttribute("totalNewCases", myFormat.format(totalNewCases));

        model.addAttribute("totalAvgChanges", myFormat.format(totalAvgChanges));

        model.addAttribute("asOfDate", service.getAsOfDate());

        model.addAttribute("prevDate", service.getPrevDate());
        return "home";
    }

    @GetMapping("/continent")
    public String continent(Model model){
        List<LocationStats> allStats = service.getAllStats();
        Map<String, String> countryContinentMap = continentDataService.countryContinentMap;

        int totalReportedCases =0;
        int totalNewCases = 0;
        double totalAvgChanges = 0.0d;

        Map<String, LocationStats> continentMap = new HashMap<>();

        for(LocationStats locationStat : allStats){
            if(countryContinentMap.containsKey(locationStat.getCountry())){
                String continent = countryContinentMap.get(locationStat.getCountry());
                LocationStats countryStat = continentMap.get(continent);
                    if(countryStat == null){
                        countryStat =new LocationStats();
                        countryStat.setCountry(continent);
                        continentMap.put(continent, countryStat);
                    }

                    countryStat.setLatestTotalCases(countryStat.getLatestTotalCases() + locationStat.getLatestTotalCases());
                    countryStat.setDiffFromPrevDay(countryStat.getDiffFromPrevDay() + locationStat.getDiffFromPrevDay());
                    countryStat.setLast3DaysAvgChange(countryStat.getLast3DaysAvgChange() + locationStat.getLast3DaysAvgChange());
                    
                    totalReportedCases+= locationStat.getLatestTotalCases();
                    totalNewCases+= locationStat.getDiffFromPrevDay();
                    totalAvgChanges+= locationStat.getLast3DaysAvgChange();
            }else{
                System.out.println("No continent found for -"+locationStat.getCountry());
            }
            }
            
            

        List<LocationStats> locationStats = new ArrayList<>(continentMap.values());
        Collections.sort(locationStats, (LocationStats o1, LocationStats o2) -> o1.getCountry().compareTo(o2.getCountry()));
        model.addAttribute("locationStats", locationStats);

        NumberFormat myFormat = NumberFormat.getInstance();
        model.addAttribute("totalReportedCases", myFormat.format(totalReportedCases));

        model.addAttribute("totalNewCases", myFormat.format(totalNewCases));

        model.addAttribute("totalAvgChanges", myFormat.format(totalAvgChanges));

        model.addAttribute("asOfDate", service.getAsOfDate());

        model.addAttribute("prevDate", service.getPrevDate());
        
        model.addAttribute("country_continent_header_value", "Continent");
        
        return "country";
    }

    @GetMapping("/country")
    public String country(Model model){
        List<LocationStats> allStats = service.getAllStats();

        int totalReportedCases =0;
        int totalNewCases = 0;
        double totalAvgChanges = 0.0d;

        Map<String, LocationStats> countryMap = new HashMap<>();

        for(LocationStats locationStat : allStats){

            LocationStats countryStat = countryMap.get(locationStat.getCountry());
            if(countryStat == null){
                countryStat =new LocationStats();
                countryStat.setCountry(locationStat.getCountry());
                countryMap.put(locationStat.getCountry(), countryStat);
            }

            countryStat.setLatestTotalCases(countryStat.getLatestTotalCases() + locationStat.getLatestTotalCases());
            countryStat.setDiffFromPrevDay(countryStat.getDiffFromPrevDay() + locationStat.getDiffFromPrevDay());
            countryStat.setLast3DaysAvgChange(countryStat.getLast3DaysAvgChange() + locationStat.getLast3DaysAvgChange());
            
            totalReportedCases+= locationStat.getLatestTotalCases();
            totalNewCases+= locationStat.getDiffFromPrevDay();
            totalAvgChanges+= locationStat.getLast3DaysAvgChange();
        }

        List<LocationStats> locationStats = new ArrayList<>(countryMap.values());
        Collections.sort(locationStats, (LocationStats o1, LocationStats o2) -> o1.getCountry().compareTo(o2.getCountry()));
        model.addAttribute("locationStats", locationStats);

        NumberFormat myFormat = NumberFormat.getInstance();
        model.addAttribute("totalReportedCases", myFormat.format(totalReportedCases));

        model.addAttribute("totalNewCases", myFormat.format(totalNewCases));

        model.addAttribute("totalAvgChanges", myFormat.format(totalAvgChanges));

        model.addAttribute("asOfDate", service.getAsOfDate());

        model.addAttribute("prevDate", service.getPrevDate());
        model.addAttribute("country_continent_header_value", "Country");
        return "country";
    }

    
}