package at.ac.univie.dse;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class ms1Configuration extends Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ms1Configuration.class);


    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty("dse2017")
    private Dse2017configuration dse2017 = new Dse2017configuration();

    @JsonProperty("dse2017")
    public Dse2017configuration getDse2017configuration() {
        return dse2017;
    }

    @JsonProperty("dse2017")
    public void setDse2017configuration(Dse2017configuration dse2017) {
        this.dse2017 = dse2017;
    }



    public DataSourceFactory getDatabaseAppDataSourceFactory() {
        return database;
    }

    byte[] getTokenSecret() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        LOGGER.debug("THIS IS IMPORTANT: Secret: {}", dse2017.getSecret() );
        return dse2017.getSecret().getBytes("UTF-8");

    }

    String getTokenType() {
        LOGGER.debug("TokenType is set to: {}", dse2017.getTokenType() );
        return dse2017.getTokenType();
    }

    boolean IsDebugging() {
        LOGGER.info("DEBUG MODE is activated!");
        return dse2017.getDebugMode();
    }

    public class Dse2017configuration {
        @NotEmpty
        @NotNull
        @JsonProperty
        private String secret;

        @NotEmpty
        @NotNull
        @JsonProperty
        private String tokenType;

        @JsonProperty
        private boolean debugMode;

        String getSecret() {
            return secret;
        }

        String getTokenType() {
            return tokenType;
        }

        boolean getDebugMode() { return debugMode; }
    }

}
