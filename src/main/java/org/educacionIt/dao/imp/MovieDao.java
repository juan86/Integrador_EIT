package org.educacionIt.dao.imp;

import org.educacionIt.dao.ConectionMySQLDB;
import org.educacionIt.dao.DAO;
import org.educacionIt.model.domain.GenderRelation;
import org.educacionIt.model.domain.Movie;
import org.educacionIt.model.domain.MovieGenre;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDao implements ConectionMySQLDB, DAO<Movie, Integer> {
    @Override
    public Movie searchById(Integer key) {

        List<Movie> movies = getByProperty("id", new Movie(key, "","",  null));

        return (!movies.isEmpty()) ? movies.get(0) : null;
    }

    @Override
    public List<Movie> getByProperty(String propertyName, Movie movie) {
        List<Movie> movies = new ArrayList<>();

        Map<String, String> columnMapping = Map.of(
                "id", "id_movie = '"+movie.getCode()+"'",
                "title", "title_movie = '"+movie.getTitle()+"'",
                "url", "url_movie = '"+movie.getUrl()+"'"
        );

        String columnFilter = columnMapping.get(propertyName);

        if(columnFilter != null) {
            String sentenceSQL = "SELECT id_movie, title_movie, url_movie, image_movie FROM movies WHERE "+columnFilter;
            try (Connection connection = getConexion();
                 PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL)) {

                try (ResultSet result = objectSentenceSQL.executeQuery()) {
                    while(result.next()){
                        Movie newMovie = new Movie(result.getInt("id_movie"), result.getString("title_movie"), result.getString("url_movie"), null);
                        //Blob imagenBlob = result.getBlob("image_movie");
                        movies.add(newMovie);
                    }
                }

            }catch (SQLException e) {
                throw new RuntimeException("Error al obtener las película: ", e);
            }
        }

        if(!movies.isEmpty()){
            movies.forEach(movieFound -> {
                List<MovieGenre> genres = getMovieGenres(movieFound.getCode());
                genres.forEach(movieFound::addGenres);
            });
        }

        return movies;
    }

    @Override
    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sentenceSQL = "SELECT id_movie, title_movie, url_movie, image_movie FROM movies";

        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);) {

            ResultSet result = objectSentenceSQL.executeQuery();

            while(result.next()){
                Movie newMovie = new Movie(result.getInt("id_movie"), result.getString("title_movie"), result.getString("url_movie"), null);
                movies.add(newMovie);
            }

            if(!movies.isEmpty()){
                movies.forEach(movie -> {
                    List<MovieGenre> genres = getMovieGenres(movie.getCode());
                    genres.forEach(movie::addGenres);
                });
            }

        }catch (SQLException e) {
            throw new RuntimeException("Error al obtener las película: ", e);
        }

        return movies;
    }

    public List<Movie> getMovieForGenre(String genre){
        List<Movie> movies = new ArrayList<>();
        String sentenceSQL = "SELECT id_movie, title_movie, url_movie, image_movie " +
                "FROM genres " +
                "INNER JOIN movie_genres ON id_genre = idgenre_movie_genres " +
                "INNER JOIN movies ON id_movie = idmovie_movie_genres " +
                "WHERE name_genre = ? OR name_genre_es = ?";
        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);) {

            objectSentenceSQL.setString(1, genre.toUpperCase());
            objectSentenceSQL.setString(2, genre.toUpperCase());

            try (ResultSet result = objectSentenceSQL.executeQuery()) {
                while (result.next()) {
                    Movie newMovie = new Movie(result.getInt("id_movie"), result.getString("title_movie"), result.getString("url_movie"), null);
                    movies.add(newMovie);
                    //movies.add(new Movie(, , , result.getString("image_movie")));
                }
            }

            if(!movies.isEmpty()){
                movies.forEach(movieFound -> {
                    List<MovieGenre> genres = getMovieGenres(movieFound.getCode());
                    genres.forEach(movieFound::addGenres);
                });
            }

        }catch (SQLException e) {
            throw new RuntimeException("Error al obtener los géneros de la película", e);
        }
        return movies;
    }

    @Override
    public int insert(Movie entity) {
        int idMovieGenerated = 0;

        String sentenceSQL = "INSERT INTO movies (title_movie, url_movie, image_movie) VALUES (?, ?, ?)";

        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL, Statement.RETURN_GENERATED_KEYS)){

            FileInputStream fileInputStream = null;
            if (entity.getImage() != null) {
                fileInputStream = new FileInputStream(entity.getImage());
            }

            objectSentenceSQL.setString(1, entity.getTitle());
            objectSentenceSQL.setString(2, entity.getUrl());
            if (fileInputStream != null) {
                objectSentenceSQL.setBlob(3, fileInputStream);
            } else {
                objectSentenceSQL.setNull(3, java.sql.Types.BLOB);
            }

            int result = objectSentenceSQL.executeUpdate();

            if(result != 1){
                throw new RuntimeException("Could not insert the movie: "+entity.getTitle());
            }

            ResultSet generatedKeys = objectSentenceSQL.getGeneratedKeys();

            if (generatedKeys.next()) {
                idMovieGenerated = generatedKeys.getInt(1);
                System.out.println("Generate Key: " + idMovieGenerated);

                GenreRelationDao genreRelationDao = new GenreRelationDao();

                final int finalIdMovieGenerated = idMovieGenerated;
                entity.getGenres().forEach(genre -> {
                    genreRelationDao.insert(new GenderRelation(finalIdMovieGenerated, genre.getId()));
                });

            }

        }catch(SQLException | FileNotFoundException e){
            throw new RuntimeException(e);
        }
        return idMovieGenerated;
    }

    @Override
    public void update(Movie entity) {
        String sentenceSQL = "UPDATE movies SET title_movie = ?, url_movie = ?, image_movie = ? WHERE id_movie = ?";
        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);){

            FileInputStream fileInputStream = null;
            if (entity.getImage() != null) {
                fileInputStream = new FileInputStream(entity.getImage());
            }

            objectSentenceSQL.setString(1, entity.getTitle());
            objectSentenceSQL.setString(2, entity.getUrl());

            if (fileInputStream != null) {
                objectSentenceSQL.setBlob(3, fileInputStream);
            } else {
                objectSentenceSQL.setNull(3, java.sql.Types.BLOB);
            }

            objectSentenceSQL.setInt(4, entity.getCode());

            int resul = objectSentenceSQL.executeUpdate();

            if(resul != 1) throw new RuntimeException("Could not update movie");
            GenreRelationDao genreRelationDao = new GenreRelationDao();

            List<GenderRelation> genreRelations = genreRelationDao.getByProperty("idmovie", new GenderRelation(entity.getCode(), 0));

            // Primero se elimina los registros que se encuentra en la tabla de relaciones y despues se generan nuevamente
            // con las modificaciones
            genreRelations.forEach(genreRelationDao::delete);
            entity.getGenres().forEach(genre -> {
                genreRelationDao.insert(new GenderRelation(entity.getCode(), genre.getId()));
            });

        }catch(SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Movie entity) {
        String sentenceSQL = "DELETE FROM movies WHERE id_movie = ?";
        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);){

            objectSentenceSQL.setInt(1, entity.getCode());
            int result = objectSentenceSQL.executeUpdate();

            if(result != 1){
                throw new RuntimeException("Could not delete the movie: "+entity.getTitle());
            }

        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MovieGenre> getMovieGenres(Integer key) {
        String sentenceSQL = "SELECT genres.id_genre, name_genre, name_genre_es " +
                "FROM movie_genres " +
                "INNER JOIN genres ON movie_genres.idgenre_movie_genres = genres.id_genre " +
                "WHERE movie_genres.idmovie_movie_genres = ?";
        List<MovieGenre> listGenre = new ArrayList<>();

        try (Connection connection = getConexion();
             PreparedStatement objectSentenceSQL = connection.prepareStatement(sentenceSQL);) {
            objectSentenceSQL.setInt(1, key);
            try (ResultSet result = objectSentenceSQL.executeQuery()) {
                while (result.next()) {
                    int genreId = result.getInt("id_genre");
                    String name = result.getString("name_genre");
                    String nameEs = result.getString("name_genre_es");
                    listGenre.add(new MovieGenre(genreId, name, nameEs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los géneros de la película", e);
        }
        return listGenre;
    }
}
