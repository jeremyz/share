package ch.asynk;

import org.apache.ibatis.session.Configuration;

public class MapperRegistration
{
    static public void registerMappers(SqlConnection conn)
    {
        conn.registerMapper("Model", Model.class, ModelMapper.class);
    }
}
