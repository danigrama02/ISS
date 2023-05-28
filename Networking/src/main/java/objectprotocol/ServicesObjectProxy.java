package objectprotocol;


import com.model.Bug;
import com.model.UserType;
import com.model.Utilizator;
import com.networking.utils.ServerException;
import com.services.IObserver;
import com.services.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesObjectProxy implements IService {

    private String host;
    private int port;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private IObserver client;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public List<Bug> getAllBugs() throws Exception {
        sendRequest(new BugRequest());
        Response response = readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServerException(err.getMessage()+"6969");
        }
        BugResponse resp = (BugResponse) response;
        System.out.println(resp.getBugs());
        return resp.getBugs();
    }

    public void setClient(IObserver client) {
        this.client = client;
        System.out.println(client.toString());
    }

    @Override
    public void notifyClientsLoggedIn() throws Exception {

    }

    @Override
    public void addBug(Bug bug) throws Exception{
        sendRequest(new AddRequest(bug));
        Response response = readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServerException(err.getMessage()+"6969");
        }
        OkResponse resp = (OkResponse) response;

    }

    @Override
    public void register(Utilizator user) throws ServerException {
        initializeConnection();
        sendRequest(new RegisterRequest(user));
        Response response = readResponse();
        if (response instanceof OkResponse)
        {
           return;
        }
        if(response instanceof ErrorResponse)
        {ErrorResponse err = (ErrorResponse) response;
            closeConnection();
            throw new ServerException(err.getMessage());
        }
    }

    @Override
    public void updateBug(Bug bug) throws ServerException {
        sendRequest(new SolveBugRequest(bug));
        Response response = readResponse();
        if (response instanceof OkResponse)
        {
            return;
        }
        if(response instanceof ErrorResponse)
        {ErrorResponse err = (ErrorResponse) response;
            closeConnection();
            throw new ServerException(err.getMessage());
        }
    }

    @Override
    public Utilizator login(String username, String password) throws Exception {
        initializeConnection();
        Utilizator user = new Utilizator(username, password,null, null);
        sendRequest(new LoginRequest(user,client));
        Response response = readResponse();
        if (response instanceof LoginResponse) {
            return ((LoginResponse) response).getUser();
        }
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            closeConnection();
            throw new ServerException(err.getMessage());
        }
        return null;
    }

    private Response readResponse() throws ServerException {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Request request) throws ServerException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServerException("Error sending object " + e);
        }

    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initializeConnection() throws ServerException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(UpdateResponse update){
        if (update instanceof UpdateBugResponse){

            //UpdateConcertsResponse conUpd=(UpdateConcertsResponse) update;
            System.out.println("Bugs to Update in ");
            try {qresponses.clear();
                client.update();

                System.out.println("Bugs to Update in ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        /*responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses){
                            responses.notify();
                        }*/
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

}
