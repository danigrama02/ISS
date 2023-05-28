package com.server;


import com.model.Bug;
import com.model.Utilizator;
import com.networking.utils.AbstractServer;
import com.networking.utils.ObjectConcurrentServer;
import com.networking.utils.ServerException;
import com.persistance.RepoBuguri;
import com.persistance.RepoUtilizatori;
import com.persistance.Repository;
import com.services.IService;
import java.io.IOException;
import java.util.Properties;


public class StartObjectServer {
    private static int defaultPort=6969;
    public static void main(String[] args) {

       Properties serverProps=new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        Repository<String , Utilizator> repoUtilizatori = new RepoUtilizatori();
        Repository<Integer, Bug> bugRepo = new RepoBuguri();
        IService serverImpl=new ServiceImpl(repoUtilizatori,bugRepo,serverProps.getProperty("host"),6969);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new ObjectConcurrentServer(chatServerPort, serverImpl);
        try {
                server.start();
        } catch (ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
