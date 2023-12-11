package com.i2i.linkedinproject_backend.repos;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.i2i.linkedinproject_backend.models.GameSession;

/*
 * Description: this interface extends the MongoRepository interface which exposes various methods for accessing and manipulating
 * data (using CRUD operations) in the "sessions" collection of the current MongoDB database instance. It also includes two custom
 * methods for retriving a GameSession document from the database by its sessionID and for querying whether or not a certain doument
 * exists by its sessionID.
 * 
 * Utilizing flapdoodle's embedded mongoDB database @see https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo
 */
@Repository
public interface GameSessionRepository extends MongoRepository<GameSession, ObjectId>{
	Optional<GameSession> findBySessionID(String sessionID);
	boolean existsBySessionID(String sessionID);
}
