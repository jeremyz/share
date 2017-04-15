package ch.asynk;

import java.util.List;

public abstract class AbstractDAOOperations<TObject, TIdentity, TMapper extends AbstractMapper<TObject, TIdentity>> extends AbstractDAO<TObject, TMapper>
{

    public int insert(final TObject obj)
    {
        return execInt(true, m -> m.insert(obj));
    }

    public int update(final TObject obj)
    {
        return execInt(true, m -> m.update(obj));
    }

    public int delete(final TObject obj)
    {
        return execInt(true, m -> m.delete(obj));
    }

    public int count()
    {
        return execInt(false, m -> m.count());
    }

    public List<TObject> select()
    {
        return execObjects(false, m -> m.select());
    }

    public TObject selectOne(final TIdentity id)
    {
        return execObject(false, m -> m.selectOne(id));
    }
}
