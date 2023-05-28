package objectprotocol;

import com.model.Bug;

import java.util.List;

public class BugResponse implements Response
{
    private List<Bug> bugs;

    public BugResponse(List<Bug> bugs) {
        this.bugs = bugs;
    }

    public List<Bug> getBugs()
    {
        return bugs;
    }
}
