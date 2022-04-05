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
            System.out.println("message= " + str1);

            // handshake done
            int compare_core = 0;
            String largest_server_type = null;
            ArrayList<Integer> largest_server_id = new ArrayList<Integer>();
            int server_index = 0;

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
                dout.flush();
                for (int i = 0; i < nRecs; i++) {
                    String str4 = in.readLine();
                    System.out.println("message= " + str4);
                    String split_str4[] = str4.split(" ");
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
                }
                // System.out.println(largest_server_type);
                // System.out.println(largest_server_id.size());

                dout.write(("OK\n").getBytes());
                dout.flush();
                //String str5 = in.readLine();
                //System.out.println("message= " + str5);

                if (server_index < largest_server_id.size() && jobinfo[0].equals("JOBN")) {
                    dout.write(("SCHD " + jobinfo[2] + " " + largest_server_type + " "
                            + largest_server_id.get(server_index) + "\n").getBytes());
                    String str6 = in.readLine();
                    dout.flush();
                    System.out.println("message= " + str6);
                    server_index++;
                }
            }
            dout.write(("QUIT\n").getBytes());
            dout.flush();
            String str8 = in.readLine();
            System.out.println("message= " + str8);
            dout.close();
            s.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
