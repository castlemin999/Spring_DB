package hello.jdbc.connection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DBConnectionUtilTest {

    @Test
    public void connectionTest() throws Exception {
        Connection conn = DBConnectionUtil.getConnection();
        assertThat(conn).isNotNull();
    }

}