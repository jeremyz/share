package ch.asynk;

import java.util.List;

public interface AbstractMapper<TObject, TIdentity>
{
    int insert(TObject obj);

    int update(TObject obj);

    int delete(TObject obj);

    int count();

    List<TObject> select();

    TObject selectOne(TIdentity id);
}
