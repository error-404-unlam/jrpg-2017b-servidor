package servidor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import mensajeria.PaquetePersonaje;
import mensajeria.PaqueteUsuario;

/**
 * Clase Conector.
 */
public class Conector {

	private String url = "primeraBase.bd";
	private Connection connect; // gradle
	static final int INDICE1 = 1;
	static final int INDICE2 = 2;
	static final int INDICE3 = 3;
	static final int INDICE4 = 4;
	static final int INDICE5 = 5;
	static final int INDICE6 = 6;
	static final int INDICE7 = 7;
	static final int INDICE8 = 8;
	static final int INDICE9 = 9;
	static final int INDICE10 = 10;
	static final int INDICE11 = 11;
	static final int INDICE12 = 12;
	static final int INDICE13 = 13;
	static final int INDICE14 = 14;
	static final int INDICE15 = 15;
	static final int INDICE16 = 16;
	static final int INDICE17 = 17;
	static final int INDICE18 = 18;
	private static final int INDICE20 = 20;
	private static final int INDICE21 = 21;
	private static final int INDICE29 = 29;

	/**
	 * Metodo Connect.
	 * Establece la conexion con la base de datos
	 */
	public void connect() {
		try {
            Servidor.getLog().append("Estableciendo conexión con la base de datos..." + System.lineSeparator());
            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
            Servidor.getLog().append("Conexión con la base de datos establecida con éxito." + System.lineSeparator());
		} catch (SQLException ex) {
            Servidor.getLog().append("Fallo al intentar establecer la conexión con la base de datos. " + ex.getMessage()
            		+ System.lineSeparator());
		}
	}

	/**
	 * Metodo Close.
	 * Cierra la conexion con la Base de datos
	 */
	public void close() {
		try {
            connect.close();
		} catch (SQLException ex) {
      Servidor.getLog().append("Error al intentar cerrar la conexión con la base de datos." + System.lineSeparator());
      Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Registrar usuario.
	 *
	 * @param user Objeto de la clase PaqueteUsuario
	 * @return boolean
	 */
	public boolean registrarUsuario(final PaqueteUsuario user) {
		//configuracion de hibernate, le paso el archivo cfg.xml
		Configuration config = new Configuration();
 		config.configure("cfg.xml");
 		
 		SessionFactory factory = config.buildSessionFactory();
 		Session session = factory.openSession();
 		
 		//utilizo criteria para las consultas
 		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
 		CriteriaQuery<PaqueteUsuario> criteriaQuery = criteriaBuilder.createQuery(PaqueteUsuario.class);
 		Root<PaqueteUsuario> root = criteriaQuery.from(PaqueteUsuario.class);//le paso la entidad que necesito
 		System.out.println(session.createQuery(criteriaQuery).getResultList().toString());
 		//de la tabla que le pase, busca si existe el nombre de usuario que ingresaron
 		criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), user.getUsername()));
 		//System.out.println(session.createQuery(criteriaQuery).getResultList());
 		
 		//si la consulta genera tabla vacia, es porque no existe el usuario y puede crearse
 		if(session.createQuery(criteriaQuery).getResultList().isEmpty()) {
 			Transaction tx = session.beginTransaction();
 			try{
 				session.saveOrUpdate(user);
 				tx.commit();
 				
 			} catch (HibernateException e) {
 				if (tx != null)
 					tx.rollback();//si hay error se descarta todo
 				e.printStackTrace();
 				
 				session.close();
 				factory.close();
 				Servidor.getLog().append("Eror al intentar registrar el usuario " + user.getUsername() + System.lineSeparator());
 				return false;
 			}
 		} else {//si la consulta devuelve un campo, es porque el usuario ya existe
 			session.close();
 			factory.close();
 			Servidor.getLog().append("El usuario " + user.getUsername() + " ya se encuentra en uso." + System.lineSeparator());
 			return false;
 		}
 		
