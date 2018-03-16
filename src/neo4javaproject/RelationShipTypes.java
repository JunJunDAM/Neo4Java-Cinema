/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neo4javaproject;

import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author alu2015059
 */
public enum RelationShipTypes implements RelationshipType{
    IS_FRIEND_OF,
    HAS_SEEN;
}
