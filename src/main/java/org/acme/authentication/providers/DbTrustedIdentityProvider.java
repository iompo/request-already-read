package org.acme.authentication.providers;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TrustedAuthenticationRequest;
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
public class DbTrustedIdentityProvider implements IdentityProvider<TrustedAuthenticationRequest> {

    @Inject
    EntityManager entityManager;

    private RequestAuthenticator requestAuthenticator;

    @PostConstruct
    public void post() {
        requestAuthenticator = new RequestAuthenticator(this.entityManager.getEntityManagerFactory());
    }

    @Override
    public Class<TrustedAuthenticationRequest> getRequestType() {
        return TrustedAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(TrustedAuthenticationRequest request, AuthenticationRequestContext context) {
        return context.runBlocking(() -> requestAuthenticator.authenticateTrusted(request));
    }
}
