package org.acme.authentication;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TrustedAuthenticationRequest;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import org.acme.AppUser;
import org.hibernate.FlushMode;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class RequestAuthenticator {

    private final EntityManagerFactory entityManagerFactory;

    public RequestAuthenticator(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public SecurityIdentity authenticateUserPassword(UsernamePasswordAuthenticationRequest userPasswordRequest) {
        EntityManager entityManager = getReadOnlyEntityManager();
        try {
            AppUser user = getUserFromUsername(entityManager, userPasswordRequest.getUsername());
            return emitUserIdentity(user, userPasswordRequest.getPassword().getPassword());
        } finally {
            entityManager.close();
        }
    }

    public SecurityIdentity authenticateTrusted(TrustedAuthenticationRequest trustedRequest) {
        EntityManager entityManager = getReadOnlyEntityManager();
        try {
            AppUser user = getUserFromUsername(entityManager, trustedRequest.getPrincipal());
            return getIdentityFrom(user);
        } finally {
            entityManager.close();
        }
    }

    private SecurityIdentity emitUserIdentity(AppUser user, char[] password) {
        if (!user.getPassword().equals(String.valueOf(password))) {
            throw new AuthenticationFailedException("Invalid credentials");
        }
        return getIdentityFrom(user);
    }

    private AppUser getUserFromUsername(EntityManager entityManager, String username) {
        TypedQuery<AppUser> query = entityManager.createQuery("SELECT u from AppUser u where u.username = :username", AppUser.class);
        query.setParameter("username", username);
        List<AppUser> users = query.getResultList();
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            throw new AuthenticationFailedException("Invalid credentials");
        }
    }

    private EntityManager getReadOnlyEntityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ((Session) entityManager).setHibernateFlushMode(FlushMode.MANUAL);
        ((Session) entityManager).setDefaultReadOnly(true);

        return entityManager;
    }

    private QuarkusSecurityIdentity getIdentityFrom(AppUser user) {
        return QuarkusSecurityIdentity.builder()
                .addRole("user")
                .setPrincipal(new QuarkusPrincipal(user.getUsername()))
                .build();
    }

}
