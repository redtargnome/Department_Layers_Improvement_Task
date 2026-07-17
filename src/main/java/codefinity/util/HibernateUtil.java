package codefinity.util;

import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor
public class HibernateUtil {
    private static final SessionFactory instance = initSessionFactory();

    private static SessionFactory initSessionFactory() {
        Configuration configuration = new Configuration().configure();
        applyEnvironmentOverride(configuration, "DB_URL", "hibernate.connection.url");
        applyEnvironmentOverride(configuration, "DB_USERNAME", "hibernate.connection.username");
        applyEnvironmentOverride(configuration, "DB_PASSWORD", "hibernate.connection.password");
        return configuration.buildSessionFactory();
    }

    private static void applyEnvironmentOverride(Configuration configuration,
                                                 String environmentVariable,
                                                 String hibernateProperty) {
        String value = System.getenv(environmentVariable);
        if (value != null && !value.isBlank()) {
            configuration.setProperty(hibernateProperty, value);
        }
    }

    public static SessionFactory getSessionFactory() {
        return instance;
    }
}
