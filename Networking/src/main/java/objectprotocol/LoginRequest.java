package objectprotocol;

import com.model.Utilizator;
import com.services.IObserver;

public class LoginRequest implements Request{
    Utilizator user;

    IObserver client;
    public LoginRequest(Utilizator user, IObserver client) {
        this.user = user;
        this.client=client;
    }

    public Utilizator getUser() {
        return user;
    }

    public IObserver getClient() {
        return client;
    }
}
