package ch.asynk;

import java.util.List;

public abstract class AbstractDAOOperations<TObject, TIdentity, TMapper extends AbstractMapper<TObject, TIdentity>> extends AbstractDAO<TObject, TMapper>
{

    public int insert(final TObject obj)
    {
        return execInt(m -> m.insert(obj), true);
    }

    public int update(final TObject obj)
    {
        return execInt(m -> m.update(obj), true);
    }

    public int delete(final TObject obj)
    {
        return execInt(m -> m.delete(obj), true);
    }

    public int count()
    {
        return execInt(m -> m.count());
    }

    public List<TObject> select()
    {
        return execObjects(m -> m.select());
    }

    public TObject selectOne(final TIdentity id)
    {
        return execObject(m -> m.selectOne(id));
    }
}
