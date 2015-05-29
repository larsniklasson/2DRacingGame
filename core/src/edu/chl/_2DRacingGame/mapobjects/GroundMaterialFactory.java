package edu.chl._2DRacingGame.mapobjects;

/**
 * Factory which finds and creates a GroundMaterial based on different arguments.
 *
 * @author Daniel Sunnerberg
 */
public class GroundMaterialFactory {

    /**
     * Creates a new GroundMaterial matching specified name.
     *
     * @param name name of the material, e.g. "dirt"
     * @return matching GroundMaterial
     * @throws IllegalArgumentException if no matches are found
     */
    public static GroundMaterial create(String name) {
        switch (name) {
            case "dirt":
                return new Dirt();
            case "ice":
                return new Ice();
            case "sand":
                return new Sand();
            default:
                throw new IllegalArgumentException("No GroundMaterial under that name was found.");
        }
    }

}
