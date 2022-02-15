package com.free.commit.entity.encrypt;

import com.free.commit.api.environment.Environment;
import com.free.commit.configuration.environment.Variable;
import com.free.commit.configuration.response.Message;
import com.free.commit.exception.HttpInternalServerErrorException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EncryptField implements AttributeConverter< String, String > {


    protected final static String      ALGORITHM = "AES";
    protected final        Environment environment;
    protected              Cipher      cipher;
    protected              Key         key;


    public EncryptField( Environment environment ) {
        this.environment = environment;
    }


    @Override
    public String convertToDatabaseColumn( String data ) {
        if ( data == null ) {
            return null;
        }

        try {
            load();
        } catch ( NoSuchPaddingException | NoSuchAlgorithmException | NullPointerException e ) {
            throw new HttpInternalServerErrorException( Message.INVALID_ENCRYPTION_KEY );
        }

        try {
            cipher.init( Cipher.ENCRYPT_MODE, key );
            return Base64.getEncoder().encodeToString( cipher.doFinal( data.getBytes() ) );
        } catch ( InvalidKeyException | BadPaddingException | IllegalBlockSizeException e ) {
            e.printStackTrace();
            throw new HttpInternalServerErrorException( "An error occurred" );
        }
    }


    @Override
    public String convertToEntityAttribute( String data ) {
        if ( data == null ) {
            return null;
        }
        
        try {
            load();
        } catch ( NoSuchPaddingException | NoSuchAlgorithmException | NullPointerException e ) {
            throw new HttpInternalServerErrorException( Message.INVALID_ENCRYPTION_KEY );
        }

        try {
            cipher.init( Cipher.DECRYPT_MODE, key );
            return new String( cipher.doFinal( Base64.getDecoder().decode( data ) ) );
        } catch ( InvalidKeyException | BadPaddingException | IllegalBlockSizeException e ) {
            throw new HttpInternalServerErrorException( "An error occurred" );
        }
    }


    private void load() throws NoSuchPaddingException, NoSuchAlgorithmException {
        if ( cipher == null ) {
            cipher = Cipher.getInstance( ALGORITHM );
        }

        if ( key == null ) {
            key = new SecretKeySpec( environment.getEnv( Variable.ENCRYPTION_KEY ).getBytes(), ALGORITHM );
        }
    }
}
