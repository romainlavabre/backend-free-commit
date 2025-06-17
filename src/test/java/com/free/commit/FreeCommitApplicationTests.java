package com.free.commit;

import ch.vorburger.exec.ManagedProcessException;
import com.free.commit.database.DatabaseProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles( { "test" } )
@DirtiesContext( classMode = DirtiesContext.ClassMode.BEFORE_CLASS )
class FreeCommitApplicationTests {


    @BeforeAll
    public static void init() throws ManagedProcessException {
        DatabaseProvider.initDB();
    }


    @Test
    void contextLoads() {
    }
}
