import java.io.*;
import java.net.*;

public class MyClient {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 50000);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream()); // write to the ds-server
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream())); // read the reponse of
                                                                                               // ds-server
            // establish a handsake with the ds-server

            dout.write(("HELO\n").getBytes());
            dout.flush();
            String str = in.readLine();
            System.out.println("message= " + str);
            dout.write(("AUTH brajesh\n").getBytes());
            dout.flush();
            String str1 = in.readLine();
            System.out.println("message= " + str1);

            // handshake done
            int compare_core = 0;
            String larges_server_type = null;

            dout.write(("REDY\n").getBytes());
            dout.flush();
            String str2 = in.readLine();
            System.out.println("message= " + str2);
            String check = str2.substring(0, 4); // To extract the first word from the response for example JOBN as that
                                                 // is what we are interested in
            while (!check.equals("NONE")) {
                String jobinfo[] = str2.split(" ");
                // dout.write(("GETS Capable " + jobinfo[4] + " " + jobinfo[5] + " " +
                // jobinfo[6] + "\n").getBytes());
                dout.write(("GETS All\n").getBytes());
                dout.flush();
                String str3 = in.readLine();
                System.out.println("message= " + str3);
                String datainfo[] = str3.split(" ");
                int nRecs = Integer.parseInt(datainfo[1]);
                dout.write(("OK\n").getBytes());
                for (int i = 0; i < nRecs; i++) {
                    String str4 = in.readLine();
                    System.out.println("message= " + str4);
                    String split_str4[] = str4.split(" ");
                    int core = Integer.parseInt(split_str4[4]);
                    System.out.println("mess= " + compare_core);
                    System.out.println("mes= " + core);

                    if (core > compare_core) {
                        larges_server_type = split_str4[0];
                        compare_core = core;
                    }
                }
                System.out.println(larges_server_type);
                dout.write(("OK\n").getBytes());
                // dout.flush();
                // String str5 = in.readLine();
                // System.out.println("message= " + str5 );

            }
            dout.close();
            s.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
