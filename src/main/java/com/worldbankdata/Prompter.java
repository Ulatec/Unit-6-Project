package com.worldbankdata;

import com.worldbankdata.model.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mark on 4/24/2017.
 */
public class Prompter {
    private BufferedReader mReader;
    private Map<String, String> options;
    private SessionManager sessionManager;
    public Prompter(){
        mReader = new BufferedReader(new InputStreamReader(System.in));
        options = new LinkedHashMap<String, String>();
        buildMenu();
        sessionManager = new SessionManager();
    }
    public static void listOptions(){

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
        options.put("list teams", "List Teams");
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
                    case "exit":
                        break;
                }
            }catch(IOException ioe){
                System.out.println("Error parsing input! Let's try again! %n");
            }
        }while(!choice.equals("quit"));
    }

    private void listEntries() {
        //System.out.printf(sessionManager.test());
        List<Country> countries = sessionManager.fetchAllCountries();
        countries.stream().forEach(System.out::println);
    }
}
