import org.junit.Test;
import Users.DemoUser;
import static org.junit.Assert.assertEquals;

public class DemoUserTest {
    @Test
    public void demoUser(){
        DemoUser bob = new DemoUser("bob", "1234");
        assertEquals(bob.getUsername(), "bob");
        assertEquals(bob.getPassword(), "1234");
    }

    @Test
    public void demoUserItems(){
        DemoUser bob = new DemoUser("bob", "1234");
        assertEquals(bob.getInventory().size(),1);
        assertEquals(bob.getWishlist().size(), 1);

    }

}
