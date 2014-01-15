package org.openmrs.module.atomfeed.common.repository;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.openmrs.api.context.ServiceContext;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

public class OpenMRSJdbcConnectionProvider implements JdbcConnectionProvider {

    @Override
    public Connection getConnection() throws SQLException {
        return getSession().connection();
    }

    private Session getSession() {
        ServiceContext serviceContext = ServiceContext.getInstance();
        Class klass = serviceContext.getClass();
        try {
            Field field = klass.getDeclaredField("applicationContext");
            field.setAccessible(true);
            ApplicationContext applicationContext = (ApplicationContext) field.get(serviceContext);
            SessionFactory factory = (SessionFactory) applicationContext.getBean("sessionFactory");
            return factory.getCurrentSession();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
    }
}
