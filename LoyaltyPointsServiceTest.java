import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class LoyaltyPointsServiceTest {

    private final LoyaltyPointsService service = new LoyaltyPointsService();

    @Test
    public void testCalculatePointsForPurchase(VertxTestContext context) {
        // Set up a mock user activity
        UserActivity activity = new UserActivity("PURCHASE", 100); // For example, $100 purchase

        // Call the service method
        int points = service.calculatePoints(activity);

        // Verify the points
        assertEquals(100, points, "Points should be 100 for a $100 purchase");

        context.completeNow();
    }
}

