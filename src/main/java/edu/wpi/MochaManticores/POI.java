package edu.wpi.MochaManticores;

import java.util.Stack;

/**
 * This is a point of interest class intended
 * to contain additional information about
 * locations on the map
 * @author aksil
 */
public class POI extends MapNode {

    String name;    //name of the location

    public POI(int ID, Stack neighborID, int clearance, String name) {
        super(ID, neighborID, clearance);
    }

    public String getName() {
        /**
         * function: getName()
         * usage: function to retrieve the name of the node
         * inputs: none
         * returns: String name (name of the POI)
         */
        return name;
    }
}
