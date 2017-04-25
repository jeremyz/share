package ch.asynk;

public interface AbstractModel<TIdentity, TObject>
{
    boolean isUnsaved();

    TObject self();

    AbstractMapper<TIdentity, TObject> getDAO();

    default void save() { getDAO().save(self()); }

    default int delete() { return getDAO().delete(self()); }

    default void reload() { feedFrom(getDAO().selectSelf(self())); }

    void feedFrom(TObject obj);
}

