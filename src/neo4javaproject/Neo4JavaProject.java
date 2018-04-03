/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neo4javaproject;

import java.io.*;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author alu2015059
 */
public class Neo4JavaProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GraphDatabaseFactory graphDBFactory = new GraphDatabaseFactory();
        GraphDatabaseService graphDBService = graphDBFactory.newEmbeddedDatabase(new File("C:/Neo4jJavaProjectDataBase"));
        int menuOption;

        do {
            menu();
            menuOption = pedirEntero();
            switch (menuOption) {
                case 1: //New user
                    try (Transaction tx = graphDBService.beginTx()) {
                        String username = pedirCadena("Introduce nombre : ");
                        Node user1 = graphDBService.createNode(Labels.USERS);
                        user1.setProperty("name", username);
                        tx.success();
                    }
                    break;
                case 2: //New film
                    try (Transaction tx = graphDBService.beginTx()) {
                        String filmName = pedirCadena("Introduce un titulo: ");
                        Node film1 = graphDBService.createNode(Labels.MOVIES);
                        film1.setProperty("title", filmName);
                        tx.success();
                    }
                    break;
                case 3: //See film
                    try (Transaction tx = graphDBService.beginTx()) {
                        ResourceIterator<Node> movies = graphDBService.findNodes(Labels.MOVIES);
                        ResourceIterator<Node> users = graphDBService.findNodes(Labels.USERS);

                        String username = pedirCadena("Introduce nombre de usuario : ");
                        String filmName = pedirCadena("Introduce titulo de pelicula : ");
                        System.out.println("Que nota le pone " + username + " a " + filmName + " ? : ");
                        int nota = pedirEntero();

                        Node user = null;
                        Node movie = null;

                        while (users.hasNext()) {
                            Node user2 = users.next();
                            if (username.equalsIgnoreCase(String.valueOf(user2.getProperty("name")))) {
                                user = user2;
                            }
                        }

                        while (movies.hasNext()) {
                            Node movie2 = movies.next();
                            if (filmName.equalsIgnoreCase(String.valueOf(movie2.getProperty("title")))) {
                                movie = movie2;
                            }
                        }

                        System.out.println("*** " + user.getProperty("name") + " ha visto " + movie.getProperty("title")
                                + " y le ha puesto un " + nota + " de nota");
                        seeMovie(user, movie, nota);
                        tx.success();
                    }
                    break;
                case 4: //Notas de peliculas
                    try (Transaction tx = graphDBService.beginTx()) {
                        ResourceIterator<Node> movies = graphDBService.findNodes(Labels.MOVIES);
                        movies = graphDBService.findNodes(Labels.MOVIES);
                        System.out.println("");
                        System.out.println("Movies ratings : ");
                        while (movies.hasNext()) {
                            Node movie = movies.next();
                            Iterable<Relationship> relationships = movie.getRelationships(
                                    Direction.INCOMING, RelationShipTypes.HAS_SEEN);
                            int totalStars = 0;
                            int relationshipCount = 0;
                            for (Relationship relationship : relationships) {
                                int stars = (int) relationship.getProperty("stars");
                                totalStars += stars;
                                relationshipCount++;
                            }
                            System.out.println(movie.getProperty("title") + ", Viewers: "
                                    + relationshipCount + ", Average rating: "
                                    + totalStars / relationshipCount);
                        }
                        tx.success();
                    }
                    break;
                case 5: //Peliculas vistas por usuarios
                    try (Transaction tx = graphDBService.beginTx()) {
                        ResourceIterator<Node> users = graphDBService.findNodes(Labels.USERS);
                        users = graphDBService.findNodes(Labels.USERS);
                        while (users.hasNext()) {
                            Node user = users.next();
                            System.out.println(user.getProperty("name")
                                    + " has seen :");
                            for (Relationship relationship : user.getRelationships(RelationShipTypes.HAS_SEEN)) {
                                Node movie = relationship.getOtherNode(user);
                                System.out.println(movie.getProperty("title"));
                            }
                            System.out.println("");
                        }
                        tx.success();
                    }
                    break;
                case 6:
                    System.out.println("Aplicacion cerrada");
                    break;
                case 7:
                    try (Transaction tx = graphDBService.beginTx()) {
                        //Muesto las peliculas
                        ResourceIterator<Node> movies = graphDBService.findNodes(Labels.MOVIES);
                        System.out.println("Movies: ");
                        while (movies.hasNext()) {
                            Node movie = movies.next();
                            System.out.print(movie.getProperty("title") + " ; ");
                        }

                        //Muestro los usuarios
                        ResourceIterator<Node> users = graphDBService.findNodes(Labels.USERS);
                        System.out.println("");
                        System.out.println("Users: ");
                        while (users.hasNext()) {
                            Node user = users.next();
                            System.out.print(user.getProperty("name") + " ; ");
                        }
                        System.out.println("");
                        tx.success();
                    }
                    break;
                default:
                    System.err.println("Opcion incorrecta");
            }
        } while (menuOption != 6);

        /*
        try (Transaction tx = graphDBService.beginTx()) {

            //Creo usuarios
            Node usu1 = graphDBService.createNode(Labels.USERS);
            usu1.setProperty("name", "Sergio");
            Node usu2 = graphDBService.createNode(Labels.USERS);
            usu2.setProperty("name", "Jun");
            Node usu3 = graphDBService.createNode(Labels.USERS);
            usu3.setProperty("name", "Dani");

            //Creo peliculas
            Node movie1 = graphDBService.createNode(Labels.MOVIES);
            movie1.setProperty("name", "Torrente");
            Node movie2 = graphDBService.createNode(Labels.MOVIES);
            movie2.setProperty("name", "Titanic");
            Node movie3 = graphDBService.createNode(Labels.MOVIES);
            movie3.setProperty("name", "King Kong");

            //Hago relaciones IS_FRIEND_OF entre usu1 y el resto
            usu1.createRelationshipTo(usu2, RelationShipTypes.IS_FRIEND_OF);
            usu1.createRelationshipTo(usu3, RelationShipTypes.IS_FRIEND_OF);

            //Creo una relacion tipo HAS_SEEN entre los usuarios y las peliculas
            seeMovie(usu1, movie1, 10);
            seeMovie(usu2, movie1, 6);
            seeMovie(usu3, movie2, 5);

            //Muesto las peliculas
            ResourceIterator<Node> movies = graphDBService.findNodes(Labels.MOVIES);
            System.out.println("Movies: ");
            while (movies.hasNext()) {
                Node movie = movies.next();
                System.out.print(movie.getProperty("name") + " ; ");
            }

            //Muestro los usuarios
            ResourceIterator<Node> users = graphDBService.findNodes(Labels.USERS);
            System.out.println("");
            System.out.println("Users: ");
            while (users.hasNext()) {
                Node user = users.next();
                System.out.print(user.getProperty("name") + " ; ");
            }

            //Notas que tienen peliculas
            movies = graphDBService.findNodes(Labels.MOVIES);
            System.out.println("");
            System.out.println("Movies ratings : ");
            while (movies.hasNext()) {
                Node movie = movies.next();
                Iterable<Relationship> relationships = movie.getRelationships(
                        Direction.INCOMING, RelationShipTypes.HAS_SEEN);
                int totalStars = 0;
                int relationshipCount = 0;
                for (Relationship relationship : relationships) {
                    int stars = (int) relationship.getProperty("stars");
                    totalStars += stars;
                    relationshipCount++;
                }
                System.out.println(movie.getProperty("name") + ", Viewers: "
                        + relationshipCount + ", Average rating: "
                        + totalStars / relationshipCount);
            }
            //Peliculas vistas por usuarios
            users = graphDBService.findNodes(Labels.USERS);
            while (users.hasNext()) {
                Node user = users.next();
                System.out.print(user.getProperty("name")
                        + " has seen :");
                for (Relationship relationship : user.getRelationships(RelationShipTypes.HAS_SEEN)) {
                    Node movie = relationship.getOtherNode(user);
                    System.out.println(movie.getProperty("name"));
                }
                System.out.println("");
            }

            tx.success();
        }
         */
    }

    //Creo la relacion HAS_SEEN para tener una relacion entre usuarios y peliculas
    public static Relationship seeMovie(Node user, Node movie, int stars) {
        Relationship relationship = user.createRelationshipTo(movie, RelationShipTypes.HAS_SEEN);
        relationship.setProperty("stars", stars);
        return relationship;
    }

    public static void menu() {
        System.out.println("************************************************");
        System.out.println("**********          Cinema            **********");
        System.out.println("************************************************");
        System.out.println("********** [1] - New User             **********");
        System.out.println("********** [2] - New Film             **********");
        System.out.println("********** [3] - Show film            **********");
        System.out.println("********** [4] - Films average        **********");
        System.out.println("********** [5] - Users showed films    *********");
        System.out.println("********** [6] - Exit                 **********");
        System.out.println("************************************************");
        System.out.println("********** Escoge una opcion: ");
    }

    //Creo una funcion para pedir Strings
    private static String pedirCadena(String mensaje) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String respuesta = "";
        boolean error;
        do {
            try {
                System.out.print(mensaje);
                respuesta = br.readLine();
                error = false;
            } catch (IOException ex) {
                System.out.println("Error de entrada/salida");
                error = true;
            }
        } while (error);
        return respuesta;
    }

    //Funcion para pedir enteros
    private static int pedirEntero() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int numero = 0;
        boolean error = false;
        do {
            try {
                numero = Integer.parseInt(br.readLine());
            } catch (IOException ex) {
                System.out.println("Error de entrada y salida");
            } catch (NumberFormatException ex) {
                System.out.println("No has introducido un nº entero.");
            }
        } while (error);
        return numero;
    }
}
