package com.vharia.coronavirustracker.contollers;

import java.util.Collections;
import java.util.List;

import com.vharia.coronavirustracker.models.LocationStats;
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

    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allStats = service.getAllStats();
        Collections.sort(allStats, (LocationStats o1, LocationStats o2) -> o1.getCountry().compareTo(o2.getCountry()));

        
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();

        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();

        model.addAttribute("locationStats", allStats);

        model.addAttribute("totalReportedCases", totalReportedCases);

        model.addAttribute("totalNewCases", totalNewCases);

        model.addAttribute("asOfDate", service.getAsOfDate());

        model.addAttribute("prevDate", service.getPrevDate());
        return "home";

    }

    
}