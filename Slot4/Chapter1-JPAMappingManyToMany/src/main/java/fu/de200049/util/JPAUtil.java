package fu.de200049.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("hsf302FU");

    public static EntityManagerFactory getEntityManagerFactory() {
        return EMF;
    }

    public static void close() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}