package com.free.commit.entity.encrypt;

import com.free.commit.configuration.environment.Variable;
import com.free.commit.configuration.response.Message;
import jakarta.persistence.AttributeConverter;
import org.romainlavabre.environment.Environment;
import org.romainlavabre.exception.HttpInternalServerErrorException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
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
            Cipher cipher = Cipher.getInstance( ALGORITHM );
            cipher.init( Cipher.ENCRYPT_MODE, key );
            return Base64.getEncoder().encodeToString( cipher.doFinal( data.getBytes() ) );
        } catch ( InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                  NoSuchPaddingException e ) {
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
            Cipher cipher = Cipher.getInstance( ALGORITHM );
            cipher.init( Cipher.DECRYPT_MODE, key );
            return new String( cipher.doFinal( Base64.getDecoder().decode( data ) ) );
        } catch ( InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                  NoSuchPaddingException e ) {
            throw new HttpInternalServerErrorException( "An error occurred" );
        }
    }


    private synchronized void load() throws NoSuchPaddingException, NoSuchAlgorithmException {
        if ( key == null ) {
            key = new SecretKeySpec( environment.getEnv( Variable.ENCRYPTION_KEY ).getBytes(), ALGORITHM );
        }
    }
}
