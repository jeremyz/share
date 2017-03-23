package ch.asynk;

import java.util.List;

public abstract class AbstractDAOOperations<TObject, TIdentity, TMapper extends AbstractMapper<TObject, TIdentity>> extends AbstractDAO<TObject, TMapper>
{
    public int insert(final TObject obj)
    {
        return execInt(true, new CallBack<Integer, TMapper>() {
            public Integer call(TMapper m) { return m.insert(obj); }
        });
    }

    public int update(final TObject obj)
    {
        return execInt(true, new CallBack<Integer, TMapper>() {
            public Integer call(TMapper m) { return m.update(obj); }
        });
    }

    public int delete(final TObject obj)
    {
        return execInt(true, new CallBack<Integer, TMapper>() {
            public Integer call(TMapper m) { return m.delete(obj); }
        });
    }

    public int count()
    {
        return execInt(false, new CallBack<Integer, TMapper>() {
            public Integer call(TMapper m) { return m.count(); }
        });
    }

    public List<TObject> select()
    {
        return execObjects(false, new CallBack<List<TObject>, TMapper>() {
            public List<TObject> call(TMapper m) { return m.select(); }
        });
    }

    public TObject selectOne(final TIdentity id)
    {
        return execObject(false, new CallBack<TObject, TMapper>() {
            public TObject call(TMapper m) { return m.selectOne(id); }
        });
    }
}
