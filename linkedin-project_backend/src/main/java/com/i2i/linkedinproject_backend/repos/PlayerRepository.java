package com.i2i.linkedinproject_backend.repos;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.i2i.linkedinproject_backend.models.Player;

/*
 * Description: this interface extends the MongoRepository interface which exposes various methods for accessing and manipulating
 * data (using CRUD operations) in the "players" collection of the current MongoDB database instance. It also includes two custom
 * methods for retriving a Player document from the database by its username and for querying whether or not a certain doument 
 * exists by its username.
 *  
 *  Utilizing flapdoodle's embedded mongoDB database @see https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo
 */
@Repository
public interface PlayerRepository extends MongoRepository<Player, ObjectId>{
	Optional<Player> findByUsername(String username);
	boolean existsByUsername(String username);
}
