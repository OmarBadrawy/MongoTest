package sample;

import java.io.IOException;



public class Main {


    public static void main( String args[] ) throws IOException, InterruptedException {


    Thread user1 = new Thread(new Controller("One"));
    //Thread user2 = new Thread(new Controller2("Two"));


    user1.start();
   /* user1.sleep(999);
    user2.start();*/

    }


}




