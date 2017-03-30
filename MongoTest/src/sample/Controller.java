package sample;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class Controller implements  Runnable{
    String name;
    String gate;

    public Controller(String x){
        name =x;

    }




    @Override
    public void run(){
//
        String ipSub="192.168.100";
        int timeout=3000;
        String hostIP = null;
        ArrayList <String> hosts = new ArrayList();
        for (int i=1;i<255;i++){
           hostIP=ipSub + "." + i;
            try {
                if (InetAddress.getByName(hostIP).isReachable(timeout)){
                    System.out.println(hostIP);
                    hosts.add(hostIP);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        hostIP= hosts.get(1);
        System.out.println(hostIP);

        MongoClient client = new MongoClient(hostIP, 27017);

        DB db = client.getDB("videoDB");
        FileInputStream inputStream=null;

        GridFS videos = new GridFS(db, "videos");

        try{
            inputStream = new FileInputStream("video4.mp4");
        } catch (FileNotFoundException e){
            System.out.println("Can't open file");
            System.exit(1);
        }

        GridFSInputFile video = videos.createFile( inputStream, "video4.mp4");
        //metadata
        BasicDBObject meta = new BasicDBObject("description", "random"); ArrayList<String> tags = new ArrayList<>();
        tags.add("Bubble Wrestling");
        meta.append("tags", tags);

        video.setMetaData(meta);
        video.save();

        System.out.println("Object ID in Files Collection: " + video.get("_id"));
        System.out.println("Saved the file to MongoDB "+ db);
        System.out.println("Now to read and make a copy");

        GridFSDBFile gridFile = videos.findOne(new BasicDBObject("filename","video4.mp4"));

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("video4_copy.mp4");
            ////OMARBADRAWY-HP//Users//Omar Badrawy//Documents//
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            gridFile.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("You have a copy!");
    }
}
