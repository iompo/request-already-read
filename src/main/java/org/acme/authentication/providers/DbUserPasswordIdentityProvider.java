package org.acme.authentication.providers;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.smallrye.mutiny.Uni;
import org.acme.authentication.RequestAuthenticator;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Alternative
@Priority(1)
@ApplicationScoped
public class DbUserPasswordIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {

    @Inject
    EntityManager entityManager;

    private RequestAuthenticator requestAuthenticator;

    @PostConstruct
    public void post() {
        requestAuthenticator = new RequestAuthenticator(entityManager.getEntityManagerFactory());
    }

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request, AuthenticationRequestContext context) {
        return context.runBlocking(() -> requestAuthenticator.authenticateUserPassword(request));
    }
}
