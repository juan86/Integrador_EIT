package org.educacionIt.dao.imp;

import org.educacionIt.dao.ConectionMySQLDB;
import org.educacionIt.dao.DAO;
import org.educacionIt.model.domain.GenderRelation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenreRelationDao implements ConectionMySQLDB, DAO<GenderRelation, Integer> {

    @Override
    public GenderRelation searchById(Integer key) {
        List<GenderRelation> genderRelations = getByProperty("id", new GenderRelation(key, 0, 0));
        return (!genderRelations.isEmpty()) ? genderRelations.get(0) : null;
    }

    @Override
    public List<GenderRelation> getByProperty(String propertyName, GenderRelation entity) {
        List<GenderRelation> genderRelations = new ArrayList<>();
        Map<String, String> columnMapping = Map.of(
                "id", "id_movie_genres = "+entity.getId(),
                "idmovie", "idmovie_movie_genres = "+entity.getIdMovie(),
                "idGender", "idgenre_movie_genres = "+entity.getIdGender()
        );

        String columnFilter = columnMapping.get(propertyName);
        if(columnFilter != null) {
            String sentenceSQL = "SELECT id_movie_genres, idmovie_movie_genres, idgenre_movie_genres FROM movie_genres WHERE "+columnFilter;
            try (Connection connection = getConexion();
                 PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL)) {
                try (ResultSet result = objectSentenceSQL.executeQuery()) {
                    while (result.next()) {
                        genderRelations.add(new GenderRelation(result.getInt("id_movie_genres"), result.getInt("idmovie_movie_genres"), result.getInt("idgenre_movie_genres")));
                    }
                }
            }catch (SQLException e) {
                throw new RuntimeException("Error al obtener las relaciones: ", e);
            }
        }
        return genderRelations;
    }

    @Override
    public List<GenderRelation> getAll() {
        List<GenderRelation> genderRelations = new ArrayList<>();
        String sentenceSQL = "SELECT id_movie_genres, idmovie_movie_genres, idgenre_movie_genres FROM movie_genres";

        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);){

            ResultSet result = objectSentenceSQL.executeQuery();
            while(result.next()){
                genderRelations.add(new GenderRelation(result.getInt("id_movie_genres"), result.getInt("idmovie_movie_genres"), result.getInt("idgenre_movie_genres")));
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return genderRelations;
    }

    @Override
    public int insert(GenderRelation entity) {
        int idGenerado = 0;
        String sentenceSQL = "INSERT INTO movie_genres (idmovie_movie_genres, idgenre_movie_genres) VALUES (?, ?)";
        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL, Statement.RETURN_GENERATED_KEYS)){

            objectSentenceSQL.setInt(1, entity.getIdMovie());
            objectSentenceSQL.setInt(2, entity.getIdGender());
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
    public void update(GenderRelation entity) {
        String sentenceSQL = "UPDATE movie_genres SET idmovie_movie_genres = ?, idgenre_movie_genres = ? WHERE id_movie_genres = ?";
        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);) {

            objectSentenceSQL.setInt(1, entity.getIdMovie());
            objectSentenceSQL.setInt(2, entity.getIdGender());
            objectSentenceSQL.setInt(3, entity.getId());
            int rowsUpdated = objectSentenceSQL.executeUpdate();

            if (rowsUpdated != 1) {
                throw new RuntimeException("Could not update Genre Relation with ID: " + entity.getId());
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(GenderRelation entity) {
        String sentenceSQL = "DELETE FROM movie_genres WHERE id_movie_genres = ?";
        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);){

            objectSentenceSQL.setInt(1, entity.getId());
            objectSentenceSQL.executeUpdate();
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

