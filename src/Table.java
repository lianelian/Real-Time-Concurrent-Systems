/**
 * The Table class ensures mutual exclusion and synchronization
 * It makes sure only one thread is accessing the table at a time
 * And, that only one chef can retrieve inredients at a time
 *
 * @author Lian Elian 101201545
 * @version 1.00
 */

public class Table {
    // size of space on the table
    public static final int SIZE = 2;
    // keeps track of ingredients on table
    private String[] onTable = new String[SIZE];
    // if there is space on the table writeable is true
    private boolean writeable = true;
    // if there is an ingredient/s on the table readable is true
    private boolean readable = false;
    // These variables are used to make sure the same chef is accessing both ingredients on the table
    private int inIndex = 0, outIndex = 0, count = 0;
    // chosen chef keeps track of the chef thread chosen to run
    private Object chosen_chef = null;
    // empty is true when there is no chef accessing the table
    private boolean empty = true;

    /**
     * Puts 2 ingredients on the table. This method only adds
     * an ingredient when there is space on the table then
     * notifyAll() is called
     *
     * @param added_ingredient a string object to be put on the table
     */
    public synchronized void put(String added_ingredient) {
        while (!writeable) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        // adds the ingredient to table and resize list
        onTable[inIndex] = added_ingredient;
        readable = true;
        inIndex = (inIndex + 1) % SIZE;
        count++;
        // check if the table is full
        if (count == SIZE)
            writeable = false;

        notifyAll();
    }

    /**
     * This method Removes an ingredient (object) from the table and returns it.
     * Before removing an item, the method checked is there is anything on
     * the table to be removed.
     *
     * @return removed_ingredient - Object removed from list onTable
     */
    public synchronized String get() {
        // check if there is any ingredients on table
        while (!readable) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        String removed_ingredient = new String();
        removed_ingredient = onTable[outIndex];
        writeable = true;
        // ingredient is removed and size of list is altered
        outIndex = (outIndex + 1) % SIZE;
        count--;
        // checks if table is empty
        if (count == 0)
            readable = false;

        notifyAll();
        return removed_ingredient;

    }

    /**
     * This method keeps track of the chosen chef thread that is chosen by the agent.
     *
     * @param chef - an Object representing the chef chosen to run by the agent
     */
    public synchronized void add_chef(Object chef) {
        // checks if a chef thread is running
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        chosen_chef = chef;
        empty = false;
        notifyAll();
    }

    /**
     * This method is called to signal the chef is done making the sandwich.
     * It sets the chosen chef value to null.
     *
     * @return the chef that made the sandwich.
     */
    // checks if there is an object to remove
    public synchronized Object remove_chef() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }

        Object removed_chef = chosen_chef;
        // allows another chef to run when add_chef is called
        chosen_chef = null;
        empty = true;
        notifyAll();

        return removed_chef;
    }

    public synchronized void check_chef() {
        /**
         * This method ensures that the chef successfully uses both ingredient placed in table to make the sandwich
         *
         */
        // Only the chef thread with the same name as the chosen chef can run
        while (empty || Thread.currentThread().getName()!= chosen_chef) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        notifyAll();
    }
}
