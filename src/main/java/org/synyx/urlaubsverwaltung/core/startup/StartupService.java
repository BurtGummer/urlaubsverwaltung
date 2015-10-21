
package org.synyx.urlaubsverwaltung.core.startup;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import org.synyx.urlaubsverwaltung.security.Authentication;

import javax.annotation.PostConstruct;


/**
 * This service is executed every time the application is started to log information about the application configuration
 * like database user and url.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
@Service
public class StartupService {

    private static final Logger LOG = Logger.getLogger(StartupService.class);

    private final String dbUser;
    private final String dbUrl;
    private final String authentication;
    private final String environment;

    @Autowired
    public StartupService(@Value("${db.username}") String dbUser,
        @Value("${db.url}") String dbUrl) {

        this.dbUser = dbUser;
        this.dbUrl = dbUrl;
        this.authentication = System.getProperty(Authentication.PROPERTY_KEY);
        this.environment = System.getProperty(Environment.PROPERTY_KEY);
    }

    @PostConstruct
    public void logStartupInfo() {

        LOG.info("DATABASE=" + dbUrl);
        LOG.info("DATABASE USER=" + dbUser);
        LOG.info("AUTHENTICATION=" + authentication);
        LOG.info("ENVIRONMENT=" + environment);
    }
}
