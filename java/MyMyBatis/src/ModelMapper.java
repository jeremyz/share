package ch.asynk;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Select;

public interface ModelMapper extends AbstractMapper<Model, Integer>
{
    final static String attrs = " id, name ";
    final static String where = " where id=#{id} ";


    final static String INSERT = "insert into models(name) values (#{name})";
    @Insert(INSERT)
    int insert(Model obj);

    final static String UPDATE = "update models set name=#{name}" + where;
    @Update(UPDATE)
    int update(Model obj);

    final static String DELETE = "delete from models" + where;
    @Delete(DELETE)
    int delete(Model obj);

    final static String COUNT = "select count(*) from models";
    @Select(COUNT)
    int count();

    final static String SELECT = "select" + attrs + "from models";
    @Select(SELECT)
    List<Model> select();

    final static String SELECT_ONE = "select" + attrs + "from models" + where;
    @Select(SELECT_ONE)
    Model selectOne(Integer id);
}
