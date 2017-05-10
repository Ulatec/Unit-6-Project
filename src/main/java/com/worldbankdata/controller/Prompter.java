package com.worldbankdata.controller;

import com.worldbankdata.DAO.CountryDaoImplementation;
import com.worldbankdata.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * Created by mark on 4/24/2017.
 */
public class Prompter {
    private BufferedReader mReader;
    private Map<String, String> options;
    private CountryDaoImplementation sessionManager;

    public Prompter(){
        mReader = new BufferedReader(new InputStreamReader(System.in));
        options = new LinkedHashMap<String, String>();
        sessionManager = new CountryDaoImplementation();
        buildMenu();
    }


    private String promptAction()throws IOException{
        System.out.printf("%n Main Menu: %n");
        for(Map.Entry<String,String> option : options.entrySet()){
            System.out.printf("%s - %s %n", option.getKey(), option.getValue());
        }
        System.out.println("What do you want to do: ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    private void buildMenu(){
        //LinkedHashMap to maintain menu order.
        options.put("list entries","List all Database Entries");
        options.put("add test", "Add a test entry");
        options.put("add player", "Add a player to a Team Roster");
        options.put("remove player", "Remove a player from a team roster.");
        options.put("experience report", "Print high-level experience report.");
        options.put("list all players", "List available players");
        options.put("quit","Exit the application.");
    }
    public void run(){
        String choice ="";
        do{
            try{
                choice = promptAction();
                switch (choice) {
                    case "list entries":
                        listEntries();
                        break;
                    case "add":
                        Country country = buildCountry();
                        add(country);
                        break;
                    case "edit":
                        edit(select());
                        break;
                    case "select":
                        System.out.print(select());
                        break;
                    case "delete":
                        delete(select());
                        break;
                    case "test":
                        test();
                        break;
                    case "show stats":
                        showStatistics();
                        break;
                    case "exit":
                        break;
                }
            }catch(IOException ioe){
                System.out.println("Error parsing input! Let's try again! %n");
            }
        }while(!choice.equals("quit"));
    }

    private void test(){
        Country testCountry = sessionManager.findCountryByCode("ZZZ");
        testCountry.setName("AYYLMAO");
        sessionManager.update(testCountry);
    }

    private void edit(Country country) {
        Country editCountry = sessionManager.findCountryByCode(country.getCode());
        String newNameString = newName(country);
        if(!newNameString.equals("")) {
            editCountry.setName(newNameString);
        }else{
            System.out.println("Did not update name.");
        }
        Double newLiteracyDouble = newLiteracy(country);
        if(newLiteracyDouble != null) {
            editCountry.setAdultLiteracyRate(newLiteracyDouble);
        }else{
            System.out.println("Did not update adult literacy rate.");
        }
        Double newInternetUsersDouble = newInternetUsers(country);
        if(newInternetUsersDouble != null) {
            editCountry.setInternetUsers(newInternetUsersDouble);
        }else{
            System.out.println("Did not update internet users.");
        }

        sessionManager.update(editCountry);
    }

    private String newCode(Country country){
        System.out.printf("The current country code is %s. %n", country.getCode());
        System.out.printf("Please enter a new country code (leave blank to leave unchanged):");
        String newCodeString = null;
        do{
            try {
                newCodeString = mReader.readLine();
                if(newCodeString.equals("")){
                    return null;
                }
            }catch(IOException exception){
                System.out.println("Sorry, that was not a valid code");
            }
        }while(newCodeString == null);
        return newCodeString;
    }
    private String newName(Country country){
        System.out.printf("The current country name is %s. %n", country.getName());
        System.out.printf("Please enter a new country name:");
        String newNameString = null;
        boolean validValue = false;
        do{
            try {
                newNameString = mReader.readLine();
                if(newNameString.equals("")){
                    return null;
                }
                validValue = true;
            }catch(IOException exception){
                System.out.println("Sorry, that was not a valid code");
            }
        }while(!validValue);
        return newNameString;
    }
    private Double newLiteracy(Country country){
        System.out.printf("The current country adult literacy rate is is %s. %n", country.getAdultLiteracyRate());
        System.out.printf("Please enter a new literacy rate:");
        Double newLiteracy = null;
        boolean validValue = false;
        do{
            try {
                newLiteracy = Double.parseDouble(mReader.readLine());
                if(newLiteracy <= 100.0 && newLiteracy >= 0.0){
                    validValue = true;
                }
            }catch(IOException | NumberFormatException exception){
                System.out.println("Sorry, that was not a valid number.");
            }
        }while(!validValue);
        return newLiteracy;
    }
    private Double newInternetUsers(Country country){
        System.out.printf("The current country internet users is %s. %n", country.getInternetUsers());
        System.out.printf("Please enter a new number of internet users (millions):");
        Double newInternetUsers = null;
        boolean validValue = false;
        do{
            try {
                newInternetUsers = Double.parseDouble(mReader.readLine());
                validValue = true;
            }catch(IOException | NumberFormatException exception){
                System.out.println("Sorry, that was not a valid number.");
            }
        }while(!validValue);
        return newInternetUsers;
    }

    private void listEntries() {
        List<Country> countries = sessionManager.fetchAllCountries();

        System.out.printf("%-30s %-30s %-30s %-30s %n", "Name", "Code", "Adult Literacy Rate", "Internet Users");
        System.out.printf("===========================================================================================================%n");
        for(Country country : countries){

            //System.out.printf("");
            String internetUsersAsString;
            String adultLiteracyAsString;
            if(country.getInternetUsers() != null) {
                internetUsersAsString = country.getInternetUsers().toString();
            }else{
                internetUsersAsString = "----";
            }
            if(country.getAdultLiteracyRate() != null) {
                adultLiteracyAsString = country.getAdultLiteracyRate().toString();
            }else{
                adultLiteracyAsString = "----";
            }
            System.out.printf("%-30s %-30s %-30s %-30s %n", country.getName(), country.getCode(), adultLiteracyAsString, internetUsersAsString);
        }
    }

    private Country buildCountry(){


        try {
            System.out.println("Creating new country. Country name:");
            String countryName = mReader.readLine();
            System.out.println("Code:");
            String code = mReader.readLine();
            System.out.println("Internet Users:");
            String internetUsers = mReader.readLine();
            System.out.println("Adult Literacy Rate:");
            String literacyRate = mReader.readLine();
            Country.CountryBuilder builder = new Country.CountryBuilder(code, countryName).withInternetUsers(new Double(internetUsers)).withLiteracyRate(new Double(literacyRate));
            return builder.build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void add(Country country){
        sessionManager.save(country);
    }

    private void addTest(){
        Country newCountry = new Country.CountryBuilder("XXX", "Pornoland").withLiteracyRate(new Double(100)).withInternetUsers(new Double(100)).build();
        System.out.printf("ID:" + sessionManager.save(newCountry) + "%n");
    }

    private Country select(){
        List<Country> countries = sessionManager.fetchAllCountries();
        int i = 1;
        for(Country country : countries){
            System.out.printf("%d.) " + country.getName() + " %n", i);
            i++;
        }
        boolean validSelection = false;
        int code;
        do{
            try{
                code  = Integer.parseInt(mReader.readLine()) - 1;
                validSelection = true;
                return countries.get(code);
            }catch(IOException | NumberFormatException exception){
                System.out.println("Sorry, that was not a valid selection.");
            }
        }while(!validSelection);


        return null;
    }

    private void delete(Country country){
            sessionManager.delete(country);
    }

    private void showStatistics(){
        System.out.printf("Most Internet users: %s - %s %n", sessionManager.getMaxInternetUsers().getName(), sessionManager.getMaxInternetUsers().getInternetUsers());
        System.out.printf("Least Internet users: %s - %s %n", sessionManager.getMinInternetUsers().getName(), sessionManager.getMinInternetUsers().getInternetUsers());
        System.out.printf("Highest adult literacy rate: %s - %s %n", sessionManager.getMaxAdultLiteracy().getName(), sessionManager.getMaxAdultLiteracy().getAdultLiteracyRate());
        System.out.printf("Lowest adult literacy rate: %s - %s %n", sessionManager.getMinAdultLiteracy().getName(), sessionManager.getMinAdultLiteracy().getAdultLiteracyRate());
        System.out.printf("Correlation coefficient between adult literacy and internet users: %s %n", sessionManager.correlationCoefficient());
    }
}
