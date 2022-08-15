package apachestorm.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class APIHandler {
    static public String get_topology_data(String job_id,String start,String frequency,String duration) throws SQLException {
        String return_statement = "";
            try {
                URL url = new URL("http://localhost:8090/api/v1/topology/"+job_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                        + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                while ((output = br.readLine()) != null) {
                        return_statement += output;
                }

            conn.disconnect();
        }
        catch (MalformedURLException e) {
            System.out.println("112");
        }
        catch (IOException e) {
            System.out.println("115");
        }
        return return_statement;
    }

    static public String get_topologies(){
        String topologies = null;
        try {
            URL url = new URL("http://localhost:8090/api/v1/topology/summary");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                                    + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                    topologies += output;
                    //System.out.println(output);
            }

            conn.disconnect();
        }
        catch (MalformedURLException e) {
            System.out.println("112");
        }
        catch (IOException e) {
            System.out.println("115");
        }
        return topologies;
    }

}
