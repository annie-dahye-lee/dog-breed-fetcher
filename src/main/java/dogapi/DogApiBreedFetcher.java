package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * All failures are reported as BreedNotFoundException to align with
 * the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {

    private static final String API_URL = "https://dog.ceo/api/breed/";

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String url = API_URL + breed.toLowerCase() + "/list";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new BreedNotFoundException(breed);
            }

            String body = response.body().string();
            JSONObject json = new JSONObject(body);

            String status = json.optString("status");
            if (!"success".equalsIgnoreCase(status)) {
                throw new BreedNotFoundException(breed);
            }

            JSONArray arr = json.getJSONArray("message");
            List<String> result = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                result.add(arr.getString(i));
            }

            return result;

        } catch (IOException e) {
            throw new BreedNotFoundException(breed);
        }
    }
}
