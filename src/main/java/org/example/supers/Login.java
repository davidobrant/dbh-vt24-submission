package org.example.supers;

import org.example.repositories.BaseRepository;


public class Login <T> extends Menu {

    protected T currentUser;
    protected Controls controls;
    public Login() { super(); }

    public boolean login(Class<T> entityClass) {
        try {
            p.menu("LOGIN");
            var entityId = p.promptInt(entityClass.getSimpleName() + " ID");
            if (entityId == -1) return false;
            this.currentUser = new BaseRepository().findById(entityClass, entityId);
            if (currentUser == null) throw new Exception("Login failed...");
            return true;
        } catch (Exception e) {
            p.printError(e.getMessage());
            login(entityClass);
        }
        return false;
    }
    public void logout() {
        super.exit();
        this.currentUser = null;
    }

}
