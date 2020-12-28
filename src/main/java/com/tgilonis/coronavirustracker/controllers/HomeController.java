package com.tgilonis.coronavirustracker.controllers;

import com.tgilonis.coronavirustracker.models.LocationStats;
import com.tgilonis.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController
{
    @Autowired
    CoronaVirusDataService coronavirusDataService;

    @GetMapping("/") // Map to the root URL
    public String home(Model model)
    {
        List<LocationStats> allStats = coronavirusDataService.getAllStats();

        int totalReportedCases = allStats.stream()
                .mapToInt(LocationStats::getLatestTotalCases)
                .sum();

        int totalNewCases = allStats.stream()
                .mapToInt(LocationStats::getDiffFromPrevDay)
                .sum();

        model.addAttribute("locationStats", coronavirusDataService.getAllStats());

        model.addAttribute("totalReportedCases", totalReportedCases);

        model.addAttribute("totalNewCases", totalNewCases);

        return "home";
    } //thymeleaf works through the templates folder to access home.html

    //when a controller is called, you can access data and build the context (i.e. the html elements)
}
