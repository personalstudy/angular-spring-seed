package org.personal.mason.ass.config;


import org.personal.mason.ass.common.jpa.AssRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by mason on 7/8/14.
 */
@Configuration
//@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableJpaRepositories(value = {"org.personal.mason.ass.domain.repository"},
        repositoryFactoryBeanClass = AssRepositoryFactoryBean.class)
public class DataSourceConfig {
    @Autowired
    Environment env;

    @Autowired
    private DataSource dataSource;

    @Profile({"local", "dev"})
    static class Local {
        @Autowired
        Environment env;

        @Bean
        public DataSource dataSource(){
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            try {
                dataSource.setDriverClassName(env.getProperty("db.mysql.driverClassName"));
                dataSource.setUrl(env.getProperty("db.mysql.url"));
                dataSource.setUsername(env.getProperty("db.mysql.username"));
                dataSource.setPassword(env.getProperty("db.mysql.password"));
//                dataSource.setMaxActive(Integer.parseInt(env.getProperty("db.mysql.maxActive")));
//                dataSource.setMaxIdle(Integer.parseInt(env.getProperty("db.mysql.maxIdle")));
//                dataSource.setMaxWait(Integer.parseInt(env.getProperty("db.mysql.maxWait")));
//                dataSource.setDefaultAutoCommit(Boolean.parseBoolean(env.getProperty("db.mysql.defaultAutoCommit")));
            }catch (Exception e){
                e.printStackTrace();
            }
            return dataSource;
        }
    }

    @Profile({ "prod" })
    @Resource(name="jdbc/FLDB")
    static class Prod {
        @Bean
        public DataSource dataSource() {
            final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
            dsLookup.setResourceRef(true);
            DataSource dataSource = dsLookup.getDataSource("java:comp/dnv/jdbc/FLDB");
            return dataSource;
        }
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(Boolean.parseBoolean(env.getProperty("jpa.ddl.generate")));
        jpaVendorAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("jpa.sql.show")));
        jpaVendorAdapter.setDatabasePlatform(env.getProperty("jpa.dialect"));
        jpaVendorAdapter.setDatabase(Database.valueOf(env.getProperty("database.name")));
        return jpaVendorAdapter;
    }

    @Bean
    public AbstractEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(jpaVendorAdapter());
        emf.setMappingResources("META-INF/orm.xml");
        emf.setPersistenceUnitName(env.getProperty("persistent.unit.name"));
        emf.setPackagesToScan(env.getProperty("packages.to.scan"));
        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor postProcessor(){
        return new PersistenceExceptionTranslationPostProcessor();
    }


}
