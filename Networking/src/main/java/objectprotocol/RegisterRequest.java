package objectprotocol;

import com.model.Utilizator;

public class RegisterRequest implements Request{

    Utilizator user;

    public RegisterRequest(Utilizator user) {
        this.user = user;
    }

    public Utilizator getUser() {
        return user;
    }

    public void setUser(Utilizator user) {
        this.user = user;
    }
}
