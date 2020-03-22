package com.vharia.coronavirustracker.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

/**
 * ContinentCountryMappingDataService
 */
@Service
public class ContinentCountryMappingDataService {

    public Map<String, String> countryContinentMap;

    @PostConstruct
    public void loadContinentCountryMappingJSONData(){
        countryContinentMap = new HashMap<>();
        System.out.println("in ContinentCountryMappingDataService....");
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Map<String, String>>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/static/countries-by-continent.json");
        try {
            List<Map<String, String>> jsonDataList = mapper.readValue(inputStream,typeReference);
            //userService.save(users);
            //System.out.println(jsonDataList);

            for(Map<String, String> map : jsonDataList){
                countryContinentMap.put(map.get("country"), map.get("continent"));
            }
        } catch (IOException e){
            System.out.println("Unable to load json file: " + e.getMessage());
        }
    }
    
}