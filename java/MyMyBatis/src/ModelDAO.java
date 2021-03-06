package ch.asynk;

import java.util.List;

public class ModelDAO extends AbstractDAOOperations<Integer, Model, ModelMapper> implements ModelMapper
{
    private static final ModelDAO instance = new ModelDAO();

    private ModelDAO() { mapperClass = ModelMapper.class; }

    public static ModelDAO getInstance() { return instance; }
}
