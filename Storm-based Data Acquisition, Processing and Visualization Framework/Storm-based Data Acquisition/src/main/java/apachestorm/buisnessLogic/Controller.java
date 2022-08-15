package apachestorm.buisnessLogic;

import apachestorm.service.APIHandler;
import apachestorm.service.DBHandler;

import java.sql.SQLException;

public class Controller {

    public static void saveInDatabase(String jobID,String result,String time,String freq,String dur, String scheduler_str,String keyword_str,String detail_str) {
        DBHandler.saveInDatabase(jobID,result,time,freq,dur,scheduler_str,keyword_str,detail_str);
    }

    public static String get_topology_data(String jobID,String time,String freq,String dur) throws SQLException {
        return APIHandler.get_topology_data(jobID,time,freq,dur);
    }

    public static String get_topologies() {
        return APIHandler.get_topologies();
    }
}
