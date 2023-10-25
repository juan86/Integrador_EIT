package org.educacionIt.dao.imp;

import org.educacionIt.dao.ConectionMySQLDB;
import org.educacionIt.dao.DAO;
import org.educacionIt.model.domain.MovieGenre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenreDao implements ConectionMySQLDB, DAO<MovieGenre, Integer> {
    @Override
    public MovieGenre searchById(Integer key) {
        List<MovieGenre> genres = getByProperty("id", new MovieGenre(key, "", ""));
        return (!genres.isEmpty()) ? genres.get(0) : null;
    }

    @Override
    public List<MovieGenre> getByProperty(String propertyName, MovieGenre entity) {
        List<MovieGenre> matchingGenres = new ArrayList<>();

        Map<String, String> columnMapping = Map.of(
                "id", "id_genre = "+entity.getId(),
                "genre", "name_genre = "+entity.getName(),
                "genre_es", "name_genre_es = "+entity.getNameEs()
        );

        String columnFilter = columnMapping.get(propertyName);

        if(columnFilter != null) {
            String sentenceSQL = "SELECT id_genre, name_genre, name_genre_es FROM genres WHERE "+columnFilter;
            try (Connection connection = getConexion();
                 PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL)) {

                try (ResultSet result = objectSentenceSQL.executeQuery()) {
                    while(result.next()){
                        matchingGenres.add(new MovieGenre(result.getInt("id_genre"), result.getString("name_genre"), result.getString("name_genre_es")));
                    }
                }

            }catch (SQLException e) {
                throw new RuntimeException("Error al obtener las película: ", e);
            }
        }
        return matchingGenres;
    }

    @Override
    public List<MovieGenre> getAll() {
        List<MovieGenre> listGenre = new ArrayList<>();
        String sentenceSQL = "SELECT * FROM genres";

        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);){

            ResultSet result = objectSentenceSQL.executeQuery();
            while(result.next()){
                listGenre.add(new MovieGenre(result.getInt("id_genre"), result.getString("name_genre"), result.getString("name_genre_es")));
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return listGenre;
    }

    @Override
    public int insert(MovieGenre entity) {
        int idGenerado = 0;
        String sentenceSQL = "INSERT INTO genres (name_genre, name_genre_es) VALUES (?, ?)";

        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL, Statement.RETURN_GENERATED_KEYS)){

            objectSentenceSQL.setString(1, entity.getName());
            objectSentenceSQL.setString(2, entity.getNameEs());
            int result = objectSentenceSQL.executeUpdate();

            if(result == 1){
                ResultSet generatedKeys = objectSentenceSQL.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idGenerado = generatedKeys.getInt(1);
                    System.out.println("Generate Key: " + idGenerado);
                }
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return idGenerado;
    }

    @Override
    public void update(MovieGenre entity) {
        String sentenceSQL = "UPDATE genres SET name_genre = ?, name_genre_es = ? WHERE id_genre = ?";

        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);) {
            objectSentenceSQL.setString(1, entity.getName());
            objectSentenceSQL.setString(2, entity.getNameEs());
            objectSentenceSQL.setInt(3, entity.getId());

            int rowsUpdated = objectSentenceSQL.executeUpdate();
            if (rowsUpdated != 1) {
                throw new RuntimeException("Could not update gender with ID: " + entity.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(MovieGenre entity) {
        String sentenceSQL = "DELETE FROM genres WHERE id_genre = ?";
        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);){
            objectSentenceSQL.setInt(1, entity.getId());
            objectSentenceSQL.executeUpdate();
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

