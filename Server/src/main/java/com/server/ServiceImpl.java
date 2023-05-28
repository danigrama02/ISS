package com.server;


import com.model.Bug;
import com.model.Utilizator;
import com.persistance.Repository;

import com.services.IObserver;
import com.services.IService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImpl implements IService {

    private Repository<String, Utilizator> userRepo;
    private Repository<Integer, Bug> bugRepo;

    private String host;
    private Integer port;

    private Deque<IObserver> loggedClients;

    private final int defaultThreadsNo=5;

    public ServiceImpl(Repository<String, Utilizator> userRepository, Repository<Integer, Bug> bugRepository, String host, Integer port) {
        this.userRepo = userRepository;
        this.bugRepo = bugRepository;
        this.host = host;
        this.port = port;
        loggedClients=new ConcurrentLinkedDeque<>();;

    }

    @Override
    public synchronized Utilizator login(String username, String password) throws Exception {
        Optional<Utilizator> user = userRepo.findOne(username);
        if (user.isEmpty()) {
            throw new Exception("Utilizator inexistent");
        }
        if (user.get().getPassword().equals(password)) {
            return user.get();
        } else throw new Exception("Parola Invalida");
    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public List<Bug> getAllBugs() throws Exception {
        List<Bug> bugs= new ArrayList<>();
        for (Bug bug : bugRepo.findAll()){
            bugs.add(bug);
        }
        return bugs;
    }

    @Override
    public synchronized void setClient(IObserver client) {
        loggedClients.add(client);
    }

    @Override
    public void notifyClientsLoggedIn() throws Exception {

        for(IObserver obs : loggedClients){
            obs.update();
            System.out.println(obs);
        }
    }

    @Override
    public synchronized void addBug(Bug bug) throws Exception {
        this.bugRepo.save(bug);
        notifyClientsLoggedIn();

    }

    @Override
    public synchronized void register(Utilizator user) {
        this.userRepo.save(user);
    }

    @Override
    public void updateBug(Bug bug) throws Exception {
        this.bugRepo.update(bug);
        notifyClientsLoggedIn();
    }


}