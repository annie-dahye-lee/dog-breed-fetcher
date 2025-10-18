package dogapi;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CachingBreedFetcher implements BreedFetcher {

    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = breed.toLowerCase(Locale.ROOT);

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        callsMade++;
        List<String> result = this.fetcher.getSubBreeds(breed);

        cache.put(key, List.copyOf(result));

        return result;
    }

    public int getCallsMade() {
        return callsMade;
    }
}
