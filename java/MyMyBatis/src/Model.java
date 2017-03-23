package ch.asynk;

import lombok.Setter;
import lombok.Getter;

public class Model
{
    private @Setter @Getter Integer id;
    private @Setter @Getter String name;

    public Model(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return String.format("model [%d] %s", id, name);
    }
}
