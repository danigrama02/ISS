package com.services;

import com.model.*;

import java.util.List;

public interface IService {


    public Utilizator login(String username, String password) throws Exception;

    public String getHost();
    public int getPort();

    public List<Bug> getAllBugs() throws Exception;

    public void setClient(IObserver client);

    public void notifyClientsLoggedIn() throws Exception;

    public void addBug(Bug bug) throws Exception;

    public void register(Utilizator user) throws Exception;

    public void updateBug(Bug bug) throws Exception;

}
