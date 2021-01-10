/**
 * @author Austin Kim
 */

package model;

/** The InHouse Class represents a subclass of a Part. */
public class InHouse extends Part {

    /** The InHouse part's machine ID. */
    private int machineId;

    /**
     * This method serves as the constructor for the InHouse class.
     * @param id the part id to set.
     * @param name the part name to set.
     * @param price the part price to set.
     * @param stock the part stock to set.
     * @param min the part min to set.
     * @param max the part max to set.
     * @param machineId the part machine ID to set.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the part's machine ID.
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the part's machine ID to set.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
