package objectprotocol;

import com.model.Utilizator;
import com.networking.utils.ServerException;
import com.services.IObserver;
import com.services.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientObjectWorker implements Runnable, IObserver {

    private IService server;

    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;

        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    private Response handleRequest(Request request){
        Response response=null;

        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq=(LoginRequest)request;
            Utilizator user=logReq.getUser();
            try {
                Utilizator user1 = server.login(user.getUsername(), user.getPassword());
                server.setClient(this);
                return new LoginResponse(user1);
            } catch (ServerException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof BugRequest){
            System.out.println("ConcertRequest ...");
            BugRequest senReq=(BugRequest) request;
            try {
                return new BugResponse(server.getAllBugs());
            } catch (ServerException e) {
                return new ErrorResponse(e.getMessage());
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof AddRequest){
            System.out.println("Add Request ...");
            AddRequest req=(AddRequest) request;
            try {
                server.addBug(req.getBug());
                //server.notifyClientsLoggedIn();
               // return new UpdateBugResponse();
                return new OkResponse();
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof RegisterRequest)
        {
            System.out.println("Register Request...");
            RegisterRequest req = (RegisterRequest) request;
            try{
                server.register(req.getUser());
                return new OkResponse();
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof SolveBugRequest)
        {
            System.out.println("solving bug");
            SolveBugRequest req = (SolveBugRequest) request;
            try{
                server.updateBug(req.getBug());
                return new OkResponse();
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        return response;
    }


    @Override
    public void update() {
        try {
            sendResponse(new UpdateBugResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
