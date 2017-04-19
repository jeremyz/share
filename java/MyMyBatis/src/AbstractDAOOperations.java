package ch.asynk;

import java.util.List;

public abstract class AbstractDAOOperations<TIdentity,
       TObject extends AbstractModel<TIdentity, TObject>,
       TMapper extends AbstractMapper<TIdentity, TObject>> extends AbstractDAO<TObject, TMapper>
{

    public int insert(final TObject obj)
    {
        return execInt(m -> m.insert(obj), true);
    }

    public int update(final TObject obj)
    {
        return execInt(m -> m.update(obj), true);
    }

    public int save(final TObject obj)
    {
        return (obj.isUnsaved() ? insert(obj) : update(obj));
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
