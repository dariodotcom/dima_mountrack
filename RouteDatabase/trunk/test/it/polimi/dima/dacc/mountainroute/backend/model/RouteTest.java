package it.polimi.dima.dacc.mountainroute.backend.model;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class RouteTest extends AppEngineTestCase {

    private Route model = new Route();

    @Test
    public void test() throws Exception {
        assertThat(model, is(notNullValue()));
    }
}
