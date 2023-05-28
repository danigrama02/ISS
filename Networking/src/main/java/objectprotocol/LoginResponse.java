package objectprotocol;

import com.model.Utilizator;

public class LoginResponse implements Response{

    private Utilizator user;

    public LoginResponse(Utilizator user) {
        this.user = user;
    }

    public Utilizator getUser() {
        return user;
    }

    public void setUser(Utilizator user) {
        this.user = user;
    }
}
