package se.vgregion.arbetsplatskoder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.repository.UserRepository;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.util.HashSet;
import java.util.Properties;

/**
 * @author Patrik Björk
 */
@Service
public class LdapLoginService {

    @Autowired
    private UserRepository userRepository;

    @Value("${ldap.service.user.dn}")
    private String serviceUserDN;

    @Value("${ldap.service.user.password}")
    private String serviceUserPassword;

    @Value("${ldap.search.base}")
    private String base;

    @Value("${ldap.url}")
    private String ldapUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapLoginService.class);

    public User login(String username, String password) throws FailedLoginException {
        return login(username, password, true);
    }

    public User loginWithoutPassword(String username) throws FailedLoginException {
        return login(username, null, false);
    }

    private User login(String username, String password, boolean verifyPassword) throws FailedLoginException {
        username = username.trim().toLowerCase();

        // first create the service context
        DirContext serviceCtx = null;
        try {
            // use the service user to authenticate
            serviceCtx = createInitialDirContext();

            NamingEnumeration<SearchResult> results = findUser(username, serviceCtx);

            if (results.hasMore()) {
                // get the users DN (distinguishedName) from the result
                SearchResult result = results.next();
                String distinguishedName = result.getNameInNamespace();

                if (verifyPassword) {
                    verifyPassword(password, distinguishedName);
                }

                User user = new User();
                user.setId(username);
                user.setFirstName((String) (result).getAttributes().get("givenName").get());
                user.setLastName((String) (result).getAttributes().get("sn").get());
                user.setMail((String) (result).getAttributes().get("mail").get());
                user.setDisplayName((String) (result).getAttributes().get("displayName").get());

                // The first user to register will be admin.
                if (userRepository.findAll().size() == 0) {
                    user.setRole(Role.ADMIN);
                } else {
                    user.setRole(Role.USER);
                }

                user = syncUser(user, result);

                return user;
            } else {
                throw new AccountNotFoundException("Nu user found with given username.");
            }
        } catch (CommunicationException e) {
            throw new RuntimeException(e);
        } catch (AccountNotFoundException e) {
            throw new FailedLoginException();
        } catch (Exception e) {
            throw new FailedLoginException();
        } finally {
            if (serviceCtx != null) {
                try {
                    serviceCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void verifyPassword(String password, String distinguishedName) throws NamingException {
        // attempt another authentication, now with the user
        Properties authEnv = new Properties();
        authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        authEnv.put(Context.PROVIDER_URL, ldapUrl);
        authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
        authEnv.put(Context.SECURITY_CREDENTIALS, password);
        new InitialDirContext(authEnv);
    }

    private NamingEnumeration<SearchResult> findUser(String username, DirContext serviceCtx) throws NamingException {
        String identifyingAttribute = "cn";
        String identifier = username;

        // we don't need all attributes, just let it get the identifying one
        String[] attributeFilter = {identifyingAttribute, "givenName", "mail", "sn", "displayName"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attributeFilter);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // use a search filter to find only the user we want to authenticate
        String searchFilter = "(" + identifyingAttribute + "=" + identifier + ")";
        return serviceCtx.search(base, searchFilter, sc);
    }

    private InitialDirContext createInitialDirContext() throws NamingException {
        Properties serviceEnv = new Properties();
        serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
        serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        serviceEnv.put(Context.SECURITY_PRINCIPAL, serviceUserDN);
        serviceEnv.put(Context.SECURITY_CREDENTIALS, serviceUserPassword);
        return new InitialDirContext(serviceEnv);
    }

    private User syncUser(User user, SearchResult result) throws NamingException {

        User foundUser = userRepository.findOne(user.getId());

        if (foundUser != null) {
            // Keep role...
            user.setRole(foundUser.getRole());

            // ... but update these.
            user.setProdn1s(new HashSet<>(foundUser.getProdn1s()));
            user.setFirstName((String) (result).getAttributes().get("givenName").get());
            user.setLastName((String) (result).getAttributes().get("sn").get());
            user.setMail((String) (result).getAttributes().get("mail").get());
            user.setDisplayName((String) (result).getAttributes().get("displayName").get());

            return userRepository.save(user);
        } else {
            return userRepository.save(user);
        }
    }

}
