import java.io.*;
import java.net.*;

//./ds-server -c ./ds-sample-config01.xml -v brief -n
//./demoS1.sh MyClient.class -n

//./stage2-test-x86 "java stage2" -o tt -n


public class stage2 {
    public static void main(String[] args) {
        try {
            String[] arrJob;
            String[] arrServer = { "0", "0", "0", "0", "0", "0", "0", "0", "0" };
            String goodServerType = ""; //the server which satisfies FF conditions
            int jobID = 0;
            int servID = 0;
            int nServers = 0;
            Integer nRecs;
            Integer sumOfwrJobs; //checking sum waiting and runnings jobs <2 so you can schd

            Socket s = new Socket("127.0.0.1", 50000);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            dout.write(("HELO\n").getBytes()); // Send HELO to server
            String str = (String) in.readLine();
            System.out.println(str);

            dout.write(("AUTH adrian\n").getBytes()); // Authentication
            str = (String) in.readLine();
            System.out.println(str);

            while (!(str.contains("NONE"))) { // Check that the last msg is not NONE
                dout.write(("REDY\n").getBytes());
                str = (String) in.readLine();
                System.out.println(str);

                while (str.contains("JCPL")) { 
                    dout.write(("REDY\n").getBytes());
                    str = (String) in.readLine();
                    System.out.println(str);
                }

                if (str.contains("NONE")) { //Break if msg is NONE
                    break; 
                }
                System.out.println("breaks on line49");
                arrJob = str.split(" "); //split up JOB into separate strings
                dout.write(("GETS Capable " + arrJob[4] + " " + arrJob[5] + " " + arrJob[6] + "\n").getBytes());
                str = (String) in.readLine();
                System.out.println(str);

                arrJob = str.split(" "); 
                nRecs = Integer.valueOf(arrJob[1]); 
                //jCores = Integer.valueOf(arrJob[4]);

                dout.write(("OK\n").getBytes());

                for (int i = 0; i < nRecs; i++) { // Identify if server can handle job 
                    str = (String) in.readLine();
                    System.out.println(str);
                    System.out.println("breaks on line 64");

                    arrServer = str.split(" ");

                    sumOfwrJobs = Integer.valueOf(arrServer[7]) + Integer.valueOf(arrServer[8]);
                    if (sumOfwrJobs < 2)  { //check if server have running or waiting jobs
                        goodServerType = arrServer[0];
                        servID = Integer.valueOf(arrServer[1]);
                        break;
                    } else if (sumOfwrJobs > 2) {
                        goodServerType = arrServer[0];
                        servID = Integer.valueOf(arrServer[1]);
                    }
                }

                dout.write(("OK\n").getBytes());
                str = (String) in.readLine();
                System.out.println(str);

                // dout.write(("OK\n").getBytes());
                // str = (String) in.readLine();
                // System.out.println(str);

                dout.write(("SCHD " + jobID + " " + goodServerType + " " + servID + "\n").getBytes());
                System.out.println("SCHD " + jobID + " " + goodServerType + " " + servID + "\n");
                str = (String) in.readLine();
                System.out.println(str);

                // if (servID == nServers) {
                //     servID = 0;
                // }
                while(!(in.readLine().contains("OK"))) {
                    str = (String) in.readLine();
                }
                jobID++;
                // nServers = 0;
            }
            dout.write(("QUIT\n").getBytes()); //Quiting when no more jobs left
            dout.close();
            s.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}