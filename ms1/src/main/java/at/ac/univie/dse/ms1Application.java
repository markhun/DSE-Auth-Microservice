package at.ac.univie.dse;

import at.ac.univie.dse.auth.BasicAuthenticator;
import at.ac.univie.dse.auth.JWTAuthenticator;
import at.ac.univie.dse.core.User;
import at.ac.univie.dse.db.UserDAO;
import at.ac.univie.dse.resources.AuthenticationResource;
import at.ac.univie.dse.resources.AuthorizationResource;
import at.ac.univie.dse.resources.DebugResource;
import at.ac.univie.dse.resources.UserResource;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.PolymorphicAuthDynamicFeature;
import io.dropwizard.auth.PolymorphicAuthValueFactoryProvider;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


public class ms1Application extends Application<ms1Configuration> {


    public static Key key;
    public static String tokenType;
    public static boolean debugMode;

    public static void main(final String[] args) throws Exception {
        new ms1Application().run(args);
    }

    public static final HibernateBundle<ms1Configuration> hibernate = new HibernateBundle<ms1Configuration>(User.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ms1Configuration configuration) {
            return configuration.getDatabaseAppDataSourceFactory();
        }
    };

    public static final MigrationsBundle<ms1Configuration> migration = new MigrationsBundle<ms1Configuration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(ms1Configuration configuration) {
            return configuration.getDatabaseAppDataSourceFactory();
        }
    };

    @Override
    public String getName() {
        return "ms1";
    }

    @Override
    public void initialize(final Bootstrap<ms1Configuration> bootstrap) {
        bootstrap.addBundle(migration);
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final ms1Configuration configuration, final Environment environment) throws ClassNotFoundException, UnsupportedEncodingException, NoSuchAlgorithmException {
        tokenType = configuration.getTokenType();
        key = new HmacKey( configuration.getTokenSecret() );
        debugMode = configuration.IsDebugging();

        // just to be sure where it happens, let's just create the singleton of our DAO here for now.
        final UserDAO userDAO = UserDAO.getInstance();

        // register resrouces here:
        environment.jersey().register(new AuthenticationResource());
        environment.jersey().register(new AuthorizationResource());
        environment.jersey().register(new UserResource());
        if(debugMode)
            environment.jersey().register(new DebugResource());

        // Please see "Transactional Resource Methods Outside Jersey Resources" at
        // http://www.dropwizard.io/1.2.2/docs/manual/hibernate.html for why we need this Proxy
        BasicAuthenticator proxyBasicAuthenticator = new UnitOfWorkAwareProxyFactory(hibernate).create(BasicAuthenticator.class, UserDAO.class, UserDAO.getInstance());
        // register AuthFilter for BasicAuthentication here:
        BasicCredentialAuthFilter<PrincipalImpl> basicFilter = new BasicCredentialAuthFilter.Builder<PrincipalImpl>()
                .setAuthenticator(proxyBasicAuthenticator)
                .setRealm("BasicAuth")
                .setPrefix("Basic")
                .buildAuthFilter();

        // register AuthFilter for JWTAuthentication here:
        // see example of dropwizard-auth-jwt at
        // https://github.com/ToastShaman/dropwizard-auth-jwt/blob/master/src/test/java/com/github/toastshaman/dropwizard/auth/jwt/example/JwtAuthApplication.java
        JwtConsumerBuilder consumerBuilder = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject(); // the subject filed has to hold the UserID
        // if we user encrypted tokens our consumer also needs a decryption key
        if (tokenType.equals("jwe")) {
            consumerBuilder.setDecryptionKey(key);
        }
        consumerBuilder.setVerificationKey(key);

        final JwtConsumer consumer = consumerBuilder.build();

        // Please see "Transactional Resource Methods Outside Jersey Resources" at
        // http://www.dropwizard.io/1.2.2/docs/manual/hibernate.html for why we need this Proxy
        JWTAuthenticator proxyJWTAuthenticator = new UnitOfWorkAwareProxyFactory(hibernate).create(JWTAuthenticator.class, UserDAO.class, UserDAO.getInstance());
        AuthFilter<JwtContext, User> jwtFilter = new JwtAuthFilter.Builder<User>()
                .setJwtConsumer(consumer)
                .setRealm("JWTAuth")
                .setPrefix("Bearer")
                .setAuthenticator(proxyJWTAuthenticator)
                .buildAuthFilter();

        // Using PolymorphicAuthDynamicFeature allows us to provide different auth filters based on binding classes.
        // see https://dropwizard.readthedocs.io/en/latest/manual/auth.html
        //
        // As we need a "point of entry" into our microservice app - an initial login - where a User or orchestrating
        // microservice can ask for their first token it makes sense to provide a authentication method not based on
        // JWTs.
        // Currently this is provided by utilizing the http standard of basic access authentication
        // https://en.wikipedia.org/wiki/Basic_access_authentication
        // TODO: This isn't a secure or sophisticated method for an initial login !
        // An orchestrating microservice should generate a JWT with username and password, signed with our pre-shared key
        // and exchange this "login-token" for a real Bearer Token at this auth-microservice.
        final PolymorphicAuthDynamicFeature feature = new PolymorphicAuthDynamicFeature<>(
                        ImmutableMap.of(
                                PrincipalImpl.class, basicFilter,
                                User.class, jwtFilter));
        final AbstractBinder binder = new PolymorphicAuthValueFactoryProvider.Binder<>(
                ImmutableSet.of(PrincipalImpl.class, User.class));


        environment.jersey().register(feature);
        environment.jersey().register(binder);


    }

}
