package ch.asynk;

import lombok.Setter;
import lombok.Getter;

public class Model implements AbstractModel<Integer>
{
    private @Setter @Getter Integer id;
    private @Setter @Getter String name;

    public Model()
    {
        this.id = null;
    }

    public Model(String name)
    {
        this.id = null;
        this.name = name;
    }

    @Override
    public boolean isUnsaved()
    {
        return (id == null);
    }

    @Override
    public String toString()
    {
        return String.format("model [%d] %s", id, name);
    }
}
