package com.sabrina.ldapapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @author Sabrina
 *
 */
@Configuration
public class LdapapiConfig {
	@Value("${spring.ldap.basedn}")
    private String baseDn;
	@Value("${spring.ldap.url}")
    private String url;
	@Value("${spring.ldap.userdn}")
    private String userDn;
	@Value("${spring.ldap.password}")
    private String password;

    @Bean
    public LdapContextSource  contextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(url);
		contextSource.setBase(baseDn);
		contextSource.setUserDn(userDn);
		contextSource.setPassword(password);
		contextSource.afterPropertiesSet();
		return contextSource;
	}
    
    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }    

}
