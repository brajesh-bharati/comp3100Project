import java.io.*;
import java.net.*;
import java.util.ArrayList;

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
            System.out.println("message1= " + str1);

            // handshake done
            int compare_core = 0;
            String largest_server_type = null;
            ArrayList<Integer> largest_server_id = new ArrayList<Integer>();
            int server_index = 0;

            dout.write(("REDY\n").getBytes());
            dout.flush();
            str1 = in.readLine();
            System.out.println("message2= " + str1);
            String check = str1.substring(0, 4); // To extract the first word from the response for example JOBN as that
                                                 // is what we are interested in
            while (!check.equals("NONE")) {
                String jobinfo[] = str1.split(" ");
                // dout.write(("GETS Capable " + jobinfo[4] + " " + jobinfo[5] + " " +
                // jobinfo[6] + "\n").getBytes());

                if (jobinfo[0].equals("JOBN")) {
                    dout.write(("GETS All\n").getBytes());
                    dout.flush();
                    str1 = in.readLine();
                    System.out.println("message3= " + str1);
                    String datainfo[] = str1.split(" ");
                    int nRecs = Integer.parseInt(datainfo[1]);
                    dout.write(("OK\n").getBytes());
                    dout.flush();
                    for (int i = 0; i < nRecs; i++) {
                        str1 = in.readLine();
                        System.out.println("message4= " + str1);
                        String split_str4[] = str1.split(" ");
                        int core = Integer.parseInt(split_str4[4]);
                        int server_id = Integer.parseInt(split_str4[1]);

                        // System.out.println("mess= " + compare_core);
                        // System.out.println("mes= " + core);

                        if (core >= compare_core) {
                            if (core > compare_core) {
                                largest_server_id.clear();
                            }
                            largest_server_type = split_str4[0];
                            compare_core = core;
                            largest_server_id.add(server_id);
                        }
                        // System.out.println(largest_server_type);
                        // System.out.println(largest_server_id.size());

                    }
                    dout.write(("OK\n").getBytes());
                    dout.flush();
                    str1 = in.readLine();
                    System.out.println("message5= " + str1);
                    if (server_index < largest_server_id.size()) {
                        dout.write(("SCHD " + jobinfo[2] + " " + largest_server_type + " "
                                + largest_server_id.get(server_index) + "\n").getBytes());
                        str1 = in.readLine();
                        dout.flush();
                        System.out.println("message6= " + str1);
                        server_index++;
                    }

                }
                //dout.write(("REDY\n").getBytes());
                //str1 = in.readLine();
                //System.out.println("message7= " + str1);
                //check = str1.substring(0, 4);

            }
            dout.write(("QUIT\n").getBytes());
            dout.flush();
            str1 = in.readLine();
            System.out.println("message8= " + str1);
            dout.close();
            s.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
