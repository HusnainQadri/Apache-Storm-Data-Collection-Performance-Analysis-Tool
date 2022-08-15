package apachestorm.service;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.bson.BSONObject;
import org.bson.json.JsonWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONString;


public class DBHandler {
    public static void saveInDatabase(String jobid,String jsonreponse,String start,String frequency,String duration,String scheduler,String keyword,String detail) {

        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        DB db = mongoClient.getDB( "Storm" );
        DBCollection coll = db.getCollection("Responses");

        // Adding Data
        BasicDBObject doc = new BasicDBObject("jobID", jobid).
                append("start", start).
                append("frequency", frequency).
                append("duration", duration).
                append("scheduler",scheduler).
                append("keyword",keyword).
                append("detail",detail).
                append("Response",jsonreponse);
        coll.insert(doc);
        mongoClient.close();
    }
}
