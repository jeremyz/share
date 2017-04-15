package ch.asynk;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.exceptions.PersistenceException;

public abstract class AbstractDAO<TObject, TMapper>
{
    public final int DAO_ERROR = -1;
    protected Class<TMapper> mapperClass = null;

    public static boolean logErrors = true;
    private static SqlConnection conn = null;

    static {
        conn = new SqlConnection();
        MapperRegistration.registerMappers(conn);
    }

    static public SqlConnection getSqlConnection() { return conn; }

    @FunctionalInterface
    public interface CallBack<TRet, TMapper>
    {
        public TRet call(TMapper m);
    }

    protected int execInt(CallBack<Integer, TMapper> cb)
    {
        return execInt(cb, false);
    }

    protected int execInt(CallBack<Integer, TMapper> cb, boolean commit)
    {
        int status = DAO_ERROR;
        SqlSession session = conn.openSqlSession();
        try {
            status = cb.call(session.getMapper(mapperClass));
            if (commit) session.commit();
        }
        catch(PersistenceException e) {
            if (commit) session.rollback();
            if (logErrors) conn.error(e);
        }
        finally { session.close(); }
        return status;
    }

    protected TObject execObject(CallBack<TObject, TMapper> cb)
    {
        TObject obj = null;
        SqlSession session = conn.openSqlSession();
        try {
            obj = cb.call(session.getMapper(mapperClass));
        }
        catch(PersistenceException e) {
            if (logErrors) conn.error(e);
        }
        finally { session.close(); }
        return obj;
    }

    protected List<TObject> execObjects(CallBack<List<TObject>, TMapper> cb)
    {
        List<TObject> list = null;
        SqlSession session = conn.openSqlSession();
        try {
            list = cb.call(session.getMapper(mapperClass));
        }
        catch(PersistenceException e) {
            if (logErrors) conn.error(e);
        }
        finally { session.close(); }
        return list;
    }
}
