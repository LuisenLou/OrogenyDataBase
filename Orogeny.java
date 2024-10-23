import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * Clase principal para gestionar las operaciones en la base de datos "Orogeny".
 * Realiza operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre una tabla "Mountains".
 */
public class Orogeny {
    // Constantes para la conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/orogeny"; //URL de la bbdd.
    private static final String USER = "root"; //Usuario por defecto de MySQL.
    private static final String PASSWORD = ""; //Contraseña , en este caso no se escogió ninguna.
    
    /**
     * Método principal que ejecuta el programa y maneja las opciones del menú.
     *
     * @param args los argumentos de la línea de comandos los cuales no se usan en el programa.
     */    public static void main(String[] args) {

        //Cargamos el driver de MySQL.
        loadDriver();  
        Scanner scanner = new Scanner(System.in);  

        try (
            // Establecer conexión con la base de datos.
            Connection conex = DriverManager.getConnection(URL, USER, PASSWORD);    
        ){
            System.out.println("¡Conexión a la BBDD con éxito!");
            
            int option; // Variable para almacenar la opción seleccionada en el menú

            do { 
                try {
                    showMenu(); // Mostrar el menú de opciones
                    System.out.println("Elige una opción");

                    // Obtener la opción seleccionada por el usuario
                    option = scanner.nextInt();
                    scanner.nextLine();

                    // Procesar la opción seleccionada
                    switch(option){
                        case 1: createTable(conex); // Crear la tabla
                            System.out.println("Tabla 'Mountains' creada.");
                        break;
                        case 2: 
                            // Solicitar datos para insertar una nueva montaña
                            System.out.println("Nombre de la montaña: ");
                            String name = scanner.nextLine();
                            System.out.println("Altura de la montaña: ");
                            int height = Integer.parseInt(scanner.nextLine());
                            System.out.println("Año del primer ascenso");
                            int first_asc = Integer.parseInt(scanner.nextLine());
                            System.out.println("Región en la que se encuentra: ");
                            String region = scanner.nextLine();
                            System.out.println("País en el que se encuentra: ");
                            String country = scanner.nextLine();
                            insertRow(conex, name, height, first_asc, region, country); // Insertar los datos
                        break;
                        case 3: selectTable(conex); // Consultar todos los datos de la tabla
                        break;
                        case 4: 
                            // Pedir el ID de la montaña a consultar.
                            System.out.println("Introduce el id de la montaña que deseas la información: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            selectRow(conex, id); //Consultar una fila.
                        break;
                        case 5:
                            // Pedir el ID de la montaña a modificar y nuevos valores
                            System.out.println("Introduce el ID de la montaña que deseas modificar: ");
                            id = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Escribe el nuevo nombre de la montaña (Espacio en blanco si desea no modificar): ");
                            String newName = scanner.nextLine();
                            System.out.println("Escribe la nueva altura de la montaña (Escriba 0 si desea no modificar): ");
                            int newHeight = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Escribe el nuevo año de primer ascenso de la montaña (Escriba 0 si desea no modificar): ");
                            int newFirst_asc = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Escribe la nueva región de la montaña (Espacio en blanco si desea no modificar): ");
                            String newRegion = scanner.nextLine();
                            System.out.println("Escribe el nuevo país de la montaña (Espacio en blanco si desea no modificar): ");
                            String newCountry = scanner.nextLine();

                            updateRow(conex, id, newName, newHeight, newFirst_asc, newRegion, newCountry);

                        break;
                        case 6:
                            // Pedir el ID de la montaña a eliminar
                            System.out.println("ID de la montaña a borrar: ");
                            id = scanner.nextInt();
                            scanner.nextLine();
                            deleteRow(conex, id); //Eliminar fila
                        break;
                        case 7: dropTable(conex); //Eliminar tabla completa
                        break;
                        case 8:
                            //Salida del programa.
                            System.out.println("Saliendo...");
                            option = 0;
                        break;
                        case 0: 
                            //Salida del programa para el caso excepcional de '0'
                            System.out.println("Cerrando el programa...");
                            break;
                        default: 
                            System.out.println("Opción no válida. Intenta de nuevo.");
                        break;
                    }
                    
                }catch(SQLException e){
                    //Manejo de errores SQL.
                    System.err.println("Error al preparar la consulta: " + e.getMessage());
                    option = -1;
                }catch(InputMismatchException nb){
                    //Manejo de errores de entrada inválida.
                    System.err.println("Error en la entrada de datos, introduzca un dato válido ");
                    scanner.nextLine();
                    option = -1;
                }
            } while (option != 0 );

        }catch(SQLException ex){
            //Manejo de errores de conexión a la base de datos.
            System.err.println("Error en la BBDD.");
        }finally{
            scanner.close(); //Cierre del recurso
        }
    }

    /**
     * Muestra el menú de opciones en la consola.
     */
    public static void showMenu(){

        System.out.println("Menú:");
            System.out.println("1. Crear una nueva tabla 'Mountains' (crear tabla)");
            System.out.println("2. Añadir una Mountain a la tabla de Mountains (añadir una fila)");
            System.out.println("3. Consultar los datos de toda la tabla Mountains (leer tabla)");
            System.out.println("4. Consultar los datos de una Mountain (leer fila)");
            System.out.println("5. Editar los campos de una Mountain (modificar fila)");
            System.out.println("6. Borrar una Mountain (eliminar fila)");
            System.out.println("7. Borrar la tabla 'Mountains' (eliminar tabla)");
            System.out.println("8. Salir");
    }

    /**
     * Carga el driver JDBC para MySQL.
     */
    public static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver JDBC: " + e.getMessage());
        }
    }

     /**
     * Crea la tabla 'Mountains' en la base de datos si no existe.
     *
     * @param conex la conexión a la base de datos.
     * @throws SQLException si ocurre algún error en la base de datos.
     */
    public static void createTable(Connection conex) throws SQLException{
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Mountains ("
            + "id INT AUTO_INCREMENT PRIMARY KEY, "
            + "name VARCHAR(50), "
            + "height INT, "
            + "first_asc INT, "
            + "region VARCHAR(50), "
            + "country VARCHAR(50))";

            try(PreparedStatement pstmt = conex.prepareStatement(createTableSQL)){
                pstmt.executeUpdate();
            }catch(SQLException createsql){
                System.err.println("Error al tratar de crear en la BBDD" + createsql.getMessage());
            }        
    }    

    /**
     * Inserta una fila en la tabla 'Mountains'.
     *
     * @param conex la conexión a la base de datos.
     * @param name el nombre de la montaña.
     * @param height la altura de la montaña.
     * @param first_asc el año del primer ascenso de la montaña.
     * @param region la región de la montaña.
     * @param country el país de la montaña.
     * @throws SQLException si ocurre algún error al insertar los datos.
     */
    public static void insertRow(Connection conex, String name, int height, int first_asc, String region, String country) throws SQLException{
        String insertSQL = "INSERT INTO Mountains (name, height, first_asc, region, country) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conex.prepareStatement(insertSQL)){
            pstmt.setString(1, name);
            pstmt.setInt(2, height);
            pstmt.setInt(3, first_asc);
            pstmt.setString(4, region);
            pstmt.setString(5, country);

            pstmt.executeUpdate();

            System.out.println("Añadido correctamente.");
            
        }catch(SQLException insql){
            System.err.println("Error al tratar de insertar en la BBDD. " + insql.getMessage());
        }
    }

    /**
     * Muestra los datos de todas las montañas en la tabla 'Mountains'.
     *
     * @param conex la conexión a la base de datos.
     */
    public static void selectTable(Connection conex){
        String selectSQL = "SELECT * FROM Mountains";
        try(PreparedStatement pstmt = conex.prepareStatement(selectSQL);
            ResultSet rs = pstmt.executeQuery(selectSQL) 
        ){
            System.out.println("Datos de la tabla 'Mountains: ");
            while(rs.next()){
                System.out.println("ID: "+ rs.getInt("id") + "\n"
                + "Nombre: " + rs.getString("name") + "\n" 
                + "Altura: " + rs.getInt("height") + "\n" 
                + "Primer Ascenso: " + rs.getInt("first_asc") + "\n"
                + "Región:" + rs.getString("region") + "\n"
                + "País: " + rs.getString("country")) ;
            }
        }catch(SQLException selesql){
            System.err.println("Error al tratar de leer en la BBDD" + selesql.getMessage());
        }
    }

    /**
     * Muestra los datos de una montaña específica, basada en su ID.
     *
     * @param conex la conexión a la base de datos.
     * @param id el ID de la montaña.
     */
    public static void selectRow (Connection conex, int id){
        String selectSQL = "SELECT * FROM Mountains WHERE id = ?";
        try(PreparedStatement pstmt = conex.prepareStatement(selectSQL); 
        ResultSet rs = pstmt.executeQuery();
        ){
            pstmt.setInt(1, id);
            
            System.out.println("Datos del ID : " + id );
            if(rs.next()){
                System.out.println("ID: "+ rs.getInt("id") + "\n"
                + "Nombre: " + rs.getString("name") + "\n" 
                + "Altura: " + rs.getInt("height") + "\n" 
                + "Primer Ascenso: " + rs.getInt("first_asc") + "\n"
                + "Región:" + rs.getString("region") + "\n"
                + "País: " + rs.getString("country")) ;
            }else{
                System.out.println("No se encontró la montaña con el ID : " + id);
            }
        }catch(SQLException selesql){
            System.err.println("Error al tratar de leer en la BBDD" + selesql.getMessage());
        }
    }

    /**
     * Actualiza los datos de una montaña en la tabla 'Mountains'.
     *
     * @param conex la conexión a la base de datos.
     * @param id el ID de la montaña a actualizar.
     * @param newName el nuevo nombre de la montaña (puede ser nulo si no se actualiza).
     * @param newHeight la nueva altura (0 si no se actualiza).
     * @param newFirst_asc el nuevo año de primer ascenso (0 si no se actualiza).
     * @param newRegion la nueva región (puede ser nula si no se actualiza).
     * @param newCountry el nuevo país (puede ser nulo si no se actualiza).
     */
    public static void updateRow(Connection conex, int id, String newName, int newHeight, int newFirst_asc, String newRegion, String newCountry){
        String updateSQL = "UPDATE Mountains SET name = COALESCE(?, name), height = COALESCE(NULLIF(?, 0), height), first_asc = COALESCE(NULLIF(?, 0), first_asc), region = COALESCE(?, region), country = COALESCE(?, country) WHERE id = ?";
        
        try(PreparedStatement pstmt = conex.prepareStatement(updateSQL)){
            pstmt.setInt(6, id);
            pstmt.setString(1, newName.isEmpty() ? null : newName);
            pstmt.setInt(2, newHeight);
            pstmt.setInt(3, newFirst_asc);
            pstmt.setString(4, newRegion.isEmpty() ? null : newRegion);
            pstmt.setString(5, newCountry.isEmpty() ? null : newCountry);
            int modifiedRows = pstmt.executeUpdate();

            if(modifiedRows > 0 ){
                System.out.println("Montaña actualizada con éxito.");
            }else{
                System.out.println("No se encontró la montaña con el ID:" + id);
            }

        }catch(SQLException updException){
            System.err.println("Error al actualizar en la BBDD " + updException.getMessage());
        }
    }

    /**
     * Elimina una fila en la tabla 'Mountains', a partir del ID.
     *
     * @param conex la conexión a la base de datos.
     * @param id el ID de la montaña a eliminar.
     * @throws SQLException si ocurre algún error en la eliminación.
     */
    public static void deleteRow(Connection conex, int id) throws SQLException{
        String deleteSQL = "DELETE FROM Mountains WHERE id = ?";

        try(PreparedStatement pstmt = conex.prepareStatement(deleteSQL)){
            pstmt.setInt(1, id);
            int deletedRows = pstmt.executeUpdate();
            if(deletedRows > 0 ){
                System.out.println("Fila eliminada correctamente. ");
            }else{
                System.out.println("No existe la montaña con el ID:" + id);
            }
        }catch(SQLException delsql){
            System.err.println("Error al eliminar en la BBDD" + delsql.getMessage());
        }
    }

    /**
     * Elimina la tabla 'Mountains' de la base de datos.
     *
     * @param conex la conexión a la base de datos.
     */
    public static void dropTable (Connection conex){
        String dropSQL = "DROP TABLE IF EXISTS Mountains";
        try (PreparedStatement pstmt = conex.prepareStatement(dropSQL)){
            
            pstmt.executeUpdate();
            System.out.println("Tabla 'Mountains' borrada correctamente");
        } catch (SQLException drpsql) {
            System.err.println("Error al intentar eliminar en la BBDD " + drpsql.getMessage());
        }
    }
}
