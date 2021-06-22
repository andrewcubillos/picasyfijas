
package co.escuelaing.edu.herokuprimer;

import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConcurrentWebApp {
    
    private static ConcurrentWebApp _instance = new ConcurrentWebApp();
    private ConcurrentWebApp(){}
    private static ConcurrentWebApp getInstance(){
        return _instance;
    }
    public static void main(String... args) throws IOException{
       ConcurrentWebApp.getInstance().startServer(args);
    }
    public static void startServer(String[] args) throws IOException {
        int port = getPort();
        ServerSocket serverSocket = null;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: "+port);
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
        while(running){
        try {
            System.out.println("Listo para recibir por el puerto "+ port);
            clientSocket = serverSocket.accept();
            pool.submit(processRequest(clientSocket));
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        

        //processRequest(clientSocket);
        }
        serverSocket.close();
    }
    
    public static Runnable processRequest(Socket clientSocket) throws IOException {
        return new Runnable() {
            @Override
            public void run() {

                
                try {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
                    String inputLine;
                    String method = "";
                    String path = "";
                    String version = "";
                    List<String> headers = new ArrayList<String>();
                    while ((inputLine = in.readLine()) != null) {
                        if (method.isEmpty()) {
                            String[] requestStrings = inputLine.split(" ");
                            method = requestStrings[0];
                            path = requestStrings[1];
                            version = requestStrings[2];
                            System.out.println("Request: " + method + " " + path + " " + version);
                        } else {
                            System.out.println("Header: " + inputLine);
                            headers.add(inputLine);
                        }
                        
                        if (!in.ready()) {
                            break;
                        }
                    }   String responseMsg = createTextResponse(path);
                    out.println(responseMsg);
                    out.close();
                    in.close();
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ConcurrentWebApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
}
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 36000; //returns default port if heroku-port isn't set(i.e. on localhost)

    }
    public static String createTextResponse(String path){
        String type ="text/html";
        if (path.endsWith(".css")){
            type="text/css";
        }else if (path.endsWith(".js")){
            type="text/javascript";
        }
        else if (path.endsWith(".ico")){
            type="image/x-icon";
        }
        else if (path.endsWith(".png")){
            type="image/png";
        }else if (path.endsWith(".jpg")){
            type="image/jpeg";
        }
        
        Path file = Paths.get("./www" + path);
        Charset charset = Charset.forName("UTF-8");
        String outmsg = "";
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
               
                outmsg = outmsg + "\r\n" + line;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: "+ type + "\r\n"
                        + "\r\n"
                        + outmsg;
    }
    
    
}