 		session.close();
 		factory.close();
 		Servidor.getLog().append("El usuario " + user.getUsername() + " se ha registrado." + System.lineSeparator());
 		return true;

	}

     /**
      * Registrar personaje.
      *
      * @param paquetePersonaje Objeto de la clase PaquetePersonaje
      * @param paqueteUsuario Objeto de la clase PaqueteUsuario
      * @return boolean
      */
     public boolean registrarPersonaje(final PaquetePersonaje paquetePersonaje, final PaqueteUsuario paqueteUsuario) {

        try {
            // Registro al personaje en la base de datos
            PreparedStatement stRegistrarPersonaje = connect.prepareStatement(
                     "INSERT INTO personaje (idInventario,idMochila,casta,raza,fuerza,destreza,inteligencia"
                     + ",saludTope,energiaTope,nombre,experiencia,nivel"
                     + ",idAlianza,puntosNoAsignados,asignadoFuerza,asignadoDestreza,asignadoInteligencia)"
                     + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS);
            stRegistrarPersonaje.setInt(INDICE1, -1);
            stRegistrarPersonaje.setInt(INDICE2, -1);
            stRegistrarPersonaje.setString(INDICE3, paquetePersonaje.getCasta());
            stRegistrarPersonaje.setString(INDICE4, paquetePersonaje.getRaza());
            stRegistrarPersonaje.setInt(INDICE5, paquetePersonaje.getFuerza());
            stRegistrarPersonaje.setInt(INDICE6, paquetePersonaje.getDestreza());
            stRegistrarPersonaje.setInt(INDICE7, paquetePersonaje.getInteligencia());
            stRegistrarPersonaje.setInt(INDICE8, paquetePersonaje.getSaludTope());
            stRegistrarPersonaje.setInt(INDICE9, paquetePersonaje.getEnergiaTope());
            stRegistrarPersonaje.setString(INDICE10, paquetePersonaje.getNombre());
            stRegistrarPersonaje.setInt(INDICE11, 0);
            stRegistrarPersonaje.setInt(INDICE12, 1);
            stRegistrarPersonaje.setInt(INDICE13, -1);
            stRegistrarPersonaje.setInt(INDICE14, 0); // Puntos a asignar
            stRegistrarPersonaje.setInt(INDICE15, 0); // Puntos asignados a fuerza
            stRegistrarPersonaje.setInt(INDICE16, 0); // Puntos asignados a destreza
            stRegistrarPersonaje.setInt(INDICE17, 0); // Puntos asignados a
                                                // inteligencia
            stRegistrarPersonaje.execute();

            // Recupero la última key generada
            ResultSet rs = stRegistrarPersonaje.getGeneratedKeys();
            if (rs != null && rs.next()) {

            	// Obtengo el ID
            	int idPersonaje = rs.getInt(1);

            	// Le asigno el ID al paquete personaje que voy a devolver
            	paquetePersonaje.setId(idPersonaje);

            	// Le asigno el personaje al usuario
            	PreparedStatement stAsignarPersonaje = connect
                        .prepareStatement("UPDATE registro SET idPersonaje=? WHERE usuario=? AND password=?");
            	stAsignarPersonaje.setInt(INDICE1, idPersonaje);
            	stAsignarPersonaje.setString(INDICE2, paqueteUsuario.getUsername());
            	stAsignarPersonaje.setString(INDICE3, paqueteUsuario.getPassword());
            	stAsignarPersonaje.execute();

            	// Por último, registro el inventario y la mochila
            	if (this.registrarInventarioMochila(idPersonaje)) {
                Servidor.getLog().append("El usuario " + paqueteUsuario.getUsername() + " ha creado el personaje "
                        	+ paquetePersonaje.getId() + System.lineSeparator());
            		return true;
            	} else {
            		Servidor.getLog().append(
                         "Error al registrar la mochila y el inventario del usuario " + paqueteUsuario.getUsername()
                                    + " con el personaje" + paquetePersonaje.getId() + System.lineSeparator());
            		return false;
            	}
            }
            return false;

		} catch (SQLException e) {
            Servidor.getLog().append(
                 "Error al intentar crear el personaje " + paquetePersonaje.getNombre() + System.lineSeparator());
            return false;
		}
	}

	/**
	 * Registrar inventario mochila.
	 *
	 * @param idInventarioMochila entero que indica el id del Inventario Mochila
	 * @return boolean
	 */
	public boolean registrarInventarioMochila(final int idInventarioMochila) {
		try {
            // Preparo la consulta para el registro el inventario en la base de
            // datos
            PreparedStatement stRegistrarInventario = connect.prepareStatement(
                 "INSERT INTO inventario(idInventario,manos1,manos2,pie,cabeza,pecho,accesorio) "
                 + "VALUES (?,-1,-1,-1,-1,-1,-1)");
            stRegistrarInventario.setInt(1, idInventarioMochila);

            // Preparo la consulta para el registro la mochila en la base de
            // datos
            PreparedStatement stRegistrarMochila = connect.prepareStatement(
                   "INSERT INTO mochila(idMochila,item1,item2,item3,item4,item5,item6,item7,item8,"
                   + "item9,item10,item11,item12,item13,item14,item15,item16,item17,item18,item19,item20) "
                   + "VALUES(?,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)");
            stRegistrarMochila.setInt(1, idInventarioMochila);

            // Registro inventario y mochila
            stRegistrarInventario.execute();
            stRegistrarMochila.execute();

            // Le asigno el inventario y la mochila al personaje
            PreparedStatement stAsignarPersonaje = connect
            		.prepareStatement("UPDATE personaje SET idInventario=?, idMochila=? WHERE idPersonaje=?");
            stAsignarPersonaje.setInt(INDICE1, idInventarioMochila);
            stAsignarPersonaje.setInt(INDICE2, idInventarioMochila);
            stAsignarPersonaje.setInt(INDICE3, idInventarioMochila);
            stAsignarPersonaje.execute();

       Servidor.getLog().append("Se ha registrado el inventario de " + idInventarioMochila + System.lineSeparator());
            return true;

		} catch (SQLException e) {
       Servidor.getLog().append("Error al registrar el inventario de " + idInventarioMochila + System.lineSeparator());
            return false;
		}
	}

	/**
	 * Loguear usuario.
	 *
	 * @param user Objeto de la clase PaqueteUsuario
	 * @return boolean
	 */
	public boolean loguearUsuario(final PaqueteUsuario user) {

       Configuration conf = new Configuration();
       conf.configure("cfg.xml");
       
       SessionFactory factory = conf.buildSessionFactory();
       Session session = factory.openSession();
       
       CriteriaBuilder cb = session.getCriteriaBuilder();
       CriteriaQuery<PaqueteUsuario> criteriaQuery = cb.createQuery(PaqueteUsuario.class);
	   Root<PaqueteUsuario> root = criteriaQuery.from(PaqueteUsuario.class);//le paso la entidad que necesito
      //busca en la tabla que el usser sea igual y la contra tmb
	   criteriaQuery.select(root).where(cb.equal(root.get("username"), user.getUsername()), cb.equal(root.get("password") , user.getPassword()));
	   Transaction tx = session.beginTransaction();
	   if(!session.createQuery(criteriaQuery).getResultList().isEmpty()){
		   try {
			tx.commit();
			Servidor.getLog()
                  .append("El usuario " + user.getUsername() + " ha iniciado sesión." + System.lineSeparator());
		   } catch (HibernateException e) {
 				if (tx != null)
 					tx.rollback();//si hay error se descarta todo
 				e.printStackTrace();
 				
 				session.close();
 				factory.close();
 				Servidor.getLog().append("El usuario " + user.getUsername()
 				      + " fallo al iniciar sesión." + System.lineSeparator());
                return false;			
		     }
	   } else {
		   Servidor.getLog().append("El usuario " + user.getUsername()
                + " ha realizado un intento fallido de inicio de sesión." + System.lineSeparator());
           return false;
         }
	   
		session.close();
		factory.close();
		return true;
	}

    /**
     * Actualizar personaje.
     *
     * @param paquetePersonaje Objeto de la clase PaquetePersonaje
     */
    public void actualizarPersonaje(final PaquetePersonaje paquetePersonaje) {
    	// Los NPCs no aparecen en la base de datos de personajes.
		if (paquetePersonaje.getId() < 0) {
            Servidor.getLog().append("El NPC " + paquetePersonaje.getId() + " ha escapado la actualización con éxito."
            		+ System.lineSeparator());
            return;
		}

		try {
            int i = 2;
            int j = 1;
            PreparedStatement stActualizarPersonaje = connect.prepareStatement(
                 "UPDATE personaje SET fuerza=?, destreza=?, inteligencia=?, saludTope=?, energiaTope=?, experiencia=?,"
                 + " nivel=?, puntosNoAsignados=?, asignadoFuerza=?, asignadoDestreza=?, asignadoInteligencia=?"
                     + "WHERE idPersonaje=?");

            stActualizarPersonaje.setInt(INDICE1, paquetePersonaje.getFuerza());
            stActualizarPersonaje.setInt(INDICE2, paquetePersonaje.getDestreza());
            stActualizarPersonaje.setInt(INDICE3, paquetePersonaje.getInteligencia());
            stActualizarPersonaje.setInt(INDICE4, paquetePersonaje.getSaludTope());
            stActualizarPersonaje.setInt(INDICE5, paquetePersonaje.getEnergiaTope());
            stActualizarPersonaje.setInt(INDICE6, paquetePersonaje.getExperiencia());
            stActualizarPersonaje.setInt(INDICE7, paquetePersonaje.getNivel());
            stActualizarPersonaje.setInt(INDICE8, paquetePersonaje.getPuntosNoAsignados()); // Puntos
                                                                                    	// a
                                                                                    	// asignar
            stActualizarPersonaje.setInt(INDICE9, paquetePersonaje.getPuntosAsignadosFuerza()); // Puntos
                                                                                    		// asignados
                                                                                    		// a
                                                                                    		// fuerza
            stActualizarPersonaje.setInt(INDICE10, paquetePersonaje.getPuntosAsignadosDestreza()); // Puntos
                                                                                                // asignados
                                                                                                // a
                                                                                                // destreza
            stActualizarPersonaje.setInt(INDICE11, paquetePersonaje.getPuntosAsignadosInteligencia()); // Puntos
                                                                                                	// asignados
                                                                                                	// a
                                                                                                	// inteligencia
            stActualizarPersonaje.setInt(INDICE12, paquetePersonaje.getId());
            stActualizarPersonaje.executeUpdate();

            PreparedStatement stDameItemsID = connect.prepareStatement("SELECT * FROM mochila WHERE idMochila = ?");
            stDameItemsID.setInt(1, paquetePersonaje.getId());
            ResultSet resultadoItemsID = stDameItemsID.executeQuery();
            PreparedStatement stDatosItem = connect.prepareStatement("SELECT * FROM item WHERE idItem = ?");
            ResultSet resultadoDatoItem = null;
            paquetePersonaje.eliminarItems();

            while (j <= INDICE13) {
            	if (resultadoItemsID.getInt(i) != -1) {
            		stDatosItem.setInt(1, resultadoItemsID.getInt(i));
            		resultadoDatoItem = stDatosItem.executeQuery();

            		paquetePersonaje.anadirItem(resultadoDatoItem.getInt("idItem"),
                        	resultadoDatoItem.getString("nombre"), resultadoDatoItem.getInt("wereable"),
                        	resultadoDatoItem.getInt("bonusSalud"), resultadoDatoItem.getInt("bonusEnergia"),
                        	resultadoDatoItem.getInt("bonusFuerza"), resultadoDatoItem.getInt("bonusDestreza"),
                        	resultadoDatoItem.getInt("bonusInteligencia"), resultadoDatoItem.getString("foto"),
                        	resultadoDatoItem.getString("fotoEquipado"));
            	}
            	i++;
            	j++;
            }
            Servidor.getLog().append("El personaje " + paquetePersonaje.getNombre() + " se ha actualizado con éxito."
            		+ System.lineSeparator());
		} catch (SQLException e) {
            Servidor.getLog().append("Fallo al intentar actualizar el personaje " + paquetePersonaje.getNombre()
            		+ System.lineSeparator());
		}

	}

	/**
	 * Obtiene the personaje.
	 *
	 * @param user Objeto de la clase PaqueteUsuario
	 * @return personaje de la clase PaquetePersonaje
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PaquetePersonaje getPersonaje(final PaqueteUsuario user) throws IOException {
		ResultSet result = null;
		ResultSet resultadoItemsID = null;
		ResultSet resultadoDatoItem = null;
		int i = 2;
		int j = 0;
		try {
            // Selecciono el personaje de ese usuario
            PreparedStatement st = connect.prepareStatement("SELECT * FROM registro WHERE usuario = ?");
            st.setString(1, user.getUsername());
            result = st.executeQuery();

            // Obtengo el id
            int idPersonaje = result.getInt("idPersonaje");

            // Selecciono los datos del personaje
            PreparedStatement stSeleccionarPersonaje = connect
            		.prepareStatement("SELECT * FROM personaje WHERE idPersonaje = ?");
            stSeleccionarPersonaje.setInt(1, idPersonaje);
            result = stSeleccionarPersonaje.executeQuery();
            // Traigo los id de los items correspondientes a mi personaje
            PreparedStatement stDameItemsID = connect.prepareStatement("SELECT * FROM mochila WHERE idMochila = ?");
            stDameItemsID.setInt(1, idPersonaje);
            resultadoItemsID = stDameItemsID.executeQuery();
            // Traigo los datos del item
            PreparedStatement stDatosItem = connect.prepareStatement("SELECT * FROM item WHERE idItem = ?");

            // Obtengo los atributos del personaje
            PaquetePersonaje personaje = new PaquetePersonaje();
            personaje.setId(idPersonaje);
            personaje.setRaza(result.getString("raza"));
            personaje.setCasta(result.getString("casta"));
            personaje.setFuerza(result.getInt("fuerza"));
            personaje.setInteligencia(result.getInt("inteligencia"));
            personaje.setDestreza(result.getInt("destreza"));
            personaje.setEnergiaTope(result.getInt("energiaTope"));
            personaje.setSaludTope(result.getInt("saludTope"));
            personaje.setNombre(result.getString("nombre"));
            personaje.setExperiencia(result.getInt("experiencia"));
            personaje.setNivel(result.getInt("nivel"));
            personaje.setPuntosNoAsignados(result.getInt("puntosNoAsignados"));
            personaje.setPuntosAsignadosFuerza(result.getInt("asignadoFuerza"));
            personaje.setPuntosAsignadosDestreza(result.getInt("asignadoDestreza"));
            personaje.setPuntosAsignadosInteligencia(result.getInt("asignadoInteligencia"));

            while (j <= INDICE13) {
            	if (resultadoItemsID.getInt(i) != -1) {
            		stDatosItem.setInt(1, resultadoItemsID.getInt(i));
            		resultadoDatoItem = stDatosItem.executeQuery();
            		personaje.anadirItem(resultadoDatoItem.getInt("idItem"), resultadoDatoItem.getString("nombre"),
                            resultadoDatoItem.getInt("wereable"), resultadoDatoItem.getInt("bonusSalud"),
                            resultadoDatoItem.getInt("bonusEnergia"), resultadoDatoItem.getInt("bonusFuerza"),
                            resultadoDatoItem.getInt("bonusDestreza"), resultadoDatoItem.getInt("bonusInteligencia"),
                            resultadoDatoItem.getString("foto"), resultadoDatoItem.getString("fotoEquipado"));
            	}
            	i++;
            	j++;
            }

            // Devuelvo el paquete personaje con sus datos
            return personaje;

		} catch (SQLException ex) {
            Servidor.getLog()
                   .append("Fallo al intentar recuperar el personaje " + user.getUsername() + System.lineSeparator());
            Servidor.getLog().append(ex.getMessage() + System.lineSeparator());
		}

		return new PaquetePersonaje();
	}

	/**
	 * Obtiene usuario.
	 *
	 * @param usuario nombre del usuario
	 * @return Objeto de la clase PaqueteUsuario
	 */
	public PaqueteUsuario getUsuario(final String usuario) {
		ResultSet result = null;
		PreparedStatement st;

		try {
            st = connect.prepareStatement("SELECT * FROM registro WHERE usuario = ?");
            st.setString(1, usuario);
            result = st.executeQuery();

            String password = result.getString("password");
            int idPersonaje = result.getInt("idPersonaje");

            PaqueteUsuario paqueteUsuario = new PaqueteUsuario();
            paqueteUsuario.setUsername(usuario);
            paqueteUsuario.setPassword(password);
            paqueteUsuario.setIdPj(idPersonaje);

            return paqueteUsuario;
		} catch (SQLException e) {
            Servidor.getLog().append("Fallo al intentar recuperar el usuario " + usuario + System.lineSeparator());
            Servidor.getLog().append(e.getMessage() + System.lineSeparator());
		}

		return new PaqueteUsuario();
	}

	/**
	 * Actualizar inventario.
	 *
	 * @param paquetePersonaje Objeto de la clase PaquetePersonaje
	 */
	public void actualizarInventario(final PaquetePersonaje paquetePersonaje) {
		int i = 0;
		PreparedStatement stActualizarMochila;
		try {
            stActualizarMochila = connect.prepareStatement(
                 "UPDATE mochila SET item1=? ,item2=? ,item3=? ,item4=? ,item5=? ,item6=? ,item7=? ,item8=? ,item9=? "
                  + ",item10=? ,item11=? ,item12=? ,item13=? ,item14=? ,item15=? ,item16=? ,item17=? "
                  + ",item18=? ,item19=? ,item20=? WHERE idMochila=?");
            while (i < paquetePersonaje.getCantItems()) {
            	stActualizarMochila.setInt(i + 1, paquetePersonaje.getItemID(i));
            	i++;
            }
            for (int j = paquetePersonaje.getCantItems(); j < INDICE20; j++) {
            	stActualizarMochila.setInt(j + 1, -1);
            }
            stActualizarMochila.setInt(INDICE21, paquetePersonaje.getId());
            stActualizarMochila.executeUpdate();

		} catch (SQLException e) {
    Servidor.getLog().append("Fallo al intentar actualizar inventario " + System.lineSeparator());
		}
	}

	/**
	 * Actualizar inventario.
	 *
	 * @param idPersonaje entero que representa el id del personaje
	 */
	public void actualizarInventario(final int idPersonaje) {
        int i = 0;
        PaquetePersonaje paquetePersonaje = Servidor.getPersonajesConectados().get(idPersonaje);
		PreparedStatement stActualizarMochila;
		try {
            stActualizarMochila = connect.prepareStatement(
                 "UPDATE mochila SET item1=? ,item2=? ,item3=? ,item4=? ,item5=? ,item6=? ,item7=? ,item8=? ,item9=? "
                 + ",item10=? ,item11=? ,item12=? ,item13=? ,item14=? ,item15=? ,item16=? ,item17=? "
                 + ",item18=? ,item19=? ,item20=? WHERE idMochila=?");
            while (i < paquetePersonaje.getCantItems()) {
            	stActualizarMochila.setInt(i + 1, paquetePersonaje.getItemID(i));
            	i++;
            }
            if (paquetePersonaje.getCantItems() < INDICE9) {
            	int itemGanado = new Random().nextInt(INDICE29);
            	itemGanado += 1;
            	stActualizarMochila.setInt(paquetePersonaje.getCantItems() + 1, itemGanado);
            	for (int j = paquetePersonaje.getCantItems() + 2; j < INDICE20; j++) {
            		stActualizarMochila.setInt(j, -1);
            	}
            } else {
            	for (int j = paquetePersonaje.getCantItems() + 1; j < INDICE20; j++) {
            		stActualizarMochila.setInt(j, -1);
            	}
            }
            stActualizarMochila.setInt(INDICE21, paquetePersonaje.getId());
            stActualizarMochila.executeUpdate();

		} catch (SQLException e) {
            Servidor.getLog().append("Falló al intentar actualizar inventario de" + idPersonaje + "\n");
		}
	}

	/**
	 * Actualizar personaje subio nivel.
	 *
	 * @param paquetePersonaje Objeto de la clase PaquetePersonaje
	 */
	public void actualizarPersonajeSubioNivel(final PaquetePersonaje paquetePersonaje) {
		try {
            PreparedStatement stActualizarPersonaje = connect.prepareStatement(
                "UPDATE personaje SET fuerza=?, destreza=?, inteligencia=?, saludTope=?, energiaTope=?, "
                + "experiencia=?, nivel=?, puntosNoAsignados=?, asignadoFuerza=?, asignadoDestreza=?, "
                + "asignadoInteligencia=? WHERE idPersonaje=?");

            stActualizarPersonaje.setInt(INDICE1, paquetePersonaje.getFuerza());
            stActualizarPersonaje.setInt(INDICE2, paquetePersonaje.getDestreza());
            stActualizarPersonaje.setInt(INDICE3, paquetePersonaje.getInteligencia());
            stActualizarPersonaje.setInt(INDICE4, paquetePersonaje.getSaludTope());
            stActualizarPersonaje.setInt(INDICE5, paquetePersonaje.getEnergiaTope());
            stActualizarPersonaje.setInt(INDICE6, paquetePersonaje.getExperiencia());
            stActualizarPersonaje.setInt(INDICE7, paquetePersonaje.getNivel());
            stActualizarPersonaje.setInt(INDICE8, paquetePersonaje.getPuntosNoAsignados()); // Puntos
                                                                                    	// a
                                                                                    	// asignar
            stActualizarPersonaje.setInt(INDICE9, paquetePersonaje.getPuntosAsignadosFuerza()); // Puntos
                                                                                    		// asignados
                                                                                    		// a
                                                                                    		// fuerza
            stActualizarPersonaje.setInt(INDICE10, paquetePersonaje.getPuntosAsignadosDestreza()); // Puntos
                                                                                                // asignados
                                                                                                // a
                                                                                                // destreza
            stActualizarPersonaje.setInt(INDICE11, paquetePersonaje.getPuntosAsignadosInteligencia()); // Puntos
                                                                                                	// asignados
                                                                                                	// a
                                                                                                	// inteligencia
            stActualizarPersonaje.setInt(INDICE12, paquetePersonaje.getId());
            stActualizarPersonaje.executeUpdate();

            Servidor.getLog().append("El personaje " + paquetePersonaje.getNombre() + " se ha actualizado con éxito."
            		+ System.lineSeparator());
		} catch (SQLException e) {
            Servidor.getLog().append("Fallo al intentar actualizar el personaje " + paquetePersonaje.getNombre()
            		+ System.lineSeparator());
		}
	}
}
