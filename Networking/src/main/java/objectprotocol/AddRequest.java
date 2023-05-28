package objectprotocol;

import com.model.Bug;

public class AddRequest implements Request{
    private Bug bug;

    public AddRequest(Bug bug) {
        this.bug = bug;
    }

    public Bug getBug() {
        return bug;
    }
}
