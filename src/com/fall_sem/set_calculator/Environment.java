package com.fall_sem.set_calculator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Environment {
    public Map<String, Double> variables = new HashMap<>();
    private TreeSet<Double> previous_result;


    public void importVariablesFromFile(String path, boolean override) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader(path) );
        StringBuilder data = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            data.append(line.replaceAll(" ", ""));
        }
        String[] vars = data.toString().split(",");
        for(String var: vars) {
            String name = var.split("=")[0];
            double value = Double.parseDouble(var.split("=")[1]);
            boolean exists = (variables.get(name) != null);
            if(exists) {
                if(override){
                    variables.put(name, value);
                }
            }
            else {
                variables.put(name, value);
            }
        }

    }


    public TreeSet<Double> getPreviousResult() {
        return previous_result;
    }

    public void setPreviousResult(TreeSet<Double> previous_result) {
        this.previous_result = previous_result;
    }
}