package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher; // the real fetcher (like DogApiBreedFetcher)
    private final Map<String, List<String>> cache = new HashMap<>(); // store results here
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // return statement included so that the starter code can compile and run.
        // If we already cached the breed, return the cached value
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        try {
            // Increment callsMade BEFORE calling the fetcher
            callsMade++;

            List<String> subBreeds = fetcher.getSubBreeds(breed); // may throw exception
            cache.put(breed, subBreeds); // only cache if it succeeds
            return subBreeds;

        } catch (BreedNotFoundException e) {
            // Don't cache failures
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}