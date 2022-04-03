import java.io.*;
import java.net.*;

//./ds-server -c ./ds-sample-config01.xml -v brief -n

public class MyClient {
    public static void main(String[] args) {
        try {
            String[] arrJob;
            String[] arrServer = { "0", "0", "0", "0", "0", "0", "0", "0", "0" };
            int maxCores = 0;
            int jobID = 0;
            Integer nRecs;
            String largestServType = "";
            int nServers = 0;
            // ArrayList<String> serverType = new ArrayList<String>();

            Socket s = new Socket("127.0.0.1", 50000);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            dout.write(("HELO\n").getBytes()); // Send HELO to server
            String str = (String) in.readLine();
            System.out.println(str);

            dout.write(("AUTH Adrian\n").getBytes()); // Authentication
            str = (String) in.readLine();
            System.out.println(str);

            while (!(str.contains("NONE"))) {
                dout.write(("REDY\n").getBytes());
                str = (String) in.readLine();
                System.out.println(str);

                while (str.contains("JCPL")) {
                    dout.write(("REDY\n").getBytes());
                    str = (String) in.readLine();
                    System.out.println(str);
                }

                if (str.contains("NONE")) {
                    break;
                }

                arrJob = str.split(" ");
                dout.write(("GETS Capable " + arrJob[4] + " " + arrJob[5] + " " + arrJob[6] + "\n").getBytes());
                str = (String) in.readLine();
                System.out.println(str);

                arrJob = str.split(" ");
                nRecs = Integer.valueOf(arrJob[1]);

                dout.write(("OK\n").getBytes());

                for (int i = 0; i < nRecs; i++) { // Identifying largest server type
                    str = (String) in.readLine();
                    System.out.println(str);
                    arrServer = str.split(" ");
                    Integer numCores = Integer.valueOf(arrServer[4]);
                    if (numCores > maxCores) {
                        maxCores = numCores;
                        largestServType = arrServer[0];
                        nServers = 1;
                    } else if (numCores == maxCores) {
                        nServers++;
                    }
                }

                System.out.println(
                        "The largest server type is: " + largestServType + ", and the no. of servers are: " + nServers);

                dout.write(("OK\n").getBytes());
                str = (String) in.readLine();
                System.out.println(str);

                dout.write(("SCHD " + jobID + " " + largestServType + " " + arrServer[1] + "\n").getBytes());
                str = (String) in.readLine();
                System.out.println(str);

                jobID++;
            }

            dout.write(("QUIT\n").getBytes());
            dout.close();
            s.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}