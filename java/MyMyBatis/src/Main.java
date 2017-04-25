package ch.asynk;

import java.util.List;

public class Main
{
    private static void failIf(boolean fail, String msg)
    {
        if (fail) {
            System.err.println("FAIL " + msg);
            System.exit(1);
        }
    }

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

        int count = dao.count();
        failIf(count != 2, "count()");
        System.out.println("count  : " + count);
        models = dao.select();
        count = models.size();
        failIf(count != 2, "select()");
        System.out.println("select : ");
        for (Model _m : models) System.out.println(" - " +_m.toString());

        m = dao.selectOne(2);
        failIf(m == null , "selectOne()");
        System.out.println("selectOne : " + m.toString());
        System.out.println("update ...");
        m.setName("adios");
        m.save();
        m.setName("wrong");
        m.reload();
        failIf(m == null , "reload()");
        failIf(!m.getName().equals("adios") , "selectSelf()");
        System.out.println("selectSelf : " + m.toString());
        count = m.delete();
        failIf(count != 1, "delete()");
        System.out.println("delete : " + count);
        count = dao.count();
        failIf(count != 1, "count()");
        System.out.println("count  : " + count);
        System.out.println("insert ...");
        m = new Model("HoMySatan");
        m.save();
        failIf(count != 1, "insert()");
        count = dao.count();
        failIf(count != 2, "count()");
        System.out.println("count  : " + count);
        System.out.println("select : ");
        models = dao.select();
        count = models.size();
        failIf(count != 2, "select()");
        for (Model _m : models) System.out.println(" - " +_m.toString());
    }
}
