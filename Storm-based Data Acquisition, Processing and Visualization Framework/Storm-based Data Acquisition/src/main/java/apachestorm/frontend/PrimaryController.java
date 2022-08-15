package apachestorm.frontend;

import apachestorm.buisnessLogic.Controller;
import apachestorm.service.APIHandler;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

import apachestorm.service.DBHandler;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.json.*;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.textfield.TextFields;

public class PrimaryController implements Initializable {

    @FXML
    private TextField duration;
    @FXML
    private ListView<String> job_list;
    @FXML
    private TextField frequency;
    @FXML
    private TextField job_id;
    @FXML
    private TextField keyword;
    @FXML
    private TextArea detail;
    @FXML
    private ListView<String> jobs_active;
    @FXML TextField scheduler;
    HashMap<String,Thread> active_jobs = new HashMap<String,Thread>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String builtin_schedulers [] = {"DefaultScheduler","IsolationScheduler","MultitenantScheduler","ResourceAwareScheduler"};
        TextFields.bindAutoCompletion(scheduler,builtin_schedulers);

        Task task2 = new Task() {
            @Override protected String call() throws Exception {
                while(true) {
                    String topologies = Controller.get_topologies();
                    topologies = topologies.substring(4, topologies.length());
                    JSONObject obj = new JSONObject(topologies);
                    JSONArray arr = obj.getJSONArray("topologies");
                    for (int i = 0; i < arr.length(); i++) {
                        String id = arr.getJSONObject(i).getString("id");
                        if (!jobs_active.getItems().contains(id) && !job_list.getItems().contains(id)) {
                            jobs_active.getItems().add(id);
                        }
                    }
                    Thread.sleep(10000);
                }
            }
        
        };
        
        Thread th = new Thread(task2);
        th.setDaemon(true);
        th.start();

    }    
  
    @FXML
    private void add(MouseEvent event) {
                
        String jobID = job_id.getText().toString();
        String freq = frequency.getText().toString();
        String dur  = duration.getText().toString();
        String scheduler_str = scheduler.getText().toString();
        String keyword_str = keyword.getText().toString();
        String detail_str = detail.getText().toString();

        jobs_active.getItems().remove(jobID);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        job_id.setText("");
        frequency.setText("");
        duration.setText("");
        scheduler.setText("");
        keyword.setText("");
        detail.setText("");

        Task task = new Task() {
            @Override protected String call() throws Exception {
                int dur_int = Integer.parseInt(dur);
                int freq_int = Integer.parseInt(freq);

                for (int i = 0; i<dur_int; i+=freq_int ) {
                    try {
                        String result = Controller.get_topology_data(jobID,dtf.format(now),freq,dur);
                        Controller.saveInDatabase(jobID,result,dtf.format(now),freq,dur,scheduler_str,keyword_str,detail_str);
                        Thread.sleep(freq_int*1000);
                    } catch (InterruptedException ex) {
                        System.out.println("130");
                        ex.printStackTrace();
                    }
                }
                job_list.getItems().remove(jobID);
                return null;
            }
        
        };
        
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        active_jobs.put(jobID, th);
        job_list.getItems().add(jobID);
    }

    @FXML
    private void remove(MouseEvent event) {
        String item = job_list.getSelectionModel().getSelectedItem();
        if (item != null) {
            Thread th = active_jobs.get(item);
            th.interrupt();
            active_jobs.remove(item);
            job_list.getItems().remove(item);
            jobs_active.getItems().add(item);
        }

    }

    @FXML
    private void select(MouseEvent event) {
        String item = jobs_active.getSelectionModel().getSelectedItem();
        if (item != null) {
            job_id.setText(item);
        }
    }
    
}
