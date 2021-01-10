/**
 * @author Austin Kim
 */

package model;

/** The Outsourced Class represents a subclass of a Part. */
public class Outsourced extends Part {

    /** The outsourced part's company name. */
    private String companyName;

    /**
     * This method serves as the constructor for the Outsourced class.
     * @param id the part id to set.
     * @param name the part name to set.
     * @param price the part price to set.
     * @param stock the part stock to set.
     * @param min the part min to set.
     * @param max the part max to set.
     * @param companyName the part company name to set.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return the part's company name.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the part's company name to set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
