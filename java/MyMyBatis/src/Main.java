package ch.asynk;

import java.util.List;

public class Main
{
    public static void main(String [] args )
    {
        try {
            ModelDAO.getSqlConnection().runScript("create.sql");
        } catch (java.io.IOException e) {
            System.err.println(e.getMessage());
        }

        ModelDAO dao = ModelDAO.getInstance();
        Model m;
        List<Model> models;

        System.out.println("count  : " + dao.count());
        models = dao.select();
        System.out.println("select : ");
        for (Model _m : models) System.out.println(" - " +_m.toString());

        m = dao.selectOne(2);
        System.out.println("selectOne : " + m.toString());
        System.out.println("update ...");
        m.setName("adios");
        dao.update(m);
        m = dao.selectOne(2);
        System.out.println("selectOne : " + m.toString());
        System.out.println("delete : " + dao.delete(m));
        System.out.println("count  : " + dao.count());
        System.out.println("insert ...");
        m = new Model(-1, "HoMySatan");
        dao.insert(m);
        System.out.println("count  : " + dao.count());
        System.out.println("select : ");
        models = dao.select();
        for (Model _m : models) System.out.println(" - " +_m.toString());
    }
}
