package org.loose.fis.project.proofing.tool.github.client.secrets;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.goterl.lazycode.lazysodium.LazySodiumJava;
import com.goterl.lazycode.lazysodium.SodiumJava;
import com.goterl.lazycode.lazysodium.exceptions.SodiumException;
import com.goterl.lazycode.lazysodium.utils.Base64MessageEncoder;
import com.goterl.lazycode.lazysodium.utils.Key;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.loose.fis.project.proofing.tool.github.client.GithubClient;
import org.loose.fis.project.proofing.tool.github.client.http.GithubHttpClient;
import org.loose.fis.project.proofing.tool.github.client.model.BasicAuthCredentials;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GithubSecretsService {

    private Map<String, String> map;
    private GithubHttpClient githubHttpClient = new GithubHttpClient();

    private BasicAuthCredentials credentials;


    public GithubSecretsService(String owner, String repo, BasicAuthCredentials credentials) {
        this.credentials = credentials;

        map = new HashMap<>();
        map.put("owner", owner);
        map.put("repo", repo);
    }

    @SneakyThrows
    public List<Secret> listAllSecretsForRepo() {


        String apiPath = GithubClient.getApiPath(map, "repos", ":owner", ":repo", "actions", "secrets");

        HttpResponse httpResponse = githubHttpClient.get(new GenericUrl(apiPath), credentials);

        SecretsForRepo secretsForRepo = httpResponse.parseAs(SecretsForRepo.class);

        return secretsForRepo.getSecrets();
    }

    @SneakyThrows
    public SecretsPublicKey getPublicKey() {

        String apiPath = GithubClient.getApiPath(map, "repos", ":owner", ":repo", "actions", "secrets", "public-key");

        HttpResponse httpResponse = githubHttpClient.get(new GenericUrl(apiPath), credentials);

        return httpResponse.parseAs(SecretsPublicKey.class);
    }

    @SneakyThrows
    public boolean createSecret(String name, String value) {
        Map newMap = new HashMap(map);
        newMap.put("name", name);

        String apiPath = GithubClient.getApiPath(newMap, "repos", ":owner", ":repo", "actions", "secrets", ":name");

        SecretRequestBody body = createNewSecret(value);

        HttpResponse httpResponse = githubHttpClient.put(new GenericUrl(apiPath), credentials, body);

        int statusCode = httpResponse.getStatusCode();
        return statusCode == 201 || statusCode == 204;
    }

    private SecretRequestBody createNewSecret(String value) {
        SecretsPublicKey publicKey = getPublicKey();

        LazySodiumJava lazySodiumJava = new LazySodiumJava(new SodiumJava(), new Base64MessageEncoder());

        String encryptedSecret;
        try {
            encryptedSecret = lazySodiumJava.cryptoBoxSealEasy(value, Key.fromBase64String(publicKey.getKey()));
        } catch (SodiumException e) {
            log.error("Secret encryption failed!", e);
            return null;
        }

        return new SecretRequestBody(encryptedSecret, publicKey.getKey_id());

    }

}
