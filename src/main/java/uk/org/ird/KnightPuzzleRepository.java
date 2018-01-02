package uk.org.ird;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.math.BigInteger;

public interface KnightPuzzleRepository extends MongoRepository<KnightPuzzle, BigInteger> {
    KnightPuzzle findByToken(BigInteger token);
}
