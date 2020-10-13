package Items;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * <h1>Item</h1>
 * Represents an item in the trading system.
 * Class variables include the item's ID, name, and description (which is optional).
 */
public class Item implements Serializable {
    private UUID id;
    private String name;
    private String description = "This is an item for trade.";

    /**
     * Creates a new item with the given name.
     * @param name This item's name.
     */
    public Item(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

    /**
     * Returns this item's name.
     * @return this item's name as a String.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Gets this item's name.
     * @return this item's name as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this item's description.
     * @param description A brief description of this item.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets this item's description.
     * @return this item's description as a String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets this item's id.
     * @return this item's id as a UUID.
     */
    public UUID getId() {
        return id;
    }


}
