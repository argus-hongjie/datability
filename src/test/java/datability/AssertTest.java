package datability;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class AssertTest {
    @Test
    public void when_null() throws Exception {
        try {
            Assert.notNull(null, "is null");
            Assertions.fail("must have failed");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class).hasMessage("is null");
        }
    }

    @Test
    public void when_non_null() throws Exception {
        String param = Assert.notNull("nonNull", "ignore");

        assertThat(param).isEqualTo("nonNull");
    }
}