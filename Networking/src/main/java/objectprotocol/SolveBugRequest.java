package objectprotocol;

import com.model.Bug;

public class SolveBugRequest implements Request{
    Bug bug;

    public SolveBugRequest(Bug bug) {
        this.bug = bug;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
}
