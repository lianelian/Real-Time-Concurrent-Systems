import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** MakeSandwich.java
 *
 * Make Sandwich
 *
 * This is a Java implementation of the Sandwich Making Chefs Problem.
 * -> Addresses the synchronization of concurrent threads and processes
 * -> Creates one Agent thread (producer) and three chef threads (consumers)
 * program.
 *
 * @author Lian Elian 101201545
 * Reference used: JavaSynch by Lynn Marshall
 * Systems and Computer Engineering,
 * Carleton University
 * Course: SYSC3303B2
 * Intstructor: Jerome Talim, PhD, PEng
 * @version 1.3, January 28, 2023
 */
public class MakeSandwich
{
    // number of sandwiches to make
    public static int NUM_SANDWICH = 20;
    /**
     * This class creates one agent (agent) thread and three chef threads (bread_chef, pb_chef, and jam_chef)
     * It also creates a Table object to be shared by the threads
     * These are used to make sandwiches.
     * @param args
     */
    public static void main(String[] args)
    {
        // Initialize the threads
        Thread agent;
        Thread bread_chef, pb_chef, jam_chef;

        // Creates a table object to be shared by the agent and chef threads
        Table table;
        table = new Table();

        // creates agent (producer) thread
        agent = new Thread(new Agent(table), "Agent");

        // creates chef (consumer) threads
        bread_chef = new Thread(new Chef(table), "Bread");
        pb_chef = new Thread(new Chef(table), "Peanut Butter");
        jam_chef = new Thread(new Chef(table), "Jam");

        // start agent and chef threads
        agent.start();
        bread_chef.start();
        pb_chef.start();
        jam_chef.start();

    }
}

/**
 * Agent is the class for the producer thread. It makes 20 sandwiches.
 * It chooses two random ingredients and calls the chef thread with the remaining ingredient to run
 */
class Agent implements Runnable
{
    private Table table;
    // Creates a list of ingredients for the sandwich
    List<String> ingredients = Arrays.asList("Bread", "Peanut Butter", "Jam");

    // assign table object to variable this.table
    public Agent(Table table) {
        this.table = table;
    }

    public String choose_ingredient() {
        /**
         * This method chooses a random ingredient from the list, ingredients, and returns it
         * @return String of the random ingredient the agent chose
         */
        Random random = new Random();
        return ingredients.get(random.nextInt(ingredients.size()));
    }

    // which consumer thread to run
    public String which_chef(List<String> ingredient_list) {
        /**
         * This method returns the name of the chef with the remaining ingredient
         *
         * @param ingredient_list a list of the two random ingredients chosen by the agent
         * @return String name of chef chosen to run
         */
        int i = 0;
        while(ingredient_list.contains(ingredients.get(i))) {
            i++;
        }
        return ingredients.get(i);
    }

    public void run() {

        for(int i = 0; i< MakeSandwich.NUM_SANDWICH; i++) {
            String chosen_chef = new String();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            System.out.println("\nSANDWICH #" + (i+1));

            // list of ingredients chosen by agent
            List<String> chosen_ingredients = new ArrayList<>();

            // choose two random ingredients and put on the table
            for(int j=0; j<2; j++) {
                // wait
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}

                String chosen = new String();
                boolean found = false;
                // make sure that the two ingredients chosen are different
                while(!found) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}

                    chosen = choose_ingredient();
                    if (!chosen_ingredients.contains(chosen)){
                        chosen_ingredients.add(chosen);
                        found = true;
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}

                table.put(chosen);
                System.out.println(Thread.currentThread().getName() + " put ingredient #" + (j+1) + ": " + chosen + ", on the table.");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}

            // allows the chef with the remaining ingredient to run
            chosen_chef = which_chef(chosen_ingredients);
            table.add_chef(chosen_chef);
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {}

        }
        System.out.println("All done!");
    }
}

/**
 * Chef is the class for the consumer thread.
 * This method gets two ingredients from the table object and makes the sandwich/
 */
class Chef implements Runnable{
    private Table table;

    public Chef(Table table) {
        this.table = table;
    }

    public void run() {
        for (int c = 0; c < MakeSandwich.NUM_SANDWICH; c++) {
            // checks if the chef thread running is the chosen chef
            table.check_chef();
            System.out.println(Thread.currentThread().getName() + " Chef has the remaining ingredient , and is chosen to make the sandwich.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            // removes the 2 ingredients from the table
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                String ingredient = new String();
                ingredient = table.get();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                System.out.println(Thread.currentThread().getName() + " Chef got ingredient " + ingredient.toString() + " from table.");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            Object chef_name = new Object();
            // removes the chef from the table and allows other chefs to make sandwiches
            chef_name = table.remove_chef();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println(chef_name + " Chef has successfully made and ate the sandwich!");
        }
    }
}
